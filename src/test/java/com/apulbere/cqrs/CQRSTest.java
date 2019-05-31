package com.apulbere.cqrs;

import com.apulbere.cqrs.command.AddItem;
import com.apulbere.cqrs.command.CreateOrder;
import com.apulbere.cqrs.command.ShipOrder;
import com.apulbere.cqrs.model.Order;
import com.apulbere.cqrs.model.OrderCommand;
import com.apulbere.cqrs.model.OrderStatus;
import com.apulbere.cqrs.repository.OrderCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.apulbere.cqrs.model.OrderCommand.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CQRSTest {

    private CommandInvoker<OrderCommand, Order> invoker = new CommandInvoker<>(Map.of(
            CREATE,     new CreateOrder(),
            ADD_ITEM,   new AddItem(),
            SHIP,       new ShipOrder()
    ));

    private OrderSnapshooter orderSnapshooter;
    private CommandHandler<OrderCommand> commandHandler;
    private CommandDataRepository<OrderCommand> cmdDataRepo;

    private UUID orderId;

    @BeforeEach
    void initEach() {
        var shortLivingCmdDataRepo = new OrderCommandRepository();
        orderSnapshooter = new OrderSnapshooter(shortLivingCmdDataRepo, invoker, 2);

        cmdDataRepo = new OrderCommandRepository();

        commandHandler = new CommandHandler<>(List.of(orderSnapshooter::snapshot, cmdDataRepo::save));

        orderId = UUID.randomUUID();
    }

    @Test
    void orderFulfilled() {
        commandHandler.handle(new CommandData<>(CREATE, orderId));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "dog"));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "dog2"));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "dog3"));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "dog4"));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "cat"));
        commandHandler.handle(new CommandData<>(SHIP, orderId, "str. Here and Now"));

        Order order = orderSnapshooter.fetch(orderId);
        Order expectedOrder = new Order(OrderStatus.SHIPPED, "str. Here and Now", List.of("dog", "dog2", "dog3", "dog4", "cat"));

        assertEquals(expectedOrder, order);
        assertEquals(7, cmdDataRepo.findAll(orderId).size());
    }


}

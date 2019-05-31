package com.apulbere.cqrs;

import static com.apulbere.cqrs.model.OrderCommand.ADD_ITEM;
import static com.apulbere.cqrs.model.OrderCommand.CREATE;
import static com.apulbere.cqrs.model.OrderCommand.SHIP;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apulbere.cqrs.model.Order;
import com.apulbere.cqrs.model.OrderCommand;
import com.apulbere.cqrs.model.OrderStatus;
import com.apulbere.cqrs.repository.OrderCommandRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CQRSTest extends CQRSBaseTest {

    private OrderSnapshooter orderSnapshooter;
    private CommandHandler<OrderCommand> commandHandler;
    private CommandDataRepository<OrderCommand> cmdDataRepo;

    private UUID orderId;

    @BeforeEach
    void initEach() {
        var invoker = new CommandInvoker<>(commands());
        var shortLivingCmdDataRepo = new OrderCommandRepository();
        orderSnapshooter = new OrderSnapshooter(shortLivingCmdDataRepo, invoker, 2);

        cmdDataRepo = new OrderCommandRepository();

        commandHandler = new CommandHandler<>(List.of(orderSnapshooter::snapshot, cmdDataRepo::save));

        orderId = UUID.randomUUID();
    }

    @Test
    void orderFulfilled() {
        commandHandler.handle(new CommandData<>(CREATE, orderId));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "beer"));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "potatoes"));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "cheese"));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "water"));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "chocolate"));
        commandHandler.handle(new CommandData<>(SHIP, orderId, "Circus St, 981"));

        Order order = orderSnapshooter.fetch(orderId);
        Order expectedOrder = new Order(OrderStatus.SHIPPED,
                "Circus St, 981",
                List.of("beer", "potatoes", "cheese", "water", "chocolate")
        );

        assertEquals(expectedOrder, order);
        assertEquals(7, cmdDataRepo.findAll(orderId).size());
    }
}

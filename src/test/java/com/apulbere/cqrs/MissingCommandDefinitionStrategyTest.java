package com.apulbere.cqrs;

import static com.apulbere.cqrs.model.OrderCommand.ADD_ITEM;
import static com.apulbere.cqrs.model.OrderCommand.CREATE;
import static com.apulbere.cqrs.model.OrderCommand.REMOVE_ITEM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.apulbere.cqrs.model.Order;
import com.apulbere.cqrs.model.OrderCommand;
import com.apulbere.cqrs.model.OrderStatus;
import com.apulbere.cqrs.repository.OrderCommandRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MissingCommandDefinitionStrategyTest extends CQRSBaseTest {

    private OrderSnapshooter orderSnapshooter;
    private CommandHandler<OrderCommand> commandHandler;

    private UUID orderId;

    void init(MissingCommandDefinitionStrategy<OrderCommand, Order> missingCommandDefinitionStrategy) {
        var invoker = new CommandInvoker<>(missingCommandDefinitionStrategy, commands());
        var shortLivingCmdDataRepo = new OrderCommandRepository();
        orderSnapshooter = new OrderSnapshooter(shortLivingCmdDataRepo, invoker, 2);

        commandHandler = new CommandHandler<>(List.of(orderSnapshooter::snapshot));

        orderId = UUID.randomUUID();
    }

    @Test
    @DisplayName("fail when an unknown command is processed")
    void failForUnknownCommand() {
        init(MissingCommandDefinitionStrategy.fail());

        var error = assertThrows(CommandFailedException.class, () -> {
            commandHandler.handle(new CommandData<>(REMOVE_ITEM, orderId, "beer"));
            orderSnapshooter.fetch(orderId);
        });

        assertEquals("no definition provided for [" + REMOVE_ITEM + "] command", error.getMessage());
    }

    @Test
    @DisplayName("ignore an unknown command")
    void ignoreUnknownCommand() {
        init(MissingCommandDefinitionStrategy.noOp());

        commandHandler.handle(new CommandData<>(CREATE, orderId));
        commandHandler.handle(new CommandData<>(ADD_ITEM, orderId, "beer"));
        commandHandler.handle(new CommandData<>(REMOVE_ITEM, orderId, "beer"));

        var expectedOrder = new Order(OrderStatus.DRAFT, null, List.of("beer"));
        assertEquals(expectedOrder, orderSnapshooter.fetch(orderId));
    }
}

package com.apulbere.cqrs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.apulbere.cqrs.command.AddItem;
import com.apulbere.cqrs.command.AddShipment;
import com.apulbere.cqrs.command.CreateOrder;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CQRSTest {

    private OrderSnapshotRepository repository;
    private CommandHandler<UUID, Order> cmdHandler;
    private UUID orderId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        repository = new OrderSnapshotRepository(2);
        cmdHandler = new CommandHandler<>(repository);
        orderId = UUID.randomUUID();
    }

    @Test
    void orderFulfilled() {
        cmdHandler.handle(orderId, new CreateOrder());
        cmdHandler.handle(orderId, new AddItem("dog"));
        cmdHandler.handle(orderId, new AddItem("dog2"));
        cmdHandler.handle(orderId, new AddItem("dog3"));
        cmdHandler.handle(orderId, new AddItem("dog4"));
        cmdHandler.handle(orderId, new AddItem("cat"));
        cmdHandler.handle(orderId, new AddShipment("str. Here and Now"));

        Order order = repository.fetch(orderId);
        Order expectedOrder = new Order(OrderStatus.SHIPPED, "str. Here and Now", List.of("dog", "dog2", "dog3", "dog4", "cat"));

        assertEquals(expectedOrder, order);
    }

    @Test
    void invalidCreateOrderCmd() {
        cmdHandler.handle(orderId, new CreateOrder());
        var error = assertThrows(CommandFailedException.class, () -> cmdHandler.handle(orderId, new CreateOrder()));
        assertEquals("order already exists", error.getMessage());
    }

    @Test
    void invalidAddItemCmd() {
        cmdHandler.handle(orderId, new CreateOrder());
        cmdHandler.handle(orderId, new AddShipment("str. Here and Now"));
        var error = assertThrows(CommandFailedException.class, () -> cmdHandler.handle(orderId, new AddItem("cat")));
        assertEquals("cannot add item [cat] due to status of order [" + OrderStatus.SHIPPED + "]", error.getMessage());
    }

    @Test
    void invalidAddShipmentCmd() {
        cmdHandler.handle(orderId, new CreateOrder());
        cmdHandler.handle(orderId, new AddShipment("str. Here and Now"));
        var error = assertThrows(CommandFailedException.class, () -> cmdHandler.handle(orderId, new AddShipment("str. Here and Now")));
        assertEquals("order is already in status " + OrderStatus.SHIPPED, error.getMessage());
    }
}

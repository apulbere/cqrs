package com.apulbere.cqrs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apulbere.cqrs.command.AddItem;
import com.apulbere.cqrs.command.AddShipment;
import com.apulbere.cqrs.command.CreateOrder;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CQRSTest {

    private OrderSnapshotRepository repository = new OrderSnapshotRepository();

    @Test
    void orderFulfilled() {
        var cmdHandler = new CommandHandler<>(repository);

        var orderId = UUID.randomUUID();
        cmdHandler.handle(orderId, new CreateOrder());
        cmdHandler.handle(orderId, new AddItem("dog"));
        cmdHandler.handle(orderId, new AddItem("cat"));
        cmdHandler.handle(orderId, new AddShipment("str. Here and Now"));

        Order order = repository.fetch(orderId);
        Order expectedOrder = new Order(OrderStatus.SHIPPED, "str. Here and Now", List.of("dog", "cat"));

        assertEquals(expectedOrder, order);
    }

}

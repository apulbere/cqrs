package com.apulbere.cqrs.command;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.model.Order;
import com.apulbere.cqrs.model.OrderCommand;
import com.apulbere.cqrs.model.OrderStatus;
import java.io.Serializable;

public class ShipOrder implements Command<OrderCommand, Order> {

    @Override
    public Order execute(Serializable data, Order initObj) {
        return initObj.withStatus(OrderStatus.SHIPPED).withShipmentInfo((String) data);
    }

    @Override
    public OrderCommand getCommandName() {
        return OrderCommand.SHIP;
    }
}

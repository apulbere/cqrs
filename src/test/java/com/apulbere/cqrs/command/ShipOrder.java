package com.apulbere.cqrs.command;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;
import com.apulbere.cqrs.OrderStatus;

import java.io.Serializable;

public class ShipOrder implements Command<Order> {

    @Override
    public Order execute(Serializable data, Order initObj) {
        return initObj.withStatus(OrderStatus.SHIPPED).withShipmentInfo((String) data);
    }
}

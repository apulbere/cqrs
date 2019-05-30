package com.apulbere.cqrs.command;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;
import com.apulbere.cqrs.OrderStatus;

import java.io.Serializable;

public class CreateOrder implements Command<Order> {

    @Override
    public Order execute(Serializable data, Order initObj) {
        return new Order(OrderStatus.DRAFT);
    }
}

package com.apulbere.cqrs.command;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.model.Order;
import com.apulbere.cqrs.model.OrderCommand;
import com.apulbere.cqrs.model.OrderStatus;
import java.io.Serializable;

public class CreateOrder implements Command<OrderCommand, Order> {

    @Override
    public Order execute(Serializable data, Order initObj) {
        return new Order(OrderStatus.DRAFT);
    }

    @Override
    public OrderCommand getCommandName() {
        return OrderCommand.CREATE;
    }
}

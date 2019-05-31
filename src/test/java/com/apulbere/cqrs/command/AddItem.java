package com.apulbere.cqrs.command;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.model.Order;
import com.apulbere.cqrs.model.OrderCommand;
import java.io.Serializable;
import java.util.stream.Stream;

public class AddItem implements Command<OrderCommand, Order> {

    @Override
    public Order execute(Serializable data, Order initObj) {
        var items = Stream.concat(initObj.getItems().stream(), of((String) data)).collect(toList());
        return initObj.withItems(items);
    }

    @Override
    public OrderCommand getCommandName() {
        return OrderCommand.ADD_ITEM;
    }
}

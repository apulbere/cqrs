package com.apulbere.cqrs.command;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;

import java.io.Serializable;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

public class AddItem implements Command<Order> {

    @Override
    public Order execute(Serializable data, Order initObj) {
        var items = Stream.concat(initObj.getItems().stream(), of((String) data)).collect(toList());
        return initObj.withItems(items);
    }
}

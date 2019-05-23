package com.apulbere.cqrs.command;

import static java.util.stream.Collectors.toUnmodifiableList;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddItem implements Command<Order> {

    private String item;

    @Override
    public Function<Order, Order> execute() {
        return order -> {
            var items = Stream.concat(order.getItems().stream(), Stream.of(item)).collect(toUnmodifiableList());
            return order.withItems(items);
        };
    }
}

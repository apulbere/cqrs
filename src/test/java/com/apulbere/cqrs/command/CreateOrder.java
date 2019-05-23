package com.apulbere.cqrs.command;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;
import com.apulbere.cqrs.OrderStatus;
import java.util.function.Function;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateOrder implements Command<Order> {

    @Override
    public Function<Order, Order> execute() {
        return noOrder -> new Order(OrderStatus.DRAFT);
    }
}

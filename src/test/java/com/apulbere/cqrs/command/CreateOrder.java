package com.apulbere.cqrs.command;

import static com.apulbere.cqrs.ValidationResult.valid;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;
import com.apulbere.cqrs.OrderStatus;
import com.apulbere.cqrs.ValidationResult;
import java.util.function.Function;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateOrder implements Command<Order> {

    @Override
    public Function<Order, Order> execute() {
        return noOrder -> new Order(OrderStatus.DRAFT);
    }

    @Override
    public ValidationResult validateTargetState(Order target) {
        return target == null ? valid() : new ValidationResult("order already exists", false);
    }
}

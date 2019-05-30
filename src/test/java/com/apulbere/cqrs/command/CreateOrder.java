package com.apulbere.cqrs.command;

import static com.apulbere.cqrs.ValidationResult.valid;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;
import com.apulbere.cqrs.OrderStatus;
import com.apulbere.cqrs.Validatable;
import com.apulbere.cqrs.ValidationResult;
import java.util.function.Function;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateOrder implements Command<Order>, Validatable<Order> {

    @Override
    public Function<Order, Order> executable() {
        return noOrder -> new Order(OrderStatus.DRAFT);
    }

    @Override
    public ValidationResult validate(Order target) {
        return target == null ? valid() : new ValidationResult("order already exists", false);
    }
}

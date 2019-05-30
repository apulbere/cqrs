package com.apulbere.cqrs.command;

import static java.util.stream.Collectors.toUnmodifiableList;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;
import com.apulbere.cqrs.OrderStatus;
import com.apulbere.cqrs.Validatable;
import com.apulbere.cqrs.ValidationResult;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddItem implements Command<Order>, Validatable<Order> {

    private String item;

    @Override
    public Function<Order, Order> executable() {
        return order -> {
            var items = Stream.concat(order.getItems().stream(), Stream.of(item)).collect(toUnmodifiableList());
            return order.withItems(items);
        };
    }

    @Override
    public ValidationResult validate(Order target) {
        if(OrderStatus.DRAFT.equals(target.getStatus())) {
            return ValidationResult.valid();
        }
        return new ValidationResult("cannot add item [" + item + "] due to status of order [" + target.getStatus() + "]", false);
    }
}

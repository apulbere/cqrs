package com.apulbere.cqrs.command;

import com.apulbere.cqrs.Command;
import com.apulbere.cqrs.Order;
import com.apulbere.cqrs.OrderStatus;
import com.apulbere.cqrs.ValidationResult;
import java.util.function.Function;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddShipment implements Command<Order> {

    private String shipmentInfo;

    @Override
    public Function<Order, Order> execute() {
        return order -> order.withShipmentInfo(shipmentInfo).withStatus(OrderStatus.SHIPPED);
    }

    @Override
    public ValidationResult validateTargetState(Order target) {
        if(OrderStatus.DRAFT.equals(target.getStatus())) {
            return ValidationResult.valid();
        }
        return new ValidationResult("order is already in status " + OrderStatus.SHIPPED, false);
    }
}

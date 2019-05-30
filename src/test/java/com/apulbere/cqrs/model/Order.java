package com.apulbere.cqrs.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.List;

@Value
@Wither
@AllArgsConstructor
public class Order {
    private OrderStatus status;
    private String shipmentInfo;
    private List<String> items;

    public Order(OrderStatus status) {
        this(status, null, List.of());
    }
}

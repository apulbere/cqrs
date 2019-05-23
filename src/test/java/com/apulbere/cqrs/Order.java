package com.apulbere.cqrs;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Wither;

@Wither
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Order {
    private OrderStatus status;
    private String shipmentInfo;
    private List<String> items = List.of();

    public Order(OrderStatus status) {
        this.status = status;
    }
}

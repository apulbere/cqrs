package com.apulbere.cqrs;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import com.apulbere.cqrs.model.Order;
import com.apulbere.cqrs.model.OrderCommand;
import java.util.List;
import java.util.ServiceLoader;

public abstract class CQRSBaseTest {

    @SuppressWarnings("unchecked")
    protected List<Command<OrderCommand, Order>> commands() {
        return stream(ServiceLoader.load(Command.class).spliterator(), false)
                .map(c -> (Command<OrderCommand, Order>) c)
                .collect(toList());
    }
}

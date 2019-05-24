package com.apulbere.cqrs;

import java.util.function.Function;
import lombok.Value;

@Value
public class Event<T> {
    private Function<T, T> command;
    private boolean isSnapshotted;
}

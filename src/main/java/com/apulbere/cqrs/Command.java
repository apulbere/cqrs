package com.apulbere.cqrs;

import java.util.function.Function;

public interface Command<T> {

    Function<T, T> execute();
}

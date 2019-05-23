package com.apulbere.cqrs;

import java.util.function.Function;

@FunctionalInterface
public interface Command<T> {

    Function<T, T> execute();

    default ValidationResult validateTargetState(T target) {
        return ValidationResult.valid();
    }
}

package com.apulbere.cqrs;

@FunctionalInterface
public interface Validatable<T> {

    ValidationResult validate(T target);
}

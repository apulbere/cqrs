package com.apulbere.cqrs;

public class CommandFailedException extends IllegalStateException {

    CommandFailedException(String s) {
        super(s);
    }
}

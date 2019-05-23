package com.apulbere.cqrs;

public class CommandFailedException extends IllegalStateException {

    public CommandFailedException(String s) {
        super(s);
    }
}

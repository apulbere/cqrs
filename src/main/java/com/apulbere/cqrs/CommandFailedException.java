package com.apulbere.cqrs;

public class CommandFailedException extends RuntimeException {

    CommandFailedException(String s) {
        super(s);
    }
}

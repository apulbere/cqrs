package com.apulbere.cqrs;

import java.io.Serializable;

@FunctionalInterface
public interface Command<CMD_NAME, T> {

    /**
     *
     * @param data to be merged in <code>initObj</code>
     * @param initObj
     * @return <code>T</code> a merge between <code>data</code> and <code>initObj</code>
     */
    T execute(Serializable data, T initObj);

    default CMD_NAME getCommandName() {
        throw new UnsupportedOperationException();
    }
}

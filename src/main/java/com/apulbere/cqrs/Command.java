package com.apulbere.cqrs;

import java.io.Serializable;

public interface Command<CMD_NAME, T> {

    /**
     *
     * @param data to be merged in <code>initObj</code>
     * @param initObj
     * @return <code>T</code> a merge between <code>data</code> and <code>initObj</code>
     */
    T execute(Serializable data, T initObj);

    CMD_NAME getCommandName();
}

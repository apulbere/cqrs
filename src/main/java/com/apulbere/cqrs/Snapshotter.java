package com.apulbere.cqrs;

import java.io.Serializable;

public interface Snapshotter<CMD_NAME, TARGET> {

    /**
     *
     * @param id
     * @return <code>TARGET</code> with most recent commands applied to it
     */
    TARGET fetch(Serializable id);

    /**
     *
     * @param commandData
     * @return <code>CommandData</code> which is a copy of <code>commandData</code> with <code>frozen</code> set to true if a snapshot was made
     */
    CommandData<CMD_NAME> snapshot(CommandData<CMD_NAME> commandData);
}

package com.apulbere.cqrs;

import java.io.Serializable;

public interface Snapshooter<CMD_NAME, TARGET> {

    /**
     *
     * @param id
     * @return <code>TARGET</code> with most recent commands applied to it
     */
    TARGET fetch(Serializable id);

    void snapshot(CommandData<CMD_NAME> commandData);
}

package com.apulbere.cqrs;

import java.io.Serializable;
import java.util.List;

public interface CommandDataRepository<CMD_NAME> {

    void save(CommandData<CMD_NAME> commandData);

    /**
     * @param id
     * @return all commands starting from the end till a frozen (command which has a snapshot) is found
     */
    List<CommandData<CMD_NAME>> findAllAfterLastFrozen(Serializable id);
}

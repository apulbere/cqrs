package com.apulbere.cqrs;

import java.io.Serializable;
import java.util.List;

public interface CommandDataRepository<CMD_NAME> {

    void save(CommandData<CMD_NAME> commandData);

    List<CommandData<CMD_NAME>> delete(Serializable id);

    List<CommandData<CMD_NAME>> findAll(Serializable id);
}

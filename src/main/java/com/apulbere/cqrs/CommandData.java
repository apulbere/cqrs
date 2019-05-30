package com.apulbere.cqrs;

import lombok.Value;
import lombok.experimental.Wither;

import java.io.Serializable;

@Value
public class CommandData<CMD_NAME> {
    private CMD_NAME name;
    private Serializable targetId;
    private Serializable value;
    /**
     * marks a commandData which has a snapshot
     */
    @Wither
    private boolean frozen;

    private CommandData(CMD_NAME name, Serializable targetId, Serializable value, boolean frozen) {
        this.name = name;
        this.targetId = targetId;
        this.value = value;
        this.frozen = frozen;
    }

    public CommandData(CMD_NAME name, Serializable targetId, Serializable value) {
        this(name, targetId, value, false);
    }

    public CommandData(CMD_NAME name, Serializable targetId) {
        this(name, targetId, null, false);
    }
}

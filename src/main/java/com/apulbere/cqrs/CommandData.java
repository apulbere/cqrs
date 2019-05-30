package com.apulbere.cqrs;

import lombok.Value;

import java.io.Serializable;

@Value
public class CommandData<CMD_NAME> {
    private CMD_NAME name;
    private Serializable targetId;
    private Serializable value;

    public CommandData(CMD_NAME name, Serializable targetId, Serializable value) {
        this.name = name;
        this.targetId = targetId;
        this.value = value;
    }

    public CommandData(CMD_NAME name, Serializable targetId) {
        this(name, targetId, null);
    }
}

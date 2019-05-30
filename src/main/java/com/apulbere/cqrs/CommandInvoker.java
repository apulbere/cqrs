package com.apulbere.cqrs;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class CommandInvoker<CMD_NAME, TARGET> {

    private Map<CMD_NAME, Command<TARGET>> commandMap;

    public TARGET execute(CommandData<CMD_NAME> commandData, TARGET initObj) {
        var cmd = commandMap.get(commandData.getName());
        if(cmd == null) {
            throw new CommandFailedException("command [" + commandData.getName() + "] is not defined");
        }
        return cmd.execute(commandData.getValue(), initObj);
    }
}

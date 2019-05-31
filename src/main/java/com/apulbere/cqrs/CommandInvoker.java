package com.apulbere.cqrs;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

public class CommandInvoker<CMD_NAME, TARGET> {

    private Map<CMD_NAME, Command<CMD_NAME, TARGET>> commandMap;

    public CommandInvoker(List<Command<CMD_NAME, TARGET>> commands) {
        commandMap = commands.stream().collect(toMap(Command::getCommandName, identity()));
    }

    public TARGET execute(CommandData<CMD_NAME> commandData, TARGET initObj) {
        var cmd = commandMap.get(commandData.getName());
        if(cmd == null) {
            throw new CommandFailedException("command [" + commandData.getName() + "] is not defined");
        }
        return cmd.execute(commandData.getValue(), initObj);
    }
}

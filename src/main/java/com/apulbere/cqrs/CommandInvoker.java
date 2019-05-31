package com.apulbere.cqrs;

import static com.apulbere.cqrs.MissingCommandDefinitionStrategy.noOp;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

public class CommandInvoker<CMD_NAME, TARGET> {

    private MissingCommandDefinitionStrategy<CMD_NAME, TARGET> missingCommandDefinitionStrategy = noOp();
    private Map<CMD_NAME, Command<CMD_NAME, TARGET>> commandMap;

    public CommandInvoker(List<Command<CMD_NAME, TARGET>> commands) {
        commandMap = commands.stream().collect(toMap(Command::getCommandName, identity()));
    }

    public CommandInvoker(MissingCommandDefinitionStrategy<CMD_NAME, TARGET> missingCommandDefinitionStrategy,
            List<Command<CMD_NAME, TARGET>> commands) {
        this(commands);
        this.missingCommandDefinitionStrategy = missingCommandDefinitionStrategy;
    }

    public TARGET execute(CommandData<CMD_NAME> commandData, TARGET initObj) {
        var cmd = missingCommandDefinitionStrategy.delegate(commandData.getName(), commandMap::get);
        return cmd.execute(commandData.getValue(), initObj);
    }
}

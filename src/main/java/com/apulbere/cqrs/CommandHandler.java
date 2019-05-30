package com.apulbere.cqrs;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
public class CommandHandler<CMD_NAME> {

    private List<Consumer<CommandData<CMD_NAME>>> commandDataConsumers;

    public void handle(CommandData<CMD_NAME> commandData) {
        commandDataConsumers.forEach(c -> c.accept(commandData));
    }
}

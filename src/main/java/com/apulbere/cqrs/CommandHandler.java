package com.apulbere.cqrs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandHandler<CMD_NAME, TARGET> {

    private Snapshotter<CMD_NAME, TARGET> snapshotter;
    private CommandDataRepository<CMD_NAME> commandDataRepository;

    public void handle(CommandData<CMD_NAME> commandData) {
        var newCommand = snapshotter.snapshot(commandData);
        commandDataRepository.save(newCommand);
    }
}

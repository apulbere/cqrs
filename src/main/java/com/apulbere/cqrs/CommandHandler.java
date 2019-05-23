package com.apulbere.cqrs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandHandler<ID, T> {

    private Repository<ID, T> repository;

    public void handle(ID dataId, Command<T> command) {
        var validationResult = command.validateTargetState(repository.fetch(dataId));
        if(!validationResult.isValid()) {
            throw new CommandFailedException(validationResult.getMessage());
        }
        repository.persist(dataId, command.execute());
    }
}

package com.apulbere.cqrs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandHandler<ID, T> {

    private Repository<ID, T> repository;

    public <C extends Command<T> & Validatable<T>> void handle(ID dataId, C validatingCommand) {
        var validationResult = validatingCommand.validate(repository.fetch(dataId));
        if(!validationResult.isValid()) {
            throw new CommandFailedException(validationResult.getMessage());
        }
        repository.persist(dataId, validatingCommand.executable());
    }
}

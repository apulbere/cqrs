package com.apulbere.cqrs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandHandler<ID, T> {

    private Repository<ID, T> repository;

    public void handle(ID dataId, Command<T> command) {
        repository.persists(dataId, command.execute());
    }
}

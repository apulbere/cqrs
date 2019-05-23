package com.apulbere.cqrs;

import java.util.function.Function;

public interface Repository<ID, T> {

    void persists(ID dataId, Function<T, T> function);

    T fetch(ID id);
}

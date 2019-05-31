package com.apulbere.cqrs;

import java.util.function.Function;

@FunctionalInterface
public interface MissingCommandDefinitionStrategy<C, T> {

    Command<C, T> delegate(C commandName, Function<C, Command<C, T>> commandFunction);

    static <C, T> MissingCommandDefinitionStrategy<C, T> noOp() {
        return (cn, cf) -> (data, initObj) -> {
                var cmd = cf.apply(cn);
                if(cmd == null) {
                    return initObj;
                }
                return cmd.execute(data, initObj);
        };
    }

    static <C, T> MissingCommandDefinitionStrategy<C, T> fail() {
        return (cn, cf) -> (data, initObj) -> {
                var cmd = cf.apply(cn);
                if(cmd == null) {
                    throw new CommandFailedException("no definition provided for [" + cn + "] command");
                }
                return cmd.execute(data, initObj);
        };
    }
}

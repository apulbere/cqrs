package com.apulbere.cqrs.repository;

import com.apulbere.cqrs.CommandData;
import com.apulbere.cqrs.CommandDataRepository;
import com.apulbere.cqrs.model.OrderCommand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderCommandRepository implements CommandDataRepository<OrderCommand> {

    private Map<Serializable, List<CommandData<OrderCommand>>> data = new HashMap<>();

    @Override
    public void save(CommandData<OrderCommand> commandData) {
        data.computeIfAbsent(commandData.getTargetId(), k -> new ArrayList<>()).add(commandData);
    }

    @Override
    public List<CommandData<OrderCommand>> delete(Serializable id) {
        var cmds = data.remove(id);
        return cmds != null ? cmds : List.of();
    }

    @Override
    public List<CommandData<OrderCommand>> findAll(Serializable id) {
        return data.get(id);
    }

}

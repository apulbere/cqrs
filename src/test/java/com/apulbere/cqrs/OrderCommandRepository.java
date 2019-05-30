package com.apulbere.cqrs;

import java.io.Serializable;
import java.util.*;

public class OrderCommandRepository implements CommandDataRepository<OrderCommand> {

    private Map<Serializable, List<CommandData<OrderCommand>>> data = new HashMap<>();

    @Override
    public void save(CommandData<OrderCommand> commandData) {
        data.computeIfAbsent(commandData.getTargetId(), k -> new ArrayList<>()).add(commandData);
    }

    @Override
    public List<CommandData<OrderCommand>> findAllAfterLastFrozen(Serializable uuid) {
        var events = data.getOrDefault(uuid, List.of());
        var lst = events.listIterator(events.size());
        var queue = new LinkedList<CommandData<OrderCommand>>();
        while(lst.hasPrevious()) {
            var event = lst.previous();

            if(event.isFrozen()) break;

            queue.push(event);
        }
        return queue;
    }

}

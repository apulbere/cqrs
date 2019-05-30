package com.apulbere.cqrs;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OrderSnapshotter implements Snapshotter<OrderCommand, Order> {

    private final OrderCommandRepository orderCommandRepository;
    private Map<Serializable, List<Order>> data = new HashMap<>();
    private final CommandInvoker<OrderCommand, Order> invoker;
    private final int eventsSnapshotDistance;
    private int currentEventsSnapshotDistance = 0;

    @Override
    public Order fetch(Serializable uuid) {
        return createSnapshot(uuid);
    }

    @Override
    public CommandData<OrderCommand> snapshot(CommandData<OrderCommand> commandData) {
        CommandData<OrderCommand> event;
        if(currentEventsSnapshotDistance == eventsSnapshotDistance) {
            var oldSnap = createSnapshot(commandData.getTargetId());
            var snap = invoker.execute(commandData, oldSnap);
            data.computeIfAbsent(commandData.getTargetId(), k -> new ArrayList<>()).add(snap);
            currentEventsSnapshotDistance = -1;
            event = commandData.withFrozen(true);
        } else {
            event = commandData;
        }
        currentEventsSnapshotDistance++;
        return event;

    }

    private Order createSnapshot(Serializable id) {
        return orderCommandRepository.findAllAfterLastFrozen(id).stream()
                .reduce(fetchLastSnapshot(id), (order, event) -> invoker.execute(event, order), (a, b) -> { throw new UnsupportedOperationException(); });
    }

    private Order fetchLastSnapshot(Serializable id) {
        var list = data.getOrDefault(id, List.of());
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }
}

package com.apulbere.cqrs;

import com.apulbere.cqrs.model.Order;
import com.apulbere.cqrs.model.OrderCommand;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OrderSnapshotter implements Snapshotter<OrderCommand, Order> {

    private final CommandDataRepository<OrderCommand> orderCommandRepository;
    private final CommandInvoker<OrderCommand, Order> invoker;
    private final int eventsSnapshotDistance;
    private int currentEventsSnapshotDistance = 0;

    private Map<Serializable, List<Order>> data = new HashMap<>();

    @Override
    public Order fetch(Serializable uuid) {
        return createSnapshot(uuid);
    }

    @Override
    public void snapshot(CommandData<OrderCommand> commandData) {
        if(currentEventsSnapshotDistance == eventsSnapshotDistance) {
            var oldSnap = createSnapshot(commandData.getTargetId());
            var snap = invoker.execute(commandData, oldSnap);
            data.computeIfAbsent(commandData.getTargetId(), k -> new ArrayList<>()).add(snap);
            currentEventsSnapshotDistance = -1;
            orderCommandRepository.delete(commandData.getTargetId());
        } else {
            orderCommandRepository.save(commandData);
        }
        currentEventsSnapshotDistance++;
    }

    private Order createSnapshot(Serializable id) {
        return orderCommandRepository.findAll(id).stream()
                .reduce(fetchLastSnapshot(id), (order, event) -> invoker.execute(event, order), (a, b) -> { throw new UnsupportedOperationException(); });
    }

    private Order fetchLastSnapshot(Serializable id) {
        var list = data.getOrDefault(id, List.of());
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }
}

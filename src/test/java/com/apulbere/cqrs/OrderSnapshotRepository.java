package com.apulbere.cqrs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Function;

public class OrderSnapshotRepository implements Repository<UUID, Order> {
    private final Map<UUID, Queue<Function<Order, Order>>> events = new HashMap<>();
    private final Map<UUID, List<Order>> snapshots = new HashMap<>();
    private final int eventsSnapshotDistance;

    private int currentEventsSnapshotDistance;

    public OrderSnapshotRepository(int eventsSnapshotDistance) {
        this.eventsSnapshotDistance = eventsSnapshotDistance;
    }

    public OrderSnapshotRepository() {
        this(2);
    }

    @Override
    public void persists(UUID dataId, Function<Order, Order> function) {
        if(currentEventsSnapshotDistance == eventsSnapshotDistance) {
            snapshots.computeIfAbsent(dataId, k -> new LinkedList<>()).add(fetchLastSnapshot(dataId));
            currentEventsSnapshotDistance = 0;
        }
        events.computeIfAbsent(dataId, k -> new LinkedList<>()).add(function);
        currentEventsSnapshotDistance++;
    }

    @Override
    public Order fetch(UUID id) {
        var queue = events.get(id);
        var order = fetchLastSnapshot(id);
        while(!queue.isEmpty()) {
            order = queue.poll().apply(order);
        }
        return order;
    }

    private Order fetchLastSnapshot(UUID id) {
        var list = snapshots.getOrDefault(id, List.of());
        return list.isEmpty() ? null : list.get(snapshots.size() - 1);
    }
}
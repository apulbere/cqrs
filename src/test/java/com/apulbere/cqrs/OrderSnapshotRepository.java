package com.apulbere.cqrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class OrderSnapshotRepository implements Repository<UUID, Order> {
    private final Map<UUID, List<Event<Order>>> eventsMap = new HashMap<>();
    private final Map<UUID, List<Order>> snapshots = new HashMap<>();
    private final int eventsSnapshotDistance;

    private int currentEventsSnapshotDistance = 0;

    /**
     *
     * @param eventsSnapshotDistance - e.g. for value 2 will result in: event, event, event/snapshot, event, event, event/snapshot ...
     */
    public OrderSnapshotRepository(int eventsSnapshotDistance) {
        this.eventsSnapshotDistance = eventsSnapshotDistance;
    }

    @Override
    public void persist(UUID dataId, Function<Order, Order> function) {
        Event<Order> event;
        if(currentEventsSnapshotDistance == eventsSnapshotDistance) {
            var snap = function.apply(createSnapshot(dataId, eventsMap.getOrDefault(dataId, List.of())));
            snapshots.computeIfAbsent(dataId, k -> new ArrayList<>()).add(snap);
            currentEventsSnapshotDistance = -1;
            event = new Event<>(function, true);
        } else {
            event = new Event<>(function, false);
        }
        eventsMap.computeIfAbsent(dataId, k -> new ArrayList<>()).add(event);
        currentEventsSnapshotDistance++;
    }

    @Override
    public Order fetch(UUID id) {
        return createSnapshot(id, eventsMap.getOrDefault(id, List.of()));
    }

    private Order createSnapshot(UUID id, List<Event<Order>> events) {
        return findAllEventsAfterMostRecentSnapshot(events)
                .reduce(fetchLastSnapshot(id), (order, event) -> event.getCommand().apply(order), (a, b) -> { throw new UnsupportedOperationException(); });
    }

    private Stream<Event<Order>> findAllEventsAfterMostRecentSnapshot(List<Event<Order>> events) {
        var lst = events.listIterator(events.size());
        var queue = new LinkedList<Event<Order>>();
        while(lst.hasPrevious()) {
            var event = lst.previous();

            if(event.isSnapshotted()) break;

            queue.push(event);
        }
        return queue.stream();
    }

    private Order fetchLastSnapshot(UUID id) {
        var list = snapshots.getOrDefault(id, List.of());
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }
}
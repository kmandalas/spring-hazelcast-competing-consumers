package dev.kmandalas.producer.store;

import com.hazelcast.collection.QueueStore;
import dev.kmandalas.common.domain.Event;
import dev.kmandalas.common.domain.Status;
import dev.kmandalas.producer.entity.EventEntity;
import dev.kmandalas.producer.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PostgresQueueStore implements QueueStore<Event> {

    private final EventRepository eventRepository;

    public PostgresQueueStore(@Lazy EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void store(Long key, Event value) {
        log.info("store() called for {}" , value);
        EventEntity entity = new EventEntity(key,
                value.getRequestId().toString(),
                value.getStatus().name(),
                value.getDateSent(),
                LocalDateTime.now());
        eventRepository.save(entity);
    }

    @Override
    public void storeAll(Map<Long, Event> map) {
        log.info("storeAll() called for {} total keys" , map.keySet().size());
        Collection<EventEntity> eventEntities = map.entrySet().stream()
                .map(entry -> new EventEntity(entry.getKey(),
                        entry.getValue().getRequestId().toString(),
                        entry.getValue().getStatus().name(),
                        entry.getValue().getDateSent(),
                        LocalDateTime.now()))
                .toList();
        eventRepository.saveAll(eventEntities);
    }

    @Override
    public void delete(Long key) {
        log.info("delete() was called for {}", key);
        eventRepository.deleteById(key);
    }

    @Override
    public void deleteAll(Collection<Long> keys) {
        log.info("deleteAll() was called for {} total keys", keys.size());
        eventRepository.deleteAllById(keys);
    }

    @Override
    public Event load(Long key) {
        log.info("load() was called for {}", key);
        return eventRepository.findById(key)
                .map(e -> Event.builder()
                        .requestId(UUID.fromString(e.getRequestId()))
                        .status(Status.valueOf(e.getStatus()))
                        .build())
                .orElse(null);

    }

    @Override
    public Map<Long, Event> loadAll(Collection<Long> keys) {
        log.info("loadAll() was called for {} total keys", keys);
        List<EventEntity> entities = eventRepository.findAllById(keys);
        return entities.stream().collect(Collectors.toMap(EventEntity::getId,
                e -> Event.builder()
                        .requestId(UUID.fromString(e.getRequestId()))
                        .status(Status.valueOf(e.getStatus()))
                        .build()));
    }

    @Override
    public Set<Long> loadAllKeys() {
        log.info("loadAllKeys() was called...");
        List<EventEntity> events = eventRepository.findAll();
        return events.stream().map(EventEntity::getId).collect(Collectors.toSet());
    }

}
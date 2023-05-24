package dev.kmandalas.consumer.repository;

import dev.kmandalas.consumer.entity.EventProcessedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventProcessedRepository extends JpaRepository<EventProcessedEntity, Long> { }
package dev.kmandalas.producer.repository;

import dev.kmandalas.producer.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> { }
package dev.kmandalas.producer.controller;

import com.hazelcast.core.HazelcastInstance;
import dev.kmandalas.common.domain.Event;
import dev.kmandalas.common.domain.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

@RestController
public class ProducerController {

    @Value("${demo.queue.name}")
    private String queueName;

    private final HazelcastInstance hazelcastInstance;

    public ProducerController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    private BlockingQueue<Event> retrieveQueue() {
        return hazelcastInstance.getQueue(queueName);
    }

    @GetMapping("/")
    public Boolean send() {
        return retrieveQueue().offer(Event.builder()
                .requestId(UUID.randomUUID())
                .status(Status.APPROVED)
                .dateSent(LocalDateTime.now())
                .build());
    }

}

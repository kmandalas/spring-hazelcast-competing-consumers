package dev.kmandalas.consumer.service;

import com.hazelcast.collection.IQueue;
import dev.kmandalas.common.domain.Event;
import dev.kmandalas.consumer.entity.EventProcessedEntity;
import dev.kmandalas.consumer.repository.EventProcessedRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.LocalDateTime;

@Service
@Slf4j
public class DemoService {

    private final ServletWebServerApplicationContext webServerAppCtxt;
    private final EventProcessedRepository eventProcessedRepository;
    private final IQueue<Event> dlq;

    public DemoService(ServletWebServerApplicationContext webServerAppCtxt,
                       EventProcessedRepository eventProcessedRepository,
                       @Qualifier("dlq")IQueue<Event> dlq) {
        this.webServerAppCtxt = webServerAppCtxt;
        this.eventProcessedRepository = eventProcessedRepository;
        this.dlq = dlq;
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 2)
    @SneakyThrows
    // @Async: enable this if you want to delegate processing to a ThreadPoolTaskExecutor
    public void doWork(Event event) {
        // do the actual processing...
        EventProcessedEntity processed = new EventProcessedEntity();
        processed.setRequestId(event.getRequestId().toString());
        processed.setStatus(event.getStatus().name());
        processed.setDateSent(event.getDateSent());
        processed.setDateProcessed(LocalDateTime.now());
        processed.setProcessedBy(InetAddress.getLocalHost().getHostAddress() + ":" + webServerAppCtxt.getWebServer().getPort());
        eventProcessedRepository.save(processed);
    }

    @Recover
    void recover(Exception e, Event event) {
        log.error("Error processing {} ", event, e);
        var result = dlq.offer(event);
        log.info("Adding {} in DQL result {}", event, result);
    }

}

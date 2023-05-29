package dev.kmandalas.consumer.service;

import com.hazelcast.collection.IQueue;
import dev.kmandalas.common.domain.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Alternative approach using a Scheduled task to poll for messages
 */
@Component
@Slf4j
public class EventPoller {

    private final IQueue<Event> queue;
    private final DemoService demoService;

    public EventPoller(@Qualifier("eventQueue") IQueue<Event> queue, DemoService demoService) {
        this.queue = queue;
        this.demoService = demoService;
    }

    // @Scheduled(fixedRate = 5000)
    @SneakyThrows
    public void consume() {
        Event event = queue.poll();
        // process event
        if (event != null) {
            log.info("EventPoller::: Processing {} ", event);
            demoService.doWork(event);
        }
    }

}

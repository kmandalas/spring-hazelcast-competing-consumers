package dev.kmandalas.consumer.service;

import com.hazelcast.collection.IQueue;
import dev.kmandalas.common.domain.Event;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.util.NamedFeature;

@Component
@Slf4j
public class EventConsumer {

    @Value("${demo.virtual.threads}")
    private int numberOfThreads = 1;
    @Value("${demo.queue.name}")
    private String queueName;
    private final IQueue<Event> queue;
    private final DemoService demoService;
    private final FeatureManager manager;
    public static final Feature CONSUMER_ENABLED = new NamedFeature("CONSUMER_ENABLED");

    public EventConsumer(@Qualifier("eventQueue") IQueue<Event> queue, DemoService demoService,
                         FeatureManager manager) {
        this.queue = queue;
        this.demoService = demoService;
        this.manager = manager;
    }

    @PostConstruct
    public void init() {
        startConsuming();
    }

    public void startConsuming() {
        for (var i = 0; i < numberOfThreads; i++) {
            Thread.ofVirtual()
                    .name(queueName + "_" + "consumer-" + i)
                    .start(this::consumeMessages);
        }
    }

    private void consumeMessages() {
        while (manager.isActive(CONSUMER_ENABLED)) {
            try {
                Event event = queue.take(); // Will block until an event is available or interrupted
                // Process the event
                log.info("EventConsumer::: Processing {} ", event);
                demoService.doWork(event);
            } catch (InterruptedException e) {
                // Handle InterruptedException as per your application's requirements
                log.error("Encountered thread interruption: ", e);
                Thread.currentThread().interrupt();
            }
        }
    }

}

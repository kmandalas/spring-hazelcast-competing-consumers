package dev.kmandalas.consumer.service;

import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import dev.kmandalas.common.domain.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;


@Slf4j
// @Component
public class EventItemListener implements ItemListener<Event> {

    private final IQueue<Event> queue;
    private final DemoService demoService;

    public EventItemListener(@Qualifier("eventQueue") IQueue<Event> queue, DemoService demoService) {
        this.queue = queue;
        this.demoService = demoService;
        this.queue.addItemListener(this, true);
    }

    @SneakyThrows
    @Override
    public void itemAdded(ItemEvent<Event> item) {
        log.debug("Received itemAdded with eventType {}", item.getEventType());
        Event event = queue.take();
        // process event
        log.info("EventItemListener::: Processing {} ", event);
        demoService.doWork(event);
    }

    @Override
    public void itemRemoved(ItemEvent<Event> item) {
        log.debug("item with requestId {} removed", item.getItem().getRequestId());
    }

}

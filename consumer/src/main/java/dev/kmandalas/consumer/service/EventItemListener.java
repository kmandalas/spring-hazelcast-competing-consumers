package dev.kmandalas.consumer.service;

import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import dev.kmandalas.common.domain.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Alternative approach using {@link ItemListener} to be notified when an item is added to the queue.
 * see <a href="https://medium.com/wix-engineering/getting-started-with-hazelcast-iqueue-as-a-messaging-service-eb3fb3c14a02"/>
 * Keep in mind that this approach will not work if consumer is down: when its started again,
 * the "ADDED" {@link ItemEvent events} will not be sent.
 */
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

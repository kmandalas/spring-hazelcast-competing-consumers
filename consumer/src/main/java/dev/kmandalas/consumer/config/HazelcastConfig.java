package dev.kmandalas.consumer.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import dev.kmandalas.common.domain.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Value("${demo.queue.name}")
    private String queueName;

    @Value("${demo.dlq.name}")
    private String dlqName;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig config = new ClientConfig();
        // add more settings per use-case
        return HazelcastClient.newHazelcastClient(config);
    }

    @Bean
    public IQueue<Event> eventQueue(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getQueue(queueName);
    }

    @Bean
    public IQueue<Event> dlq(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getQueue(dlqName);
    }

}

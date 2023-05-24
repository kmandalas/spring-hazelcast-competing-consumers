package dev.kmandalas.producer.config;

import com.hazelcast.collection.QueueStore;
import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.Hazelcast;
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
    public HazelcastInstance hazelcastInstance(QueueStore<Event> queueStore) {
        Config config = new Config();

        QueueConfig queueConfig = config.getQueueConfig("main");
        queueConfig.setName(queueName)
                .setBackupCount(1)
                .setMaxSize(0)
                .setStatisticsEnabled(true);
        queueConfig.setQueueStoreConfig(new QueueStoreConfig()
                .setEnabled(true)
                .setStoreImplementation(queueStore)
                .setProperty("binary", "false"));

        QueueConfig dlqConfig = config.getQueueConfig("main-dlq");
        dlqConfig.setName(dlqName)
                .setBackupCount(1)
                .setMaxSize(0)
                .setStatisticsEnabled(true);

        config.addQueueConfig(queueConfig);
        config.addQueueConfig(dlqConfig);
        return Hazelcast.newHazelcastInstance(config);
    }

}

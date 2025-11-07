package com.example.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    // Create a topic with name "porting-topic", 3 partitions, 1 replica
    @Bean
    public NewTopic portingTopic() {
        return TopicBuilder.name("porting-topic")
                .partitions(2)
                .replicas(1)
                .build();
    }
    
    // Create a topic with name "portout-topic", 3 partitions, 1 replica
    @Bean
    public NewTopic portoutTopic() {
        return TopicBuilder.name("portout-topic")
                .build();
    }
}

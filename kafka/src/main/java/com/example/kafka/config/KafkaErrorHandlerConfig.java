package com.example.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

import com.example.kafka.consumer.PortingRequestConsumer;

@Configuration
public class KafkaErrorHandlerConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(PortingRequestConsumer.class);

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        // Retry 3 times with exponential backoff
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1000L);  // 1s
        backOff.setMultiplier(2.0);         // doubles each retry
        backOff.setMaxInterval(10000L);     // cap at 10s

        // Send to Dead Letter Topic after retries fail
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        // Optionally skip specific exceptions
        // errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        // Logging after retries exhausted
        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
        	logger.info("Retry attempt " + deliveryAttempt + " for record: " + record.value());
        });

        return errorHandler;
    }
}

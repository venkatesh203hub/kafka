//package com.example.kafka.config;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
//import org.springframework.kafka.listener.DefaultErrorHandler;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import org.springframework.util.backoff.FixedBackOff;
//
//@Configuration
//public class KafkaConfig {
//	private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);
//	
//    @Bean
//    public ProducerFactory<String, Object> producerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);
//        
//     // 🔄 Enable retries and idempotence
//        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
//        config.put(ProducerConfig.ACKS_CONFIG, "all");
//        config.put(ProducerConfig.RETRIES_CONFIG, 5);
//        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
//        
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//     //   config.put(ConsumerConfig.GROUP_ID_CONFIG, "grp1");
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(KafkaTemplate<String, Object> kafkaTemplate) {
//    	
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        
//        factory.setConsumerFactory(consumerFactory());
//        
//        // Retry every 5 seconds, up to 3 attempts
//        FixedBackOff backOff = new FixedBackOff(5000L, 3L);
//
//        // Send failed messages to DLT
//        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
//
//		DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);
//
//        factory.setCommonErrorHandler(errorHandler);
//        
//        return factory;
//    }
    
    
    
    //for retry
    
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(KafkaTemplate<String, Object> kafkaTemplate) {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        
//        factory.setConsumerFactory(consumerFactory());
//        
//        // Define which error codes should be retried (Java 8 compatible)
//        Set<String> retryableErrorCodes = new HashSet<>(Arrays.asList("63000")); // Only 63000 will retry
//        
//        // Retry every 5 seconds, up to 3 attempts
//        FixedBackOff backOff = new FixedBackOff(5000L, 3L);
//        
//        // Send failed messages to DLT
//        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
//        
//        // Use custom error handler
//        ConditionalErrorHandler errorHandler = new ConditionalErrorHandler(recoverer, backOff, retryableErrorCodes);
//        
//        // Add retry listener for logging
//        errorHandler.setRetryListeners(new RetryListener() {
//            @Override
//            public void failedDelivery(ConsumerRecord<?, ?> record, Exception ex, int deliveryAttempt) {
//                logger.info("🔄 Retry attempt " + deliveryAttempt + " for record: " + record.key());
//            }
//        });
//
//        factory.setCommonErrorHandler(errorHandler);
//        return factory;
//    }
    
//    @Bean
//    public NewTopic createTopic() {
//        return new NewTopic("porting-topic", 2, (short) 1); // 3 partitions
//    }
//}

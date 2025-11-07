package com.example.kafka.handlers;

import java.util.List;
import java.util.Set;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.backoff.BackOff;

import com.example.kafka.exception.KafkaExeception;

public class ConditionalErrorHandler extends DefaultErrorHandler {
	private static final Logger logger = LoggerFactory.getLogger(ConditionalErrorHandler.class);
    private final Set<String> retryableErrorCodes;
    private final ConsumerRecordRecoverer recoverer;
    
    public ConditionalErrorHandler(ConsumerRecordRecoverer recoverer, BackOff backOff, Set<String> retryableErrorCodes) {
        super(recoverer, backOff);
        this.retryableErrorCodes = retryableErrorCodes;
        this.recoverer = recoverer;
    }
    
    @Override
    public void handleRemaining(Exception thrownException, List<ConsumerRecord<?, ?>> records,
                                 Consumer<?, ?> consumer, MessageListenerContainer container) {
    	logger.info("🚨 handleRemaining called with exception: " + thrownException.getClass().getName());
    	logger.info("🚨 Exception message: " + thrownException.getMessage());
        
        // Extract the actual KafkaExeception (might be wrapped)
        KafkaExeception kafkaEx = extractKafkaException(thrownException);
        
        if (kafkaEx != null) {
        	logger.info("🔍 Error code received: '" + kafkaEx.getErrorCode() + "'");
        	logger.info("🔍 Retryable codes: " + retryableErrorCodes);
            
            if (!retryableErrorCodes.contains(kafkaEx.getErrorCode())) {
                // Not retryable - send directly to recoverer (DLT)
                logger.info("❌ Error code '" + kafkaEx.getErrorCode() + "' is NOT in retryable list, sending to DLT immediately (NO RETRIES)");
                // Invoke recoverer directly for each record
                for (ConsumerRecord<?, ?> record : records) {
                    this.recoverer.accept(record, thrownException);
                }
                return; // Exit here - no retries
            } else {
            	 logger.info("✅ Error code '" + kafkaEx.getErrorCode() + "' is in retryable list, will retry up to 3 times");
            }
        } else {
            logger.info("⚠️ Could not extract KafkaExeception from: " + thrownException.getClass().getName());
        }
        
        // For retryable errors or other exceptions, use default retry behavior
        super.handleRemaining(thrownException, records, consumer, container);
    }
    
    // Helper method to extract KafkaExeception from wrapped exceptions
    private KafkaExeception extractKafkaException(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        
        if (throwable instanceof KafkaExeception) {
            return (KafkaExeception) throwable;
        }
        
        // Check the cause
        return extractKafkaException(throwable.getCause());
    }
}
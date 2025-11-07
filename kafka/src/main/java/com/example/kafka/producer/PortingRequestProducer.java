package com.example.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.kafka.model.PortOutRequest;
import com.example.kafka.model.PortingRequest;

@Service
public class PortingRequestProducer {
	private static final Logger logger = LoggerFactory.getLogger(PortingRequestProducer.class);
 //   private final KafkaTemplate<String, PortingRequest> kafkaTemplate;

//    public PortingRequestProducer(KafkaTemplate<String, PortingRequest> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendPortingRequest(PortingRequest request) {
//        kafkaTemplate.send("porting-topic", request);
//        System.out.println("✅ Sent PortingRequest: " + request.getRequestId());
//    }
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPortingRequest(PortingRequest request) {
    	logger.info("in PortingRequestProducer to send the data to porting-topic -------");
        kafkaTemplate.send("porting-topic",/*request.getRequestId(),*/ request);
    }
    
    public void sendPortoutRequest(PortOutRequest req) {
    	logger.info("in PortingRequestProducer to send the data to portout-topic -------");
        kafkaTemplate.send("portout-topic",req.getRequestId(), req);
    }
}

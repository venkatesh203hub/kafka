package com.example.kafka.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.model.PortOutRequest;
import com.example.kafka.model.PortingRequest;
import com.example.kafka.producer.PortingRequestProducer;

@RestController
@RequestMapping("/api/porting")
public class PortingController {
	
	private static final Logger logger = LoggerFactory.getLogger(PortingController.class);

    private final PortingRequestProducer producer;

    public PortingController(PortingRequestProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/send")
    public String sendPortingRequest(@RequestBody PortingRequest request) {
    	logger.info("Received request to porting msisdn: {}", request.getMsisdn());
       // producer.sendPortingRequest(request);
    	producer.sendPortingRequest(request);
    	logger.info("Sent PortingRequest RequestId: {}", request.getRequestId());
        return "Sent PortingRequest: " + request.getRequestId();
    }
    
    @PostMapping("/out")
    public String sendPortoutRequest(@RequestBody PortOutRequest request) {
    	logger.info("Received request to porting msisdn: {}", request.getMsisdn());
       // producer.sendPortingRequest(request);
    	producer.sendPortoutRequest(request);
    	logger.info("Sent PortOutRequest RequestId: {}", request.getRequestId());
        return "Sent PortOutRequest: " + request.getRequestId();
    }
}

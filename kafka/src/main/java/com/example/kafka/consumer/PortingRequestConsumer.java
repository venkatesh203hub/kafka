package com.example.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.kafka.exception.KafkaExeception;
import com.example.kafka.model.PortOutRequest;
import com.example.kafka.model.PortingRequest;

@Service
public class PortingRequestConsumer {
	private static final Logger logger = LoggerFactory.getLogger(PortingRequestConsumer.class);
//    @KafkaListener(topics = "porting-topic", groupId = "my-group")
//    public void listen(PortingRequest request) {
//        System.out.println("📩 Received PortingRequest: " + request.getRequestId() +
//                " for MSISDN: " + request.getMsisdn());
//    }
	
//
//	    @KafkaListener(topics = "porting-topic", groupId = "grp1")
//	    public void consume(PortingRequest request) {
//	        System.out.println("Received: " + request.getMsisdn() + " from " + request.getDonor());
//	    }


	
	@KafkaListener(topics = "porting-topic", groupId = "grp1")
    public void consumeA(ConsumerRecord<String, Object> record) throws KafkaExeception {
        Object message = record.value();

        if (message instanceof PortingRequest) {
            PortingRequest request = (PortingRequest) message;
           
            // Simulate an error for testing
            if (request.getMsisdn().equals("9876543210")) {
                throw new KafkaExeception("6300","❌ Simulated processing failure for testing retries");
            }else{
            	logger.info("📩 Received PortingRequest consumeA grp1: {}" + request.getMsisdn() + " from " + request.getDonor());
            	 System.out.println("📩 Received PortingRequest consumeA grp1: " + request.getMsisdn() + " from " + request.getDonor());
            }
        } else {
            System.out.println("⚠️ Unknown message type: " + message.getClass().getName());
        }
    }
	
	
	@KafkaListener(topics = "porting-topic", groupId = "grp1")
    public void consumeB(ConsumerRecord<String, Object> record) {
        Object message = record.value();

        if (message instanceof PortingRequest) {
            PortingRequest request = (PortingRequest) message;
            System.out.println("📩 Received PortingRequest consumeB grp1: " + request.getMsisdn() + " from " + request.getDonor());
        } else {
            System.out.println("⚠️ Unknown message type: " + message.getClass().getName());
        }
    }
	
	@KafkaListener(topics = "portout-topic", groupId = "grp2")
    public void consumeC(ConsumerRecord<String, Object> record) {
        Object message = record.value();

        if (message instanceof PortOutRequest) {
        	PortOutRequest request = (PortOutRequest) message;
        	logger.info("📩 Received PortOutRequest consumeC grp2: {}" + request.getMsisdn() + " from " + request.getDonor());
            System.out.println("📩 Received PortOutRequest consumeC grp2: " + request.getMsisdn() + " from " + request.getDonor());
        } else {
            System.out.println("⚠️ Unknown message type: " + message.getClass().getName());
        }
    }
	
	@KafkaListener(topics = {"porting-topic","portout-topic"}, groupId = "grp3")
    public void consumeD(ConsumerRecord<String, Object> record) {
        Object message = record.value();
        
        if (message instanceof PortingRequest) {
            PortingRequest request = (PortingRequest) message;
            System.out.println("📩 Received PortingRequest consumeD grp3: " + request.getMsisdn() + " from " + request.getDonor());
        }else if (message instanceof PortOutRequest) {
        	PortOutRequest request = (PortOutRequest) message;
            System.out.println("📩 Received PortOutRequest consumeD grp3: " + request.getMsisdn() + " from " + request.getDonor());
        }
        else {
            System.out.println("⚠️ Unknown message type: " + message.getClass().getName());
        }
    }
	
	@KafkaListener(topics = "porting-topic.DLT", groupId = "grp-dlt")
	public void consumeDLT(ConsumerRecord<String, Object> record) {
		 Object message = record.value();
		System.out.println("☠️ DLT Received: " + message);
		if (message instanceof PortingRequest) {
            PortingRequest request = (PortingRequest) message;
            logger.info("📩 Received PortOutRequest consumeDLT grpdlt: {}" + request.getMsisdn() + " from " + request.getDonor());
            System.out.println("📩 Received PortOutRequest consumeDLT grpdlt: " + request.getMsisdn() + " from " + request.getDonor());
		}
	}

}

package com.harshit.kafka.interview.q5;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * INTERVIEW QUESTION 5 (Part 2): How do you implement Kafka Consumer in Spring Boot?
 * 
 * ANSWER:
 * In Spring Boot, you use @KafkaListener annotation on methods.
 * 
 * STEPS:
 * 1. Configure Kafka in application.properties
 * 2. Annotate method with @KafkaListener
 * 3. Specify topics and groupId
 * 4. Receive message in method parameter
 * 5. Use @Header for metadata (partition, offset, etc.)
 * 6. Use Acknowledgment for manual commit
 * 
 * KEY FEATURES:
 * - Automatic consumer creation
 * - Easy topic subscription
 * - Access to metadata via @Header
 * - Manual/automatic acknowledgment
 * - Error handling with @RetryableTopic
 * 
 * USAGE:
 * - Start Spring Boot application
 * - Send messages using SpringKafkaProducerService
 * - Messages will be automatically consumed by listeners
 */
@Service
public class SpringKafkaConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(SpringKafkaConsumerService.class);
    
    /**
     * Basic consumer - receives message payload only
     */
    @KafkaListener(topics = "basic-topic", groupId = "spring-consumer-group")
    public void consumeMessage(String message) {
        logger.info("Received message: {}", message);
        System.out.println("=========================================");
        System.out.println("Basic Consumer - Message: " + message);
        System.out.println("=========================================\n");
    }
    
    /**
     * Advanced consumer - receives full ConsumerRecord with metadata
     */
    @KafkaListener(topics = "basic-topic", groupId = "spring-consumer-group-advanced")
    public void consumeMessageWithMetadata(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        
        logger.info("Received message with metadata");
        System.out.println("=========================================");
        System.out.println("Advanced Consumer - Full Metadata:");
        System.out.println("  Topic: " + topic);
        System.out.println("  Partition: " + partition);
        System.out.println("  Offset: " + offset);
        System.out.println("  Key: " + key);
        System.out.println("  Message: " + message);
        System.out.println("=========================================\n");
    }
    
    /**
     * Consumer with manual acknowledgment
     */
    @KafkaListener(topics = "basic-topic", groupId = "spring-consumer-group-manual")
    public void consumeWithManualAck(
            @Payload String message,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        try {
            logger.info("Processing message from partition {} at offset {}", partition, offset);
            System.out.println("Processing message: " + message);
            
            // Simulate business logic
            processMessage(message);
            
            // Manually acknowledge after successful processing
            acknowledgment.acknowledge();
            logger.info("Message acknowledged successfully");
            
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage());
            // Don't acknowledge - message will be redelivered
            // In production, you might want to send to DLQ
        }
    }
    
    /**
     * Consumer receiving ConsumerRecord directly
     */
    @KafkaListener(topics = "basic-topic", groupId = "spring-consumer-group-record")
    public void consumeRecord(ConsumerRecord<String, String> record) {
        logger.info("Received ConsumerRecord");
        System.out.println("=========================================");
        System.out.println("ConsumerRecord Details:");
        System.out.println("  Topic: " + record.topic());
        System.out.println("  Partition: " + record.partition());
        System.out.println("  Offset: " + record.offset());
        System.out.println("  Key: " + record.key());
        System.out.println("  Value: " + record.value());
        System.out.println("  Timestamp: " + record.timestamp());
        System.out.println("=========================================\n");
    }
    
    private void processMessage(String message) {
        // Simulate processing
        try {
            Thread.sleep(100);
            System.out.println("Message processed: " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


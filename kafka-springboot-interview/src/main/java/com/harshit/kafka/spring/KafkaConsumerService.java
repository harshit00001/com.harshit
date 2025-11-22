package com.harshit.kafka.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * SPRING KAFKA CONSUMER SERVICE - Interview Explanation
 * 
 * This service demonstrates how to consume messages from Kafka topics using
 * Spring Kafka's @KafkaListener annotation. This is the most common and
 * recommended way to consume messages in Spring Boot applications.
 * 
 * Interview Question: "How do you consume messages from Kafka in Spring Boot?"
 * 
 * Answer Explanation:
 * 
 * In Spring Boot, we use the @KafkaListener annotation to create message consumers.
 * This annotation is much simpler than manually creating KafkaConsumer instances
 * and managing the consumption loop.
 * 
 * The @KafkaListener annotation tells Spring to create a consumer that listens
 * to the specified topics. Spring automatically handles all the complexity of
 * creating the consumer, managing the consumer group, handling partitions, and
 * managing offsets.
 * 
 * You can specify multiple topics, a consumer group ID, and other configuration
 * options directly in the annotation. Spring will use the properties from
 * application.properties for additional configuration like bootstrap servers,
 * serializers, and offset reset behavior.
 * 
 * The method annotated with @KafkaListener receives the message payload as a
 * parameter. You can also access metadata like the topic, partition, offset,
 * and headers using additional parameters annotated with @Header.
 * 
 * For manual offset acknowledgment, you can inject an Acknowledgment object and
 * call ack() after successfully processing the message. This gives you fine-grained
 * control over when offsets are committed.
 */
@Slf4j
@Service
public class KafkaConsumerService {
    
    /**
     * Interview Question: "What is @KafkaListener and how does it work?"
     * 
     * This is the simplest form of a Kafka listener. It listens to a single topic
     * and processes messages as they arrive. Spring handles all the consumer management
     * automatically.
     */
    @KafkaListener(topics = "order-created-topic", groupId = "order-processing-group")
    public void consumeOrderCreated(@Payload String message) {
        log.info("📦 Received order created message: {}", message);
        
        // Interview Point: Process the message here
        // This could involve:
        // - Parsing the message
        // - Calling business logic
        // - Updating database
        // - Sending notifications
    }
    
    /**
     * Interview Question: "How do you consume from multiple topics?"
     * 
     * You can specify multiple topics in the topics array. The consumer will
     * receive messages from all specified topics.
     */
    @KafkaListener(
        topics = {"payment-processed-topic", "inventory-updated-topic"},
        groupId = "notification-group"
    )
    public void consumeMultipleTopics(@Payload String message,
                                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("📨 Received message from topic: {}, message: {}", topic, message);
        
        // Interview Point: You can handle different topics differently
        if (topic.equals("payment-processed-topic")) {
            log.info("Processing payment notification...");
        } else if (topic.equals("inventory-updated-topic")) {
            log.info("Processing inventory update...");
        }
    }
    
    /**
     * Interview Question: "How do you access message metadata like partition and offset?"
     * 
     * You can access metadata about the message using @Header annotations.
     * This is useful for logging, debugging, or implementing idempotency.
     */
    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consumeWithMetadata(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {
        
        log.info("📬 Message Details:");
        log.info("   Topic: {}", topic);
        log.info("   Partition: {}", partition);
        log.info("   Offset: {}", offset);
        log.info("   Timestamp: {}", timestamp);
        log.info("   Message: {}", message);
    }
    
    /**
     * Interview Question: "How do you manually acknowledge messages?"
     * 
     * By default, Spring Kafka uses automatic acknowledgment. However, for
     * better control, especially when processing might fail, you can use
     * manual acknowledgment. This ensures offsets are only committed after
     * successful processing.
     */
    @KafkaListener(topics = "critical-topic", groupId = "critical-group")
    public void consumeWithManualAck(
            @Payload String message,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Processing critical message: {}", message);
            
            // Interview Point: Process the message
            // This could be a database operation, external API call, etc.
            processMessage(message);
            
            // Interview Point: Only acknowledge after successful processing
            // This ensures the message won't be reprocessed if something fails
            acknowledgment.ack();
            
            log.info("Message processed and acknowledged successfully");
            
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            // Interview Point: Don't acknowledge on error
            // The message will be redelivered for retry
            // In production, you might want to implement retry logic or
            // send to a dead letter topic after max retries
        }
    }
    
    /**
     * Interview Question: "How do you handle different message types?"
     * 
     * You can consume different types of messages by using different parameter
     * types or by parsing the message. For complex objects, you can use JSON
     * deserializers and receive POJOs directly.
     */
    @KafkaListener(topics = "user-events-topic", groupId = "user-events-group")
    public void consumeUserEvent(@Payload String message) {
        log.info("👤 User event received: {}", message);
        
        // Interview Point: Parse and process based on message content
        // In real applications, you might deserialize to a DTO/Entity
        if (message.contains("created")) {
            handleUserCreated(message);
        } else if (message.contains("updated")) {
            handleUserUpdated(message);
        } else if (message.contains("deleted")) {
            handleUserDeleted(message);
        }
    }
    
    // Helper methods for processing
    private void processMessage(String message) {
        // Simulate processing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void handleUserCreated(String message) {
        log.info("Handling user creation: {}", message);
    }
    
    private void handleUserUpdated(String message) {
        log.info("Handling user update: {}", message);
    }
    
    private void handleUserDeleted(String message) {
        log.info("Handling user deletion: {}", message);
    }
}


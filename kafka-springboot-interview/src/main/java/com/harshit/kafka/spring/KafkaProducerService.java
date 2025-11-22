package com.harshit.kafka.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * SPRING KAFKA PRODUCER SERVICE - Interview Explanation
 * 
 * This service demonstrates how to use Spring Kafka's KafkaTemplate to send
 * messages to Kafka topics. This is the Spring Boot way of creating producers,
 * which is much simpler than using the raw Kafka client.
 * 
 * Interview Question: "How do you send messages to Kafka in Spring Boot?"
 * 
 * Answer Explanation:
 * 
 * In Spring Boot, we use KafkaTemplate instead of directly creating KafkaProducer
 * instances. Spring Boot automatically configures KafkaTemplate for us based on
 * the properties in application.properties or application.yml.
 * 
 * KafkaTemplate is injected using dependency injection, typically with @Autowired
 * or constructor injection. It's thread-safe and can be used across multiple
 * threads in your application.
 * 
 * To send a message, we simply call the send() method with the topic name and
 * message. The send() method returns a CompletableFuture, which allows us to
 * handle the result asynchronously. We can add callbacks to handle success or
 * failure scenarios.
 * 
 * Spring Kafka also supports sending messages with keys, which is important for
 * partitioning. Messages with the same key will always go to the same partition,
 * ensuring ordering for that key.
 * 
 * The KafkaTemplate handles serialization automatically based on the configured
 * serializers. It also handles connection management, retries, and error handling
 * based on your configuration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    
    // Interview Point: KafkaTemplate is auto-configured by Spring Boot
    // It's thread-safe and handles all the complexity of producer management
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    /**
     * Interview Question: "How do you send a simple message to a Kafka topic?"
     * 
     * This method demonstrates the simplest way to send a message to Kafka
     * using Spring Kafka. We just need the topic name and the message value.
     */
    public void sendMessage(String topic, String message) {
        log.info("Sending message to topic: {}, message: {}", topic, message);
        
        // Interview Point: send() is asynchronous and returns CompletableFuture
        // The message is sent in the background
        CompletableFuture<SendResult<String, String>> future = 
            kafkaTemplate.send(topic, message);
        
        // Interview Point: Handle success and failure with callbacks
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("Message sent successfully! Topic: {}, Partition: {}, Offset: {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send message: {}", exception.getMessage());
            }
        });
    }
    
    /**
     * Interview Question: "How do you send a message with a key to ensure partitioning?"
     * 
     * This method demonstrates sending messages with keys. Messages with the same
     * key will always go to the same partition, which is crucial for maintaining
     * message ordering for related messages.
     */
    public void sendMessageWithKey(String topic, String key, String message) {
        log.info("Sending message with key. Topic: {}, Key: {}, Message: {}", 
            topic, key, message);
        
        // Interview Point: Sending with key ensures messages with same key
        // go to the same partition, maintaining order for that key
        kafkaTemplate.send(topic, key, message);
    }
    
    /**
     * Interview Question: "How do you handle producer callbacks and errors?"
     * 
     * This method shows how to properly handle both success and failure scenarios
     * when sending messages. This is important for production applications where
     * you need to know if messages were successfully published.
     */
    public void sendMessageWithCallback(String topic, String message) {
        log.info("Sending message with callback. Topic: {}, Message: {}", topic, message);
        
        CompletableFuture<SendResult<String, String>> future = 
            kafkaTemplate.send(topic, message);
        
        // Interview Point: Using whenComplete for both success and failure
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                // Success case
                log.info("✅ Message sent successfully!");
                log.info("   Topic: {}", result.getRecordMetadata().topic());
                log.info("   Partition: {}", result.getRecordMetadata().partition());
                log.info("   Offset: {}", result.getRecordMetadata().offset());
                log.info("   Timestamp: {}", result.getRecordMetadata().timestamp());
            } else {
                // Failure case - handle the error appropriately
                log.error("❌ Failed to send message", exception);
                // In production, you might want to:
                // - Retry the message
                // - Store it in a dead letter queue
                // - Send an alert
            }
        });
    }
    
    /**
     * Interview Question: "How do you send messages synchronously?"
     * 
     * Sometimes you need to wait for the message to be sent before continuing.
     * You can do this by calling get() on the CompletableFuture, but be careful
     * as this blocks the thread.
     */
    public void sendMessageSynchronously(String topic, String message) {
        log.info("Sending message synchronously. Topic: {}, Message: {}", topic, message);
        
        try {
            // Interview Point: get() blocks until message is sent
            // Use this only when you need to ensure message is sent before continuing
            SendResult<String, String> result = kafkaTemplate.send(topic, message).get();
            
            log.info("Message sent synchronously. Partition: {}, Offset: {}",
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
                
        } catch (Exception e) {
            log.error("Error sending message synchronously", e);
            throw new RuntimeException("Failed to send message", e);
        }
    }
}


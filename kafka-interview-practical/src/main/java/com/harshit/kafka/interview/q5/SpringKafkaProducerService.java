package com.harshit.kafka.interview.q5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * INTERVIEW QUESTION 5: How do you implement Kafka Producer in Spring Boot?
 * 
 * ANSWER:
 * In Spring Boot, you can use KafkaTemplate which is auto-configured by Spring.
 * 
 * STEPS:
 * 1. Configure Kafka in application.properties
 * 2. Inject KafkaTemplate in your service
 * 3. Use send() method to publish messages
 * 4. Handle callbacks for success/failure
 * 
 * KEY BENEFITS:
 * - Auto-configuration (less boilerplate)
 * - Integration with Spring ecosystem
 * - Easy testing with @SpringBootTest
 * - Support for transactions
 * 
 * USAGE:
 * - Start Spring Boot application
 * - Call REST endpoint: POST http://localhost:8080/api/kafka/send?message=Hello
 * - Or use: POST http://localhost:8080/api/kafka/send-with-key?key=user-1&message=Hello
 */
@Service
public class SpringKafkaProducerService {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private static final String TOPIC_NAME = "basic-topic";
    
    /**
     * Send message without key
     */
    public void sendMessage(String message) {
        // Simple send - fire and forget
        kafkaTemplate.send(TOPIC_NAME, message);
        System.out.println("Message sent: " + message);
    }
    
    /**
     * Send message with key (for partitioning)
     */
    public void sendMessageWithKey(String key, String message) {
        kafkaTemplate.send(TOPIC_NAME, key, message);
        System.out.println("Message sent with key [" + key + "]: " + message);
    }
    
    /**
     * Send message with callback (asynchronous)
     */
    public void sendMessageWithCallback(String message) {
        CompletableFuture<SendResult<String, String>> future = 
            kafkaTemplate.send(TOPIC_NAME, message);
        
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                System.out.println("Message sent successfully!");
                System.out.println("Topic: " + result.getRecordMetadata().topic());
                System.out.println("Partition: " + result.getRecordMetadata().partition());
                System.out.println("Offset: " + result.getRecordMetadata().offset());
            } else {
                System.err.println("Failed to send message: " + exception.getMessage());
            }
        });
    }
    
    /**
     * Send message synchronously (wait for result)
     */
    public void sendMessageSynchronously(String message) {
        try {
            SendResult<String, String> result = kafkaTemplate.send(TOPIC_NAME, message).get();
            System.out.println("Message sent synchronously!");
            System.out.println("Offset: " + result.getRecordMetadata().offset());
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}


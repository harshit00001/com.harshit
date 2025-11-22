package com.harshit.kafka.controller;

import com.harshit.kafka.spring.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * KAFKA REST CONTROLLER - Interview Explanation
 * 
 * This controller demonstrates how to integrate Kafka producers with REST APIs.
 * This is a common pattern in microservices where HTTP endpoints trigger
 * Kafka message publishing.
 * 
 * Interview Question: "How do you integrate Kafka with REST APIs in Spring Boot?"
 * 
 * Answer Explanation:
 * 
 * In a typical microservices architecture, you often have REST endpoints that
 * need to publish events to Kafka. This controller shows how to do that.
 * 
 * When a client sends an HTTP request, the controller receives it, performs
 * any necessary validation or processing, and then publishes a message to Kafka.
 * This decouples the HTTP request handling from the actual event processing,
 * which is handled by consumers in other services.
 * 
 * This pattern is useful for:
 * - Event-driven architectures
 * - Microservices communication
 * - Asynchronous processing
 * - Decoupling services
 */
@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {
    
    private final KafkaProducerService kafkaProducerService;
    
    /**
     * Interview Question: "How do you publish a message to Kafka from a REST endpoint?"
     * 
     * This endpoint demonstrates the basic pattern: receive HTTP request,
     * publish to Kafka, return response.
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam String topic,
            @RequestParam String message) {
        
        kafkaProducerService.sendMessage(topic, message);
        
        return ResponseEntity.ok("Message sent to topic: " + topic);
    }
    
    /**
     * Interview Question: "How do you handle order creation events?"
     * 
     * This is a real-world example where an order is created via REST API,
     * and we publish an event to Kafka for other services to consume.
     */
    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody String orderData) {
        // Interview Point: In real application, you would:
        // 1. Validate the order data
        // 2. Save to database
        // 3. Publish event to Kafka for other services
        
        kafkaProducerService.sendMessage("order-created-topic", orderData);
        
        return ResponseEntity.ok("Order created and event published");
    }
    
    /**
     * Interview Question: "How do you send messages with keys for partitioning?"
     * 
     * This endpoint shows how to send messages with keys, which ensures
     * messages with the same key go to the same partition.
     */
    @PostMapping("/send-with-key")
    public ResponseEntity<String> sendMessageWithKey(
            @RequestParam String topic,
            @RequestParam String key,
            @RequestParam String message) {
        
        kafkaProducerService.sendMessageWithKey(topic, key, message);
        
        return ResponseEntity.ok("Message sent with key: " + key);
    }
}


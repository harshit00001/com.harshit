package com.harshit.kafka.interview.q5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for testing Spring Boot Kafka Producer
 */
@RestController
@RequestMapping("/api/kafka")
public class KafkaController {
    
    @Autowired
    private SpringKafkaProducerService producerService;
    
    /**
     * Send simple message
     * GET http://localhost:8080/api/kafka/send?message=Hello Kafka
     */
    @GetMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        producerService.sendMessage(message);
        return ResponseEntity.ok("Message sent: " + message);
    }
    
    /**
     * Send message with key
     * GET http://localhost:8080/api/kafka/send-with-key?key=user-1&message=Hello
     */
    @GetMapping("/send-with-key")
    public ResponseEntity<String> sendMessageWithKey(
            @RequestParam String key,
            @RequestParam String message) {
        producerService.sendMessageWithKey(key, message);
        return ResponseEntity.ok("Message sent with key [" + key + "]: " + message);
    }
    
    /**
     * Send message with callback
     * POST http://localhost:8080/api/kafka/send-callback
     * Body: "Hello Kafka with Callback"
     */
    @PostMapping("/send-callback")
    public ResponseEntity<String> sendMessageWithCallback(@RequestBody String message) {
        producerService.sendMessageWithCallback(message);
        return ResponseEntity.ok("Message sent with callback: " + message);
    }
    
    /**
     * Send message synchronously
     * POST http://localhost:8080/api/kafka/send-sync
     * Body: "Hello Kafka Synchronous"
     */
    @PostMapping("/send-sync")
    public ResponseEntity<String> sendMessageSynchronously(@RequestBody String message) {
        producerService.sendMessageSynchronously(message);
        return ResponseEntity.ok("Message sent synchronously: " + message);
    }
}


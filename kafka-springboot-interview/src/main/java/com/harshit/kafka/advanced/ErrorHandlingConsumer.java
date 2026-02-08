package com.harshit.kafka.advanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

/**
 * ERROR HANDLING IN KAFKA CONSUMERS - Interview Explanation
 * 
 * This class demonstrates advanced error handling strategies in Kafka consumers.
 * Proper error handling is crucial for production applications.
 * 
 * Interview Question: "How do you handle errors in Kafka consumers?"
 * 
 * Answer Explanation:
 * 
 * Error handling in Kafka consumers is critical for building resilient applications.
 * There are several strategies you can use:
 * 
 * 1. Retry with Exponential Backoff: When a message fails to process, you can
 *    retry it with increasing delays. This is useful for transient errors like
 *    network issues or temporary database unavailability.
 * 
 * 2. Dead Letter Topic (DLT): After exhausting retries, failed messages can be
 *    sent to a dead letter topic for manual inspection and processing. This prevents
 *    bad messages from blocking the main processing flow.
 * 
 * 3. Manual Acknowledgment: Only acknowledge messages after successful processing.
 *    If processing fails, don't acknowledge, and the message will be redelivered.
 * 
 * 4. Exception Handling: Catch specific exceptions and handle them differently.
 *    For example, validation errors might go to a different topic than system errors.
 */
@Slf4j
@Service
public class ErrorHandlingConsumer {
    
    /**
     * Interview Question: "How do you implement retry logic with dead letter topic?"
     * 
     * The @RetryableTopic annotation automatically handles retries and sends
     * failed messages to a dead letter topic after exhausting retries.
     */
    @RetryableTopic(
        attempts = "3",  // Retry 3 times
        backoff = @Backoff(delay = 1000, multiplier = 2),  // Exponential backoff: 1s, 2s, 4s
        dltStrategy = DltStrategy.FAIL_ON_ERROR,  // Send to DLT on failure
        dltTopicSuffix = "-dlt"  // DLT topic will be "payment-topic-dlt"
    )
    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
    public void processPaymentWithRetry(@Payload String message) {
        log.info("Processing payment: {}", message);
        
        // Interview Point: Simulate processing that might fail
        if (message.contains("invalid")) {
            throw new RuntimeException("Invalid payment data");
        }
        
        // Process payment logic here
        log.info("Payment processed successfully");
    }
    
    /**
     * Interview Question: "How do you handle dead letter topic messages?"
     * 
     * This listener processes messages that failed after all retries.
     * You can implement custom logic like alerting, logging, or manual review.
     */
    @KafkaListener(topics = "payment-topic-dlt", groupId = "dlt-group")
    public void handleDeadLetterMessage(
            @Payload String message,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage,
            @Header(KafkaHeaders.EXCEPTION_STACKTRACE) String stackTrace) {
        
        log.error("❌ Dead Letter Message Received:");
        log.error("   Original Message: {}", message);
        log.error("   Exception: {}", exceptionMessage);
        log.error("   Stack Trace: {}", stackTrace);
        
        // Interview Point: In production, you might:
        // - Send alert to monitoring system
        // - Store in database for manual review
        // - Notify administrators
        // - Attempt alternative processing
    }
    
    /**
     * Interview Question: "How do you manually handle errors with try-catch?"
     * 
     * Sometimes you need more control over error handling. You can use
     * try-catch blocks and decide what to do with failed messages.
     */
    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void processOrderWithManualErrorHandling(
            @Payload String message,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Processing order: {}", message);
            
            // Interview Point: Validate message
            if (message == null || message.isEmpty()) {
                throw new IllegalArgumentException("Empty order message");
            }
            
            // Interview Point: Process the order
            processOrder(message);
            
            // Interview Point: Only acknowledge on success
            acknowledgment.acknowledge();
            log.info("Order processed and acknowledged");
            
        } catch (IllegalArgumentException e) {
            // Interview Point: Handle validation errors differently
            log.error("Validation error for order: {}", message, e);
            // Don't acknowledge - message will be redelivered
            // In production, you might send to a validation-error topic
            
        } catch (Exception e) {
            // Interview Point: Handle other errors
            log.error("Error processing order: {}", message, e);
            // Don't acknowledge - message will be redelivered
            // After max retries, consider sending to DLT
        }
    }
    
    private void processOrder(String message) {
        // Simulate order processing
        if (message.contains("fail")) {
            throw new RuntimeException("Order processing failed");
        }
        log.info("Order processed: {}", message);
    }
}


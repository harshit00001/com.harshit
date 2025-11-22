package com.harshit.springboot.advanced;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * EXCEPTION HANDLING AND RESILIENCE PATTERNS
 * 
 * In microservices architectures, services often depend on other services. When
 * a downstream service fails, it's important to ensure that upstream services
 * remain resilient and can continue operating. This requires implementing various
 * resilience patterns and exception handling strategies.
 * 
 * To ensure upstream services remain resilient regardless of the type of error
 * in a downstream service, you need to implement a combination of resilience
 * patterns, exception handling, and fallback strategies. This includes centralized
 * exception handling, circuit breakers, timeouts, retries, and asynchronous
 * communication patterns.
 */
@RestController
@RequestMapping("/api/resilience")
public class ExceptionHandlingAndResilience {
    
    private final RestTemplate restTemplate;
    
    public ExceptionHandlingAndResilience(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * STRATEGY 1: CENTRALIZED EXCEPTION HANDLING
     * 
     * The first strategy is to catch all exceptions from the downstream service
     * using try-catch blocks. You handle specific exceptions like
     * HttpClientErrorException for 4xx errors, ResourceAccessException for
     * connection issues, and provide meaningful fallback responses. This ensures
     * that exceptions don't propagate and crash your service.
     */
    public ResponseEntity<Map<String, String>> callServiceDWithExceptionHandling() {
        try {
            // Interview Point: Try-catch handles all exceptions
            ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://service-d/api/data", Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // Handle 4xx errors (client errors)
            System.out.println("Service D returned client error: " + e.getStatusCode());
            Map<String, String> fallback = new HashMap<>();
            fallback.put("message", "Service D unavailable - client error");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallback);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            // Handle connection/timeout errors
            System.out.println("Service D connection failed: " + e.getMessage());
            Map<String, String> fallback = new HashMap<>();
            fallback.put("message", "Service D unavailable - connection error");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallback);
        } catch (Exception e) {
            // Handle any other exceptions
            System.out.println("Service D failed with error: " + e.getMessage());
            Map<String, String> fallback = new HashMap<>();
            fallback.put("message", "Service D unavailable - unknown error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fallback);
        }
    }
    
    /**
     * STRATEGY 2: CIRCUIT BREAKER PATTERN
     * 
     * A circuit breaker automatically short-circuits calls to a service after
     * repeated failures. When the circuit is open, calls fail fast without
     * actually calling the service, which prevents cascading failures and
     * protects your service. After a timeout period, the circuit enters a
     * half-open state to test if the service has recovered.
     * 
     * Resilience4j is a popular library for implementing circuit breakers in
     * Spring Boot. You annotate methods with @CircuitBreaker and provide a
     * fallback method that executes when the circuit is open or when an
     * exception occurs.
     */
    @CircuitBreaker(name = "serviceD", fallbackMethod = "fallbackForServiceD")
    public ResponseEntity<Map<String, String>> callServiceDWithCircuitBreaker() {
        // Interview Point: Circuit breaker automatically handles failures
        // After threshold failures, circuit opens and fallback is called
        ResponseEntity<Map> response = restTemplate.getForEntity(
            "http://service-d/api/data", Map.class);
        return ResponseEntity.ok(response.getBody());
    }
    
    /**
     * FALLBACK METHOD FOR CIRCUIT BREAKER
     * 
     * This method is called when the circuit breaker is open or when an exception
     * occurs. It provides a default response so that your service can continue
     * operating even when the downstream service is unavailable.
     */
    public ResponseEntity<Map<String, String>> fallbackForServiceD(Throwable t) {
        // Interview Point: Fallback provides default response
        // Service continues operating even when downstream service fails
        System.out.println("Circuit breaker activated, using fallback: " + t.getMessage());
        Map<String, String> fallback = new HashMap<>();
        fallback.put("message", "Service D is currently unavailable");
        fallback.put("status", "fallback");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallback);
    }
    
    /**
     * STRATEGY 3: TIMEOUTS AND RETRIES
     * 
     * Setting timeouts prevents your service from blocking indefinitely while
     * waiting for a response. If a response doesn't arrive within the timeout
     * period, the call fails and you can handle it appropriately. Retry logic
     * is useful for transient errors like network glitches or 429 Too Many Requests
     * responses, where retrying after a short delay might succeed.
     */
    public ResponseEntity<Map<String, String>> callServiceDWithTimeout() {
        // Interview Point: Timeouts prevent indefinite blocking
        // Configure in RestTemplate or WebClient
        // If timeout occurs, handle gracefully with fallback
        try {
            // RestTemplate with timeout configured
            ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://service-d/api/data", Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (org.springframework.web.client.ResourceAccessException e) {
            // Timeout or connection error
            System.out.println("Timeout or connection error: " + e.getMessage());
            Map<String, String> fallback = new HashMap<>();
            fallback.put("message", "Service D timeout - using cached data");
            return ResponseEntity.ok(fallback);
        }
    }
    
    /**
     * STRATEGY 4: ASYNCHRONOUS COMMUNICATION
     * 
     * Using message queues like Kafka or RabbitMQ decouples your service from
     * the downstream service. Instead of making a synchronous call and waiting
     * for a response, you publish an event and continue processing. The downstream
     * service processes the event asynchronously, and you can handle the response
     * through a callback or by polling for results.
     */
    public void callServiceDAsynchronously() {
        // Interview Point: Asynchronous communication decouples services
        // Service C can continue without waiting for Service D
        // Use Kafka, RabbitMQ, or other message brokers
        
        // Publish event to message queue
        // kafkaTemplate.send("service-d-events", eventData);
        
        // Service C continues processing without waiting
        System.out.println("Event published to Service D, continuing processing...");
    }
}


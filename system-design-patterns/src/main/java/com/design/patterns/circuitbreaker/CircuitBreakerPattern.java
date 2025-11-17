package com.design.patterns.circuitbreaker;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * CIRCUIT BREAKER PATTERN - Interview Explanation:
 * 
 * Problem: When a service is down or slow, continuously calling it
 * wastes resources and can cause cascading failures.
 * 
 * Solution: Circuit Breaker acts like an electrical circuit breaker.
 * It monitors failures and "opens" the circuit when failures exceed
 * a threshold, preventing further calls. After a timeout, it tries
 * again (half-open state) to see if service recovered.
 * 
 * Three States:
 * 1. CLOSED: Normal operation, requests pass through
 * 2. OPEN: Too many failures, requests are rejected immediately
 * 3. HALF_OPEN: Testing if service recovered, allows limited requests
 * 
 * Interview Tip: Used to prevent cascading failures in microservices.
 */
public class CircuitBreakerPattern {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CIRCUIT BREAKER PATTERN ===\n");
        
        // Interview Point: Create circuit breaker with failure threshold
        CircuitBreaker circuitBreaker = new CircuitBreaker(3, 5000); // 3 failures, 5 sec timeout
        
        // Simulate service calls
        ExternalService service = new ExternalService();
        
        System.out.println("Making requests to external service...\n");
        
        // First few requests - service is working
        for (int i = 1; i <= 2; i++) {
            makeRequest(circuitBreaker, service, i);
            Thread.sleep(500);
        }
        
        // Simulate service failure
        System.out.println("\n--- Service starts failing ---\n");
        service.setFailing(true);
        
        // These requests will fail and eventually open the circuit
        for (int i = 3; i <= 5; i++) {
            makeRequest(circuitBreaker, service, i);
            Thread.sleep(500);
        }
        
        // Circuit is now OPEN - requests are rejected immediately
        System.out.println("\n--- Circuit is OPEN ---\n");
        for (int i = 6; i <= 8; i++) {
            makeRequest(circuitBreaker, service, i);
            Thread.sleep(500);
        }
        
        // Wait for timeout period
        System.out.println("\n--- Waiting for circuit to enter HALF_OPEN state ---\n");
        Thread.sleep(6000);
        
        // Service recovers
        service.setFailing(false);
        System.out.println("--- Service recovered ---\n");
        
        // Circuit is HALF_OPEN - testing if service is back
        for (int i = 9; i <= 10; i++) {
            makeRequest(circuitBreaker, service, i);
            Thread.sleep(500);
        }
    }
    
    private static void makeRequest(CircuitBreaker circuitBreaker, ExternalService service, int requestId) {
        try {
            String result = circuitBreaker.execute(() -> service.call());
            System.out.println("Request " + requestId + ": " + result);
        } catch (CircuitBreakerOpenException e) {
            System.out.println("Request " + requestId + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Request " + requestId + ": Failed - " + e.getMessage());
        }
    }
}

/**
 * Interview Point: Circuit Breaker Implementation
 * 
 * Simple Explanation:
 * - Tracks how many times the service failed
 * - When failures reach threshold, it "opens" (stops allowing requests)
 * - After timeout, it "half-opens" (tests if service is back)
 * - If test succeeds, it "closes" (back to normal)
 */
class CircuitBreaker {
    private final int failureThreshold;
    private final long timeoutMillis;
    
    private AtomicInteger failureCount = new AtomicInteger(0);
    private AtomicInteger successCount = new AtomicInteger(0);
    private AtomicReference<CircuitState> state = new AtomicReference<>(CircuitState.CLOSED);
    private AtomicReference<LocalDateTime> lastFailureTime = new AtomicReference<>();
    private final int halfOpenSuccessThreshold = 2; // Need 2 successes to close
    
    public CircuitBreaker(int failureThreshold, long timeoutMillis) {
        this.failureThreshold = failureThreshold;
        this.timeoutMillis = timeoutMillis;
    }
    
    /**
     * Interview Point: Execute method that wraps service calls
     * Checks circuit state before allowing the call
     */
    public <T> T execute(ServiceCall<T> serviceCall) throws Exception {
        // Check if circuit should transition from OPEN to HALF_OPEN
        if (state.get() == CircuitState.OPEN) {
            if (shouldAttemptReset()) {
                state.set(CircuitState.HALF_OPEN);
                System.out.println("  → Circuit state: OPEN → HALF_OPEN (testing service)");
                successCount.set(0);
            } else {
                throw new CircuitBreakerOpenException("Circuit breaker is OPEN. Service unavailable.");
            }
        }
        
        // Execute the service call
        try {
            T result = serviceCall.call();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw e;
        }
    }
    
    /**
     * Interview Point: Handle successful call
     * - Reset failure count in CLOSED state
     * - Increment success count in HALF_OPEN state
     */
    private void onSuccess() {
        if (state.get() == CircuitState.HALF_OPEN) {
            int successes = successCount.incrementAndGet();
            System.out.println("  ✓ Success in HALF_OPEN state (" + successes + "/" + halfOpenSuccessThreshold + ")");
            
            // Interview Point: If we get enough successes, close the circuit
            if (successes >= halfOpenSuccessThreshold) {
                state.set(CircuitState.CLOSED);
                failureCount.set(0);
                System.out.println("  → Circuit state: HALF_OPEN → CLOSED (service recovered)");
            }
        } else {
            // In CLOSED state, reset failure count on success
            failureCount.set(0);
        }
    }
    
    /**
     * Interview Point: Handle failed call
     * - Increment failure count
     * - Open circuit if threshold reached
     */
    private void onFailure() {
        int failures = failureCount.incrementAndGet();
        lastFailureTime.set(LocalDateTime.now());
        
        System.out.println("  ✗ Failure count: " + failures + "/" + failureThreshold);
        
        if (state.get() == CircuitState.HALF_OPEN) {
            // Interview Point: If it fails in HALF_OPEN, go back to OPEN
            state.set(CircuitState.OPEN);
            System.out.println("  → Circuit state: HALF_OPEN → OPEN (service still failing)");
        } else if (failures >= failureThreshold) {
            // Interview Point: Open the circuit when threshold is reached
            state.set(CircuitState.OPEN);
            System.out.println("  → Circuit state: CLOSED → OPEN (too many failures)");
        }
    }
    
    /**
     * Interview Point: Check if enough time has passed to try again
     */
    private boolean shouldAttemptReset() {
        LocalDateTime lastFailure = lastFailureTime.get();
        if (lastFailure == null) {
            return true;
        }
        long secondsSinceLastFailure = ChronoUnit.MILLIS.between(lastFailure, LocalDateTime.now());
        return secondsSinceLastFailure >= timeoutMillis;
    }
    
    public CircuitState getState() {
        return state.get();
    }
}

/**
 * Interview Point: Circuit States
 */
enum CircuitState {
    CLOSED,   // Normal operation - requests pass through
    OPEN,     // Circuit is open - requests are rejected immediately
    HALF_OPEN // Testing state - allows limited requests to test if service recovered
}

/**
 * Interview Point: Functional interface for service calls
 */
@FunctionalInterface
interface ServiceCall<T> {
    T call() throws Exception;
}

/**
 * Interview Point: Exception thrown when circuit is open
 */
class CircuitBreakerOpenException extends Exception {
    public CircuitBreakerOpenException(String message) {
        super(message);
    }
}

/**
 * Interview Point: Simulated external service
 * Can be configured to fail for testing
 */
class ExternalService {
    private boolean failing = false;
    
    public String call() throws Exception {
        // Simulate network delay
        Thread.sleep(100);
        
        if (failing) {
            throw new Exception("Service unavailable");
        }
        
        return "Service response: Success";
    }
    
    public void setFailing(boolean failing) {
        this.failing = failing;
    }
}

/**
 * INTERVIEW SUMMARY: Circuit Breaker Pattern
 * 
 * When to use:
 * - Calling external services or APIs
 * - Microservices communication
 * - Preventing cascading failures
 * - When service can be temporarily unavailable
 * 
 * Benefits:
 * - Prevents resource waste on failing services
 * - Fails fast instead of timing out
 * - Automatic recovery when service is back
 * - Protects downstream services
 * 
 * Configuration:
 * - Failure threshold: How many failures before opening
 * - Timeout: How long to wait before trying again
 * - Half-open success threshold: How many successes to close circuit
 * 
 * Real-world examples:
 * - Netflix Hystrix (now deprecated, but concept lives on)
 * - Resilience4j (Java library)
 * - Spring Cloud Circuit Breaker
 */




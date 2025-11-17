package com.design.patterns.retry;

import java.util.Random;
import java.util.function.Supplier;

/**
 * RETRY PATTERN - Interview Explanation:
 * 
 * Problem: Network calls, database connections, and external services
 * can fail temporarily. Giving up immediately is not ideal.
 * 
 * Solution: Retry the operation with increasing delays between attempts.
 * This handles transient failures (temporary issues that might resolve).
 * 
 * Simple Explanation:
 * - If something fails, try again
 * - Wait a bit longer each time (exponential backoff)
 * - Give up after maximum attempts
 * - Like knocking on a door - if no answer, wait and try again
 * 
 * Strategies:
 * 1. Fixed Delay: Wait same time between retries
 * 2. Exponential Backoff: Wait longer each time (1s, 2s, 4s, 8s...)
 * 3. Jitter: Add randomness to prevent "thundering herd"
 * 
 * Interview Tip: Essential for resilient distributed systems.
 */
public class RetryPattern {

    public static void main(String[] args) {
        System.out.println("=== RETRY PATTERN ===\n");
        
        // Interview Point: Create retry handler with exponential backoff
        RetryHandler retryHandler = new RetryHandler(3, 1000, RetryStrategy.EXPONENTIAL_BACKOFF);
        
        System.out.println("--- Retry with Exponential Backoff ---");
        demonstrateRetry(retryHandler);
        
        System.out.println("\n--- Retry with Fixed Delay ---");
        RetryHandler fixedRetry = new RetryHandler(3, 500, RetryStrategy.FIXED_DELAY);
        demonstrateRetry(fixedRetry);
        
        System.out.println("\n--- Retry with Jitter (Randomness) ---");
        RetryHandler jitterRetry = new RetryHandler(3, 1000, RetryStrategy.EXPONENTIAL_BACKOFF_WITH_JITTER);
        demonstrateRetry(jitterRetry);
    }
    
    private static void demonstrateRetry(RetryHandler retryHandler) {
        // Interview Point: Simulate an operation that might fail
        UnreliableService service = new UnreliableService();
        
        try {
            String result = retryHandler.execute(() -> {
                return service.callExternalAPI();
            });
            System.out.println("  ✓ Success: " + result);
        } catch (RetryException e) {
            System.out.println("  ✗ Failed after all retries: " + e.getMessage());
        }
    }
}

// ==================== RETRY HANDLER ====================

/**
 * Interview Point: Retry Handler
 * Wraps operations and retries them on failure
 */
class RetryHandler {
    private final int maxAttempts;
    private final long baseDelayMillis;
    private final RetryStrategy strategy;
    private final Random random = new Random();
    
    public RetryHandler(int maxAttempts, long baseDelayMillis, RetryStrategy strategy) {
        this.maxAttempts = maxAttempts;
        this.baseDelayMillis = baseDelayMillis;
        this.strategy = strategy;
    }
    
    /**
     * Interview Point: Execute operation with retry logic
     * Tries the operation, and if it fails, retries with delay
     */
    public <T> T execute(Supplier<T> operation) throws RetryException {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                // Interview Point: Try to execute the operation
                System.out.println("  Attempt " + attempt + "/" + maxAttempts);
                return operation.get();
                
            } catch (Exception e) {
                lastException = e;
                System.out.println("    ✗ Failed: " + e.getMessage());
                
                // Interview Point: Don't retry on last attempt
                if (attempt < maxAttempts) {
                    long delay = calculateDelay(attempt);
                    System.out.println("    ⏳ Waiting " + delay + "ms before retry...");
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RetryException("Retry interrupted", ie);
                    }
                }
            }
        }
        
        // Interview Point: All attempts failed
        throw new RetryException("Operation failed after " + maxAttempts + " attempts", lastException);
    }
    
    /**
     * Interview Point: Calculate delay based on strategy
     * Different strategies for different use cases
     */
    private long calculateDelay(int attempt) {
        switch (strategy) {
            case FIXED_DELAY:
                // Interview Point: Same delay every time
                return baseDelayMillis;
                
            case EXPONENTIAL_BACKOFF:
                // Interview Point: Delay doubles each time: 1s, 2s, 4s, 8s...
                return baseDelayMillis * (long) Math.pow(2, attempt - 1);
                
            case EXPONENTIAL_BACKOFF_WITH_JITTER:
                // Interview Point: Exponential backoff + random jitter
                // Prevents all clients retrying at the same time (thundering herd)
                long exponentialDelay = baseDelayMillis * (long) Math.pow(2, attempt - 1);
                long jitter = (long) (random.nextDouble() * exponentialDelay * 0.3); // 30% jitter
                return exponentialDelay + jitter;
                
            default:
                return baseDelayMillis;
        }
    }
}

/**
 * Interview Point: Retry Strategies
 */
enum RetryStrategy {
    FIXED_DELAY,                      // Same delay every time
    EXPONENTIAL_BACKOFF,             // Delay doubles each time
    EXPONENTIAL_BACKOFF_WITH_JITTER // Exponential + randomness
}

/**
 * Interview Point: Custom exception for retry failures
 */
class RetryException extends Exception {
    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}

// ==================== SIMULATED SERVICE ====================

/**
 * Interview Point: Simulated unreliable service
 * Fails randomly to demonstrate retry pattern
 */
class UnreliableService {
    private int callCount = 0;
    private Random random = new Random();
    
    public String callExternalAPI() throws Exception {
        callCount++;
        
        // Interview Point: Simulate random failures
        // In real system, this could be network timeout, service unavailable, etc.
        if (callCount < 3) {
            throw new Exception("Service temporarily unavailable (attempt " + callCount + ")");
        }
        
        // Interview Point: Eventually succeeds
        return "Success! Data retrieved on attempt " + callCount;
    }
}

// ==================== ADVANCED: RETRY WITH CIRCUIT BREAKER ====================

/**
 * Interview Point: Combining Retry with Circuit Breaker
 * 
 * Simple Explanation:
 * - Retry handles temporary failures
 * - Circuit breaker stops retrying if service is completely down
 * - Best of both worlds: resilient but not wasteful
 */
class RetryWithCircuitBreaker {
    // In real implementation, you'd combine:
    // 1. Retry for transient failures
    // 2. Circuit breaker to stop if service is down
    // 3. Fallback mechanism if all fails
}

/**
 * INTERVIEW SUMMARY: Retry Pattern
 * 
 * When to use:
 * - Network calls (can fail temporarily)
 * - External API calls
 * - Database connections
 * - Any operation that can have transient failures
 * 
 * When NOT to use:
 * - Permanent failures (wrong input, authentication errors)
 * - Operations that are not idempotent (might cause duplicates)
 * - Very long-running operations
 * 
 * Best Practices:
 * - Use exponential backoff (don't overwhelm failing service)
 * - Add jitter (prevent thundering herd problem)
 * - Set maximum attempts (don't retry forever)
 * - Log retry attempts (for debugging)
 * - Consider circuit breaker (stop if service is down)
 * 
 * Real-world examples:
 * - HTTP clients (OkHttp, Apache HttpClient)
 * - Database connection pools
 * - Message queues (Kafka, RabbitMQ)
 * - Cloud services (AWS SDK retries automatically)
 * 
 * Common Configurations:
 * - Max attempts: 3-5
 * - Base delay: 100ms - 1s
 * - Max delay: 30s - 60s
 * - Strategy: Exponential backoff with jitter
 */




package com.design.patterns.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RATE LIMITING PATTERN - Interview Explanation:
 * 
 * Problem: Without rate limiting, a single user or bot can overwhelm
 * your system with too many requests, causing it to crash.
 * 
 * Solution: Rate limiting restricts how many requests a user/client
 * can make in a given time period.
 * 
 * Simple Explanation:
 * - Like a speed limit on a road - you can only go so fast
 * - "You can make 100 requests per minute"
 * - If you exceed the limit, requests are rejected
 * 
 * Common Algorithms:
 * 1. Token Bucket: Tokens added at fixed rate, consume tokens per request
 * 2. Sliding Window: Track requests in time windows
 * 3. Fixed Window: Count requests in fixed time periods
 * 
 * Interview Tip: Essential for API protection and preventing abuse.
 */
public class RateLimitingPattern {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== RATE LIMITING PATTERN ===\n");
        
        // Interview Point: Token Bucket Algorithm
        System.out.println("--- Token Bucket Algorithm ---");
        demonstrateTokenBucket();
        
        Thread.sleep(2000);
        
        // Interview Point: Sliding Window Algorithm
        System.out.println("\n--- Sliding Window Algorithm ---");
        demonstrateSlidingWindow();
    }
    
    /**
     * Interview Point: Token Bucket
     * - Bucket has capacity (max tokens)
     * - Tokens are added at a fixed rate
     * - Each request consumes a token
     * - If no tokens, request is rejected
     */
    private static void demonstrateTokenBucket() throws InterruptedException {
        // Interview Point: Create rate limiter: 10 tokens, refill 2 tokens per second
        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(10, 2);
        
        // Simulate requests
        for (int i = 1; i <= 15; i++) {
            String userId = "user1";
            boolean allowed = rateLimiter.allowRequest(userId);
            
            if (allowed) {
                System.out.println("Request " + i + ": ✓ Allowed");
            } else {
                System.out.println("Request " + i + ": ✗ Rate limited");
            }
            
            Thread.sleep(200); // 200ms between requests
        }
    }
    
    /**
     * Interview Point: Sliding Window
     * - Tracks requests in time windows
     * - Window slides as time passes
     * - More accurate than fixed window
     */
    private static void demonstrateSlidingWindow() throws InterruptedException {
        // Interview Point: Allow 5 requests per 2 seconds
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(5, 2000);
        
        for (int i = 1; i <= 10; i++) {
            String userId = "user1";
            boolean allowed = rateLimiter.allowRequest(userId);
            
            if (allowed) {
                System.out.println("Request " + i + ": ✓ Allowed");
            } else {
                System.out.println("Request " + i + ": ✗ Rate limited");
            }
            
            Thread.sleep(300);
        }
    }
}

// ==================== TOKEN BUCKET ====================

/**
 * Interview Point: Token Bucket Rate Limiter
 * 
 * Simple Explanation:
 * - Imagine a bucket that can hold 10 tokens
 * - Every second, 2 new tokens are added
 * - Each request takes 1 token
 * - If bucket is empty, request is rejected
 */
class TokenBucketRateLimiter {
    private final int capacity; // Max tokens
    private final int refillRate; // Tokens per second
    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    
    public TokenBucketRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }
    
    /**
     * Interview Point: Check if request is allowed
     * Refills tokens based on time passed, then checks if tokens available
     */
    public boolean allowRequest(String userId) {
        TokenBucket bucket = buckets.computeIfAbsent(userId, 
            k -> new TokenBucket(capacity, refillRate));
        
        return bucket.consumeToken();
    }
}

/**
 * Interview Point: Token Bucket for a single user
 */
class TokenBucket {
    private final int capacity;
    private final int refillRate; // tokens per second
    private AtomicInteger tokens;
    private AtomicLong lastRefillTime;
    
    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = new AtomicInteger(capacity); // Start with full bucket
        this.lastRefillTime = new AtomicLong(System.currentTimeMillis());
    }
    
    /**
     * Interview Point: Try to consume a token
     * First refills tokens based on time passed
     */
    public boolean consumeToken() {
        refillTokens();
        
        // Interview Point: Try to get a token (decrement if > 0)
        while (true) {
            int current = tokens.get();
            if (current <= 0) {
                return false; // No tokens available
            }
            
            if (tokens.compareAndSet(current, current - 1)) {
                return true; // Successfully consumed token
            }
            // Retry if CAS failed
        }
    }
    
    /**
     * Interview Point: Refill tokens based on time passed
     * Calculates how many tokens should be added since last refill
     */
    private void refillTokens() {
        long now = System.currentTimeMillis();
        long lastRefill = lastRefillTime.get();
        long timePassed = now - lastRefill;
        
        // Interview Point: Calculate tokens to add
        // If 1 second passed and rate is 2 tokens/sec, add 2 tokens
        int tokensToAdd = (int) ((timePassed / 1000.0) * refillRate);
        
        if (tokensToAdd > 0) {
            // Interview Point: Update tokens and time atomically
            while (true) {
                int current = tokens.get();
                int newValue = Math.min(capacity, current + tokensToAdd);
                
                if (tokens.compareAndSet(current, newValue)) {
                    lastRefillTime.compareAndSet(lastRefill, now);
                    break;
                }
            }
        }
    }
}

// ==================== SLIDING WINDOW ====================

/**
 * Interview Point: Sliding Window Rate Limiter
 * 
 * Simple Explanation:
 * - Tracks when each request happened
 * - Only counts requests in the last N seconds
 * - If count exceeds limit, reject request
 * - Window "slides" as time passes
 */
class SlidingWindowRateLimiter {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final ConcurrentHashMap<String, RequestWindow> windows = new ConcurrentHashMap<>();
    
    public SlidingWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }
    
    /**
     * Interview Point: Check if request is allowed
     * Removes old requests outside window, then checks count
     */
    public boolean allowRequest(String userId) {
        RequestWindow window = windows.computeIfAbsent(userId, 
            k -> new RequestWindow(maxRequests, windowSizeMillis));
        
        return window.allowRequest();
    }
}

/**
 * Interview Point: Request Window for a single user
 * Stores timestamps of requests in the current window
 */
class RequestWindow {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final java.util.Queue<Long> requestTimestamps = new java.util.LinkedList<>();
    
    public RequestWindow(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }
    
    /**
     * Interview Point: Check if request is allowed
     * Synchronized to ensure thread safety
     */
    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        long windowStart = now - windowSizeMillis;
        
        // Interview Point: Remove requests outside the window
        while (!requestTimestamps.isEmpty() && requestTimestamps.peek() < windowStart) {
            requestTimestamps.poll();
        }
        
        // Interview Point: Check if we're under the limit
        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.offer(now);
            return true;
        }
        
        return false; // Rate limited
    }
}

/**
 * INTERVIEW SUMMARY: Rate Limiting Pattern
 * 
 * When to use:
 * - API protection (prevent abuse)
 * - DDoS protection
 * - Cost control (prevent excessive usage)
 * - Fair resource allocation
 * 
 * Common Algorithms:
 * 1. Token Bucket: Good for burst traffic, smooth rate
 * 2. Sliding Window: More accurate, tracks exact time
 * 3. Fixed Window: Simple, but can allow bursts at window boundaries
 * 
 * Implementation considerations:
 * - Per-user vs global limits
 * - Distributed rate limiting (Redis, etc.)
 * - Different limits for different endpoints
 * - Rate limit headers in responses
 * 
 * Real-world examples:
 * - API gateways (Kong, AWS API Gateway)
 * - Social media (Twitter rate limits)
 * - Payment systems (prevent fraud)
 * - Cloud services (AWS, Azure quotas)
 */


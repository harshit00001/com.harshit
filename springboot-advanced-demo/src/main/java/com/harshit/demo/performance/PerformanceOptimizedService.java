package com.harshit.demo.performance;

import com.harshit.demo.entity.User;
import com.harshit.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * INTERVIEW QUESTION 1: How can you improve the performance of a Spring Boot application?
 * 
 * This service demonstrates 10 key performance optimization strategies:
 * 1. Connection Pooling (configured in application.properties)
 * 2. Lazy Loading for JPA entities
 * 3. Caching with @Cacheable
 * 4. Asynchronous processing with @Async
 * 5. Database query optimization
 * 6. HTTP compression (configured in application.properties)
 * 7. Spring Boot Actuator for monitoring
 * 8. Thread pool configuration
 * 9. Connection pooling for HTTP calls
 * 10. JVM optimizations
 */
@Service
public class PerformanceOptimizedService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * STRATEGY 3: CACHING
     * @Cacheable caches the result - subsequent calls with same ID return cached value
     * This avoids hitting the database every time, significantly improving response time
     */
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        System.out.println("⏱️  Fetching user from database: " + id + " - Thread: " + 
            Thread.currentThread().getName());
        // This method result will be cached
        // Subsequent calls with same ID will return cached value without hitting database
        return userRepository.findById(id).orElse(null);
    }
    
    /**
     * STRATEGY 4: ASYNCHRONOUS PROCESSING
     * @Async runs this in a separate thread, not blocking the caller
     * This allows the application to handle more requests concurrently
     */
    @Async
    public CompletableFuture<String> sendEmailAsync(String email) {
        System.out.println("📧 Sending email in thread: " + Thread.currentThread().getName());
        // This runs in a separate thread, not blocking the caller
        emailService.send(email);
        return CompletableFuture.completedFuture("Email sent to: " + email);
    }
    
    /**
     * STRATEGY 5: DATABASE QUERY OPTIMIZATION
     * Using pagination instead of loading all records
     * This reduces memory usage and improves response times
     */
    @Transactional(readOnly = true)
    public Page<User> getUsersPaginated(Pageable pageable) {
        System.out.println("📄 Fetching users with pagination - Page: " + pageable.getPageNumber() + 
            ", Size: " + pageable.getPageSize());
        // Only loads requested page, not all records
        return userRepository.findAll(pageable);
    }
    
    /**
     * STRATEGY 5: OPTIMIZED QUERY
     * Using custom query with proper joins
     */
    @Transactional(readOnly = true)
    public List<User> findUsersByEmail(String email) {
        System.out.println("🔍 Finding users by email with optimized query");
        // Custom query is more efficient than multiple separate queries
        return userRepository.findByEmailContaining(email);
    }
    
    /**
     * DEMONSTRATION METHOD
     * Shows how different performance optimizations work together
     */
    public void demonstratePerformance() {
        System.out.println("\n=========================================");
        System.out.println("🚀 PERFORMANCE OPTIMIZATION DEMONSTRATION");
        System.out.println("=========================================\n");
        
        // Demonstrate caching
        System.out.println("1️⃣  CACHING DEMONSTRATION:");
        System.out.println("   First call (hits database):");
        long start1 = System.currentTimeMillis();
        getUserById(1L);
        long time1 = System.currentTimeMillis() - start1;
        System.out.println("   ⏱️  Time taken: " + time1 + "ms\n");
        
        System.out.println("   Second call (uses cache):");
        long start2 = System.currentTimeMillis();
        getUserById(1L);
        long time2 = System.currentTimeMillis() - start2;
        System.out.println("   ⏱️  Time taken: " + time2 + "ms");
        System.out.println("   ✅ Cache improved performance by: " + (time1 - time2) + "ms\n");
        
        // Demonstrate async processing
        System.out.println("2️⃣  ASYNC PROCESSING DEMONSTRATION:");
        System.out.println("   Main thread: " + Thread.currentThread().getName());
        System.out.println("   Starting async email...");
        long start3 = System.currentTimeMillis();
        CompletableFuture<String> future = sendEmailAsync("user@example.com");
        long time3 = System.currentTimeMillis() - start3;
        System.out.println("   ⏱️  Async call returned in: " + time3 + "ms (non-blocking)");
        System.out.println("   ✅ Main thread continues immediately\n");
        
        // Demonstrate pagination
        System.out.println("3️⃣  PAGINATION DEMONSTRATION:");
        org.springframework.data.domain.PageRequest pageRequest = 
            org.springframework.data.domain.PageRequest.of(0, 10);
        Page<User> page = getUsersPaginated(pageRequest);
        System.out.println("   ✅ Loaded " + page.getContent().size() + " users out of " + 
            page.getTotalElements() + " total");
        System.out.println("   ✅ Memory efficient - only loaded requested page\n");
        
        System.out.println("=========================================\n");
    }
}

/**
 * Email Service for async demonstration
 */
@Service
class EmailService {
    public void send(String email) {
        try {
            System.out.println("      📧 [Async Thread] Simulating email sending to: " + email);
            Thread.sleep(2000); // Simulate time-consuming email sending
            System.out.println("      ✅ [Async Thread] Email sent successfully to: " + email);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("      ❌ Email sending interrupted");
        }
    }
}


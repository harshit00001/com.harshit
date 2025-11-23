package com.harshit.demo.service;

import com.harshit.demo.entity.User;
import com.harshit.demo.performance.*;
import com.harshit.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Step-by-Step Demonstration Service
 * 
 * This service provides detailed, step-by-step demonstrations
 * of each performance optimization strategy with explanations
 */
@Service
public class StepByStepDemoService {
    
    @Autowired
    private CachingExample cachingExample;
    
    @Autowired
    private AsyncProcessingExample asyncExample;
    
    @Autowired
    private QueryOptimizationExample queryOptimization;
    
    @Autowired
    private ConnectionPoolDemo connectionPoolDemo;
    
    @Autowired
    private LazyLoadingDemo lazyLoadingDemo;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * STRATEGY 3: Complete Caching Demonstration with Step-by-Step Explanation
     */
    public void demonstrateCachingStepByStep(Long userId) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📦 STRATEGY 3: CACHING - STEP BY STEP DEMONSTRATION");
        System.out.println("=".repeat(60));
        
        System.out.println("\nSTEP 1: First call to getUserById(" + userId + ")");
        System.out.println("   → @Cacheable checks cache: MISS");
        System.out.println("   → Method executes: Database query runs");
        System.out.println("   → Result stored in cache");
        
        long start1 = System.currentTimeMillis();
        User user1 = cachingExample.getUserById(userId);
        long time1 = System.currentTimeMillis() - start1;
        
        System.out.println("   ✅ Result: " + (user1 != null ? user1.getName() : "Not found"));
        System.out.println("   ⏱️  Time: " + time1 + "ms (Database query)");
        
        System.out.println("\nSTEP 2: Second call to getUserById(" + userId + ")");
        System.out.println("   → @Cacheable checks cache: HIT");
        System.out.println("   → Method does NOT execute");
        System.out.println("   → Result returned from cache");
        
        long start2 = System.currentTimeMillis();
        User user2 = cachingExample.getUserById(userId);
        long time2 = System.currentTimeMillis() - start2;
        
        System.out.println("   ✅ Result: " + (user2 != null ? user2.getName() : "Not found"));
        System.out.println("   ⏱️  Time: " + time2 + "ms (From cache)");
        
        System.out.println("\nSTEP 3: Performance Improvement");
        long improvement = time1 - time2;
        System.out.println("   📊 Improvement: " + improvement + "ms faster");
        System.out.println("   📈 Speed increase: " + (time1 > 0 ? (time1 * 100 / time2) : 0) + "%");
        
        System.out.println("\n💡 KEY TAKEAWAYS:");
        System.out.println("   • First call: Hits database (slower)");
        System.out.println("   • Subsequent calls: Uses cache (much faster)");
        System.out.println("   • Cache key: userId");
        System.out.println("   • Cache name: 'users'");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * STRATEGY 4: Complete Async Processing Demonstration
     */
    public void demonstrateAsyncStepByStep() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⚡ STRATEGY 4: ASYNC PROCESSING - STEP BY STEP DEMONSTRATION");
        System.out.println("=".repeat(60));
        
        System.out.println("\nSTEP 1: Main thread before async call");
        System.out.println("   → Thread: " + Thread.currentThread().getName());
        System.out.println("   → Status: Ready to process");
        
        System.out.println("\nSTEP 2: Calling async method");
        System.out.println("   → Method: sendEmailAsync()");
        System.out.println("   → Annotation: @Async");
        System.out.println("   → Behavior: Returns immediately, doesn't wait");
        
        long start = System.currentTimeMillis();
        CompletableFuture<String> future = asyncExample.processDataAsync("test@example.com");
        long duration = System.currentTimeMillis() - start;
        
        System.out.println("   ✅ Async call returned in: " + duration + "ms");
        System.out.println("   ✅ Main thread continues immediately");
        System.out.println("   → Thread: " + Thread.currentThread().getName());
        
        System.out.println("\nSTEP 3: Background processing");
        System.out.println("   → Email sending happens in separate thread");
        System.out.println("   → Thread pool: async-1, async-2, etc.");
        System.out.println("   → Main thread is free to handle other requests");
        
        System.out.println("\nSTEP 4: Getting result (optional)");
        future.thenAccept(result -> {
            System.out.println("   ✅ Async task completed: " + result);
            System.out.println("   → Thread: " + Thread.currentThread().getName());
        });
        
        System.out.println("\n💡 KEY TAKEAWAYS:");
        System.out.println("   • @Async executes method in separate thread");
        System.out.println("   • Main thread doesn't block");
        System.out.println("   • Perfect for I/O operations (emails, APIs, etc.)");
        System.out.println("   • Returns CompletableFuture for getting results later");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * STRATEGY 5: Complete Pagination Demonstration
     */
    public void demonstratePaginationStepByStep() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📄 STRATEGY 5: PAGINATION - STEP BY STEP DEMONSTRATION");
        System.out.println("=".repeat(60));
        
        System.out.println("\nSTEP 1: Without Pagination (BAD)");
        System.out.println("   → Loads ALL records into memory");
        System.out.println("   → Problem: If you have 10,000 users, all loaded");
        System.out.println("   → Memory: High usage");
        System.out.println("   → Performance: Slow");
        
        System.out.println("\nSTEP 2: With Pagination (GOOD)");
        System.out.println("   → Request: Page 0, Size 10");
        System.out.println("   → Only loads 10 records");
        
        try {
            long start = System.currentTimeMillis();
            var page = queryOptimization.getUsersPaginated(0, 10);
            long duration = System.currentTimeMillis() - start;
            
            System.out.println("   ✅ Loaded: " + page.getContent().size() + " records");
            System.out.println("   ✅ Total available: " + page.getTotalElements() + " records");
            System.out.println("   ✅ Total pages: " + page.getTotalPages());
            System.out.println("   ⏱️  Time: " + duration + "ms");
            
            if (page.getTotalElements() == 0) {
                System.out.println("   ⚠️  No data found. DataInitializer should create test users on startup.");
            } else {
                System.out.println("   📋 Sample users:");
                page.getContent().stream()
                    .limit(3)
                    .forEach(user -> System.out.println("      - " + user.getName()));
            }
        } catch (Exception e) {
            System.out.println("   ❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\nSTEP 3: Memory Efficiency");
        System.out.println("   → Memory used: Only for 10 records");
        System.out.println("   → Network transfer: Minimal");
        System.out.println("   → Database load: Optimized query");
        
        System.out.println("\nSTEP 4: Try Different Pages");
        System.out.println("   → Page 0: First 10 records");
        System.out.println("   → Page 1: Next 10 records");
        System.out.println("   → Each page loads independently");
        
        System.out.println("\n💡 KEY TAKEAWAYS:");
        System.out.println("   • Use Pageable for large datasets");
        System.out.println("   • Load only what you need");
        System.out.println("   • Reduces memory usage");
        System.out.println("   • Improves response times");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * STRATEGY 1: Connection Pool Demonstration
     */
    public void demonstrateConnectionPoolStepByStep() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔌 STRATEGY 1: CONNECTION POOLING - STEP BY STEP DEMONSTRATION");
        System.out.println("=".repeat(60));
        
        System.out.println("\nSTEP 1: Without Connection Pool (BAD)");
        System.out.println("   → Each request creates new connection");
        System.out.println("   → Connection overhead: High");
        System.out.println("   → Performance: Slow");
        System.out.println("   → Resource usage: Wasteful");
        
        System.out.println("\nSTEP 2: With Connection Pool (GOOD)");
        System.out.println("   → Pool maintains ready connections");
        System.out.println("   → Connections are reused");
        System.out.println("   → Configuration:");
        System.out.println("     • Maximum pool size: 20");
        System.out.println("     • Minimum idle: 5");
        System.out.println("     • Connection timeout: 30s");
        
        try {
            System.out.println("\nSTEP 3: Getting Connection from Pool");
            connectionPoolDemo.useConnectionFromPool();
            
            System.out.println("\nSTEP 4: Multiple Connections");
            connectionPoolDemo.useMultipleConnections();
            
        } catch (Exception e) {
            System.out.println("   ❌ Error: " + e.getMessage());
        }
        
        System.out.println("\n💡 KEY TAKEAWAYS:");
        System.out.println("   • Connections are reused, not created each time");
        System.out.println("   • Pool size configured in application.properties");
        System.out.println("   • HikariCP is default and very fast");
        System.out.println("   • Monitor via Actuator endpoints");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * STRATEGY 2: Lazy Loading Demonstration
     */
    public void demonstrateLazyLoadingStepByStep() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⚡ STRATEGY 2: LAZY LOADING - STEP BY STEP DEMONSTRATION");
        System.out.println("=".repeat(60));
        
        System.out.println("\nSTEP 1: EAGER Loading Problem (BAD)");
        System.out.println("   → @OneToMany(fetch = FetchType.EAGER)");
        System.out.println("   → Loading 100 users:");
        System.out.println("     • Query 1: SELECT * FROM users (100 users)");
        System.out.println("     • Query 2: SELECT * FROM orders WHERE user_id = 1");
        System.out.println("     • Query 3: SELECT * FROM orders WHERE user_id = 2");
        System.out.println("     • ... (100 more queries)");
        System.out.println("   → Total: 101 queries (N+1 problem)");
        System.out.println("   → Performance: Very slow");
        
        System.out.println("\nSTEP 2: LAZY Loading Solution (GOOD)");
        System.out.println("   → @OneToMany(fetch = FetchType.LAZY)");
        System.out.println("   → Loading 100 users:");
        System.out.println("     • Query 1: SELECT * FROM users (100 users)");
        System.out.println("     • Orders loaded only when accessed");
        System.out.println("   → Total: 1 query initially");
        System.out.println("   → Performance: Much faster");
        
        System.out.println("\nSTEP 3: Demonstrating Lazy Loading");
        lazyLoadingDemo.demonstrateLazyLoading();
        
        System.out.println("\nSTEP 4: When You Need Related Data");
        System.out.println("   → Use JOIN FETCH in query");
        System.out.println("   → Loads user and orders in single query");
        lazyLoadingDemo.loadUserWithOrdersEfficiently();
        
        System.out.println("\n💡 KEY TAKEAWAYS:");
        System.out.println("   • Always use FetchType.LAZY for relationships");
        System.out.println("   • Prevents N+1 query problem");
        System.out.println("   • Use JOIN FETCH when you need related data");
        System.out.println("   • Better performance with large datasets");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Complete demonstration of all strategies
     */
    public void demonstrateAllStrategies() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 COMPLETE PERFORMANCE OPTIMIZATION DEMONSTRATION");
        System.out.println("=".repeat(60));
        
        System.out.println("\n📋 Demonstrating 10 Performance Optimization Strategies:");
        System.out.println("   1. Connection Pooling");
        System.out.println("   2. Lazy Loading");
        System.out.println("   3. Caching");
        System.out.println("   4. Async Processing");
        System.out.println("   5. Query Optimization");
        System.out.println("   6. HTTP Compression");
        System.out.println("   7. Actuator Monitoring");
        System.out.println("   8. Thread Pool Configuration");
        System.out.println("   9. HTTP Connection Pooling");
        System.out.println("   10. JVM Optimizations");
        
        // Create a test user first
        User testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser = userRepository.save(testUser);
        
        System.out.println("\n" + "-".repeat(60));
        demonstrateConnectionPoolStepByStep();
        
        System.out.println("\n" + "-".repeat(60));
        demonstrateLazyLoadingStepByStep();
        
        System.out.println("\n" + "-".repeat(60));
        demonstrateCachingStepByStep(testUser.getId());
        
        System.out.println("\n" + "-".repeat(60));
        demonstrateAsyncStepByStep();
        
        System.out.println("\n" + "-".repeat(60));
        demonstratePaginationStepByStep();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ ALL DEMONSTRATIONS COMPLETED!");
        System.out.println("=".repeat(60) + "\n");
    }
}


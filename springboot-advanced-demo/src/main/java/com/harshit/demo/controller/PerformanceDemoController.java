package com.harshit.demo.controller;

import com.harshit.demo.entity.User;
import com.harshit.demo.performance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for demonstrating all performance optimization strategies
 * 
 * You can call these endpoints to see each strategy working in real-time
 */
@RestController
@RequestMapping("/api/demo")
public class PerformanceDemoController {
    
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
    private PerformanceOptimizedService performanceService;
    
    /**
     * STRATEGY 3: CACHING DEMONSTRATION
     * 
     * Call this endpoint multiple times to see caching in action
     * First call: Hits database (slower)
     * Subsequent calls: Returns from cache (much faster)
     * 
     * URL: GET /api/demo/cache/{id}
     */
    @GetMapping("/cache/{id}")
    public ResponseEntity<Map<String, Object>> demonstrateCaching(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("\n=========================================");
        System.out.println("📦 CACHING DEMONSTRATION");
        System.out.println("=========================================");
        System.out.println("Requesting user ID: " + id);
        
        long startTime = System.currentTimeMillis();
        var user = cachingExample.getUserById(id);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        response.put("strategy", "Caching with @Cacheable");
        response.put("userId", id);
        response.put("userName", user != null ? user.getName() : "Not found");
        response.put("duration", duration + "ms");
        response.put("explanation", duration < 10 ? 
            "✅ FAST - Data returned from CACHE (no database query)" : 
            "⏱️ SLOW - Data fetched from DATABASE (first time)");
        response.put("tip", "Call this endpoint again with same ID to see cache in action!");
        
        System.out.println("Duration: " + duration + "ms");
        System.out.println("=========================================\n");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * STRATEGY 4: ASYNC PROCESSING DEMONSTRATION
     * 
     * Shows how async methods don't block the main thread
     * URL: POST /api/demo/async
     */
    @PostMapping("/async")
    public ResponseEntity<Map<String, Object>> demonstrateAsync(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("\n=========================================");
        System.out.println("⚡ ASYNC PROCESSING DEMONSTRATION");
        System.out.println("=========================================");
        System.out.println("Main Thread: " + Thread.currentThread().getName());
        System.out.println("Starting async email sending...");
        
        long startTime = System.currentTimeMillis();
        
        // This returns immediately, doesn't wait for email to send
        CompletableFuture<String> future = asyncExample.processDataAsync(email);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        response.put("strategy", "Asynchronous Processing with @Async");
        response.put("mainThread", Thread.currentThread().getName());
        response.put("asyncCallDuration", duration + "ms");
        response.put("explanation", "✅ Async call returned immediately (non-blocking)");
        response.put("note", "Email is being sent in background thread. Check console for async thread name.");
        response.put("tip", "Main thread can continue processing other requests!");
        
        System.out.println("Async call returned in: " + duration + "ms (non-blocking)");
        System.out.println("Main thread continues immediately!");
        System.out.println("=========================================\n");
        
        // Wait for async to complete (optional, for demo)
        future.thenAccept(result -> {
            System.out.println("✅ Async task completed: " + result);
        });
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * STRATEGY 5: PAGINATION DEMONSTRATION
     * 
     * Shows how pagination loads only requested page
     * URL: GET /api/demo/pagination?page=0&size=10
     */
    @GetMapping("/pagination")
    public ResponseEntity<Map<String, Object>> demonstratePagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("\n=========================================");
        System.out.println("📄 PAGINATION DEMONSTRATION");
        System.out.println("=========================================");
        System.out.println("Requesting page: " + page + ", size: " + size);
        
        try {
            long startTime = System.currentTimeMillis();
            var pageResult = queryOptimization.getUsersPaginated(page, size);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // Prepare user data for response
            List<Map<String, Object>> users = new ArrayList<>();
            for (User user : pageResult.getContent()) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("name", user.getName());
                userData.put("email", user.getEmail());
                users.add(userData);
            }
            
            response.put("strategy", "Database Query Optimization - Pagination");
            response.put("page", page);
            response.put("size", size);
            response.put("totalElements", pageResult.getTotalElements());
            response.put("totalPages", pageResult.getTotalPages());
            response.put("currentPageSize", pageResult.getContent().size());
            response.put("users", users);
            response.put("duration", duration + "ms");
            response.put("explanation", "✅ Only loaded " + pageResult.getContent().size() + 
                " records out of " + pageResult.getTotalElements() + " total");
            response.put("benefit", "Memory efficient - doesn't load all records into memory");
            response.put("tip", "Try different page numbers: ?page=1&size=5");
            
            if (pageResult.getTotalElements() == 0) {
                response.put("warning", "No data found. Make sure DataInitializer has created test users.");
            }
            
            System.out.println("Loaded: " + pageResult.getContent().size() + " users");
            System.out.println("Total: " + pageResult.getTotalElements() + " users");
            System.out.println("Duration: " + duration + "ms");
            
            // Show first few user names
            if (!pageResult.getContent().isEmpty()) {
                System.out.println("Sample users on this page:");
                pageResult.getContent().stream()
                    .limit(5)
                    .forEach(user -> System.out.println("   - " + user.getName() + " (ID: " + user.getId() + ")"));
            }
            
            System.out.println("=========================================\n");
            
        } catch (Exception e) {
            response.put("error", "Pagination failed: " + e.getMessage());
            response.put("strategy", "Database Query Optimization - Pagination");
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * STRATEGY 1: CONNECTION POOL DEMONSTRATION
     * 
     * Shows connection pooling in action
     * URL: GET /api/demo/connection-pool
     */
    @GetMapping("/connection-pool")
    public ResponseEntity<Map<String, Object>> demonstrateConnectionPool() {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("\n=========================================");
        System.out.println("🔌 CONNECTION POOL DEMONSTRATION");
        System.out.println("=========================================");
        
        try {
            connectionPoolDemo.useConnectionFromPool();
            connectionPoolDemo.useMultipleConnections();
            
            response.put("strategy", "Connection Pooling (HikariCP)");
            response.put("explanation", "✅ Connections are reused from pool, not created each time");
            response.put("configuration", "Check application.properties for pool settings");
            response.put("poolSize", "Maximum: 20, Minimum Idle: 5");
            response.put("benefit", "Significantly reduces connection overhead");
            response.put("monitor", "Visit: http://localhost:8080/actuator/metrics/hikari.connections.active");
            
            System.out.println("=========================================\n");
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * STRATEGY 2: LAZY LOADING DEMONSTRATION
     * 
     * Shows lazy loading benefits
     * URL: GET /api/demo/lazy-loading
     */
    @GetMapping("/lazy-loading")
    public ResponseEntity<Map<String, Object>> demonstrateLazyLoading() {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("\n=========================================");
        System.out.println("⚡ LAZY LOADING DEMONSTRATION");
        System.out.println("=========================================");
        
        lazyLoadingDemo.demonstrateLazyLoading();
        lazyLoadingDemo.loadUserWithOrdersEfficiently();
        
        response.put("strategy", "Lazy Loading (FetchType.LAZY)");
        response.put("explanation", "✅ Related entities loaded only when accessed");
        response.put("benefit", "Prevents N+1 query problem");
        response.put("configuration", "See User.java - @OneToMany with FetchType.LAZY");
        response.put("tip", "Use JOIN FETCH when you need related data");
        
        System.out.println("=========================================\n");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * COMPLETE DEMONSTRATION - All strategies at once
     * 
     * URL: GET /api/demo/all
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> demonstrateAll() {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🚀 COMPLETE PERFORMANCE OPTIMIZATION DEMO");
        System.out.println("=".repeat(50) + "\n");
        
        // Run all demonstrations
        performanceService.demonstratePerformance();
        
        response.put("message", "All performance strategies demonstrated!");
        response.put("strategies", new String[]{
            "1. Connection Pooling",
            "2. Lazy Loading",
            "3. Caching",
            "4. Async Processing",
            "5. Query Optimization"
        });
        response.put("checkConsole", "See console output for detailed demonstrations");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * CACHE OPERATIONS DEMONSTRATION
     * 
     * Shows @CachePut and @CacheEvict
     * URL: GET /api/demo/cache-operations/{id}
     */
    @GetMapping("/cache-operations/{id}")
    public ResponseEntity<Map<String, Object>> demonstrateCacheOperations(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("\n=========================================");
        System.out.println("🔄 CACHE OPERATIONS DEMONSTRATION");
        System.out.println("=========================================");
        
        // Get from cache
        System.out.println("1. Getting from cache...");
        var user1 = cachingExample.getUserById(id);
        
        // Update and cache
        if (user1 != null) {
            System.out.println("2. Updating user (updates cache with @CachePut)...");
            user1.setName("Updated Name");
            cachingExample.updateUser(user1);
        }
        
        // Get again (should be from updated cache)
        System.out.println("3. Getting again (should be from updated cache)...");
        var user2 = cachingExample.getUserById(id);
        
        // Evict from cache
        System.out.println("4. Evicting from cache...");
        cachingExample.deleteUser(id);
        
        response.put("strategy", "Cache Operations (@CachePut, @CacheEvict)");
        response.put("operations", new String[]{
            "1. @Cacheable - Caches result",
            "2. @CachePut - Updates cache",
            "3. @CacheEvict - Removes from cache"
        });
        
        System.out.println("=========================================\n");
        
        return ResponseEntity.ok(response);
    }
}


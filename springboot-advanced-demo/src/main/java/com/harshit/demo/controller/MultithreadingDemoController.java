package com.harshit.demo.controller;

import com.harshit.demo.multithreading.AsyncExceptionHandlingService;
import com.harshit.demo.multithreading.DeadlockExplanationDemo;
import com.harshit.demo.multithreading.MultithreadingScenariosDemo;
import com.harshit.demo.multithreading.ThreadSafeCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for demonstrating multithreading scenarios
 */
@RestController
@RequestMapping("/api/multithreading")
public class MultithreadingDemoController {
    
    @Autowired
    private MultithreadingScenariosDemo scenariosDemo;
    
    @Autowired
    private ThreadSafeCacheService cacheService;
    
    @Autowired
    private AsyncExceptionHandlingService asyncExceptionService;
    
    @Autowired
    private DeadlockExplanationDemo deadlockExplanation;
    
    /**
     * Run all multithreading demonstrations
     */
    @PostMapping("/demo/all")
    public ResponseEntity<Map<String, String>> runAllDemonstrations() {
        scenariosDemo.runAllDemonstrations();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "All demonstrations completed. Check console for output.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate race conditions
     */
    @PostMapping("/demo/race-condition")
    public ResponseEntity<Map<String, String>> demonstrateRaceCondition() {
        scenariosDemo.demonstrateRaceCondition();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Race condition demonstration completed. Check console.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate deadlock prevention
     */
    @PostMapping("/demo/deadlock-prevention")
    public ResponseEntity<Map<String, String>> demonstrateDeadlockPrevention() {
        scenariosDemo.demonstrateDeadlockPrevention();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Deadlock prevention demonstration completed.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate producer-consumer pattern
     */
    @PostMapping("/demo/producer-consumer")
    public ResponseEntity<Map<String, String>> demonstrateProducerConsumer(
            @RequestParam(defaultValue = "20") int taskCount) throws InterruptedException {
        scenariosDemo.startConsumers();
        scenariosDemo.produceTasks(taskCount);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Produced " + taskCount + " tasks. Consumers are processing.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate rate limiting
     */
    @PostMapping("/demo/rate-limiting")
    public ResponseEntity<Map<String, String>> demonstrateRateLimiting() {
        scenariosDemo.demonstrateRateLimiting();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Rate limiting demonstration started. Check console.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate memory visibility
     */
    @PostMapping("/demo/memory-visibility")
    public ResponseEntity<Map<String, String>> demonstrateMemoryVisibility() {
        scenariosDemo.demonstrateMemoryVisibility();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Memory visibility demonstration completed.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate read-write lock
     */
    @PostMapping("/demo/read-write-lock")
    public ResponseEntity<Map<String, String>> demonstrateReadWriteLock() {
        scenariosDemo.demonstrateReadWriteLock();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Read-write lock demonstration completed.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate CompletableFuture
     */
    @PostMapping("/demo/completable-future")
    public ResponseEntity<Map<String, String>> demonstrateCompletableFuture() {
        scenariosDemo.demonstrateCompletableFuture();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "CompletableFuture demonstration started. Check console.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate CountDownLatch
     */
    @PostMapping("/demo/countdown-latch")
    public ResponseEntity<Map<String, String>> demonstrateCountDownLatch() {
        scenariosDemo.demonstrateCountDownLatch();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "CountDownLatch demonstration completed.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate CyclicBarrier
     */
    @PostMapping("/demo/cyclic-barrier")
    public ResponseEntity<Map<String, String>> demonstrateCyclicBarrier() {
        scenariosDemo.demonstrateCyclicBarrier();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "CyclicBarrier demonstration completed.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate interruption handling
     */
    @PostMapping("/demo/interruption-handling")
    public ResponseEntity<Map<String, String>> demonstrateInterruptionHandling() {
        scenariosDemo.demonstrateInterruptionHandling();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Interruption handling demonstration completed.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Cache operations
     */
    @PostMapping("/cache/{key}")
    public ResponseEntity<Map<String, String>> putCache(
            @PathVariable String key,
            @RequestParam String value) {
        cacheService.putCachedValue(key, value);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Value cached for key: " + key);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cache/{key}")
    public ResponseEntity<Map<String, Object>> getCache(@PathVariable String key) {
        String value = cacheService.getCachedValue(key);
        
        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("value", value);
        response.put("found", value != null);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Async exception handling demonstration
     */
    @PostMapping("/async/exception-handling")
    public ResponseEntity<Map<String, String>> demonstrateAsyncExceptionHandling() {
        asyncExceptionService.demonstrateAsyncExceptionHandling();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Async exception handling demonstration started. Check console.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Async method with exception handling
     */
    @PostMapping("/async/process")
    public ResponseEntity<Map<String, Object>> processAsync(@RequestParam String input) {
        CompletableFuture<String> future = asyncExceptionService.asyncMethodWithExceptionHandling(input);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "processing");
        response.put("message", "Processing asynchronously. Use future.get() to get result.");
        
        future.thenAccept(result -> {
            System.out.println("Async processing completed: " + result);
        }).exceptionally(ex -> {
            System.err.println("Async processing failed: " + ex.getMessage());
            return null;
        });
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Explain what lock1 and lock2 are and how they fix deadlocks
     */
    @GetMapping("/explain/deadlock")
    public ResponseEntity<Map<String, String>> explainDeadlock() {
        deadlockExplanation.runAllExplanations();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Deadlock explanation completed. Check console for detailed output.");
        response.put("documentation", "See DEADLOCK_EXPLANATION.md for complete guide");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate deadlock solution (safe - no actual deadlock)
     */
    @PostMapping("/demo/deadlock-solution")
    public ResponseEntity<Map<String, String>> demonstrateDeadlockSolution() {
        deadlockExplanation.demonstrateDeadlockSolution();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Deadlock solution demonstration completed. Check console.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Demonstrate race condition fix with locks
     */
    @PostMapping("/demo/race-condition-fix")
    public ResponseEntity<Map<String, String>> demonstrateRaceConditionFix() {
        deadlockExplanation.demonstrateRaceConditionFix();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Race condition fix demonstration completed. Check console.");
        return ResponseEntity.ok(response);
    }
}


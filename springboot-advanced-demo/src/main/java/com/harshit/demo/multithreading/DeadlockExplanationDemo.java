package com.harshit.demo.multithreading;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Visual demonstration of deadlock problem and solution
 * Shows how lock1 and lock2 work and how they fix thread issues
 */
@Service
public class DeadlockExplanationDemo {
    
    // ========== WHAT ARE lock1 and lock2? ==========
    // These are synchronization objects (just regular Java Objects)
    // They act as "keys" that threads must acquire before accessing resources
    private final Object lock1 = new Object();  // Lock for Resource 1
    private final Object lock2 = new Object();  // Lock for Resource 2
    
    // Shared resources that need protection
    private int resource1 = 100;  // Protected by lock1
    private int resource2 = 200;  // Protected by lock2
    
    // ========== PROBLEM: DEADLOCK SCENARIO ==========
    
    /**
     * ❌ PROBLEM: Different lock order causes deadlock
     * Thread 1: lock1 → lock2
     * Thread 2: lock2 → lock1
     * Result: DEADLOCK!
     */
    public void demonstrateDeadlock() {
        System.out.println("\n=== DEADLOCK DEMONSTRATION ===");
        System.out.println("Thread 1 will acquire: lock1 → lock2");
        System.out.println("Thread 2 will acquire: lock2 → lock1");
        System.out.println("This will cause DEADLOCK!\n");
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Thread 1: Tries to get lock1, then lock2
        executor.submit(() -> {
            synchronized (lock1) {  // ✅ Thread 1 gets lock1
                System.out.println("Thread 1: ✅ Acquired lock1");
                try {
                    Thread.sleep(100);  // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                System.out.println("Thread 1: ⏸️ Waiting for lock2...");
                synchronized (lock2) {  // ❌ Thread 1 waits for lock2 (but Thread 2 has it!)
                    System.out.println("Thread 1: ✅ Acquired lock2 (this won't print - DEADLOCK!)");
                }
            }
        });
        
        // Thread 2: Tries to get lock2, then lock1
        executor.submit(() -> {
            synchronized (lock2) {  // ✅ Thread 2 gets lock2
                System.out.println("Thread 2: ✅ Acquired lock2");
                try {
                    Thread.sleep(100);  // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                System.out.println("Thread 2: ⏸️ Waiting for lock1...");
                synchronized (lock1) {  // ❌ Thread 2 waits for lock1 (but Thread 1 has it!)
                    System.out.println("Thread 2: ✅ Acquired lock1 (this won't print - DEADLOCK!)");
                }
            }
        });
        
        try {
            executor.shutdown();
            // Wait a bit to see the deadlock
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                System.out.println("\n⚠️ DEADLOCK DETECTED! Threads are stuck waiting for each other.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ========== SOLUTION: SAME LOCK ORDER ==========
    
    /**
     * ✅ SOLUTION: Always acquire locks in same order
     * Thread 1: lock1 → lock2
     * Thread 2: lock1 → lock2 (SAME ORDER!)
     * Result: No deadlock!
     */
    public void demonstrateDeadlockSolution() {
        System.out.println("\n=== DEADLOCK SOLUTION DEMONSTRATION ===");
        System.out.println("Thread 1 will acquire: lock1 → lock2");
        System.out.println("Thread 2 will acquire: lock1 → lock2 (SAME ORDER!)");
        System.out.println("This prevents DEADLOCK!\n");
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Thread 1: Gets lock1, then lock2
        executor.submit(() -> {
            synchronized (lock1) {  // ✅ Step 1: Always lock1 first
                System.out.println("Thread 1: ✅ Acquired lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                synchronized (lock2) {  // ✅ Step 2: Then lock2
                    System.out.println("Thread 1: ✅ Acquired lock2");
                    // Access both resources safely
                    resource1 += 10;
                    resource2 += 20;
                    System.out.println("Thread 1: ✅ Completed work with both resources");
                }  // lock2 released
            }  // lock1 released
        });
        
        // Thread 2: Gets lock1, then lock2 (SAME ORDER!)
        executor.submit(() -> {
            synchronized (lock1) {  // ✅ Step 1: Same order - lock1 first
                System.out.println("Thread 2: ✅ Acquired lock1 (waited for Thread 1 to finish)");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                synchronized (lock2) {  // ✅ Step 2: Then lock2
                    System.out.println("Thread 2: ✅ Acquired lock2");
                    // Access both resources safely
                    resource1 += 10;
                    resource2 += 20;
                    System.out.println("Thread 2: ✅ Completed work with both resources");
                }  // lock2 released
            }  // lock1 released
        });
        
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("\n✅ No deadlock! Both threads completed successfully.");
            System.out.println("Final resource1: " + resource1);
            System.out.println("Final resource2: " + resource2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ========== HOW LOCKS FIX THREAD ISSUES ==========
    
    /**
     * Demonstrates how locks prevent race conditions
     */
    public void demonstrateRaceConditionFix() {
        System.out.println("\n=== HOW LOCKS FIX RACE CONDITIONS ===");
        
        int unsafeCounter = 0;
        int safeCounter = 0;
        final Object counterLock = new Object();
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // ❌ Without lock: Race condition
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                unsafeCounter++;  // Not thread-safe!
            });
        }
        
        // ✅ With lock: Thread-safe
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                synchronized (counterLock) {  // Lock ensures only one thread at a time
                    safeCounter++;  // Thread-safe!
                }
            });
        }
        
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            
            System.out.println("Unsafe counter (race condition): " + unsafeCounter + 
                " (should be 1000, but might be less due to race condition)");
            System.out.println("Safe counter (with lock): " + safeCounter + 
                " (correctly 1000)");
            System.out.println("\n✅ Lock prevents race condition by ensuring mutual exclusion!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ========== EXPLANATION METHODS ==========
    
    /**
     * Explains what lock1 and lock2 are
     */
    public void explainWhatAreLocks() {
        System.out.println("\n=== WHAT ARE lock1 AND lock2? ===");
        System.out.println("lock1 and lock2 are synchronization objects:");
        System.out.println("  1. They are just regular Java Objects (new Object())");
        System.out.println("  2. They don't store any data");
        System.out.println("  3. They act as 'keys' or 'tokens'");
        System.out.println("  4. Only ONE thread can hold a lock at a time");
        System.out.println("  5. Other threads must WAIT until the lock is released");
        System.out.println("\nThink of them like bathroom keys:");
        System.out.println("  - Only one person can have the key at a time");
        System.out.println("  - Others must wait for the key to be returned");
        System.out.println("  - This prevents multiple people using the bathroom simultaneously");
    }
    
    /**
     * Explains how locks fix thread issues
     */
    public void explainHowLocksFixIssues() {
        System.out.println("\n=== HOW DO LOCKS FIX THREAD ISSUES? ===");
        System.out.println("Locks fix three main problems:");
        System.out.println("\n1. RACE CONDITIONS:");
        System.out.println("   Problem: Multiple threads modify shared data simultaneously");
        System.out.println("   Solution: Lock ensures only one thread accesses data at a time");
        System.out.println("   Result: Data remains consistent");
        System.out.println("\n2. DATA CORRUPTION:");
        System.out.println("   Problem: Concurrent reads/writes cause inconsistent state");
        System.out.println("   Solution: Lock provides mutual exclusion");
        System.out.println("   Result: Changes are atomic and visible");
        System.out.println("\n3. DEADLOCKS:");
        System.out.println("   Problem: Threads wait for each other in circular dependency");
        System.out.println("   Solution: Always acquire locks in SAME ORDER");
        System.out.println("   Result: No circular wait, threads execute sequentially");
    }
    
    /**
     * Run all explanations
     */
    public void runAllExplanations() {
        explainWhatAreLocks();
        explainHowLocksFixIssues();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Running demonstrations...");
        System.out.println("=".repeat(60));
        
        try {
            demonstrateRaceConditionFix();
            Thread.sleep(2000);
            
            demonstrateDeadlockSolution();
            Thread.sleep(2000);
            
            System.out.println("\n⚠️ Warning: Next demonstration will show deadlock!");
            System.out.println("It may hang - this is intentional to show the problem.");
            Thread.sleep(2000);
            
            // Uncomment to see deadlock (will hang)
            // demonstrateDeadlock();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


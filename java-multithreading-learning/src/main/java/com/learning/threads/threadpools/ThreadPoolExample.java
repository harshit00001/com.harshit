package com.learning.threads.threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * THREAD POOLS - Interview Explanation:
 * 
 * Problem with creating threads manually:
 * - Creating/destroying threads is expensive
 * - Too many threads can exhaust system resources
 * - Difficult to manage thread lifecycle
 * 
 * Solution: Thread Pools
 * - Reuse existing threads instead of creating new ones
 * - Better resource management
 * - Control the number of concurrent threads
 * 
 * Java provides ExecutorService interface for thread pools
 */
public class ThreadPoolExample {

    public static void main(String[] args) {
        System.out.println("=== Fixed Thread Pool ===");
        demonstrateFixedThreadPool();
        
        System.out.println("\n=== Cached Thread Pool ===");
        demonstrateCachedThreadPool();
        
        System.out.println("\n=== Scheduled Thread Pool ===");
        demonstrateScheduledThreadPool();
        
        System.out.println("\n=== Single Thread Executor ===");
        demonstrateSingleThreadExecutor();
    }
    
    /**
     * Interview Point: Fixed Thread Pool
     * - Has a fixed number of threads
     * - If all threads are busy, tasks wait in queue
     * - Good for CPU-intensive tasks
     * - Threads are reused for multiple tasks
     */
    private static void demonstrateFixedThreadPool() {
        // Interview Point: Creates a pool with 3 threads
        // These 3 threads will be reused for all submitted tasks
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Interview Point: Submit 10 tasks, but only 3 threads will execute them
        // Other tasks will wait in the queue
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " executed by " + 
                    Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // Simulate work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        
        // Interview Point: Shutdown gracefully
        // shutdown() stops accepting new tasks but completes existing ones
        executor.shutdown();
        
        try {
            // Interview Point: Wait for all tasks to complete (with timeout)
            if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force shutdown if timeout
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
    
    /**
     * Interview Point: Cached Thread Pool
     * - Creates new threads as needed
     * - Reuses existing threads if available
     * - Terminates idle threads after 60 seconds
     * - Good for short-lived, I/O-intensive tasks
     * - Can create many threads if needed (be careful!)
     */
    private static void demonstrateCachedThreadPool() {
        ExecutorService executor = Executors.newCachedThreadPool();
        
        // Interview Point: Each task might get a new thread if all are busy
        // Or reuse an existing thread if one is available
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " executed by " + 
                    Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
    
    /**
     * Interview Point: Scheduled Thread Pool
     * - Executes tasks after a delay
     * - Can schedule tasks to run periodically
     * - Useful for timers, periodic cleanup, etc.
     */
    private static void demonstrateScheduledThreadPool() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        
        // Interview Point: Schedule a task to run after 2 seconds
        System.out.println("Scheduling task to run after 2 seconds...");
        scheduler.schedule(() -> {
            System.out.println("Delayed task executed!");
        }, 2, TimeUnit.SECONDS);
        
        // Interview Point: Schedule a task to run repeatedly
        // First execution after 1 second, then every 2 seconds
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Periodic task executed at: " + 
                System.currentTimeMillis());
        }, 1, 2, TimeUnit.SECONDS);
        
        // Let it run for a bit
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
    
    /**
     * Interview Point: Single Thread Executor
     * - Only one thread executes tasks sequentially
     * - Guarantees tasks execute in submission order
     * - Useful when you need sequential execution but want thread pool benefits
     */
    private static void demonstrateSingleThreadExecutor() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        // Interview Point: All tasks will execute one after another
        // Even though we submit them concurrently
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " executed by " + 
                    Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}

/**
 * Interview Point: Best Practices for Thread Pools
 * 
 * 1. Always shutdown ExecutorService when done
 *    - shutdown(): Graceful shutdown (waits for tasks to complete)
 *    - shutdownNow(): Force shutdown (interrupts running tasks)
 * 
 * 2. Use appropriate pool size:
 *    - CPU-bound tasks: Number of CPU cores
 *    - I/O-bound tasks: More threads (2 * CPU cores or more)
 * 
 * 3. Consider using ThreadPoolExecutor for more control:
 *    - Custom queue size
 *    - Custom rejection policies
 *    - Thread factory for custom thread creation
 */


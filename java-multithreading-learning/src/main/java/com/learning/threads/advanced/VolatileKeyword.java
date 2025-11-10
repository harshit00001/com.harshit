package com.learning.threads.advanced;

/**
 * VOLATILE KEYWORD - Interview Explanation:
 * 
 * Problem: In multi-threaded environments, variables might be cached
 * in CPU registers or local cache, causing visibility issues.
 * 
 * Solution: volatile keyword ensures:
 * 1. Visibility: Changes are immediately visible to all threads
 * 2. Prevents compiler optimizations that might reorder operations
 * 
 * Interview Point: volatile does NOT provide atomicity!
 * For atomic operations, use AtomicInteger, AtomicLong, etc.
 */
public class VolatileKeyword {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Problem: Without volatile ===");
        demonstrateVisibilityProblem();
        
        Thread.sleep(2000);
        
        System.out.println("\n=== Solution: With volatile ===");
        demonstrateVolatileSolution();
    }
    
    /**
     * Interview Point: Demonstrates visibility problem
     * Without volatile, the flag might be cached, and the loop
     * might run forever even after flag is set to true.
     */
    private static void demonstrateVisibilityProblem() throws InterruptedException {
        // Interview Point: Without volatile, this might be cached
        // The thread might not see the updated value
        SharedFlag flag = new SharedFlag();
        
        Thread worker = new Thread(() -> {
            System.out.println("Worker thread started, waiting for flag...");
            // Interview Point: This loop might run forever
            // because the thread might not see the updated flag value
            while (!flag.isRunning()) {
                // Busy waiting - might not see the updated flag
            }
            System.out.println("Worker thread detected flag change!");
        });
        
        worker.start();
        Thread.sleep(1000);
        
        System.out.println("Main thread setting flag to true...");
        flag.setRunning(true);
        
        // Wait a bit - worker might not see the change
        Thread.sleep(1000);
        
        if (worker.isAlive()) {
            System.out.println("⚠️ Worker thread still running - visibility issue!");
            worker.interrupt();
        }
    }
    
    /**
     * Interview Point: Demonstrates volatile solution
     * With volatile, changes are immediately visible to all threads.
     */
    private static void demonstrateVolatileSolution() throws InterruptedException {
        // Interview Point: Using volatile ensures visibility
        VolatileFlag flag = new VolatileFlag();
        
        Thread worker = new Thread(() -> {
            System.out.println("Worker thread started, waiting for flag...");
            // Interview Point: With volatile, this will see the updated value
            while (!flag.isRunning()) {
                // Busy waiting - will see the updated flag immediately
            }
            System.out.println("Worker thread detected flag change! ✓");
        });
        
        worker.start();
        Thread.sleep(1000);
        
        System.out.println("Main thread setting flag to true...");
        flag.setRunning(true);
        
        worker.join();
        System.out.println("Worker thread completed successfully!");
    }
}

/**
 * Interview Point: Without volatile - visibility issue
 * The running flag might be cached in CPU register or local cache.
 */
class SharedFlag {
    private boolean running = false; // Not volatile!
    
    public boolean isRunning() {
        return running; // Might read from cache
    }
    
    public void setRunning(boolean running) {
        this.running = running; // Might write to cache
    }
}

/**
 * Interview Point: With volatile - ensures visibility
 * Changes to volatile variables are:
 * 1. Immediately written to main memory (not just cache)
 * 2. Immediately visible to all threads
 * 3. Prevents compiler optimizations
 */
class VolatileFlag {
    // Interview Point: volatile keyword ensures visibility
    private volatile boolean running = false;
    
    public boolean isRunning() {
        // Interview Point: Always reads from main memory
        return running;
    }
    
    public void setRunning(boolean running) {
        // Interview Point: Always writes to main memory
        this.running = running;
    }
}

/**
 * Interview Point: Important Notes about volatile
 * 
 * 1. Visibility: Ensures all threads see the latest value
 * 2. NOT Atomicity: volatile does NOT make operations atomic
 *    - Example: count++ is NOT atomic even if count is volatile
 *    - Use AtomicInteger for atomic operations
 * 
 * 3. Use Cases:
 *    - Flags (like stop flags)
 *    - Status indicators
 *    - Simple state variables
 * 
 * 4. When NOT to use:
 *    - For complex operations requiring atomicity
 *    - When you need locking (use synchronized or Lock)
 */


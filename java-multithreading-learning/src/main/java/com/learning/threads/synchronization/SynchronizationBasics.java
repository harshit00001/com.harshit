package com.learning.threads.synchronization;

/**
 * SYNCHRONIZATION BASICS - Interview Explanation:
 * 
 * Problem: When multiple threads access shared resources simultaneously,
 * we can get race conditions and inconsistent data.
 * 
 * Solution: Synchronization ensures only one thread can access a critical
 * section at a time.
 * 
 * Two ways to synchronize:
 * 1. Synchronized methods
 * 2. Synchronized blocks
 * 
 * Interview Tip: Synchronization uses intrinsic locks (monitor locks)
 * associated with objects or classes.
 */
public class SynchronizationBasics {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Problem: Without Synchronization ===");
        demonstrateProblem();
        
        Thread.sleep(2000);
        
        System.out.println("\n=== Solution: With Synchronization ===");
        demonstrateSolution();
    }
    
    /**
     * Interview Point: This demonstrates the problem without synchronization
     * Multiple threads can read and write the same variable simultaneously,
     * leading to lost updates and incorrect results.
     */
    private static void demonstrateProblem() throws InterruptedException {
        Counter2 counter = new Counter2();
        
        // Create multiple threads that increment the counter
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.incrementUnsafe(); // Not synchronized
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.incrementUnsafe(); // Not synchronized
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        // Interview Point: Expected value is 2000, but we might get less
        // This happens because both threads read, modify, and write simultaneously
        System.out.println("Final count (unsafe): " + counter.getCount());
        System.out.println("Expected: 2000, but got: " + counter.getCount());
    }
    
    /**
     * Interview Point: This demonstrates the solution with synchronization
     * Only one thread can execute synchronized methods/blocks at a time,
     * ensuring thread-safe operations.
     */
    private static void demonstrateSolution() throws InterruptedException {
        Counter2 counter = new Counter2();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.incrementSafe(); // Synchronized
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.incrementSafe(); // Synchronized
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        // Interview Point: With synchronization, we always get the correct value
        System.out.println("Final count (safe): " + counter.getCount());
        System.out.println("Expected: 2000, got: " + counter.getCount() + " ✓");
    }
}

/**
 * Counter class to demonstrate synchronization
 */
class Counter2 {
    private int count = 0;
    
    /**
     * Interview Point: This method is NOT synchronized
     * Multiple threads can execute this simultaneously, causing race conditions.
     * 
     * What happens:
     * 1. Thread-1 reads count = 100
     * 2. Thread-2 reads count = 100 (before Thread-1 writes)
     * 3. Thread-1 increments and writes count = 101
     * 4. Thread-2 increments and writes count = 101 (lost update!)
     */
    public void incrementUnsafe() {
        count++; // This is not atomic! It's: read -> increment -> write
    }
    
    /**
     * Interview Point: Synchronized method
     * - Only one thread can execute this method at a time
     * - Uses the object's intrinsic lock (this)
     * - Other threads must wait until the lock is released
     * 
     * How it works:
     * - When a thread enters this method, it acquires the lock on 'this' object
     * - Other threads trying to enter wait until the lock is released
     * - Lock is automatically released when method exits
     */
    public synchronized void incrementSafe() {
        count++; // Now this is thread-safe
    }
    
    /**
     * Interview Point: Synchronized block
     * - More flexible than synchronized method
     * - Allows you to synchronize only specific parts of code
     * - Can use any object as a lock (not just 'this')
     * 
     * Syntax: synchronized(object) { ... }
     * - Thread must acquire lock on 'object' before entering the block
     */
    public void incrementWithBlock() {
        // Some non-critical code can run without synchronization
        System.out.println("Doing some work...");
        
        // Only this critical section needs synchronization
        synchronized (this) {
            count++;
        }
        
        // More non-critical code
        System.out.println("Done!");
    }
    
    /**
     * Interview Point: Static synchronized method
     * - Uses the class's lock, not the instance lock
     * - All instances of the class share the same lock
     * - Useful for class-level synchronization
     */
    public static synchronized void staticSynchronizedMethod() {
        // This uses Counter.class as the lock
        // All threads calling this method from any instance will be synchronized
    }
    
    public int getCount() {
        return count;
    }
}


package com.learning.threads.synchronization;

/**
 * COUNTER EXAMPLE - Demonstrating Race Conditions and Synchronization
 * 
 * This example demonstrates a classic problem in multithreading: race conditions.
 * When multiple threads access and modify a shared variable without proper
 * synchronization, the results become unpredictable. This is one of the most
 * important concepts to understand for multithreading interviews.
 * 
 * A race condition occurs when the outcome of a program depends on the relative
 * timing of events, such as when multiple threads access shared data and at least
 * one thread modifies it. Without synchronization, you might get incorrect results
 * because operations that should be atomic are actually composed of multiple steps
 * that can be interleaved between threads.
 */
public class CounterExample {
    
    public static void main(String[] args) throws InterruptedException {
        demonstrateRaceCondition();
        demonstrateSynchronizedSolution();
    }
    
    /**
     * DEMONSTRATING RACE CONDITION
     * 
     * This method shows what happens when multiple threads access a shared counter
     * without synchronization. The increment operation might seem like a single
     * operation, but it actually consists of three steps: read the current value,
     * add one to it, and write it back. When multiple threads do this simultaneously,
     * they might read the same value, both increment it, and both write back the same
     * incremented value, causing lost updates.
     */
    public static void demonstrateRaceCondition() throws InterruptedException {
        System.out.println("=== RACE CONDITION DEMONSTRATION ===");
        
        // Create a counter without synchronization
        Counter counter = new Counter();
        
        // Create two threads that both increment the counter
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment(); // Not synchronized - race condition!
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment(); // Not synchronized - race condition!
            }
        });
        
        thread1.start();
        thread2.start();
        
        // Wait for both threads to complete
        thread1.join();
        thread2.join();
        
        // Interview Point: Expected value is 2000, but we might get less
        // This is because of race conditions - some increments are lost
        System.out.println("Final count (without synchronization): " + counter.getCount());
        System.out.println("Expected: 2000");
        System.out.println("Note: The actual value is likely less than 2000 due to race conditions");
        System.out.println();
    }
    
    /**
     * DEMONSTRATING SYNCHRONIZED SOLUTION
     * 
     * This method shows how to fix the race condition using the synchronized keyword.
     * When a method is synchronized, only one thread can execute it at a time for a
     * given object. This ensures that the increment operation is atomic - it completes
     * entirely before another thread can start it.
     */
    public static void demonstrateSynchronizedSolution() throws InterruptedException {
        System.out.println("=== SYNCHRONIZED SOLUTION ===");
        
        // Create a synchronized counter
        SynchronizedCounter syncCounter = new SynchronizedCounter();
        
        // Create two threads that both increment the counter
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                syncCounter.increment(); // Synchronized - no race condition!
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                syncCounter.increment(); // Synchronized - no race condition!
            }
        });
        
        thread1.start();
        thread2.start();
        
        // Wait for both threads to complete
        thread1.join();
        thread2.join();
        
        // Interview Point: With synchronization, we always get the correct value
        System.out.println("Final count (with synchronization): " + syncCounter.getCount());
        System.out.println("Expected: 2000");
        System.out.println("Note: The value is always 2000 because synchronization prevents race conditions");
        System.out.println();
    }
}

/**
 * Counter without synchronization - demonstrates race condition
 * 
 * Interview Point: This class is NOT thread-safe. Multiple threads accessing
 * the increment() method simultaneously can cause lost updates because the
 * increment operation is not atomic.
 */
class Counter {
    private int count = 0;
    
    // Interview Point: This method is NOT synchronized
    // Multiple threads can execute this simultaneously
    // The operation count++ is actually: read count, add 1, write count
    // These three steps can be interleaved between threads
    public void increment() {
        count++; // This is NOT atomic!
    }
    
    public int getCount() {
        return count;
    }
}

/**
 * Counter with synchronization - thread-safe
 * 
 * Interview Point: This class IS thread-safe because the increment() method
 * is synchronized. Only one thread can execute this method at a time for a
 * given Counter object, ensuring that the increment operation is atomic.
 */
class SynchronizedCounter {
    private int count = 0;
    
    // Interview Point: synchronized keyword ensures only one thread executes this at a time
    // The lock is acquired on 'this' object (the SynchronizedCounter instance)
    // Other threads trying to call increment() must wait until the lock is released
    public synchronized void increment() {
        count++; // This is now atomic because of synchronization
    }
    
    public synchronized int getCount() {
        return count;
    }
}


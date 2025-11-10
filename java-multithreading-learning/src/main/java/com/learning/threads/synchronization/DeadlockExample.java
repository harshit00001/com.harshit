package com.learning.threads.synchronization;

/**
 * DEADLOCK - Interview Explanation:
 * 
 * Deadlock occurs when two or more threads are blocked forever,
 * waiting for each other to release locks.
 * 
 * Conditions for Deadlock (all must be true):
 * 1. Mutual Exclusion: Resources cannot be shared
 * 2. Hold and Wait: Thread holds one lock and waits for another
 * 3. No Preemption: Locks cannot be forcibly taken
 * 4. Circular Wait: Thread-1 waits for Thread-2, Thread-2 waits for Thread-1
 * 
 * Interview Tip: Always acquire locks in the same order to prevent deadlock!
 */
public class DeadlockExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Deadlock Demonstration ===\n");
        System.out.println("This will create a deadlock situation.");
        System.out.println("The program may appear to hang...\n");
        
        // Interview Point: Two shared resources (locks)
        Object lock1 = new Object();
        Object lock2 = new Object();
        
        // Thread-1: Acquires lock1, then tries to acquire lock2
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread-1: Acquired lock1");
                try {
                    Thread.sleep(100); // Simulate some work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println("Thread-1: Waiting for lock2...");
                synchronized (lock2) {
                    // Interview Point: This will never execute if deadlock occurs
                    System.out.println("Thread-1: Acquired both locks");
                }
            }
        });
        
        // Thread-2: Acquires lock2, then tries to acquire lock1
        // Interview Point: This is the OPPOSITE order - causes deadlock!
        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread-2: Acquired lock2");
                try {
                    Thread.sleep(100); // Simulate some work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println("Thread-2: Waiting for lock1...");
                synchronized (lock1) {
                    // Interview Point: This will never execute if deadlock occurs
                    System.out.println("Thread-2: Acquired both locks");
                }
            }
        });
        
        thread1.start();
        thread2.start();
        
        // Wait a bit to see the deadlock
        Thread.sleep(2000);
        
        // Interview Point: Check if threads are still alive (deadlocked)
        if (thread1.isAlive() && thread2.isAlive()) {
            System.out.println("\n⚠️ DEADLOCK DETECTED!");
            System.out.println("Thread-1 state: " + thread1.getState());
            System.out.println("Thread-2 state: " + thread2.getState());
            System.out.println("\nBoth threads are waiting for each other's locks.");
        }
    }
}

/**
 * SOLUTION: How to prevent deadlock
 * 
 * Interview Explanation:
 * Always acquire locks in the SAME ORDER across all threads.
 * This breaks the circular wait condition.
 */
class DeadlockPrevention {
    
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    /**
     * Interview Point: CORRECT approach - Always acquire locks in same order
     * Both methods acquire lock1 first, then lock2
     */
    public void method1() {
        synchronized (lock1) {  // Always acquire lock1 first
            synchronized (lock2) {  // Then lock2
                // Critical section
            }
        }
    }
    
    public void method2() {
        synchronized (lock1) {  // Same order: lock1 first
            synchronized (lock2) {  // Then lock2
                // Critical section
            }
        }
    }
    
    /**
     * Interview Point: ALTERNATIVE solution - Use timeout with tryLock()
     * This is available with ReentrantLock (not synchronized keyword)
     * If lock cannot be acquired within timeout, release held locks and retry
     */
}


package com.learning.threads.basics;

/**
 * THREAD LIFECYCLE - Interview Explanation:
 * 
 * A thread in Java goes through different states during its lifetime:
 * 
 * 1. NEW: Thread is created but not started yet
 * 2. RUNNABLE: Thread is ready to run (may be running or waiting for CPU)
 * 3. BLOCKED: Thread is waiting for a monitor lock (synchronized block)
 * 4. WAITING: Thread waits indefinitely for another thread action
 * 5. TIMED_WAITING: Thread waits for a specified time
 * 6. TERMINATED: Thread has completed execution
 * 
 * Interview Tip: You can check thread state using thread.getState()
 */
public class ThreadLifecycle {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Demonstrating Thread Lifecycle States ===\n");
        
        // Interview Point: NEW state - Thread is created but not started
        Thread thread = new Thread(new LifecycleDemo());
        System.out.println("After creation: " + thread.getState()); // NEW
        
        // Interview Point: RUNNABLE state - Thread is started and ready to run
        thread.start();
        System.out.println("After start(): " + thread.getState()); // RUNNABLE
        
        // Give thread some time to run
        Thread.sleep(100);
        System.out.println("While running: " + thread.getState()); // RUNNABLE
        
        // Wait for thread to complete
        thread.join();
        System.out.println("After completion: " + thread.getState()); // TERMINATED
        
        System.out.println("\n=== Demonstrating WAITING State ===");
        demonstrateWaitingState();
        
        System.out.println("\n=== Demonstrating TIMED_WAITING State ===");
        demonstrateTimedWaitingState();
        
        System.out.println("\n=== Demonstrating BLOCKED State ===");
        demonstrateBlockedState();
    }
    
    /**
     * Interview Point: WAITING state occurs when:
     * - thread.wait() is called (waiting for notify/notifyAll)
     * - thread.join() is called without timeout
     * - LockSupport.park() is called
     */
    private static void demonstrateWaitingState() throws InterruptedException {
        Object lock = new Object();
        Thread waitingThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("Thread entering WAITING state...");
                    lock.wait(); // Thread enters WAITING state
                    System.out.println("Thread resumed from WAITING state");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        waitingThread.start();
        Thread.sleep(50);
        System.out.println("Thread state: " + waitingThread.getState()); // WAITING
        
        // Wake up the waiting thread
        synchronized (lock) {
            lock.notify();
        }
        
        waitingThread.join();
    }
    
    /**
     * Interview Point: TIMED_WAITING state occurs when:
     * - Thread.sleep(time) is called
     * - thread.wait(timeout) is called
     * - thread.join(timeout) is called
     * - LockSupport.parkNanos() or parkUntil() is called
     */
    private static void demonstrateTimedWaitingState() throws InterruptedException {
        Thread timedThread = new Thread(() -> {
            try {
                System.out.println("Thread entering TIMED_WAITING state...");
                Thread.sleep(2000); // Thread enters TIMED_WAITING state
                System.out.println("Thread resumed from TIMED_WAITING state");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        timedThread.start();
        Thread.sleep(50);
        System.out.println("Thread state: " + timedThread.getState()); // TIMED_WAITING
        
        timedThread.join();
    }
    
    /**
     * Interview Point: BLOCKED state occurs when:
     * - Thread is trying to enter a synchronized block/method
     * - Another thread already holds the lock
     * - Thread waits until the lock becomes available
     */
    private static void demonstrateBlockedState() throws InterruptedException {
        Object sharedLock = new Object();
        
        // First thread holds the lock
        Thread thread1 = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("Thread-1 acquired lock");
                try {
                    Thread.sleep(2000); // Hold lock for 2 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread-1 releasing lock");
            }
        });
        
        // Second thread tries to acquire the same lock
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread-2 trying to acquire lock...");
            synchronized (sharedLock) {
                System.out.println("Thread-2 acquired lock");
            }
        });
        
        thread1.start();
        Thread.sleep(100); // Give thread1 time to acquire lock
        
        thread2.start();
        Thread.sleep(100); // Give thread2 time to try acquiring lock
        
        System.out.println("Thread-2 state: " + thread2.getState()); // BLOCKED
        
        thread1.join();
        thread2.join();
    }
}

/**
 * Simple Runnable to demonstrate thread execution
 */
class LifecycleDemo implements Runnable {
    @Override
    public void run() {
        // Interview Point: This code runs in RUNNABLE state
        System.out.println("Thread is executing...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread execution completed");
    }
}


package com.learning.threads.advanced;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

/**
 * REENTRANTLOCK - Interview Explanation:
 * 
 * ReentrantLock is an alternative to synchronized keyword with more features:
 * 
 * Advantages over synchronized:
 * 1. Try to acquire lock with timeout
 * 2. Interruptible lock acquisition
 * 3. Fair locking (FIFO order)
 * 4. Multiple condition variables
 * 5. Lock status checking
 * 
 * When to use:
 * - Need advanced features (timeout, fairness, conditions)
 * - Need to release lock in different method than acquired
 * - Need lock status information
 * 
 * When to use synchronized:
 * - Simple cases where basic locking is enough
 * - Less code, more readable
 */
public class ReentrantLockExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Basic ReentrantLock ===");
        demonstrateBasicLock();
        
        Thread.sleep(2000);
        
        System.out.println("\n=== TryLock with Timeout ===");
        demonstrateTryLock();
        
        Thread.sleep(2000);
        
        System.out.println("\n=== Fair Lock ===");
        demonstrateFairLock();
        
        Thread.sleep(2000);
        
        System.out.println("\n=== Condition Variables ===");
        demonstrateCondition();
    }
    
    /**
     * Interview Point: Basic ReentrantLock usage
     * Similar to synchronized but more explicit
     */
    private static void demonstrateBasicLock() throws InterruptedException {
        Lock lock = new ReentrantLock();
        SharedCounter counter = new SharedCounter(lock);
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Final count: " + counter.getCount());
    }
    
    /**
     * Interview Point: tryLock() with timeout
     * Attempts to acquire lock, returns immediately if cannot acquire
     * Useful to avoid deadlocks
     */
    private static void demonstrateTryLock() throws InterruptedException {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        
        Thread t1 = new Thread(() -> {
            if (lock1.tryLock()) {
                try {
                    System.out.println("Thread-1 acquired lock1");
                    Thread.sleep(100);
                    
                    // Interview Point: Try to acquire lock2 with timeout
                    if (lock2.tryLock(500, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                        try {
                            System.out.println("Thread-1 acquired lock2");
                        } finally {
                            lock2.unlock();
                        }
                    } else {
                        System.out.println("Thread-1 couldn't acquire lock2, releasing lock1");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock1.unlock();
                }
            }
        });
        
        Thread t2 = new Thread(() -> {
            if (lock2.tryLock()) {
                try {
                    System.out.println("Thread-2 acquired lock2");
                    Thread.sleep(100);
                    
                    if (lock1.tryLock(500, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                        try {
                            System.out.println("Thread-2 acquired lock1");
                        } finally {
                            lock1.unlock();
                        }
                    } else {
                        System.out.println("Thread-2 couldn't acquire lock1, releasing lock2");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock2.unlock();
                }
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
    }
    
    /**
     * Interview Point: Fair Lock
     * Fair lock ensures threads acquire lock in FIFO order
     * Prevents thread starvation
     * Slightly slower than non-fair lock
     */
    private static void demonstrateFairLock() throws InterruptedException {
        // Interview Point: Fair lock - threads acquire in order
        Lock fairLock = new ReentrantLock(true); // true = fair lock
        
        Runnable task = () -> {
            fairLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " acquired lock");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                fairLock.unlock();
            }
        };
        
        // Create multiple threads
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(task, "Thread-" + (i + 1));
        }
        
        // Start all threads
        for (Thread t : threads) {
            t.start();
            Thread.sleep(10); // Small delay to ensure order
        }
        
        for (Thread t : threads) {
            t.join();
        }
    }
    
    /**
     * Interview Point: Condition Variables
     * Similar to wait/notify but more flexible
     * Can have multiple conditions per lock
     */
    private static void demonstrateCondition() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        SharedResource resource = new SharedResource(lock, condition);
        
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                resource.consume();
            }
        }, "Consumer");
        
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                resource.produce(i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Producer");
        
        consumer.start();
        producer.start();
        
        consumer.join();
        producer.join();
    }
}

/**
 * Shared counter using ReentrantLock
 */
class SharedCounter {
    private int count = 0;
    private Lock lock;
    
    public SharedCounter(Lock lock) {
        this.lock = lock;
    }
    
    public void increment() {
        // Interview Point: Must manually lock and unlock
        // Always use try-finally to ensure unlock is called
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock(); // Always unlock in finally block!
        }
    }
    
    public int getCount() {
        return count;
    }
}

/**
 * Shared resource with Condition
 */
class SharedResource {
    private int value;
    private boolean available = false;
    private Lock lock;
    private Condition condition;
    
    public SharedResource(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }
    
    public void produce(int newValue) {
        lock.lock();
        try {
            while (available) {
                // Interview Point: await() is like wait()
                // Releases lock and waits
                condition.await();
            }
            value = newValue;
            available = true;
            System.out.println(Thread.currentThread().getName() + 
                ": Produced " + value);
            // Interview Point: signal() is like notify()
            // signalAll() is like notifyAll()
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    
    public int consume() {
        lock.lock();
        try {
            while (!available) {
                condition.await();
            }
            int consumed = value;
            available = false;
            System.out.println(Thread.currentThread().getName() + 
                ": Consumed " + consumed);
            condition.signal();
            return consumed;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            lock.unlock();
        }
    }
}

/**
 * Interview Point: ReentrantLock vs synchronized
 * 
 * ReentrantLock advantages:
 * + Try lock with timeout (prevents deadlock)
 * + Interruptible lock acquisition
 * + Fair locking option
 * + Multiple condition variables
 * + Lock status checking (isLocked(), getHoldCount())
 * 
 * synchronized advantages:
 * + Simpler syntax
 * + Automatic lock release (even on exception)
 * + Less error-prone
 * + JVM optimizations
 * 
 * Interview Tip: Use synchronized for simple cases,
 * use ReentrantLock when you need advanced features.
 */


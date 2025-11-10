package com.learning.threads.communication;

/**
 * THREAD COMMUNICATION: wait(), notify(), notifyAll()
 * 
 * Interview Explanation:
 * 
 * These methods allow threads to communicate and coordinate with each other.
 * They must be called from within a synchronized block/method.
 * 
 * wait(): 
 * - Causes current thread to release the lock and wait
 * - Thread enters WAITING state
 * - Must be called on the same object that's locked
 * 
 * notify():
 * - Wakes up ONE waiting thread (arbitrary choice)
 * - Thread must reacquire lock before continuing
 * 
 * notifyAll():
 * - Wakes up ALL waiting threads
 * - All threads compete for the lock
 * 
 * Interview Tip: Always use wait() in a loop to check condition
 * (spurious wakeups can occur)
 */
public class WaitNotifyExample {

    public static void main(String[] args) {
        System.out.println("=== Producer-Consumer Pattern with wait/notify ===\n");
        
        // Interview Point: Shared resource between producer and consumer
        SharedResource resource = new SharedResource();
        
        // Producer thread: Produces items
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                resource.produce(i);
                try {
                    Thread.sleep(1000); // Simulate production time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Producer");
        
        // Consumer thread: Consumes items
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                resource.consume();
                try {
                    Thread.sleep(1500); // Simulate consumption time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Consumer");
        
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n=== Multiple Consumers Example ===");
        demonstrateMultipleConsumers();
    }
    
    /**
     * Interview Point: Demonstrates notify() vs notifyAll()
     * - notify() wakes only one thread
     * - notifyAll() wakes all waiting threads
     */
    private static void demonstrateMultipleConsumers() {
        SharedResource resource = new SharedResource();
        
        // Create multiple consumer threads
        Thread consumer1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                resource.consume();
            }
        }, "Consumer-1");
        
        Thread consumer2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                resource.consume();
            }
        }, "Consumer-2");
        
        Thread consumer3 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                resource.consume();
            }
        }, "Consumer-3");
        
        // Producer produces items
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 9; i++) {
                resource.produce(i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Producer");
        
        consumer1.start();
        consumer2.start();
        consumer3.start();
        producer.start();
        
        try {
            consumer1.join();
            consumer2.join();
            consumer3.join();
            producer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Shared resource with producer-consumer pattern
 * 
 * Interview Explanation:
 * - Producer produces items when buffer is empty
 * - Consumer consumes items when buffer is full
 * - Uses wait/notify for coordination
 */
class SharedResource {
    private int value;
    private boolean available = false; // Flag to track if value is available
    
    /**
     * Interview Point: Producer method
     * - Produces a value and notifies waiting consumers
     * - Waits if value is already available (buffer full)
     */
    public synchronized void produce(int newValue) {
        // Interview Point: Always use wait() in a while loop
        // This prevents spurious wakeups (threads waking up without notify)
        while (available) {
            try {
                System.out.println(Thread.currentThread().getName() + 
                    ": Buffer full, waiting for consumer...");
                wait(); // Release lock and wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Interview Point: Now we have the lock and condition is met
        value = newValue;
        available = true;
        System.out.println(Thread.currentThread().getName() + 
            ": Produced value = " + value);
        
        // Interview Point: Notify waiting consumers
        // notify() wakes one thread, notifyAll() wakes all
        notifyAll(); // Wake up all waiting consumers
    }
    
    /**
     * Interview Point: Consumer method
     * - Consumes the value and notifies waiting producers
     * - Waits if no value is available (buffer empty)
     */
    public synchronized int consume() {
        // Interview Point: Wait while no value is available
        while (!available) {
            try {
                System.out.println(Thread.currentThread().getName() + 
                    ": Buffer empty, waiting for producer...");
                wait(); // Release lock and wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Interview Point: Consume the value
        int consumedValue = value;
        available = false;
        System.out.println(Thread.currentThread().getName() + 
            ": Consumed value = " + consumedValue);
        
        // Interview Point: Notify waiting producers
        notifyAll(); // Wake up waiting producers
        
        return consumedValue;
    }
}


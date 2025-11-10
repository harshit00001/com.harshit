package com.learning.threads.basics;

/**
 * BASIC THREAD CREATION - Interview Explanation:
 * 
 * There are two main ways to create threads in Java:
 * 1. Extending Thread class
 * 2. Implementing Runnable interface
 * 
 * Why Runnable is preferred:
 * - Java doesn't support multiple inheritance, so if you extend Thread, 
 *   you can't extend another class
 * - Runnable separates the task from the thread execution mechanism
 * - More flexible - can be passed to Thread constructor or ExecutorService
 */
public class BasicThreadCreation {

    public static void main(String[] args) {
        System.out.println("=== Method 1: Extending Thread Class ===");
        
        // Interview Point: When you extend Thread, you override run() method
        // The run() method contains the code that will execute in a separate thread
        MyThread thread1 = new MyThread("Thread-1");
        MyThread thread2 = new MyThread("Thread-2");
        
        // Interview Point: start() method creates a new thread and calls run()
        // If you call run() directly, it executes in the same thread (main thread)
        thread1.start(); // Creates new thread
        thread2.start(); // Creates another new thread
        
        // Interview Point: Main thread continues executing while other threads run
        System.out.println("Main thread continues...");
        
        try {
            // Interview Point: join() makes the current thread wait for the specified thread to finish
            // This ensures main thread waits for thread1 and thread2 to complete
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n=== Method 2: Implementing Runnable Interface ===");
        
        // Interview Point: Runnable is a functional interface with single run() method
        // This is the preferred approach because it's more flexible
        Runnable task1 = new MyRunnable("Task-1");
        Runnable task2 = new MyRunnable("Task-2");
        
        // Interview Point: Pass Runnable to Thread constructor
        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n=== Method 3: Using Lambda Expression (Java 8+) ===");
        
        // Interview Point: Since Runnable is a functional interface, we can use lambda
        // This is the most concise way to create threads for simple tasks
        Thread lambdaThread = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Lambda Thread: " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        lambdaThread.start();
        
        try {
            lambdaThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nAll threads completed!");
    }
}

/**
 * Method 1: Extending Thread Class
 * 
 * Interview Explanation:
 * - We extend Thread class and override the run() method
 * - The run() method contains the code that executes in the new thread
 * - We call start() to begin thread execution, not run()
 */
class MyThread extends Thread {
    private String threadName;
    
    public MyThread(String name) {
        this.threadName = name;
    }
    
    @Override
    public void run() {
        // Interview Point: This code runs in a separate thread
        // Each thread has its own execution path
        for (int i = 1; i <= 5; i++) {
            System.out.println(threadName + " - Count: " + i);
            try {
                // Interview Point: sleep() pauses the thread for specified milliseconds
                // This is useful for simulating work or creating delays
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                // Interview Point: InterruptedException occurs when thread is interrupted
                // We should handle this gracefully
                System.out.println(threadName + " was interrupted");
                return; // Exit the thread
            }
        }
        System.out.println(threadName + " finished execution");
    }
}

/**
 * Method 2: Implementing Runnable Interface
 * 
 * Interview Explanation:
 * - Runnable is a functional interface with single abstract method: run()
 * - This approach is preferred because:
 *   1. Java doesn't support multiple inheritance
 *   2. Separates task definition from thread execution
 *   3. More flexible - can be used with ExecutorService, thread pools, etc.
 */
class MyRunnable implements Runnable {
    private String taskName;
    
    public MyRunnable(String name) {
        this.taskName = name;
    }
    
    @Override
    public void run() {
        // Interview Point: Same as Thread's run() method
        // Contains the code that will execute in the thread
        for (int i = 1; i <= 5; i++) {
            System.out.println(taskName + " - Count: " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(taskName + " was interrupted");
                return;
            }
        }
        System.out.println(taskName + " finished execution");
    }
}


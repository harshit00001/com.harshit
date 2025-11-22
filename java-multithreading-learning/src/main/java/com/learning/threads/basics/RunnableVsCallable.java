package com.learning.threads.basics;

import java.util.concurrent.*;

/**
 * RUNNABLE vs CALLABLE - Complete Comparison
 * 
 * This is a fundamental concept that appears in almost every Java multithreading interview.
 * Understanding the difference between Runnable and Callable is crucial for working with
 * the Executor Framework and understanding how to handle tasks that need to return values
 * or throw checked exceptions.
 * 
 * Runnable is an interface that has been part of Java since version 1.0. It represents a
 * task that can be executed by a thread. The Runnable interface has a single method called
 * run() that takes no parameters and returns void. This means that tasks implemented using
 * Runnable cannot return a value to the caller, and they cannot throw checked exceptions
 * that propagate outside the run method.
 * 
 * Callable is an interface introduced in Java 5 as part of the java.util.concurrent package.
 * It is similar to Runnable in that it represents a task that can be executed, but it has
 * one crucial difference: the call() method can return a value and can throw checked exceptions.
 * This makes Callable more powerful for scenarios where you need to get results back from
 * your tasks or handle exceptions properly.
 */
public class RunnableVsCallable {
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        demonstrateRunnable();
        demonstrateCallable();
        compareRunnableVsCallable();
        demonstrateFuture();
    }
    
    /**
     * RUNNABLE DEMONSTRATION
     * 
     * This method demonstrates how Runnable works. When you implement Runnable, you override
     * the run() method which contains the code that will execute in a separate thread. The
     * run() method returns void, which means you cannot get a return value from the task.
     * If you need to handle exceptions, you must catch them within the run() method itself,
     * because run() doesn't declare any checked exceptions.
     */
    public static void demonstrateRunnable() {
        System.out.println("=== RUNNABLE DEMONSTRATION ===");
        
        // Create a Runnable task
        Runnable runnableTask = new Runnable() {
            @Override
            public void run() {
                // Interview Point: This method returns void
                // All logic that needs to execute in a thread goes here
                System.out.println("Runnable task executing in thread: " + 
                    Thread.currentThread().getName());
                
                // Simulate some work
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Interview Point: Must handle exceptions within run() method
                    // Cannot throw checked exceptions from run()
                    System.out.println("Task was interrupted");
                }
                
                System.out.println("Runnable task completed");
            }
        };
        
        // Execute using Thread
        Thread thread = new Thread(runnableTask);
        thread.start();
        
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Execute using ExecutorService
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(runnableTask);
        executor.shutdown();
        
        System.out.println();
    }
    
    /**
     * CALLABLE DEMONSTRATION
     * 
     * This method demonstrates how Callable works. When you implement Callable, you override
     * the call() method which can return a value of any type specified by the generic type
     * parameter. The call() method can also throw checked exceptions, which is different from
     * Runnable. This makes Callable suitable for tasks that need to return results or handle
     * exceptions that need to be propagated to the caller.
     */
    public static void demonstrateCallable() throws ExecutionException, InterruptedException {
        System.out.println("=== CALLABLE DEMONSTRATION ===");
        
        // Create a Callable task that returns a String
        Callable<String> callableTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                // Interview Point: This method can return a value
                // Can also throw checked exceptions
                System.out.println("Callable task executing in thread: " + 
                    Thread.currentThread().getName());
                
                // Simulate some work
                Thread.sleep(1000);
                
                // Return a result
                return "Task completed successfully! Result: " + 
                    Thread.currentThread().getName();
            }
        };
        
        // Execute using ExecutorService
        // Interview Point: submit() with Callable returns Future
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(callableTask);
        
        // Interview Point: get() blocks until task completes and returns the result
        String result = future.get();
        System.out.println("Result from Callable: " + result);
        
        executor.shutdown();
        System.out.println();
    }
    
    /**
     * COMPARISON: Runnable vs Callable
     * 
     * This method provides a side-by-side comparison of Runnable and Callable, showing
     * the key differences in code. Understanding these differences is crucial for
     * choosing the right interface for your specific use case.
     */
    public static void compareRunnableVsCallable() throws ExecutionException, InterruptedException {
        System.out.println("=== RUNNABLE vs CALLABLE COMPARISON ===");
        
        // Runnable example
        Runnable runnable = () -> {
            System.out.println("Runnable: No return type, no checked exceptions");
            // Cannot return value
            // Cannot throw checked exceptions (must catch them)
        };
        
        // Callable example
        Callable<String> callable = () -> {
            System.out.println("Callable: Has return type, can throw exceptions");
            return "This is the return value";
            // Can return value
            // Can throw checked exceptions
        };
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        // Runnable: submit() returns Future<?> (no useful return value)
        Future<?> future1 = executor.submit(runnable);
        future1.get(); // Returns null
        
        // Callable: submit() returns Future<T> with actual result
        Future<String> future2 = executor.submit(callable);
        String result = future2.get(); // Returns the actual result
        
        System.out.println("Callable result: " + result);
        
        executor.shutdown();
        System.out.println();
    }
    
    /**
     * FUTURE DEMONSTRATION
     * 
     * Future is an interface that represents the result of an asynchronous computation.
     * When you submit a Callable task to an ExecutorService, you get back a Future object
     * that allows you to check if the task is done, cancel it, or get the result. This is
     * how Callable tasks communicate their results back to the caller.
     */
    public static void demonstrateFuture() throws ExecutionException, InterruptedException {
        System.out.println("=== FUTURE DEMONSTRATION ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Submit multiple Callable tasks
        Future<Integer> future1 = executor.submit(() -> {
            Thread.sleep(1000);
            return 10;
        });
        
        Future<Integer> future2 = executor.submit(() -> {
            Thread.sleep(2000);
            return 20;
        });
        
        Future<Integer> future3 = executor.submit(() -> {
            Thread.sleep(500);
            return 30;
        });
        
        // Interview Point: isDone() checks if task is completed
        System.out.println("Task 1 done: " + future1.isDone());
        System.out.println("Task 2 done: " + future2.isDone());
        System.out.println("Task 3 done: " + future3.isDone());
        
        // Interview Point: get() blocks until result is available
        // Can throw ExecutionException if task threw an exception
        // Can throw InterruptedException if thread was interrupted
        Integer result1 = future1.get();
        Integer result2 = future2.get();
        Integer result3 = future3.get();
        
        System.out.println("Result 1: " + result1);
        System.out.println("Result 2: " + result2);
        System.out.println("Result 3: " + result3);
        System.out.println("Sum: " + (result1 + result2 + result3));
        
        // Interview Point: cancel() can cancel a task if it hasn't started
        Future<Integer> future4 = executor.submit(() -> {
            Thread.sleep(5000);
            return 40;
        });
        
        boolean cancelled = future4.cancel(true);
        System.out.println("Task 4 cancelled: " + cancelled);
        
        executor.shutdown();
        System.out.println();
    }
}


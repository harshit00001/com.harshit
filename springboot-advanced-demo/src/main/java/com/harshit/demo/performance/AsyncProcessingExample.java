package com.harshit.demo.performance;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * STRATEGY 4: ASYNCHRONOUS PROCESSING - Complete Implementation
 * 
 * This class shows actual async code with @Async annotation
 */
@Service
public class AsyncProcessingExample {
    
    /**
     * @Async with void return - Fire and forget
     * Method executes in background thread, caller doesn't wait
     */
    @Async
    public void sendEmailAsync(String to, String subject, String body) {
        // This runs in a separate thread (async-1, async-2, etc.)
        System.out.println("Thread: " + Thread.currentThread().getName() + 
            " - Sending email to: " + to);
        
        // Simulate email sending (time-consuming operation)
        try {
            Thread.sleep(2000); // Simulate network delay
            System.out.println("Email sent successfully to: " + to);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * @Async with CompletableFuture return
     * Caller can get result later using CompletableFuture
     */
    @Async
    public CompletableFuture<String> processDataAsync(String data) {
        System.out.println("Thread: " + Thread.currentThread().getName() + 
            " - Processing data: " + data);
        
        // Simulate processing
        try {
            Thread.sleep(3000);
            String result = "Processed: " + data.toUpperCase();
            return CompletableFuture.completedFuture(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * @Async with Future return (older approach)
     * Still supported but CompletableFuture is preferred
     */
    @Async
    public Future<String> fetchDataAsync(String url) {
        System.out.println("Thread: " + Thread.currentThread().getName() + 
            " - Fetching from: " + url);
        
        try {
            Thread.sleep(2000);
            return new org.springframework.scheduling.annotation.AsyncResult<>(
                "Data from " + url
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new org.springframework.scheduling.annotation.AsyncResult<>("Failed");
        }
    }
    
    /**
     * Multiple async operations - Can run concurrently
     */
    @Async
    public CompletableFuture<String> task1() {
        System.out.println("Task 1 running in: " + Thread.currentThread().getName());
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        return CompletableFuture.completedFuture("Task 1 completed");
    }
    
    @Async
    public CompletableFuture<String> task2() {
        System.out.println("Task 2 running in: " + Thread.currentThread().getName());
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        return CompletableFuture.completedFuture("Task 2 completed");
    }
    
    /**
     * Example: Run multiple async tasks and wait for all
     */
    public void runMultipleAsyncTasks() {
        CompletableFuture<String> future1 = task1();
        CompletableFuture<String> future2 = task2();
        
        // Both tasks run concurrently
        CompletableFuture.allOf(future1, future2).thenRun(() -> {
            System.out.println("All tasks completed!");
        });
    }
}


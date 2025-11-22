package com.harshit.springboot.basics;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @Async ANNOTATION - Asynchronous Method Execution
 * 
 * The @Async annotation in Spring allows you to execute methods asynchronously,
 * meaning the method call returns immediately while the actual execution happens
 * in a separate thread. This is useful for improving application performance by
 * not blocking the calling thread while waiting for time-consuming operations
 * to complete.
 * 
 * When you annotate a method with @Async, Spring automatically creates a proxy
 * that executes the method in a thread from a thread pool. The calling thread
 * doesn't wait for the method to complete, which allows it to continue with
 * other work. This is particularly useful for I/O operations, external API calls,
 * or any operation that doesn't need immediate results.
 * 
 * To use @Async, you need to enable it in your configuration by adding
 * @EnableAsync to a configuration class or your main application class. Spring
 * then uses a TaskExecutor to manage the thread pool for asynchronous execution.
 */
@Service
public class AsyncAnnotationExample {
    
    /**
     * ASYNC METHOD WITHOUT RETURN VALUE
     * 
     * This method demonstrates a simple async method that doesn't return a value.
     * When you call this method, it returns immediately, and the actual work
     * happens in a background thread. This is useful for fire-and-forget operations
     * where you don't need to wait for the result.
     */
    @Async
    public void sendEmailAsync(String to, String subject, String body) {
        // Interview Point: This method executes in a separate thread
        // The calling thread doesn't wait for this to complete
        System.out.println("Sending email to " + to + " in thread: " + 
            Thread.currentThread().getName());
        
        // Simulate time-consuming email sending operation
        try {
            Thread.sleep(2000);  // Simulate network delay
            System.out.println("Email sent successfully to " + to);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Email sending interrupted");
        }
    }
    
    /**
     * ASYNC METHOD RETURNING FUTURE
     * 
     * This method demonstrates an async method that returns a Future, which allows
     * you to get the result later. The Future object represents a result that will
     * be available in the future. You can check if the result is ready, wait for it,
     * or cancel the operation.
     */
    @Async
    public Future<String> processDataAsync(String data) {
        // Interview Point: Returns Future<String> - can get result later
        System.out.println("Processing data in thread: " + 
            Thread.currentThread().getName());
        
        try {
            Thread.sleep(3000);  // Simulate processing time
            String result = "Processed: " + data.toUpperCase();
            return new AsyncResult<>(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new AsyncResult<>("Processing interrupted");
        }
    }
    
    /**
     * ASYNC METHOD RETURNING COMPLETABLEFUTURE
     * 
     * CompletableFuture is a more modern and powerful alternative to Future.
     * It provides better exception handling, allows chaining operations, and
     * supports functional programming style. This is the recommended approach
     * for async methods that return values.
     */
    @Async
    public CompletableFuture<String> fetchDataAsync(String url) {
        // Interview Point: CompletableFuture is more powerful than Future
        // Supports chaining, better exception handling, functional style
        System.out.println("Fetching data from " + url + " in thread: " + 
            Thread.currentThread().getName());
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);  // Simulate network call
                return "Data from " + url;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Fetch interrupted", e);
            }
        });
    }
    
    /**
     * DEMONSTRATION METHOD
     * 
     * This method shows how to use async methods and handle their results.
     * Notice how the calling thread continues immediately while async operations
     * run in the background.
     */
    public void demonstrateAsync() throws Exception {
        System.out.println("=== ASYNC ANNOTATION DEMONSTRATION ===");
        System.out.println("Main thread: " + Thread.currentThread().getName());
        
        // Fire and forget - don't wait for result
        sendEmailAsync("user@example.com", "Hello", "This is async");
        System.out.println("Email sending started (not waiting for completion)");
        
        // Get result using Future
        Future<String> future = processDataAsync("test data");
        System.out.println("Processing started, doing other work...");
        
        // Do other work while processing happens
        Thread.sleep(1000);
        System.out.println("Doing other work in main thread...");
        
        // Get result when ready
        String result = future.get();  // Blocks until result is available
        System.out.println("Result: " + result);
        
        // Using CompletableFuture
        CompletableFuture<String> cf = fetchDataAsync("http://api.example.com/data");
        cf.thenAccept(data -> System.out.println("Received: " + data));
        
        System.out.println();
    }
}


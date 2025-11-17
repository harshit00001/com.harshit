package com.learning.threads.advanced;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * COMPLETABLEFUTURE - Interview Explanation:
 * 
 * CompletableFuture is used for asynchronous programming in Java.
 * It represents a future result of an asynchronous computation.
 * 
 * Key Features:
 * 1. Non-blocking asynchronous operations
 * 2. Chain multiple async operations
 * 3. Combine multiple futures
 * 4. Handle exceptions
 * 5. Better than Future (from ExecutorService)
 * 
 * Interview Point: CompletableFuture is the modern way to handle
 * asynchronous operations in Java (Java 8+)
 */
public class CompletableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("=== Basic CompletableFuture ===");
        demonstrateBasic();
        
        System.out.println("\n=== Chaining Operations ===");
        demonstrateChaining();
        
        System.out.println("\n=== Combining Futures ===");
        demonstrateCombining();
        
        System.out.println("\n=== Exception Handling ===");
        demonstrateExceptionHandling();
        
        System.out.println("\n=== AllOf and AnyOf ===");
        demonstrateAllOfAndAnyOf();
    }
    
    /**
     * Interview Point: Basic CompletableFuture
     * - supplyAsync(): Runs task asynchronously and returns result
     * - runAsync(): Runs task asynchronously without return value
     * - get(): Blocks until result is available
     */
    private static void demonstrateBasic() throws ExecutionException, InterruptedException {
        // Interview Point: supplyAsync() executes task in ForkJoinPool
        // Returns CompletableFuture that will contain the result
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Executing async task in: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000); // Simulate work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello from async task!";
        });
        
        System.out.println("Main thread continues...");
        
        // Interview Point: get() blocks until result is available
        String result = future.get();
        System.out.println("Result: " + result);
    }
    
    /**
     * Interview Point: Chaining operations
     * - thenApply(): Transform result (synchronous)
     * - thenCompose(): Chain another CompletableFuture (async)
     * - thenAccept(): Consume result without returning
     */
    private static void demonstrateChaining() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture
            .supplyAsync(() -> {
                System.out.println("Step 1: Fetching data");
                return "Data";
            })
            .thenApply(data -> {
                // Interview Point: thenApply() runs synchronously
                // Transforms the result
                System.out.println("Step 2: Processing " + data);
                return data + " Processed";
            })
            .thenApply(processed -> {
                System.out.println("Step 3: Formatting " + processed);
                return processed + " Formatted";
            });
        
        System.out.println("Final result: " + future.get());
        
        // Interview Point: thenCompose() for async chaining
        CompletableFuture<String> asyncChain = CompletableFuture
            .supplyAsync(() -> "First")
            .thenCompose(result -> 
                CompletableFuture.supplyAsync(() -> result + " Second")
            );
        
        System.out.println("Async chain result: " + asyncChain.get());
    }
    
    /**
     * Interview Point: Combining multiple futures
     * - thenCombine(): Combine two independent futures
     * - allOf(): Wait for all futures to complete
     * - anyOf(): Wait for any future to complete
     */
    private static void demonstrateCombining() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result from Future 1";
        });
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result from Future 2";
        });
        
        // Interview Point: Combine results from two futures
        CompletableFuture<String> combined = future1.thenCombine(future2, (result1, result2) -> {
            return result1 + " + " + result2;
        });
        
        System.out.println("Combined result: " + combined.get());
    }
    
    /**
     * Interview Point: Exception handling
     * - handle(): Handle both success and exception
     * - exceptionally(): Handle only exceptions
     * - whenComplete(): Execute code regardless of success/failure
     */
    private static void demonstrateExceptionHandling() throws ExecutionException, InterruptedException {
        // Interview Point: Future that throws exception
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Something went wrong!");
            }
            return "Success!";
        });
        
        // Interview Point: Handle exception gracefully
        CompletableFuture<String> handled = future.exceptionally(ex -> {
            System.out.println("Exception caught: " + ex.getMessage());
            return "Default value on error";
        });
        
        System.out.println("Result: " + handled.get());
        
        // Interview Point: handle() for both success and error
        CompletableFuture<Object> handled2 = CompletableFuture.supplyAsync(() -> {
                throw new RuntimeException("Error!");
            })
            .handle((result, ex) -> {
                if (ex != null) {
                    return "Handled error: " + ex.getMessage();
                }
                return result;
            });
        
        System.out.println("Handled result: " + handled2.get());
    }
    
    /**
     * Interview Point: allOf() and anyOf()
     * - allOf(): Wait for all futures to complete
     * - anyOf(): Returns first completed future
     */
    private static void demonstrateAllOfAndAnyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Future 1";
        });
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Future 2";
        });
        
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Future 3";
        });
        
        // Interview Point: allOf() waits for all to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);
        allFutures.get(); // Wait for all
        System.out.println("All futures completed!");
        
        // Interview Point: anyOf() returns first completed
        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(future1, future2, future3);
        System.out.println("First completed: " + anyFuture.get());
    }
}

/**
 * Interview Point: CompletableFuture Best Practices
 * 
 * 1. Use supplyAsync() for tasks that return values
 * 2. Use runAsync() for tasks that don't return values
 * 3. Chain operations with thenApply(), thenCompose(), etc.
 * 4. Always handle exceptions with exceptionally() or handle()
 * 5. Use allOf() to wait for multiple futures
 * 6. Use anyOf() to get first completed result
 * 7. Avoid blocking get() in production - use callbacks instead
 * 
 * Interview Point: CompletableFuture vs Future
 * 
 * CompletableFuture advantages:
 * + Non-blocking (can chain operations)
 * + Exception handling built-in
 * + Can combine multiple futures
 * + More functional programming style
 * 
 * Future (from ExecutorService) limitations:
 * - Blocking get() method
 * - No chaining
 * - Manual exception handling
 * - Cannot combine futures easily
 */


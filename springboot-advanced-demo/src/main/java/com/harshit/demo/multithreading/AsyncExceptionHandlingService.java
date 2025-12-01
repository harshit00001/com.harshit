package com.harshit.demo.multithreading;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Demonstrates proper exception handling in async methods
 * Shows how to handle exceptions in @Async methods and CompletableFuture
 */
@Service
public class AsyncExceptionHandlingService {
    
    /**
     * ❌ PROBLEM: Exception is silently swallowed
     */
    @Async
    public void asyncMethodWithoutExceptionHandling() {
        throw new RuntimeException("This exception will be lost!");
    }
    
    /**
     * ✅ SOLUTION 1: Return CompletableFuture and handle exceptions
     */
    @Async
    public CompletableFuture<String> asyncMethodWithExceptionHandling(String input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (input == null || input.isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be null or empty");
                }
                // Simulate work
                Thread.sleep(1000);
                return "Processed: " + input;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            } catch (Exception e) {
                // Log the exception
                System.err.println("Error processing: " + e.getMessage());
                throw new CompletionException(e);
            }
        });
    }
    
    /**
     * ✅ SOLUTION 2: Use exceptionally() for error handling
     */
    @Async
    public CompletableFuture<String> asyncMethodWithExceptionally(String input) {
        return CompletableFuture
            .supplyAsync(() -> {
                if (input == null) {
                    throw new IllegalArgumentException("Input is null");
                }
                return "Success: " + input;
            })
            .exceptionally(ex -> {
                // Handle exception and return default value
                System.err.println("Exception occurred: " + ex.getMessage());
                return "Default value due to error";
            });
    }
    
    /**
     * ✅ SOLUTION 3: Chain exception handling
     */
    @Async
    public CompletableFuture<String> asyncMethodWithChainedHandling(String input) {
        return CompletableFuture
            .supplyAsync(() -> {
                if (input == null) {
                    throw new IllegalArgumentException("Input is null");
                }
                return input.toUpperCase();
            })
            .thenApply(result -> "Processed: " + result)
            .handle((result, ex) -> {
                if (ex != null) {
                    System.err.println("Error in chain: " + ex.getMessage());
                    return "Error occurred";
                }
                return result;
            });
    }
    
    /**
     * Example: Calling async method and handling exceptions
     */
    public void demonstrateAsyncExceptionHandling() {
        System.out.println("\n=== ASYNC EXCEPTION HANDLING DEMONSTRATION ===");
        
        // Handle exception using get()
        CompletableFuture<String> future1 = asyncMethodWithExceptionHandling("valid input");
        try {
            String result = future1.get();
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.err.println("Caught exception: " + e.getCause().getMessage());
        }
        
        // Handle exception using exceptionally()
        CompletableFuture<String> future2 = asyncMethodWithExceptionally(null);
        future2.thenAccept(result -> {
            System.out.println("Result with exception handling: " + result);
        });
        
        // Handle exception in chain
        CompletableFuture<String> future3 = asyncMethodWithChainedHandling(null);
        future3.thenAccept(result -> {
            System.out.println("Chained result: " + result);
        });
    }
    
    /**
     * ✅ SOLUTION 4: Multiple async operations with exception handling
     */
    public CompletableFuture<String> processMultipleAsyncOperations(String[] inputs) {
        CompletableFuture<String>[] futures = new CompletableFuture[inputs.length];
        
        for (int i = 0; i < inputs.length; i++) {
            final int index = i;
            futures[i] = asyncMethodWithExceptionHandling(inputs[i])
                .exceptionally(ex -> {
                    System.err.println("Error processing input[" + index + "]: " + ex.getMessage());
                    return "Failed";
                });
        }
        
        return CompletableFuture.allOf(futures)
            .thenApply(v -> {
                StringBuilder result = new StringBuilder();
                for (CompletableFuture<String> future : futures) {
                    try {
                        result.append(future.get()).append(", ");
                    } catch (Exception e) {
                        result.append("Error, ");
                    }
                }
                return result.toString();
            });
    }
    
    /**
     * ✅ SOLUTION 5: Timeout handling
     */
    @Async
    public CompletableFuture<String> asyncMethodWithTimeout(String input) {
        return CompletableFuture
            .supplyAsync(() -> {
                try {
                    Thread.sleep(5000); // Simulate long operation
                    return "Result: " + input;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new CompletionException(e);
                }
            })
            .orTimeout(2, java.util.concurrent.TimeUnit.SECONDS) // ✅ Timeout after 2 seconds
            .exceptionally(ex -> {
                if (ex instanceof java.util.concurrent.TimeoutException) {
                    System.err.println("Operation timed out");
                    return "Timeout";
                }
                System.err.println("Error: " + ex.getMessage());
                return "Error";
            });
    }
}


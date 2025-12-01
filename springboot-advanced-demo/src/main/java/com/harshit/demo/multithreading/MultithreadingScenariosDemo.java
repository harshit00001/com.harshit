package com.harshit.demo.multithreading;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Comprehensive demonstration of multithreading scenarios in production
 * This class shows both problems and solutions for common concurrency issues
 */
@Service
public class MultithreadingScenariosDemo {

    // ========== SCENARIO 1: Race Conditions ==========
    
    // ❌ PROBLEM: Non-thread-safe counter
    private int unsafeCounter = 0;
    
    // ✅ SOLUTION 1: AtomicInteger
    private final AtomicInteger safeCounter = new AtomicInteger(0);
    
    // ✅ SOLUTION 2: Synchronized
    private int synchronizedCounter = 0;
    private final Object counterLock = new Object();
    
    // ✅ SOLUTION 3: ReentrantLock
    private int lockCounter = 0;
    private final ReentrantLock lock = new ReentrantLock();
    
    public void demonstrateRaceCondition() {
        System.out.println("\n=== RACE CONDITION DEMONSTRATION ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Unsafe counter
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> unsafeCounter++);
        }
        
        // Safe counter with AtomicInteger
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> safeCounter.incrementAndGet());
        }
        
        // Safe counter with synchronized
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                synchronized (counterLock) {
                    synchronizedCounter++;
                }
            });
        }
        
        // Safe counter with ReentrantLock
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                lock.lock();
                try {
                    lockCounter++;
                } finally {
                    lock.unlock();
                }
            });
        }
        
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            
            System.out.println("Unsafe counter (should be 1000): " + unsafeCounter);
            System.out.println("Safe counter (AtomicInteger): " + safeCounter.get());
            System.out.println("Safe counter (Synchronized): " + synchronizedCounter);
            System.out.println("Safe counter (ReentrantLock): " + lockCounter);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ========== SCENARIO 2: Deadlock Prevention ==========
    
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    private final ReentrantLock reentrantLock1 = new ReentrantLock();
    private final ReentrantLock reentrantLock2 = new ReentrantLock();
    
    public void demonstrateDeadlockPrevention() {
        System.out.println("\n=== DEADLOCK PREVENTION DEMONSTRATION ===");
        
        // ✅ SOLUTION: Always acquire locks in same order
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        executor.submit(() -> {
            synchronized (lock1) {  // Always lock1 first
                System.out.println("Thread 1: Acquired lock1");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lock2) {  // Then lock2
                    System.out.println("Thread 1: Acquired lock2");
                }
            }
        });
        
        executor.submit(() -> {
            synchronized (lock1) {  // Same order: lock1 first
                System.out.println("Thread 2: Acquired lock1");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lock2) {  // Then lock2
                    System.out.println("Thread 2: Acquired lock2");
                }
            }
        });
        
        try {
            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ========== SCENARIO 3: Producer-Consumer with BlockingQueue ==========
    
    private final BlockingQueue<String> taskQueue = new LinkedBlockingQueue<>(100);
    private final ExecutorService consumerPool = Executors.newFixedThreadPool(5);
    private volatile boolean consumersRunning = false;
    
    public void startConsumers() {
        if (consumersRunning) {
            return;
        }
        consumersRunning = true;
        
        for (int i = 0; i < 5; i++) {
            final int consumerId = i;
            consumerPool.submit(() -> {
                while (consumersRunning || !taskQueue.isEmpty()) {
                    try {
                        String task = taskQueue.poll(1, TimeUnit.SECONDS);
                        if (task != null) {
                            System.out.println("Consumer " + consumerId + " processing: " + task);
                            Thread.sleep(500); // Simulate processing
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }
    
    public void produceTasks(int count) throws InterruptedException {
        System.out.println("\n=== PRODUCER-CONSUMER DEMONSTRATION ===");
        System.out.println("Producing " + count + " tasks...");
        
        for (int i = 0; i < count; i++) {
            taskQueue.put("Task-" + i);
        }
        
        System.out.println("All tasks produced. Queue size: " + taskQueue.size());
    }
    
    public void stopConsumers() {
        consumersRunning = false;
        consumerPool.shutdown();
    }
    
    // ========== SCENARIO 4: Thread Pool Exhaustion Prevention ==========
    
    private final Semaphore semaphore = new Semaphore(10); // Max 10 concurrent
    
    @Async
    public CompletableFuture<String> processWithRateLimit(String taskId) {
        try {
            semaphore.acquire(); // ✅ Rate limiting
            try {
                System.out.println("Processing task: " + taskId + " in thread: " + 
                    Thread.currentThread().getName());
                Thread.sleep(2000); // Simulate work
                return CompletableFuture.completedFuture("Completed: " + taskId);
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }
    
    public void demonstrateRateLimiting() {
        System.out.println("\n=== RATE LIMITING DEMONSTRATION ===");
        
        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            processWithRateLimit("Task-" + taskId)
                .thenAccept(result -> System.out.println(result));
        }
    }
    
    // ========== SCENARIO 5: Memory Visibility with Volatile ==========
    
    private volatile boolean flag = false; // ✅ Ensures visibility
    
    public void setFlag() {
        flag = true;
        System.out.println("Flag set to true");
    }
    
    public boolean isFlag() {
        return flag;
    }
    
    public void demonstrateMemoryVisibility() {
        System.out.println("\n=== MEMORY VISIBILITY DEMONSTRATION ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Writer thread
        executor.submit(() -> {
            try {
                Thread.sleep(1000);
                setFlag();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Reader thread
        executor.submit(() -> {
            while (!isFlag()) {
                // Busy wait - will see flag change due to volatile
            }
            System.out.println("Flag detected as true!");
        });
        
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ========== SCENARIO 6: Read-Write Lock ==========
    
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private String sharedData = "Initial Data";
    
    public String readData() {
        readWriteLock.readLock().lock();
        try {
            System.out.println("Reading data: " + sharedData + " in thread: " + 
                Thread.currentThread().getName());
            Thread.sleep(100); // Simulate read operation
            return sharedData;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
    
    public void writeData(String newData) {
        readWriteLock.writeLock().lock();
        try {
            System.out.println("Writing data: " + newData + " in thread: " + 
                Thread.currentThread().getName());
            Thread.sleep(200); // Simulate write operation
            sharedData = newData;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
    
    public void demonstrateReadWriteLock() {
        System.out.println("\n=== READ-WRITE LOCK DEMONSTRATION ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Multiple readers (can run concurrently)
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> readData());
        }
        
        // One writer (exclusive)
        executor.submit(() -> writeData("Updated Data"));
        
        // More readers
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> readData());
        }
        
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ========== SCENARIO 7: CompletableFuture Chaining ==========
    
    @Async
    public CompletableFuture<String> fetchData(String source) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                return "Data from " + source;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }
    
    public void demonstrateCompletableFuture() {
        System.out.println("\n=== COMPLETABLEFUTURE DEMONSTRATION ===");
        
        CompletableFuture<String> future1 = fetchData("Source1");
        CompletableFuture<String> future2 = fetchData("Source2");
        CompletableFuture<String> future3 = fetchData("Source3");
        
        // Wait for all
        CompletableFuture.allOf(future1, future2, future3)
            .thenRun(() -> {
                try {
                    System.out.println("All futures completed:");
                    System.out.println("  " + future1.get());
                    System.out.println("  " + future2.get());
                    System.out.println("  " + future3.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        
        // Chain operations
        future1
            .thenApply(data -> data.toUpperCase())
            .thenAccept(data -> System.out.println("Processed: " + data))
            .exceptionally(ex -> {
                System.err.println("Error: " + ex.getMessage());
                return null;
            });
    }
    
    // ========== SCENARIO 8: CountDownLatch ==========
    
    public void demonstrateCountDownLatch() {
        System.out.println("\n=== COUNTDOWNLATCH DEMONSTRATION ===");
        
        int workerCount = 5;
        CountDownLatch latch = new CountDownLatch(workerCount);
        
        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        
        for (int i = 0; i < workerCount; i++) {
            final int workerId = i;
            executor.submit(() -> {
                try {
                    System.out.println("Worker " + workerId + " starting...");
                    Thread.sleep(1000 + workerId * 100);
                    System.out.println("Worker " + workerId + " completed");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown(); // ✅ Signal completion
                }
            });
        }
        
        try {
            System.out.println("Waiting for all workers to complete...");
            latch.await(); // ✅ Wait for all workers
            System.out.println("All workers completed!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }
    
    // ========== SCENARIO 9: CyclicBarrier ==========
    
    public void demonstrateCyclicBarrier() {
        System.out.println("\n=== CYCLICBARRIER DEMONSTRATION ===");
        
        int parties = 3;
        CyclicBarrier barrier = new CyclicBarrier(parties, () -> {
            System.out.println("All parties reached barrier! Continuing...");
        });
        
        ExecutorService executor = Executors.newFixedThreadPool(parties);
        
        for (int i = 0; i < parties; i++) {
            final int partyId = i;
            executor.submit(() -> {
                try {
                    System.out.println("Party " + partyId + " doing work...");
                    Thread.sleep(1000 + partyId * 200);
                    System.out.println("Party " + partyId + " reached barrier");
                    barrier.await(); // ✅ Wait for all parties
                    System.out.println("Party " + partyId + " continuing after barrier");
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        try {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ========== SCENARIO 10: Proper Interruption Handling ==========
    
    public void demonstrateInterruptionHandling() {
        System.out.println("\n=== INTERRUPTION HANDLING DEMONSTRATION ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        
        Future<?> future = executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) { // ✅ Check interruption
                try {
                    System.out.println("Working...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // ✅ Restore interrupt status
                    System.out.println("Interrupted! Exiting...");
                    break;
                }
            }
        });
        
        try {
            Thread.sleep(3000);
            future.cancel(true); // ✅ Interrupt the task
            System.out.println("Task cancelled");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }
    
    // ========== Main demonstration method ==========
    
    public void runAllDemonstrations() {
        try {
            demonstrateRaceCondition();
            Thread.sleep(2000);
            
            demonstrateDeadlockPrevention();
            Thread.sleep(2000);
            
            startConsumers();
            produceTasks(20);
            Thread.sleep(5000);
            stopConsumers();
            Thread.sleep(2000);
            
            demonstrateRateLimiting();
            Thread.sleep(5000);
            
            demonstrateMemoryVisibility();
            Thread.sleep(2000);
            
            demonstrateReadWriteLock();
            Thread.sleep(2000);
            
            demonstrateCompletableFuture();
            Thread.sleep(5000);
            
            demonstrateCountDownLatch();
            Thread.sleep(2000);
            
            demonstrateCyclicBarrier();
            Thread.sleep(2000);
            
            demonstrateInterruptionHandling();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


package com.learning.threads.advanced;

import java.util.*;
import java.util.concurrent.*;

/**
 * CONCURRENT COLLECTIONS - Interview Explanation:
 * 
 * Problem: Standard collections (ArrayList, HashMap, etc.) are NOT thread-safe.
 * Using them in multi-threaded environments can cause:
 * - ConcurrentModificationException
 * - Data corruption
 * - Inconsistent state
 * 
 * Solution: Concurrent collections from java.util.concurrent package
 * - Thread-safe without explicit synchronization
 * - Better performance than synchronized collections
 * - Designed for concurrent access
 * 
 * Common Concurrent Collections:
 * - ConcurrentHashMap
 * - CopyOnWriteArrayList
 * - BlockingQueue implementations
 * - ConcurrentLinkedQueue
 */
public class ConcurrentCollections {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Problem: Non-thread-safe HashMap ===");
        demonstrateHashMapProblem();
        
        Thread.sleep(2000);
        
        System.out.println("\n=== Solution: ConcurrentHashMap ===");
        demonstrateConcurrentHashMap();
        
        System.out.println("\n=== CopyOnWriteArrayList ===");
        demonstrateCopyOnWriteArrayList();
        
        System.out.println("\n=== BlockingQueue ===");
        demonstrateBlockingQueue();
    }
    
    /**
     * Interview Point: Demonstrates problem with HashMap
     * Multiple threads modifying HashMap can cause exceptions or data loss
     */
    private static void demonstrateHashMapProblem() throws InterruptedException {
        Map<String, Integer> map = new HashMap<>(); // Not thread-safe!
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                map.put("key" + i, i);
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 1000; i < 2000; i++) {
                map.put("key" + i, i);
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Map size: " + map.size());
        System.out.println("Expected: 2000, but might get less or exception");
    }
    
    /**
     * Interview Point: ConcurrentHashMap solution
     * - Thread-safe without explicit synchronization
     * - Better performance than synchronized HashMap
     * - Uses segment locking (in older versions) or CAS (in newer versions)
     */
    private static void demonstrateConcurrentHashMap() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                map.put("key" + i, i);
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 1000; i < 2000; i++) {
                map.put("key" + i, i);
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Map size: " + map.size());
        System.out.println("Expected: 2000, got: " + map.size() + " ✓");
        
        // Interview Point: ConcurrentHashMap operations
        System.out.println("\nConcurrentHashMap operations:");
        map.putIfAbsent("newKey", 999); // Put only if key doesn't exist
        System.out.println("putIfAbsent result: " + map.get("newKey"));
        
        map.compute("newKey", (k, v) -> v * 2); // Compute new value
        System.out.println("After compute: " + map.get("newKey"));
        
        map.merge("newKey", 100, (oldVal, newVal) -> oldVal + newVal);
        System.out.println("After merge: " + map.get("newKey"));
    }
    
    /**
     * Interview Point: CopyOnWriteArrayList
     * - Thread-safe variant of ArrayList
     * - Creates a new copy of array on write operations
     * - Good for read-heavy scenarios
     * - Iterators don't throw ConcurrentModificationException
     */
    private static void demonstrateCopyOnWriteArrayList() throws InterruptedException {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Item1");
        list.add("Item2");
        list.add("Item3");
        
        // Interview Point: Iterator doesn't throw ConcurrentModificationException
        // even if list is modified during iteration
        Thread reader = new Thread(() -> {
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                System.out.println("Reading: " + it.next());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(50);
                list.add("Item4");
                System.out.println("Added Item4");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        reader.start();
        writer.start();
        
        reader.join();
        writer.join();
        
        System.out.println("Final list: " + list);
    }
    
    /**
     * Interview Point: BlockingQueue
     * - Thread-safe queue with blocking operations
     * - put() blocks if queue is full
     * - take() blocks if queue is empty
     * - Perfect for producer-consumer pattern
     */
    private static void demonstrateBlockingQueue() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5); // Capacity: 5
        
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = "Item" + i;
                    queue.put(item); // Blocks if queue is full
                    System.out.println("Produced: " + item);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = queue.take(); // Blocks if queue is empty
                    System.out.println("Consumed: " + item);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
    }
}

/**
 * Interview Point: Summary of Concurrent Collections
 * 
 * 1. ConcurrentHashMap:
 *    - Thread-safe HashMap
 *    - Better than Collections.synchronizedMap()
 *    - Supports atomic operations (putIfAbsent, compute, merge)
 * 
 * 2. CopyOnWriteArrayList:
 *    - Thread-safe ArrayList
 *    - Creates copy on write (expensive writes, cheap reads)
 *    - Good for read-heavy scenarios
 * 
 * 3. BlockingQueue:
 *    - ArrayBlockingQueue: Bounded queue backed by array
 *    - LinkedBlockingQueue: Can be bounded or unbounded
 *    - PriorityBlockingQueue: Priority-based ordering
 * 
 * 4. ConcurrentLinkedQueue:
 *    - Lock-free thread-safe queue
 *    - Non-blocking operations
 * 
 * 5. ConcurrentSkipListMap/Set:
 *    - Thread-safe TreeMap/TreeSet
 *    - Maintains sorted order
 */


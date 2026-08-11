package com.harshit.collections.advanced;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * CONCURRENT COLLECTIONS - Thread-Safe Collections
 * 
 * Regular collections (ArrayList, HashMap, etc.) are NOT thread-safe.
 * In multi-threaded environments, use concurrent collections.
 * 
 * Main Concurrent Collections:
 * 1. ConcurrentHashMap: Thread-safe HashMap
 * 2. CopyOnWriteArrayList: Thread-safe ArrayList
 * 3. CopyOnWriteArraySet: Thread-safe HashSet
 * 4. BlockingQueue: Thread-safe queue with blocking operations
 * 5. ConcurrentLinkedQueue: Lock-free thread-safe queue
 */
public class ConcurrentCollections {
    
    public static void main(String[] args) throws InterruptedException {
        demonstrateConcurrentHashMap();
        demonstrateCopyOnWriteArrayList();
        demonstrateBlockingQueue();
        demonstrateConcurrentLinkedQueue();
        compareThreadSafety();
    }
    
    /**
     * CONCURRENTHASHMAP DEMONSTRATION
     * 
     * ConcurrentHashMap is a thread-safe version of HashMap.
     * 
     * Key Features:
     * - Thread-safe: Multiple threads can access simultaneously
     * - Fail-Safe iterator: No ConcurrentModificationException
     * - Better performance than synchronized HashMap
     * - Uses lock striping (multiple locks instead of one)
     * 
     * How it works:
     * - Divides map into segments/buckets
     * - Each segment has its own lock
     * - Multiple threads can work on different segments simultaneously
     * - Read operations don't require locking (volatile reads)
     * 
     * Best For:
     * - Multi-threaded environments
     * - High concurrency scenarios
     * - When you need thread-safe Map
     */
    public static void demonstrateConcurrentHashMap() {
        System.out.println("=== CONCURRENTHASHMAP ===");
        
        // Create ConcurrentHashMap
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        // Basic operations (same as HashMap)
        map.put("Apple", 10);
        map.put("Banana", 20);
        map.put("Cherry", 30);
        
        System.out.println("Map: " + map);
        
        // Thread-safe operations
        // putIfAbsent: Put only if key doesn't exist
        Integer oldValue = map.putIfAbsent("Apple", 15);  // Won't update (key exists)
        System.out.println("putIfAbsent('Apple', 15): " + oldValue);  // Returns 10
        
        Integer newValue = map.putIfAbsent("Mango", 40);  // Will add (key doesn't exist)
        System.out.println("putIfAbsent('Mango', 40): " + newValue);  // Returns null
        
        // remove: Remove only if key-value matches
        boolean removed = map.remove("Banana", 25);  // Won't remove (value doesn't match)
        System.out.println("remove('Banana', 25): " + removed);  // false
        
        removed = map.remove("Banana", 20);  // Will remove (value matches)
        System.out.println("remove('Banana', 20): " + removed);  // true
        
        // replace: Replace only if key-value matches
        boolean replaced = map.replace("Cherry", 30, 35);
        System.out.println("replace('Cherry', 30, 35): " + replaced);  // true
        
        System.out.println("Final map: " + map);
        
        // Fail-Safe iterator
        System.out.println("\nFail-Safe Iterator:");
        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            map.put("Grape", 50);  // No exception!
        }
        
        System.out.println();
    }
    
    /**
     * COPYONWRITEARRAYLIST DEMONSTRATION
     * 
     * CopyOnWriteArrayList is a thread-safe version of ArrayList.
     * 
     * Key Features:
     * - Thread-safe: Multiple threads can read simultaneously
     * - Fail-Safe iterator: No ConcurrentModificationException
     * - Write operations create a new copy
     * - Expensive writes, cheap reads
     * 
     * How it works:
     * - On write (add, remove, set): Create new array, copy all elements, modify, replace
     * - Iterators hold reference to old array
     * - Multiple readers can read simultaneously (no locking)
     * 
     * Best For:
     * - Read-heavy scenarios (many reads, few writes)
     * - Event listeners lists
     * - Snapshot-based iteration
     * 
     * Not Good For:
     * - Write-heavy scenarios (expensive copying)
     * - Large collections (memory overhead)
     */
    public static void demonstrateCopyOnWriteArrayList() {
        System.out.println("=== COPYONWRITEARRAYLIST ===");
        
        // Create CopyOnWriteArrayList
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        
        System.out.println("List: " + list);
        
        // Fail-Safe iterator
        System.out.println("\nIterating (snapshot created):");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String item = it.next();
            System.out.println("  Processing: " + item);
            list.add("D");  // No exception! But not visible to current iterator
        }
        
        System.out.println("After iteration, list: " + list);
        System.out.println("Note: 'D' was added but iterator shows old snapshot");
        
        // Thread-safe operations
        System.out.println("\nThread-safe operations:");
        list.addIfAbsent("A");  // Won't add (already exists)
        list.addIfAbsent("E");  // Will add (doesn't exist)
        System.out.println("After addIfAbsent: " + list);
        
        System.out.println();
    }
    
    /**
     * BLOCKINGQUEUE DEMONSTRATION
     * 
     * BlockingQueue is a thread-safe queue with blocking operations.
     * 
     * Key Features:
     * - Thread-safe: Multiple threads can add/remove
     * - Blocking operations: Blocks thread if queue is full/empty
     * - Producer-Consumer pattern support
     * 
     * Main Implementations:
     * - ArrayBlockingQueue: Bounded queue (fixed size)
     * - LinkedBlockingQueue: Can be bounded or unbounded
     * - PriorityBlockingQueue: Priority-based ordering
     * 
     * Operations:
     * - put(): Add element, blocks if queue is full
     * - take(): Remove element, blocks if queue is empty
     * - offer(): Add element, returns false if queue is full (non-blocking)
     * - poll(): Remove element, returns null if queue is empty (non-blocking)
     */
    public static void demonstrateBlockingQueue() throws InterruptedException {
        System.out.println("=== BLOCKINGQUEUE ===");
        
        // Create bounded BlockingQueue (capacity: 3)
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                queue.put("Item1");
                System.out.println("Produced: Item1");
                queue.put("Item2");
                System.out.println("Produced: Item2");
                queue.put("Item3");
                System.out.println("Produced: Item3");
                queue.put("Item4");  // Will block until space available
                System.out.println("Produced: Item4");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(2000);  // Wait before consuming
                String item = queue.take();
                System.out.println("Consumed: " + item);
                item = queue.take();
                System.out.println("Consumed: " + item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
        
        System.out.println("Final queue: " + queue);
        System.out.println();
    }
    
    /**
     * CONCURRENTLINKEDQUEUE DEMONSTRATION
     * 
     * ConcurrentLinkedQueue is a lock-free thread-safe queue.
     * 
     * Key Features:
     * - Lock-free: Uses CAS (Compare-And-Swap) operations
     * - High performance: No locking overhead
     * - Unbounded: Can grow indefinitely
     * - Non-blocking: Operations don't block
     * 
     * Best For:
     * - High-concurrency scenarios
     * - When you need lock-free operations
     * - Producer-Consumer with high throughput
     */
    public static void demonstrateConcurrentLinkedQueue() {
        System.out.println("=== CONCURRENTLINKEDQUEUE ===");
        
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        
        // Add elements
        queue.offer("A");
        queue.offer("B");
        queue.offer("C");
        
        System.out.println("Queue: " + queue);
        
        // Remove elements
        String item = queue.poll();
        System.out.println("Polled: " + item);
        
        // Peek (don't remove)
        String peeked = queue.peek();
        System.out.println("Peeked: " + peeked);
        System.out.println("Queue after peek: " + queue);  // Still has elements
        
        System.out.println();
    }
    
    /**
     * COMPARISON: Thread Safety
     */
    public static void compareThreadSafety() {
        System.out.println("=== THREAD SAFETY COMPARISON ===");
        
        System.out.println("\nCollection Type          | Thread-Safe | Fail-Safe Iterator");
        System.out.println("--------------------------|-------------|-------------------");
        System.out.println("ArrayList                 | ❌ No       | ❌ No (Fail-Fast)");
        System.out.println("HashMap                   | ❌ No       | ❌ No (Fail-Fast)");
        System.out.println("HashSet                   | ❌ No       | ❌ No (Fail-Fast)");
        System.out.println("ConcurrentHashMap         | ✅ Yes      | ✅ Yes (Fail-Safe)");
        System.out.println("CopyOnWriteArrayList      | ✅ Yes      | ✅ Yes (Fail-Safe)");
        System.out.println("CopyOnWriteArraySet       | ✅ Yes      | ✅ Yes (Fail-Safe)");
        System.out.println("BlockingQueue             | ✅ Yes      | ✅ Yes");
        System.out.println("ConcurrentLinkedQueue     | ✅ Yes      | ✅ Yes");
        
        System.out.println("\n📝 Key Points:");
        System.out.println("1. Regular collections are NOT thread-safe");
        System.out.println("2. Use concurrent collections in multi-threaded environments");
        System.out.println("3. ConcurrentHashMap > synchronized HashMap (better performance)");
        System.out.println("4. CopyOnWriteArrayList is good for read-heavy scenarios");
        System.out.println();
    }
}


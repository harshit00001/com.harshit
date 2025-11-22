package com.harshit.collections.basics;

import java.util.*;

/**
 * QUEUE BASICS - Understanding Java Queue Interface
 * 
 * The Queue interface represents a collection designed for holding elements prior to processing.
 * Queues typically follow the First-In-First-Out (FIFO) principle, where elements are added at one end
 * and removed from the other end. Think of it like a line at a grocery store - the first person to
 * join the line is the first person to be served.
 * 
 * The Queue interface extends the Collection interface and provides additional operations for inserting,
 * removing, and examining elements. These operations come in two forms: one that throws an exception
 * if the operation fails, and one that returns a special value (null or false) if the operation fails.
 * 
 * The main Queue implementations include LinkedList, PriorityQueue, and ArrayDeque. LinkedList implements
 * both List and Queue interfaces, making it versatile but not always the best choice for queue operations.
 * PriorityQueue stores elements according to their natural ordering or a provided Comparator, not in
 * insertion order. ArrayDeque is a resizable array implementation that can be used as both a queue and
 * a stack, and it's generally faster than LinkedList for queue operations.
 */
public class QueueBasics {
    
    public static void main(String[] args) {
        demonstrateBasicQueue();
        demonstratePriorityQueue();
        demonstrateArrayDeque();
        compareQueueImplementations();
    }
    
    /**
     * BASIC QUEUE OPERATIONS DEMONSTRATION
     * 
     * This method demonstrates the fundamental operations of a Queue. The Queue interface provides
     * methods for adding elements, removing elements, and examining elements. Each operation comes
     * in two forms: one that throws an exception if it fails, and one that returns a special value.
     * 
     * The add() method adds an element to the queue and throws an IllegalStateException if the
     * queue is full. The offer() method also adds an element but returns false if the queue is full
     * instead of throwing an exception. The remove() method removes and returns the head of the queue,
     * throwing a NoSuchElementException if the queue is empty. The poll() method does the same but
     * returns null if the queue is empty. The element() method returns the head without removing it,
     * throwing an exception if empty, while peek() returns null if empty.
     */
    public static void demonstrateBasicQueue() {
        System.out.println("=== BASIC QUEUE OPERATIONS ===");
        
        // Create a Queue using LinkedList
        Queue<String> queue = new LinkedList<>();
        
        // Adding elements
        queue.add("First");   // Throws exception if fails
        queue.offer("Second"); // Returns false if fails
        queue.offer("Third");
        queue.offer("Fourth");
        
        System.out.println("Queue: " + queue);
        
        // Examining the head
        System.out.println("Head element (element()): " + queue.element()); // Throws exception if empty
        System.out.println("Head element (peek()): " + queue.peek());      // Returns null if empty
        
        // Removing elements
        System.out.println("Removed (remove()): " + queue.remove()); // Throws exception if empty
        System.out.println("Removed (poll()): " + queue.poll());     // Returns null if empty
        
        System.out.println("Queue after removals: " + queue);
        
        // Iterating through queue
        System.out.println("\nIterating queue:");
        while (!queue.isEmpty()) {
            System.out.println("  Processing: " + queue.poll());
        }
        
        System.out.println();
    }
    
    /**
     * PRIORITYQUEUE DEMONSTRATION
     * 
     * PriorityQueue is a special type of queue where elements are ordered according to their priority,
     * not their insertion order. The head of the queue is always the element with the highest priority
     * according to the natural ordering or a provided Comparator. When you remove elements, they are
     * removed in priority order, not insertion order.
     * 
     * PriorityQueue is implemented using a heap data structure, which provides efficient insertion and
     * removal of the highest priority element. The time complexity for insertion and removal is O(log n),
     * which is slower than a regular queue but necessary for maintaining priority order.
     * 
     * PriorityQueue is useful for scenarios where you need to process elements based on priority, such
     * as task scheduling, event processing, or implementing algorithms like Dijkstra's shortest path.
     */
    public static void demonstratePriorityQueue() {
        System.out.println("=== PRIORITYQUEUE DEMONSTRATION ===");
        
        // Natural ordering (ascending for numbers)
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.offer(5);
        priorityQueue.offer(2);
        priorityQueue.offer(8);
        priorityQueue.offer(1);
        priorityQueue.offer(9);
        
        System.out.println("PriorityQueue (natural order - smallest first): " + priorityQueue);
        System.out.println("Note: Order may not be visible in toString(), but head is always smallest");
        
        System.out.println("\nRemoving elements (in priority order):");
        while (!priorityQueue.isEmpty()) {
            System.out.println("  Removed: " + priorityQueue.poll());
        }
        
        // Custom comparator (descending order)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(2);
        maxHeap.offer(8);
        maxHeap.offer(1);
        maxHeap.offer(9);
        
        System.out.println("\nPriorityQueue (reverse order - largest first):");
        while (!maxHeap.isEmpty()) {
            System.out.println("  Removed: " + maxHeap.poll());
        }
        
        // PriorityQueue with custom objects
        PriorityQueue<Task> taskQueue = new PriorityQueue<>((t1, t2) -> t1.priority - t2.priority);
        taskQueue.offer(new Task("Low priority task", 3));
        taskQueue.offer(new Task("High priority task", 1));
        taskQueue.offer(new Task("Medium priority task", 2));
        
        System.out.println("\nTask queue (by priority):");
        while (!taskQueue.isEmpty()) {
            System.out.println("  Processing: " + taskQueue.poll());
        }
        
        System.out.println();
    }
    
    /**
     * ARRAYDEQUE DEMONSTRATION
     * 
     * ArrayDeque is a resizable array implementation of the Deque interface, which extends Queue.
     * Deque stands for "double-ended queue" and allows insertion and removal from both ends.
     * ArrayDeque can be used as both a queue (FIFO) and a stack (LIFO - Last In First Out).
     * 
     * ArrayDeque is generally faster than LinkedList for queue operations because it uses an array
     * internally, which provides better cache locality and avoids the overhead of node objects.
     * ArrayDeque is not thread-safe, but it's more efficient than LinkedList for single-threaded
     * queue and stack operations.
     * 
     * ArrayDeque doesn't allow null elements, which is different from LinkedList. This makes it
     * suitable for scenarios where you need a fast, non-thread-safe queue or stack implementation.
     */
    public static void demonstrateArrayDeque() {
        System.out.println("=== ARRAYDEQUE DEMONSTRATION ===");
        
        // Using as Queue (FIFO)
        System.out.println("Using ArrayDeque as Queue (FIFO):");
        Deque<String> queue = new ArrayDeque<>();
        queue.offerLast("First");
        queue.offerLast("Second");
        queue.offerLast("Third");
        
        System.out.println("Queue: " + queue);
        System.out.println("Removed from front: " + queue.pollFirst());
        System.out.println("Removed from front: " + queue.pollFirst());
        System.out.println("Queue after removals: " + queue);
        
        // Using as Stack (LIFO)
        System.out.println("\nUsing ArrayDeque as Stack (LIFO):");
        Deque<String> stack = new ArrayDeque<>();
        stack.push("First");   // push() adds to front
        stack.push("Second");
        stack.push("Third");
        
        System.out.println("Stack: " + stack);
        System.out.println("Popped: " + stack.pop());  // pop() removes from front
        System.out.println("Popped: " + stack.pop());
        System.out.println("Stack after pops: " + stack);
        
        // Double-ended operations
        System.out.println("\nDouble-ended operations:");
        Deque<String> deque = new ArrayDeque<>();
        deque.offerFirst("Front1");
        deque.offerLast("Back1");
        deque.offerFirst("Front2");
        deque.offerLast("Back2");
        
        System.out.println("Deque: " + deque);
        System.out.println("Removed from front: " + deque.pollFirst());
        System.out.println("Removed from back: " + deque.pollLast());
        System.out.println("Deque after removals: " + deque);
        
        System.out.println();
    }
    
    /**
     * COMPARISON: Queue Implementations
     */
    public static void compareQueueImplementations() {
        System.out.println("=== QUEUE IMPLEMENTATIONS COMPARISON ===");
        
        System.out.println("\nQueue Implementation | Order        | Null Allowed | Thread-Safe | Use Case");
        System.out.println("---------------------|--------------|--------------|-------------|------------------");
        System.out.println("LinkedList           | Insertion    | Yes          | No          | General purpose");
        System.out.println("PriorityQueue        | Priority     | No           | No          | Priority-based");
        System.out.println("ArrayDeque           | Insertion    | No           | No          | Fast queue/stack");
        System.out.println("ConcurrentLinkedQueue| Insertion    | No           | Yes         | Thread-safe queue");
        System.out.println("BlockingQueue        | Insertion    | No           | Yes         | Producer-consumer");
        
        System.out.println("\nKey Points:");
        System.out.println("1. LinkedList: Versatile but slower, allows null");
        System.out.println("2. PriorityQueue: Elements ordered by priority, not insertion order");
        System.out.println("3. ArrayDeque: Fast, no null, can be used as queue or stack");
        System.out.println("4. Use ArrayDeque instead of Stack class (Stack is legacy)");
        System.out.println("5. Use PriorityQueue when you need priority-based processing");
        System.out.println();
    }
    
    /**
     * Helper class for PriorityQueue demonstration
     */
    static class Task {
        String name;
        int priority; // Lower number = higher priority
        
        Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
        
        @Override
        public String toString() {
            return name + " (Priority: " + priority + ")";
        }
    }
}


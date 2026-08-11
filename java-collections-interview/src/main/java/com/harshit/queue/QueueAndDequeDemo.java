package com.harshit.queue;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * INTERVIEW Q: PriorityQueue vs ArrayDeque vs LinkedList for queue/stack?
 *
 * <p><b>PriorityQueue</b> — binary heap. Elements ordered by natural order or Comparator.
 * poll() returns highest-priority (or lowest per comparator). NOT FIFO by insertion time.
 * offer/poll O(log n), peek O(1). Iterator does NOT return sorted order!
 *
 * <p><b>ArrayDeque</b> — circular array. Fast stack (push/pop) and queue (offer/poll).
 * Preferred over legacy {@code Stack} and often over LinkedList.
 *
 * <p><b>INTERVIEW Q: Why avoid Stack class?</b>
 * ANSWER: Legacy, extends Vector (unnecessary synchronization), LIFO API only.
 * ArrayDeque is faster and more flexible.
 */
public final class QueueAndDequeDemo {

    private QueueAndDequeDemo() {}

    public static void main(String[] args) {
        fifoQueueDemo();
        stackDemo();
        priorityQueueDemo();
        topKWithPriorityQueue();
    }

    /**
     * FIFO queue — ArrayDeque is the modern default for Queue operations.
     */
    private static void fifoQueueDemo() {
        System.out.println("=== FIFO Queue (ArrayDeque) ===");

        Queue<String> queue = new ArrayDeque<>();
        queue.offer("Task-1");
        queue.offer("Task-2");
        queue.offer("Task-3");

        System.out.println("poll: " + queue.poll()); // Task-1 — first in, first out
        System.out.println("peek: " + queue.peek()); // Task-2 — inspect without remove
        System.out.println("remaining: " + queue);
        System.out.println();
    }

    /**
     * LIFO stack — ArrayDeque push/pop. O(1) amortized.
     */
    private static void stackDemo() {
        System.out.println("=== Stack (ArrayDeque) ===");

        ArrayDeque<String> stack = new ArrayDeque<>();
        stack.push("bottom");
        stack.push("middle");
        stack.push("top");

        System.out.println("pop: " + stack.pop());   // top
        System.out.println("peek: " + stack.peek());   // middle
        System.out.println("→ Use ArrayDeque, not java.util.Stack.");
        System.out.println();
    }

    /**
     * PriorityQueue — "urgent" tasks processed first regardless of arrival order.
     * Default: natural ordering (Comparable) — for Integer, smallest first (min-heap).
     */
    private static void priorityQueueDemo() {
        System.out.println("=== PriorityQueue ===");

        Queue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(30);
        minHeap.offer(10);
        minHeap.offer(20);

        System.out.print("poll order (ascending): ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " "); // 10 20 30
        }
        System.out.println();

        // Max-heap via reversed comparator
        Queue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(java.util.List.of(30, 10, 20));
        System.out.print("poll order (descending): ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " "); // 30 20 10
        }
        System.out.println();

        // TRAP: iteration order ≠ priority order
        PriorityQueue<Integer> pq = new PriorityQueue<>(java.util.List.of(5, 1, 3));
        System.out.println("Iterator (NOT sorted): " + pq); // e.g. [1, 5, 3] — heap internal order
        System.out.println();
    }

    /**
     * Accenture scenario: find top 3 scores from stream without sorting entire list O(n log n).
     * Maintain min-heap of size K → O(n log K) time, O(K) space.
     */
    private static void topKWithPriorityQueue() {
        System.out.println("=== Top-K with PriorityQueue (interview scenario) ===");

        int[] scores = {45, 92, 78, 95, 88, 61, 99, 73};
        int k = 3;

        // Min-heap of size k — root is smallest of top-k → evict when better score arrives
        PriorityQueue<Integer> topK = new PriorityQueue<>(k);

        for (int score : scores) {
            topK.offer(score);
            if (topK.size() > k) {
                topK.poll(); // remove smallest — keeps largest k elements
            }
        }

        System.out.println("Top " + k + " scores: " + topK); // unsorted heap view
        System.out.println("Extract best-first: ");
        while (!topK.isEmpty()) {
            System.out.println("  " + topK.poll());
        }
    }
}

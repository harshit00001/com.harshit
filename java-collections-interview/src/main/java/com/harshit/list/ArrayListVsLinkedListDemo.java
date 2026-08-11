package com.harshit.list;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * INTERVIEW Q: ArrayList vs LinkedList — when to use which?
 *
 * <p><b>ArrayList</b> (dynamic array):
 * <ul>
 *   <li>get(index) — O(1) — contiguous memory, CPU cache friendly</li>
 *   <li>add at end — amortized O(1) — occasional resize copy</li>
 *   <li>add/remove in middle — O(n) — elements shift</li>
 * </ul>
 *
 * <p><b>LinkedList</b> (doubly linked nodes):
 * <ul>
 *   <li>get(index) — O(n) — must traverse from head/tail</li>
 *   <li>add at head/tail — O(1) if you have the node reference</li>
 *   <li>add/remove in middle — O(1) IF you already hold a ListIterator at that node</li>
 * </ul>
 *
 * <p><b>4 YOE answer:</b> Default to ArrayList. LinkedList rarely wins in real benchmarks
 * due to node allocation overhead and poor cache locality. Use ArrayDeque for stack/queue.
 */
public final class ArrayListVsLinkedListDemo {

    private ArrayListVsLinkedListDemo() {}

    public static void main(String[] args) {
        randomAccessComparison();
        insertionAtBeginningComparison();
        iterationComparison();
    }

    /**
     * Random access is ArrayList's strength — direct index into backing array.
     */
    private static void randomAccessComparison() {
        System.out.println("=== RANDOM ACCESS ===");

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        for (int i = 0; i < 100_000; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long start = System.nanoTime();
        for (int i = 0; i < 10_000; i++) {
            arrayList.get(50_000); // O(1)
        }
        long arrayListTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < 10_000; i++) {
            linkedList.get(50_000); // O(n) — walks ~50k nodes each time
        }
        long linkedListTime = System.nanoTime() - start;

        System.out.printf("ArrayList get(50000) x10000: %,d ns%n", arrayListTime);
        System.out.printf("LinkedList get(50000) x10000: %,d ns%n", linkedListTime);
        System.out.println("→ ArrayList wins dramatically for index-based reads.");
        System.out.println();
    }

    /**
     * Textbooks say LinkedList wins for insert-at-beginning.
     * In practice, ArrayList's small-array case + JVM optimizations often compete;
     * at scale, LinkedList still allocates a node per element (GC pressure).
     */
    private static void insertionAtBeginningComparison() {
        System.out.println("=== INSERT AT INDEX 0 ===");

        int n = 20_000;

        List<Integer> arrayList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            arrayList.add(0, i); // O(n) each — shifts all elements right
        }
        long arrayListTime = System.nanoTime() - start;

        List<Integer> linkedList = new LinkedList<>();
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            linkedList.add(0, i); // O(1) for linked structure, but still node allocation
        }
        long linkedListTime = System.nanoTime() - start;

        System.out.printf("ArrayList add(0) x%d: %,d ns%n", n, arrayListTime);
        System.out.printf("LinkedList add(0) x%d: %,d ns%n", n, linkedListTime);
        System.out.println("→ LinkedList can win here, but profile before choosing.");
        System.out.println();
    }

    /**
     * Sequential iteration: ArrayList wins due to memory locality (array contiguous in heap).
     */
    private static void iterationComparison() {
        System.out.println("=== ITERATION ===");

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < 200_000; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long start = System.nanoTime();
        long sum = 0;
        for (int value : arrayList) {
            sum += value;
        }
        long arrayListTime = System.nanoTime() - start;

        start = System.nanoTime();
        sum = 0;
        for (int value : linkedList) {
            sum += value;
        }
        long linkedListTime = System.nanoTime() - start;

        System.out.printf("ArrayList foreach sum: %,d ns (sum=%d)%n", arrayListTime, sum);
        System.out.printf("LinkedList foreach sum: %,d ns%n", linkedListTime);
        System.out.println("→ Prefer ArrayList for typical CRUD + iteration workloads.");
    }
}

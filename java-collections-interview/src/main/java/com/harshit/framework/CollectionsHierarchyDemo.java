package com.harshit.framework;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * INTERVIEW Q: What is the Java Collections Framework?
 * ANSWER: A unified architecture of interfaces and classes to store and manipulate
 * groups of objects. Core interfaces: Collection, List, Set, Queue, Map.
 *
 * <p>INTERVIEW Q: List vs Set vs Map?
 * <ul>
 *   <li>List — ordered, allows duplicates, index-based access</li>
 *   <li>Set — no duplicates, uniqueness enforced</li>
 *   <li>Map — key-value pairs, keys are unique</li>
 * </ul>
 *
 * <p>INTERVIEW Q: Is Map part of Collection?
 * ANSWER: No. Map is a separate interface. Collection extends Iterable; Map does not.
 */
public final class CollectionsHierarchyDemo {

    private CollectionsHierarchyDemo() {}

    public static void main(String[] args) {
        demonstrateList();
        demonstrateSet();
        demonstrateMap();
        demonstrateQueue();
        demonstratePolymorphism();
    }

    /**
     * List = sequence. Duplicates allowed. Common impl: ArrayList (default choice).
     */
    private static void demonstrateList() {
        System.out.println("=== LIST ===");

        // ArrayList: dynamic array backing — fast random access O(1)
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Java");
        arrayList.add("Collections");
        arrayList.add("Java"); // duplicate allowed

        // LinkedList: doubly-linked nodes — fast insert at ends, slow get(index)
        List<String> linkedList = new LinkedList<>(arrayList);

        System.out.println("ArrayList size (with duplicate): " + arrayList.size()); // 3
        System.out.println("Index 0: " + arrayList.get(0)); // O(1) for ArrayList
        System.out.println("LinkedList same content: " + linkedList);
        System.out.println();
    }

    /**
     * Set = mathematical set. No duplicates.
     * HashSet (unordered), LinkedHashSet (insertion order), TreeSet (sorted).
     */
    private static void demonstrateSet() {
        System.out.println("=== SET ===");

        Set<String> hashSet = new HashSet<>();
        hashSet.add("A");
        hashSet.add("B");
        hashSet.add("A"); // ignored — Set uses equals() to detect duplicate

        System.out.println("HashSet after adding A twice: " + hashSet); // [A, B] order not guaranteed
        System.out.println("contains(\"A\"): " + hashSet.contains("A")); // O(1) average for HashSet
        System.out.println();
    }

    /**
     * Map = associative array. One key maps to one value (key uniqueness).
     * Not a Collection — you iterate entrySet/keySet/values separately.
     */
    private static void demonstrateMap() {
        System.out.println("=== MAP ===");

        Map<Integer, String> employees = new HashMap<>();
        employees.put(101, "Alice");
        employees.put(102, "Bob");
        employees.put(101, "Alice Updated"); // same key → value replaced, not duplicated

        System.out.println("Map size: " + employees.size()); // 2
        System.out.println("get(101): " + employees.get(101));
        System.out.println("keySet: " + employees.keySet());
        System.out.println();
    }

    /**
     * Queue = FIFO (typically). Deque = double-ended queue (stack + queue).
     * Prefer ArrayDeque over legacy Stack class.
     */
    private static void demonstrateQueue() {
        System.out.println("=== QUEUE / DEQUE ===");

        Queue<String> queue = new ArrayDeque<>();
        queue.offer("first");  // enqueue
        queue.offer("second");
        System.out.println("poll (dequeue): " + queue.poll()); // "first" — FIFO

        // ArrayDeque also works as stack: push/pop
        ArrayDeque<String> stack = new ArrayDeque<>();
        stack.push("bottom");
        stack.push("top");
        System.out.println("stack pop: " + stack.pop()); // LIFO — "top"
        System.out.println();
    }

    /**
     * INTERVIEW Q: Why program to interfaces (List, Map) not implementations?
     * ANSWER: Swap implementations without changing client code; easier testing/mocking.
     */
    private static void demonstratePolymorphism() {
        System.out.println("=== POLYMORPHISM ===");

        // Method accepts Collection — works with List OR Set
        printSize(new ArrayList<>(List.of("x", "y")));
        printSize(new HashSet<>(Set.of("x", "y")));

        // Map is NOT a Collection — separate hierarchy
        Map<String, Integer> map = new HashMap<>();
        map.put("score", 95);
        System.out.println("Map is not a Collection, but entrySet is: " + map.entrySet().getClass());
    }

    private static void printSize(Collection<?> collection) {
        System.out.println(collection.getClass().getSimpleName() + " size = " + collection.size());
    }
}

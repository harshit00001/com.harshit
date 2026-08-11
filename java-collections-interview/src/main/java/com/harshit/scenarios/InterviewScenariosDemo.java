package com.harshit.scenarios;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Accenture-style scenario questions (4 YOE) with collection choices explained in comments.
 *
 * <p>Each method answers: "Which collection and why?"
 */
public final class InterviewScenariosDemo {

    private InterviewScenariosDemo() {}

    public static void main(String[] args) {
        uniqueEmployeeIdsFastLookup();
        uniqueUsersInArrivalOrder();
        sortedUniqueScores();
        concurrentMetricsCounter();
        undoStack();
        deduplicateApiResponses();
    }

    /**
     * Q: Store unique employee IDs with O(1) lookup — which collection?
     * A: HashSet (or HashMap if you need associated data).
     */
    private static void uniqueEmployeeIdsFastLookup() {
        System.out.println("=== Scenario: unique employee IDs, fast lookup ===");

        Set<Integer> employeeIds = new HashSet<>();
        employeeIds.add(101);
        employeeIds.add(102);
        employeeIds.add(101); // duplicate ignored

        System.out.println("contains(101): " + employeeIds.contains(101)); // O(1) average
        System.out.println("Choice: HashSet — uniqueness + fast contains/add.");
        System.out.println();
    }

    /**
     * Q: Track unique users in order they first appeared?
     * A: LinkedHashSet — HashSet uniqueness + insertion-order linked list.
     */
    private static void uniqueUsersInArrivalOrder() {
        System.out.println("=== Scenario: unique users in arrival order ===");

        Set<String> visitors = new LinkedHashSet<>();
        visitors.add("user-A");
        visitors.add("user-B");
        visitors.add("user-A"); // already seen — order unchanged
        visitors.add("user-C");

        System.out.println("Visitors: " + visitors); // [user-A, user-B, user-C]
        System.out.println("Choice: LinkedHashSet.");
        System.out.println();
    }

    /**
     * Q: Maintain sorted unique scores with range queries?
     * A: TreeSet — red-black tree, O(log n), navigable (subSet, ceiling, floor).
     */
    private static void sortedUniqueScores() {
        System.out.println("=== Scenario: sorted unique scores ===");

        TreeSet<Integer> scores = new TreeSet<>(List.of(85, 92, 78, 92, 88));
        System.out.println("Sorted unique: " + scores); // [78, 85, 88, 92]

        // Range query — common follow-up at 4 YOE
        System.out.println("Scores 80-90: " + scores.subSet(80, true, 90, true));
        System.out.println("Choice: TreeSet for sorted uniqueness + range ops.");
        System.out.println();
    }

    /**
     * Q: 50k req/s updating shared in-memory metrics map?
     * A: ConcurrentHashMap + merge/compute — NOT synchronized HashMap or Hashtable.
     */
    private static void concurrentMetricsCounter() {
        System.out.println("=== Scenario: concurrent API metrics ===");

        Map<String, Long> requestCounts = new ConcurrentHashMap<>();

        // Simulated concurrent-safe increment per endpoint
        requestCounts.merge("/api/orders", 1L, Long::sum);
        requestCounts.merge("/api/orders", 1L, Long::sum);
        requestCounts.merge("/api/users", 1L, Long::sum);

        System.out.println("Metrics: " + requestCounts);
        System.out.println("Choice: ConcurrentHashMap.merge — atomic, fine-grained locking.");
        System.out.println("Alt for extreme contention on single counter: LongAdder.");
        System.out.println();
    }

    /**
     * Q: Implement undo for text editor?
     * A: ArrayDeque as stack — O(1) push/pop, better than Stack/LinkedList typically.
     */
    private static void undoStack() {
        System.out.println("=== Scenario: undo stack ===");

        ArrayDeque<String> undoStack = new ArrayDeque<>();
        undoStack.push("Hello");
        undoStack.push("Hello World");
        undoStack.push("Hello World!");

        System.out.println("Undo: " + undoStack.pop()); // revert to "Hello World"
        System.out.println("Current: " + undoStack.peek());
        System.out.println("Choice: ArrayDeque (stack API).");
        System.out.println();
    }

    /**
     * Q: Deduplicate API responses by composite business key before batch insert?
     * A: HashMap with immutable composite key (correct equals/hashCode).
     */
    private static void deduplicateApiResponses() {
        System.out.println("=== Scenario: dedupe API batch by business key ===");

        record OrderKey(String customerId, String productId) {}

        Map<OrderKey, String> deduped = new HashMap<>();
        deduped.put(new OrderKey("C1", "P1"), "payload-v1");
        deduped.put(new OrderKey("C1", "P1"), "payload-v2"); // replaces — one entry per key
        deduped.put(new OrderKey("C2", "P1"), "payload-v3");

        System.out.println("Deduped batch size: " + deduped.size()); // 2
        System.out.println("Choice: HashMap + immutable record key.");

        // Bonus: top-5 revenue products from huge list — PriorityQueue size 5
        record Product(String name, double revenue) {}

        List<Product> catalog = List.of(
                new Product("A", 100), new Product("B", 500),
                new Product("C", 300), new Product("D", 800), new Product("E", 200),
                new Product("F", 900)
        );

        PriorityQueue<Product> top5 = new PriorityQueue<>(5, Comparator.comparingDouble(Product::revenue));
        for (Product p : catalog) {
            top5.offer(p);
            if (top5.size() > 5) top5.poll();
        }
        System.out.println("Top products by revenue (min-heap trick): " + top5);
    }
}

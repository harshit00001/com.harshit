package com.harshit.set;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * INTERVIEW Q: HashSet vs LinkedHashSet vs TreeSet?
 *
 * <table>
 *   <tr><th>Implementation</th><th>Order</th><th>Performance</th><th>Null?</th></tr>
 *   <tr><td>HashSet</td><td>None guaranteed</td><td>O(1) avg add/contains</td><td>One null allowed</td></tr>
 *   <tr><td>LinkedHashSet</td><td>Insertion order</td><td>O(1) avg + linked list overhead</td><td>One null allowed</td></tr>
 *   <tr><td>TreeSet</td><td>Sorted (natural or Comparator)</td><td>O(log n)</td><td>Null not allowed (NPE)</td></tr>
 * </table>
 *
 * <p>INTERVIEW Q: How does HashSet work internally?
 * ANSWER: Backed by HashMap — element is the key, dummy PRESENT object is the value.
 */
public final class SetVariantsDemo {

    private SetVariantsDemo() {}

    public static void main(String[] args) {
        hashSetDemo();
        linkedHashSetDemo();
        treeSetDemo();
        treeSetCompareToTrap();
    }

    private static void hashSetDemo() {
        System.out.println("=== HashSet ===");

        Set<String> set = new HashSet<>();
        set.add("Banana");
        set.add("Apple");
        set.add("Cherry");
        set.add("Apple"); // duplicate ignored

        // Order NOT guaranteed — depends on hash buckets
        System.out.println("HashSet: " + set);
        System.out.println("contains(\"Apple\"): " + set.contains("Apple")); // O(1) average
        System.out.println();
    }

    /**
     * LinkedHashSet = HashSet + doubly-linked list for predictable iteration order.
     * Use when you need uniqueness AND insertion-order (e.g., unique visit log).
     */
    private static void linkedHashSetDemo() {
        System.out.println("=== LinkedHashSet (insertion order) ===");

        Set<String> set = new LinkedHashSet<>();
        set.add("First");
        set.add("Second");
        set.add("Third");
        set.add("First"); // ignored

        System.out.println("LinkedHashSet: " + set); // [First, Second, Third]
        System.out.println();
    }

    /**
     * TreeSet = Red-Black tree. Sorted iteration. O(log n) operations.
     * Requires Comparable OR Comparator.
     */
    private static void treeSetDemo() {
        System.out.println("=== TreeSet (sorted) ===");

        Set<Integer> scores = new TreeSet<>();
        scores.add(85);
        scores.add(92);
        scores.add(78);
        scores.add(92); // duplicate

        System.out.println("TreeSet ascending: " + scores); // [78, 85, 92]

        // Custom comparator — descending order
        Set<Integer> descending = new TreeSet<>(Comparator.reverseOrder());
        descending.addAll(scores);
        System.out.println("TreeSet descending: " + descending);
        System.out.println();
    }

    /**
     * INTERVIEW TRAP (4 YOE): TreeSet uniqueness is defined by compareTo/Comparator,
     * NOT equals(). If compareTo returns 0 but equals is false → only one element stored!
     *
     * <p>Rule: compareTo consistent with equals for TreeSet/TreeMap keys.
     */
    private static void treeSetCompareToTrap() {
        System.out.println("=== TreeSet compareTo vs equals trap ===");

        Set<InconsistentEmployee> set = new TreeSet<>();
        set.add(new InconsistentEmployee("Harshit", 50000));
        set.add(new InconsistentEmployee("Harshit", 60000)); // same name, different salary

        // compareTo only uses name → returns 0 → second add treated as duplicate!
        System.out.println("Set size (expected 2, actual): " + set.size()); // 1
        System.out.println("→ compareTo says 'equal' but equals would say 'different' — data loss risk.");
    }

    /**
     * Anti-pattern: compareTo uses only name, equals uses name + salary.
     * TreeSet uses compareTo for ordering AND uniqueness.
     */
    static final class InconsistentEmployee implements Comparable<InconsistentEmployee> {
        private final String name;
        private final int salary;

        InconsistentEmployee(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }

        @Override
        public int compareTo(InconsistentEmployee other) {
            return this.name.compareTo(other.name); // ignores salary
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof InconsistentEmployee other)) return false;
            return salary == other.salary && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode() ^ salary;
        }

        @Override
        public String toString() {
            return name + "($" + salary + ")";
        }
    }
}

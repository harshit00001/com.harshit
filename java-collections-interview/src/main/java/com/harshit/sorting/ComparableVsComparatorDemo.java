package com.harshit.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * INTERVIEW Q: Comparable vs Comparator?
 *
 * <p><b>Comparable</b> — natural ordering inside the class ({@code compareTo}).
 * One sort order per class. Used by TreeSet, TreeMap, Collections.sort implicitly.
 *
 * <p><b>Comparator</b> — external strategy ({@code compare}). Multiple sort orders.
 * Prefer for DTOs where you don't want sorting logic in domain object.
 *
 * <p>INTERVIEW Q: Should compareTo be consistent with equals?
 * ANSWER: Strongly recommended for TreeSet/TreeMap. If compareTo==0 but !equals,
 * TreeSet treats them as duplicates → subtle data loss.
 *
 * <p>INTERVIEW Q: Is List.sort stable?
 * ANSWER: Yes (TimSort). Equal elements retain relative order.
 */
public final class ComparableVsComparatorDemo {

    private ComparableVsComparatorDemo() {}

    public static void main(String[] args) {
        comparableDemo();
        comparatorDemo();
        multiFieldComparator();
        consistencyTrap();
    }

    /**
     * Comparable = object knows its natural order (e.g., names alphabetically).
     */
    private static void comparableDemo() {
        System.out.println("=== Comparable (natural order) ===");

        List<Employee> employees = new ArrayList<>(List.of(
                new Employee("Charlie", 70000),
                new Employee("Alice", 90000),
                new Employee("Bob", 80000)
        ));

        // Employee implements Comparable — sort by name
        employees.sort(null); // null → natural order (Comparable)
        // Same as: Collections.sort(employees) or employees.sort(Employee::compareTo)

        System.out.println("Sorted by name (Comparable):");
        employees.forEach(System.out::println);
        System.out.println();
    }

    /**
     * Comparator = external, flexible, chainable (Java 8+).
     */
    private static void comparatorDemo() {
        System.out.println("=== Comparator (custom order) ===");

        List<Employee> employees = new ArrayList<>(List.of(
                new Employee("Charlie", 70000),
                new Employee("Alice", 90000),
                new Employee("Bob", 80000)
        ));

        // Sort by salary descending — no change to Employee class needed
        employees.sort(Comparator.comparingInt(Employee::salary).reversed());

        System.out.println("Sorted by salary desc (Comparator):");
        employees.forEach(System.out::println);
        System.out.println();
    }

    /**
     * Multi-field sort: salary desc, then name asc — common interview follow-up.
     */
    private static void multiFieldComparator() {
        System.out.println("=== Multi-field Comparator ===");

        List<Employee> employees = new ArrayList<>(List.of(
                new Employee("Alice", 80000),
                new Employee("Bob", 80000),
                new Employee("Charlie", 90000)
        ));

        employees.sort(
                Comparator.comparingInt(Employee::salary).reversed()
                        .thenComparing(Employee::name)
        );

        employees.forEach(System.out::println);
        System.out.println();
    }

    /**
     * Demonstrates TreeSet rejecting second element when compareTo returns 0
     * even though equals would distinguish them.
     */
    private static void consistencyTrap() {
        System.out.println("=== compareTo vs equals in TreeSet ===");

        TreeSet<Version> versions = new TreeSet<>();
        versions.add(new Version(1, 0)); // major=1, minor=0
        versions.add(new Version(1, 5)); // major=1, minor=5

        // compareTo only compares major → both have major=1 → "equal" for TreeSet
        System.out.println("TreeSet size: " + versions.size()); // 1 not 2!
        System.out.println("→ Fix: include all identity fields in compareTo OR use HashSet if no sort needed.");
    }

    /** Natural order by name — good Comparable example. */
    record Employee(String name, int salary) implements Comparable<Employee> {
        @Override
        public int compareTo(Employee other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + " ($" + salary + ")";
        }
    }

    /** Inconsistent: compareTo uses major only, equals uses major + minor. */
    static final class Version implements Comparable<Version> {
        private final int major;
        private final int minor;

        Version(int major, int minor) {
            this.major = major;
            this.minor = minor;
        }

        @Override
        public int compareTo(Version other) {
            return Integer.compare(this.major, other.major); // ignores minor!
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Version other)) return false;
            return major == other.major && minor == other.minor;
        }

        @Override
        public int hashCode() {
            return 31 * major + minor;
        }
    }
}

package com.harshit.preparation.compare;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <h2>Comparable vs Comparator</h2>
 * <p>
 * {@link Comparable} — natural ordering defined <i>inside</i> the class ({@code compareTo}).
 * <p>
 * {@link Comparator} — external strategies (sort by name, salary, etc.) without changing the class.
 */
public final class ComparableComparatorDemo {

    public static void main(String[] args) {
        List<Employee> staff = new ArrayList<>(List.of(
                new Employee(2, "Bob"),
                new Employee(1, "Alice"),
                new Employee(3, "Chen")
        ));

        staff.sort(Comparator.naturalOrder());
        System.out.println("by id (Comparable): " + staff);

        staff.sort(Comparator.comparing(Employee::name));
        System.out.println("by name (Comparator): " + staff);
    }

    public record Employee(int id, String name) implements Comparable<Employee> {

        @Override
        public int compareTo(Employee o) {
            return Integer.compare(this.id, o.id);
        }
    }

    private ComparableComparatorDemo() {
    }
}

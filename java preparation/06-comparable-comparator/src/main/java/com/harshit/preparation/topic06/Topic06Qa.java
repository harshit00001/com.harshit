package com.harshit.preparation.topic06;

import java.util.Comparator;
import java.util.List;

/**
 * Topic 06 — Comparable vs Comparator.
 */
public final class Topic06Qa {

    private Topic06Qa() {
    }

    /*
     * Q: Difference between Comparable and Comparator?
     *
     * SCRIPT:
     * Comparable is implemented by the class itself and defines the natural ordering—compareTo is
     * the one method. Comparator is a separate strategy object that compares two instances; I can
     * have many comparators for the same class, for example sort employees by id, by name, or by
     * salary without changing the Employee class. In code I use Collections.sort(list) for natural
     * order, and Collections.sort(list, comparator) or stream().sorted(comparator) for alternate sorts.
     *
     * REAL LIFE:
     * A flight search has a default “best” ranking, but the user can switch to “cheapest” or “shortest”—those are different comparators.
     */

    public record Employee(int id, String name) implements Comparable<Employee> {
        @Override
        public int compareTo(Employee o) {
            return Integer.compare(id, o.id);
        }
    }

    public static void demo() {
        List<Employee> list = List.of(new Employee(2, "Bob"), new Employee(1, "Ann"));
        list.stream().sorted().forEach(System.out::println);
        list.stream().sorted(Comparator.comparing(Employee::name)).forEach(System.out::println);
    }

    public static void main(String[] args) {
        demo();
    }
}

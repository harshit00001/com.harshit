package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;
import com.harshit.interview.model.Employee;

import java.util.Comparator;
import java.util.List;

/**
 * INTERVIEW Q: Explain Comparator.comparing, thenComparing, reversed, nullsFirst/nullsLast.
 *
 * <p><b>Accenture coding:</b> Sort employees by department asc, then salary desc.
 */
public final class Q12ComparatorAndSorting implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q12: Comparator & Sorting ===\n");

        List<Employee> employees = List.of(
                new Employee("Raj", 1, "Pune", 70_000, "IT"),
                new Employee("Priya", 2, "Pune", 90_000, "HR"),
                new Employee("Amit", 3, "Pune", 80_000, "IT"),
                new Employee("Sneha", 4, "Pune", 85_000, "HR")
        );

        Comparator<Employee> byDeptThenSalary = Comparator
                .comparing(Employee::getDepartment)
                .thenComparing(Employee::getSalary, Comparator.reverseOrder());

        System.out.println("Dept asc, salary desc:");
        employees.stream()
                .sorted(byDeptThenSalary)
                .forEach(e -> System.out.println("  " + e.getDepartment() + " | "
                        + e.getName() + " | " + e.getSalary()));

        // null-safe sorting
        List<String> withNulls = new java.util.ArrayList<>(List.of("Zebra", null, "Apple", "Mango"));
        withNulls.sort(Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
        System.out.println("\nNulls last sort: " + withNulls);

        // comparingInt avoids boxing
        List<String> words = List.of("streams", "java", "api");
        words.stream()
                .sorted(Comparator.comparingInt(String::length))
                .forEach(w -> System.out.println("By length: " + w));

        System.out.println("\n→ thenComparing builds lexicographic sort keys — like ORDER BY col1, col2 DESC in SQL.");
    }

    public static void main(String[] args) throws Exception {
        new Q12ComparatorAndSorting().run();
    }
}

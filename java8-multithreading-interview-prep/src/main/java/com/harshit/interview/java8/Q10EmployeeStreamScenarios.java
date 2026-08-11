package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;
import com.harshit.interview.model.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * INTERVIEW Q (Accenture classic): Given List&lt;Employee&gt;, solve using Streams:
 *
 * <ol>
 *   <li>Count employees in a city</li>
 *   <li>Salary &gt; threshold</li>
 *   <li>Names uppercase + sorted</li>
 *   <li>First HR employee or throw</li>
 *   <li>Total salary by department</li>
 *   <li>Map name → salary</li>
 *   <li>Top N by salary</li>
 * </ol>
 *
 * <p><b>4 YOE SCRIPT:</b> "I use streams for declarative aggregation; I avoid shared mutable state
 * and collect to immutable structures at the end."
 */
public final class Q10EmployeeStreamScenarios implements InterviewDemo {

    private static List<Employee> employees() {
        return List.of(
                new Employee("Raj", 1, "Bengaluru", 85_000, "IT"),
                new Employee("Priya", 2, "Bengaluru", 92_000, "HR"),
                new Employee("Amit", 3, "Pune", 68_000, "IT"),
                new Employee("Sneha", 4, "Pune", 120_000, "Accounts"),
                new Employee("Kiran", 5, "Bengaluru", 55_000, "IT"),
                new Employee("Meera", 6, "Hyderabad", 95_000, "HR")
        );
    }

    @Override
    public void run() {
        System.out.println("=== Q10: Employee Stream Scenarios (Accenture) ===\n");
        List<Employee> employees = employees();

        // 1) Count in Bengaluru
        long blrCount = employees.stream()
                .filter(e -> "Bengaluru".equals(e.getCity()))
                .count();
        System.out.println("1) Bengaluru count: " + blrCount);

        // 2) Salary > 80k
        System.out.println("2) Salary > 80k:");
        employees.stream()
                .filter(e -> e.getSalary() > 80_000)
                .forEach(e -> System.out.println("   " + e.getName() + " -> " + e.getSalary()));

        // 3) Names uppercase, sorted
        List<String> names = employees.stream()
                .map(e -> e.getName().toUpperCase())
                .sorted()
                .toList();
        System.out.println("3) Names sorted upper: " + names);

        // 4) First HR or throw
        Employee hr = employees.stream()
                .filter(e -> "HR".equals(e.getDepartment()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No HR employee"));
        System.out.println("4) First HR: " + hr.getName());

        // 5) Total salary IT dept
        double itTotal = employees.stream()
                .filter(e -> "IT".equals(e.getDepartment()))
                .mapToDouble(Employee::getSalary)
                .sum();
        System.out.println("5) IT total salary: " + itTotal);

        // 6) Map name → salary (merge if duplicate names in real data)
        Map<String, Double> nameSalary = employees.stream()
                .collect(Collectors.toMap(Employee::getName, Employee::getSalary, Double::max));
        System.out.println("6) Name→Salary sample: " + nameSalary.get("Raj"));

        // 7) Top 2 earners
        System.out.println("7) Top 2 salaries:");
        employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(2)
                .forEach(e -> System.out.println("   " + e.getName() + " : " + e.getSalary()));

        // Bonus: allMatch / anyMatch — common follow-up
        boolean allAbove50k = employees.stream().allMatch(e -> e.getSalary() > 50_000);
        boolean anyHr = employees.stream().anyMatch(e -> "HR".equals(e.getDepartment()));
        System.out.println("\nBonus allMatch(>50k): " + allAbove50k + ", anyMatch(HR): " + anyHr);
    }

    public static void main(String[] args) throws Exception {
        new Q10EmployeeStreamScenarios().run();
    }
}

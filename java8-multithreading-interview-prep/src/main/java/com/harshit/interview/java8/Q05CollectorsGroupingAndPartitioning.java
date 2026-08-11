package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;
import com.harshit.interview.model.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * INTERVIEW Q: Explain Collectors — groupingBy, partitioningBy, joining, summarizing.
 *
 * <p><b>4 YOE:</b> Be ready to write SQL-like aggregations in memory using streams.
 */
public final class Q05CollectorsGroupingAndPartitioning implements InterviewDemo {

    private static List<Employee> sampleEmployees() {
        return List.of(
                new Employee("Raj", 1, "Bengaluru", 80_000, "IT"),
                new Employee("Priya", 2, "Bengaluru", 95_000, "HR"),
                new Employee("Amit", 3, "Pune", 70_000, "IT"),
                new Employee("Sneha", 4, "Pune", 110_000, "Accounts"),
                new Employee("Kiran", 5, "Bengaluru", 60_000, "IT")
        );
    }

    @Override
    public void run() {
        System.out.println("=== Q05: Collectors ===\n");

        List<Employee> employees = sampleEmployees();

        // groupingBy — Map key → List
        Map<String, List<Employee>> byCity = employees.stream()
                .collect(Collectors.groupingBy(Employee::getCity));
        System.out.println("Group by city: " + byCity.keySet());

        // groupingBy with downstream collector — count per department
        Map<String, Long> countByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
        System.out.println("Count by dept: " + countByDept);

        // partitioningBy — always Map<Boolean, List> (only two buckets)
        Map<Boolean, List<Employee>> salaryPartition = employees.stream()
                .collect(Collectors.partitioningBy(e -> e.getSalary() >= 80_000));
        System.out.println("Salary >= 80k count: " + salaryPartition.get(true).size());

        // joining
        String namesCsv = employees.stream()
                .map(Employee::getName)
                .sorted()
                .collect(Collectors.joining(", "));
        System.out.println("Names: " + namesCsv);

        // summarizingDouble — avg salary per city
        Map<String, Double> avgSalaryByCity = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getCity,
                        Collectors.averagingDouble(Employee::getSalary)));
        System.out.println("Avg salary by city: " + avgSalaryByCity);

        // toMap — watch duplicate keys!
        Map<Integer, String> idToName = employees.stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getName));
        System.out.println("Id map size: " + idToName.size());

        // sorting employees by salary desc — often asked with streams
        employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(2)
                .forEach(e -> System.out.println("Top earner sample: " + e.getName()));
    }

    public static void main(String[] args) throws Exception {
        new Q05CollectorsGroupingAndPartitioning().run();
    }
}

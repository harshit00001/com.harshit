package com.harshit.java8.streams.practice;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * STREAM API PRACTICE QUESTIONS - Interview Explanation:
 * 
 * This file contains common Stream API interview questions with solutions.
 * Practice these to master Stream API for interviews.
 */
public class StreamPracticeQuestions {

    public static void main(String[] args) {
        System.out.println("=== STREAM API PRACTICE QUESTIONS ===\n");
        
        question1_FindEvenNumbers();
        question2_FindMaxMin();
        question3_SumOfNumbers();
        question4_GroupByDepartment();
        question5_FindDuplicates();
        question6_SortEmployees();
        question7_FindFirst();
        question8_CountOccurrences();
        question9_FlatMapExample();
        question10_PartitionByAge();
    }
    
    /**
     * Question 1: Find all even numbers from a list
     * 
     * Interview Point: Basic filter operation
     */
    private static void question1_FindEvenNumbers() {
        System.out.println("--- Q1: Find Even Numbers ---");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        
        System.out.println("  Input: " + numbers);
        System.out.println("  Output: " + evens);
        System.out.println("  → Use filter() with predicate\n");
    }
    
    /**
     * Question 2: Find maximum and minimum from a list
     * 
     * Interview Point: Using max() and min() with Comparator
     */
    private static void question2_FindMaxMin() {
        System.out.println("--- Q2: Find Max and Min ---");
        
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3);
        
        Optional<Integer> max = numbers.stream()
            .max(Integer::compareTo);
        Optional<Integer> min = numbers.stream()
            .min(Integer::compareTo);
        
        System.out.println("  Input: " + numbers);
        System.out.println("  Max: " + max.orElse(-1));
        System.out.println("  Min: " + min.orElse(-1));
        System.out.println("  → Use max()/min() with Comparator\n");
    }
    
    /**
     * Question 3: Find sum of all numbers
     * 
     * Interview Point: Using reduce() or sum()
     */
    private static void question3_SumOfNumbers() {
        System.out.println("--- Q3: Sum of Numbers ---");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // Method 1: Using reduce
        int sum1 = numbers.stream()
            .reduce(0, Integer::sum);
        
        // Method 2: Using mapToInt and sum
        int sum2 = numbers.stream()
            .mapToInt(Integer::intValue)
            .sum();
        
        System.out.println("  Input: " + numbers);
        System.out.println("  Sum (reduce): " + sum1);
        System.out.println("  Sum (mapToInt): " + sum2);
        System.out.println("  → Use reduce() or mapToInt().sum()\n");
    }
    
    /**
     * Question 4: Group employees by department
     * 
     * Interview Point: Using groupingBy collector
     */
    private static void question4_GroupByDepartment() {
        System.out.println("--- Q4: Group by Department ---");
        
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Engineering", 50000),
            new Employee("Bob", "Marketing", 45000),
            new Employee("Charlie", "Engineering", 60000),
            new Employee("David", "Sales", 40000)
        );
        
        Map<String, List<Employee>> byDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));
        
        System.out.println("  Employees: " + employees);
        System.out.println("  Grouped by Department:");
        byDept.forEach((dept, emps) -> 
            System.out.println("    " + dept + ": " + emps));
        System.out.println("  → Use Collectors.groupingBy()\n");
    }
    
    /**
     * Question 5: Find duplicate elements
     * 
     * Interview Point: Using frequency or groupingBy
     */
    private static void question5_FindDuplicates() {
        System.out.println("--- Q5: Find Duplicates ---");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 2, 4, 5, 3, 6);
        
        // Method 1: Using frequency
        Set<Integer> duplicates = numbers.stream()
            .filter(n -> Collections.frequency(numbers, n) > 1)
            .collect(Collectors.toSet());
        
        // Method 2: Using groupingBy
        Map<Integer, Long> counts = numbers.stream()
            .collect(Collectors.groupingBy(n -> n, Collectors.counting()));
        
        Set<Integer> duplicates2 = counts.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        
        System.out.println("  Input: " + numbers);
        System.out.println("  Duplicates (method 1): " + duplicates);
        System.out.println("  Duplicates (method 2): " + duplicates2);
        System.out.println("  → Use frequency() or groupingBy()\n");
    }
    
    /**
     * Question 6: Sort employees by salary
     * 
     * Interview Point: Using sorted() with Comparator
     */
    private static void question6_SortEmployees() {
        System.out.println("--- Q6: Sort Employees by Salary ---");
        
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Engineering", 50000),
            new Employee("Bob", "Marketing", 45000),
            new Employee("Charlie", "Engineering", 60000)
        );
        
        List<Employee> sorted = employees.stream()
            .sorted(Comparator.comparing(Employee::getSalary))
            .collect(Collectors.toList());
        
        System.out.println("  Input: " + employees);
        System.out.println("  Sorted by Salary: " + sorted);
        System.out.println("  → Use sorted() with Comparator\n");
    }
    
    /**
     * Question 7: Find first employee with salary > 50000
     * 
     * Interview Point: Using findFirst() with filter
     */
    private static void question7_FindFirst() {
        System.out.println("--- Q7: Find First Employee with Salary > 50000 ---");
        
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Engineering", 50000),
            new Employee("Bob", "Marketing", 55000),
            new Employee("Charlie", "Engineering", 60000)
        );
        
        Optional<Employee> first = employees.stream()
            .filter(e -> e.getSalary() > 50000)
            .findFirst();
        
        System.out.println("  Employees: " + employees);
        System.out.println("  First with salary > 50000: " + 
            first.map(Employee::getName).orElse("None"));
        System.out.println("  → Use filter() + findFirst()\n");
    }
    
    /**
     * Question 8: Count occurrences of each element
     * 
     * Interview Point: Using groupingBy with counting
     */
    private static void question8_CountOccurrences() {
        System.out.println("--- Q8: Count Occurrences ---");
        
        List<String> words = Arrays.asList("apple", "banana", "apple", "cherry", "banana");
        
        Map<String, Long> counts = words.stream()
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        
        System.out.println("  Input: " + words);
        System.out.println("  Counts: " + counts);
        System.out.println("  → Use groupingBy() with counting()\n");
    }
    
    /**
     * Question 9: Flatten list of lists
     * 
     * Interview Point: Using flatMap()
     */
    private static void question9_FlatMapExample() {
        System.out.println("--- Q9: Flatten List of Lists ---");
        
        List<List<Integer>> listOfLists = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5),
            Arrays.asList(6, 7, 8)
        );
        
        List<Integer> flattened = listOfLists.stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
        
        System.out.println("  Input: " + listOfLists);
        System.out.println("  Flattened: " + flattened);
        System.out.println("  → Use flatMap() to flatten nested collections\n");
    }
    
    /**
     * Question 10: Partition employees by age (>= 30)
     * 
     * Interview Point: Using partitioningBy
     */
    private static void question10_PartitionByAge() {
        System.out.println("--- Q10: Partition by Age >= 30 ---");
        
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Engineering", 50000, 25),
            new Employee("Bob", "Marketing", 45000, 35),
            new Employee("Charlie", "Engineering", 60000, 28),
            new Employee("David", "Sales", 40000, 32)
        );
        
        Map<Boolean, List<Employee>> partitioned = employees.stream()
            .collect(Collectors.partitioningBy(e -> e.getAge() >= 30));
        
        System.out.println("  Employees: " + employees);
        System.out.println("  Partitioned (age >= 30):");
        partitioned.forEach((key, emps) -> 
            System.out.println("    " + key + ": " + emps));
        System.out.println("  → Use partitioningBy() for boolean partitioning\n");
    }
}

/**
 * Employee class for practice questions
 */
class Employee {
    private String name;
    private String department;
    private double salary;
    private int age;
    
    public Employee(String name, String department, double salary) {
        this(name, department, salary, 0);
    }
    
    public Employee(String name, String department, double salary, int age) {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.age = age;
    }
    
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    public int getAge() { return age; }
    
    @Override
    public String toString() {
        return name + "(" + department + ", $" + salary + 
            (age > 0 ? ", " + age + "y" : "") + ")";
    }
}

/**
 * INTERVIEW SUMMARY: Stream Practice Questions
 * 
 * Common Patterns:
 * 1. Filter: filter() with predicate
 * 2. Transform: map() to transform elements
 * 3. Aggregate: reduce(), sum(), max(), min()
 * 4. Group: groupingBy() to group elements
 * 5. Partition: partitioningBy() for boolean split
 * 6. Find: findFirst(), findAny()
 * 7. Flatten: flatMap() for nested collections
 * 8. Count: counting() collector
 * 9. Sort: sorted() with Comparator
 * 10. Collect: collect() with various collectors
 */


package com.harshit.java8.streams;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * STREAM API - Interview Explanation:
 * 
 * Stream: Sequence of elements supporting functional-style operations
 * 
 * Key Characteristics:
 * 1. Not a data structure (doesn't store data)
 * 2. Functional in nature (doesn't modify source)
 * 3. Lazy evaluation (operations executed only when needed)
 * 4. Can be consumed only once
 * 
 * Simple Explanation:
 * - Like a pipeline for processing data
 * - You can filter, transform, and collect data
 * - Operations are chained together
 * - Original data is not modified
 * 
 * Operations:
 * - Intermediate: filter, map, sorted, distinct, etc. (return Stream)
 * - Terminal: collect, forEach, reduce, count, etc. (return result)
 */
public class StreamAPI {

    public static void main(String[] args) {
        System.out.println("=== STREAM API ===\n");
        
        demonstrateStreamCreation();
        demonstrateIntermediateOperations();
        demonstrateTerminalOperations();
        demonstrateCollectors();
        demonstrateParallelStreams();
    }
    
    /**
     * Interview Point: Creating Streams
     */
    private static void demonstrateStreamCreation() {
        System.out.println("--- Creating Streams ---");
        
        // Interview Point: From Collection
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Stream<String> stream1 = list.stream();
        
        // Interview Point: From Array
        String[] array = {"dog", "cat", "bird"};
        Stream<String> stream2 = Arrays.stream(array);
        
        // Interview Point: Using Stream.of()
        Stream<String> stream3 = Stream.of("one", "two", "three");
        
        // Interview Point: Using Stream.builder()
        Stream<String> stream4 = Stream.<String>builder()
            .add("first")
            .add("second")
            .build();
        
        // Interview Point: Infinite stream
        Stream<Integer> infinite = Stream.iterate(0, n -> n + 2);
        System.out.println("  First 5 even numbers:");
        infinite.limit(5).forEach(n -> System.out.println("    " + n));
        
        System.out.println();
    }
    
    /**
     * Interview Point: Intermediate Operations
     * Return new Stream, can be chained
     */
    private static void demonstrateIntermediateOperations() {
        System.out.println("--- Intermediate Operations ---");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Interview Point: filter - keeps elements matching predicate
        System.out.println("  Filter (even numbers):");
        numbers.stream()
            .filter(n -> n % 2 == 0)
            .forEach(n -> System.out.println("    " + n));
        
        // Interview Point: map - transforms each element
        System.out.println("  Map (square each number):");
        numbers.stream()
            .map(n -> n * n)
            .forEach(n -> System.out.println("    " + n));
        
        // Interview Point: distinct - removes duplicates
        List<Integer> withDuplicates = Arrays.asList(1, 2, 2, 3, 3, 3, 4);
        System.out.println("  Distinct:");
        withDuplicates.stream()
            .distinct()
            .forEach(n -> System.out.println("    " + n));
        
        // Interview Point: sorted - sorts elements
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
        System.out.println("  Sorted:");
        names.stream()
            .sorted()
            .forEach(n -> System.out.println("    " + n));
        
        // Interview Point: limit - limits number of elements
        System.out.println("  Limit (first 3):");
        numbers.stream()
            .limit(3)
            .forEach(n -> System.out.println("    " + n));
        
        // Interview Point: skip - skips first n elements
        System.out.println("  Skip (skip first 3):");
        numbers.stream()
            .skip(3)
            .forEach(n -> System.out.println("    " + n));
        
        System.out.println();
    }
    
    /**
     * Interview Point: Terminal Operations
     * Consume stream and return result
     */
    private static void demonstrateTerminalOperations() {
        System.out.println("--- Terminal Operations ---");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // Interview Point: forEach - performs action on each element
        System.out.println("  forEach:");
        numbers.stream().forEach(n -> System.out.println("    " + n));
        
        // Interview Point: collect - collects into collection
        List<Integer> doubled = numbers.stream()
            .map(n -> n * 2)
            .collect(Collectors.toList());
        System.out.println("  collect: " + doubled);
        
        // Interview Point: count - counts elements
        long count = numbers.stream().count();
        System.out.println("  count: " + count);
        
        // Interview Point: reduce - reduces to single value
        int sum = numbers.stream()
            .reduce(0, (a, b) -> a + b);
        System.out.println("  reduce (sum): " + sum);
        
        // Interview Point: anyMatch - returns true if any matches
        boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        System.out.println("  anyMatch (has even): " + hasEven);
        
        // Interview Point: allMatch - returns true if all match
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        System.out.println("  allMatch (all positive): " + allPositive);
        
        // Interview Point: findFirst - returns first element
        Optional<Integer> first = numbers.stream().findFirst();
        System.out.println("  findFirst: " + first.orElse(-1));
        
        System.out.println();
    }
    
    /**
     * Interview Point: Collectors
     * Utility methods for collecting stream results
     */
    private static void demonstrateCollectors() {
        System.out.println("--- Collectors ---");
        
        List<Person> people = Arrays.asList(
            new Person("Alice", 25, "Engineering"),
            new Person("Bob", 30, "Marketing"),
            new Person("Charlie", 25, "Engineering"),
            new Person("David", 35, "Sales")
        );
        
        // Interview Point: toList
        List<String> names = people.stream()
            .map(Person::getName)
            .collect(Collectors.toList());
        System.out.println("  toList: " + names);
        
        // Interview Point: toSet
        Set<String> departments = people.stream()
            .map(Person::getDepartment)
            .collect(Collectors.toSet());
        System.out.println("  toSet (departments): " + departments);
        
        // Interview Point: groupingBy - group by key
        Map<String, List<Person>> byDept = people.stream()
            .collect(Collectors.groupingBy(Person::getDepartment));
        System.out.println("  groupingBy (department):");
        byDept.forEach((dept, persons) -> 
            System.out.println("    " + dept + ": " + persons));
        
        // Interview Point: partitioningBy - partition by predicate
        Map<Boolean, List<Person>> byAge = people.stream()
            .collect(Collectors.partitioningBy(p -> p.getAge() >= 30));
        System.out.println("  partitioningBy (age >= 30):");
        byAge.forEach((key, persons) -> 
            System.out.println("    " + key + ": " + persons));
        
        // Interview Point: averagingInt - calculate average
        double avgAge = people.stream()
            .collect(Collectors.averagingInt(Person::getAge));
        System.out.println("  averagingInt (average age): " + avgAge);
        
        // Interview Point: joining - join strings
        String joined = people.stream()
            .map(Person::getName)
            .collect(Collectors.joining(", "));
        System.out.println("  joining: " + joined);
        
        System.out.println();
    }
    
    /**
     * Interview Point: Parallel Streams
     * Process elements in parallel
     */
    private static void demonstrateParallelStreams() {
        System.out.println("--- Parallel Streams ---");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Interview Point: parallelStream() - creates parallel stream
        System.out.println("  Parallel processing:");
        numbers.parallelStream()
            .forEach(n -> System.out.println("    " + n + " - Thread: " + 
                Thread.currentThread().getName()));
        
        // Interview Point: Use parallel for CPU-intensive operations
        long sum = numbers.parallelStream()
            .mapToInt(n -> n * n)
            .sum();
        System.out.println("  Sum of squares (parallel): " + sum);
        
        System.out.println();
    }
}

class Person {
    private String name;
    private int age;
    private String department;
    
    public Person(String name, int age, String department) {
        this.name = name;
        this.age = age;
        this.department = department;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDepartment() { return department; }
    
    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}

/**
 * INTERVIEW SUMMARY: Stream API
 * 
 * Key Points:
 * 1. Stream doesn't store data
 * 2. Operations are lazy (executed on terminal operation)
 * 3. Stream can be consumed only once
 * 4. Original collection is not modified
 * 
 * Common Operations:
 * - Intermediate: filter, map, sorted, distinct, limit, skip
 * - Terminal: collect, forEach, reduce, count, anyMatch, findFirst
 * 
 * Collectors:
 * - toList, toSet, groupingBy, partitioningBy, joining
 */


package com.harshit.collections.basics;

import java.util.*;

/**
 * COMPARATOR vs COMPARABLE
 * 
 * This is a fundamental concept in Java Collections that appears in almost every interview.
 * Understanding the difference between Comparator and Comparable is crucial for working with
 * sorted collections like TreeSet, TreeMap, and for sorting lists.
 * 
 * Comparable is an interface that a class implements to define its natural ordering. When a
 * class implements Comparable, it means that instances of that class can be compared to each
 * other and sorted according to the natural order defined by the compareTo() method. This is
 * useful when there's a single, obvious way to order objects of that class.
 * 
 * Comparator is an interface that defines a way to compare two objects. Unlike Comparable,
 * Comparator is typically implemented in a separate class or as a lambda expression, allowing
 * you to define multiple ways to compare the same type of objects. This is useful when you
 * need different sorting orders for the same class, or when you can't modify the class to
 * implement Comparable.
 */
public class ComparatorVsComparable {
    
    public static void main(String[] args) {
        demonstrateComparable();
        demonstrateComparator();
        demonstrateBothTogether();
        compareComparableVsComparator();
    }
    
    /**
     * COMPARABLE DEMONSTRATION
     * 
     * This method demonstrates how to use the Comparable interface. When a class implements
     * Comparable, it defines a natural ordering for its objects. The compareTo() method returns
     * a negative integer if this object is less than the other, zero if equal, or a positive
     * integer if greater.
     */
    public static void demonstrateComparable() {
        System.out.println("=== COMPARABLE DEMONSTRATION ===");
        
        // Person class implements Comparable (natural order by age)
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 30));
        people.add(new Person("Charlie", 20));
        people.add(new Person("Diana", 28));
        
        System.out.println("Before sorting: " + people);
        
        // Sort using natural ordering (Comparable)
        Collections.sort(people);
        System.out.println("After sorting (by age): " + people);
        
        // TreeSet uses Comparable for ordering
        TreeSet<Person> personSet = new TreeSet<>(people);
        System.out.println("TreeSet (sorted by age): " + personSet);
        
        System.out.println();
    }
    
    /**
     * COMPARATOR DEMONSTRATION
     * 
     * This method demonstrates how to use the Comparator interface. Comparator allows you to
     * define custom ordering without modifying the class. You can create multiple Comparators
     * for the same class to sort in different ways.
     */
    public static void demonstrateComparator() {
        System.out.println("=== COMPARATOR DEMONSTRATION ===");
        
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 30));
        people.add(new Person("Charlie", 20));
        people.add(new Person("Diana", 28));
        
        // Sort by name using Comparator
        Collections.sort(people, new NameComparator());
        System.out.println("Sorted by name: " + people);
        
        // Sort by age descending using Comparator
        Collections.sort(people, new AgeDescendingComparator());
        System.out.println("Sorted by age (descending): " + people);
        
        // Using lambda expression (Java 8+)
        Collections.sort(people, (p1, p2) -> p1.name.length() - p2.name.length());
        System.out.println("Sorted by name length: " + people);
        
        // Using method reference
        Collections.sort(people, Comparator.comparing(Person::getName));
        System.out.println("Sorted by name (method reference): " + people);
        
        // TreeSet with Comparator
        TreeSet<Person> personSet = new TreeSet<>(new NameComparator());
        personSet.addAll(people);
        System.out.println("TreeSet (sorted by name): " + personSet);
        
        System.out.println();
    }
    
    /**
     * DEMONSTRATE BOTH TOGETHER
     * 
     * This method shows how Comparable and Comparator can work together. When you provide a
     * Comparator to a sorted collection or sort method, it overrides the natural ordering
     * defined by Comparable.
     */
    public static void demonstrateBothTogether() {
        System.out.println("=== COMPARABLE AND COMPARATOR TOGETHER ===");
        
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 30));
        people.add(new Person("Charlie", 20));
        
        // Default: Uses Comparable (natural order by age)
        Collections.sort(people);
        System.out.println("Using Comparable (by age): " + people);
        
        // Override: Use Comparator (by name)
        Collections.sort(people, new NameComparator());
        System.out.println("Using Comparator (by name): " + people);
        
        // TreeSet with natural ordering (Comparable)
        TreeSet<Person> set1 = new TreeSet<>();
        set1.addAll(people);
        System.out.println("TreeSet with Comparable: " + set1);
        
        // TreeSet with Comparator (overrides Comparable)
        TreeSet<Person> set2 = new TreeSet<>(new NameComparator());
        set2.addAll(people);
        System.out.println("TreeSet with Comparator: " + set2);
        
        System.out.println();
    }
    
    /**
     * COMPARISON: Comparable vs Comparator
     */
    public static void compareComparableVsComparator() {
        System.out.println("=== COMPARABLE vs COMPARATOR ===");
        
        System.out.println("\nAspect              | Comparable           | Comparator");
        System.out.println("--------------------|----------------------|----------------------");
        System.out.println("Package             | java.lang            | java.util");
        System.out.println("Method              | compareTo()          | compare()");
        System.out.println("Modifies Class      | Yes (implements)     | No (separate class)");
        System.out.println("Sorting Logic       | Single (natural)     | Multiple (custom)");
        System.out.println("When to Use         | Natural ordering     | Custom ordering");
        System.out.println("Example             | String, Integer      | Custom comparators");
        
        System.out.println("\nKey Points:");
        System.out.println("1. Use Comparable when there's a single, natural way to order objects");
        System.out.println("2. Use Comparator when you need multiple ways to order, or can't modify the class");
        System.out.println("3. Comparator overrides Comparable when both are provided");
        System.out.println("4. Comparator is more flexible and follows the Open-Closed Principle");
        System.out.println();
    }
    
    /**
     * Person class implementing Comparable
     */
    static class Person implements Comparable<Person> {
        String name;
        int age;
        
        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        String getName() {
            return name;
        }
        
        int getAge() {
            return age;
        }
        
        // Natural ordering: by age
        @Override
        public int compareTo(Person other) {
            return this.age - other.age;
        }
        
        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }
    
    /**
     * Comparator for sorting by name
     */
    static class NameComparator implements Comparator<Person> {
        @Override
        public int compare(Person p1, Person p2) {
            return p1.name.compareTo(p2.name);
        }
    }
    
    /**
     * Comparator for sorting by age descending
     */
    static class AgeDescendingComparator implements Comparator<Person> {
        @Override
        public int compare(Person p1, Person p2) {
            return p2.age - p1.age; // Reverse order
        }
    }
}


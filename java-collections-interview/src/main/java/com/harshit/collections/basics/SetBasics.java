package com.harshit.collections.basics;

import java.util.*;

/**
 * SET BASICS - Understanding Java Set Interface
 * 
 * Set is a collection that does NOT allow duplicate elements.
 * 
 * Key Characteristics:
 * - No Duplicates: Each element is unique
 * - At most one null: Can contain at most one null element
 * - No Index: Elements are not accessed by index
 * 
 * Main Implementations:
 * 1. HashSet: Hash table, O(1) average, no order
 * 2. LinkedHashSet: Hash table + linked list, maintains insertion order
 * 3. TreeSet: Red-black tree, sorted order, O(log n) operations
 */
public class SetBasics {
    
    public static void main(String[] args) {
        demonstrateHashSet();
        demonstrateLinkedHashSet();
        demonstrateTreeSet();
        compareSetImplementations();
    }
    
    /**
     * HASHSET DEMONSTRATION
     * 
     * HashSet is backed by a HashMap.
     * Uses hash code to determine bucket location.
     * 
     * Best For:
     * - Fast lookup, add, remove operations
     * - When order doesn't matter
     * - Checking if element exists
     * 
     * Time Complexity:
     * - add(): O(1) average, O(n) worst case
     * - contains(): O(1) average, O(n) worst case
     * - remove(): O(1) average, O(n) worst case
     */
    public static void demonstrateHashSet() {
        System.out.println("=== HASHSET DEMONSTRATION ===");
        
        // Create HashSet
        Set<String> hashSet = new HashSet<>();
        
        // Add elements
        hashSet.add("Apple");
        hashSet.add("Banana");
        hashSet.add("Cherry");
        hashSet.add("Apple");  // Duplicate - will be ignored
        hashSet.add(null);     // Null allowed (only one)
        hashSet.add(null);     // Second null - ignored
        
        System.out.println("HashSet: " + hashSet);
        System.out.println("Size: " + hashSet.size());  // 4 (Apple, Banana, Cherry, null)
        
        // Check if contains
        System.out.println("Contains 'Apple': " + hashSet.contains("Apple"));
        System.out.println("Contains 'Mango': " + hashSet.contains("Mango"));
        
        // Remove element
        hashSet.remove("Banana");
        System.out.println("After removing 'Banana': " + hashSet);
        
        // Iterate (order is not guaranteed)
        System.out.println("\nIterating HashSet (order may vary):");
        for (String fruit : hashSet) {
            System.out.println("  - " + fruit);
        }
        
        System.out.println();
    }
    
    /**
     * LINKEDHASHSET DEMONSTRATION
     * 
     * LinkedHashSet extends HashSet and maintains insertion order.
     * Uses hash table + doubly linked list.
     * 
     * Best For:
     * - When you need HashSet performance + insertion order
     * - LRU cache implementation
     * - Maintaining order while ensuring uniqueness
     * 
     * Time Complexity: Same as HashSet (O(1) average)
     */
    public static void demonstrateLinkedHashSet() {
        System.out.println("=== LINKEDHASHSET DEMONSTRATION ===");
        
        // Create LinkedHashSet
        Set<String> linkedHashSet = new LinkedHashSet<>();
        
        // Add elements
        linkedHashSet.add("Third");
        linkedHashSet.add("First");
        linkedHashSet.add("Second");
        linkedHashSet.add("First");  // Duplicate - ignored
        
        System.out.println("LinkedHashSet: " + linkedHashSet);
        System.out.println("Order maintained: Third, First, Second");
        
        // Iterate (order is maintained)
        System.out.println("\nIterating LinkedHashSet (insertion order):");
        for (String item : linkedHashSet) {
            System.out.println("  - " + item);
        }
        
        System.out.println();
    }
    
    /**
     * TREESET DEMONSTRATION
     * 
     * TreeSet is backed by a TreeMap (Red-Black Tree).
     * Elements are stored in sorted order.
     * 
     * Best For:
     * - When you need sorted elements
     * - Range queries (headSet, tailSet, subSet)
     * - Finding min/max elements
     * 
     * Time Complexity:
     * - add(): O(log n)
     * - contains(): O(log n)
     * - remove(): O(log n)
     * 
     * Requirements:
     * - Elements must implement Comparable, OR
     * - Provide Comparator in constructor
     */
    public static void demonstrateTreeSet() {
        System.out.println("=== TREESET DEMONSTRATION ===");
        
        // Create TreeSet (natural ordering - ascending)
        Set<Integer> treeSet = new TreeSet<>();
        
        // Add elements in random order
        treeSet.add(5);
        treeSet.add(2);
        treeSet.add(8);
        treeSet.add(1);
        treeSet.add(9);
        treeSet.add(2);  // Duplicate - ignored
        
        System.out.println("TreeSet (sorted): " + treeSet);
        // Output: [1, 2, 5, 8, 9] - automatically sorted
        
        // First and last elements
        System.out.println("First element: " + ((TreeSet<Integer>) treeSet).first());
        System.out.println("Last element: " + ((TreeSet<Integer>) treeSet).last());
        
        // Range operations
        TreeSet<Integer> treeSet2 = (TreeSet<Integer>) treeSet;
        System.out.println("Elements less than 5: " + treeSet2.headSet(5));
        System.out.println("Elements greater than or equal to 5: " + treeSet2.tailSet(5));
        System.out.println("Elements between 2 and 8: " + treeSet2.subSet(2, 8));
        
        // Custom comparator (descending order)
        TreeSet<Integer> descendingSet = new TreeSet<>(Collections.reverseOrder());
        descendingSet.addAll(treeSet);
        System.out.println("Descending order: " + descendingSet);
        
        // TreeSet with custom objects
        TreeSet<Person> personSet = new TreeSet<>((p1, p2) -> p1.age - p2.age);
        personSet.add(new Person("Alice", 25));
        personSet.add(new Person("Bob", 30));
        personSet.add(new Person("Charlie", 20));
        
        System.out.println("\nPersons sorted by age:");
        for (Person person : personSet) {
            System.out.println("  - " + person);
        }
        
        System.out.println();
    }
    
    /**
     * COMPARISON: HashSet vs LinkedHashSet vs TreeSet
     */
    public static void compareSetImplementations() {
        System.out.println("=== SET IMPLEMENTATIONS COMPARISON ===");
        
        System.out.println("\nFeature Comparison:");
        System.out.println("Feature          | HashSet | LinkedHashSet | TreeSet");
        System.out.println("-----------------|---------|---------------|---------");
        System.out.println("Order           | No      | Insertion     | Sorted");
        System.out.println("Null allowed    | Yes(1)  | Yes(1)        | No*");
        System.out.println("Performance     | O(1)    | O(1)          | O(log n)");
        System.out.println("Use Case        | General | Order needed  | Sorted needed");
        
        System.out.println("\n*TreeSet doesn't allow null if using natural ordering");
        System.out.println();
    }
    
    /**
     * Helper class for TreeSet demonstration
     */
    static class Person {
        String name;
        int age;
        
        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        @Override
        public String toString() {
            return name + " (" + age + ")";
        }
    }
}


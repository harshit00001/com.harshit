package com.harshit.collections.basics;

import java.util.*;

/**
 * COLLECTIONS UTILITY CLASS
 * 
 * The Collections class is a utility class that provides static methods for operating on collections.
 * These methods provide common operations like sorting, searching, reversing, shuffling, and finding
 * maximum or minimum elements. Understanding these utility methods is important because they can
 * save you from writing boilerplate code and provide optimized implementations.
 * 
 * The Collections class works with the Collection interface and its implementations, providing
 * algorithms that are commonly needed when working with collections. These methods are well-tested,
 * optimized, and handle edge cases properly, making them preferable to writing your own implementations
 * in most cases.
 */
public class CollectionsUtility {
    
    public static void main(String[] args) {
        demonstrateSorting();
        demonstrateSearching();
        demonstrateReversingAndShuffling();
        demonstrateMinMax();
        demonstrateSynchronization();
        demonstrateOtherUtilities();
    }
    
    /**
     * SORTING DEMONSTRATION
     * 
     * The Collections.sort() method sorts a list according to the natural ordering of its elements,
     * or according to a provided Comparator. The sort is stable, meaning that equal elements maintain
     * their relative order. The algorithm used is a modified merge sort that offers guaranteed
     * n log(n) performance.
     */
    public static void demonstrateSorting() {
        System.out.println("=== SORTING ===");
        
        // Sort list of integers (natural order)
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9, 3));
        System.out.println("Original: " + numbers);
        
        Collections.sort(numbers);
        System.out.println("Sorted (ascending): " + numbers);
        
        // Sort in descending order using Comparator
        Collections.sort(numbers, Collections.reverseOrder());
        System.out.println("Sorted (descending): " + numbers);
        
        // Sort with custom Comparator
        List<String> words = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "date"));
        Collections.sort(words, (a, b) -> b.length() - a.length()); // Sort by length (longest first)
        System.out.println("Words sorted by length: " + words);
        
        System.out.println();
    }
    
    /**
     * SEARCHING DEMONSTRATION
     * 
     * The Collections.binarySearch() method searches for a specified element in a sorted list using
     * the binary search algorithm. The list must be sorted in ascending order according to the natural
     * ordering or a Comparator before calling binarySearch. If the list is not sorted, the results
     * are undefined.
     * 
     * Binary search returns the index of the search key if it's found, or a negative value if it's
     * not found. The negative value indicates where the key would be inserted to maintain sorted order.
     */
    public static void demonstrateSearching() {
        System.out.println("=== SEARCHING ===");
        
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9, 11, 13));
        System.out.println("Sorted list: " + numbers);
        
        // Binary search (list must be sorted)
        int index = Collections.binarySearch(numbers, 7);
        System.out.println("Index of 7: " + index); // Returns 3
        
        index = Collections.binarySearch(numbers, 6);
        System.out.println("Index of 6: " + index); // Returns -4 (would be inserted at index 4)
        
        // Binary search with Comparator
        List<String> words = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "date"));
        Collections.sort(words);
        index = Collections.binarySearch(words, "cherry");
        System.out.println("Index of 'cherry': " + index);
        
        System.out.println();
    }
    
    /**
     * REVERSING AND SHUFFLING DEMONSTRATION
     * 
     * Collections.reverse() reverses the order of elements in a list. Collections.shuffle() randomly
     * permutes the list using a default source of randomness, or a specified Random object. These
     * operations are useful for various algorithms and testing scenarios.
     */
    public static void demonstrateReversingAndShuffling() {
        System.out.println("=== REVERSING AND SHUFFLING ===");
        
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("Original: " + numbers);
        
        // Reverse
        Collections.reverse(numbers);
        System.out.println("Reversed: " + numbers);
        
        // Shuffle
        Collections.shuffle(numbers);
        System.out.println("Shuffled: " + numbers);
        
        // Shuffle with seed (for reproducible results)
        Collections.shuffle(numbers, new Random(42));
        System.out.println("Shuffled (seed 42): " + numbers);
        
        System.out.println();
    }
    
    /**
     * MIN AND MAX DEMONSTRATION
     * 
     * Collections.min() and Collections.max() return the minimum and maximum elements of a collection
     * according to the natural ordering or a provided Comparator. These methods are useful when you
     * need to find extreme values without sorting the entire collection.
     */
    public static void demonstrateMinMax() {
        System.out.println("=== MIN AND MAX ===");
        
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9, 3));
        System.out.println("List: " + numbers);
        
        Integer min = Collections.min(numbers);
        Integer max = Collections.max(numbers);
        System.out.println("Minimum: " + min);
        System.out.println("Maximum: " + max);
        
        // Min/Max with Comparator
        List<String> words = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "date"));
        String shortest = Collections.min(words, Comparator.comparing(String::length));
        String longest = Collections.max(words, Comparator.comparing(String::length));
        System.out.println("Shortest word: " + shortest);
        System.out.println("Longest word: " + longest);
        
        System.out.println();
    }
    
    /**
     * SYNCHRONIZATION DEMONSTRATION
     * 
     * Collections.synchronizedList(), synchronizedSet(), and synchronizedMap() return thread-safe
     * wrappers around collections. These wrappers synchronize all methods, ensuring that only one
     * thread can access the collection at a time. However, iteration still requires external
     * synchronization.
     */
    public static void demonstrateSynchronization() {
        System.out.println("=== SYNCHRONIZATION ===");
        
        // Create synchronized collections
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
        Map<String, String> syncMap = Collections.synchronizedMap(new HashMap<>());
        
        // These collections are now thread-safe
        syncList.add("Item1");
        syncSet.add("Item1");
        syncMap.put("Key1", "Value1");
        
        System.out.println("Synchronized List: " + syncList);
        System.out.println("Synchronized Set: " + syncSet);
        System.out.println("Synchronized Map: " + syncMap);
        
        System.out.println("\nNote: Iteration still requires external synchronization:");
        System.out.println("  synchronized(syncList) {");
        System.out.println("    for (String item : syncList) { ... }");
        System.out.println("  }");
        
        System.out.println();
    }
    
    /**
     * OTHER UTILITY METHODS DEMONSTRATION
     * 
     * The Collections class provides many other useful methods like frequency(), disjoint(),
     * fill(), copy(), and more. These methods handle common operations that you might otherwise
     * need to implement yourself.
     */
    public static void demonstrateOtherUtilities() {
        System.out.println("=== OTHER UTILITY METHODS ===");
        
        // Frequency - count occurrences
        List<String> words = new ArrayList<>(Arrays.asList("apple", "banana", "apple", "cherry", "apple"));
        int frequency = Collections.frequency(words, "apple");
        System.out.println("Frequency of 'apple': " + frequency);
        
        // Disjoint - check if two collections have no elements in common
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        List<Integer> list3 = Arrays.asList(3, 4, 5);
        System.out.println("list1 and list2 are disjoint: " + Collections.disjoint(list1, list2));
        System.out.println("list1 and list3 are disjoint: " + Collections.disjoint(list1, list3));
        
        // Fill - replace all elements with specified value
        List<String> fillList = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        Collections.fill(fillList, "X");
        System.out.println("After fill with 'X': " + fillList);
        
        // Copy - copy elements from source to destination
        List<String> source = Arrays.asList("1", "2", "3", "4");
        List<String> dest = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        Collections.copy(dest, source);
        System.out.println("After copy: " + dest);
        
        // Swap - swap elements at specified positions
        List<Integer> swapList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Collections.swap(swapList, 0, 4);
        System.out.println("After swapping index 0 and 4: " + swapList);
        
        // Rotate - rotate elements by specified distance
        List<Integer> rotateList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Collections.rotate(rotateList, 2);
        System.out.println("After rotating by 2: " + rotateList);
        
        System.out.println();
    }
}


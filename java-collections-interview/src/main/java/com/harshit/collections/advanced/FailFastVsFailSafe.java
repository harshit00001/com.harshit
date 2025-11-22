package com.harshit.collections.advanced;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * FAIL-FAST vs FAIL-SAFE ITERATORS
 * 
 * This is one of the most critical concepts to understand for Java Collections interviews. The difference between
 * fail-fast and fail-safe iterators lies in how they handle concurrent modifications to the collection during iteration.
 * Understanding this concept is essential because it affects how you write code that iterates over collections, especially
 * in multi-threaded environments.
 * 
 * Fail-fast iterators immediately throw a ConcurrentModificationException if they detect that the collection has been
 * modified during iteration, except through the iterator's own remove method. The term "fail-fast" reflects the
 * philosophy of failing immediately and visibly rather than risking arbitrary, non-deterministic behavior. These iterators
 * work directly on the original collection, which means they can see changes immediately, but they also detect when those
 * changes happen during iteration and throw an exception to prevent inconsistent behavior.
 * 
 * Examples of collections that provide fail-fast iterators include ArrayList, HashMap, and HashSet. When you iterate over
 * these collections using an iterator or enhanced for loop, and you modify the collection during iteration, you'll get a
 * ConcurrentModificationException. This is actually a good thing because it prevents you from seeing inconsistent or
 * corrupted data.
 * 
 * Fail-safe iterators, on the other hand, don't throw exceptions when the collection is modified during iteration.
 * Instead, they work on a snapshot or copy of the collection that was taken when the iterator was created. This means
 * modifications to the original collection don't affect the iterator, and the iterator continues to work on the old
 * snapshot. Examples of collections that provide fail-safe iterators include CopyOnWriteArrayList and ConcurrentHashMap.
 * 
 * A good analogy to understand this is thinking about walking down a street. With fail-fast iterators, it's like walking
 * down a street while someone keeps changing the path behind you. If the path changes while you're walking, you trip and
 * fall - an exception is thrown. With fail-safe iterators, it's like walking on a snapshot of the street. Even if the
 * real street changes, your path remains stable because you're walking on the snapshot, not the actual street - no
 * tripping, no exceptions.
 */
public class FailFastVsFailSafe {
    
    public static void main(String[] args) {
        demonstrateFailFast();
        demonstrateFailSafe();
        howFailFastWorks();
        howFailSafeWorks();
        comparisonTable();
    }
    
    /**
     * FAIL-FAST ITERATOR DEMONSTRATION
     * 
     * This method demonstrates how fail-fast iterators work and what happens when you try to modify a collection during
     * iteration. Fail-fast iterators detect structural modifications, which are any changes that affect the size of the
     * collection, such as adding an element, removing an element, or clearing the collection. When such a modification is
     * detected, the iterator immediately throws a ConcurrentModificationException.
     * 
     * It's important to understand the difference between structural and non-structural modifications. Structural
     * modifications change the size of the collection, while non-structural modifications don't. For example, adding a new
     * element to a list is a structural modification because it increases the size. Removing an element is also a structural
     * modification because it decreases the size. However, updating the value associated with an existing key in a Map is not
     * a structural modification because it doesn't change the size of the map - it just changes the value.
     * 
     * This method shows three examples of fail-fast behavior: one with ArrayList, one with HashMap, and one with HashSet.
     * In each case, we try to modify the collection during iteration, and in each case, we catch the
     * ConcurrentModificationException that is thrown. This demonstrates that fail-fast iterators don't allow modifications
     * during iteration, which helps prevent bugs and ensures data consistency.
     */
    public static void demonstrateFailFast() {
        System.out.println("=== FAIL-FAST ITERATOR ===");
        
        // Example 1: ArrayList (Fail-Fast)
        System.out.println("\n1. ArrayList - Adding during iteration:");
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        
        try {
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                String item = it.next();
                System.out.println("  Processing: " + item);
                list.add("D");  // Structural modification - EXCEPTION!
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ❌ ConcurrentModificationException: " + e.getMessage());
        }
        
        // Example 2: HashMap (Fail-Fast)
        System.out.println("\n2. HashMap - Removing during iteration:");
        Map<String, String> map = new HashMap<>();
        map.put("A", "Apple");
        map.put("B", "Banana");
        map.put("C", "Cherry");
        
        try {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                System.out.println("  Processing: " + entry.getKey());
                map.remove("B");  // Structural modification - EXCEPTION!
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ❌ ConcurrentModificationException: " + e.getMessage());
        }
        
        // Example 3: HashSet (Fail-Fast)
        System.out.println("\n3. HashSet - Modifying during iteration:");
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        set.add("C");
        
        try {
            for (String item : set) {  // Enhanced for loop uses iterator
                System.out.println("  Processing: " + item);
                set.remove("B");  // Structural modification - EXCEPTION!
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ❌ ConcurrentModificationException: " + e.getMessage());
        }
        
        System.out.println("\n✅ Solution: Use iterator's remove() method");
        System.out.println("   OR collect modifications and apply after iteration");
        System.out.println();
    }
    
    /**
     * FAIL-SAFE ITERATOR DEMONSTRATION
     * 
     * Fail-Safe iterators work on a copy/snapshot of the collection.
     * Modifications to original collection don't affect the iterator.
     * 
     * Important Notes:
     * - Iterator shows snapshot at time of creation
     * - Changes to original collection are not visible to iterator
     * - No exception is thrown
     * - More memory overhead (copying collection)
     */
    public static void demonstrateFailSafe() {
        System.out.println("=== FAIL-SAFE ITERATOR ===");
        
        // Example 1: CopyOnWriteArrayList (Fail-Safe)
        System.out.println("\n1. CopyOnWriteArrayList - Adding during iteration:");
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        
        System.out.println("  Original list: " + list);
        
        Iterator<String> it = list.iterator();
        System.out.println("  Iterating (snapshot created):");
        while (it.hasNext()) {
            String item = it.next();
            System.out.println("    Processing: " + item);
            list.add("D");  // No exception! But not visible to current iterator
        }
        
        System.out.println("  After iteration, list: " + list);
        System.out.println("  Note: 'D' was added but not seen by iterator (snapshot)");
        
        // Example 2: ConcurrentHashMap (Fail-Safe)
        System.out.println("\n2. ConcurrentHashMap - Modifying during iteration:");
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("A", "Apple");
        map.put("B", "Banana");
        map.put("C", "Cherry");
        
        System.out.println("  Original map: " + map);
        
        Iterator<Map.Entry<String, String>> it2 = map.entrySet().iterator();
        System.out.println("  Iterating:");
        while (it2.hasNext()) {
            Map.Entry<String, String> entry = it2.next();
            System.out.println("    Processing: " + entry.getKey());
            map.put("D", "Date");  // No exception!
            map.remove("B");       // No exception!
        }
        
        System.out.println("  After iteration, map: " + map);
        System.out.println("  Note: Changes may or may not be visible (weakly consistent)");
        
        System.out.println();
    }
    
    /**
     * HOW FAIL-FAST WORKS INTERNALLY
     * 
     * This method explains the internal mechanism that makes fail-fast iterators work. Fail-fast iterators use a mechanism
     * called "modCount", which stands for modification count. This is an internal counter that tracks how many times the
     * collection has been structurally modified.
     * 
     * Here's how it works step by step. First, every collection that supports fail-fast iterators maintains a modCount field
     * that gets incremented every time a structural modification is made to the collection. When you create an iterator, the
     * iterator stores the current value of modCount in a field called expectedModCount. This expectedModCount represents what
     * the iterator expects the modification count to be.
     * 
     * Every time you call the iterator's next method to get the next element, the iterator checks if the expectedModCount
     * still matches the collection's current modCount. If they match, it means no structural modifications have been made
     * since the iterator was created, so the iterator continues normally. However, if they don't match, it means the
     * collection was modified during iteration, and the iterator immediately throws a ConcurrentModificationException.
     * 
     * This mechanism is why structural modifications cause an exception immediately - the check happens on every call to next,
     * so as soon as you try to access the next element after a modification, the exception is thrown. This immediate failure
     * is actually beneficial because it makes bugs visible right away, rather than allowing inconsistent behavior that might
     * be harder to debug.
     */
    public static void howFailFastWorks() {
        System.out.println("=== HOW FAIL-FAST WORKS ===");
        
        System.out.println("\nInternal Mechanism:");
        System.out.println("1. Collection has 'modCount' field (modification counter)");
        System.out.println("2. Iterator stores 'expectedModCount' when created");
        System.out.println("3. On each next() call:");
        System.out.println("   - Check: expectedModCount == modCount?");
        System.out.println("   - If NO -> throw ConcurrentModificationException");
        System.out.println("   - If YES -> continue");
        
        System.out.println("\nCode Example:");
        System.out.println("```java");
        System.out.println("List<String> list = new ArrayList<>();");
        System.out.println("list.add(\"A\");  // modCount = 1");
        System.out.println("list.add(\"B\");  // modCount = 2");
        System.out.println("Iterator<String> it = list.iterator();  // expectedModCount = 2");
        System.out.println("it.next();  // Check: 2 == 2? YES -> OK");
        System.out.println("list.add(\"C\");  // modCount = 3");
        System.out.println("it.next();  // Check: 2 == 3? NO -> EXCEPTION!");
        System.out.println("```");
        
        System.out.println("\n✅ Solution: Use iterator.remove()");
        System.out.println("   Iterator's remove() updates both modCount and expectedModCount");
        System.out.println();
    }
    
    /**
     * HOW FAIL-SAFE WORKS INTERNALLY
     * 
     * This method explains how fail-safe iterators work internally. Unlike fail-fast iterators that work directly on the
     * original collection, fail-safe iterators work on a copy or snapshot of the collection. This fundamental difference
     * is what makes them safe from concurrent modifications.
     * 
     * When you create a fail-safe iterator, the collection creates a snapshot or copy of its current state. This snapshot
     * is taken at the moment the iterator is created, and it captures all the elements that were in the collection at that
     * time. The iterator then iterates over this snapshot, not the original collection.
     * 
     * Because the iterator works on a snapshot, any changes made to the original collection don't affect the iterator. You
     * can add elements, remove elements, or modify the collection in any way, and the iterator will continue to work on its
     * snapshot without throwing any exceptions. This is why they're called fail-safe - they don't fail even when the
     * collection is modified.
     * 
     * However, there are important trade-offs to consider. First, creating a snapshot requires copying the collection, which
     * uses additional memory. For large collections, this can be significant. Second, the iterator may not see the latest
     * changes to the collection because it's working on an old snapshot. If you add an element to the collection during
     * iteration, that element won't appear in the current iterator - you'd need to create a new iterator to see it. Third,
     * creating the snapshot takes time, which can make iteration slower, especially for the first iteration.
     * 
     * Despite these trade-offs, fail-safe iterators are essential for concurrent programming where multiple threads might
     * be modifying the collection simultaneously. They provide a safe way to iterate without worrying about exceptions or
     * data corruption.
     */
    public static void howFailSafeWorks() {
        System.out.println("=== HOW FAIL-SAFE WORKS ===");
        
        System.out.println("\nInternal Mechanism:");
        System.out.println("1. When iterator is created, collection is copied");
        System.out.println("2. Iterator works on the copy/snapshot");
        System.out.println("3. Original collection can be modified safely");
        System.out.println("4. Changes to original are not visible to current iterator");
        System.out.println("5. New iterator will see the changes");
        
        System.out.println("\nExample: CopyOnWriteArrayList");
        System.out.println("- On write operation (add, remove, set):");
        System.out.println("  1. Create a new array");
        System.out.println("  2. Copy all elements to new array");
        System.out.println("  3. Modify new array");
        System.out.println("  4. Replace old array with new array");
        System.out.println("- Iterators hold reference to old array");
        System.out.println("- No exception, but iterator sees old data");
        
        System.out.println("\n⚠️ Important:");
        System.out.println("- Fail-Safe doesn't mean you see latest data");
        System.out.println("- It means no exception is thrown");
        System.out.println("- Iterator shows snapshot at creation time");
        System.out.println();
    }
    
    /**
     * COMPARISON TABLE
     */
    public static void comparisonTable() {
        System.out.println("=== FAIL-FAST vs FAIL-SAFE COMPARISON ===");
        
        System.out.println("\n┌─────────────────────────┬──────────────────┬──────────────────┐");
        System.out.println("│ Feature                 │ Fail-Fast        │ Fail-Safe         │");
        System.out.println("├─────────────────────────┼──────────────────┼──────────────────┤");
        System.out.println("│ Behavior on Modification│ Throws Exception │ Continues Safely  │");
        System.out.println("│ Underlying Mechanism    │ Original         │ Cloned Copy       │");
        System.out.println("│ Examples                │ ArrayList        │ CopyOnWriteArrayList│");
        System.out.println("│                         │ HashMap          │ ConcurrentHashMap │");
        System.out.println("│                         │ HashSet          │                   │");
        System.out.println("│ Thread Safety           │ ❌ Not Safe       │ ✅ Thread Safe     │");
        System.out.println("│ Performance             │ ⚡ Faster        │ 🐌 Slower         │");
        System.out.println("│ Memory Overhead         │ Low              │ High (copying)    │");
        System.out.println("│ Use Case                │ Single-threaded  │ Multi-threaded    │");
        System.out.println("│                         │ Controlled env  │ Concurrent env    │");
        System.out.println("└─────────────────────────┴──────────────────┴──────────────────┘");
        
        System.out.println("\n📝 Key Takeaways:");
        System.out.println("1. Fail-Fast: Fast, but throws exception on modification");
        System.out.println("2. Fail-Safe: Safe, but slower and uses more memory");
        System.out.println("3. Use Fail-Fast in single-threaded environments");
        System.out.println("4. Use Fail-Safe in multi-threaded environments");
        System.out.println("5. Fail-Safe iterators show snapshot, not live data");
        System.out.println();
    }
}


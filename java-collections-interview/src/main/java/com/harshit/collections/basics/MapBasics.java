package com.harshit.collections.basics;

import java.util.*;

/**
 * MAP BASICS - Understanding Java Map Interface
 * 
 * Map stores key-value pairs. Each key maps to exactly one value.
 * 
 * Key Characteristics:
 * - Key-Value Pairs: Each entry has a key and a value
 * - No Duplicate Keys: Each key is unique
 * - One null key: HashMap/LinkedHashMap allow one null key
 * - Multiple null values: Can have multiple null values
 * 
 * Main Implementations:
 * 1. HashMap: Hash table, O(1) average, no order
 * 2. LinkedHashMap: Hash table + linked list, maintains insertion/access order
 * 3. TreeMap: Red-black tree, sorted by keys, O(log n) operations
 * 4. Hashtable: Legacy, synchronized, thread-safe (use ConcurrentHashMap instead)
 */
public class MapBasics {
    
    public static void main(String[] args) {
        demonstrateHashMap();
        demonstrateLinkedHashMap();
        demonstrateTreeMap();
        demonstrateHashMapAsKey();
        compareMapImplementations();
    }
    
    /**
     * HASHMAP DEMONSTRATION
     * 
     * HashMap is backed by an array of buckets (hash table).
     * Uses hash code of key to determine bucket location.
     * 
     * Best For:
     * - Fast key-value lookups
     * - When order doesn't matter
     * - General purpose key-value storage
     * 
     * Time Complexity:
     * - put(): O(1) average, O(n) worst case
     * - get(): O(1) average, O(n) worst case
     * - remove(): O(1) average, O(n) worst case
     * 
     * Important:
     * - Keys must properly implement hashCode() and equals()
     * - Default initial capacity: 16
     * - Load factor: 0.75 (when 75% full, capacity doubles)
     */
    public static void demonstrateHashMap() {
        System.out.println("=== HASHMAP DEMONSTRATION ===");
        
        // Create HashMap
        Map<String, Integer> map = new HashMap<>();
        
        // Put key-value pairs
        map.put("Apple", 10);
        map.put("Banana", 20);
        map.put("Cherry", 30);
        map.put("Apple", 15);  // Updates existing key
        map.put(null, 0);      // Null key allowed (only one)
        map.put("Mango", null); // Null value allowed
        
        System.out.println("HashMap: " + map);
        System.out.println("Size: " + map.size());
        
        // Get value by key
        System.out.println("Value for 'Apple': " + map.get("Apple"));
        System.out.println("Value for 'Grape': " + map.get("Grape"));  // null if not found
        
        // Check if contains key/value
        System.out.println("Contains key 'Banana': " + map.containsKey("Banana"));
        System.out.println("Contains value 20: " + map.containsValue(20));
        
        // Remove entry
        map.remove("Banana");
        System.out.println("After removing 'Banana': " + map);
        
        // Get all keys
        Set<String> keys = map.keySet();
        System.out.println("Keys: " + keys);
        
        // Get all values
        Collection<Integer> values = map.values();
        System.out.println("Values: " + values);
        
        // Get all entries
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        System.out.println("\nEntries:");
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println("  Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        
        // Iterate using forEach (Java 8+)
        System.out.println("\nUsing forEach:");
        map.forEach((key, value) -> System.out.println("  " + key + " -> " + value));
        
        System.out.println();
    }
    
    /**
     * LINKEDHASHMAP DEMONSTRATION
     * 
     * LinkedHashMap maintains insertion order or access order.
     * 
     * Best For:
     * - When you need HashMap performance + order
     * - LRU (Least Recently Used) cache implementation
     * - Maintaining insertion/access order
     */
    public static void demonstrateLinkedHashMap() {
        System.out.println("=== LINKEDHASHMAP DEMONSTRATION ===");
        
        // Insertion order (default)
        Map<String, Integer> insertionOrder = new LinkedHashMap<>();
        insertionOrder.put("Third", 3);
        insertionOrder.put("First", 1);
        insertionOrder.put("Second", 2);
        
        System.out.println("Insertion order: " + insertionOrder);
        
        // Access order (for LRU cache)
        // accessOrder = true means order by access, not insertion
        Map<String, Integer> accessOrder = new LinkedHashMap<>(16, 0.75f, true);
        accessOrder.put("A", 1);
        accessOrder.put("B", 2);
        accessOrder.put("C", 3);
        
        System.out.println("Before access: " + accessOrder.keySet());
        accessOrder.get("A");  // Access "A"
        System.out.println("After accessing 'A': " + accessOrder.keySet());
        // "A" moves to end (most recently used)
        
        System.out.println();
    }
    
    /**
     * TREEMAP DEMONSTRATION
     * 
     * TreeMap is backed by a Red-Black Tree.
     * Keys are stored in sorted order.
     * 
     * Best For:
     * - When you need sorted keys
     * - Range queries
     * - Finding min/max keys
     * 
     * Time Complexity: O(log n) for all operations
     */
    public static void demonstrateTreeMap() {
        System.out.println("=== TREEMAP DEMONSTRATION ===");
        
        // Natural ordering (ascending)
        Map<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("Zebra", 26);
        treeMap.put("Apple", 1);
        treeMap.put("Banana", 2);
        treeMap.put("Cherry", 3);
        
        System.out.println("TreeMap (sorted by key): " + treeMap);
        // Output: {Apple=1, Banana=2, Cherry=3, Zebra=26}
        
        // First and last keys
        TreeMap<String, Integer> treeMap2 = (TreeMap<String, Integer>) treeMap;
        System.out.println("First key: " + treeMap2.firstKey());
        System.out.println("Last key: " + treeMap2.lastKey());
        
        // Range operations
        System.out.println("Keys less than 'D': " + treeMap2.headMap("D"));
        System.out.println("Keys greater than or equal to 'C': " + treeMap2.tailMap("C"));
        
        // Descending order
        TreeMap<String, Integer> descending = new TreeMap<>(Collections.reverseOrder());
        descending.putAll(treeMap);
        System.out.println("Descending order: " + descending);
        
        System.out.println();
    }
    
    /**
     * USING CUSTOM CLASS AS KEY IN HASHMAP
     * 
     * IMPORTANT: When using custom class as key, you MUST:
     * 1. Override hashCode() - determines bucket location
     * 2. Override equals() - determines if keys are equal
     * 
     * Contract:
     * - If two objects are equal (equals() returns true), 
     *   they MUST have same hashCode()
     * - If hashCode() is same, objects may or may not be equal
     * 
     * Best Practice:
     * - Use immutable objects as keys
     * - Use final fields for hashCode calculation
     */
    public static void demonstrateHashMapAsKey() {
        System.out.println("=== CUSTOM CLASS AS HASHMAP KEY ===");
        
        // WRONG: Class without hashCode() and equals()
        Map<BadKey, String> badMap = new HashMap<>();
        BadKey badKey1 = new BadKey(1, "A");
        BadKey badKey2 = new BadKey(1, "A");
        
        badMap.put(badKey1, "Value1");
        System.out.println("BadKey - Same object: " + badMap.get(badKey1));  // Found
        System.out.println("BadKey - Equal object: " + badMap.get(badKey2));  // null (NOT FOUND!)
        // Problem: badKey1 and badKey2 are logically equal but HashMap treats them as different
        
        // CORRECT: Class with hashCode() and equals()
        Map<GoodKey, String> goodMap = new HashMap<>();
        GoodKey goodKey1 = new GoodKey(1, "A");
        GoodKey goodKey2 = new GoodKey(1, "A");
        
        goodMap.put(goodKey1, "Value1");
        System.out.println("GoodKey - Same object: " + goodMap.get(goodKey1));  // Found
        System.out.println("GoodKey - Equal object: " + goodMap.get(goodKey2));  // Found!
        // Works correctly: goodKey1 and goodKey2 are treated as same key
        
        System.out.println("\nKey Takeaway:");
        System.out.println("Always override hashCode() and equals() when using custom class as Map key!");
        System.out.println();
    }
    
    /**
     * COMPARISON: HashMap vs LinkedHashMap vs TreeMap
     */
    public static void compareMapImplementations() {
        System.out.println("=== MAP IMPLEMENTATIONS COMPARISON ===");
        
        System.out.println("\nFeature Comparison:");
        System.out.println("Feature          | HashMap | LinkedHashMap | TreeMap");
        System.out.println("-----------------|---------|---------------|---------");
        System.out.println("Order           | No      | Insertion     | Sorted");
        System.out.println("Null key        | Yes(1)  | Yes(1)        | No");
        System.out.println("Null values     | Yes     | Yes           | Yes");
        System.out.println("Performance     | O(1)    | O(1)          | O(log n)");
        System.out.println("Use Case        | General | Order needed  | Sorted needed");
        System.out.println();
    }
    
    /**
     * BAD EXAMPLE: Class without hashCode() and equals()
     * DON'T USE THIS AS MAP KEY!
     */
    static class BadKey {
        int id;
        String name;
        
        BadKey(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    
    /**
     * GOOD EXAMPLE: Class with hashCode() and equals()
     * CAN BE USED AS MAP KEY
     */
    static class GoodKey {
        private final int id;      // final for immutability
        private final String name;  // final for immutability
        
        GoodKey(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        @Override
        public int hashCode() {
            // Use all fields that are used in equals()
            return Objects.hash(id, name);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            GoodKey goodKey = (GoodKey) obj;
            return id == goodKey.id && Objects.equals(name, goodKey.name);
        }
        
        @Override
        public String toString() {
            return "GoodKey{id=" + id + ", name='" + name + "'}";
        }
    }
}


package com.harshit.leetcode.arrays;

/**
 * Problem: Design HashMap
 * 
 * Design a HashMap without using any built-in hash table libraries.
 * 
 * Implement the MyHashMap class:
 * - MyHashMap() initializes the object with an empty map.
 * - void put(int key, int value) inserts a (key, value) pair into the HashMap. If the key
 *   already exists in the map, update the corresponding value.
 * - int get(int key) returns the value to which the specified key is mapped, or -1 if this
 *   map contains no mapping for the key.
 * - void remove(int key) removes the key and its corresponding value if the map contains
 *   the mapping for the key.
 * 
 * Example 1:
 * Input
 * ["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
 * [[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
 * Output
 * [null, null, null, 1, -1, null, 1, null, -1]
 * 
 * Explanation
 * MyHashMap myHashMap = new MyHashMap();
 * myHashMap.put(1, 1); // The map is now [[1,1]]
 * myHashMap.put(2, 2); // The map is now [[1,1], [2,2]]
 * myHashMap.get(1);    // return 1, The map is now [[1,1], [2,2]]
 * myHashMap.get(3);    // return -1 (i.e., not found), The map is now [[1,1], [2,2]]
 * myHashMap.put(2, 1); // The map is now [[1,1], [2,1]] (i.e., update the existing value)
 * myHashMap.get(2);    // return 1, The map is now [[1,1], [2,1]]
 * myHashMap.remove(2); // remove the mapping for 2, The map is now [[1,1]]
 * myHashMap.get(2);    // return -1 (i.e., not found), The map is now [[1,1]]
 * 
 * Constraints:
 * - 0 <= key, value <= 10^6
 * - At most 10^4 calls will be made to put, get, and remove.
 */
public class DesignHashMap {
    
    /**
     * MyHashMap class implementation
     */
    public static class MyHashMap {
        
        public MyHashMap() {
            // Write your logic here
            // Initialize your data structure
        }
        
        public void put(int key, int value) {
            // Write your logic here
            // Hint: Use array or list with hashing
            // Handle collisions (chaining or open addressing)
        }
        
        public int get(int key) {
            // Write your logic here
            // Hint: Hash the key, search in bucket
            // Return value if found, -1 otherwise
            return -1;
        }
        
        public void remove(int key) {
            // Write your logic here
            // Hint: Hash the key, remove from bucket
        }
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MyHashMap myHashMap = new MyHashMap();
        
        myHashMap.put(1, 1);
        myHashMap.put(2, 2);
        
        int result1 = myHashMap.get(1);
        System.out.println("get(1): " + result1 + " (Expected: 1)");
        
        int result2 = myHashMap.get(3);
        System.out.println("get(3): " + result2 + " (Expected: -1)");
        
        myHashMap.put(2, 1);
        int result3 = myHashMap.get(2);
        System.out.println("get(2) after update: " + result3 + " (Expected: 1)");
        
        myHashMap.remove(2);
        int result4 = myHashMap.get(2);
        System.out.println("get(2) after remove: " + result4 + " (Expected: -1)");
    }
}


package com.harshit.collections.advanced;

import java.util.*;
import java.lang.ref.WeakReference;

/**
 * WEAKHASHMAP - Special Map with Weak References
 * 
 * WeakHashMap is a special implementation of Map where keys are stored
 * using weak references.
 * 
 * Key Concept: WEAK REFERENCES
 * - Normal reference (strong): Object cannot be garbage collected
 * - Weak reference: Object CAN be garbage collected if no strong references
 * 
 * How WeakHashMap Works:
 * 1. Keys are stored as WeakReference objects
 * 2. If a key has no strong references elsewhere, it becomes eligible for GC
 * 3. When key is garbage collected, the entry is automatically removed
 * 4. This happens automatically - you don't need to manually remove entries
 * 
 * Use Cases:
 * - Cache implementations (auto-cleanup when keys are no longer used)
 * - Metadata storage (cleanup when objects are GC'd)
 * - Temporary mappings
 * 
 * Important Notes:
 * - Values are NOT weak references (only keys)
 * - If you want weak values, use Guava's MapMaker
 * - Not thread-safe (use Collections.synchronizedMap if needed)
 */
public class WeakHashMapDemo {
    
    public static void main(String[] args) throws InterruptedException {
        demonstrateWeakHashMap();
        compareWeakVsStrongReference();
        useCaseCacheExample();
    }
    
    /**
     * WEAKHASHMAP DEMONSTRATION
     * 
     * Shows how WeakHashMap automatically removes entries when keys are GC'd.
     */
    public static void demonstrateWeakHashMap() throws InterruptedException {
        System.out.println("=== WEAKHASHMAP DEMONSTRATION ===");
        
        // Create WeakHashMap
        WeakHashMap<String, String> weakMap = new WeakHashMap<>();
        
        // Create keys
        String key1 = new String("Key1");  // Strong reference
        String key2 = new String("Key2");  // Strong reference
        
        // Put entries
        weakMap.put(key1, "Value1");
        weakMap.put(key2, "Value2");
        weakMap.put(new String("Key3"), "Value3");  // No strong reference!
        
        System.out.println("Initial map size: " + weakMap.size());
        System.out.println("Map: " + weakMap);
        
        // Remove strong reference to key1
        key1 = null;
        System.out.println("\nRemoved strong reference to key1");
        
        // Suggest garbage collection
        System.gc();
        Thread.sleep(1000);  // Give GC time to run
        
        System.out.println("After GC, map size: " + weakMap.size());
        System.out.println("Map: " + weakMap);
        System.out.println("Note: key1 entry may be removed (depends on GC)");
        
        // key2 still has strong reference
        System.out.println("\nkey2 still has strong reference: " + weakMap.containsKey(key2));
        
        // Remove strong reference to key2
        key2 = null;
        System.gc();
        Thread.sleep(1000);
        
        System.out.println("After removing key2 reference and GC:");
        System.out.println("Map size: " + weakMap.size());
        System.out.println("Map: " + weakMap);
        
        System.out.println();
    }
    
    /**
     * COMPARISON: Weak Reference vs Strong Reference
     */
    public static void compareWeakVsStrongReference() {
        System.out.println("=== WEAK vs STRONG REFERENCE ===");
        
        System.out.println("\nStrong Reference (Normal HashMap):");
        System.out.println("  - Object cannot be garbage collected");
        System.out.println("  - Map holds strong reference to key");
        System.out.println("  - Key exists as long as map exists");
        System.out.println("  - Memory leak risk if keys are not removed");
        
        System.out.println("\nWeak Reference (WeakHashMap):");
        System.out.println("  - Object CAN be garbage collected");
        System.out.println("  - Map holds weak reference to key");
        System.out.println("  - Key can be GC'd if no other strong references");
        System.out.println("  - Entry automatically removed when key is GC'd");
        System.out.println("  - Prevents memory leaks");
        
        System.out.println("\nExample:");
        System.out.println("  HashMap: Key exists even if original reference is null");
        System.out.println("  WeakHashMap: Key can be GC'd if original reference is null");
        System.out.println();
    }
    
    /**
     * USE CASE: Cache Implementation
     * 
     * WeakHashMap is useful for cache implementations where you want
     * automatic cleanup when cached objects are no longer referenced.
     */
    public static void useCaseCacheExample() {
        System.out.println("=== USE CASE: CACHE IMPLEMENTATION ===");
        
        // Example: Metadata cache
        // When User objects are no longer used, their metadata is automatically removed
        WeakHashMap<User, UserMetadata> metadataCache = new WeakHashMap<>();
        
        User user1 = new User("Alice", 25);
        User user2 = new User("Bob", 30);
        
        metadataCache.put(user1, new UserMetadata("Admin", "Active"));
        metadataCache.put(user2, new UserMetadata("User", "Active"));
        
        System.out.println("Cache size: " + metadataCache.size());
        System.out.println("User1 metadata: " + metadataCache.get(user1));
        
        // When user1 is no longer referenced, its metadata is automatically removed
        user1 = null;
        System.gc();
        
        System.out.println("\nAfter user1 is GC'd:");
        System.out.println("Cache size: " + metadataCache.size());
        System.out.println("Note: user1 entry may be removed automatically");
        
        System.out.println("\n✅ Benefits:");
        System.out.println("  - Automatic cleanup");
        System.out.println("  - No manual cache invalidation needed");
        System.out.println("  - Prevents memory leaks");
        System.out.println();
    }
    
    /**
     * Helper class for demonstration
     */
    static class User {
        String name;
        int age;
        
        User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        @Override
        public String toString() {
            return name + " (" + age + ")";
        }
    }
    
    /**
     * Helper class for demonstration
     */
    static class UserMetadata {
        String role;
        String status;
        
        UserMetadata(String role, String status) {
            this.role = role;
            this.status = status;
        }
        
        @Override
        public String toString() {
            return "Role: " + role + ", Status: " + status;
        }
    }
}


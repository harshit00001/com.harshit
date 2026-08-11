package com.harshit.map;

import java.util.HashMap;
import java.util.Map;

/**
 * INTERVIEW Q: Explain HashMap internals (Accenture favorite at 4 YOE).
 *
 * <p><b>put(key, value) flow:</b>
 * <ol>
 *   <li>Compute hash = spread(key.hashCode()) — extra bit-mixing reduces collisions</li>
 *   <li>Bucket index = (table.length - 1) & hash</li>
 *   <li>If bucket empty → insert new Node</li>
 *   <li>If bucket occupied → walk chain; if same key (equals) → replace value</li>
 *   <li>Else append to linked list</li>
 *   <li>Java 8+: if chain length ≥ 8 AND table length ≥ 64 → convert to red-black tree</li>
 *   <li>If size > capacity * loadFactor (default 0.75) → resize (double) and rehash</li>
 * </ol>
 *
 * <p><b>get(key):</b> same hash → find bucket → equals() match along chain/tree.
 *
 * <p><b>Complexity:</b> average O(1) put/get; worst O(n) before treeify, O(log n) with tree bins.
 *
 * <p><b>Thread safety:</b> HashMap is NOT thread-safe. Use ConcurrentHashMap for concurrency.
 *
 * <p><b>Null:</b> one null key allowed; multiple null values allowed.
 */
public final class HashMapInternalsDemo {

    private HashMapInternalsDemo() {}

    public static void main(String[] args) {
        basicOperations();
        collisionDemo();
        resizeConcept();
        nullKeyDemo();
    }

    /**
     * Core API: put replaces value for duplicate key; get returns null if absent
     * (or if value itself is null — ambiguity is why CHM disallows null).
     */
    private static void basicOperations() {
        System.out.println("=== BASIC OPERATIONS ===");

        Map<String, Integer> map = new HashMap<>();
        map.put("Java", 17);
        map.put("Python", 3);
        map.put("Java", 21); // same key → value replaced, size stays 2

        System.out.println("size: " + map.size());
        System.out.println("get(\"Java\"): " + map.get("Java")); // 21
        System.out.println("containsKey(\"Go\"): " + map.containsKey("Go")); // false
        System.out.println();
    }

    /**
     * INTERVIEW Q: What happens when two keys have the same hash?
     * ANSWER: Collision — both land in same bucket. Linked list (or tree) holds entries.
     * equals() distinguishes keys; hashCode alone is NOT enough.
     *
     * <p>Bad hashCode (always returns constant) → all keys in one bucket → degrades to O(n).
     */
    private static void collisionDemo() {
        System.out.println("=== COLLISIONS ===");

        // Keys with identical hashCode — forces same bucket (extreme demo)
        Map<BadHashKey, String> badMap = new HashMap<>();
        badMap.put(new BadHashKey("A"), "value-A");
        badMap.put(new BadHashKey("B"), "value-B"); // different key, same bucket

        System.out.println("BadHashKey map size: " + badMap.size()); // 2 — equals keeps them distinct
        System.out.println("get(B): " + badMap.get(new BadHashKey("B")));

        // Good keys spread across buckets
        Map<String, String> goodMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            goodMap.put("key-" + i, "val-" + i);
        }
        System.out.println("Well-distributed map size: " + goodMap.size());
        System.out.println("→ Always override hashCode + equals for custom keys.");
        System.out.println();
    }

    /**
     * Default: capacity=16, loadFactor=0.75 → resize when size > 12.
     * Resize doubles table; all entries rehashed into new buckets.
     * Amortized O(1) despite occasional O(n) resize.
     */
    private static void resizeConcept() {
        System.out.println("=== RESIZE CONCEPT ===");

        // Initial capacity hint avoids repeated resize during bulk insert
        Map<Integer, String> map = new HashMap<>(32); // starts with capacity 32

        for (int i = 0; i < 20; i++) {
            map.put(i, "employee-" + i);
        }

        System.out.println("Inserted 20 entries into HashMap(capacity=32)");
        System.out.println("size=" + map.size() + " — no resize yet (threshold ~24)");
        System.out.println("→ For known size N, use new HashMap<>((int)(N/0.75)+1) to avoid resize churn.");
        System.out.println();
    }

    /**
     * HashMap allows exactly one null key. Multiple null values OK.
     * ConcurrentHashMap does NOT allow null — avoids ambiguous get() results under concurrency.
     */
    private static void nullKeyDemo() {
        System.out.println("=== NULL KEY ===");

        Map<String, String> map = new HashMap<>();
        map.put(null, "null-key-value");
        map.put("name", null);

        System.out.println("get(null): " + map.get(null));
        System.out.println("get(\"missing\"): " + map.get("missing")); // also null — ambiguous!
        System.out.println("containsKey(\"missing\"): " + map.containsKey("missing")); // use this to distinguish
    }

    /**
     * Anti-pattern for interviews: constant hashCode causes single-bucket pile-up.
     */
    static final class BadHashKey {
        private final String id;

        BadHashKey(String id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return 42; // deliberate collision — NEVER do this in production
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BadHashKey other)) return false;
            return id.equals(other.id);
        }
    }
}

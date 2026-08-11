package com.harshit.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * INTERVIEW Q: How to implement an LRU cache using Java Collections?
 *
 * <p><b>Answer:</b> {@link LinkedHashMap} with {@code accessOrder=true}.
 * Override {@code removeEldestEntry} to evict when size exceeds capacity.
 *
 * <p><b>How access-order works:</b>
 * <ul>
 *   <li>get/put moves entry to tail (most recently used)</li>
 *   <li>Head = least recently used → evicted first</li>
 * </ul>
 *
 * <p><b>4 YOE note:</b> In production, prefer Caffeine or Guava Cache for
 * concurrency, stats, TTL, and weighted eviction. LinkedHashMap LRU is the
 * interview pattern and fine for single-threaded bounded caches.
 *
 * <p><b>Complexity:</b> get/put O(1) average (hash + linked list pointer updates).
 */
public final class LruCacheDemo {

    private LruCacheDemo() {}

    public static void main(String[] args) {
        demonstrateLruEviction();
        demonstrateAccessOrder();
    }

    /**
     * LRU cache with max 3 entries. When 4th is added, eldest (LRU) is removed automatically.
     */
    private static void demonstrateLruEviction() {
        System.out.println("=== LRU Cache (max size 3) ===");

        LruCache<String, String> cache = new LruCache<>(3);

        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        cache.put("user:3", "Charlie");
        System.out.println("After 3 inserts: " + cache.snapshot());

        cache.put("user:4", "Diana"); // triggers eviction of LRU entry (user:1)
        System.out.println("After inserting user:4 (evicts eldest): " + cache.snapshot());

        cache.get("user:2"); // access moves user:2 to MRU — affects next eviction victim
        cache.put("user:5", "Eve");
        System.out.println("After get(user:2) + insert user:5: " + cache.snapshot());
        System.out.println();
    }

    /**
     * Compare insertion-order vs access-order LinkedHashMap behavior.
     */
    private static void demonstrateAccessOrder() {
        System.out.println("=== insertion-order vs access-order ===");

        // insertion-order (default): iteration order = first insert order
        Map<String, Integer> insertionOrder = new LinkedHashMap<>();
        insertionOrder.put("A", 1);
        insertionOrder.put("B", 2);
        insertionOrder.get("A"); // does NOT move A in insertion-order mode
        System.out.println("Insertion-order after get(A): " + insertionOrder.keySet());

        // access-order: get/put reorders — basis for LRU
        Map<String, Integer> accessOrder = new LinkedHashMap<>(16, 0.75f, true);
        accessOrder.put("A", 1);
        accessOrder.put("B", 2);
        accessOrder.get("A"); // A moves to tail (most recently used)
        System.out.println("Access-order after get(A): " + accessOrder.keySet()); // [B, A]
    }

    /**
     * LinkedHashMap subclass — classic LRU interview implementation.
     *
     * @param <K> key type
     * @param <V> value type
     */
    static final class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;

        /**
         * @param maxSize maximum entries before eldest is evicted
         */
        LruCache(int maxSize) {
            // initialCapacity, loadFactor, accessOrder=true ← critical for LRU
            super(maxSize, 0.75f, true);
            this.maxSize = maxSize;
        }

        /**
         * Called by LinkedHashMap after insert. Return true to remove eldest (LRU) entry.
         * Interview tip: this hook is what makes LRU "automatic".
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxSize;
        }

        Map<K, V> snapshot() {
            return new LinkedHashMap<>(this);
        }
    }
}

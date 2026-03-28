package com.harshit.preparation.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h2>Interview: How does HashMap work internally?</h2>
 * <p><b>Short answer:</b> A HashMap is backed by an array of <i>buckets</i>. The array index is
 * derived from {@code hash(key)} (after spreading with XOR in modern JDK to improve distribution).
 * If two keys collide (same bucket), entries are chained — since Java 8 as linked list, and
 * converted to a balanced tree when a bucket grows large (treeify threshold).
 * <p>
 * <b>Default capacity</b> is 16; <b>load factor</b> 0.75 — when {@code size > capacity * loadFactor},
 * the table <i>resizes</i> (rehash), which is O(n) but amortized O(1) for typical use.
 * <p>
 * <b>Same hashCode, different keys:</b> Both land in the same bucket; {@code equals} distinguishes
 * keys. You must implement {@code hashCode} and {@code equals} consistently for custom keys.
 * <p>
 * <b>Thread-safety:</b> {@link HashMap} is not thread-safe. For concurrent access use
 * {@link java.util.concurrent.ConcurrentHashMap} or wrap with
 * {@link java.util.Collections#synchronizedMap}.
 * <p>
 * <b>Null key:</b> HashMap allows one {@code null} key (stored in bucket 0). {@link java.util.TreeMap}
 * does not allow null keys if natural ordering cannot handle it.
 */
public final class HashMapInternalsDemo {

    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 90);
        scores.put("Bob", 85);
        scores.merge("Alice", 5, Integer::sum);
        System.out.println("scores = " + scores);
        System.out.println("Average get/put is O(1) if hash spread is good; worst O(n) if all keys collide.");

        /*
         * Custom key: interview trap — if equals/hashCode wrong, map breaks.
         */
        Map<PersonId, String> roster = new HashMap<>();
        roster.put(new PersonId(1L), "Harshit");
        System.out.println("lookup same logical id: " + roster.get(new PersonId(1L)));
    }

    /** Minimal key type with correct equals/hashCode for use in HashMap / HashSet. */
    static final class PersonId {
        private final long id;

        PersonId(long id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PersonId other)) {
                return false;
            }
            return id == other.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    private HashMapInternalsDemo() {
    }
}

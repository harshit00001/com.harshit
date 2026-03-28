package com.harshit.preparation.topic01;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Topic 01 — HashMap, HashSet, Collections.
 * Each block below is a short spoken script for interviews (read the SCRIPT paragraphs aloud-style).
 */
public final class Topic01Qa {

    private Topic01Qa() {
    }

    /*
     * Q: How does HashMap work internally?
     *
     * SCRIPT:
     * I would explain that a HashMap is backed by an array of buckets. For each key we use
     * hashCode()—after some bit-mixing in the JDK—to pick a bucket index. Inside a bucket, if
     * multiple keys collide, entries are chained; from Java 8, if a bucket grows very long it
     * can become a tree for performance. To retrieve a value we hash the key, find the bucket,
     * then walk that bucket comparing keys with equals—not ==. So hashCode narrows the search
     * and equals confirms identity.
     *
     * REAL LIFE:
     * Think of a post office: the hash tells you which row of boxes; equals is the name on the box.
     *
     * CODE: see examples().
     */

    /*
     * Q: Default capacity and load factor?
     *
     * SCRIPT:
     * I would say the defaults are capacity 16 and load factor 0.75. The load factor controls when
     * the table grows: when the number of entries crosses capacity times load factor, the array
     * is resized and entries are rehashed. A higher load factor means fewer resizes but more
     * collisions in each bucket, so it is a trade-off the JDK chose for general-purpose use.
     */

    /*
     * Q: What if two keys have the same hashCode?
     *
     * SCRIPT:
     * Collisions are expected. Both keys map to the same bucket, and the map still works because
     * equals() tells them apart. In the interview I would stress that you must implement hashCode
     * and equals together—if two objects are equal they must have the same hash, or the map breaks.
     */

    /*
     * Q: Is HashMap thread-safe?
     *
     * SCRIPT:
     * No—ordinary HashMap is not safe if one thread writes while another reads or writes; the
     * internal structure can get corrupted. I would say for concurrent access I use
     * ConcurrentHashMap, or wrap with Collections.synchronizedMap, or guard access with a lock,
     * depending on contention and consistency needs.
     */

    /*
     * Q: HashMap vs LinkedHashMap vs TreeMap?
     *
     * SCRIPT:
     * HashMap does not guarantee iteration order. LinkedHashMap keeps insertion order—or optionally
     * access order—which is useful for LRU-style caches. TreeMap keeps keys sorted using a
     * Comparator or natural ordering, backed by a red-black tree, so lookups are log n. I would
     * pick HashMap by default, LinkedHashMap when order matters, TreeMap when I need sorted keys
     * or range queries.
     */

    /*
     * Q: Can HashMap have a null key?
     *
     * SCRIPT:
     * Yes—HashMap allows one null key because it can be placed in a dedicated bucket. I would add
     * that ConcurrentHashMap does not allow null keys or values, because null is ambiguous in
     * concurrent code—did the key not exist or is it null?
     */

    /*
     * Q: Time complexity of HashMap operations?
     *
     * SCRIPT:
     * I would say average case get and put are constant time if the hash function spreads keys
     * well. Worst case, if every key collides in one bucket, it degrades toward linear time. In
     * practice good equals/hashCode and resizing keep performance close to O(1).
     */

    /*
     * Q: How does ConcurrentHashMap work (Java 8+)?
     *
     * SCRIPT:
     * I would describe it as a node array with finer-grained locking than Hashtable. Often reads
     * do not take the heavy lock; writes may CAS or lock the first node of a bucket. It is designed
     * for concurrent scalability, and iterators are weakly consistent—they may reflect recent changes
     * without throwing ConcurrentModificationException like HashMap’s fail-fast iterator.
     */

    /*
     * Q: Make an existing HashMap thread-safe without ConcurrentHashMap?
     *
     * SCRIPT:
     * I can wrap it with Collections.synchronizedMap, which serializes all operations on one lock.
     * It is simpler but can become a bottleneck under high contention, which is why ConcurrentHashMap
     * is usually preferred for parallel workloads.
     */

    /*
     * Q: HashMap vs ConcurrentHashMap?
     *
     * SCRIPT:
     * HashMap is for single-threaded or externally synchronized use. ConcurrentHashMap is built
     * for many threads reading and writing; it has different iterator semantics and stricter null
     * rules. I would not use HashMap as a shared cache across threads without synchronization.
     */

    /*
     * Q: Fail-fast vs fail-safe iterators?
     *
     * SCRIPT:
     * Fail-fast means if the map is structurally changed while iterating—except through the
     * iterator’s own remove—the iterator detects that and throws ConcurrentModificationException.
     * ConcurrentHashMap’s iterators are fail-safe in the sense they do not throw that way; they
     * reflect a weakly consistent view of the map, which may include concurrent updates.
     */

    /*
     * Q: How does HashSet identify duplicates?
     *
     * SCRIPT:
     * HashSet is implemented with a HashMap behind the scenes. The element you add is the key,
     * and a dummy object is the value. If add returns false, the element was already in the set—
     * that is how duplicates are rejected in constant expected time.
     */

    /*
     * Q: Custom class in a Set—what should you consider?
     *
     * SCRIPT:
     * I would say override equals and hashCode using the same business fields, and prefer
     * immutable fields for keys. If someone mutates an object after it was inserted, its hash
     * bucket may no longer match, and the set can silently break—so immutability is the safe choice.
     */

    /** Runnable snippets tied to the scripts above. */
    public static void examples() {
        Map<String, Integer> map = new HashMap<>();
        map.put("k1", 1);
        map.merge("k1", 2, Integer::sum);

        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("a");

        ConcurrentHashMap<String, String> chm = new ConcurrentHashMap<>();
        chm.put("x", "y");

        Map<Key, String> m2 = new HashMap<>();
        m2.put(new Key(1L), "one");
        System.out.println(m2.get(new Key(1L)));
    }

    static final class Key {
        private final long id;

        Key(long id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Key k && id == k.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    public static void main(String[] args) {
        examples();
    }
}

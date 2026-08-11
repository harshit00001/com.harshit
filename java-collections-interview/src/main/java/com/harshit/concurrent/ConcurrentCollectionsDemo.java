package com.harshit.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * INTERVIEW Q: HashMap vs Hashtable vs ConcurrentHashMap?
 *
 * <table>
 *   <tr><th></th><th>Thread-safe?</th><th>Null key/value</th><th>Locking</th></tr>
 *   <tr><td>HashMap</td><td>No</td><td>Yes</td><td>None</td></tr>
 *   <tr><td>Hashtable</td><td>Yes (legacy)</td><td>No</td><td>Synchronizes entire map</td></tr>
 *   <tr><td>ConcurrentHashMap</td><td>Yes</td><td>No</td><td>Bucket-level / CAS (Java 8+)</td></tr>
 * </table>
 *
 * <p>INTERVIEW Q: Is ConcurrentHashMap fully safe for check-then-act?
 * ANSWER: No. {@code if (!map.containsKey(k)) map.put(k,v)} is still racy.
 * Use atomic methods: putIfAbsent, compute, computeIfAbsent, merge.
 *
 * <p>INTERVIEW Q: CopyOnWriteArrayList — when?
 * ANSWER: Read-heavy, write-rare (listeners, config snapshots). Writes copy entire array → O(n).
 */
public final class ConcurrentCollectionsDemo {

    private ConcurrentCollectionsDemo() {}

    public static void main(String[] args) throws InterruptedException {
        hashMapNotThreadSafe();
        concurrentHashMapAtomicOps();
        checkThenActTrap();
        copyOnWriteDemo();
    }

    /**
     * Plain HashMap under concurrent writes can corrupt internal structure or lose updates.
     * This demo shows lost increments (race on read-modify-write).
     */
    private static void hashMapNotThreadSafe() throws InterruptedException {
        System.out.println("=== HashMap is NOT thread-safe ===");

        Map<String, Integer> unsafeMap = new HashMap<>();
        unsafeMap.put("counter", 0);

        int threads = 10;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            new Thread(() -> {
                for (int i = 0; i < incrementsPerThread; i++) {
                    // RACE: read → increment → write is not atomic
                    Integer current = unsafeMap.get("counter");
                    unsafeMap.put("counter", current + 1);
                }
                latch.countDown();
            }).start();
        }
        latch.await();

        int expected = threads * incrementsPerThread;
        int actual = unsafeMap.get("counter");
        System.out.printf("Expected counter=%d, actual=%d (lost updates!)%n", expected, actual);
        System.out.println();
    }

    /**
     * ConcurrentHashMap.merge atomically combines values — safe counter increment.
     */
    private static void concurrentHashMapAtomicOps() throws InterruptedException {
        System.out.println("=== ConcurrentHashMap atomic merge ===");

        Map<String, Integer> safeMap = new ConcurrentHashMap<>();
        safeMap.put("counter", 0);

        int threads = 10;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            new Thread(() -> {
                for (int i = 0; i < incrementsPerThread; i++) {
                    // Atomic: no lost updates
                    safeMap.merge("counter", 1, Integer::sum);
                }
                latch.countDown();
            }).start();
        }
        latch.await();

        System.out.println("Counter with merge: " + safeMap.get("counter")); // always 10000
        System.out.println();

        // computeIfAbsent — lazy initialization (e.g., cache miss loads value)
        Map<String, String> cache = new ConcurrentHashMap<>();
        String value = cache.computeIfAbsent("user:101", key -> "Loaded from DB for " + key);
        System.out.println("computeIfAbsent: " + value);
        System.out.println("Second call returns cached: " + cache.computeIfAbsent("user:101", k -> "should-not-run"));
        System.out.println();
    }

    /**
     * Even with CHM, compound check-then-act without atomic API is wrong.
     */
    private static void checkThenActTrap() throws InterruptedException {
        System.out.println("=== check-then-act trap ===");

        Map<String, String> map = new ConcurrentHashMap<>();
        AtomicInteger putCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(2);

        Runnable racyInsert = () -> {
            // WRONG pattern — two threads can both pass containsKey check
            if (!map.containsKey("token")) {
                map.put("token", "generated-" + Thread.currentThread().getId());
                putCount.incrementAndGet();
            }
            latch.countDown();
        };

        new Thread(racyInsert).start();
        new Thread(racyInsert).start();
        latch.await();

        System.out.println("Racy put attempts that succeeded: " + putCount.get()); // may be 2
        System.out.println("→ Use putIfAbsent or computeIfAbsent instead.");

        map.clear();
        map.putIfAbsent("token", "safe-token");
        System.out.println("putIfAbsent result: " + map.get("token"));
        System.out.println();
    }

    /**
     * CopyOnWriteArrayList: iterators snapshot array at creation time.
     * Writers copy entire backing array — expensive for frequent writes.
     */
    private static void copyOnWriteDemo() {
        System.out.println("=== CopyOnWriteArrayList ===");

        CopyOnWriteArrayList<String> listeners = new CopyOnWriteArrayList<>();
        listeners.add("AuditLogger");
        listeners.add("MetricsExporter");

        // Iterator never throws ConcurrentModificationException — reads snapshot
        for (String listener : listeners) {
            System.out.println("Notifying: " + listener);
            if ("AuditLogger".equals(listener)) {
                listeners.add("NewListener"); // triggers array copy, iterator won't see it
            }
        }

        System.out.println("Final listeners: " + listeners);
        System.out.println("→ Ideal for read-mostly listener lists; bad for write-heavy workloads.");

        // synchronizedMap wraps entire map — simpler but coarse-grained lock
        Map<String, Integer> synced = Collections.synchronizedMap(new HashMap<>());
        synced.put("legacy", 1);
        System.out.println("Collections.synchronizedMap: " + synced);
    }
}

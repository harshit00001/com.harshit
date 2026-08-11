package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * INTERVIEW Q: HashMap vs ConcurrentHashMap? ArrayList vs CopyOnWriteArrayList?
 *
 * <p><b>ConcurrentHashMap:</b> thread-safe map with segment/bucket locking (Java 8+ CAS + synchronized bins).
 * Never wrap HashMap with Collections.synchronizedMap for high concurrency without understanding lock contention.
 *
 * <p><b>CopyOnWriteArrayList:</b> snapshot iterator — great for read-heavy, rare writes; writes copy entire array.
 */
public final class Q07ConcurrentCollections implements InterviewDemo {

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q07: Concurrent Collections ===\n");

        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        Runnable writer = () -> {
            for (int i = 0; i < 1_000; i++) {
                concurrentMap.merge("key", 1, Integer::sum);
            }
        };

        Thread t1 = new Thread(writer);
        Thread t2 = new Thread(writer);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("ConcurrentHashMap merge count (expect 2000): " + concurrentMap.get("key"));

        // synchronizedMap — single lock on entire map operations
        Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
        syncMap.put("a", 1);
        System.out.println("Synchronized map: " + syncMap);

        CopyOnWriteArrayList<String> listeners = new CopyOnWriteArrayList<>();
        listeners.add("Listener-1");
        listeners.add("Listener-2");

        // Iterator never throws CME even if list modified during iteration (snapshot)
        for (String l : listeners) {
            if ("Listener-1".equals(l)) {
                listeners.add("Listener-3"); // safe for iteration, expensive write
            }
            System.out.println("Listener: " + l);
        }
        System.out.println("Final listeners: " + listeners);

        System.out.println("\n→ Fail-fast vs concurrent collections ties to iterator behavior in interviews.");
    }

    public static void main(String[] args) throws Exception {
        new Q07ConcurrentCollections().run();
    }
}

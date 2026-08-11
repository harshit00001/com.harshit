package com.harshit.iteration;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * INTERVIEW Q: Fail-fast vs fail-safe iterators?
 *
 * <p><b>Fail-fast</b> (ArrayList, HashMap, HashSet):
 * Iterator tracks expected modCount. Structural change during iteration →
 * ConcurrentModificationException on next call.
 *
 * <p><b>Fail-safe / weakly consistent</b> (CopyOnWriteArrayList, ConcurrentHashMap):
 * Iterator works on snapshot or tolerates concurrent changes — no CME,
 * but may not reflect latest state.
 *
 * <p>INTERVIEW Q: How to remove elements while iterating a Map?
 * ANSWER: Use Iterator.remove(), entrySet iterator, removeIf, or collect keys first.
 * Never map.remove(key) during enhanced-for over keySet.
 */
public final class FailFastVsFailSafeDemo {

    private FailFastVsFailSafeDemo() {}

    public static void main(String[] args) {
        failFastDemo();
        safeRemovalDemo();
        failSafeDemo();
        mapViewDemo();
    }

    /**
     * Modifying list structurally during enhanced-for triggers CME.
     */
    private static void failFastDemo() {
        System.out.println("=== Fail-fast (ConcurrentModificationException) ===");

        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));

        try {
            for (String item : list) {
                if ("B".equals(item)) {
                    list.remove(item); // structural modification during iteration
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Caught CME: " + e.getClass().getSimpleName());
            System.out.println("→ Fail-fast iterator detected modCount change.");
        }
        System.out.println();
    }

    /**
     * Correct removal: Iterator.remove() is designed for in-loop deletion.
     */
    private static void safeRemovalDemo() {
        System.out.println("=== Safe removal with Iterator.remove() ===");

        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if ("B".equals(it.next())) {
                it.remove(); // updates modCount correctly — no CME
            }
        }
        System.out.println("After safe removal: " + list); // [A, C, D]

        // Java 8+ alternative
        list.removeIf(s -> "D".equals(s));
        System.out.println("After removeIf: " + list); // [A, C]
        System.out.println();
    }

    /**
     * CopyOnWriteArrayList iterator snapshots array — no CME even if list modified during loop.
     */
    private static void failSafeDemo() {
        System.out.println("=== Fail-safe (CopyOnWriteArrayList) ===");

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("X", "Y", "Z"));

        for (String item : list) {
            System.out.println("Reading: " + item);
            list.add("New-" + item); // no CME — but iterator won't see new elements
        }
        System.out.println("Final list: " + list);
        System.out.println("→ Iterator saw snapshot; concurrent adds didn't appear mid-loop.");
        System.out.println();
    }

    /**
     * Map views (keySet, values, entrySet) are backed by the map.
     * Mutating view mutates map; removing via entry iterator is safe.
     */
    private static void mapViewDemo() {
        System.out.println("=== Map backed views ===");

        Map<String, Integer> map = new HashMap<>();
        map.put("Java", 17);
        map.put("Python", 3);
        map.put("Go", 1);

        // WRONG: remove during keySet enhanced-for
        try {
            for (String key : map.keySet()) {
                if ("Python".equals(key)) {
                    map.remove(key); // CME
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("keySet loop + map.remove → CME");
        }

        // CORRECT: entrySet iterator
        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            if ("Go".equals(entry.getKey())) {
                it.remove();
            }
        }
        System.out.println("After entry iterator removal: " + map);
    }


}

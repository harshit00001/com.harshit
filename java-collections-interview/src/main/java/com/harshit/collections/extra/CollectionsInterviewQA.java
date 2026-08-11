package com.harshit.collections.extra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * Java Collections interview Q&A — placed under java-collections-interview project.
 *
 * <p><b>Q. How does HashMap work internally?</b> Array of buckets; hash(key) maps to index; collisions
 * use linked nodes (treeified if many). Rehash when load factor exceeded. Real life: O(1) average
 * lookups for userId → session.
 *
 * <p><b>Q. Default capacity and load factor?</b> Typically capacity 16, load factor 0.75 — when
 * size > capacity * load factor, map resizes (rehashes). Tuning trade-off: memory vs rehash cost.
 *
 * <p><b>Q. Hash collision?</b> Multiple keys same bucket index; equals() disambiguates entries.
 * Bad hash spreads hurt performance (long chains). Real life: custom keys must implement good hashCode.
 *
 * <p><b>Q. HashMap vs LinkedHashMap vs TreeMap?</b> HashMap unordered; LinkedHashMap insertion/access
 * order; TreeMap sorted by key (Red-Black tree). Real life: LRU cache often uses LinkedHashMap;
 * sorted leaderboard uses TreeMap.
 *
 * <p><b>Q. Null keys/values in HashMap?</b> One null key allowed; many null values. Hashtable/ConcurrentHashMap differ.
 *
 * <p><b>Q. List vs Set vs Map?</b> List ordered, allows duplicates; Set unique elements; Map key→value.
 * Real life: List = ticket queue; Set = unique visitor IDs; Map = productId → price.
 *
 * <p><b>Q. ArrayList vs LinkedList?</b> ArrayList backed by array (fast random access); LinkedList
 * doubly-linked (fast inserts in middle with iterator). Real life: mostly choose ArrayList; LinkedList
 * for frequent head/tail ops.
 *
 * <p><b>Q. Comparable vs Comparator?</b> Comparable natural ordering inside class (compareTo);
 * Comparator external strategy. Real life: Employee compares by id internally; HR sorts by name via Comparator.
 *
 * <p><b>Q. How sorting works?</b> Collections.sort / List.sort use TimSort (objects) or Arrays.sort
 * primitives; TreeSet/TreeMap keep sorted order via Comparable/Comparator.
 */
public final class CollectionsInterviewQA {

    private CollectionsInterviewQA() {
    }

    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("ada", 100);
        scores.put("bob", 92);
        scores.put(null, 0); // allowed in HashMap
        System.out.println("HashMap: " + scores);

        Map<String, Integer> ordered = new LinkedHashMap<>();
        ordered.put("second", 2);
        ordered.put("first", 1);
        System.out.println("LinkedHashMap preserves insertion: " + ordered.keySet());

        Map<Integer, String> sorted = new TreeMap<>();
        sorted.put(10, "ten");
        sorted.put(2, "two");
        System.out.println("TreeMap keys sorted: " + sorted);

        List<String> names = new ArrayList<>(Arrays.asList("Zara", "amy", "Ben"));
        names.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println("Sorted with Comparator: " + names);

        List<Integer> linked = new LinkedList<>(Arrays.asList(1, 2, 3));
        linked.add(0, 0);
        System.out.println("LinkedList after head insert: " + linked);

        List<Person> people = Arrays.asList(new Person(2, "Bob"), new Person(1, "Ada"));
        people.sort(Comparator.comparingInt(Person::id));
        System.out.println("Comparable/Comparator demo: " + people);
    }

    static final class Person {
        private final int id;
        private final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = Objects.requireNonNull(name);
        }

        int id() {
            return id;
        }

        @Override
        public String toString() {
            return id + ":" + name;
        }
    }
}

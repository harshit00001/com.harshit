package com.harshit.preparation.streams;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <h2>Streams, functional interfaces, Optional (Java 8+)</h2>
 * <p>
 * <b>Intermediate ops</b> (lazy): filter, map, flatMap, distinct, sorted.
 * <b>Terminal ops</b> (eager): collect, forEach, reduce, findFirst.
 * <p>
 * {@link java.util.Optional} models absent values without null — prefer for return types, not fields.
 */
public final class StreamBasicsDemo {

    public static void main(String[] args) {
        List<String> words = List.of("apple", "banana", "apricot", "cherry");

        Predicate<String> startsWithA = s -> s.startsWith("a");
        long count = words.stream().filter(startsWithA).count();
        System.out.println("count starting with a: " + count);

        List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4));
        List<Integer> flat = nested.stream().flatMap(List::stream).toList();
        System.out.println("flatMap: " + flat);

        Optional<String> first = Stream.of("x", "y").findFirst();
        System.out.println("optional: " + first.orElse("none"));
    }

    private StreamBasicsDemo() {
    }
}

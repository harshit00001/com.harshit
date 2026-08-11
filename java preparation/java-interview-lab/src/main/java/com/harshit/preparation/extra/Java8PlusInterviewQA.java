package com.harshit.preparation.extra;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Java 8+ interview topics — under java-interview-lab (java preparation workspace).
 *
 * <p><b>Q. Lambda expressions?</b> Anonymous functions implementing a functional interface.
 * Real life: event handlers, stream operations without boilerplate classes.
 *
 * <p><b>Q. Stream API?</b> Declarative pipeline (source → intermediate ops → terminal op) for collections.
 * Real life: filter active users, map to DTOs, collect — readable data transforms.
 *
 * <p><b>Q. map vs flatMap?</b> map: 1-to-1; flatMap: 1-to-many flattening (Stream of Streams → Stream).
 * Real life: map user→email; flatMap user→list of orders → stream of all order lines.
 *
 * <p><b>Q. Optional?</b> Container for absent/present values to avoid null checks; orElse, map, flatMap.
 * Real life: repository.findById returns Optional — explicit handling of missing rows.
 *
 * <p><b>Q. Functional interfaces?</b> Single abstract method (SAM) types: Predicate, Function, Consumer, Supplier.
 *
 * <p><b>Q. Method reference?</b> Shorthand for lambdas calling existing methods: String::length, User::getName.
 */
public final class Java8PlusInterviewQA {

    private Java8PlusInterviewQA() {
    }

    public static void main(String[] args) {
        List<String> words = List.of("  apple ", "banana", "  ");

        List<String> cleaned = words.stream()
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .toList();
        System.out.println("map + filter: " + cleaned);

        // flatMap: lines split into letters stream (example)
        long letterCount = Stream.of("hi", "there")
                .flatMap(s -> s.chars().mapToObj(c -> String.valueOf((char) c)))
                .count();
        System.out.println("flatMap letter count: " + letterCount);

        Predicate<Integer> positive = n -> n > 0;
        Function<Integer, Integer> square = n -> n * n;
        Consumer<Integer> print = System.out::println;
        print.accept(square.apply(4));
        System.out.println("positive.test(-1)=" + positive.test(-1));

        Optional<String> maybe = Optional.of("token");
        System.out.println(maybe.map(String::toUpperCase).orElse("none"));
        System.out.println(Optional.<String>empty().orElse("fallback"));
    }
}

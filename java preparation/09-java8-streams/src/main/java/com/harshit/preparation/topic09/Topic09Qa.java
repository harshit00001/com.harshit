package com.harshit.preparation.topic09;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Topic 09 — Java 8 streams, Optional, functional interfaces.
 */
public final class Topic09Qa {

    private Topic09Qa() {
    }

    /*
     * Q: What is the Stream API and why use it?
     *
     * SCRIPT:
     * Streams let me express data processing as a pipeline—filter, map, reduce—in a declarative way
     * instead of hand-written loops. Intermediate operations are lazy; nothing runs until a terminal
     * operation like collect or forEach triggers the pipeline. That can make code clearer and easier
     * to parallelize later, though I only use parallelStream when profiling shows it helps.
     */

    /*
     * Q: map vs flatMap?
     *
     * SCRIPT:
     * map transforms each element to exactly one output. flatMap transforms each element into a
     * stream of zero or more outputs and then flattens those into a single stream—classic example is
     * a list of lists where I want one stream of all inner elements without nested loops.
     */

    /*
     * Q: Predicate, Function, Consumer?
     *
     * SCRIPT:
     * They are functional interfaces used all over the JDK. Predicate tests a condition and returns
     * boolean—think filter. Function takes one input and returns another type—think map. Consumer
     * takes a value and returns void—think forEach side effects. Naming them in an interview shows I
     * understand the lambda shapes, not only syntax.
     */

    /*
     * Q: Optional?
     *
     * SCRIPT:
     * Optional is a box that may or may not contain a value. It encourages callers to handle absence
     * explicitly with orElse, orElseThrow, or ifPresent instead of null. I would not store Optional in
     * fields or use it for every return type, but for public API methods where absence is normal it
     * documents intent better than null alone.
     */

    /*
     * Q: Intermediate vs terminal operations?
     *
     * SCRIPT:
     * Intermediate operations like filter and map build a pipeline but do not execute until a
     * terminal operation runs. Terminal operations like collect, count, or forEach pull data through
     * the pipeline once. That laziness means I can short-circuit with findFirst without processing
     * the whole collection.
     */

    /*
     * Q: Parallel stream?
     *
     * SCRIPT:
     * parallelStream uses the common ForkJoinPool to split work across cores. It helps CPU-bound,
     * independent operations on large data, but it can hurt if the pipeline is tiny, if ordering
     * matters, or if there is shared mutable state. I would say I measure first, then enable
     * parallelism—not by default.
     */

    public static void demo() {
        List<String> words = List.of("a", "bb", "a");
        Predicate<String> p = s -> s.length() > 1;
        long n = words.stream().filter(p).count();
        Optional<String> first = Stream.of("x", "y").findFirst();
        System.out.println(n + " " + first.orElse("?"));
    }

    public static void main(String[] args) {
        demo();
    }
}

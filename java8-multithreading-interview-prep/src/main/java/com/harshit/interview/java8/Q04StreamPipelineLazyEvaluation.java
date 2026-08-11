package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.List;
import java.util.stream.Stream;

/**
 * INTERVIEW Q: Intermediate vs terminal operations? Is Stream lazy?
 *
 * <p><b>Answer:</b> Intermediate ops (filter, map, sorted) build a pipeline and are <b>lazy</b>.
 * Nothing executes until a <b>terminal</b> op (collect, forEach, count, reduce) runs.
 *
 * <p><b>Short-circuit terminals:</b> findFirst, anyMatch, allMatch — can stop early.
 *
 * <p><b>Trap:</b> Reusing same Stream after terminal op → IllegalStateException.
 */
public final class Q04StreamPipelineLazyEvaluation implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q04: Stream Pipeline & Laziness ===\n");

        List<String> fruits = List.of("Apple", "Orange", "Papaya", "Banana");

        System.out.println("Building pipeline (no work yet)...");

        Stream<String> pipeline = fruits.stream()
                .peek(s -> System.out.println("  peek before filter: " + s))
                .filter(s -> {
                    System.out.println("  filtering: " + s);
                    return s.length() > 5;
                })
                .map(s -> {
                    System.out.println("  mapping: " + s);
                    return s.toUpperCase();
                });

        System.out.println("Pipeline built. Calling findFirst() now...\n");

        pipeline.findFirst().ifPresent(result ->
                System.out.println("Short-circuit result: " + result));

        System.out.println("\n→ Only elements needed for findFirst were processed (lazy + short-circuit).");

        System.out.println("\nmap vs flatMap:");
        List<List<String>> nested = List.of(
                List.of("a", "b"),
                List.of("c", "d", "e"));

        List<String> flat = nested.stream()
                .flatMap(List::stream) // flatMap: 1 input → many outputs flattened
                .toList();
        System.out.println("flatMap result: " + flat);
    }

    public static void main(String[] args) throws Exception {
        new Q04StreamPipelineLazyEvaluation().run();
    }
}

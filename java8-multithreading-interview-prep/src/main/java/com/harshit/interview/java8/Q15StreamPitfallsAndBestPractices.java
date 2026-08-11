package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.List;
import java.util.stream.Stream;

/**
 * INTERVIEW Q: Stream pitfalls — reuse, side effects, infinite streams, when NOT to use streams.
 *
 * <p><b>Rules for 4 YOE:</b>
 * <ul>
 *   <li>Don't reuse a Stream after terminal operation</li>
 *   <li>Avoid mutating external state in forEach/map</li>
 *   <li>Simple indexed loops may be clearer for tiny lists</li>
 *   <li>IO in stream pipeline blocks parallel benefit</li>
 * </ul>
 */
public final class Q15StreamPitfallsAndBestPractices implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q15: Stream Pitfalls ===\n");

        List<String> items = List.of("A", "B", "C");
        Stream<String> stream = items.stream();

        long count = stream.count();
        System.out.println("Count: " + count);

        try {
            stream.forEach(System.out::println); // IllegalStateException — stream already closed
        } catch (IllegalStateException e) {
            System.out.println("Reuse error (expected): " + e.getClass().getSimpleName());
        }

        // Side effect anti-pattern
        StringBuilder bad = new StringBuilder();
        items.stream().forEach(bad::append); // works but prefer collect(joining) or reduce
        System.out.println("Side-effect concat: " + bad);

        String good = String.join("", items);
        System.out.println("Preferred join: " + good);

        // Infinite stream with limit — useful for sequences
        List<Integer> first10Squares = Stream.iterate(1, n -> n + 1)
                .limit(10)
                .map(n -> n * n)
                .toList();
        System.out.println("First 10 squares: " + first10Squares);

        // When loop is fine
        int sum = 0;
        for (int i : List.of(1, 2, 3)) {
            sum += i;
        }
        System.out.println("Simple sum via loop (OK for small data): " + sum);

        System.out.println("\n→ Accenture: 'Streams for readability and aggregation; loops for tight control and index access.'");
    }

    public static void main(String[] args) throws Exception {
        new Q15StreamPitfallsAndBestPractices().run();
    }
}

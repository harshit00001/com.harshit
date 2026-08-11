package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

/**
 * INTERVIEW Q: IntStream vs Stream&lt;Integer&gt;? When to use primitive streams?
 *
 * <p><b>Why primitive streams?</b> Avoid boxing overhead; specialized ops: sum(), average(), summaryStatistics().
 *
 * <p><b>Conversion:</b> boxed(), mapToInt(), mapToObj().
 */
public final class Q13PrimitiveStreams implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q13: Primitive Streams ===\n");

        int[] arr = {1, 2, 3, 4, 5};

        // IntStream from array — no boxing
        int sum = Arrays.stream(arr).sum();
        double avg = Arrays.stream(arr).average().orElse(0);
        IntSummaryStatistics stats = Arrays.stream(arr).summaryStatistics();
        System.out.println("Sum=" + sum + ", Avg=" + avg + ", stats=" + stats);

        // range — generate sequences without collection
        System.out.print("Evens 1-10: ");
        IntStream.rangeClosed(1, 10)
                .filter(n -> n % 2 == 0)
                .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Box when you need Collection<Integer>
        var boxed = Arrays.stream(arr).boxed().toList();
        System.out.println("Boxed list: " + boxed);

        // mapToObj — primitive → object stream
        Arrays.stream(arr)
                .mapToObj(n -> "num-" + n)
                .forEach(System.out::println);

        System.out.println("\n→ Interview trap: Stream<Integer>.reduce for sum boxes every element; use mapToInt().sum().");
    }

    public static void main(String[] args) throws Exception {
        new Q13PrimitiveStreams().run();
    }
}

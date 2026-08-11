package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * INTERVIEW Q: reduce vs collect? Explain findFirst vs findAny, match operations.
 *
 * <p><b>reduce</b> — combines elements to single value (sum, max, concat).
 *
 * <p><b>collect</b> — mutable reduction into Collection/Map via Collector.
 *
 * <p><b>findFirst</b> — first in encounter order (deterministic in sequential stream).
 *
 * <p><b>findAny</b> — any match; in parallel may return different element (faster).
 */
public final class Q11ReduceMatchAndTerminalOps implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q11: reduce, match, findFirst/findAny ===\n");

        List<Integer> nums = Arrays.asList(3, 7, 2, 9, 5);

        // reduce — sum
        int sum = nums.stream().reduce(0, Integer::sum);
        System.out.println("Sum (reduce): " + sum);

        // reduce — max without max()
        int max = nums.stream().reduce(Integer.MIN_VALUE, Integer::max);
        System.out.println("Max (reduce): " + max);

        // Optional reduce — empty-safe
        Optional<Integer> product = nums.stream().reduce((a, b) -> a * b);
        System.out.println("Product: " + product.orElse(0));

        // match operations — short-circuit
        System.out.println("anyMatch > 8: " + nums.stream().anyMatch(n -> n > 8));
        System.out.println("allMatch > 0: " + nums.stream().allMatch(n -> n > 0));
        System.out.println("noneMatch < 0: " + nums.stream().noneMatch(n -> n < 0));

        // findFirst vs findAny
        Optional<Integer> first = nums.stream().filter(n -> n > 4).findFirst();
        Optional<Integer> any = nums.stream().filter(n -> n > 4).findAny();
        System.out.println("findFirst > 4: " + first.orElse(-1));
        System.out.println("findAny > 4: " + any.orElse(-1));

        // String reduce — concatenation
        String joined = List.of("Java", "8", "Streams").stream()
                .reduce("", (a, b) -> a.isEmpty() ? b : a + "-" + b);
        System.out.println("Joined via reduce: " + joined);

        System.out.println("\n→ Accenture: prefer Collectors.summingInt over reduce for readability in team code.");
    }

    public static void main(String[] args) throws Exception {
        new Q11ReduceMatchAndTerminalOps().run();
    }
}

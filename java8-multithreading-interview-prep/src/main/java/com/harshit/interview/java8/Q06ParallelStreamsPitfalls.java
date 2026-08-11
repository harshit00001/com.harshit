package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * INTERVIEW Q: When to use parallelStream? What are the pitfalls?
 *
 * <p><b>Use when:</b> Large data, CPU-bound, independent operations, thread-safe pipeline.
 *
 * <p><b>Avoid when:</b> Small collections, IO-bound work, ordering matters, shared mutable state.
 *
 * <p><b>Deep dive:</b> parallelStream uses common {@link ForkJoinPool} (by default).
 * Custom thread pool requires careful setup (Java 8+ system property or custom spliterator — advanced).
 */
public final class Q06ParallelStreamsPitfalls implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q06: Parallel Streams ===\n");

        long sumSequential = IntStream.rangeClosed(1, 1_000_000)
                .mapToLong(i -> i)
                .sum();
        System.out.println("Sequential sum: " + sumSequential);

        long sumParallel = IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .mapToLong(i -> i)
                .sum();
        System.out.println("Parallel sum:   " + sumParallel);

        // PITFALL: mutating shared state from parallel stream
        List<Integer> unsafe = new ArrayList<>();
        try {
            IntStream.range(0, 1_000).parallel().forEach(unsafe::add);
            System.out.println("Unsafe list size (may be < 1000): " + unsafe.size());
        } catch (Exception e) {
            System.out.println("Unsafe approach may throw: " + e);
        }

        // FIX: collect with thread-safe reduction
        List<Integer> safe = IntStream.range(0, 1_000)
                .parallel()
                .boxed()
                .toList();
        System.out.println("Safe toList size: " + safe.size());

        System.out.println("\n→ Accenture answer: 'I profile first. parallelStream is not free — splitting/merging has cost.'");
    }

    public static void main(String[] args) throws Exception {
        new Q06ParallelStreamsPitfalls().run();
    }
}

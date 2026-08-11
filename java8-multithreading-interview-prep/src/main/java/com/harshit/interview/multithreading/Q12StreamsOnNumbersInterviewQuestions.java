package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * INTERVIEW Q: Streams with numbers — even/odd, sort, sum, second largest, partition (Accenture coding).
 *
 * <p>Bridges Java 8 streams + concurrency awareness (when parallel helps).
 */
public final class Q12StreamsOnNumbersInterviewQuestions implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q12: Streams on Numbers ===\n");

        List<Integer> nums = Arrays.asList(11, 62, 84, 55, 11, 20, 90, 83, 100);

        System.out.println("Evens:");
        nums.stream().filter(n -> n % 2 == 0).forEach(n -> System.out.println("  " + n));

        System.out.println("\nRange 10-20 evens:");
        IntStream.rangeClosed(10, 20)
                .filter(n -> n % 2 == 0)
                .forEach(n -> System.out.println("  " + n));

        System.out.println("\nDesc sort:");
        nums.stream().sorted((a, b) -> b - a).forEach(n -> System.out.println("  " + n));

        int sum = nums.stream().mapToInt(Integer::intValue).sum();
        double avg = nums.stream().mapToInt(Integer::intValue).average().orElse(0);
        System.out.println("\nSum=" + sum + ", Avg=" + avg);

        int max = nums.stream().max(Integer::compareTo).orElseThrow();
        int min = nums.stream().min(Integer::compareTo).orElseThrow();
        System.out.println("Max=" + max + ", Min=" + min);

        int secondLargest = nums.stream()
                .distinct()
                .sorted((a, b) -> b - a)
                .skip(1)
                .findFirst()
                .orElseThrow();
        System.out.println("Second largest: " + secondLargest);

        nums.stream()
                .filter(n -> n > 50)
                .findFirst()
                .ifPresent(n -> System.out.println("First > 50: " + n));

        var partition = nums.stream()
                .collect(java.util.stream.Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Evens partition: " + partition.get(true));

        // Async + streams — common in microservices (fetch in parallel)
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            List<CompletableFuture<Integer>> futures = nums.stream()
                    .limit(3)
                    .map(n -> CompletableFuture.supplyAsync(() -> n * n, pool))
                    .toList();
            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
            System.out.print("Squares async: ");
            futures.forEach(f -> System.out.print(f.join() + " "));
            System.out.println();
        } finally {
            pool.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        new Q12StreamsOnNumbersInterviewQuestions().run();
    }
}

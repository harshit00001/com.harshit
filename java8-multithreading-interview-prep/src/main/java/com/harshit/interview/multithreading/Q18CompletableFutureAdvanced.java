package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * INTERVIEW Q: CompletableFuture — thenCombine, allOf, applyToEither (microservices pattern).
 *
 * <p><b>Accenture scenario:</b> Fetch user + account in parallel, merge result; timeout on slow service.
 */
public final class Q18CompletableFutureAdvanced implements InterviewDemo {

    @Override
    public void run() throws Exception {
        System.out.println("=== Q18: CompletableFuture Advanced ===\n");

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(this::fetchUser, pool);
            CompletableFuture<Double> balanceFuture = CompletableFuture.supplyAsync(this::fetchBalance, pool);

            // Combine two independent async results
            CompletableFuture<String> combined = userFuture.thenCombine(balanceFuture,
                    (user, balance) -> user + " balance=" + balance);
            System.out.println("Combined: " + combined.get());

            // allOf — wait for all
            CompletableFuture<Void> all = CompletableFuture.allOf(userFuture, balanceFuture);
            all.join();
            System.out.println("allOf completed");

            // applyToEither — first successful result wins
            CompletableFuture<String> fastest = CompletableFuture
                    .supplyAsync(this::slowService, pool)
                    .applyToEither(CompletableFuture.supplyAsync(this::fastService, pool), s -> s);
            System.out.println("Fastest service: " + fastest.get());

            // handle — both result and exception
            CompletableFuture<String> handled = CompletableFuture
                    .<String>supplyAsync(() -> {
                        throw new RuntimeException("payment down");
                    }, pool)
                    .handle((result, ex) -> ex != null ? "fallback-response" : result);
            System.out.println("Handled failure: " + handled.get());

        } finally {
            pool.shutdown();
        }

        System.out.println("\n→ In Spring @Async / WebClient, same async composition ideas apply.");
    }

    private String fetchUser() {
        sleep(100);
        return "User-Raj";
    }

    private double fetchBalance() {
        sleep(120);
        return 15_000.50;
    }

    private String slowService() {
        sleep(300);
        return "slow";
    }

    private String fastService() {
        sleep(50);
        return "fast";
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws Exception {
        new Q18CompletableFutureAdvanced().run();
    }
}

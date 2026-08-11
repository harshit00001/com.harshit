package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * INTERVIEW Q: Runnable vs Callable vs Future vs CompletableFuture?
 *
 * <p><b>Runnable:</b> no result, no checked exceptions from run().
 *
 * <p><b>Callable:</b> returns result, throws checked exceptions → Future.get().
 *
 * <p><b>CompletableFuture (Java 8):</b> async pipeline — thenApply, thenCompose, allOf, exceptionally.
 *
 * <p><b>4 YOE:</b> Mention not blocking on get() in reactive paths; handle timeouts and exceptions.
 */
public final class Q06CallableFutureCompletableFuture implements InterviewDemo {

    @Override
    public void run() throws ExecutionException, InterruptedException, TimeoutException {
        System.out.println("=== Q06: Callable, Future, CompletableFuture ===\n");

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Callable<Integer> callable = () -> {
                Thread.sleep(200);
                return 42;
            };
            Future<Integer> future = pool.submit(callable);
            System.out.println("Future result: " + future.get());

            CompletableFuture<String> cf = CompletableFuture
                    .supplyAsync(() -> "Accenture", pool)
                    .thenApply(s -> s + "-India")
                    .thenApply(String::toUpperCase);

            System.out.println("CompletableFuture chain: " + cf.get(2, TimeUnit.SECONDS));

            CompletableFuture<Void> combined = CompletableFuture
                    .supplyAsync(() -> fetchUser(), pool)
                    .thenCompose(user -> CompletableFuture.supplyAsync(() -> fetchProfile(user), pool))
                    .thenAccept(profile -> System.out.println("Profile loaded: " + profile));

            combined.join();

            CompletableFuture<Integer> fail = CompletableFuture
                    .<Integer>supplyAsync(() -> {
                        throw new IllegalStateException("downstream error");
                    }, pool)
                    .exceptionally(ex -> {
                        System.out.println("Recovered from: " + ex.getMessage());
                        return -1;
                    });
            System.out.println("After exceptionally: " + fail.get());

        } finally {
            pool.shutdown();
        }
    }

    private static String fetchUser() {
        return "user-101";
    }

    private static String fetchProfile(String user) {
        return "profile-for-" + user;
    }

    public static void main(String[] args) throws Exception {
        new Q06CallableFutureCompletableFuture().run();
    }
}

package com.harshit.preparation.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <h2>Runnable vs Callable</h2>
 * <p>
 * {@link Runnable#run} returns void and cannot throw checked exceptions.
 * {@link Callable#call} returns a value and may throw checked exceptions — used with
 * {@link java.util.concurrent.ExecutorService#submit(Callable)}.
 */
public final class CallableRunnableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Callable<Integer> multiply = () -> 21 * 2;
            Future<Integer> future = pool.submit(multiply);
            System.out.println("callable result: " + future.get());

            pool.submit((Runnable) () -> System.out.println("runnable done"));
        } finally {
            pool.shutdown();
        }
    }

    private CallableRunnableDemo() {
    }
}

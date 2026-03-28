package com.harshit.preparation.topic08;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Topic 08 — Concurrency: synchronized, locks, Callable vs Runnable.
 */
public final class Topic08Qa {

    private Topic08Qa() {
    }

    /*
     * Q: What happens when we use synchronized?
     *
     * SCRIPT:
     * synchronized acquires the monitor lock on an object before entering the block or method, and
     * releases it on exit. Only one thread at a time can hold that lock on the same object for that
     * kind of critical section, which gives mutual exclusion. It also creates a happens-before
     * relationship so that memory writes inside the block are visible to the next thread that acquires
     * the same lock—important for correctness, not just “avoiding two writers at once.”
     */

    /*
     * Q: synchronized vs ReentrantLock?
     *
     * SCRIPT:
     * synchronized is built into the JVM and is enough for most simple guards. ReentrantLock gives
     * me tryLock with timeouts, fair ordering if I need it, and lockInterruptibly for responsive
     * cancellation. I pick ReentrantLock when I need that extra control; otherwise I keep code
     * simpler with synchronized.
     */

    /*
     * Q: How do threads communicate?
     *
     * SCRIPT:
     * In Java, shared memory is the main model: threads communicate by reading and writing fields
     * on objects they can both see, with synchronization to coordinate. Higher-level tools include
     * wait and notify on monitors, BlockingQueues for producer-consumer, and CompletableFuture for
     * async pipelines—I prefer those over low-level wait/notify in new code because they are easier
     * to get right.
     */

    /*
     * Q: Runnable vs Callable?
     *
     * SCRIPT:
     * Runnable represents a task that returns nothing and cannot throw checked exceptions from run.
     * Callable is for tasks that return a result and may throw checked exceptions; ExecutorService’s
     * submit(Callable) gives a Future so I can get the result or handle errors. I use Callable when
     * the worker thread must produce a value back to the caller.
     */

    public static void callableDemo() throws ExecutionException, InterruptedException {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        try {
            Callable<Integer> task = () -> 2 + 3;
            Future<Integer> f = ex.submit(task);
            System.out.println(f.get());
        } finally {
            ex.shutdown();
        }
    }

    public static void main(String[] args) {
        try {
            callableDemo();
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}

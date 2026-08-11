package com.learning.threads.extra;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Multithreading & concurrency interview Q&A — under java-multithreading-learning.
 *
 * <p><b>Q. What is a thread?</b> Lightweight unit of execution inside a process with its own stack;
 * shares heap with peers. Real life: web server handles each request on a thread (or virtual thread).
 *
 * <p><b>Q. Process vs thread?</b> Process has isolated memory; threads share address space of their process.
 * Real life: Chrome tabs used to be processes for isolation; threads within a JVM share heap.
 *
 * <p><b>Q. Synchronization?</b> Mutual exclusion around critical sections (synchronized, locks).
 * Real life: two cashiers updating the same account balance must not interleave blindly.
 *
 * <p><b>Q. Deadlock?</b> Circular wait for locks. Avoid consistent lock ordering, timeouts, tryLock.
 * Real life: service A locks row1 then row2 while B locks row2 then row1 — both stuck.
 *
 * <p><b>Q. Thread lifecycle?</b> NEW → RUNNABLE → (BLOCKED/WAITING/TIMED_WAITING) → TERMINATED.
 *
 * <p><b>Q. ExecutorService?</b> Thread pool abstraction; reuse threads instead of spawning unbounded threads.
 * Real life: batch API fan-out with a fixed pool.
 *
 * <p><b>Q. Callable vs Runnable?</b> Runnable void run(); Callable<V> returns V and throws checked exceptions,
 * usable with Future. Real life: download task that must return bytes → Callable.
 *
 * <p><b>Q. volatile?</b> Visibility guarantee across threads; not atomicity for compound ops.
 * Real life: shutdown flag checked by many threads.
 *
 * <p><b>Q. Thread safety?</b> Correct behavior under concurrent use — immutability, confinement,
 * synchronization, concurrent collections, atomics.
 */
public final class MultithreadingInterviewQA {

    private MultithreadingInterviewQA() {
    }

    private static final AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Runnable task = () -> System.out.println("Runnable on " + Thread.currentThread().getName());
        Thread t = new Thread(task);
        t.start();
        t.join();

        ExecutorService pool = Executors.newFixedThreadPool(2);
        Callable<Integer> callable = () -> {
            counter.incrementAndGet();
            return counter.get();
        };
        Future<Integer> f = pool.submit(callable);
        System.out.println("Callable result: " + f.get());
        pool.shutdown();
    }
}

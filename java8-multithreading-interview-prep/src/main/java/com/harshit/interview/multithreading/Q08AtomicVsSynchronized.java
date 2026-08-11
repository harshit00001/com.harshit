package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * INTERVIEW Q: synchronized vs AtomicInteger vs LongAdder?
 *
 * <p><b>Atomic*</b> uses CAS (Compare-And-Swap) — lock-free for simple operations.
 *
 * <p><b>LongAdder</b> (Java 8) — stripes counters under contention; better throughput than AtomicLong for many writers.
 */
public final class Q08AtomicVsSynchronized implements InterviewDemo {

    private int syncCounter = 0;
    private final Object lock = new Object();
    private final AtomicInteger atomicCounter = new AtomicInteger();
    private final LongAdder longAdder = new LongAdder();

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q08: Atomic vs synchronized ===\n");

        Runnable syncTask = () -> {
            for (int i = 0; i < 10_000; i++) {
                synchronized (lock) {
                    syncCounter++;
                }
            }
        };

        Runnable atomicTask = () -> {
            for (int i = 0; i < 10_000; i++) {
                atomicCounter.incrementAndGet();
            }
        };

        Runnable adderTask = () -> {
            for (int i = 0; i < 10_000; i++) {
                longAdder.increment();
            }
        };

        runTwoThreads(syncTask);
        System.out.println("synchronized: " + syncCounter);

        runTwoThreads(atomicTask);
        System.out.println("AtomicInteger: " + atomicCounter.get());

        runTwoThreads(adderTask);
        System.out.println("LongAdder sum: " + longAdder.sum());

        System.out.println("\n→ Use atomics for counters/flags; synchronized/Lock for multi-step invariants.");
    }

    private void runTwoThreads(Runnable task) throws InterruptedException {
        Thread a = new Thread(task);
        Thread b = new Thread(task);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    public static void main(String[] args) throws Exception {
        new Q08AtomicVsSynchronized().run();
    }
}

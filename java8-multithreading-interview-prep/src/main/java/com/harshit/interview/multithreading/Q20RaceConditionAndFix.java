package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * INTERVIEW Q: What is a race condition? Show broken code and fix.
 *
 * <p><b>Accenture:</b> They often ask "two threads increment counter — expected 20000, get less?"
 */
public final class Q20RaceConditionAndFix implements InterviewDemo {

    private int unsafeCounter = 0;
    private final AtomicInteger safeCounter = new AtomicInteger();

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q20: Race Condition & Fix ===\n");

        Runnable unsafeTask = () -> {
            for (int i = 0; i < 10_000; i++) {
                unsafeCounter++; // read-modify-write — NOT atomic
            }
        };

        Thread u1 = new Thread(unsafeTask);
        Thread u2 = new Thread(unsafeTask);
        u1.start();
        u2.start();
        u1.join();
        u2.join();
        System.out.println("Unsafe counter (expect 20000): " + unsafeCounter);

        Runnable safeTask = () -> {
            for (int i = 0; i < 10_000; i++) {
                safeCounter.incrementAndGet();
            }
        };

        Thread s1 = new Thread(safeTask);
        Thread s2 = new Thread(safeTask);
        s1.start();
        s2.start();
        s1.join();
        s2.join();
        System.out.println("AtomicInteger counter (expect 20000): " + safeCounter.get());

        System.out.println("\n→ Fixes: synchronized block, Lock, Atomic*, or immutable data structures.");
    }

    public static void main(String[] args) throws Exception {
        new Q20RaceConditionAndFix().run();
    }
}

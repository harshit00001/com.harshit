package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

/**
 * INTERVIEW Q: Explain happens-before rules in Java (JMM basics for 4 YOE).
 *
 * <p><b>Key rules (memorize for Accenture):</b>
 * <ol>
 *   <li>Unlock on monitor happens-before subsequent lock on same monitor</li>
 *   <li>volatile write happens-before volatile read of same variable</li>
 *   <li>Thread.start happens-before any action in started thread</li>
 *   <li>Actions in thread happen-before Thread.join returns</li>
 *   <li>Transitivity — chain of happens-before guarantees visibility</li>
 * </ol>
 */
public final class Q19HappensBeforeRules implements InterviewDemo {

    private static int shared;
    private static volatile boolean flag;

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q19: Happens-Before Rules ===\n");

        // Rule: thread start
        Thread t = new Thread(() -> {
            shared = 42; // visible to main after join due to happens-before
        });
        t.start();
        t.join(); // join establishes happens-before
        System.out.println("After join, shared=" + shared);

        // Rule: volatile
        Thread writer = new Thread(() -> {
            shared = 100;
            flag = true; // volatile write
        });
        Thread reader = new Thread(() -> {
            while (!flag) {
                // spin
            }
            System.out.println("Reader sees shared=" + shared + " after volatile flag=true");
        });
        reader.start();
        writer.start();
        writer.join();
        reader.join();

        System.out.println("\n→ Without proper synchronization, reader might see flag=true but shared=0 (reordering).");
    }

    public static void main(String[] args) throws Exception {
        new Q19HappensBeforeRules().run();
    }
}

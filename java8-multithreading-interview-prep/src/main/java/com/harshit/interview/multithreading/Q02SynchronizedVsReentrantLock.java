package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * INTERVIEW Q: synchronized vs ReentrantLock?
 *
 * <p><b>synchronized:</b> JVM intrinsic lock, automatic release, simple, no try/fail modes.
 *
 * <p><b>ReentrantLock:</b> tryLock(timeout), lockInterruptibly(), optional fairness, explicit unlock in finally.
 *
 * <p><b>Both</b> provide mutual exclusion + memory visibility (happens-before).
 */
public final class Q02SynchronizedVsReentrantLock implements InterviewDemo {

    private int synchronizedCounter = 0;
    private int lockCounter = 0;
    private final Object monitor = new Object();
    private final Lock reentrantLock = new ReentrantLock(true); // fair lock — slower but ordered

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q02: synchronized vs ReentrantLock ===\n");

        Runnable syncTask = () -> {
            for (int i = 0; i < 1_000; i++) {
                synchronized (monitor) {
                    synchronizedCounter++;
                }
            }
        };

        Runnable lockTask = () -> {
            for (int i = 0; i < 1_000; i++) {
                reentrantLock.lock();
                try {
                    lockCounter++;
                } finally {
                    reentrantLock.unlock(); // MUST unlock in finally
                }
            }
        };

        Thread t1 = new Thread(syncTask);
        Thread t2 = new Thread(syncTask);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("synchronized counter (expect 2000): " + synchronizedCounter);

        Thread t3 = new Thread(lockTask);
        Thread t4 = new Thread(lockTask);
        t3.start();
        t4.start();
        t3.join();
        t4.join();
        System.out.println("ReentrantLock counter (expect 2000): " + lockCounter);

        // tryLock — useful to avoid indefinite blocking (e.g. deadlock avoidance)
        if (reentrantLock.tryLock()) {
            try {
                System.out.println("tryLock acquired");
            } finally {
                reentrantLock.unlock();
            }
        }

        System.out.println("\n→ Use synchronized by default; ReentrantLock when you need tryLock / interruptible / fair queue.");
    }

    public static void main(String[] args) throws Exception {
        new Q02SynchronizedVsReentrantLock().run();
    }
}

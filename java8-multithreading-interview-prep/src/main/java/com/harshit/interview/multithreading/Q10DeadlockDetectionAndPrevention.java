package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

/**
 * INTERVIEW Q: What is deadlock? How to detect and prevent?
 *
 * <p><b>Deadlock:</b> Thread A holds L1 waits L2; Thread B holds L2 waits L1 — circular wait.
 *
 * <p><b>Prevention:</b>
 * <ul>
 *   <li>Lock ordering — always acquire locks in same global order</li>
 *   <li>tryLock with timeout</li>
 *   <li>Reduce lock scope / avoid nested locks</li>
 *   <li>Use higher-level APIs (ConcurrentHashMap, j.u.c.)</li>
 * </ul>
 */
public final class Q10DeadlockDetectionAndPrevention implements InterviewDemo {

    private final Object lockA = new Object();
    private final Object lockB = new Object();

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q10: Deadlock ===\n");

        // SAFE: consistent lock order (always A then B)
        Thread safe1 = new Thread(() -> transferSafe(lockA, lockB), "safe-1");
        Thread safe2 = new Thread(() -> transferSafe(lockB, lockA), "safe-2");
        safe1.start();
        safe2.start();
        safe1.join();
        safe2.join();
        System.out.println("Safe transfers completed (ordered locks).\n");

        System.out.println("Unsafe pattern (commented) — opposite lock order causes deadlock:");
        System.out.println("  Thread1: synchronized(A){ synchronized(B){...}}");
        System.out.println("  Thread2: synchronized(B){ synchronized(A){...}}");
        System.out.println("\n→ jstack / VisualVM thread dump shows 'Found one Java-level deadlock'.");
    }

    private void transferSafe(Object first, Object second) {
        Object lock1 = first;
        Object lock2 = second;
        // Normalize order by identity hash — both threads lock smaller id first
        if (System.identityHashCode(lock1) > System.identityHashCode(lock2)) {
            Object tmp = lock1;
            lock1 = lock2;
            lock2 = tmp;
        }
        synchronized (lock1) {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " transferred with ordered locks");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Q10DeadlockDetectionAndPrevention().run();
    }
}

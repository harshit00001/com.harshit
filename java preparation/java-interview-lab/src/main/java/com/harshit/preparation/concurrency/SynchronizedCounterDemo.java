package com.harshit.preparation.concurrency;

/**
 * <h2>synchronized keyword — what happens?</h2>
 * <p>
 * Each object has an <i>intrinsic lock</i> (monitor). {@code synchronized (lock)} acquires that
 * lock before entering the block; releases at exit. Only one thread at a time executes the block
 * on the same lock — <b>mutual exclusion</b>.
 * <p>
 * Also establishes <b>happens-before</b>: actions in one thread before unlock are visible to the
 * next thread that locks after.
 * <p>
 * {@link java.util.concurrent.locks.ReentrantLock} offers tryLock, timeouts, interruptible waits —
 * use when you need those; otherwise {@code synchronized} is simpler.
 */
public final class SynchronizedCounterDemo {

    private int count;

    public synchronized void increment() {
        count++;
    }

    public synchronized int get() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedCounterDemo c = new SynchronizedCounterDemo();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                c.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                c.increment();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("count (expect 20000): " + c.get());
    }

    private SynchronizedCounterDemo() {
    }
}

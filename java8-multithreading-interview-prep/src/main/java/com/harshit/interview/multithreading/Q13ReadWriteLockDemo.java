package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * INTERVIEW Q: ReadWriteLock vs synchronized? When is it better?
 *
 * <p><b>ReadWriteLock:</b> Multiple concurrent readers OR one writer.
 * Good for read-heavy caches (config, reference data).
 *
 * <p><b>Trap:</b> Write lock blocks all readers; still need correct lock pairing (lock/unlock in finally).
 */
public final class Q13ReadWriteLockDemo implements InterviewDemo {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private String cache = "initial-config";

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q13: ReadWriteLock ===\n");

        Runnable reader = () -> {
            rwLock.readLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + " READ  -> " + cache);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.readLock().unlock();
            }
        };

        Thread r1 = new Thread(reader, "reader-1");
        Thread r2 = new Thread(reader, "reader-2");
        r1.start();
        r2.start();
        r1.join();
        r2.join();

        rwLock.writeLock().lock();
        try {
            cache = "updated-config-v2";
            System.out.println("WRITER updated cache");
        } finally {
            rwLock.writeLock().unlock();
        }

        Thread r3 = new Thread(reader, "reader-3");
        r3.start();
        r3.join();

        System.out.println("\n→ Use when reads >> writes; otherwise synchronized may be simpler.");
    }

    public static void main(String[] args) throws Exception {
        new Q13ReadWriteLockDemo().run();
    }
}

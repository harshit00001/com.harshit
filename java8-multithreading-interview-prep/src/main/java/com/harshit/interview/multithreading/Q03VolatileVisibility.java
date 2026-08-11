package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

/**
 * INTERVIEW Q: What does volatile do? Is it enough for counter++?
 *
 * <p><b>volatile guarantees:</b>
 * <ul>
 *   <li>Visibility — write visible to other threads immediately (happens-before)</li>
 *   <li>No reordering of read/write with other volatile ops</li>
 * </ul>
 *
 * <p><b>Does NOT guarantee atomicity</b> for read-modify-write like {@code count++}.
 * Use synchronized, Lock, or AtomicInteger for compound updates.
 */
public final class Q03VolatileVisibility implements InterviewDemo {

    // Without volatile, reader thread might never see update (CPU cache)
    private volatile boolean shutdownRequested = false;
    private volatile int volatileCounter = 0;
    private int plainCounter = 0;

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q03: volatile Visibility ===\n");

        Thread reader = new Thread(() -> {
            while (!shutdownRequested) {
                // busy spin — in production use wait/notify or BlockingQueue
            }
            System.out.println("Reader observed shutdown flag = true");
        }, "reader");

        reader.start();
        Thread.sleep(100);
        shutdownRequested = true; // visible to reader because volatile
        reader.join();

        // Demonstrate volatile is NOT atomic for ++
        Thread t1 = new Thread(() -> incrementVolatile(10_000));
        Thread t2 = new Thread(() -> incrementVolatile(10_000));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("volatileCounter (expect 20000, often less): " + volatileCounter);

        Thread t3 = new Thread(() -> incrementPlain(10_000));
        Thread t4 = new Thread(() -> incrementPlain(10_000));
        t3.start();
        t4.start();
        t3.join();
        t4.join();
        System.out.println("plainCounter (expect 20000, often less): " + plainCounter);

        System.out.println("\n→ Interview line: 'volatile is for flags/status; atomics/locks for compound state.'");
    }

    private void incrementVolatile(int times) {
        for (int i = 0; i < times; i++) {
            volatileCounter++; // NOT atomic — lost updates possible
        }
    }

    private void incrementPlain(int times) {
        for (int i = 0; i < times; i++) {
            plainCounter++;
        }
    }

    public static void main(String[] args) throws Exception {
        new Q03VolatileVisibility().run();
    }
}

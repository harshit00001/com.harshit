package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.TimeUnit;

/**
 * INTERVIEW Q: How do you handle thread interruption correctly?
 *
 * <p><b>Rules:</b>
 * <ul>
 *   <li>Don't swallow InterruptedException without restoring interrupt flag</li>
 *   <li>Prefer exit or throw if interrupt means cancel</li>
 *   <li>Thread.sleep / wait / BlockingQueue.take respond to interrupt</li>
 * </ul>
 */
public final class Q16InterruptHandling implements InterviewDemo {

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q16: Interrupt Handling ===\n");

        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("Worker doing work...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // CORRECT: restore interrupt and exit
                    System.out.println("Worker caught interrupt — shutting down");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Worker stopped cleanly");
        }, "worker");

        worker.start();
        Thread.sleep(1200);
        worker.interrupt();
        worker.join();

        // BAD pattern (shown in comment only):
        // catch (InterruptedException e) { /* empty */ }  // hides cancel request

        System.out.println("\n→ ExecutorService.shutdownNow() sends interrupts to running tasks.");
    }

    public static void main(String[] args) throws Exception {
        new Q16InterruptHandling().run();
    }
}

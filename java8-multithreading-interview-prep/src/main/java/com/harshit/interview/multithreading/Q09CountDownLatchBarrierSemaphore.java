package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * INTERVIEW Q: CountDownLatch vs CyclicBarrier vs Semaphore?
 *
 * <ul>
 *   <li><b>CountDownLatch</b> — one-shot; threads wait until count reaches 0 (e.g. wait for services to start)</li>
 *   <li><b>CyclicBarrier</b> — reusable; N threads wait at barrier then proceed together (e.g. parallel phases)</li>
 *   <li><b>Semaphore</b> — limits concurrent access (permits); rate limiting / resource pool</li>
 * </ul>
 */
public final class Q09CountDownLatchBarrierSemaphore implements InterviewDemo {

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q09: Latch, Barrier, Semaphore ===\n");

        countDownLatchDemo();
        cyclicBarrierDemo();
        semaphoreDemo();
    }

    private void countDownLatchDemo() throws InterruptedException {
        System.out.println("--- CountDownLatch ---");
        CountDownLatch ready = new CountDownLatch(3);

        for (int i = 1; i <= 3; i++) {
            int id = i;
            new Thread(() -> {
                System.out.println("Service-" + id + " starting...");
                try {
                    Thread.sleep(100L * id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                ready.countDown();
                System.out.println("Service-" + id + " ready (count=" + ready.getCount() + ")");
            }).start();
        }

        ready.await();
        System.out.println("All services up — main proceeds.\n");
    }

    private void cyclicBarrierDemo() throws InterruptedException {
        System.out.println("--- CyclicBarrier ---");
        CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("Barrier action: phase complete"));

        java.util.function.IntFunction<Runnable> worker = id -> () -> {
            try {
                System.out.println("Worker-" + id + " phase-1 done, waiting...");
                barrier.await();
                System.out.println("Worker-" + id + " phase-2 after barrier");
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 1; i <= 3; i++) {
            new Thread(worker.apply(i)).start();
        }
        Thread.sleep(500);
        System.out.println();
    }

    private void semaphoreDemo() throws InterruptedException {
        System.out.println("--- Semaphore (max 2 concurrent) ---");
        Semaphore permits = new Semaphore(2);

        java.util.function.IntFunction<Runnable> job = id -> () -> {
            try {
                permits.acquire();
                System.out.println("Job-" + id + " running on " + Thread.currentThread().getName());
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                permits.release();
                System.out.println("Job-" + id + " released permit");
            }
        };

        for (int i = 1; i <= 4; i++) {
            new Thread(job.apply(i)).start();
        }
        Thread.sleep(1500);
        System.out.println("\n→ Semaphore limits concurrency; different from rate-per-minute limiting.");
    }

    public static void main(String[] args) throws Exception {
        new Q09CountDownLatchBarrierSemaphore().run();
    }
}

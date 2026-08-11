package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * INTERVIEW Q: How do threads communicate? wait/notify vs BlockingQueue?
 *
 * <p><b>Low-level:</b> wait() releases lock and waits; notify()/notifyAll() wakes waiters.
 * Always use while loop (not if) to re-check condition (spurious wakeup).
 *
 * <p><b>Production:</b> Prefer {@link BlockingQueue} — handles waiting/signaling internally.
 */
public final class Q04WaitNotifyAndBlockingQueue implements InterviewDemo {

    private final Object lock = new Object();
    private String message;

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q04: wait/notify & BlockingQueue ===\n");

        waitNotifyDemo();
        blockingQueueDemo();
    }

    private void waitNotifyDemo() throws InterruptedException {
        System.out.println("--- wait/notify producer-consumer ---");

        Thread consumer = new Thread(() -> {
            synchronized (lock) {
                while (message == null) {
                    try {
                        System.out.println("Consumer waiting...");
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("Consumer received: " + message);
            }
        }, "consumer");

        consumer.start();
        Thread.sleep(200);

        synchronized (lock) {
            message = "Payroll processed";
            System.out.println("Producer set message, calling notify()");
            lock.notify();
        }
        consumer.join();
        System.out.println();
    }

    private void blockingQueueDemo() throws InterruptedException {
        System.out.println("--- BlockingQueue (preferred) ---");

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

        Thread producer = new Thread(() -> {
            try {
                queue.put("Task-1");
                queue.put("Task-2");
                System.out.println("Producer enqueued 2 tasks");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "bq-producer");

        Thread consumer = new Thread(() -> {
            try {
                String first = queue.poll(2, TimeUnit.SECONDS);
                String second = queue.poll(2, TimeUnit.SECONDS);
                System.out.println("Consumer took: " + first + ", " + second);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "bq-consumer");

        consumer.start();
        producer.start();
        producer.join();
        consumer.join();

        System.out.println("\n→ Accenture: 'I avoid wait/notify in new code; BlockingQueue or CompletableFuture.'");
    }

    public static void main(String[] args) throws Exception {
        new Q04WaitNotifyAndBlockingQueue().run();
    }
}

package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * INTERVIEW Q: Implement producer-consumer using BlockingQueue (Accenture coding).
 *
 * <p><b>Why BlockingQueue?</b> put/take block automatically — no manual wait/notify bugs.
 */
public final class Q17ProducerConsumerBlockingQueue implements InterviewDemo {

    private static final BlockingQueue<String> QUEUE = new ArrayBlockingQueue<>(5);
    private static volatile boolean running = true;

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q17: Producer-Consumer (BlockingQueue) ===\n");

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    String item = "Order-" + i;
                    QUEUE.put(item);
                    System.out.println("Produced: " + item + " (queue size=" + QUEUE.size() + ")");
                    Thread.sleep(80);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                running = false;
            }
        }, "producer");

        Thread consumer = new Thread(() -> {
            try {
                while (running || !QUEUE.isEmpty()) {
                    String item = QUEUE.poll(200, TimeUnit.MILLISECONDS);
                    if (item != null) {
                        System.out.println("  Consumed: " + item);
                        Thread.sleep(150);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("\n→ Bounded queue gives natural backpressure when producers outpace consumers.");
    }

    public static void main(String[] args) throws Exception {
        new Q17ProducerConsumerBlockingQueue().run();
    }
}

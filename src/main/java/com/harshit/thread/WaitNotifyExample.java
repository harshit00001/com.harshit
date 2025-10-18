package com.harshit.thread;

public class WaitNotifyExample {

    private static final Object lock = new Object();
    private static boolean conditionMet = false;

    public static void main(String[] args) throws InterruptedException {
        // Start 3 waiting threads
        for (int i = 1; i <= 3; i++) {
            Thread t = new Thread(new WaitingWorker(), "Worker-" + i);
            t.start();
        }

        // Give time for all workers to start and wait
        Thread.sleep(1000);

        // Notifier thread using notify()
        Thread notifier1 = new Thread(() -> {
            synchronized (lock) {
                conditionMet = true;
                System.out.println("Notifier-1: calling notify()");
                lock.notify(); // wakes up one waiting thread
            }
        }, "Notifier-1");
        notifier1.start();

        Thread.sleep(2000);

        // Notifier thread using notifyAll()
        Thread notifier2 = new Thread(() -> {
            synchronized (lock) {
                conditionMet = true;
                System.out.println("Notifier-2: calling notifyAll()");
                lock.notifyAll(); // wakes up all waiting threads
            }
        }, "Notifier-2");
        notifier2.start();
    }

    static class WaitingWorker implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                while (!conditionMet) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is waiting...");
                        lock.wait(); // releases lock and waits
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " is proceeding!");
            }
        }
    }
}
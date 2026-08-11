package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

/**
 * INTERVIEW Q: Explain thread lifecycle and ways to create threads.
 *
 * <p><b>States:</b> NEW → RUNNABLE → (BLOCKED/WAITING/TIMED_WAITING) → TERMINATED
 *
 * <p><b>Creation (prefer pool over raw Thread in production):</b>
 * <ol>
 *   <li>extends Thread — avoid (breaks inheritance)</li>
 *   <li>implements Runnable — preferred for task separation</li>
 *   <li>ExecutorService — production standard</li>
 * </ol>
 *
 * <p><b>4 YOE:</b> Mention thread != lightweight process; context switch cost; why pools reuse threads.
 */
public final class Q01ThreadBasicsAndLifecycle implements InterviewDemo {

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q01: Thread Basics & Lifecycle ===\n");
        System.out.println("Main thread: " + Thread.currentThread().getName());

        Thread worker = new Thread(() -> {
            System.out.println("Worker running on: " + Thread.currentThread().getName());
            System.out.println("Worker state while running: " + Thread.currentThread().getState());
        }, "accenture-worker");

        System.out.println("Before start: " + worker.getState()); // NEW
        worker.start(); // moves to RUNNABLE — do NOT call run() directly
        worker.join(); // main waits for worker to finish
        System.out.println("After join: " + worker.getState()); // TERMINATED

        System.out.println("\n→ start() schedules OS thread; run() is ordinary method call on current thread.");
    }

    public static void main(String[] args) throws Exception {
        new Q01ThreadBasicsAndLifecycle().run();
    }
}

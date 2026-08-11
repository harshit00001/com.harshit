package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * INTERVIEW Q: Explain ExecutorService and common thread pool types.
 *
 * <p><b>Why pools?</b> Creating threads is expensive; pools reuse worker threads.
 *
 * <p><b>Types:</b>
 * <ul>
 *   <li>FixedThreadPool — fixed size, unbounded queue</li>
 *   <li>CachedThreadPool — grows/shrinks, good for many short tasks (careful in prod)</li>
 *   <li>SingleThreadExecutor — sequential execution, ordered tasks</li>
 *   <li>ScheduledThreadPool — delayed / periodic</li>
 *   <li>ThreadPoolExecutor — full control (core, max, queue, rejection policy)</li>
 * </ul>
 *
 * <p><b>Always shutdown:</b> shutdown() + awaitTermination or shutdownNow in finally.
 */
public final class Q05ExecutorServiceAndThreadPools implements InterviewDemo {

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q05: ExecutorService & Thread Pools ===\n");

        demoFixedPool();
        demoCustomPoolWithRejection();
    }

    private void demoFixedPool() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            for (int i = 1; i <= 4; i++) {
                int taskId = i;
                pool.submit(() -> {
                    System.out.println("Task-" + taskId + " on " + Thread.currentThread().getName());
                    return taskId;
                });
            }
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        }
        System.out.println("Fixed pool finished.\n");
    }

    private void demoCustomPoolWithRejection() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,                      // core pool size
                2,                      // max pool size
                30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2), // small queue → triggers rejection when full
                new ThreadPoolExecutor.CallerRunsPolicy() // runs task on caller thread
        );

        try {
            for (int i = 1; i <= 5; i++) {
                int id = i;
                executor.execute(() ->
                        System.out.println("Custom pool task-" + id + " thread=" + Thread.currentThread().getName()));
            }
        } finally {
            executor.shutdown();
        }

        System.out.println("\n→ Rejection policies: AbortPolicy (default), CallerRuns, Discard, DiscardOldest.");
    }

    public static void main(String[] args) throws Exception {
        new Q05ExecutorServiceAndThreadPools().run();
    }
}

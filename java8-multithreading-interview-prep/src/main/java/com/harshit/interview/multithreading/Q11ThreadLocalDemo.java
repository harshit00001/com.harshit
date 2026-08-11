package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

/**
 * INTERVIEW Q: What is ThreadLocal? Use cases and memory leak risk?
 *
 * <p><b>Use case:</b> Per-thread context — user session, SimpleDateFormat (legacy), requestId in logs.
 *
 * <p><b>With thread pools:</b> Worker thread is reused → must {@link ThreadLocal#remove()} after use
 * or stale data leaks to next task (and can prevent GC in old app servers).
 *
 * <p><b>Modern alternative:</b> Pass context explicitly or use MDC (SLF4J) with clear in finally.
 */
public final class Q11ThreadLocalDemo implements InterviewDemo {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q11: ThreadLocal ===\n");

        Runnable task = () -> {
            REQUEST_ID.set("REQ-" + Thread.currentThread().getName());
            System.out.println("Handling " + REQUEST_ID.get());
            // simulate work
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                REQUEST_ID.remove(); // critical in pooled threads
            }
        };

        Thread t1 = new Thread(task, "worker-1");
        Thread t2 = new Thread(task, "worker-2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Main sees REQUEST_ID: " + REQUEST_ID.get()); // null — isolated per thread
        System.out.println("\n→ InheritableThreadLocal propagates to child threads — rarely needed today.");
    }

    public static void main(String[] args) throws Exception {
        new Q11ThreadLocalDemo().run();
    }
}

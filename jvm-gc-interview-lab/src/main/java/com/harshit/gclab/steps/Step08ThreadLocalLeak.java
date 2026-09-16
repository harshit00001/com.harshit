package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.GcStats;
import com.harshit.gclab.support.Say;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Answers: why is ThreadLocal a classic leak in a Spring Boot app?
 * <p>
 * The pool threads outlive every request. ThreadLocalMap holds its key weakly but its value
 * strongly, so a value set during a request stays reachable through the thread until someone calls
 * {@code remove()}. Run both modes and compare the live set:
 * <pre>
 *   step08 leak   -> ~4 threads x 4 MB retained after GC
 *   step08 fixed  -> live set returns to the baseline
 * </pre>
 */
public class Step08ThreadLocalLeak implements GcLabStep {

    private static final ThreadLocal<byte[]> REQUEST_CONTEXT = new ThreadLocal<>();
    private static final int POOL_SIZE = 4;
    private static final int TASKS = 40;
    private static final int PAYLOAD_MB = 4;

    @Override
    public String id() {
        return "step08";
    }

    @Override
    public String question() {
        return "Why does ThreadLocal leak in a thread pool, and what does remove() actually fix?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms128m -Xmx128m";
    }

    @Override
    public void run(String[] args) throws Exception {
        boolean cleanUp = args.length > 0 && "fixed".equalsIgnoreCase(args[0]);
        Say.line("mode: %s (pass 'fixed' to call remove() in a finally block)",
                cleanUp ? "FIXED" : "LEAK");

        Garbage.requestFullGcForDemoOnly();
        long baseline = GcStats.take().oldGenLiveAfterGc();
        Say.line("live set before any request: %s%n", Say.bytes(baseline));

        // The pool must still be alive when we measure: shutting it down kills the threads and with
        // them their ThreadLocalMaps, which would hide the leak.
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        CountDownLatch done = new CountDownLatch(TASKS);
        for (int i = 0; i < TASKS; i++) {
            pool.submit(() -> {
                try {
                    handleRequest(cleanUp);
                } finally {
                    done.countDown();
                }
            });
        }
        Say.line("all %d tasks finished: %s", TASKS, done.await(30, TimeUnit.SECONDS));

        Garbage.requestFullGcForDemoOnly();
        long afterRequests = GcStats.take().oldGenLiveAfterGc();
        long retained = afterRequests - baseline;

        Say.blank();
        Say.line("live set after the requests : %s", Say.bytes(afterRequests));
        Say.line("still retained by the pool  : %s (expected ~%s in leak mode)",
                Say.bytes(Math.max(0, retained)), Say.bytes((long) POOL_SIZE * Garbage.mb(PAYLOAD_MB)));
        Say.line("verdict: %s", retained > (long) Garbage.mb(PAYLOAD_MB)
                ? "LEAKING — one payload per pool thread is unreachable from the request but still alive"
                : "CLEAN — nothing survives the request");

        pool.shutdown();
        boolean threadsDead = pool.awaitTermination(30, TimeUnit.SECONDS);
        Garbage.requestFullGcForDemoOnly();
        Say.line("live set once the pool threads have died (%s): %s",
                threadsDead ? "terminated" : "still running",
                Say.bytes(GcStats.take().oldGenLiveAfterGc()));
        Say.line("  -> the value was only ever reachable through the thread, so killing the thread releases it");

        Say.takeaway(
                "ThreadLocalMap keys are weak, values are strong: only the thread dying or remove() releases the value.",
                "Tomcat and executor threads are pooled for the process lifetime, so 'leak per thread' is permanent, not transient.",
                "Always clear in a finally block — that is exactly what Spring's RequestContextHolder and MDC cleanup do for you.",
                "In production this shows as a live set that steps up after each deploy and never falls; a heap dump names the thread as the retainer.",
                "Same shape of bug: unclosed resources, listeners never deregistered, and static caches without eviction.");
    }

    private void handleRequest(boolean cleanUp) {
        try {
            REQUEST_CONTEXT.set(Garbage.payload(Garbage.mb(PAYLOAD_MB)));
            Say.sleep(5);
        } finally {
            if (cleanUp) {
                REQUEST_CONTEXT.remove();
            }
        }
    }
}

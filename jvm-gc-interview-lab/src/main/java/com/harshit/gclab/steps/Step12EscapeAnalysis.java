package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Say;

import java.lang.management.ManagementFactory;

/**
 * Answers: is a non-escaping object really allocated on the stack?
 * <p>
 * Measures bytes actually allocated by this thread while creating ten million short-lived objects.
 * Run it twice:
 * <pre>
 *   default                      -> allocation nearly disappears (scalar replacement)
 *   -XX:-DoEscapeAnalysis        -> roughly 24 bytes per iteration shows up
 * </pre>
 * The garbage the GC never has to collect is the cheapest garbage of all.
 */
public class Step12EscapeAnalysis implements GcLabStep {

    private static final int ITERATIONS = 10_000_000;

    /** Volatile sink so the JIT cannot delete the result of the computation itself. */
    private static volatile long consumed;

    @Override
    public String id() {
        return "step12";
    }

    @Override
    public String question() {
        return "Does escape analysis allocate objects on the stack, and how would you prove it?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms128m -Xmx128m   (then repeat with -XX:-DoEscapeAnalysis)";
    }

    @Override
    public void run(String[] args) {
        com.sun.management.ThreadMXBean threads =
                (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();
        threads.setThreadAllocatedMemoryEnabled(true);
        long threadId = Thread.currentThread().getId();

        Say.line("escape analysis enabled: %s", com.harshit.gclab.support.Jvm.flag("DoEscapeAnalysis"));
        Say.line("warming up so the JIT compiles and analyses the hot method...");
        run(2_000_000);

        long before = threads.getThreadAllocatedBytes(threadId);
        long startNanos = System.nanoTime();
        run(ITERATIONS);
        long elapsedMillis = (System.nanoTime() - startNanos) / 1_000_000L;
        long allocated = threads.getThreadAllocatedBytes(threadId) - before;

        Say.blank();
        Say.line("iterations            : %,d", ITERATIONS);
        Say.line("bytes allocated       : %s", Say.bytes(allocated));
        Say.line("bytes per iteration   : %.2f", allocated / (double) ITERATIONS);
        Say.line("elapsed               : %d ms", elapsedMillis);
        Say.line("consumed (anti-dead-code): %d", consumed);
        Say.blank();
        Say.line(allocated / (double) ITERATIONS < 4.0
                ? "Allocation was eliminated: the object never became a heap object."
                : "Allocation happened per iteration — this is what -XX:-DoEscapeAnalysis looks like.");

        Say.takeaway(
                "HotSpot does not literally allocate on the stack: when JIT proves an object never escapes it performs scalar replacement, so the fields live in registers or stack slots.",
                "The allocation disappears entirely, which means zero GC pressure — measurable with com.sun.management.ThreadMXBean#getThreadAllocatedBytes.",
                "It only works after the method is JIT-compiled and only when the object does not escape: storing it in a field, a collection or returning it defeats it.",
                "Practical consequence: small short-lived wrappers in a hot loop are often free, so avoid contorting readable code for 'object avoidance' without measuring first.");
    }

    private static void run(int iterations) {
        long total = 0;
        for (int i = 0; i < iterations; i++) {
            Point point = new Point(i, i + 1);
            total += point.sum();
        }
        consumed = total;
    }

    /** Never stored anywhere, so the JIT can prove it does not escape {@link #run(int)}. */
    private static final class Point {
        private final int x;
        private final int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private int sum() {
            return x + y;
        }
    }
}

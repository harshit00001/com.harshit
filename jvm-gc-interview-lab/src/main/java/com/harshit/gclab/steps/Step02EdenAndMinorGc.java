package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.GcStats;
import com.harshit.gclab.support.Say;

/**
 * Answers: walk me through an object's life, and what is a minor GC?
 * <p>
 * The loop allocates 64 KB at a time and immediately abandons it. Eden fills, a young collection
 * runs, and heap usage returns to roughly where it started — 300 MB of allocation inside a 64 MB
 * heap only works because almost everything died young.
 * <p>
 * Chunk size matters here: in a 64 MB heap G1 picks 1 MB regions, so a 1 MB array would exceed the
 * humongous threshold (half a region) and skip Eden altogether. That case is {@code step09}.
 */
public class Step02EdenAndMinorGc implements GcLabStep {

    private static final int TOTAL_MB = 300;
    private static final int CHUNK_KB = 64;
    private static final int CHUNKS_PER_MB = 1024 / CHUNK_KB;

    @Override
    public String id() {
        return "step02";
    }

    @Override
    public String question() {
        return "What happens between allocating an object and a minor GC reclaiming it?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms64m -Xmx64m -Xlog:gc";
    }

    @Override
    public void run(String[] args) {
        GcStats start = GcStats.take();
        Say.line("heap max %s, heap used at start %s",
                Say.bytes(Runtime.getRuntime().maxMemory()), Say.bytes(start.heapUsed()));
        Say.line("Allocating %d MB of short-lived objects in %d KB chunks.%n", TOTAL_MB, CHUNK_KB);

        Say.line("%8s %14s %14s %14s", "ALLOCED", "EDEN USED", "HEAP USED", "YOUNG GCs");
        for (int megabyte = 1; megabyte <= TOTAL_MB; megabyte++) {
            Garbage.churn(CHUNKS_PER_MB, Garbage.kb(CHUNK_KB));
            if (megabyte % 30 == 0) {
                GcStats now = GcStats.take();
                Say.line("%6d MB %14s %14s %14d", megabyte,
                        Say.bytes(now.poolUsed("Eden")), Say.bytes(now.heapUsed()),
                        now.collections() - start.collections());
            }
        }

        GcStats end = GcStats.take();
        Say.blank();
        GcStats.printDelta("Allocating " + TOTAL_MB + " MB inside a " + Say.bytes(Runtime.getRuntime().maxMemory())
                + " heap", start, end);

        Say.takeaway(
                "Objects are born in Eden, inside a thread-local allocation buffer, so allocation is a pointer bump with no locking.",
                "Eden fills, a young collection copies the few survivors out, and Eden is emptied wholesale — cost scales with survivors, not with garbage.",
                "That is the weak generational hypothesis: most objects die young, which is why " + TOTAL_MB
                        + " MB of traffic fits in a tiny heap.",
                "Heap used returning to its baseline is the proof that nothing leaked.");
    }
}

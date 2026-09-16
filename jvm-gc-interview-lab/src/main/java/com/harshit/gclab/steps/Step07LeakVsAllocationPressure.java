package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.GcStats;
import com.harshit.gclab.support.Say;

import java.util.ArrayList;
import java.util.List;

/**
 * Answers: latency spikes in production — is it a leak or allocation pressure?
 * <p>
 * Both modes allocate the same number of bytes. Only the reading of "live bytes after the last
 * collection" separates them, which is exactly the call you make from a GC log at 2 a.m.
 * <pre>
 *   step07 leak    -> live set climbs every round and never comes back down
 *   step07 churn   -> live set is flat; the GC is busy but healthy
 * </pre>
 */
public class Step07LeakVsAllocationPressure implements GcLabStep {

    /** A static collection: the most common accidental leak in a long-running service. */
    private static final List<byte[]> LEAKED_CACHE = new ArrayList<>();

    private static final int ROUNDS = 10;
    private static final int MB_PER_ROUND = 8;

    @Override
    public String id() {
        return "step07";
    }

    @Override
    public String question() {
        return "A service has latency spikes: how do you tell a memory leak from allocation pressure?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms96m -Xmx96m -Xlog:gc -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dumps";
    }

    @Override
    public void run(String[] args) {
        boolean leaking = args.length == 0 || "leak".equalsIgnoreCase(args[0]);
        Say.line("mode: %s (pass 'churn' or 'leak' to switch)", leaking ? "LEAK" : "CHURN");
        Say.line("Both modes allocate %d MB in total; only retention differs.%n", ROUNDS * MB_PER_ROUND);

        GcStats start = GcStats.take();
        Say.line("%6s %16s %18s %14s", "ROUND", "HEAP AFTER GC", "LIVE SET AFTER GC", "GC PAUSE ms");
        try {
            for (int round = 1; round <= ROUNDS; round++) {
                for (int mb = 0; mb < MB_PER_ROUND; mb++) {
                    byte[] payload = Garbage.payload(Garbage.mb(1));
                    if (leaking) {
                        LEAKED_CACHE.add(payload);
                    }
                }
                Garbage.requestFullGcForDemoOnly();
                GcStats now = GcStats.take();
                Say.line("%6d %16s %18s %14d", round,
                        Say.bytes(now.heapUsed()),
                        Say.bytes(now.oldGenLiveAfterGc()),
                        now.collectionTimeMillis() - start.collectionTimeMillis());
            }
        } catch (OutOfMemoryError e) {
            LEAKED_CACHE.clear();
            Garbage.requestFullGcForDemoOnly();
            Say.blank();
            Say.line("OutOfMemoryError: %s", e.getMessage());
            Say.line("This is the moment -XX:+HeapDumpOnOutOfMemoryError pays for itself:");
            Say.line("  open the dump in Eclipse MAT, run Leak Suspects, follow the dominator tree to the retaining root.");
        }

        Say.blank();
        GcStats.printDelta(leaking ? "Leak mode" : "Churn mode", start, GcStats.take());
        Say.line("objects still retained by the static cache: %d", LEAKED_CACHE.size());

        Say.takeaway(
                "Allocation rate alone tells you nothing — read the live set after each collection, not the used heap.",
                "Live set climbing monotonically across full GCs = leak; take a heap dump and find the retaining root.",
                "Live set flat while GC count is high = allocation pressure; profile allocations with JFR or async-profiler and allocate less.",
                "GC pause time rising while reclaimed bytes shrink is the classic slide toward 'GC overhead limit exceeded'.",
                "Always ship with -XX:+HeapDumpOnOutOfMemoryError: without the dump you get one shot at reproducing it.");
    }
}

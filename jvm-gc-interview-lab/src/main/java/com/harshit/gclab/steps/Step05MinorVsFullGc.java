package com.harshit.gclab.steps;

import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.GcStats;
import com.harshit.gclab.support.Say;

import java.util.ArrayList;
import java.util.List;

/**
 * Answers: minor vs major vs full GC, and does {@code System.gc()} guarantee anything?
 * <p>
 * Phase 1 produces only young collections. Phase 2 asks for a full collection and shows the cost
 * landing on the <em>old</em> collector instead — the per-collector counters are how you tell them
 * apart in production without guessing.
 */
public class Step05MinorVsFullGc implements GcLabStep {

    private static final int TOTAL_MB = 400;
    private static final int LIVE_MB = 40;
    private static final int CHUNK_KB = 256;
    private static final int CHUNKS_PER_MB = 1024 / CHUNK_KB;

    @Override
    public String id() {
        return "step05";
    }

    @Override
    public String question() {
        return "What is the difference between a minor, major and full GC in real numbers?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms128m -Xmx128m -Xlog:gc";
    }

    @Override
    public void run(String[] args) {
        // 256 KB chunks stay below G1's humongous threshold at these heap sizes, so the numbers
        // reflect generational behaviour rather than region rounding (that story is step09).
        Say.section("Phase 1: short-lived garbage only");
        GcStats beforeYoung = GcStats.take();
        Garbage.churn(TOTAL_MB * CHUNKS_PER_MB, Garbage.kb(CHUNK_KB));
        GcStats afterYoung = GcStats.take();
        GcStats.printDelta(TOTAL_MB + " MB of short-lived allocation", beforeYoung, afterYoung);
        perCollectionAverage("young", beforeYoung, afterYoung);

        Say.section("Phase 2: a long-lived population, then an explicit full collection");
        List<byte[]> retained = new ArrayList<>();
        for (int i = 0; i < LIVE_MB * CHUNKS_PER_MB; i++) {
            retained.add(Garbage.payload(Garbage.kb(CHUNK_KB)));
        }
        GcStats beforeFull = GcStats.take();
        Garbage.requestFullGcForDemoOnly();
        GcStats afterFull = GcStats.take();
        GcStats.printDelta("System.gc() with " + LIVE_MB + " MB live", beforeFull, afterFull);
        Say.line("  live set retained on purpose: %s", Say.bytes((long) LIVE_MB * 1024 * 1024));
        Say.line("  old gen still used after the full GC: %s", Say.bytes(afterFull.oldGenLiveAfterGc()));
        perCollectionAverage("full", beforeFull, afterFull);

        Say.section("Phase 3: the same request once the population is unreachable");
        retained.clear();
        GcStats beforeClear = GcStats.take();
        Garbage.requestFullGcForDemoOnly();
        GcStats.printDelta("System.gc() after dropping the references", beforeClear, GcStats.take());

        Say.takeaway(
                "A minor GC touches young gen only: frequent, short, and cheap because it copies survivors rather than scanning garbage.",
                "A full GC walks young, old and Metaspace and usually compacts, so its cost scales with the live set — modest on this small heap, seconds on a multi-gigabyte one.",
                "The per-collector counters name which one ran: 'G1 Young Generation' versus 'G1 Old Generation' or 'G1 Concurrent GC'.",
                "System.gc() is only a hint — it can be disabled with -XX:+DisableExplicitGC, so calling it in application code is a code smell.",
                "Cost tracks the live set, not the garbage: the same call reclaims far more once references are dropped.");
    }

    private void perCollectionAverage(String label, GcStats before, GcStats after) {
        long collections = after.collections() - before.collections();
        long millis = after.collectionTimeMillis() - before.collectionTimeMillis();
        Say.line("  average %s pause: %s",
                label, collections == 0 ? "no collections" : String.format("%.2f ms over %d collections",
                        millis / (double) collections, collections));
    }
}

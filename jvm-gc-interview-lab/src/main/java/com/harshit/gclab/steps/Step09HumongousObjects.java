package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.GcStats;
import com.harshit.gclab.support.Jvm;
import com.harshit.gclab.support.Say;

import java.util.ArrayList;
import java.util.List;

/**
 * Answers: what is a humongous object in G1, and why should a Kafka consumer care?
 * <p>
 * Phase A and B move the same bytes with different chunk sizes and watch Eden: humongous
 * allocations grow the heap without touching Eden, because G1 places them straight into contiguous
 * humongous regions. Phase C shows where the real cost lives — retained humongous objects plus
 * churn, which fragments the heap and forces full collections.
 * <p>
 * Note for honesty in an interview: since JDK 8u60 G1 <em>eagerly reclaims</em> short-lived
 * humongous regions during young collections, so a stream of big-and-immediately-dead arrays is
 * cheaper than it used to be. The pain shows up when they survive or when the heap fragments.
 */
public class Step09HumongousObjects implements GcLabStep {

    private static final int TOTAL_MB = 240;

    @Override
    public String id() {
        return "step09";
    }

    @Override
    public String question() {
        return "What is a humongous allocation in G1 and when does it actually hurt?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms256m -Xmx256m -XX:G1HeapRegionSize=1m -Xlog:gc";
    }

    @Override
    public void run(String[] args) {
        long regionSize = Jvm.g1RegionSize();
        if (regionSize <= 0) {
            Say.line("This step needs G1 (active collector is %s). Re-run with -XX:+UseG1GC.",
                    Jvm.activeCollector());
            return;
        }
        int humongousBytes = (int) (regionSize * 6 / 10);
        int smallBytes = (int) (regionSize / 16);
        Say.line("G1 region size    : %s", Say.bytes(regionSize));
        Say.line("humongous cut-off : %s (anything larger than half a region)", Say.bytes(regionSize / 2));
        Say.line("phase A chunk     : %s -> humongous", Say.bytes(humongousBytes));
        Say.line("phase B chunk     : %s -> ordinary Eden allocation", Say.bytes(smallBytes));

        Say.section("Phase A: humongous chunks — watch Eden stay flat");
        allocateAndWatchEden(humongousBytes);

        Say.section("Phase B: the same bytes in Eden-sized chunks");
        allocateAndWatchEden(smallBytes);

        Say.section("Phase C: humongous objects that survive, plus churn");
        retainHumongousUnderChurn(humongousBytes, smallBytes);

        Say.section("Phase D: region rounding — the trap in new byte[1MB]");
        measureRegionRounding(regionSize);

        Say.takeaway(
                "Humongous = any allocation larger than half a G1 region; it skips Eden and needs contiguous regions.",
                "Eden staying flat while the heap grows is the fingerprint of humongous allocation in a GC log or MXBean reading.",
                "Short-lived humongous objects are eagerly reclaimed on modern JDKs, so phase A and B cost about the same — say that rather than repeating folklore.",
                "The measured cost is in phase C: pinning half the heap in humongous regions leaves less room for Eden, so young collections became roughly ten times more frequent.",
                "Phase D is the detail that impresses: new byte[1MB] in a 1 MB-region heap needs two regions because of the object header, so it wastes nearly half the space it occupies.",
                "Real triggers on my stack: large Kafka batches, big JSON or XML payloads, and unpaginated JPA result sets.",
                "Fixes in order: lower max.poll.records or page the query, stream instead of buffering, then consider a larger -XX:G1HeapRegionSize.",
                "'To-space exhausted' or 'Evacuation failure' in the log is this story escalating: G1 had nowhere to copy survivors.");
    }

    private void allocateAndWatchEden(int chunkBytes) {
        GcStats start = GcStats.take();
        long edenStart = start.poolUsed("Eden");
        long target = (long) TOTAL_MB * 1024 * 1024;
        long allocated = 0;
        long edenPeak = edenStart;

        while (allocated < target) {
            Garbage.sink = new byte[chunkBytes];
            allocated += chunkBytes;
            if (allocated % (60L * 1024 * 1024) < chunkBytes) {
                edenPeak = Math.max(edenPeak, GcStats.take().poolUsed("Eden"));
            }
        }

        GcStats end = GcStats.take();
        Say.line("  eden used: %s at start, peak %s during %d MB of allocation",
                Say.bytes(edenStart), Say.bytes(edenPeak), TOTAL_MB);
        GcStats.printDelta("  " + TOTAL_MB + " MB in " + Say.bytes(chunkBytes) + " chunks", start, end);
    }

    /**
     * A humongous object occupies whole regions, so an array whose payload exactly equals the region
     * size needs one more region for its 16-byte header — half the heap disappears into padding.
     */
    private void measureRegionRounding(long regionSize) {
        int count = 20;
        long exactly = heapCostOf(count, (int) regionSize);
        long justUnder = heapCostOf(count, (int) regionSize - 1024);

        Say.line("  %d arrays of exactly the region size (%s) cost %s of heap",
                count, Say.bytes(regionSize), Say.bytes(exactly));
        Say.line("  %d arrays 1 KB smaller                        cost %s of heap",
                count, Say.bytes(justUnder));
        Say.line("  -> the header pushes each array into a second region, wasting nearly half the space");
    }

    private long heapCostOf(int count, int arrayBytes) {
        Garbage.requestFullGcForDemoOnly();
        long before = GcStats.take().heapUsed();
        List<byte[]> held = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            held.add(new byte[arrayBytes]);
        }
        long after = GcStats.take().heapUsed();
        held.clear();
        return after - before;
    }

    private void retainHumongousUnderChurn(int humongousBytes, int smallBytes) {
        // Clear the garbage left by the earlier phases so the retention target is meaningful.
        Garbage.requestFullGcForDemoOnly();

        List<byte[]> retained = new ArrayList<>();
        long retainTarget = Runtime.getRuntime().maxMemory() / 2;
        GcStats start = GcStats.take();

        try {
            long retainedBytes = 0;
            while (retainedBytes + humongousBytes <= retainTarget) {
                retained.add(new byte[humongousBytes]);
                retainedBytes += humongousBytes;
            }
            Say.line("  pinned %d humongous objects (%s of a %s heap)", retained.size(),
                    Say.bytes(retainedBytes), Say.bytes(Runtime.getRuntime().maxMemory()));
            for (int round = 0; round < 40; round++) {
                Garbage.churn(256, smallBytes);
            }
        } catch (OutOfMemoryError e) {
            Say.line("  OutOfMemoryError while holding humongous regions: %s", e.getMessage());
        }

        GcStats end = GcStats.take();
        GcStats.printDelta("  churn with half the heap pinned in humongous regions", start, end);
        Say.line("  young collections: %d | old-generation collections (the expensive ones): %d",
                end.youngGenerationCollections() - start.youngGenerationCollections(),
                end.oldGenerationCollections() - start.oldGenerationCollections());
        retained.clear();
    }
}

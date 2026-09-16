package com.harshit.gclab.support;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A point-in-time reading of the GC counters and heap pools, so a step can prove what happened
 * between two moments instead of asserting it.
 * <p>
 * Two readings matter for interviews:
 * <ul>
 *   <li>{@link MemoryPoolMXBean#getUsage()} — bytes used right now, garbage included.</li>
 *   <li>{@link MemoryPoolMXBean#getCollectionUsage()} — bytes still used <em>after</em> the last
 *       collection of that pool. This is the live set, and a monotonic rise here is the signature
 *       of a memory leak rather than mere allocation churn.</li>
 * </ul>
 */
public final class GcStats {

    private final Map<String, long[]> collectors = new LinkedHashMap<>();
    private final Map<String, Long> poolUsed = new LinkedHashMap<>();
    private final Map<String, Long> poolLiveAfterGc = new LinkedHashMap<>();
    private final long heapUsed;
    private final long nanos;

    private GcStats() {
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            collectors.put(gc.getName(), new long[]{gc.getCollectionCount(), gc.getCollectionTime()});
        }
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            poolUsed.put(pool.getName(), used(pool.getUsage()));
            poolLiveAfterGc.put(pool.getName(), used(pool.getCollectionUsage()));
        }
        this.heapUsed = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        this.nanos = System.nanoTime();
    }

    public static GcStats take() {
        return new GcStats();
    }

    public long heapUsed() {
        return heapUsed;
    }

    public long collections() {
        return collectors.values().stream().mapToLong(v -> v[0]).sum();
    }

    public long collectionTimeMillis() {
        return collectors.values().stream().mapToLong(v -> v[1]).sum();
    }

    /** Collections attributed to the old-generation collector, i.e. the expensive ones. */
    public long oldGenerationCollections() {
        return collectors.entrySet().stream()
                .filter(entry -> isOldCollector(entry.getKey()))
                .mapToLong(entry -> entry.getValue()[0])
                .sum();
    }

    public long youngGenerationCollections() {
        return collectors.entrySet().stream()
                .filter(entry -> !isOldCollector(entry.getKey()))
                .mapToLong(entry -> entry.getValue()[0])
                .sum();
    }

    /** Live bytes left in the old generation after its last collection, or -1 when unavailable. */
    public long oldGenLiveAfterGc() {
        for (Map.Entry<String, Long> entry : poolLiveAfterGc.entrySet()) {
            if (isOldGen(entry.getKey())) {
                return entry.getValue();
            }
        }
        return -1L;
    }

    public long poolUsed(String nameFragment) {
        for (Map.Entry<String, Long> entry : poolUsed.entrySet()) {
            if (entry.getKey().toLowerCase().contains(nameFragment.toLowerCase())) {
                return entry.getValue();
            }
        }
        return -1L;
    }

    public void printPools() {
        Say.line("%-26s %12s %12s", "HEAP POOL", "USED NOW", "LIVE AFTER GC");
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getType() != MemoryType.HEAP) {
                continue;
            }
            Say.line("%-26s %12s %12s", pool.getName(),
                    Say.bytes(used(pool.getUsage())), Say.bytes(used(pool.getCollectionUsage())));
        }
    }

    /** Per-collector counts and times, which is how you tell a young GC from a full GC. */
    public static void printDelta(String title, GcStats before, GcStats after) {
        Say.line("%s (wall clock %d ms)", title,
                (after.nanos - before.nanos) / 1_000_000L);
        Say.line("  %-30s %12s %12s", "COLLECTOR", "COLLECTIONS", "PAUSE TIME");
        for (Map.Entry<String, long[]> entry : after.collectors.entrySet()) {
            long[] now = entry.getValue();
            long[] then = before.collectors.getOrDefault(entry.getKey(), new long[]{0, 0});
            Say.line("  %-30s %12d %10d ms", entry.getKey(), now[0] - then[0], now[1] - then[1]);
        }
        Say.line("  heap used: %s -> %s", Say.bytes(before.heapUsed), Say.bytes(after.heapUsed));
    }

    private static boolean isOldCollector(String collectorName) {
        String name = collectorName.toLowerCase();
        return name.contains("old") || name.contains("marksweep") || name.contains("concurrent")
                || name.contains("pauses");
    }

    private static boolean isOldGen(String poolName) {
        String name = poolName.toLowerCase();
        return name.contains("old gen") || name.contains("tenured");
    }

    private static long used(MemoryUsage usage) {
        return usage == null ? -1L : usage.getUsed();
    }
}

package com.harshit.gclab.support;

import com.sun.management.HotSpotDiagnosticMXBean;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

/**
 * Reads the JVM's own view of its configuration, which is the honest way to answer
 * "which collector is running and how is the heap sized".
 */
public final class Jvm {

    private static final HotSpotDiagnosticMXBean DIAGNOSTIC =
            ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);

    private Jvm() {
    }

    /** Flag value as the JVM resolved it, or {@code null} when this build has no such flag. */
    public static String flag(String name) {
        try {
            return DIAGNOSTIC.getVMOption(name).getValue();
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    public static boolean flagEnabled(String name) {
        return "true".equals(flag(name));
    }

    public static String activeCollector() {
        for (String candidate : new String[]{"UseG1GC", "UseZGC", "UseParallelGC", "UseSerialGC",
                "UseShenandoahGC", "UseEpsilonGC"}) {
            if (flagEnabled(candidate)) {
                return candidate.substring(3);
            }
        }
        return "unknown";
    }

    /** G1 region size in bytes, or -1 when G1 is not the active collector. */
    public static long g1RegionSize() {
        String value = flag("G1HeapRegionSize");
        return value == null ? -1L : Long.parseLong(value);
    }

    public static void printIdentity() {
        Say.line("java %s (%s)", System.getProperty("java.version"), System.getProperty("java.vm.name"));
        Say.line("active collector: %s", activeCollector());
        Say.line("collectors registered: %s", ManagementFactory.getGarbageCollectorMXBeans().stream()
                .map(GarbageCollectorMXBean::getName).toList());
        Say.line("heap max: %s | cpus visible to JVM: %d",
                Say.bytes(Runtime.getRuntime().maxMemory()), Runtime.getRuntime().availableProcessors());
    }
}

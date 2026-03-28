package com.harshit.preparation.jvm;

/**
 * <h2>Heap vs stack + tiny runtime snapshot (interview)</h2>
 * <p>
 * <b>Stack:</b> per-thread; stores frames (local primitives, references). Fast allocation/deallocation.
 * <p>
 * <b>Heap:</b> all object instances; shared; managed by GC.
 * <p>
 * <b>JMM (Java Memory Model):</b> rules for visibility and ordering across threads — use
 * {@code synchronized}, {@code volatile}, or java.util.concurrent utilities for safe publication.
 */
public final class MemoryOverviewDemo {

    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        long max = rt.maxMemory();
        long total = rt.totalMemory();
        long free = rt.freeMemory();
        System.out.println("Runtime.maxMemory() bytes ~ " + (max / 1024 / 1024) + " MiB");
        System.out.println("total heap (current) ~ " + (total / 1024 / 1024) + " MiB");
        System.out.println("approx used ~ " + ((total - free) / 1024 / 1024) + " MiB");
        System.out.println("GC types to mention in interview: G1 (common default), ZGC, Parallel, Serial.");
    }

    private MemoryOverviewDemo() {
    }
}

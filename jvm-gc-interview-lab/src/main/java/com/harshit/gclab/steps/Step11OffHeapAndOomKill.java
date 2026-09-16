package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.Jvm;
import com.harshit.gclab.support.Say;

import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Answers: the pod was OOMKilled but there is no OutOfMemoryError in the logs — explain.
 * <p>
 * Direct buffers live outside the heap. The heap stays almost empty here while the process footprint
 * grows, which is precisely how a container gets killed by the kernel with a healthy-looking JVM.
 */
public class Step11OffHeapAndOomKill implements GcLabStep {

    private static final int CHUNK_MB = 8;
    private static final int MAX_CHUNKS = 64;

    @Override
    public String id() {
        return "step11";
    }

    @Override
    public String question() {
        return "Why can a container be OOMKilled while the Java heap looks fine?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms64m -Xmx64m -XX:MaxDirectMemorySize=64m -XX:NativeMemoryTracking=summary";
    }

    @Override
    public void run(String[] args) {
        Say.line("heap max              : %s", Say.bytes(Runtime.getRuntime().maxMemory()));
        Say.line("MaxDirectMemorySize   : %s", Jvm.flag("MaxDirectMemorySize"));
        Say.line("NativeMemoryTracking  : %s%n", Jvm.flag("NativeMemoryTracking"));

        List<ByteBuffer> buffers = new ArrayList<>();
        Say.line("%8s %16s %16s %16s", "DIRECT", "BUFFER COUNT", "OFF-HEAP USED", "HEAP USED");
        try {
            for (int i = 1; i <= MAX_CHUNKS; i++) {
                buffers.add(ByteBuffer.allocateDirect(Garbage.mb(CHUNK_MB)));
                if (i % 2 == 0) {
                    printFootprint(i * CHUNK_MB);
                }
            }
            Say.blank();
            Say.line("Reached %d MB of direct memory without an error.", MAX_CHUNKS * CHUNK_MB);
        } catch (OutOfMemoryError e) {
            Say.blank();
            Say.line("OutOfMemoryError: %s", e.getMessage());
            Say.line("Note which limit was hit: this is the direct-memory ceiling, not the heap.");
        } finally {
            buffers.clear();
        }

        Say.section("What makes up process RSS besides the heap");
        Say.line("  heap (-Xmx)               : %s", Say.bytes(Runtime.getRuntime().maxMemory()));
        Say.line("  Metaspace + code cache    : %s",
                Say.bytes(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()));
        Say.line("  direct / mapped buffers   : see the table above");
        Say.line("  thread stacks             : ~1 MB per thread x %d live threads",
                Thread.activeCount());
        Say.line("  GC bookkeeping            : remembered sets and card tables scale with heap");
        Say.line("  inspect it all with       : jcmd <pid> VM.native_memory summary");

        Say.takeaway(
                "OutOfMemoryError comes from the JVM refusing an allocation; OOMKilled (exit 137) comes from the kernel enforcing the cgroup limit.",
                "Heap is only part of RSS — Metaspace, code cache, thread stacks, direct buffers and GC structures ride on top.",
                "So sizing the heap at 100% of the pod limit guarantees an OOMKill; I leave 25-30% headroom via MaxRAMPercentage=70.",
                "Netty and NIO clients allocate direct buffers, so bound them with -XX:MaxDirectMemorySize to fail loudly instead of silently growing.",
                "Diagnose native growth with Native Memory Tracking, not a heap dump — a heap dump will look innocent.");
    }

    private void printFootprint(int allocatedMb) {
        long offHeap = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class).stream()
                .filter(pool -> "direct".equals(pool.getName()))
                .mapToLong(BufferPoolMXBean::getMemoryUsed)
                .sum();
        long count = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class).stream()
                .filter(pool -> "direct".equals(pool.getName()))
                .mapToLong(BufferPoolMXBean::getCount)
                .sum();
        Say.line("%6d MB %16d %16s %16s", allocatedMb, count, Say.bytes(offHeap),
                Say.bytes(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()));
    }
}

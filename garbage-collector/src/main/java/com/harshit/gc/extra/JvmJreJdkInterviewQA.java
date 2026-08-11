package com.harshit.gc.extra;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * Interview prep (placed under the garbage-collector project): runtime & JVM stack.
 *
 * <p><b>Q. What is the JVM?</b>
 * The Java Virtual Machine executes bytecode, manages memory (heap, metaspace, etc.),
 * schedules garbage collection, and provides threads. Real life: the "engine" that runs your
 * compiled app the same way on Windows or Linux — write once, run anywhere.
 *
 * <p><b>Q. What is the JRE?</b>
 * Java Runtime Environment = JVM + standard libraries + deployment pieces needed to run Java
 * programs (no compiler). Real life: what you install on a server that only needs to execute jars.
 *
 * <p><b>Q. What is the JDK?</b>
 * Java Development Kit = JRE + tools (javac, javadoc, jcmd, etc.). Real life: what developers
 * install to compile and debug; CI build agents use a JDK.
 *
 * <p><b>Q. How does this relate to garbage collection?</b>
 * The JVM's GC reclaims unreachable heap objects. Tuning flags (-Xmx, choice of collector) are
 * JVM options. Real life: a payment service holding too many cached DTOs may need a larger heap
 * or a low-pause collector (G1/ZGC) so user transactions do not stall during Full GC.
 */
public final class JvmJreJdkInterviewQA {

    private JvmJreJdkInterviewQA() {
    }

    public static void main(String[] args) {
        System.out.println("=== JVM identity ===");
        System.out.println("Java vendor: " + System.getProperty("java.vendor"));
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("VM name: " + System.getProperty("java.vm.name"));

        // Code demo: read heap usage via JMX (available in a running JVM).
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memory.getHeapMemoryUsage();
        System.out.println("\n=== Heap snapshot (JMX) ===");
        System.out.println("init=" + heap.getInit() + " used=" + heap.getUsed()
                + " committed=" + heap.getCommitted() + " max=" + heap.getMax());

        // Code demo: object becomes eligible for GC when unreachable (do not rely on finalize).
        demoUnreachableObject();
    }

    private static void demoUnreachableObject() {
        StringBuilder big = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            big.append('x');
        }
        big = null; // eligible for GC — no live reference
        System.out.println("\n(big reference cleared; object may be collected on next GC cycle)");
    }
}

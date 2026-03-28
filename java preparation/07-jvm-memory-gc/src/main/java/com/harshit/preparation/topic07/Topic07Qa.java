package com.harshit.preparation.topic07;

/**
 * Topic 07 — JVM, heap, stack, GC, JMM (overview).
 */
public final class Topic07Qa {

    private Topic07Qa() {
    }

    /*
     * Q: Heap vs stack?
     *
     * SCRIPT:
     * I describe the stack as thread-private: each method call gets a frame with local variables
     * and references, and when the method returns the frame pops—very fast. The heap is where all
     * objects live and is shared across threads; allocation is cheap but unused objects must be
     * reclaimed by the garbage collector. So locals and small scopes lean on the stack; objects
     * always live on the heap.
     */

    /*
     * Q: How does garbage collection work?
     *
     * SCRIPT:
     * I would say the GC finds objects that are not reachable from GC roots—threads, static refs,
     * JNI, etc.—and reclaims their memory. Most JVMs use generational collectors because most objects
     * die young, so the young generation is collected often with a cheap algorithm, and survivors
     * move to the old generation over time. Tuning is about latency versus throughput and choosing
     * a collector—G1 is common; ZGC or Shenandoah for very large heaps and low pause goals.
     */

    /*
     * Q: Types of garbage collectors?
     *
     * SCRIPT:
     * At a high level I name Serial (single-threaded, small heaps), Parallel or Throughput collectors
     * focused on batch work, G1 which divides the heap into regions and targets predictable pauses,
     * and low-latency options like ZGC. I would add that the choice depends on pause requirements,
     * heap size, and what we observe in GC logs—not guesswork.
     */

    /*
     * Q: Java Memory Model?
     *
     * SCRIPT:
     * The JMM defines when writes by one thread become visible to another. Without synchronization,
     * the compiler and CPU can reorder instructions in ways that break naive assumptions. I would
     * mention that synchronized and volatile establish happens-before edges, and that java.util.concurrent
     * utilities are built on those rules so we do not have to reason about every reordering by hand.
     */

    public static void heapSnapshot() {
        Runtime rt = Runtime.getRuntime();
        System.out.println("maxMemory=" + rt.maxMemory());
        System.out.println("totalMemory=" + rt.totalMemory());
    }

    public static void main(String[] args) {
        heapSnapshot();
    }
}

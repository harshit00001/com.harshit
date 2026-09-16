package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.GcStats;
import com.harshit.gclab.support.Jvm;
import com.harshit.gclab.support.Say;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

/**
 * Answers: which collector is running, how is the heap divided, and how would you know in a
 * container? Everything printed here is read from the running JVM, not assumed.
 */
public class Step01WhichCollector implements GcLabStep {

    @Override
    public String id() {
        return "step01";
    }

    @Override
    public String question() {
        return "Which collector is my JVM using, and how is the heap laid out?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms256m -Xmx256m";
    }

    @Override
    public void run(String[] args) {
        Say.section("Collector pairs (young collector + old collector)");
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            Say.line("%-30s manages pools %s", gc.getName(), String.join(", ", gc.getMemoryPoolNames()));
        }

        Say.section("Heap pools");
        GcStats.take().printPools();

        Say.section("Non-heap (not collected by the heap collectors)");
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getType() == MemoryType.NON_HEAP) {
                Say.line("%-26s %12s", pool.getName(), Say.bytes(pool.getUsage().getUsed()));
            }
        }

        Say.section("Flags that decide GC behaviour");
        printFlag("MaxHeapSize", "-Xmx, the ceiling the collector works within");
        printFlag("MaxRAMPercentage", "share of the container limit that becomes heap, when -Xmx is absent");
        printFlag("UseContainerSupport", "Linux-only flag: read cgroup limits instead of host RAM");
        printFlag("MaxGCPauseMillis", "G1's soft pause target; negative means this collector has no goal");
        printFlag("G1HeapRegionSize", "region size; humongous = allocation bigger than half of this");
        printFlag("MaxTenuringThreshold", "young collections survived before promotion to old gen");
        printFlag("MaxMetaspaceSize", "class metadata ceiling; -1 or 0 means effectively unbounded");
        printFlag("MaxDirectMemorySize", "off-heap NIO ceiling; 0 means 'default to the heap size'");

        Say.takeaway(
                "Java 8 defaults to Parallel, Java 9+ defaults to G1 on server-class machines.",
                "A small container (under ~1792 MB or fewer than 2 CPUs) silently falls back to Serial GC.",
                "Metaspace and code cache are non-heap: GC tuning will not shrink them.",
                "In Kubernetes I set MaxRAMPercentage instead of -Xmx so one image fits any pod limit.",
                "UseContainerSupport only exists on Linux builds, which is why it reads 'not in this build' on Windows.");
    }

    private void printFlag(String name, String why) {
        String value = Jvm.flag(name);
        Say.line("%-22s = %-14s (%s)", name, value == null ? "not in this build" : value, why);
    }
}

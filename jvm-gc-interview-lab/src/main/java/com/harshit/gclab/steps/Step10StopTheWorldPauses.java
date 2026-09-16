package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.GcStats;
import com.harshit.gclab.support.Jvm;
import com.harshit.gclab.support.PauseDetector;
import com.harshit.gclab.support.Say;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Answers: why does GC pause my threads, and what does that look like as user-visible latency?
 * <p>
 * A detector thread measures the latency the JVM steals while a workload keeps a large live set and
 * churns garbage around it. Run the same step under different collectors to see the trade-off:
 * <pre>
 *   -XX:+UseSerialGC     large pauses, cheapest CPU
 *   -XX:+UseParallelGC   best throughput, pauses scale with heap
 *   -XX:+UseG1GC         pauses aimed at MaxGCPauseMillis
 *   -XX:+UseZGC          pauses largely independent of heap size
 * </pre>
 */
public class Step10StopTheWorldPauses implements GcLabStep {

    private static final int SECONDS = 12;
    private static final int LIVE_SET_MB = 60;

    @Override
    public String id() {
        return "step10";
    }

    @Override
    public String question() {
        return "Why are stop-the-world pauses unavoidable, and how much latency do they add?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms256m -Xmx256m -Xlog:gc";
    }

    @Override
    public void run(String[] args) {
        Say.line("collector under test: %s | pause target: %s",
                Jvm.activeCollector(), pauseTarget());
        Say.line("Holding %d MB live and churning garbage for %d seconds.%n", LIVE_SET_MB, SECONDS);

        List<byte[]> liveSet = new ArrayList<>();
        for (int i = 0; i < LIVE_SET_MB; i++) {
            liveSet.add(Garbage.payload(Garbage.mb(1)));
        }

        GcStats start = GcStats.take();
        Random random = new Random(42);
        long deadline = System.currentTimeMillis() + (SECONDS * 1000L);

        try (PauseDetector detector = new PauseDetector()) {
            while (System.currentTimeMillis() < deadline) {
                Garbage.churn(20, Garbage.kb(256));
                // Replacing live entries forces real promotion and old-gen work, not just Eden churn.
                liveSet.set(random.nextInt(liveSet.size()), Garbage.payload(Garbage.mb(1)));
            }
            Say.blank();
            detector.report("observed application stalls");
        }

        GcStats end = GcStats.take();
        GcStats.printDelta("Workload", start, end);
        long pauseMillis = end.collectionTimeMillis() - start.collectionTimeMillis();
        Say.line("  GC accounted for %d ms of the %d s run (%.1f%% of wall clock)",
                pauseMillis, SECONDS, (pauseMillis * 100.0) / (SECONDS * 1000.0));

        Say.takeaway(
                "Moving objects requires a consistent heap view, so threads are parked at safepoints while references are updated.",
                "The stalls the detector sees are exactly what your p99 latency and health-check timeouts feel.",
                "Not every pause is GC: biased locking revocation, deoptimisation and heap dumps also stop the world — check -Xlog:safepoint.",
                "ZGC and Shenandoah mark and compact concurrently using load barriers, trading a little throughput for pauses that barely grow with heap size.",
                "Reducing allocation is usually a bigger win than switching collectors, because pause cost tracks the live set.");
    }

    /** Serial and Parallel report a negative sentinel here because they have no pause goal. */
    private static String pauseTarget() {
        String value = Jvm.flag("MaxGCPauseMillis");
        if (value == null) {
            return "not supported by this collector";
        }
        return Long.parseLong(value) < 0 ? "none (this collector has no pause goal)" : value + " ms";
    }
}

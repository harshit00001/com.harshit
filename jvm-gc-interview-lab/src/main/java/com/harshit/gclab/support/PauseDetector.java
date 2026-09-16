package com.harshit.gclab.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Detects stop-the-world pauses the way a user experiences them: a thread that only wants to sleep
 * 1 ms at a time, recording how much longer it actually took to wake up. Application threads cannot
 * run during a GC pause, so the missing time shows up here as latency the JVM stole.
 */
public final class PauseDetector implements AutoCloseable {

    private static final long TICK_MILLIS = 1;

    private final List<Long> hiccupsMicros = Collections.synchronizedList(new ArrayList<>());
    private final Thread thread;
    private volatile boolean running = true;

    public PauseDetector() {
        this.thread = new Thread(this::loop, "pause-detector");
        thread.setDaemon(true);
        thread.start();
    }

    private void loop() {
        while (running) {
            long start = System.nanoTime();
            Say.sleep(TICK_MILLIS);
            long overshootMicros = ((System.nanoTime() - start) / 1_000L) - (TICK_MILLIS * 1_000L);
            if (overshootMicros > 0) {
                hiccupsMicros.add(overshootMicros);
            }
        }
    }

    public void report(String title) {
        List<Long> samples;
        synchronized (hiccupsMicros) {
            samples = new ArrayList<>(hiccupsMicros);
        }
        if (samples.isEmpty()) {
            Say.line("%s: no measurable stalls", title);
            return;
        }
        Collections.sort(samples);
        Say.line("%s: %d stalls | p50 %.1f ms | p99 %.1f ms | worst %.1f ms | over 20 ms: %d",
                title, samples.size(),
                percentileMillis(samples, 50), percentileMillis(samples, 99),
                samples.get(samples.size() - 1) / 1000.0,
                samples.stream().filter(micros -> micros > 20_000).count());
    }

    private static double percentileMillis(List<Long> sorted, int percentile) {
        int index = Math.min(sorted.size() - 1, (int) Math.ceil(sorted.size() * percentile / 100.0) - 1);
        return sorted.get(Math.max(0, index)) / 1000.0;
    }

    @Override
    public void close() {
        running = false;
        thread.interrupt();
    }
}

package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.Say;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Answers: strong vs soft vs weak vs phantom references, and when would you use each?
 * <p>
 * Run this with a small heap (-Xmx64m) so the soft reference actually faces memory pressure;
 * with a large heap it will simply never be cleared, which is itself the lesson.
 */
public class Step06ReferenceTypes implements GcLabStep {

    @Override
    public String id() {
        return "step06";
    }

    @Override
    public String question() {
        return "How do soft, weak and phantom references differ, and where would you use them?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms64m -Xmx64m";
    }

    @Override
    public void run(String[] args) {
        Say.section("Weak: cleared at the first collection once only weakly reachable");
        Object weakTarget = Garbage.payload(Garbage.mb(1));
        WeakReference<Object> weak = new WeakReference<>(weakTarget);
        Garbage.requestFullGcForDemoOnly();
        Say.line("  with a strong reference still held : %s", state(weak.get()));
        weakTarget = null;
        Garbage.requestFullGcForDemoOnly();
        Say.line("  after dropping the strong reference: %s", state(weak.get()));

        Say.section("Soft: survives collections until the heap is actually under pressure");
        SoftReference<byte[]> soft = new SoftReference<>(Garbage.payload(Garbage.mb(4)));
        Garbage.requestFullGcForDemoOnly();
        Say.line("  after a full GC with free heap       : %s", state(soft.get()));
        Say.line("  now filling the heap to create pressure...");
        Say.line("  cleared under pressure               : %s", applyMemoryPressure(soft));

        Say.section("Phantom: get() is always null, its only job is post-mortem notification");
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        Object phantomTarget = Garbage.payload(Garbage.kb(512));
        PhantomReference<Object> phantom = new PhantomReference<>(phantomTarget, queue);
        Say.line("  get() while the object is alive      : %s", state(phantom.get()));
        phantomTarget = null;
        Garbage.requestFullGcForDemoOnly();
        Say.line("  enqueued after collection            : %s", queue.poll() != null);

        Say.section("WeakHashMap: entries disappear with their keys");
        Map<Object, String> cache = new WeakHashMap<>();
        Object key = new Object();
        cache.put(key, "cached-value");
        cache.put(new Object(), "orphan-value");
        Garbage.requestFullGcForDemoOnly();
        Say.line("  entries left after GC: %d (the one whose key we still hold)", cache.size());

        Say.takeaway(
                "Strong is the default and the only one that guarantees retention.",
                "Soft suits memory-sensitive caches: the JVM clears it rather than throwing OutOfMemoryError, but you cannot predict when.",
                "Weak suits canonical maps and metadata keyed by another object's lifetime — WeakHashMap and ThreadLocalMap keys use it.",
                "Phantom replaces finalize() for native cleanup: no resurrection, and Cleaner is the modern API on top of it.",
                "In a Spring service I would reach for a bounded Caffeine cache before soft references, because eviction you control beats eviction the GC guesses.");
    }

    /** Allocates until the soft reference is cleared, then releases everything. */
    private String applyMemoryPressure(SoftReference<byte[]> soft) {
        List<byte[]> ballast = new ArrayList<>();
        try {
            for (int i = 0; i < 4096; i++) {
                ballast.add(new byte[Garbage.mb(1)]);
                if (soft.get() == null) {
                    return "CLEARED after " + (i + 1) + " MB of pressure";
                }
            }
            return "still alive (heap too large for this demo — try -Xmx64m)";
        } catch (OutOfMemoryError e) {
            return soft.get() == null
                    ? "CLEARED just before OutOfMemoryError"
                    : "OutOfMemoryError reached with the reference still set";
        } finally {
            ballast.clear();
        }
    }

    private String state(Object referent) {
        return referent == null ? "null (cleared)" : "present";
    }
}

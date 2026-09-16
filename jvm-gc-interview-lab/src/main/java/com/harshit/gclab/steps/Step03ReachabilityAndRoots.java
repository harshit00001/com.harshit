package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.Say;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * Answers: what are GC roots, and does the JVM use reference counting?
 * <p>
 * A {@link WeakReference} is the measuring instrument: after a collection it reads {@code null}
 * only if the referent was unreachable, so it proves collection without guessing.
 */
public class Step03ReachabilityAndRoots implements GcLabStep {

    /** A static field is a GC root, which is exactly why static caches leak. */
    private static Node staticRoot;

    @Override
    public String id() {
        return "step03";
    }

    @Override
    public String question() {
        return "What are GC roots, and how does the JVM handle objects that reference each other?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms64m -Xmx64m";
    }

    @Override
    public void run(String[] args) {
        Say.section("Case 1: reachable only from a local variable");
        Node local = new Node("local");
        WeakReference<Node> watchLocal = new WeakReference<>(local);
        local = null;
        Garbage.requestFullGcForDemoOnly();
        report("dropped local variable", watchLocal);

        Say.section("Case 2: reachable from a static field (a GC root)");
        staticRoot = new Node("static");
        WeakReference<Node> watchStatic = new WeakReference<>(staticRoot);
        Garbage.requestFullGcForDemoOnly();
        report("still referenced by static field", watchStatic);
        staticRoot = null;
        Garbage.requestFullGcForDemoOnly();
        report("after clearing the static field", watchStatic);

        Say.section("Case 3: island of isolation (two objects referencing each other)");
        ReferenceQueue<Node> queue = new ReferenceQueue<>();
        Node first = new Node("first");
        Node second = new Node("second");
        first.peer = second;
        second.peer = first;
        WeakReference<Node> watchFirst = new WeakReference<>(first, queue);
        first = null;
        second = null;
        Garbage.requestFullGcForDemoOnly();
        report("mutually referencing pair, unreachable from any root", watchFirst);
        Say.line("  reference queue delivered a notification: %s", queue.poll() != null);

        Say.takeaway(
                "GC roots are live thread stacks, static fields, JNI references, active threads and monitors.",
                "Reachability from a root decides life, so a reference cycle with no root is still collected — the JVM does not reference-count.",
                "Static fields being roots is the mechanism behind most real leaks: a static Map keeps everything it holds alive for the process lifetime.",
                "WeakReference plus ReferenceQueue is how you prove collection in a test instead of hoping.");
    }

    private void report(String label, WeakReference<Node> watch) {
        Node value = watch.get();
        Say.line("  %-52s -> %s", label, value == null ? "COLLECTED" : "STILL ALIVE (" + value.name + ")");
    }

    private static final class Node {
        private final String name;
        private final byte[] weight = new byte[64 * 1024];
        private Node peer;

        private Node(String name) {
            this.name = name;
        }
    }
}

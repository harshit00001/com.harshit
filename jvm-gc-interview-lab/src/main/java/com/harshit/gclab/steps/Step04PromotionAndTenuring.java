package com.harshit.gclab.steps;

import com.harshit.gclab.GcLabStep;
import com.harshit.gclab.support.Garbage;
import com.harshit.gclab.support.GcStats;
import com.harshit.gclab.support.Say;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Answers: how does an object get promoted to the old generation?
 * <p>
 * A rolling window of "session" objects is kept alive while garbage churns around it. Each young
 * collection copies the window into a survivor space; once it has survived enough collections it is
 * tenured, and the old generation grows even though nothing leaked.
 */
public class Step04PromotionAndTenuring implements GcLabStep {

    private static final int WINDOW_SIZE = 60;
    private static final int ROUNDS = 12;

    @Override
    public String id() {
        return "step04";
    }

    @Override
    public String question() {
        return "How do survivor spaces and tenuring move an object into the old generation?";
    }

    @Override
    public String suggestedFlags() {
        return "-Xms128m -Xmx128m -Xlog:gc,gc+age=trace";
    }

    @Override
    public void run(String[] args) {
        Say.line("MaxTenuringThreshold for this JVM: %s young collections",
                com.harshit.gclab.support.Jvm.flag("MaxTenuringThreshold"));
        Say.line("Holding a rolling window of %d x 256 KB objects while churning garbage around it.%n",
                WINDOW_SIZE);

        Deque<byte[]> liveWindow = new ArrayDeque<>();
        GcStats start = GcStats.take();

        Say.line("%6s %14s %14s %14s %12s", "ROUND", "SURVIVOR", "OLD GEN", "OLD AFTER GC", "YOUNG GCs");
        for (int round = 1; round <= ROUNDS; round++) {
            for (int i = 0; i < WINDOW_SIZE; i++) {
                liveWindow.addLast(Garbage.payload(Garbage.kb(256)));
                if (liveWindow.size() > WINDOW_SIZE) {
                    liveWindow.removeFirst();
                }
                Garbage.churn(8, Garbage.mb(1));
            }
            GcStats now = GcStats.take();
            Say.line("%6d %14s %14s %14s %12d", round,
                    Say.bytes(now.poolUsed("Survivor")),
                    Say.bytes(now.poolUsed("Old Gen")),
                    Say.bytes(now.oldGenLiveAfterGc()),
                    now.collections() - start.collections());
        }

        Say.blank();
        Say.line("window still referenced at the end: %d objects (%s)",
                liveWindow.size(), Say.bytes((long) liveWindow.size() * Garbage.kb(256)));
        GcStats.printDelta("Promotion workload", start, GcStats.take());

        Say.takeaway(
                "Young collection copies survivors between the two survivor spaces and increments their age.",
                "Objects older than MaxTenuringThreshold are promoted to old gen — normal behaviour for anything request-spanning, like a cache entry or an open session.",
                "Old gen growing is not automatically a leak: what matters is whether it plateaus after collections.",
                "If survivor space is too small, objects are promoted early (premature promotion) and old collections get more frequent — check -Xlog:gc+age=trace.");
    }
}

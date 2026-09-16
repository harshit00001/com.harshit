package com.harshit.gclab;

import com.harshit.gclab.steps.Step01WhichCollector;
import com.harshit.gclab.steps.Step02EdenAndMinorGc;
import com.harshit.gclab.steps.Step03ReachabilityAndRoots;
import com.harshit.gclab.steps.Step04PromotionAndTenuring;
import com.harshit.gclab.steps.Step05MinorVsFullGc;
import com.harshit.gclab.steps.Step06ReferenceTypes;
import com.harshit.gclab.steps.Step07LeakVsAllocationPressure;
import com.harshit.gclab.steps.Step08ThreadLocalLeak;
import com.harshit.gclab.steps.Step09HumongousObjects;
import com.harshit.gclab.steps.Step10StopTheWorldPauses;
import com.harshit.gclab.steps.Step11OffHeapAndOomKill;
import com.harshit.gclab.steps.Step12EscapeAnalysis;
import com.harshit.gclab.support.Jvm;
import com.harshit.gclab.support.Say;

import java.util.Arrays;
import java.util.List;

/**
 * Entry point: {@code java -cp target/classes com.harshit.gclab.GcLab <stepId> [stepArgs...]}.
 * <p>
 * Each step needs different JVM flags to show its effect, which is why the steps are separate runs
 * rather than one long program. {@code run.ps1} wires the flags for you.
 */
public final class GcLab {

    private static final List<GcLabStep> STEPS = List.of(
            new Step01WhichCollector(),
            new Step02EdenAndMinorGc(),
            new Step03ReachabilityAndRoots(),
            new Step04PromotionAndTenuring(),
            new Step05MinorVsFullGc(),
            new Step06ReferenceTypes(),
            new Step07LeakVsAllocationPressure(),
            new Step08ThreadLocalLeak(),
            new Step09HumongousObjects(),
            new Step10StopTheWorldPauses(),
            new Step11OffHeapAndOomKill(),
            new Step12EscapeAnalysis());

    private GcLab() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || "--list".equals(args[0])) {
            printCatalog();
            return;
        }

        GcLabStep step = STEPS.stream()
                .filter(candidate -> candidate.id().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);
        if (step == null) {
            Say.line("Unknown step '%s'. Run with --list to see the catalog.", args[0]);
            return;
        }

        Say.banner(step.id().toUpperCase() + " — " + step.question());
        Say.line("suggested flags: %s", step.suggestedFlags());
        Jvm.printIdentity();
        step.run(Arrays.copyOfRange(args, 1, args.length));
        Say.blank();
    }

    private static void printCatalog() {
        Say.banner("JVM GC interview lab — 12 questions, each answered by measurement");
        Jvm.printIdentity();
        Say.blank();
        for (GcLabStep step : STEPS) {
            Say.line("%-8s %s", step.id(), step.question());
            Say.line("%-8s flags: %s", "", step.suggestedFlags());
        }
        Say.blank();
        Say.line("Run one step:  .\\run.ps1 step07 leak");
        Say.line("Or directly :  java -Xmx96m -Xlog:gc -cp target/classes com.harshit.gclab.GcLab step07 leak");
    }
}

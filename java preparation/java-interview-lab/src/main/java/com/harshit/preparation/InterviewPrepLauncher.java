package com.harshit.preparation;

import com.harshit.preparation.coding.CodingProblemsDemo;
import com.harshit.preparation.collections.HashMapInternalsDemo;
import com.harshit.preparation.collections.SetAndCustomKeyDemo;
import com.harshit.preparation.compare.ComparableComparatorDemo;
import com.harshit.preparation.concurrency.CallableRunnableDemo;
import com.harshit.preparation.concurrency.SynchronizedCounterDemo;
import com.harshit.preparation.jvm.MemoryOverviewDemo;
import com.harshit.preparation.oop.StaticHidingDemo;
import com.harshit.preparation.oop.defaults.DiamondDefaultMethodDemo;
import com.harshit.preparation.oop.polymorphism.CheckoutDemo;
import com.harshit.preparation.patterns.StrategyFactoryDemo;
import com.harshit.preparation.serialization.ImmutablePersonDemo;
import com.harshit.preparation.string.StringPoolDemo;
import com.harshit.preparation.streams.StreamBasicsDemo;

/**
 * <h2>Java 17 interview lab — entry point</h2>
 * <p>
 * Run: {@code mvn -q exec:java} from {@code java-interview-lab}, or run this class from the IDE.
 * <p>
 * Each {@code *Demo} class is self-contained: read the class-level Javadoc and {@code main} output.
 * Topics mirror {@code ../01-..19-} interview folders (Markdown).
 */
public final class InterviewPrepLauncher {

    public static void main(String[] args) {
        System.out.println("=== Java 17 Interview Lab ===\n");

        HashMapInternalsDemo.main(new String[0]);
        System.out.println();
        SetAndCustomKeyDemo.main(new String[0]);
        System.out.println();
        StringPoolDemo.main(new String[0]);
        System.out.println();
        ImmutablePersonDemo.main(new String[0]);
        System.out.println();
        ComparableComparatorDemo.main(new String[0]);
        System.out.println();
        MemoryOverviewDemo.main(new String[0]);
        System.out.println();
        SynchronizedCounterDemo.main(new String[0]);
        System.out.println();
        CallableRunnableDemo.main(new String[0]);
        System.out.println();
        StreamBasicsDemo.main(new String[0]);
        System.out.println();
        CheckoutDemo.main(new String[0]);
        System.out.println();
        DiamondDefaultMethodDemo.main(new String[0]);
        System.out.println();
        StaticHidingDemo.main(new String[0]);
        System.out.println();
        StrategyFactoryDemo.main(new String[0]);
        System.out.println();
        CodingProblemsDemo.main(new String[0]);

        System.out.println("\n=== Done ===");
    }

    private InterviewPrepLauncher() {
    }
}

package com.harshit.interview;

import com.harshit.interview.common.InterviewDemo;
import com.harshit.interview.java8.Q01LambdaAndFunctionalInterfaces;
import com.harshit.interview.java8.Q02MethodReferences;
import com.harshit.interview.java8.Q03OptionalDeepDive;
import com.harshit.interview.java8.Q04StreamPipelineLazyEvaluation;
import com.harshit.interview.java8.Q05CollectorsGroupingAndPartitioning;
import com.harshit.interview.java8.Q06ParallelStreamsPitfalls;
import com.harshit.interview.java8.Q07DateTimeApi;
import com.harshit.interview.java8.Q08DefaultAndStaticInterfaceMethods;
import com.harshit.interview.java8.Q09StreamsCodingInterviewQuestions;
import com.harshit.interview.java8.Q10EmployeeStreamScenarios;
import com.harshit.interview.java8.Q11ReduceMatchAndTerminalOps;
import com.harshit.interview.java8.Q12ComparatorAndSorting;
import com.harshit.interview.java8.Q13PrimitiveStreams;
import com.harshit.interview.java8.Q14FunctionalComposition;
import com.harshit.interview.java8.Q15StreamPitfallsAndBestPractices;
import com.harshit.interview.java8.Q16StringProcessingWithStreams;
import com.harshit.interview.multithreading.Q01ThreadBasicsAndLifecycle;
import com.harshit.interview.multithreading.Q02SynchronizedVsReentrantLock;
import com.harshit.interview.multithreading.Q03VolatileVisibility;
import com.harshit.interview.multithreading.Q04WaitNotifyAndBlockingQueue;
import com.harshit.interview.multithreading.Q05ExecutorServiceAndThreadPools;
import com.harshit.interview.multithreading.Q06CallableFutureCompletableFuture;
import com.harshit.interview.multithreading.Q07ConcurrentCollections;
import com.harshit.interview.multithreading.Q08AtomicVsSynchronized;
import com.harshit.interview.multithreading.Q09CountDownLatchBarrierSemaphore;
import com.harshit.interview.multithreading.Q10DeadlockDetectionAndPrevention;
import com.harshit.interview.multithreading.Q11ThreadLocalDemo;
import com.harshit.interview.multithreading.Q12StreamsOnNumbersInterviewQuestions;
import com.harshit.interview.multithreading.Q13ReadWriteLockDemo;
import com.harshit.interview.multithreading.Q14ForkJoinPoolDemo;
import com.harshit.interview.multithreading.Q15DoubleCheckedLocking;
import com.harshit.interview.multithreading.Q16InterruptHandling;
import com.harshit.interview.multithreading.Q17ProducerConsumerBlockingQueue;
import com.harshit.interview.multithreading.Q18CompletableFutureAdvanced;
import com.harshit.interview.multithreading.Q19HappensBeforeRules;
import com.harshit.interview.multithreading.Q20RaceConditionAndFix;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Entry point — run all demos or pick one by number.
 *
 * <pre>
 *   mvn -q exec:java
 *   mvn -q exec:java -Dexec.args="java8 5"
 *   mvn -q exec:java -Dexec.args="mt 3"
 *   mvn -q exec:java -Dexec.args="all"
 * </pre>
 */
public final class InterviewPrepRunner {

    private static final Map<String, InterviewDemo> JAVA8 = new LinkedHashMap<>();
    private static final Map<String, InterviewDemo> MULTITHREADING = new LinkedHashMap<>();

    static {
        JAVA8.put("1", new Q01LambdaAndFunctionalInterfaces());
        JAVA8.put("2", new Q02MethodReferences());
        JAVA8.put("3", new Q03OptionalDeepDive());
        JAVA8.put("4", new Q04StreamPipelineLazyEvaluation());
        JAVA8.put("5", new Q05CollectorsGroupingAndPartitioning());
        JAVA8.put("6", new Q06ParallelStreamsPitfalls());
        JAVA8.put("7", new Q07DateTimeApi());
        JAVA8.put("8", new Q08DefaultAndStaticInterfaceMethods());
        JAVA8.put("9", new Q09StreamsCodingInterviewQuestions());
        JAVA8.put("10", new Q10EmployeeStreamScenarios());
        JAVA8.put("11", new Q11ReduceMatchAndTerminalOps());
        JAVA8.put("12", new Q12ComparatorAndSorting());
        JAVA8.put("13", new Q13PrimitiveStreams());
        JAVA8.put("14", new Q14FunctionalComposition());
        JAVA8.put("15", new Q15StreamPitfallsAndBestPractices());
        JAVA8.put("16", new Q16StringProcessingWithStreams());

        MULTITHREADING.put("1", new Q01ThreadBasicsAndLifecycle());
        MULTITHREADING.put("2", new Q02SynchronizedVsReentrantLock());
        MULTITHREADING.put("3", new Q03VolatileVisibility());
        MULTITHREADING.put("4", new Q04WaitNotifyAndBlockingQueue());
        MULTITHREADING.put("5", new Q05ExecutorServiceAndThreadPools());
        MULTITHREADING.put("6", new Q06CallableFutureCompletableFuture());
        MULTITHREADING.put("7", new Q07ConcurrentCollections());
        MULTITHREADING.put("8", new Q08AtomicVsSynchronized());
        MULTITHREADING.put("9", new Q09CountDownLatchBarrierSemaphore());
        MULTITHREADING.put("10", new Q10DeadlockDetectionAndPrevention());
        MULTITHREADING.put("11", new Q11ThreadLocalDemo());
        MULTITHREADING.put("12", new Q12StreamsOnNumbersInterviewQuestions());
        MULTITHREADING.put("13", new Q13ReadWriteLockDemo());
        MULTITHREADING.put("14", new Q14ForkJoinPoolDemo());
        MULTITHREADING.put("15", new Q15DoubleCheckedLocking());
        MULTITHREADING.put("16", new Q16InterruptHandling());
        MULTITHREADING.put("17", new Q17ProducerConsumerBlockingQueue());
        MULTITHREADING.put("18", new Q18CompletableFutureAdvanced());
        MULTITHREADING.put("19", new Q19HappensBeforeRules());
        MULTITHREADING.put("20", new Q20RaceConditionAndFix());
    }

    private InterviewPrepRunner() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            interactiveMenu();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "all" -> runAll();
            case "java8" -> runOne(JAVA8, args.length > 1 ? args[1] : null);
            case "mt", "multithreading" -> runOne(MULTITHREADING, args.length > 1 ? args[1] : null);
            default -> {
                System.out.println("Usage: java InterviewPrepRunner [all|java8 [n]|mt [n]]");
                printCatalog();
            }
        }
    }

    private static void interactiveMenu() throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("""
                        
                        ===== Java 8 & Multithreading Interview Prep (Accenture 4 YOE) =====
                        1) Run ALL demos
                        2) Java 8 catalog
                        3) Multithreading catalog
                        4) Run single Java 8 demo (enter id)
                        5) Run single Multithreading demo (enter id)
                        0) Exit
                        Choice:""");
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> runAll();
                    case "2" -> printSection("Java 8", JAVA8);
                    case "3" -> printSection("Multithreading", MULTITHREADING);
                    case "4" -> {
                        System.out.print("Java 8 demo id: ");
                        runOne(JAVA8, scanner.nextLine().trim());
                    }
                    case "5" -> {
                        System.out.print("Multithreading demo id: ");
                        runOne(MULTITHREADING, scanner.nextLine().trim());
                    }
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        }
    }

    private static void runAll() throws Exception {
        System.out.println("\n########## JAVA 8 ##########");
        for (InterviewDemo demo : JAVA8.values()) {
            runDemo(demo);
        }
        System.out.println("\n########## MULTITHREADING ##########");
        for (InterviewDemo demo : MULTITHREADING.values()) {
            runDemo(demo);
        }
    }

    private static void runOne(Map<String, InterviewDemo> catalog, String id) throws Exception {
        if (id == null || id.isBlank()) {
            printSection("Catalog", catalog);
            return;
        }
        InterviewDemo demo = catalog.get(id);
        if (demo == null) {
            System.out.println("Unknown id: " + id);
            printSection("Catalog", catalog);
            return;
        }
        runDemo(demo);
    }

    private static void runDemo(InterviewDemo demo) throws Exception {
        System.out.println("\n>>> " + demo.getClass().getSimpleName());
        demo.run();
        System.out.println("--- done ---");
    }

    private static void printCatalog() {
        printSection("Java 8", JAVA8);
        printSection("Multithreading", MULTITHREADING);
    }

    private static void printSection(String title, Map<String, InterviewDemo> catalog) {
        System.out.println("\n--- " + title + " ---");
        catalog.forEach((id, demo) ->
                System.out.println("  " + id + ") " + demo.getClass().getSimpleName()));
    }
}

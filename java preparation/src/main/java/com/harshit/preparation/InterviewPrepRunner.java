package com.harshit.preparation;

import com.harshit.preparation.topic01.Topic01Qa;
import com.harshit.preparation.topic02.Topic02Qa;
import com.harshit.preparation.topic03.Topic03Qa;
import com.harshit.preparation.topic04.Topic04Qa;
import com.harshit.preparation.topic05.Topic05Qa;
import com.harshit.preparation.topic06.Topic06Qa;
import com.harshit.preparation.topic07.Topic07Qa;
import com.harshit.preparation.topic08.Topic08Qa;
import com.harshit.preparation.topic09.Topic09Qa;
import com.harshit.preparation.topic10.Topic10Qa;
import com.harshit.preparation.topic11.Topic11Qa;
import com.harshit.preparation.topic12.Topic12Qa;
import com.harshit.preparation.topic13.Topic13Qa;
import com.harshit.preparation.topic14.Topic14Qa;
import com.harshit.preparation.topic15.Topic15Qa;
import com.harshit.preparation.topic16.Topic16Qa;
import com.harshit.preparation.topic17.Topic17Qa;
import com.harshit.preparation.topic18.Topic18Qa;
import com.harshit.preparation.topic19.Topic19Qa;

import java.util.concurrent.ExecutionException;

/**
 * Runs the small code demos embedded in {@code TopicNNQa} classes so you can compile and verify output.
 *
 * <p>Usage:
 *
 * <pre>
 *   mvn -q compile exec:java
 *   mvn -q exec:java -Dexec.args="01"
 *   mvn -q exec:java -Dexec.args="all"
 * </pre>
 *
 * Or from the IDE: run this class with program arguments {@code all} or {@code 04}, etc.
 */
public final class InterviewPrepRunner {

    private InterviewPrepRunner() {
    }

    public static void main(String[] args) {
        String which = args.length == 0 ? "all" : args[0].trim();
        if (which.equalsIgnoreCase("help") || which.equals("-h") || which.equals("--help")) {
            printUsage();
            return;
        }
        if (which.equalsIgnoreCase("all")) {
            for (int n = 1; n <= 19; n++) {
                runTopic(String.format("%02d", n));
            }
            return;
        }
        runTopic(which);
    }

    private static void printUsage() {
        System.out.println("InterviewPrepRunner — run embedded TopicNNQa demos.");
        System.out.println("  (no args)  — same as 'all'");
        System.out.println("  all        — topics 01–19 (each TopicNNQa.demo())");
        System.out.println("  01 .. 19   — one topic");
    }

    private static void runTopic(String id) {
        banner(id);
        try {
            switch (id) {
                case "01" -> Topic01Qa.examples();
                case "02" -> Topic02Qa.demoPolymorphism();
                case "03" -> Topic03Qa.demo();
                case "04" -> Topic04Qa.demo();
                case "05" -> Topic05Qa.demo();
                case "06" -> Topic06Qa.demo();
                case "07" -> Topic07Qa.heapSnapshot();
                case "08" -> Topic08Qa.callableDemo();
                case "09" -> Topic09Qa.demo();
                case "10" -> Topic10Qa.demo();
                case "11" -> Topic11Qa.demo();
                case "12" -> Topic12Qa.demo();
                case "13" -> Topic13Qa.demo();
                case "14" -> Topic14Qa.demo();
                case "15" -> Topic15Qa.demo();
                case "16" -> Topic16Qa.demo();
                case "17" -> Topic17Qa.demo();
                case "18" -> Topic18Qa.demo();
                case "19" -> Topic19Qa.demo();
                default -> System.out.println("Unknown topic: " + id + " — use 01..19 or all.");
            }
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Topic " + id + " failed: " + e.getMessage());
        }
    }

    private static void banner(String id) {
        System.out.println();
        System.out.println("========== Topic " + id + " ==========");
    }
}

package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * INTERVIEW Q: What changed in Java 8? Explain lambda and functional interfaces.
 *
 * <p><b>4 YOE / Accenture angle:</b> You should connect syntax to JDK types — not only "arrow functions".
 *
 * <p><b>Lambda</b> = anonymous function implementing a <b>functional interface</b> (exactly one abstract method).
 * Compiler uses <b>invokedynamic</b> + LambdaMetafactory (not a new class per lambda at runtime in most cases).
 *
 * <p><b>Common @FunctionalInterface types:</b>
 * <ul>
 *   <li>{@link Predicate} — test → boolean (filter)</li>
 *   <li>{@link Function} — T → R (map)</li>
 *   <li>{@link Consumer} — T → void (forEach)</li>
 *   <li>{@link Supplier} — () → T (lazy creation)</li>
 *   <li>{@link BiFunction} — (T,U) → R (reduce, merge)</li>
 * </ul>
 *
 * <p><b>Interview trap:</b> Lambda can capture effectively final locals; it cannot reassign outer variables.
 */
public final class Q01LambdaAndFunctionalInterfaces implements InterviewDemo {

    @FunctionalInterface
    interface DiscountPolicy {
        double apply(double price);
    }

    @Override
    public void run() {
        System.out.println("=== Q01: Lambda & Functional Interfaces ===\n");

        // Before Java 8: anonymous inner class
        DiscountPolicy anonymous = new DiscountPolicy() {
            @Override
            public double apply(double price) {
                return price * 0.90;
            }
        };

        // Java 8: lambda — same SAM (Single Abstract Method)
        DiscountPolicy lambda = price -> price * 0.90;

        double price = 1_000;
        System.out.println("Anonymous discount: " + anonymous.apply(price));
        System.out.println("Lambda discount:    " + lambda.apply(price));

        // Predicate — used in Stream.filter
        Predicate<String> longWord = s -> s.length() > 5;
        System.out.println("'Accenture'.longWord? " + longWord.test("Accenture"));

        // Function — used in Stream.map
        Function<String, Integer> lengthFn = String::length;
        System.out.println("Length of Java: " + lengthFn.apply("Java"));

        // Consumer — side effect, no return
        Consumer<String> log = msg -> System.out.println("LOG >> " + msg);
        log.accept("Pipeline started");

        // Supplier — lazy / deferred creation (used in Optional.orElseGet)
        Supplier<String> idSupplier = () -> "REQ-" + System.currentTimeMillis();
        System.out.println("Generated id: " + idSupplier.get());

        // Method reference forms (preview — see Q02 for full breakdown)
        Comparator<String> byLength = Comparator.comparingInt(String::length);
        System.out.println("Shorter word first: "
                + byLength.compare("Streams", "Java"));

        System.out.println("\n→ Accenture follow-up: When NOT to use lambda? "
                + "When you need multiple methods, stateful anonymous class, or clearer stack traces in legacy code.");
    }

    public static void main(String[] args) throws Exception {
        new Q01LambdaAndFunctionalInterfaces().run();
    }
}

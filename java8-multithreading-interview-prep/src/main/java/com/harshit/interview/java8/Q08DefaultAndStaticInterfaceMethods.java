package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.Comparator;
import java.util.List;

/**
 * INTERVIEW Q: Default and static methods in interfaces — why added in Java 8?
 *
 * <p><b>Problem:</b> Adding method to interface broke all implementors (binary compatibility).
 *
 * <p><b>Solution:</b> default methods with body + static helper methods on interface.
 *
 * <p><b>Multiple inheritance conflict:</b> Class must override; use InterfaceName.super.method().
 */
public final class Q08DefaultAndStaticInterfaceMethods implements InterviewDemo {

    interface Notifier {
        void send(String message);

        // default — backward compatible evolution
        default void sendWithPrefix(String message) {
            send("[ALERT] " + message);
        }

        static Notifier console() {
            return message -> System.out.println("STATIC factory: " + message);
        }
    }

    static class EmailNotifier implements Notifier {
        @Override
        public void send(String message) {
            System.out.println("EMAIL: " + message);
        }
    }

    @Override
    public void run() {
        System.out.println("=== Q08: Default & Static Interface Methods ===\n");

        Notifier email = new EmailNotifier();
        email.send("Plain message");
        email.sendWithPrefix("Server down"); // uses default method

        Notifier console = Notifier.console();
        console.send("From static factory");

        // Comparator default methods — huge Java 8 win
        List<String> words = List.of("streams", "java", "lambda");
        words.stream()
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);

        System.out.println("\n→ Also powers Iterable.forEach, Collection.stream, List.sort, etc.");
    }

    public static void main(String[] args) throws Exception {
        new Q08DefaultAndStaticInterfaceMethods().run();
    }
}

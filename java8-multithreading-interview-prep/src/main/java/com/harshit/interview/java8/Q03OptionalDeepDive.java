package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * INTERVIEW Q: What is Optional? Best practices and anti-patterns?
 *
 * <p><b>Purpose:</b> Explicit API contract — "value may be absent" without null checks everywhere.
 *
 * <p><b>DO:</b> Return Optional from methods where absence is valid; chain with map/flatMap/filter.
 *
 * <p><b>DON'T:</b>
 * <ul>
 *   <li>Use Optional as field type or method parameter (usually)</li>
 *   <li>Call {@code get()} without checking (prefer orElseThrow / ifPresent)</li>
 *   <li>Use {@code orElse(expensive())} when you mean {@code orElseGet(() -> expensive())}</li>
 * </ul>
 *
 * <p><b>4 YOE depth:</b> Optional is a <b>container</b>, not a replacement for null in entire codebase.
 */
public final class Q03OptionalDeepDive implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q03: Optional Deep Dive ===\n");

        Optional<String> present = Optional.of("Accenture");
        Optional<String> empty = Optional.empty();

        // Safe consumption
        present.ifPresent(v -> System.out.println("Present: " + v));
        System.out.println("Empty or default: " + empty.orElse("N/A"));

        // orElse vs orElseGet — lazy vs eager
        System.out.println("orElse (eager — supplier runs even if value present): "
                + present.orElse(expensiveDefault()));
        System.out.println("orElseGet (lazy — supplier only if empty): "
                + present.orElseGet(this::expensiveDefault));

        // map / flatMap — avoid nested Optional
        Optional<Integer> length = present.map(String::length);
        System.out.println("Mapped length: " + length.orElse(-1));

        Optional<String> flat = present.flatMap(this::findDepartmentCode);
        System.out.println("Department code: " + flat.orElse("UNKNOWN"));

        // filter
        Optional<String> filtered = present.filter(s -> s.startsWith("Acc"));
        System.out.println("Filtered: " + filtered.orElse("filtered out"));

        // orElseThrow — production style for mandatory values
        try {
            empty.orElseThrow(() -> new NoSuchElementException("Employee not found"));
        } catch (NoSuchElementException e) {
            System.out.println("Caught expected: " + e.getMessage());
        }

        System.out.println("\n→ Java 9+ adds ifPresentOrElse, stream(), or — know they exist for senior rounds.");
    }

    private String expensiveDefault() {
        System.out.println("  [expensiveDefault() invoked]");
        return "DEFAULT";
    }

    private Optional<String> findDepartmentCode(String name) {
        return Optional.of("DEPT-" + name.substring(0, 3).toUpperCase());
    }

    public static void main(String[] args) throws Exception {
        new Q03OptionalDeepDive().run();
    }
}

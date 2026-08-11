package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * INTERVIEW Q: How do you compose Predicate and Function? (and, or, negate, compose, andThen)
 *
 * <p><b>Accenture scenario:</b> Validate request — active user AND valid email AND age &gt;= 18.
 */
public final class Q14FunctionalComposition implements InterviewDemo {

    record User(String email, int age, boolean active) {}

    @Override
    public void run() {
        System.out.println("=== Q14: Functional Composition ===\n");

        Predicate<User> active = User::active;
        Predicate<User> adult = u -> u.age() >= 18;
        Predicate<User> corporateEmail = u -> u.email() != null && u.email().endsWith("@accenture.com");

        Predicate<User> validUser = active.and(adult).and(corporateEmail);

        User ok = new User("raj@accenture.com", 28, true);
        User bad = new User("raj@gmail.com", 17, true);

        System.out.println("Valid user? " + validUser.test(ok));
        System.out.println("Invalid user? " + validUser.test(bad));

        Predicate<User> notActive = active.negate();
        System.out.println("Not active (ok user): " + notActive.test(ok));

        // Function pipeline: extract domain → uppercase
        Function<User, String> toDomain = u -> u.email().substring(u.email().indexOf('@') + 1);
        Function<User, String> pipeline = toDomain.andThen(String::toUpperCase);
        System.out.println("Domain: " + pipeline.apply(ok));

        // compose — reverse order: f.compose(g) == g.andThen(f)
        Function<Integer, Integer> times2 = x -> x * 2;
        Function<Integer, Integer> plus3 = x -> x + 3;
        Function<Integer, Integer> combined = times2.compose(plus3); // plus3 then times2 → (x+3)*2
        System.out.println("(5+3)*2 = " + combined.apply(5));

        System.out.println("\n→ Predicate composition keeps validation readable vs nested if blocks.");
    }

    public static void main(String[] args) throws Exception {
        new Q14FunctionalComposition().run();
    }
}

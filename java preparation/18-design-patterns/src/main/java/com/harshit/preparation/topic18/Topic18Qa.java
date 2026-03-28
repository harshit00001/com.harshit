package com.harshit.preparation.topic18;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

/**
 * Topic 18 — Design patterns.
 */
public final class Topic18Qa {

    private Topic18Qa() {
    }

    /*
     * Q: Strategy + Factory to replace heavy if-else?
     *
     * SCRIPT:
     * I map a discriminator—tier, country, payment type—to a strategy object or function. Adding
     * a new case means registering a new strategy, not editing a giant if chain, which follows open-closed.
     * In Spring I sometimes inject a Map of strategy beans keyed by type.
     *
     * REAL LIFE:
     * Checkout chooses payment handler by method; each handler encapsulates validation and capture logic.
     */

    public static BigDecimal applyDiscount(String tier, BigDecimal amount) {
        Map<String, Function<BigDecimal, BigDecimal>> strategies = Map.of(
                "GOLD", a -> a.multiply(new BigDecimal("0.9")),
                "STD", Function.identity()
        );
        return strategies.getOrDefault(tier.toUpperCase(), Function.identity()).apply(amount);
    }

    /*
     * Q: Singleton?
     *
     * SCRIPT:
     * Only one instance—Spring’s default bean scope is singleton for stateless services. I warn that
     * global mutable singletons hurt testability. Enum singleton is a clean pattern for true singletons
     * with serialization guarantees.
     */

    /*
     * Q: Builder pattern?
     *
     * SCRIPT:
     * When a class has many optional fields, telescoping constructors become unreadable. A builder
     * gives fluent calls and validates invariants before build(). Lombok’s @Builder is common in
     * production code for brevity.
     */

    public static void demo() {
        BigDecimal amount = new BigDecimal("100.00");
        System.out.println("STD:  " + applyDiscount("STD", amount));
        System.out.println("GOLD: " + applyDiscount("GOLD", amount));
    }

    public static void main(String[] args) {
        demo();
    }
}

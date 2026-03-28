package com.harshit.preparation.patterns;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

/**
 * <h2>Strategy + factory (replace big if-else)</h2>
 * <p>
 * Map tier string → discount function. Adding a new tier = new map entry, not editing a long if chain.
 */
public final class StrategyFactoryDemo {

    public static void main(String[] args) {
        var pricing = new PricingService(Map.of(
                "GOLD", amount -> amount.multiply(new BigDecimal("0.9")),
                "SILVER", amount -> amount.multiply(new BigDecimal("0.95")),
                "STANDARD", Function.identity()
        ));
        System.out.println(pricing.apply("GOLD", new BigDecimal("100")));
        System.out.println(pricing.apply("STANDARD", new BigDecimal("100")));
    }

    static final class PricingService {

        private final Map<String, Function<BigDecimal, BigDecimal>> strategies;

        PricingService(Map<String, Function<BigDecimal, BigDecimal>> strategies) {
            this.strategies = Map.copyOf(strategies);
        }

        BigDecimal apply(String tier, BigDecimal amount) {
            return strategies.getOrDefault(tier.toUpperCase(), Function.identity()).apply(amount);
        }
    }

    private StrategyFactoryDemo() {
    }
}

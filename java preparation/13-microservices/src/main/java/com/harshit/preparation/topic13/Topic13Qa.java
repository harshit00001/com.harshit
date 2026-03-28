package com.harshit.preparation.topic13;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Topic 13 — Microservices.
 */
public final class Topic13Qa {

    private Topic13Qa() {
    }

    /*
     * Q: What is the role of an API Gateway?
     *
     * SCRIPT:
     * The gateway is the single front door for clients. It routes requests to internal services,
     * often handles TLS termination, authentication, rate limiting, and sometimes request aggregation.
     * It hides the internal network layout so clients see one stable URL while services evolve behind it.
     */

    /*
     * Q: What is service discovery?
     *
     * SCRIPT:
     * Services register their network location with a registry; clients or the gateway resolve
     * instances dynamically instead of hard-coding IPs. In Kubernetes this is often DNS and Services,
     * elsewhere Eureka or Consul. Discovery pairs with health checks so traffic only goes to live instances.
     */

    /*
     * Q: Synchronous vs asynchronous communication?
     *
     * SCRIPT:
     * Synchronous HTTP or RPC is simple: caller waits, easy to reason about, but failures propagate
     * and coupling is higher. Asynchronous messaging decouples producers and consumers, smooths spikes,
     * and supports eventual consistency—but I must design idempotency and retries. I pick sync for
     * queries that need an immediate answer; async for notifications and long-running work.
     */

    /*
     * Q: Circuit breaker?
     *
     * SCRIPT:
     * I describe it as a safety switch: if downstream is failing repeatedly, I stop calling it for a
     * while, fail fast, and optionally return a fallback. After a cooldown, half-open probes try again.
     * Resilience4j is the modern choice in Spring; it prevents cascading outages.
     */

    /*
     * Q: Bulkhead pattern?
     *
     * SCRIPT:
     * I isolate thread pools or connection limits per dependency so one slow service cannot exhaust
     * all threads and take down unrelated features—like bulkheads on a ship containing water in one compartment.
     */

    /*
     * Q: Distributed tracing?
     *
     * SCRIPT:
     * A trace ID propagates across HTTP headers or messaging so every log line in every service
     * can be tied to one user request. I mention OpenTelemetry, Zipkin, or Jaeger—without tracing,
     * debugging microservices is guesswork.
     */

    /*
     * Q: Partial failures?
     *
     * SCRIPT:
     * I combine timeouts, retries with backoff, idempotency keys, circuit breakers, sagas with
     * compensating transactions, and dead-letter queues for async. The theme is assume failure and
     * design for recovery, not assume the happy path only.
     */

    /** Tiny circuit-breaker state machine + partition assignment (Kafka-style) — illustrates the scripts above. */
    public static void demo() {
        System.out.println("service discovery (static): " + Map.of("orders", "10.0.0.5:8080", "payments", "10.0.0.6:8080"));

        List<String> consumers = List.of("c1", "c2");
        List<Integer> partitions = List.of(0, 1, 2, 3);
        Map<String, List<Integer>> assignment = assignPartitionsRoundRobin(consumers, partitions);
        System.out.println("Kafka consumer group assignment (same group, one consumer per partition max): " + assignment);

        CircuitBreaker cb = new CircuitBreaker(3);
        for (int i = 0; i < 6; i++) {
            if (i == 4) {
                cb.reset();
                System.out.println("(reset breaker — e.g. after cooldown / ops fix)");
            }
            final int step = i;
            String outcome = cb.call(() -> {
                if (step < 3) {
                    throw new IllegalStateException("downstream");
                }
                return "ok";
            });
            System.out.println("call " + i + " → " + outcome + " state=" + cb.state);
        }
    }

    static Map<String, List<Integer>> assignPartitionsRoundRobin(List<String> consumerIds, List<Integer> parts) {
        Map<String, List<Integer>> out = new LinkedHashMap<>();
        for (String c : consumerIds) {
            out.put(c, new ArrayList<>());
        }
        for (int i = 0; i < parts.size(); i++) {
            String c = consumerIds.get(i % consumerIds.size());
            out.get(c).add(parts.get(i));
        }
        return out;
    }

    enum State {
        CLOSED,
        OPEN
    }

    static final class CircuitBreaker {
        private final int failureThreshold;
        private State state = State.CLOSED;
        private final AtomicInteger failures = new AtomicInteger();

        CircuitBreaker(int failureThreshold) {
            this.failureThreshold = failureThreshold;
        }

        String call(java.util.concurrent.Callable<String> downstream) {
            if (state == State.OPEN) {
                return "fail-fast (circuit OPEN)";
            }
            try {
                String r = downstream.call();
                failures.set(0);
                state = State.CLOSED;
                return r;
            } catch (Exception e) {
                if (failures.incrementAndGet() >= failureThreshold) {
                    state = State.OPEN;
                }
                return "error → " + e.getMessage();
            }
        }

        void reset() {
            state = State.CLOSED;
            failures.set(0);
        }
    }

    public static void main(String[] args) {
        demo();
    }
}

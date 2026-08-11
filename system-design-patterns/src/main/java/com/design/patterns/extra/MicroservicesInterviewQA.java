package com.design.patterns.extra;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Microservices interview Q&A — under system-design-patterns (distributed architecture focus).
 *
 * <p><b>Q. Microservices?</b> Small, independently deployable services owned by teams, communicating over
 * the network. Real life: checkout service separate from catalog service so teams ship at different cadences.
 *
 * <p><b>Q. Monolith vs microservices?</b> Monolith: single deployable; simpler early, harder to scale teams/parts.
 * Microservices: operational complexity (network, observability) for agility and isolation.
 *
 * <p><b>Q. API Gateway?</b> Edge entry for auth, routing, rate limits, SSL termination. Real life: Kong/AWS
 * API Gateway fronts dozens of internal services.
 *
 * <p><b>Q. Service discovery?</b> Services register and lookup instances (Eureka, Consul, k8s DNS). Real life:
 * dynamic scaling adds pods; clients resolve current IPs.
 *
 * <p><b>Q. Load balancing?</b> Distributes traffic across healthy instances (round-robin, least-conn).
 * Real life: ALB spreads HTTP across service replicas.
 *
 * <p><b>Q. Circuit breaker?</b> Stop calling failing dependencies; fail fast; periodic retry. Real life:
 * if ratings API is down, product page degrades gracefully instead of hanging threads.
 *
 * <p><b>Q. Feign client?</b> Declarative HTTP client in Spring Cloud (interfaces + annotations). Real life:
 * type-safe calls between services with retries/timeouts configured centrally.
 *
 * <p><b>Q. Inter-service communication?</b> Sync REST/gRPC; async messaging (Kafka/RabbitMQ); hybrid. Choose
 * based on coupling, latency, consistency needs.
 *
 * <p>Tiny in-memory “service registry” toy (not production — illustrates discovery idea).
 */
public final class MicroservicesInterviewQA {

    private MicroservicesInterviewQA() {
    }

    static final class ServiceRegistry {
        private final Map<String, String> nameToUrl = new HashMap<>();

        void register(String name, String baseUrl) {
            nameToUrl.put(Objects.requireNonNull(name), Objects.requireNonNull(baseUrl));
        }

        String resolve(String name) {
            return nameToUrl.get(name);
        }
    }

    public static void main(String[] args) {
        ServiceRegistry registry = new ServiceRegistry();
        registry.register("billing", "http://billing.local");
        registry.register("inventory", "http://inventory.local");
        System.out.println("Discovery toy: billing -> " + registry.resolve("billing"));
    }
}

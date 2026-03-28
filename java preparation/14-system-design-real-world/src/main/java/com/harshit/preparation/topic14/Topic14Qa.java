package com.harshit.preparation.topic14;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Topic 14 — System design, production, caching, Saga.
 */
public final class Topic14Qa {

    private Topic14Qa() {
    }

    /*
     * Q: API Gateway and authentication flow?
     *
     * SCRIPT:
     * The client talks only to the gateway. The gateway validates JWT or session, may enrich the
     * request with user or tenant headers, and forwards to internal services over a trusted network.
     * Services trust the gateway or validate tokens themselves depending on zero-trust design—either
     * way the user never holds internal service URLs.
     */

    /*
     * Q: SAGA and distributed transactions?
     *
     * SCRIPT:
     * I explain that there is no single ACID transaction across microservice databases. A saga is a
     * sequence of local transactions with compensating actions—if payment succeeded but shipping fails,
     * I run a compensating refund or cancel order step. The system becomes eventually consistent, and
     * I design idempotency and monitoring around that reality.
     */

    /*
     * Q: Cache-aside with Redis?
     *
     * SCRIPT:
     * On read I try Redis first; on miss I load from the database, populate Redis with a TTL, and
     * return. On update I either write through or invalidate the cache so readers do not see stale
     * data forever. I mention cache stampede mitigation and that Redis is not the source of truth—
     * the database is.
     */

    /*
     * Q: Consistency across two JVM instances?
     *
     * SCRIPT:
     * JVMs do not share heap. If one instance updates data, others learn through a shared store—
     * typically the database—or a pub/sub channel that tells everyone to invalidate caches. Sticky
     * sessions only help with user session, not with business data visible to all users.
     */

    /*
     * Q: High traffic in production?
     *
     * SCRIPT:
     * I start with measurement—APM, logs, DB slow queries. Then horizontal scaling, autoscaling,
     * CDN for static assets, read replicas, caching, async offload, and tuning connection pools. I
     * never throw hardware at a problem before understanding whether the database or a hot loop is the bottleneck.
     */

    /*
     * Q: Alerts for 500 errors every five minutes?
     *
     * SCRIPT:
     * I define SLOs and alert on error rate or burn rate, not a single failure. Tools like Datadog,
     * Prometheus, or ELK can page on-call when thresholds breach. The alert should link to dashboards
     * and recent deploys so triage is fast.
     */

    /*
     * Q: Duplicate payments?
     *
     * SCRIPT:
     * I use idempotency keys from the client, unique constraints in the database, and return the
     * same payment result if the client retries. Double-clicks are a real scenario—design for them.
     */

    /*
     * Q: Zero data loss?
     *
     * SCRIPT:
     * Durable messaging with acknowledgements, database replication, Kafka min in-sync replicas,
     * backups and point-in-time recovery. “Zero loss” is a business requirement that drives cost—I
     * align engineering choices to the actual RPO and RTO we need.
     */

    /*
     * Q: Update ten million records efficiently?
     *
     * SCRIPT:
     * Batch processing in chunks, key-range parallelism, JDBC batching or JPA batch size, avoid
     * loading everything into memory, run during maintenance windows if locks matter, and validate
     * with EXPLAIN plans so I do not accidentally table-scan repeatedly.
     */

    /** Cache-aside read + idempotency key (duplicate payment) — plain Java. */
    public static void demo() {
        var cache = new ConcurrentHashMap<String, String>();
        String v1 = readThroughCache(cache, "user:42", Topic14Qa::loadUserFromDb);
        String v2 = readThroughCache(cache, "user:42", Topic14Qa::loadUserFromDb);
        System.out.println("cache-aside first read=" + v1 + " second read (cached)=" + v2);

        var processed = new ConcurrentHashMap<String, String>();
        System.out.println("idempotency pay-1: " + payOnce(processed, "idemp-key-99", "99.00"));
        System.out.println("idempotency pay-1 retry: " + payOnce(processed, "idemp-key-99", "99.00"));
    }

    private static String loadUserFromDb(String key) {
        return "row(" + key + ")";
    }

    static String readThroughCache(Map<String, String> cache, String key, java.util.function.Function<String, String> loader) {
        return cache.computeIfAbsent(key, loader);
    }

    static String payOnce(Map<String, String> ledger, String idempotencyKey, String amount) {
        return ledger.computeIfAbsent(idempotencyKey, k -> "PAID_" + amount);
    }

    public static void main(String[] args) {
        demo();
    }
}

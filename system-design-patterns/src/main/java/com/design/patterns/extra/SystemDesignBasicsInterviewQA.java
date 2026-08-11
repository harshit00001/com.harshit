package com.design.patterns.extra;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic system design interview Q&A — under system-design-patterns.
 *
 * <p><b>Q. URL shortener?</b> Store mapping shortKey→longUrl; use base62 encoding of incremental id or hash;
 * handle collisions; cache hot keys (Redis); rate limit creation. Real life: bit.ly style analytics + TTL.
 *
 * <p><b>Q. Scalable system?</b> Stateless services, horizontal scale, caching, async work, DB read replicas/
 * sharding, CDN for static assets, observability. Real life: Black Friday traffic via autoscaling + cache.
 *
 * <p><b>Q. Caching?</b> Store expensive reads closer to consumers (browser/CDN/app/DB). Patterns: cache-aside,
 * write-through, TTL/invalidation. Real life: product catalog in Redis with 5-minute TTL.
 *
 * <p><b>Q. Load balancer?</b> Distributes requests; L4 vs L7; health checks. Real life: NGINX/ALB fronts app tier.
 *
 * <p><b>Q. Database sharding?</b> Split rows by shard key across databases to scale writes. Trade-offs: cross-shard
 * queries hard; rebalancing complex. Real life: shard users by region or userId range.
 *
 * <p><b>Q. CAP theorem?</b> In partition events, choose between strong Consistency vs Availability for a given
 * model; Partition tolerance is mandatory in distributed systems. Real life: bank ledger may favor CP; social
 * likes may favor AP + eventual consistency.
 *
 * <p>Runnable toy: bounded in-memory map (evicts an existing key when full) — illustrates cache size limits.
 */
public final class SystemDesignBasicsInterviewQA {

    private SystemDesignBasicsInterviewQA() {
    }

    static final class TinyUrlService {
        private final Map<String, String> keyToUrl = new ConcurrentHashMap<>();
        private final int maxKeys;

        TinyUrlService(int maxKeys) {
            this.maxKeys = maxKeys;
        }

        synchronized void put(String key, String url) {
            if (keyToUrl.size() >= maxKeys && !keyToUrl.containsKey(key)) {
                String victim = keyToUrl.keySet().iterator().next();
                keyToUrl.remove(victim);
            }
            keyToUrl.put(key, url);
        }

        String get(String key) {
            return keyToUrl.get(key);
        }
    }

    public static void main(String[] args) {
        TinyUrlService svc = new TinyUrlService(2);
        svc.put("a", "https://example.com/very-long");
        svc.put("b", "https://shop.example/p/1");
        svc.put("c", "https://news.example/article");
        System.out.println("resolve c? " + Objects.requireNonNull(svc.get("c")).startsWith("https://"));
    }
}

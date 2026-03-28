package com.harshit.preparation.topic15;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Topic 15 — Kafka.
 */
public final class Topic15Qa {

    private Topic15Qa() {
    }

    /*
     * Q: Delivery semantics?
     *
     * SCRIPT:
     * At-most-once can drop messages under failure. At-least-once is common—consumers may see duplicates,
     * so I design idempotent handlers. Exactly-once is expensive and often transactional—use when the
     * business truly requires it and we accept operational complexity.
     */

    /*
     * Q: Consumer failure handling?
     *
     * SCRIPT:
     * I retry transient errors with backoff, and after a limit I send the message to a dead-letter
     * topic for manual inspection. I monitor DLQ depth because poison messages indicate bugs or bad
     * data, not just network blips.
     */

    /*
     * Q: DLQ?
     *
     * SCRIPT:
     * A separate topic or queue for messages that cannot be processed after retries. Operators triage
     * them, fix the code or data, and replay when safe. Without DLQs, failed messages either block
     * processing or get lost silently.
     */

    /*
     * Q: Schema Registry?
     *
     * SCRIPT:
     * It stores Avro or JSON schemas for topics so producers and consumers agree on fields. Compatibility
     * modes define whether old readers can read new data or vice versa—critical for evolving events
     * without taking down consumers.
     */

    /*
     * Q: Kafka vs ActiveMQ?
     *
     * SCRIPT:
     * Kafka is a distributed log with partitions and high throughput; consumers can replay. ActiveMQ
     * is a traditional message broker with queues and JMS semantics. I pick Kafka for event streaming
     * and scale; ActiveMQ when an enterprise already standardized on JMS.
     */

    /*
     * Q: Multiple consumer instances—only one processes?
     *
     * SCRIPT:
     * Within the same consumer group, each partition is assigned to one consumer at a time, so
     * ordering per partition is preserved. If I need strict single-threaded processing for the whole
     * topic, I use one partition—or I redesign for partition-level parallelism. More consumers than
     * partitions means some consumers sit idle.
     */

    /** Simulates retries → DLQ and at-least-once handling (no broker required). */
    public static void demo() {
        List<String> inbox = new ArrayList<>(List.of("ok1", "bad", "ok2"));
        Deque<String> dlq = new ArrayDeque<>();
        int maxAttempts = 2;
        for (int i = 0; i < inbox.size(); i++) {
            String msg = inbox.get(i);
            boolean delivered = false;
            for (int attempt = 1; attempt <= maxAttempts && !delivered; attempt++) {
                try {
                    process(msg);
                    delivered = true;
                    System.out.println("acked: " + msg);
                } catch (IllegalStateException e) {
                    System.out.println("retry " + attempt + " failed for " + msg);
                    if (attempt == maxAttempts) {
                        dlq.addLast(msg);
                        System.out.println("→ DLQ: " + msg);
                    }
                }
            }
        }
        System.out.println("dead-letter queue: " + dlq);
    }

    private static void process(String msg) {
        if ("bad".equals(msg)) {
            throw new IllegalStateException("poison");
        }
    }

    public static void main(String[] args) {
        demo();
    }
}

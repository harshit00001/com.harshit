package com.harshit.preparation.topic19.extra;

/**
 * Behavioral / HR interview prompts — under java preparation / 19-agile module.
 *
 * <p><b>Q. Tell me about yourself.</b> 60–90 seconds: current role + tech stack, one strong impact metric,
 * why this company/role. Real life: “I’m a backend engineer on billing; cut p95 latency 40% by caching
 * and async; excited by your payments domain.”
 *
 * <p><b>Q. Why switch?</b> Positive framing: growth, tech depth, product mission, location — never bad-mouth.
 * Real life: “I want deeper distributed systems exposure; your platform scale matches my goals.”
 *
 * <p><b>Q. Challenging situation?</b> STAR: Situation, Task, Action, Result with metrics. Real life:
 * production outage → coordinated rollback → added canary deploys → MTTR dropped.
 *
 * <p><b>Q. Explain your project.</b> Problem, users, your ownership, architecture diagram in words, trade-offs,
 * metrics. Real life: “Order service: Spring Boot + Postgres + Kafka outbox; I owned refunds module.”
 *
 * <p><b>Q. Your role?</b> Be specific: design, coding %, reviews, on-call, mentoring. Real life: “IC on squad of 5;
 * led API design for checkout v2.”
 *
 * <p><b>Q. Deadlines?</b> Prioritize MVP, communicate risks early, cut scope with PM, add tests on critical paths.
 * Real life: “Slipped non-critical UI polish; shipped core API on time with feature flag.”
 */
public final class BehavioralInterviewQA {

    private BehavioralInterviewQA() {
    }

    public static void main(String[] args) {
        System.out.println("Behavioral Q&A — use STAR stories from your real projects; this class is the crib sheet.");
    }
}

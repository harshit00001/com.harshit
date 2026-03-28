package com.harshit.preparation.topic11;

/**
 * Topic 11 — Spring @Transactional.
 */
public final class Topic11Qa {

    private Topic11Qa() {
    }

    /*
     * Q: Can we use @Transactional on protected or private methods?
     *
     * SCRIPT:
     * I would say Spring’s default proxy-based AOP only intercepts public methods on the Spring bean
     * proxy. Private and protected methods are not advised the same way, so @Transactional on them
     * usually does not give the transaction you expect. The fix is to move transactional logic to a
     * public method on a Spring bean, or use AspectJ compile-time weaving if we truly need finer control.
     */

    /*
     * Q: How is @Transactional implemented internally?
     *
     * SCRIPT:
     * I describe the chain: a proxy wraps the bean, TransactionInterceptor runs around the method,
     * PlatformTransactionManager starts or joins a transaction, binds resources like the EntityManager
     * to the thread, and on success commits or on failure rolls back based on exception rules. That
     * is why self-invocation inside the same class bypasses the proxy—this.method() is not going
     * through the interceptor.
     */

    /*
     * Q: What actually rolls back? (tricky)
     *
     * SCRIPT:
     * By default unchecked exceptions and Error roll back; checked exceptions do not unless I set
     * rollbackFor. I also clarify that if I catch a runtime exception inside the method and swallow
     * it, the transaction may still commit depending on flow—so I either rethrow or mark rollback-only
     * explicitly. I never assume “any exception” rolls back without checking the rules.
     */

    /*
     * Q: How would you log all @Transactional methods?
     *
     * SCRIPT:
     * I would use Spring AOP with an @Around advice on @annotation(Transactional), logging the join
     * signature, maybe MDC correlation id, and timing. That keeps logging out of business code and
     * applies consistently across services.
     */

    /**
     * Plain-Java sketch of commit vs rollback (like PlatformTransactionManager). Unchecked exception → rollback.
     */
    public static void demo() {
        runInTransaction(() -> System.out.println("work: insert row (commit)"));
        runInTransaction(() -> {
            System.out.println("work: will throw → rollback");
            throw new IllegalStateException("boom");
        });
    }

    private static void runInTransaction(Runnable work) {
        System.out.println("BEGIN");
        try {
            work.run();
            System.out.println("COMMIT");
        } catch (RuntimeException e) {
            System.out.println("ROLLBACK (" + e.getClass().getSimpleName() + ")");
        }
    }

    public static void main(String[] args) {
        demo();
    }
}

package com.harshit.transaction.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * <h2>Interview topic: what happens inside a Spring transaction, and "after commit" callbacks</h2>
 *
 * <p><b>Typical interview question (full wording):</b>
 * "If I need to publish a message to Kafka or send an email only when the database transaction has
 * successfully committed, how would you do that in Spring? Why is it wrong to do it in the middle
 * of the service method?"
 *
 * <p><b>Answer in plain language:</b>
 * While your {@code @Transactional} method is still running, the database work might still be rolled
 * back (for example if an exception happens later in the same method, or in another participant).
 * If you send a message or fire an event <i>before</i> commit, you can create inconsistency: the
 * outside world thinks something happened, but the database never persisted it. Spring therefore
 * lets you register a {@link TransactionSynchronization} callback. The method
 * {@link TransactionSynchronization#afterCommit()} is invoked only after the transaction manager
 * has successfully committed the transaction, so side effects that must match the database state
 * belong there (or in equivalent mechanisms like transactional outbox).
 *
 * <p><b>How this ties to Spring's internal flow (high level):</b>
 * <ol>
 *   <li>The transaction interceptor opens a transaction and binds resources (e.g. JDBC connection,
 *       Hibernate session) to the current thread.</li>
 *   <li>Your business code runs and may flush SQL to the database, but commit has not happened yet.</li>
 *   <li>On success, the transaction manager commits; on failure, it rolls back.</li>
 *   <li>Callbacks registered with {@link TransactionSynchronizationManager#registerSynchronization}
 *       run at defined phases: {@code beforeCommit}, {@code afterCommit}, {@code afterCompletion}
 *       (the last one runs for both commit and rollback — use it for cleanup, not for business events
 *       that must only happen on success).</li>
 * </ol>
 *
 * <p>This class only demonstrates {@code afterCommit}. In production you might use Spring's
 * {@code TransactionalEventListener} with {@code phase = AFTER_COMMIT} for domain events, or
 * patterns like outbox for reliable messaging.
 */
@Service
public class TransactionInternalsDemo {

    /**
     * Registers a {@link Runnable} to run once, and only if the current transaction commits
     * successfully.
     *
     * <p><b>Why we check {@link TransactionSynchronizationManager#isSynchronizationActive()}:</b>
     * Synchronizations can only be registered when a Spring-managed transaction is active and
     * synchronization is enabled (it normally is for Spring's transaction manager). If you call
     * this method without an active transaction, there is no commit phase — so we fail fast.
     *
     * <p><b>Where the "real" work goes in a real service:</b>
     * In this demo, the method body only registers the callback. In a real application you would
     * first perform your JPA/JDBC updates in the same method; those participate in the same
     * transaction. When the method returns successfully, Spring completes the transaction: if it
     * commits, {@code afterCommit} runs your action (e.g. publish "OrderPaid" event). If the
     * transaction rolls back, {@code afterCommit} is not called, so you never notify external systems
     * of work that was discarded.
     *
     * <p><b>Thread note:</b> Callbacks run on the same thread that completed the transaction, not
     * on a background thread, unless you schedule work yourself inside {@code afterCommit}.
     */
    @Transactional
    public void scheduleAfterCommit(Runnable action) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalStateException(
                    "No active transaction synchronization — call this from within a @Transactional "
                            + "method (or enable synchronization on the transaction manager).");
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
        // In real code: load entities, call repositories, apply business rules — all still in this TX.
        // Commit happens after this method returns successfully; then afterCommit() runs.
    }
}

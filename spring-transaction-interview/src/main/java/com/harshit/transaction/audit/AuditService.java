package com.harshit.transaction.audit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h2>Propagation {@code REQUIRES_NEW}: audit that survives a failed business transaction</h2>
 *
 * <p><b>Interview question:</b> "We need to log every payment attempt for compliance, even when
 * the payment fails and the business transaction rolls back. How can Spring do that?"
 *
 * <p><b>Answer:</b> Use {@link Propagation#REQUIRES_NEW} on the audit method. When it runs, Spring
 * <i>suspends</i> the outer transaction (if any), opens a <i>new</i> database transaction, commits
 * the audit row in that new transaction, then resumes the outer transaction. If the outer
 * transaction later fails, its changes roll back, but the audit insert was already committed in
 * its own transaction and remains visible.
 *
 * <p><b>Mental model:</b> {@code REQUIRED} (default) means "join the same transaction — same fate
 * as everyone else." {@code REQUIRES_NEW} means "start fresh — my commit is independent of the
 * outer rollback." Use it sparingly: more open transactions mean more overhead, and you must still
 * design idempotency and error handling correctly.
 */
@Service
public class AuditService {

    private final AuditLogRepository auditLogs;

    public AuditService(AuditLogRepository auditLogs) {
        this.auditLogs = auditLogs;
    }

    /**
     * Each call commits in its own transaction, independent of the caller's transaction.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String message) {
        auditLogs.save(new AuditLog(message));
    }
}

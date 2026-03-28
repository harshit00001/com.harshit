package com.harshit.transaction.audit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <p>Simple audit row: one record per logical event we want to remember for compliance or debugging.
 *
 * <p>In this project, rows are inserted by {@link AuditService#record}, which runs in a separate
 * transaction ({@code REQUIRES_NEW}) so that when the main business operation rolls back, the
 * audit row can still remain committed. That is the whole point of the facade demo
 * ({@link com.harshit.transaction.facade.AuditedTransferFacade}).
 */
@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    protected AuditLog() {
    }

    public AuditLog(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}

package com.harshit.transaction.selfinvocation;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * <h2>Fixing self-invocation: call the Spring proxy, not {@code this}</h2>
 *
 * <p><b>Interview question:</b> "How do you fix the self-invocation problem without moving code to
 * another class?"
 *
 * <p><b>Answer:</b> Inject a reference to your own bean (the proxy). When you call
 * {@code self.saveReport()}, the call goes through the proxy, so the transaction interceptor runs.
 * Use {@link Lazy} on the constructor argument to break the circular dependency: Spring first
 * creates a proxy, then injects it once the bean is fully constructed.
 *
 * <p><b>Alternative:</b> extract {@code saveReport()} into a separate {@code @Service} — often
 * clearer than self-injection.
 */
@Service
public class ReportingServiceGood {

    private final ReportingServiceGood self;

    public ReportingServiceGood(@Lazy ReportingServiceGood self) {
        this.self = self;
    }

    public void publishDailyReport() {
        self.saveReport();
    }

    @Transactional
    public void saveReport() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Expected active transaction");
        }
    }
}

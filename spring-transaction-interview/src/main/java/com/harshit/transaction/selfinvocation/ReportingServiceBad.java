package com.harshit.transaction.selfinvocation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * <h2>Self-invocation problem: why {@code @Transactional} sometimes "does not work"</h2>
 *
 * <p><b>Interview question:</b> "I have a {@code @Transactional} method {@code saveReport()} and
 * I call it from {@code publishDailyReport()} in the same class. Why is there no transaction?"
 *
 * <p><b>Answer:</b> Spring applies {@code @Transactional} through an <i>AOP proxy</i>. When some
 * other bean calls your service, it calls the proxy, which opens the transaction and then delegates
 * to your real object. When you call {@code this.saveReport()} from inside the same class, you are
 * bypassing the proxy: you invoke the real method directly, so the transaction interceptor never
 * runs. That is why {@link TransactionSynchronizationManager#isActualTransactionActive()} returns
 * false here.
 *
 * <p><b>Fixes (conceptual):</b> call through an injected dependency (often {@code @Lazy} self),
 * move the transactional code to another Spring bean, use {@code TransactionTemplate}, or AspectJ
 * compile-time weaving so the aspect applies even to internal calls.
 *
 * <p>This class <i>intentionally</i> throws to make the failure obvious in demos.
 */
@Service
public class ReportingServiceBad {

    /**
     * Not transactional — calls {@link #saveReport()} via {@code this}, so no proxy involved.
     */
    public void publishDailyReport() {
        saveReport();
    }

    /**
     * Would be transactional if called from outside the bean; self-invocation skips the proxy.
     */
    @Transactional
    public void saveReport() {
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();
        if (!active) {
            throw new IllegalStateException(
                    "BUG DEMO: saveReport() expected TX but proxy was skipped (self-invocation)");
        }
    }
}

package com.harshit.transaction.rollback;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h2>Rollback rules: which exceptions roll back the transaction?</h2>
 *
 * <p><b>Interview question:</b> "By default, which exceptions cause a rollback in Spring
 * {@code @Transactional}?"
 *
 * <p><b>Answer:</b> By default, Spring rolls back on <i>unchecked</i> exceptions — subclasses of
 * {@link RuntimeException} and {@link Error}. A <i>checked</i> exception (e.g. plain
 * {@link Exception}) does <b>not</b> trigger rollback unless you configure it. That surprises many
 * people: the transaction may still commit even though your method threw a checked exception, which
 * can leave inconsistent data if you assumed rollback.
 *
 * <p><b>What to say in an interview:</b> "For business methods that throw checked exceptions, I set
 * {@code rollbackFor = Exception.class} when we need every failure to abort the transaction, or I
 * use unchecked exceptions for true failure cases." Also mention {@code noRollbackFor} for rare
 * cases where a runtime exception should not roll back.
 *
 * <p><b>Warning:</b> The methods below throw on purpose — they are for study and unit tests, not
 * for production call paths without a test transaction boundary.
 */
@Service
public class RollbackDemoService {

    /**
     * Unchecked exception — default rollback behavior applies.
     */
    @Transactional
    public void throwsRuntime() {
        throw new IllegalStateException("runtime — will rollback");
    }

    /**
     * Checked exception — with default rules, transaction may still commit (dangerous if you rely
     * on rollback).
     */
    @Transactional
    public void throwsChecked() throws Exception {
        throw new Exception("checked — default may commit (dangerous)");
    }

    /**
     * Explicit rollback for all {@link Exception} types including checked.
     */
    @Transactional(rollbackFor = Exception.class)
    public void throwsCheckedWithRollback() throws Exception {
        throw new Exception("checked — rolls back because of rollbackFor");
    }
}

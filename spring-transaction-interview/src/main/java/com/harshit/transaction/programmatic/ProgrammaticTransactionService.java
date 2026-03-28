package com.harshit.transaction.programmatic;

import com.harshit.transaction.account.Account;
import com.harshit.transaction.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * <h2>Programmatic transactions: {@link TransactionTemplate}</h2>
 *
 * <p><b>Interview question:</b> "When would you use {@code TransactionTemplate} instead of
 * {@code @Transactional}?"
 *
 * <p><b>Answer:</b> Use the template when the transactional boundary does not map cleanly to a
 * single public method — for example loops where each iteration should be its own transaction,
 * APIs that do not go through Spring's proxy, or dynamic control flow where you need to call
 * {@code setRollbackOnly()} on the status object. Under the hood it still uses the same
 * {@code PlatformTransactionManager} as declarative transactions.
 *
 * <p><b>Trade-off:</b> Declarative ({@code @Transactional}) is easier to read for simple CRUD
 * services; programmatic control is explicit but more verbose.
 */
@Service
public class ProgrammaticTransactionService {

    private final TransactionTemplate transactionTemplate;
    private final AccountRepository accounts;

    public ProgrammaticTransactionService(TransactionTemplate transactionTemplate,
                                          AccountRepository accounts) {
        this.transactionTemplate = transactionTemplate;
        this.accounts = accounts;
    }

    /**
     * Runs the lambda inside a new transaction (default propagation of the template).
     */
    public void adjustBalanceInTemplate(Long accountId, long deltaCents) {
        transactionTemplate.executeWithoutResult(status -> {
            Account acc = accounts.findById(accountId).orElseThrow();
            if (deltaCents >= 0) {
                acc.credit(deltaCents);
            } else {
                acc.debit(-deltaCents);
            }
            accounts.save(acc);
        });
    }
}

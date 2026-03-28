package com.harshit.transaction.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h2>Classic interview example: bank / wallet transfer and ACID atomicity</h2>
 *
 * <p><b>Interview question:</b> "Why do we put debit and credit in one {@code @Transactional}
 * method instead of two separate methods each with its own transaction?"
 *
 * <p><b>Answer:</b> Money must move atomically: either both accounts reflect the new balances or
 * neither does. If debit succeeded in transaction A and credit failed in transaction B, money would
 * disappear or duplicate. A single transaction boundary groups all database changes so they commit
 * once or roll back together — that is the "A" (Atomicity) in ACID.
 *
 * <p><b>What Spring does here:</b> When a caller invokes {@link #transfer}, Spring's transaction
 * interceptor starts (or joins) a transaction before the method body runs. Hibernate/JPA flushes SQL
 * to the database; the commit is issued when the method completes without a rollback-only marker.
 * If any unchecked exception propagates out (by default), the transaction rolls back and the
 * database returns to its previous state.
 */
@Service
public class TransferService {

    private final AccountRepository accounts;

    public TransferService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    /**
     * Happy path: debit source, credit destination, persist — one atomic unit of work.
     */
    @Transactional
    public void transfer(Long fromId, Long toId, long amountCents) {
        Account from = accounts.findById(fromId).orElseThrow();
        Account to = accounts.findById(toId).orElseThrow();
        from.debit(amountCents);
        to.credit(amountCents);
        accounts.save(from);
        accounts.save(to);
    }

    /**
     * <b>Interview follow-up:</b> "What happens if we throw after only the debit was saved?"
     *
     * <p>Because the whole method is still one transaction, nothing is permanently committed until
     * the end. The thrown {@link IllegalStateException} causes rollback, so the debit is undone
     * along with anything else in this transaction — balances stay consistent.
     */
    @Transactional
    public void transferThatFailsAfterDebit(Long fromId, Long toId, long amountCents) {
        Account from = accounts.findById(fromId).orElseThrow();
        Account to = accounts.findById(toId).orElseThrow();
        from.debit(amountCents);
        accounts.save(from);
        throw new IllegalStateException("Simulated downstream failure — rollback both sides");
    }
}

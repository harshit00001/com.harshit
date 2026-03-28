package com.harshit.transaction.facade;

import com.harshit.transaction.account.AccountRepository;
import com.harshit.transaction.account.TransferService;
import com.harshit.transaction.audit.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h2>Composing services: outer transaction + independent audit</h2>
 *
 * <p><b>Interview question:</b> "The business transfer runs in one transaction and fails, but we
 * must keep an audit row that says we attempted it. How?"
 *
 * <p><b>Answer:</b> The facade method is {@code @Transactional} — it starts one transaction for the
 * business work. The first step calls {@link AuditService#record}, which uses
 * {@code Propagation.REQUIRES_NEW}. That writes and commits the audit in a separate transaction
 * before the rest of the work continues. When {@link TransferService#transferThatFailsAfterDebit}
 * throws, only the <i>outer</i> transaction rolls back (account balances revert), but the audit row
 * inserted in the inner transaction remains committed.
 *
 * <p>This pattern appears in compliance logging, security event trails, and "outbox"-like
 * scenarios — always consider failure modes and duplicate handling.
 */
@Service
public class AuditedTransferFacade {

    private final TransferService transferService;
    private final AuditService auditService;
    private final AccountRepository accounts;

    public AuditedTransferFacade(TransferService transferService,
                                 AuditService auditService,
                                 AccountRepository accounts) {
        this.transferService = transferService;
        this.auditService = auditService;
        this.accounts = accounts;
    }

    @Transactional
    public void transferWithAuditTrail(Long fromId, Long toId, long amountCents) {
        auditService.record("TRANSFER_ATTEMPT from=" + fromId + " to=" + toId + " amount=" + amountCents);
        transferService.transferThatFailsAfterDebit(fromId, toId, amountCents);
    }

    public long balance(Long id) {
        return accounts.findById(id).map(a -> a.getBalanceCents()).orElse(-1L);
    }
}

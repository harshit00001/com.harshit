package com.harshit.transaction.demo;

import com.harshit.transaction.account.Account;
import com.harshit.transaction.account.AccountRepository;
import com.harshit.transaction.account.TransferService;
import com.harshit.transaction.audit.AuditLogRepository;
import com.harshit.transaction.facade.AuditedTransferFacade;
import com.harshit.transaction.internal.TransactionInternalsDemo;
import com.harshit.transaction.programmatic.ProgrammaticTransactionService;
import com.harshit.transaction.proxy.ProxyShowcase;
import com.harshit.transaction.selfinvocation.ReportingServiceBad;
import com.harshit.transaction.selfinvocation.ReportingServiceGood;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <h2>Runnable "lab" that prints each concept to the console on startup</h2>
 *
 * <p>Walk through {@link #run} in order while reading the Javadoc on each service class. Each
 * {@code printSection} block matches one interview theme: proxies, atomic transfer, rollback,
 * {@code REQUIRES_NEW} audit, self-invocation, {@link org.springframework.transaction.support.TransactionTemplate},
 * and {@code afterCommit}.
 *
 * <p>For long-form explanations (not limited to short comments), see {@code docs/INTERVIEW_SCRIPT.md}
 * and {@code docs/DEEP_DIVE.md} in this project.
 */
@Component
@Order(1)
public class InterviewDemoRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InterviewDemoRunner.class);

    private final AccountRepository accounts;
    private final TransferService transferService;
    private final AuditedTransferFacade auditedTransferFacade;
    private final AuditLogRepository auditLogs;
    private final ReportingServiceBad reportingServiceBad;
    private final ReportingServiceGood reportingServiceGood;
    private final ProgrammaticTransactionService programmaticTransactionService;
    private final ProxyShowcase proxyShowcase;
    private final TransactionInternalsDemo transactionInternalsDemo;

    public InterviewDemoRunner(AccountRepository accounts,
                               TransferService transferService,
                               AuditedTransferFacade auditedTransferFacade,
                               AuditLogRepository auditLogs,
                               ReportingServiceBad reportingServiceBad,
                               ReportingServiceGood reportingServiceGood,
                               ProgrammaticTransactionService programmaticTransactionService,
                               ProxyShowcase proxyShowcase,
                               TransactionInternalsDemo transactionInternalsDemo) {
        this.accounts = accounts;
        this.transferService = transferService;
        this.auditedTransferFacade = auditedTransferFacade;
        this.auditLogs = auditLogs;
        this.reportingServiceBad = reportingServiceBad;
        this.reportingServiceGood = reportingServiceGood;
        this.programmaticTransactionService = programmaticTransactionService;
        this.proxyShowcase = proxyShowcase;
        this.transactionInternalsDemo = transactionInternalsDemo;
    }

    @Override
    public void run(String... args) {
        seedAccounts();

        Long a = accounts.findAll().get(0).getId();
        Long b = accounts.findAll().get(1).getId();

        printSection("1) Proxy types (JDK vs CGLIB)");
        System.out.println(proxyShowcase.describeProxies());

        printSection("2) Successful @Transactional transfer (atomicity)");
        printBalances("Before", a, b);
        transferService.transfer(a, b, 1000);
        printBalances("After", a, b);

        printSection("3) Failed transfer — full rollback");
        long balBefore = accounts.findById(a).orElseThrow().getBalanceCents();
        try {
            transferService.transferThatFailsAfterDebit(a, b, 50_000);
        } catch (IllegalStateException e) {
            log.info("Expected: {}", e.getMessage());
        }
        long balAfter = accounts.findById(a).orElseThrow().getBalanceCents();
        System.out.println("Balance unchanged after rollback? " + (balBefore == balAfter));

        printSection("4) REQUIRES_NEW audit survives outer rollback");
        long auditBefore = auditLogs.count();
        try {
            auditedTransferFacade.transferWithAuditTrail(a, b, 10_000);
        } catch (IllegalStateException e) {
            log.info("Expected: {}", e.getMessage());
        }
        System.out.println("Audit rows added: " + (auditLogs.count() - auditBefore));
        printBalances("After failed audited transfer (transfer rolled back)", a, b);

        printSection("5) Self-invocation — BAD (no TX on internal call)");
        try {
            reportingServiceBad.publishDailyReport();
        } catch (IllegalStateException e) {
            System.out.println("Caught (expected): " + e.getMessage());
        }

        printSection("6) Self-invocation — GOOD (via injected proxy)");
        reportingServiceGood.publishDailyReport();
        System.out.println("OK — transaction active on saveReport()");

        printSection("7) Programmatic TransactionTemplate");
        programmaticTransactionService.adjustBalanceInTemplate(a, 500);
        System.out.println("Account A balance after +500 cents: "
                + accounts.findById(a).orElseThrow().getBalanceCents());

        printSection("8) afterCommit callback (see log line after TX commits)");
        transactionInternalsDemo.scheduleAfterCommit(() ->
                System.out.println(">>> afterCommit ran — safe to publish domain event / send message"));
        System.out.println("(afterCommit runs when @Transactional method commits)");

        System.out.println("\n=== Demo finished — open docs/INTERVIEW_SCRIPT.md for Q&A ===\n");
    }

    private void seedAccounts() {
        accounts.deleteAll();
        accounts.save(new Account("Alice", 100_000));
        accounts.save(new Account("Bob", 50_000));
    }

    private void printBalances(String label, Long a, Long b) {
        long ba = accounts.findById(a).orElseThrow().getBalanceCents();
        long bb = accounts.findById(b).orElseThrow().getBalanceCents();
        System.out.println(label + " | A(cents)=" + ba + " | B(cents)=" + bb);
    }

    private static void printSection(String title) {
        System.out.println("\n--- " + title + " ---");
    }
}

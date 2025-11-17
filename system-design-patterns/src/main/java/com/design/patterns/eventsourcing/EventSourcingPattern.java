package com.design.patterns.eventsourcing;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * EVENT SOURCING PATTERN - Interview Explanation:
 * 
 * Problem: Traditional systems store current state. If you need to know
 * what happened in the past, you lose that information.
 * 
 * Solution: Instead of storing current state, store all events (changes)
 * that happened. Current state is reconstructed by replaying events.
 * 
 * Simple Explanation:
 * - Instead of: "User balance = $100" (current state)
 * - We store: "Deposited $50", "Withdrew $20", "Deposited $70" (events)
 * - To get current balance: Add all deposits, subtract all withdrawals
 * 
 * Benefits:
 * - Complete audit trail (know exactly what happened)
 * - Can rebuild state at any point in time
 * - Can replay events for debugging
 * - Can create new views/projections from events
 * 
 * Interview Tip: Often used with CQRS pattern.
 */
public class EventSourcingPattern {

    public static void main(String[] args) {
        System.out.println("=== EVENT SOURCING PATTERN ===\n");
        
        // Interview Point: Event store - stores all events
        EventStore eventStore = new EventStore();
        AccountRepository repository = new AccountRepository(eventStore);
        
        String accountId = "acc-123";
        
        System.out.println("--- Creating Account and Performing Transactions ---\n");
        
        // Interview Point: Instead of updating balance directly, we create events
        repository.createAccount(accountId, "John Doe", 100.0);
        repository.deposit(accountId, 50.0);
        repository.withdraw(accountId, 30.0);
        repository.deposit(accountId, 25.0);
        
        System.out.println("\n--- Current State (Reconstructed from Events) ---");
        Account account = repository.getAccount(accountId);
        System.out.println("Account: " + account);
        System.out.println("Current Balance: $" + account.getBalance());
        
        System.out.println("\n--- Event History ---");
        List<DomainEvent> events = eventStore.getEvents(accountId);
        events.forEach(event -> System.out.println("  " + event));
        
        System.out.println("\n--- Rebuilding State at Different Points in Time ---");
        // Interview Point: Can rebuild state at any point
        Account accountAfterFirstDeposit = repository.getAccountAt(accountId, 2); // After 2 events
        System.out.println("Balance after first deposit: $" + accountAfterFirstDeposit.getBalance());
        
        System.out.println("\n--- Audit Trail ---");
        // Interview Point: Complete audit trail - know exactly what happened
        repository.printAuditTrail(accountId);
    }
}

// ==================== EVENT STORE ====================

/**
 * Interview Point: Event Store
 * Stores all events in order. This is the source of truth.
 * In real systems, this could be a database, message queue, etc.
 */
class EventStore {
    private Map<String, List<DomainEvent>> events = new HashMap<>();
    
    /**
     * Interview Point: Append-only store
     * Events are never modified or deleted (immutable)
     */
    public void appendEvent(String aggregateId, DomainEvent event) {
        events.computeIfAbsent(aggregateId, k -> new ArrayList<>()).add(event);
        System.out.println("  → Event stored: " + event.getClass().getSimpleName());
    }
    
    /**
     * Interview Point: Get all events for an aggregate
     * These events can be replayed to rebuild state
     */
    public List<DomainEvent> getEvents(String aggregateId) {
        return events.getOrDefault(aggregateId, new ArrayList<>());
    }
    
    /**
     * Interview Point: Get events up to a certain point
     * Useful for rebuilding state at a specific time
     */
    public List<DomainEvent> getEventsUpTo(String aggregateId, int eventCount) {
        List<DomainEvent> allEvents = getEvents(aggregateId);
        return allEvents.stream()
            .limit(eventCount)
            .collect(Collectors.toList());
    }
}

// ==================== DOMAIN EVENTS ====================

/**
 * Interview Point: Domain Events
 * Represent something that happened in the system
 * They are immutable and contain all information about the event
 */
interface DomainEvent {
    String getAggregateId();
    LocalDateTime getTimestamp();
}

class AccountCreatedEvent implements DomainEvent {
    private String accountId;
    private String ownerName;
    private double initialBalance;
    private LocalDateTime timestamp;
    
    public AccountCreatedEvent(String accountId, String ownerName, double initialBalance) {
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.initialBalance = initialBalance;
        this.timestamp = LocalDateTime.now();
    }
    
    @Override
    public String getAggregateId() { return accountId; }
    
    public String getOwnerName() { return ownerName; }
    public double getInitialBalance() { return initialBalance; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("[%s] AccountCreated: %s, Initial: $%.2f", 
            timestamp, ownerName, initialBalance);
    }
}

class MoneyDepositedEvent implements DomainEvent {
    private String accountId;
    private double amount;
    private LocalDateTime timestamp;
    
    public MoneyDepositedEvent(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    @Override
    public String getAggregateId() { return accountId; }
    
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("[%s] MoneyDeposited: $%.2f", timestamp, amount);
    }
}

class MoneyWithdrawnEvent implements DomainEvent {
    private String accountId;
    private double amount;
    private LocalDateTime timestamp;
    
    public MoneyWithdrawnEvent(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    @Override
    public String getAggregateId() { return accountId; }
    
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("[%s] MoneyWithdrawn: $%.2f", timestamp, amount);
    }
}

// ==================== AGGREGATE ====================

/**
 * Interview Point: Aggregate (Domain Model)
 * Current state is derived from events, not stored directly
 */
class Account {
    private String accountId;
    private String ownerName;
    private double balance;
    
    public Account(String accountId, String ownerName, double balance) {
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = balance;
    }
    
    /**
     * Interview Point: Apply event to update state
     * This is how we rebuild state from events
     */
    public void apply(AccountCreatedEvent event) {
        this.accountId = event.getAggregateId();
        this.ownerName = event.getOwnerName();
        this.balance = event.getInitialBalance();
    }
    
    public void apply(MoneyDepositedEvent event) {
        this.balance += event.getAmount();
    }
    
    public void apply(MoneyWithdrawnEvent event) {
        this.balance -= event.getAmount();
    }
    
    public String getAccountId() { return accountId; }
    public String getOwnerName() { return ownerName; }
    public double getBalance() { return balance; }
    
    @Override
    public String toString() {
        return String.format("Account{id='%s', owner='%s', balance=$%.2f}", 
            accountId, ownerName, balance);
    }
}

// ==================== REPOSITORY ====================

/**
 * Interview Point: Repository
 * Handles event sourcing logic - stores events and rebuilds state
 */
class AccountRepository {
    private EventStore eventStore;
    
    public AccountRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }
    
    /**
     * Interview Point: Create account by storing event
     * We don't store the account directly, we store the event
     */
    public void createAccount(String accountId, String ownerName, double initialBalance) {
        AccountCreatedEvent event = new AccountCreatedEvent(accountId, ownerName, initialBalance);
        eventStore.appendEvent(accountId, event);
        System.out.println("Created account: " + ownerName + " with initial balance: $" + initialBalance);
    }
    
    /**
     * Interview Point: Deposit by storing event
     */
    public void deposit(String accountId, double amount) {
        MoneyDepositedEvent event = new MoneyDepositedEvent(accountId, amount);
        eventStore.appendEvent(accountId, event);
        System.out.println("Deposited: $" + amount);
    }
    
    /**
     * Interview Point: Withdraw by storing event
     */
    public void withdraw(String accountId, double amount) {
        MoneyWithdrawnEvent event = new MoneyWithdrawnEvent(accountId, amount);
        eventStore.appendEvent(accountId, event);
        System.out.println("Withdrew: $" + amount);
    }
    
    /**
     * Interview Point: Get current account state
     * Rebuilds state by replaying all events
     */
    public Account getAccount(String accountId) {
        List<DomainEvent> events = eventStore.getEvents(accountId);
        return rebuildAccount(events);
    }
    
    /**
     * Interview Point: Get account state at a specific point
     * Replays events up to that point
     */
    public Account getAccountAt(String accountId, int eventCount) {
        List<DomainEvent> events = eventStore.getEventsUpTo(accountId, eventCount);
        return rebuildAccount(events);
    }
    
    /**
     * Interview Point: Rebuild account by replaying events
     * This is the core of event sourcing - state is derived from events
     */
    private Account rebuildAccount(List<DomainEvent> events) {
        Account account = new Account("", "", 0.0);
        
        for (DomainEvent event : events) {
            if (event instanceof AccountCreatedEvent) {
                account.apply((AccountCreatedEvent) event);
            } else if (event instanceof MoneyDepositedEvent) {
                account.apply((MoneyDepositedEvent) event);
            } else if (event instanceof MoneyWithdrawnEvent) {
                account.apply((MoneyWithdrawnEvent) event);
            }
        }
        
        return account;
    }
    
    /**
     * Interview Point: Complete audit trail
     * We can see exactly what happened and when
     */
    public void printAuditTrail(String accountId) {
        List<DomainEvent> events = eventStore.getEvents(accountId);
        System.out.println("Audit Trail for Account: " + accountId);
        System.out.println("Total Events: " + events.size());
        events.forEach(event -> System.out.println("  " + event));
    }
}

/**
 * INTERVIEW SUMMARY: Event Sourcing Pattern
 * 
 * When to use:
 * - Need complete audit trail
 * - Need to rebuild state at any point in time
 * - Need to debug by replaying events
 * - Complex business logic with many state changes
 * - Compliance/regulatory requirements
 * 
 * Benefits:
 * - Complete history (never lose information)
 * - Time travel (see state at any point)
 * - Audit trail (know exactly what happened)
 * - Debugging (replay events to find issues)
 * - Flexibility (create new views from events)
 * 
 * Challenges:
 * - Eventual consistency (if using projections)
 * - Storage (can grow large over time)
 * - Complexity (more complex than CRUD)
 * - Replay performance (rebuilding state can be slow)
 * 
 * Often combined with:
 * - CQRS (events for writes, projections for reads)
 * - Snapshots (periodic state snapshots for performance)
 * 
 * Real-world examples:
 * - Banking systems (transaction history)
 * - Version control (Git stores events, not current state)
 * - Accounting systems (ledger entries)
 * - Game state (replay game from events)
 */




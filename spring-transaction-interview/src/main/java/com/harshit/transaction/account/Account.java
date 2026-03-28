package com.harshit.transaction.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <p>Domain object for the transfer demos: one row per account holder. Amounts are stored in
 * <b>cents</b> (integer) to avoid floating-point rounding issues — a common interview talking point
 * for financial code.
 *
 * <p>All persistence of this entity happens inside whatever transaction wraps the calling service
 * method (for example {@link TransferService#transfer}).
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String owner;
    private long balanceCents;

    protected Account() {
    }

    public Account(String owner, long balanceCents) {
        this.owner = owner;
        this.balanceCents = balanceCents;
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public long getBalanceCents() {
        return balanceCents;
    }

    public void debit(long amount) {
        if (amount < 0 || balanceCents < amount) {
            throw new IllegalArgumentException("Insufficient funds or invalid amount");
        }
        this.balanceCents -= amount;
    }

    public void credit(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        this.balanceCents += amount;
    }
}

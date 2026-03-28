package com.harshit.preparation.topics;

/**
 * Maps to folder: {@code ../02-oop-basics/} and {@code INTERVIEW.md}.
 * <p>
 * <b>Encapsulation</b> — hide state behind methods so invariants stay valid (balance never negative).
 */
public final class Topic02OopBasics {

    public static void runDemo() {
        BankAccount acc = new BankAccount(100);
        acc.deposit(50);
        acc.withdraw(30);
        System.out.println("Topic02 OOP — balance after ops: " + acc.getBalance());
    }

    static final class BankAccount {

        private int balance;

        BankAccount(int initial) {
            if (initial < 0) {
                throw new IllegalArgumentException();
            }
            this.balance = initial;
        }

        public void deposit(int amount) {
            if (amount < 0) {
                throw new IllegalArgumentException();
            }
            balance += amount;
        }

        public void withdraw(int amount) {
            if (amount < 0 || amount > balance) {
                throw new IllegalArgumentException();
            }
            balance -= amount;
        }

        public int getBalance() {
            return balance;
        }
    }

    private Topic02OopBasics() {
    }
}

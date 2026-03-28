package com.harshit.preparation.topic02;

import java.math.BigDecimal;

/**
 * Topic 02 — OOP basics.
 */
public final class Topic02Qa {

    private Topic02Qa() {
    }

    /*
     * Q: Explain OOP with real-time examples.
     *
     * SCRIPT:
     * I usually walk through the four pillars in plain language. Encapsulation means I hide the
     * internal state and only expose behavior through methods—like a bank account where the
     * balance is private but deposit and withdraw are public. Inheritance is the IS-A relation,
     * for example a SavingsAccount is an Account and reuses common behavior. Polymorphism means the
     * same method call can run different implementations at runtime, like pay() routing to card
     * or UPI. Abstraction means I expose a simple contract—sendNotification()—without forcing the
     * caller to know whether we use email or SMS behind the scenes.
     *
     * REAL LIFE:
     * Checkout “Pay” is one button, but the payment implementation changes per user choice—that is polymorphism.
     */

    /*
     * Q: Abstract class vs interface?
     *
     * SCRIPT:
     * I would say an abstract class is for sharing code and state in a small family of types—you
     * get constructors, fields, and both abstract and concrete methods, but Java allows only one
     * superclass. An interface is a pure contract; a class can implement many interfaces, and
     * since Java 8 we can add default methods. I use abstract classes when several subclasses truly
     * share implementation, and interfaces when I want capability-style APIs that many unrelated
     * classes can plug into.
     */

    /*
     * Q: Why abstract classes if interfaces have default methods?
     *
     * SCRIPT:
     * Default methods are great for evolving an interface without breaking implementors, but they
     * are not a full replacement for an abstract class. An abstract class can hold instance state,
     * use protected helpers, and define construction flow for subclasses. Heavy shared behavior and
     * fields still belong in a base class; interfaces stay lean and focused on what the outside world
     * should call.
     */

    /*
     * Q: Can we override a static method?
     *
     * SCRIPT:
     * I would clarify that static methods are not overridden in the object-oriented sense—they are
     * hidden based on the compile-time type of the reference. Instance methods use virtual dispatch,
     * so the runtime type decides which implementation runs; static methods bind to the declared
     * type at compile time, which surprises people if they expect polymorphic static calls.
     */

    /** Polymorphism: runtime type of Payment decides which pay() executes. */
    public static void demoPolymorphism() {
        Payment p = new UpiPayment();
        p.pay(new BigDecimal("10"));
        p = new CardPayment();
        p.pay(new BigDecimal("10"));
    }

    interface Payment {
        void pay(BigDecimal amount);
    }

    static class UpiPayment implements Payment {
        @Override
        public void pay(BigDecimal amount) {
            System.out.println("UPI " + amount);
        }
    }

    static class CardPayment implements Payment {
        @Override
        public void pay(BigDecimal amount) {
            System.out.println("Card " + amount);
        }
    }

    public static void main(String[] args) {
        demoPolymorphism();
    }
}

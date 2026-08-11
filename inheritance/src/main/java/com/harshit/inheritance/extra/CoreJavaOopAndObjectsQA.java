package com.harshit.inheritance.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Core Java fundamentals (OOP + objects + serialization) — lives in the inheritance learning repo.
 *
 * <p><b>Q1. What is OOP? Principles?</b> Modeling as objects (state + behavior). Pillars:
 * encapsulation, abstraction, inheritance, polymorphism. Real life: a "Payment" object hides card
 * details, exposes pay(), subclasses implement different providers.
 *
 * <p><b>Q2. Abstraction vs encapsulation?</b> Abstraction hides complexity and shows essentials
 * (interfaces, abstract types). Encapsulation hides fields and exposes controlled access (private +
 * getters). Real life: car steering wheel is abstraction; engine cover is encapsulation.
 *
 * <p><b>Q3. Polymorphism — compile-time vs runtime?</b> Compile-time = overloading (resolved by
 * signature). Runtime = overriding (dynamic dispatch on actual object type). See demo methods.
 *
 * <p><b>Q4. Inheritance? Types?</b> IS-A from a superclass; Java allows single class inheritance,
 * multiple interface inheritance. Real life: SavingsAccount extends Account.
 *
 * <p><b>Q5. Immutable classes?</b> No mutators after construction; final class, private final
 * fields, defensive copies for mutable members. Real life: money snapshots in a ledger.
 *
 * <p><b>Q6. equals() vs hashCode()?</b> Equal objects must have equal hash codes for HashMap/HashSet.
 * Real life: two employees with same employeeId should map to one desk badge bucket.
 *
 * <p><b>Q7. String immutability?</b> Content cannot change in place; operations return new strings.
 * Enables interning/thread safety. Real life: map keys stay stable while code passes strings around.
 *
 * <p><b>Q8. String vs StringBuilder vs StringBuffer?</b> String immutable; StringBuilder mutable,
 * not synchronized; StringBuffer mutable with synchronized methods (legacy thread-safety).
 * Real life: build CSV in a loop → StringBuilder.
 *
 * <p><b>Q9. Serialization? serialVersionUID?</b> Turning objects to bytes and back. serialVersionUID
 * versions the shape of the class for compatibility across releases. Real life: cache snapshots,
 * messaging payloads — mismatching UID causes InvalidClassException.
 */
public final class CoreJavaOopAndObjectsQA {

    private CoreJavaOopAndObjectsQA() {
    }

    /** Runtime polymorphism: JVM picks Dog#speak even if reference type is Animal. */
    static class Animal {
        String speak() {
            return "…";
        }
    }

    static class Dog extends Animal {
        @Override
        String speak() {
            return "woof";
        }
    }

    /** Compile-time polymorphism: overload resolution. */
    static int add(int a, int b) {
        return a + b;
    }

    static double add(double a, double b) {
        return a + b;
    }

    static final class Money implements Serializable {
        private static final long serialVersionUID = 42L;
        private final String currency;
        private final long minorUnits;

        Money(String currency, long minorUnits) {
            this.currency = Objects.requireNonNull(currency);
            this.minorUnits = minorUnits;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Money)) {
                return false;
            }
            Money money = (Money) o;
            return minorUnits == money.minorUnits && currency.equals(money.currency);
        }

        @Override
        public int hashCode() {
            return Objects.hash(currency, minorUnits);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Animal a = new Dog();
        System.out.println("Runtime polymorphism: " + a.speak());
        System.out.println("Compile-time polymorphism int: " + add(1, 2));
        System.out.println("Compile-time polymorphism double: " + add(1.0, 2.0));

        Money m1 = new Money("USD", 1999);
        Money m2 = new Money("USD", 1999);
        System.out.println("equals/hash contract: " + m1.equals(m2) + " hash1=" + m1.hashCode() + " hash2=" + m2.hashCode());

        // Serialization round-trip demo (bytes in memory, not a file — same idea).
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(m1);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            Money restored = (Money) ois.readObject();
            System.out.println("Deserialized equals original: " + m1.equals(restored));
        }

        StringBuilder csv = new StringBuilder();
        csv.append("id").append(',').append("name");
        System.out.println("StringBuilder for efficient concat: " + csv);
    }
}

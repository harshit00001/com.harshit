package com.harshit.preparation.oop;

/**
 * <h2>Static methods are not overridden — they are hidden</h2>
 * <p>
 * Resolution uses compile-time type of the reference, not runtime class. Instance methods use
 * virtual dispatch (runtime type); static methods do not.
 */
public final class StaticHidingDemo {

    public static void main(String[] args) {
        Parent p = new Child();
        p.show();
        Child c = new Child();
        c.show();
    }

    static class Parent {

        static void show() {
            System.out.println("Parent static");
        }
    }

    static class Child extends Parent {

        static void show() {
            System.out.println("Child static");
        }
    }

    private StaticHidingDemo() {
    }
}

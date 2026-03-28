package com.harshit.preparation.topic03;

/**
 * Topic 03 — Abstract classes vs interfaces, default method conflict.
 */
public final class Topic03Qa {

    private Topic03Qa() {
    }

    /*
     * Q: Abstract class vs interface—when to use what?
     *
     * SCRIPT:
     * I choose an abstract class when I am modeling a small hierarchy that shares real behavior
     * and possibly protected state—think template method pattern where the skeleton is fixed but steps
     * vary. I choose an interface when I want to mark a capability that many unrelated types can
     * implement, or when I need multiple types of behavior mixed into one class. Practically, Spring
     * services often depend on interfaces for testing with mocks, while domain hierarchies sometimes
     * use abstract bases for shared rules.
     */

    /*
     * Q: Default methods conflict in two interfaces?
     *
     * SCRIPT:
     * If two interfaces give me the same default method signature, the compiler forces my class to
     * override that method—I cannot inherit two conflicting defaults blindly. In the override I can
     * call IfA.super.shared() or IfB.super.shared(), or merge behavior. I would explain this as the
     * “diamond problem” solved by explicit resolution in the implementing class.
     *
     * REAL LIFE:
     * Two mix-ins both add logging defaults—you write one log() that delegates or combines them.
     */

    /** Resolves conflicting defaults by delegating to IfA.super. */
    static final class Resolved implements IfA, IfB {
        @Override
        public void shared() {
            IfA.super.shared();
        }
    }

    interface IfA {
        default void shared() {
            System.out.println("A");
        }
    }

    interface IfB {
        default void shared() {
            System.out.println("B");
        }
    }

    /** Resolves the diamond default conflict; prints which delegate ran. */
    public static void demo() {
        new Resolved().shared();
    }

    public static void main(String[] args) {
        demo();
    }
}

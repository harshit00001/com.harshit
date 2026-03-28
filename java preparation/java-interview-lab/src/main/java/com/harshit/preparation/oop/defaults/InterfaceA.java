package com.harshit.preparation.oop.defaults;

/**
 * Default method on interface (Java 8+). Implementing classes inherit unless they override.
 */
public interface InterfaceA {

    default void shared() {
        System.out.println("default from A");
    }
}

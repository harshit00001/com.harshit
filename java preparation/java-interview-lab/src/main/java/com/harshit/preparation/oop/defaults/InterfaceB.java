package com.harshit.preparation.oop.defaults;

/**
 * Same default method name as {@link InterfaceA} — class implementing both must resolve conflict.
 */
public interface InterfaceB {

    default void shared() {
        System.out.println("default from B");
    }
}

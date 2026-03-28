package com.harshit.preparation.oop.defaults;

/**
 * <h2>Default method conflict</h2>
 * <p>
 * If a class implements two interfaces that both define the same default method, the class
 * <b>must override</b> and may delegate with {@code InterfaceName.super.method()}.
 */
public final class DiamondDefaultMethodDemo {

    public static void main(String[] args) {
        new Resolved().shared();
    }

    static final class Resolved implements InterfaceA, InterfaceB {

        @Override
        public void shared() {
            InterfaceA.super.shared();
        }
    }

    private DiamondDefaultMethodDemo() {
    }
}

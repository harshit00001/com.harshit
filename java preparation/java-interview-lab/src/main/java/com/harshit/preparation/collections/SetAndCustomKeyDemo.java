package com.harshit.preparation.collections;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <h2>Interview: How does HashSet find duplicates?</h2>
 * <p>
 * {@link java.util.HashSet} is backed by a {@link java.util.HashMap} internally. The element is the
 * <i>key</i>; a dummy {@code Object} is the value. {@code add(e)} returns {@code false} if the key
 * was already present — that is how "duplicate" is detected.
 * <p>
 * For a <b>custom class</b> in a Set: override {@code equals} and {@code hashCode} using the same
 * fields you consider identity. If the object is <b>mutable</b> and you change a field used in
 * hashCode after insertion, the set can behave incorrectly (element "lost" in wrong bucket).
 */
public final class SetAndCustomKeyDemo {

    public static void main(String[] args) {
        Set<Email> emails = new HashSet<>();
        emails.add(new Email("a@x.com"));
        boolean second = emails.add(new Email("a@x.com"));
        System.out.println("duplicate add rejected? " + !second + " (size=" + emails.size() + ")");
    }

    static final class Email {
        private final String address;

        Email(String address) {
            this.address = address == null ? "" : address.trim().toLowerCase();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Email e)) {
                return false;
            }
            return address.equals(e.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(address);
        }

        @Override
        public String toString() {
            return address;
        }
    }

    private SetAndCustomKeyDemo() {
    }
}

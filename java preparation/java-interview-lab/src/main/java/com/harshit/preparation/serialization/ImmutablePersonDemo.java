package com.harshit.preparation.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <h2>Immutability + serialVersionUID (interview)</h2>
 * <p>
 * <b>Immutable class checklist:</b> final class; final fields; no setters; defensive copies for
 * mutable components (lists, dates in older code).
 * <p>
 * {@link Serializable}: set {@code serialVersionUID} explicitly so class evolution can be managed;
 * otherwise JVM generates one and incompatible changes break deserialization.
 */
public final class ImmutablePersonDemo {

    public static void main(String[] args) {
        ImmutablePerson p = new ImmutablePerson("Ada", List.of("java", "math"));
        System.out.println(p);
        System.out.println("tags are immutable view: " + p.tags());
    }

    /**
     * Immutable person — safe as HashMap key if you only use immutable fields in equals/hashCode.
     */
    public static final class ImmutablePerson implements Serializable {

        private static final long serialVersionUID = 2026_03_28_001L;

        private final String name;
        private final List<String> tags;

        public ImmutablePerson(String name, List<String> tags) {
            this.name = Objects.requireNonNull(name);
            this.tags = List.copyOf(tags);
        }

        public String name() {
            return name;
        }

        /**
         * Returns unmodifiable list — callers cannot add/remove (Java 9+ unmodifiable already).
         */
        public List<String> tags() {
            return tags;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ImmutablePerson that)) {
                return false;
            }
            return name.equals(that.name) && tags.equals(that.tags);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, tags);
        }

        @Override
        public String toString() {
            return "ImmutablePerson{name='" + name + "', tags=" + tags + "}";
        }
    }

    /**
     * Example of defensive copy when you must wrap a mutable list passed in (older style).
     */
    public static final class LegacyStyleImmutable {

        private final List<String> items;

        public LegacyStyleImmutable(List<String> items) {
            this.items = Collections.unmodifiableList(new ArrayList<>(items));
        }

        public List<String> items() {
            return items;
        }
    }

    private ImmutablePersonDemo() {
    }
}

package com.harshit.preparation.topic05;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Topic 05 — Serialization and immutability.
 */
public final class Topic05Qa {

    private Topic05Qa() {
    }

    /*
     * Q: What is serialVersionUID?
     *
     * SCRIPT:
     * When a class implements Serializable, the JVM can serialize it to bytes. serialVersionUID is
     * a version stamp for that class shape. If I do not declare it, the JVM generates one, and any
     * incompatible field change can break deserialization with InvalidClassException. In production
     * I prefer an explicit UID and a disciplined approach to compatible changes so rolling upgrades
     * do not break old data on disk or in caches.
     */

    /*
     * Q: How to make a class immutable?
     *
     * SCRIPT:
     * I would list the checklist: make the class final or prevent subclass misuse, make fields final,
     * set them only in the constructor, do not expose setters, and if a field holds a mutable object
     * like a List or a legacy Date, return defensive copies or unmodifiable views. Immutability
     * makes objects safe to share across threads and safe to use as keys in maps when the identity
     * fields never change.
     */

    public static final class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String name;
        private final List<String> tags;

        public User(String name, List<String> tags) {
            this.name = Objects.requireNonNull(name);
            this.tags = List.copyOf(tags);
        }

        public String name() {
            return name;
        }

        public List<String> tags() {
            return tags;
        }
    }

    public static void demo() {
        User u = new User("demo", List.of("tag1", "tag2"));
        System.out.println("User(name=" + u.name() + ", tags=" + u.tags() + ")");
    }

    public static void main(String[] args) {
        demo();
    }
}

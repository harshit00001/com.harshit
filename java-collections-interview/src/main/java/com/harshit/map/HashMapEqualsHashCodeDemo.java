package com.harshit.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * INTERVIEW Q: Why override both hashCode() AND equals()?
 *
 * <p><b>Contract (from Object):</b>
 * <ul>
 *   <li>If a.equals(b) then a.hashCode() == b.hashCode()</li>
 *   <li>Consistent: same object → same hashCode unless fields used in hashCode change</li>
 * </ul>
 *
 * <p><b>What breaks if you override equals but not hashCode?</b>
 * HashMap/HashSet use hashCode for bucket placement. Equal objects in different buckets
 * → duplicate entries in Set, failed lookups in Map.
 *
 * <p><b>Mutable key trap (4 YOE must-know):</b>
 * Never mutate fields used in equals/hashCode after inserting into HashMap/HashSet.
 * The object stays in the old bucket but "looks like" a different key.
 */
public final class HashMapEqualsHashCodeDemo {

    private HashMapEqualsHashCodeDemo() {}

    public static void main(String[] args) {
        brokenSetWithoutHashCode();
        correctImmutableKey();
        mutableKeyTrap();
    }

    /**
     * Classic Java pitfall: equals overridden, hashCode not → Set allows "duplicates".
     */
    private static void brokenSetWithoutHashCode() {
        System.out.println("=== BROKEN: equals without hashCode ===");

        Set<BrokenPerson> broken = new HashSet<>();
        broken.add(new BrokenPerson("Harshit", 101));
        broken.add(new BrokenPerson("Harshit", 101)); // logically equal

        // Without hashCode override, Object.hashCode uses identity → TWO entries!
        System.out.println("Broken set size (expected 1, actual): " + broken.size());
        System.out.println("→ Interview line: 'Equal objects must share hashCode for hash-based collections.'");
        System.out.println();
    }

    /**
     * Correct pattern: immutable key fields, both methods overridden (or record).
     */
    private static void correctImmutableKey() {
        System.out.println("=== CORRECT: immutable key ===");

        Map<EmployeeKey, String> orgChart = new HashMap<>();
        EmployeeKey key = new EmployeeKey("ACC", 101);
        orgChart.put(key, "Senior Developer");

        // Lookup with equal-but-different instance works because hashCode + equals align
        EmployeeKey lookup = new EmployeeKey("ACC", 101);
        System.out.println("Lookup works: " + orgChart.get(lookup));

        Set<EmployeeKey> uniqueIds = new HashSet<>();
        uniqueIds.add(new EmployeeKey("ACC", 101));
        uniqueIds.add(new EmployeeKey("ACC", 101));
        System.out.println("Set dedup works, size: " + uniqueIds.size()); // 1
        System.out.println();
    }

    /**
     * INTERVIEW Q: What happens if you change a key after putting it in HashMap?
     * ANSWER: Entry remains in old bucket. get(newKey) returns null even though
     * entry still exists — effectively "lost" until rehash/removal.
     */
    private static void mutableKeyTrap() {
        System.out.println("=== MUTABLE KEY TRAP ===");

        Map<MutableKey, String> map = new HashMap<>();
        MutableKey key = new MutableKey(1);
        map.put(key, "important-data");

        System.out.println("Before mutation, get(key): " + map.get(key));
        System.out.println("map size: " + map.size());

        // Mutate field used in hashCode/equals — key identity changes!
        key.setId(999);

        System.out.println("After mutation, get(same reference): " + map.get(key)); // null!
        System.out.println("map size still: " + map.size()); // 1 — orphaned entry in wrong bucket
        System.out.println("→ Production rule: use immutable keys (String, Integer, record, final fields).");
    }

    /** Broken: equals overridden, hashCode uses Object default (identity). */
    static final class BrokenPerson {
        private final String name;
        private final int id;

        BrokenPerson(String name, int id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BrokenPerson other)) return false;
            return id == other.id && name.equals(other.name);
        }
    }

    /** Correct immutable key — safe for HashMap/HashSet. */
    static final class EmployeeKey {
        private final String company;
        private final int empId;

        EmployeeKey(String company, int empId) {
            this.company = company;
            this.empId = empId;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof EmployeeKey other)) return false;
            return empId == other.empId && company.equals(other.company);
        }

        @Override
        public int hashCode() {
            return Objects.hash(company, empId);
        }
    }

    /** Anti-pattern: mutable fields in equals/hashCode. */
    static final class MutableKey {
        private int id;

        MutableKey(int id) {
            this.id = id;
        }

        void setId(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof MutableKey other)) return false;
            return id == other.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}

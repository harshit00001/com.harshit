package com.harshit.preparation.string;

/**
 * <h2>String pool, intern(), == vs equals()</h2>
 * <p>
 * <b>String literals</b> may be interned: the same literal in the same class loader often refers to
 * the same instance in the string pool (implementation detail — do not rely on == for business logic).
 * <p>
 * {@code new String("hi")} creates a <i>separate</i> heap String; {@code "hi" == new String("hi")} is
 * typically false. Always use {@link String#equals} for value comparison.
 * <p>
 * {@link String#intern()} adds/canonicalizes in the pool — use sparingly (memory pressure in older
 * stories; still not for hot paths without profiling).
 * <p>
 * <b>StringBuilder / StringBuffer:</b> mutable char sequences. Builder — not synchronized, faster for
 * single-threaded concatenation. Buffer — synchronized methods, rarely needed today.
 */
public final class StringPoolDemo {

    public static void main(String[] args) {
        String a = "java";
        String b = new String("java");
        System.out.println("a == b: " + (a == b));
        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("a == b.intern(): " + (a == b.intern()));

        StringBuilder sb = new StringBuilder();
        for (String part : new String[] {"order", "-", "123"}) {
            sb.append(part);
        }
        System.out.println("built: " + sb);
    }

    private StringPoolDemo() {
    }
}

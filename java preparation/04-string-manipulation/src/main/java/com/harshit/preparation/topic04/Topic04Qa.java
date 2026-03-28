package com.harshit.preparation.topic04;

/**
 * Topic 04 — String, StringBuilder, StringBuffer, pool.
 */
public final class Topic04Qa {

    private Topic04Qa() {
    }

    /*
     * Q: String vs StringBuilder vs StringBuffer?
     *
     * SCRIPT:
     * String is immutable—every time I concatenate in a loop I may create many intermediate objects,
     * which is wasteful. StringBuilder is mutable and not synchronized, so it is what I use for
     * building strings in a single thread. StringBuffer is like StringBuilder but its methods are
     * synchronized; I only mention it when legacy code or rare shared builders need thread safety,
     * otherwise StringBuilder is the default for performance.
     */

    /*
     * Q: String pool, intern(), == vs equals?
     *
     * SCRIPT:
     * I always tell the panel that for business logic we compare strings with equals, not ==,
     * because == checks reference identity. Literals may be interned and share one instance, but
     * new String("x") is a different object on the heap. The intern() method can put a string in the
     * pool, but I would use it carefully after profiling—uncontrolled interning can stress memory.
     *
     * REAL LIFE:
     * Two printed labels might show the same text; equals says the text matches; == asks if it is literally the same sticker.
     */

    public static void demo() {
        String a = "java";
        String b = new String("java");
        System.out.println(a.equals(b));
        System.out.println(a == b.intern());
        StringBuilder sb = new StringBuilder();
        for (String s : new String[] {"a", "b", "c"}) {
            sb.append(s);
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        demo();
    }
}

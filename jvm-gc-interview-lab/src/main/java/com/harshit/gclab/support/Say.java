package com.harshit.gclab.support;

/**
 * Console formatting shared by every step, so output reads like an interview answer.
 */
public final class Say {

    private static final int WIDTH = 96;

    /**
     * Typographic characters mapped to ASCII before printing. The Windows console defaults to a
     * code page that renders them as '?', and this output is meant to be pasted into notes.
     */
    private static final String[][] ASCII_FALLBACKS = {
            {"\u2014", "-"}, {"\u2013", "-"}, {"\u2018", "'"}, {"\u2019", "'"},
            {"\u201c", "\""}, {"\u201d", "\""}, {"\u2026", "..."}, {"\u2248", "~"},
            {"\u00d7", "x"}, {"\u2022", "*"}
    };

    private Say() {
    }

    public static void banner(String title) {
        System.out.println("=".repeat(WIDTH));
        System.out.println(ascii(title));
        System.out.println("=".repeat(WIDTH));
    }

    public static void section(String title) {
        System.out.println();
        System.out.println("--- " + ascii(title) + " " + "-".repeat(Math.max(0, WIDTH - title.length() - 5)));
    }

    public static void line(String format, Object... args) {
        System.out.println(ascii(args.length == 0 ? format : String.format(format, args)));
    }

    private static String ascii(String text) {
        String result = text;
        for (String[] fallback : ASCII_FALLBACKS) {
            result = result.replace(fallback[0], fallback[1]);
        }
        return result;
    }

    public static void blank() {
        System.out.println();
    }

    /** The "what to say in the interview" takeaway that each step ends with. */
    public static void takeaway(String... points) {
        System.out.println();
        System.out.println("INTERVIEW TAKEAWAY");
        for (String point : points) {
            System.out.println("  * " + ascii(point));
        }
    }

    public static String bytes(long value) {
        if (value < 0) {
            return "n/a";
        }
        double kb = value / 1024.0;
        if (kb < 1) {
            return value + " B";
        }
        double mb = kb / 1024.0;
        if (mb < 1) {
            return String.format("%.1f KB", kb);
        }
        double gb = mb / 1024.0;
        return gb < 1 ? String.format("%.1f MB", mb) : String.format("%.2f GB", gb);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

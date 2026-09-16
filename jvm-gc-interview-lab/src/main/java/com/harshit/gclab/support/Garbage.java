package com.harshit.gclab.support;

/**
 * Allocation workloads. {@code byte[]} is used deliberately: its size is predictable, and the JIT
 * cannot optimise the allocation away once the array escapes into {@link #sink}.
 */
public final class Garbage {

    /** Keeps allocations reachable long enough that escape analysis cannot delete them. */
    public static volatile Object sink;

    private Garbage() {
    }

    /** Allocates and immediately abandons {@code chunks} arrays — pure short-lived garbage. */
    public static void churn(int chunks, int bytesPerChunk) {
        for (int i = 0; i < chunks; i++) {
            sink = new byte[bytesPerChunk];
        }
    }

    /** A "request payload" of realistic shape, so heap dumps show something recognisable. */
    public static byte[] payload(int bytes) {
        byte[] data = new byte[bytes];
        data[0] = 1;
        data[bytes - 1] = 1;
        return data;
    }

    public static int mb(int megabytes) {
        return megabytes * 1024 * 1024;
    }

    public static int kb(int kilobytes) {
        return kilobytes * 1024;
    }

    /**
     * Requests a full collection and waits briefly. Deliberately named to make the point that
     * {@code System.gc()} is only a hint — production code should never rely on it.
     */
    public static void requestFullGcForDemoOnly() {
        System.gc();
        Say.sleep(120);
    }
}

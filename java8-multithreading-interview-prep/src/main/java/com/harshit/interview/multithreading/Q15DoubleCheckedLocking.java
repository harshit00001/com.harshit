package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

/**
 * INTERVIEW Q: Explain double-checked locking. Why is volatile required?
 *
 * <p>Without volatile, another thread may see partially constructed singleton (instruction reordering).
 *
 * <p><b>Modern alternative:</b> static holder idiom or enum singleton — prefer in interviews.
 */
public final class Q15DoubleCheckedLocking implements InterviewDemo {

    /** Nested type keeps demo class instantiable while singleton ctor stays private. */
    static final class Singleton {
        private static volatile Singleton instance;

        private Singleton() {}

        static Singleton getInstance() {
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }

        static class Holder {
            static final Singleton INSTANCE = new Singleton();
        }
    }

    @Override
    public void run() throws InterruptedException {
        System.out.println("=== Q15: Double-Checked Locking ===\n");

        Runnable task = () -> System.out.println("DCL instance: " + Singleton.getInstance().hashCode());
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Holder instance: " + Singleton.Holder.INSTANCE.hashCode());
        System.out.println("\n→ volatile + DCL works but holder pattern is simpler and safe without sync on hot path.");
    }

    public static void main(String[] args) throws Exception {
        new Q15DoubleCheckedLocking().run();
    }
}

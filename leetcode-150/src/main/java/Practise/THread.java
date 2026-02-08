package Practise;

import java.lang.*;
class Counter {
    int count = 0;

    synchronized void increment() {
        System.out.println(count);
        count++;

    }
}
public class THread extends Thread {
    public static void main(String[] args) throws Exception {
        Counter c = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 6; i < 12; i++) {
                System.out.println("i: "+i);
                c.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("i: "+i);
                c.increment();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(c.count);
        Runnable task = () -> {
            System.out.println("Running in thread: "
                    + Thread.currentThread().getName());
        };

        Thread t3 = new Thread(task);
        t3.start();

        System.out.println("Main thread: "
                + Thread.currentThread().getName());
    }
}



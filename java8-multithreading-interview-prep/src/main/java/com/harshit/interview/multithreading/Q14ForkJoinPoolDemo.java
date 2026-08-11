package com.harshit.interview.multithreading;

import com.harshit.interview.common.InterviewDemo;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * INTERVIEW Q: What is ForkJoinPool? Relation to parallelStream?
 *
 * <p><b>Answer:</b> Work-stealing pool for divide-and-conquer. {@code parallelStream()} uses
 * common pool ({@code ForkJoinPool.commonPool()}).
 *
 * <p><b>4 YOE:</b> Know when to use custom pool vs default; avoid blocking IO inside FJP tasks.
 */
public final class Q14ForkJoinPoolDemo implements InterviewDemo {

    static class SumTask extends RecursiveTask<Long> {
        private final int[] data;
        private final int start;
        private final int end;
        private static final int THRESHOLD = 4;

        SumTask(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += data[i];
                }
                return sum;
            }
            int mid = (start + end) / 2;
            SumTask left = new SumTask(data, start, mid);
            SumTask right = new SumTask(data, mid, end);
            left.fork();
            long rightSum = right.compute();
            long leftSum = left.join();
            return leftSum + rightSum;
        }
    }

    @Override
    public void run() {
        System.out.println("=== Q14: ForkJoinPool ===\n");

        int[] data = {1, 2, 3, 4, 5, 6, 7, 8};

        ForkJoinPool pool = new ForkJoinPool(2);
        try {
            long sum = pool.invoke(new SumTask(data, 0, data.length));
            System.out.println("ForkJoin sum: " + sum);
        } finally {
            pool.shutdown();
        }

        long parallelSum = java.util.Arrays.stream(data).parallel().sum();
        System.out.println("parallelStream sum: " + parallelSum);

        System.out.println("\n→ Blocking calls in parallelStream stall common pool — use dedicated Executor for mixed workloads.");
    }

    public static void main(String[] args) throws Exception {
        new Q14ForkJoinPoolDemo().run();
    }
}

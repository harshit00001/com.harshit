package com.harshit.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SPRING BATCH + SPRING SCHEDULER APPLICATION
 * 
 * @EnableScheduling: Enables Spring's scheduled task execution capability
 * 
 * Simple Explanation:
 * - This annotation tells Spring: "Hey, I want to run tasks automatically"
 * - Without it, @Scheduled annotations won't work
 */
@SpringBootApplication
@EnableScheduling  // Interview Point: Must enable scheduling
public class SpringBatchSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchSchedulerApplication.class, args);
        System.out.println("\n=== Spring Batch + Scheduler Application Started ===");
        System.out.println("CSV processing job will run automatically based on schedule");
    }
}


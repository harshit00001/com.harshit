package com.harshit.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * SPRING SCHEDULER - Interview Explanation:
 * 
 * Technical Definition:
 * - Automates task execution at defined intervals
 * - Uses @Scheduled annotation
 * - Supports cron expressions, fixed delay, fixed rate
 * - Runs in background thread pool
 * 
 * Simple Explanation:
 * - Like an alarm clock for your code
 * - Runs code automatically at specific times
 * - No manual trigger needed
 * 
 * Integration with Spring Batch:
 * - Scheduler triggers Batch job at scheduled time
 * - JobLauncher starts the batch job
 * - Job runs automatically (e.g., every night at midnight)
 */
@Component
public class BatchJobScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job processUserJob;

    /**
     * Interview Point: @Scheduled with Cron Expression
     * 
     * Technical: Cron expression defines when to run
     * Format: second minute hour day month weekday
     * 
     * "0 0 0 * * ?" means:
     * - 0 seconds
     * - 0 minutes
     * - 0 hours (midnight)
     * - Every day
     * - Every month
     * - Any weekday
     * 
     * Simple: "Run every night at 12:00 AM"
     */
    @Scheduled(cron = "0 0 0 * * ?")  // Interview Point: Runs at midnight
    public void scheduleMidnightJob() {
        System.out.println("\n=== Scheduled Job Triggered at Midnight ===");
        runBatchJob("MIDNIGHT_SCHEDULE");
    }

    /**
     * Interview Point: @Scheduled with Fixed Delay
     * 
     * Technical: Runs after fixed delay from previous completion
     * 
     * Simple: "Wait 5 minutes after previous job finishes, then run again"
     * 
     * Note: Commented out to avoid running too frequently in demo
     */
    // @Scheduled(fixedDelay = 300000) // 5 minutes in milliseconds
    public void scheduleFixedDelayJob() {
        System.out.println("\n=== Scheduled Job (Fixed Delay) ===");
        runBatchJob("FIXED_DELAY");
    }

    /**
     * Interview Point: @Scheduled with Fixed Rate
     * 
     * Technical: Runs at fixed interval regardless of execution time
     * 
     * Simple: "Run every 10 minutes, no matter how long previous job took"
     * 
     * Note: Commented out to avoid running too frequently in demo
     */
    // @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void scheduleFixedRateJob() {
        System.out.println("\n=== Scheduled Job (Fixed Rate) ===");
        runBatchJob("FIXED_RATE");
    }

    /**
     * Interview Point: Manual Trigger (for testing)
     * Can be called via REST endpoint or manually
     */
    @Scheduled(cron = "0 */1 * * * ?") // Every minute (for demo purposes)
    public void scheduleDemoJob() {
        System.out.println("\n=== Demo Scheduled Job (Every Minute) ===");
        runBatchJob("DEMO_SCHEDULE");
    }

    /**
     * Interview Point: Run Batch Job
     * 
     * Technical: Uses JobLauncher to start batch job
     * Creates unique job parameters for each run
     * 
     * Simple: "Start the batch job with unique ID"
     */
    private void runBatchJob(String scheduleType) {
        try {
            // Interview Point: Create unique job parameters
            // Each run needs unique parameters (timestamp)
            JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("scheduleType", scheduleType)
                .toJobParameters();

            // Interview Point: Launch the batch job
            jobLauncher.run(processUserJob, jobParameters);
            
            System.out.println("  ✓ Batch job completed successfully");
        } catch (Exception e) {
            System.err.println("  ✗ Error running batch job: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/**
 * INTERVIEW SUMMARY: Spring Scheduler
 * 
 * Key Points:
 * 1. @Scheduled: Annotation to schedule methods
 * 2. Cron Expression: "0 0 0 * * ?" = midnight daily
 * 3. Fixed Delay: Wait after previous completion
 * 4. Fixed Rate: Run at fixed interval
 * 5. @EnableScheduling: Must enable in main class
 * 
 * Cron Expression Format:
 * second minute hour day month weekday
 * 
 * Examples:
 * - "0 0 0 * * ?" = Every day at midnight
 * - "0 0 12 * * ?" = Every day at noon
 * - "0 0 0 1 * ?" = First day of every month at midnight
 * - "0 0 0 ? * MON" = Every Monday at midnight
 * 
 * Integration Flow:
 * Scheduler → JobLauncher → Batch Job → Step → Reader/Processor/Writer
 */


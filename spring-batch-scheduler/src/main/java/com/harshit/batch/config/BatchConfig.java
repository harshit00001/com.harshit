package com.harshit.batch.config;

import com.harshit.batch.model.User;
import com.harshit.batch.processor.UserItemProcessor;
import com.harshit.batch.reader.UserItemReader;
import com.harshit.batch.writer.UserItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SPRING BATCH CONFIGURATION - Interview Explanation:
 * 
 * Spring Batch: Framework for processing large volumes of data
 * 
 * Technical Definition:
 * - Handles batch processing (reading, processing, writing data)
 * - Provides infrastructure for robust batch jobs
 * - Supports chunk-based processing, transaction management, job restart
 * 
 * Simple Explanation:
 * - Like an assembly line for data
 * - Read data → Process/Transform → Write data
 * - Handles large amounts of data efficiently
 * 
 * Key Components:
 * 1. ItemReader: Reads data (from CSV, database, etc.)
 * 2. ItemProcessor: Transforms/validates data
 * 3. ItemWriter: Writes data (to database, file, etc.)
 * 4. Job: Complete workflow (contains steps)
 * 5. Step: One unit of work (Reader → Processor → Writer)
 */
@Configuration
@EnableBatchProcessing  // Interview Point: Enables Spring Batch features
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * Interview Point: ItemReader Bean
     * 
     * Technical: Reads data from source (CSV file in our case)
     * Simple: Like reading a book - goes through each line/record
     */
    @Bean
    public UserItemReader reader() {
        return new UserItemReader();
    }

    /**
     * Interview Point: ItemProcessor Bean
     * 
     * Technical: Transforms/validates each item
     * Simple: Like editing - takes raw data, makes it better
     */
    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    /**
     * Interview Point: ItemWriter Bean
     * 
     * Technical: Writes processed data to destination (database)
     * Simple: Like saving - puts processed data where it needs to go
     */
    @Bean
    public UserItemWriter writer() {
        return new UserItemWriter();
    }

    /**
     * Interview Point: Step Definition
     * 
     * Technical: One unit of work in a batch job
     * Contains: Reader → Processor → Writer
     * 
     * Simple: One complete cycle of read-process-write
     * 
     * chunk(10): Process 10 items at a time (transaction boundary)
     */
    @Bean
    public Step processUserStep() {
        return stepBuilderFactory.get("processUserStep")
            .<User, User>chunk(10)  // Interview Point: Process 10 items at a time
            .reader(reader())       // Read from CSV
            .processor(processor()) // Transform data
            .writer(writer())       // Write to database
            .build();
    }

    /**
     * Interview Point: Job Definition
     * 
     * Technical: Complete batch job containing one or more steps
     * 
     * Simple: The entire task/workflow
     * 
     * RunIdIncrementer: Allows job to run multiple times (each run gets unique ID)
     */
    @Bean
    public Job processUserJob() {
        return jobBuilderFactory.get("processUserJob")
            .incrementer(new RunIdIncrementer())  // Interview Point: Allows multiple runs
            .flow(processUserStep())               // Add step to job
            .end()
            .build();
    }
}

/**
 * INTERVIEW SUMMARY: Spring Batch Configuration
 * 
 * Key Points:
 * 1. @EnableBatchProcessing: Enables Spring Batch
 * 2. Job: Complete workflow (contains steps)
 * 3. Step: One unit of work (Reader → Processor → Writer)
 * 4. Chunk: Process N items at a time (transaction boundary)
 * 5. Reader: Reads data from source
 * 6. Processor: Transforms/validates data
 * 7. Writer: Writes data to destination
 * 
 * Flow:
 * CSV File → Reader → Processor → Writer → Database
 */


package com.harshit.springboot.advanced;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * BATCH PROCESSING - Spring Batch Example
 * 
 * Batch processing is a technique where a large volume of data is processed in
 * chunks or batches, instead of handling each record one by one in real time.
 * It's commonly used for tasks like data migration, report generation, billing,
 * or cleanup jobs - basically, anything that doesn't need immediate user interaction.
 * 
 * The main advantage of batch processing is that it improves performance and
 * reduces memory usage. Instead of loading all records into memory at once, you
 * process them in smaller chunks. For example, if you have 10,000 records to
 * process, you can read 100 records at a time, process them, write the results,
 * and then move to the next 100 records. This approach is much more efficient
 * than processing all 10,000 records at once.
 * 
 * Spring Batch is a framework that provides infrastructure for batch processing
 * in Java. It handles common batch processing concerns like reading data, processing
 * it in chunks, writing results, transaction management, job scheduling, and error
 * handling. This allows you to focus on your business logic rather than the
 * infrastructure code needed for batch processing.
 */
@Configuration
@EnableBatchProcessing
public class BatchProcessingExample {
    
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    
    public BatchProcessingExample(JobBuilderFactory jobBuilderFactory, 
                                  StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }
    
    /**
     * BATCH JOB DEFINITION
     * 
     * A batch job consists of one or more steps. Each step typically has a reader
     * that reads data, a processor that processes it, and a writer that writes
     * the results. The job orchestrates these steps and handles the overall
     * execution flow.
     */
    @Bean
    public Job dataProcessingJob() {
        // Interview Point: Job is the top-level container for batch processing
        // Can have multiple steps that execute in sequence
        return jobBuilderFactory.get("dataProcessingJob")
                .start(processDataStep())  // First step
                .build();
    }
    
    /**
     * BATCH STEP DEFINITION
     * 
     * A step is a domain object that encapsulates an independent, sequential phase
     * of a batch job. Each step has a reader, processor, and writer. The chunk
     * size determines how many items are processed together before committing
     * the transaction.
     */
    @Bean
    public Step processDataStep() {
        // Interview Point: Step processes data in chunks
        // chunk(10) means process 10 items at a time
        return stepBuilderFactory.get("processDataStep")
                .<String, String>chunk(10)  // Process 10 items per chunk
                .reader(itemReader())        // Read data
                .processor(itemProcessor())  // Process data
                .writer(itemWriter())        // Write results
                .build();
    }
    
    /**
     * ITEM READER
     * 
     * The reader is responsible for reading data from a source. It could read from
     * a database, file, message queue, or any other data source. Spring Batch provides
     * many built-in readers for common scenarios, or you can implement a custom reader.
     */
    @Bean
    public ItemReader<String> itemReader() {
        // Interview Point: Reader reads data from source
        // In real application, might read from database, file, etc.
        List<String> data = Arrays.asList(
            "Item1", "Item2", "Item3", "Item4", "Item5",
            "Item6", "Item7", "Item8", "Item9", "Item10",
            "Item11", "Item12", "Item13", "Item14", "Item15"
        );
        return new ListItemReader<>(data);
    }
    
    /**
     * ITEM PROCESSOR
     * 
     * The processor is responsible for transforming or validating items. It receives
     * an item from the reader, processes it, and returns the processed item. If
     * the processor returns null, the item is filtered out and not passed to the
     * writer. This is useful for filtering invalid data.
     */
    @Bean
    public ItemProcessor<String, String> itemProcessor() {
        // Interview Point: Processor transforms/validates data
        // Can return null to filter out items
        return item -> {
            System.out.println("Processing: " + item);
            return "Processed " + item.toUpperCase();
        };
    }
    
    /**
     * ITEM WRITER
     * 
     * The writer is responsible for writing processed items to a destination. It
     * could write to a database, file, message queue, or any other destination.
     * Spring Batch provides many built-in writers, or you can implement a custom
     * writer. The writer receives a list of items (the chunk size) and writes
     * them together, which is more efficient than writing one item at a time.
     */
    @Bean
    public ItemWriter<String> itemWriter() {
        // Interview Point: Writer writes processed items
        // Receives chunk of items (10 in this case) and writes them together
        return items -> {
            System.out.println("Writing chunk of " + items.size() + " items:");
            for (String item : items) {
                System.out.println("  Written: " + item);
            }
        };
    }
}


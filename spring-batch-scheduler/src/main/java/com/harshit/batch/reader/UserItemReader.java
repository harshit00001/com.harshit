package com.harshit.batch.reader;

import com.harshit.batch.model.User;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

/**
 * ITEM READER - Interview Explanation:
 * 
 * Technical Definition:
 * - Reads data from a source (CSV file, database, etc.)
 * - Implements ItemReader interface
 * - Returns one item at a time
 * - Returns null when no more items
 * 
 * Simple Explanation:
 * - Like reading a book line by line
 * - Reads one record at a time from CSV
 * - Stops when file ends (returns null)
 * 
 * In our example:
 * - Reads user data from CSV file
 * - Each line becomes a User object
 */
public class UserItemReader implements ItemReader<User> {

    private FlatFileItemReader<User> reader;
    private boolean initialized = false;

    /**
     * Interview Point: read() method
     * 
     * Technical: Returns next item from source, null when done
     * Simple: "Give me the next record, or null if done"
     */
    @Override
    public User read() throws Exception, UnexpectedInputException, 
            ParseException, NonTransientResourceException {
        
        if (!initialized) {
            initializeReader();
            initialized = true;
        }
        
        return reader.read();
    }

    /**
     * Interview Point: Initialize CSV Reader
     * 
     * Technical: Configures FlatFileItemReader to read CSV
     * Simple: Sets up the CSV reader with file location and mapping
     */
    private void initializeReader() {
        reader = new FlatFileItemReader<>();
        
        // Interview Point: Set CSV file location
        // In real app, this could be from file system, S3, etc.
        reader.setResource(new ClassPathResource("users.csv"));
        
        // Interview Point: Skip header line
        reader.setLinesToSkip(1);
        
        // Interview Point: Configure line mapper (how to parse each line)
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        
        // Interview Point: Tokenizer - splits line by comma
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("id", "name", "email", "age", "department");
        
        // Interview Point: Field mapper - maps CSV columns to User object
        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
        
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        reader.setLineMapper(lineMapper);
        
        System.out.println("  → CSV Reader initialized");
        System.out.println("  → Reading from: users.csv");
        System.out.println("  → Columns: id, name, email, age, department");
    }
}

/**
 * INTERVIEW SUMMARY: ItemReader
 * 
 * Key Points:
 * 1. Reads data from source (CSV, DB, etc.)
 * 2. Returns one item at a time
 * 3. Returns null when done
 * 4. Can read from files, databases, APIs, etc.
 * 
 * In this example:
 * - Reads CSV file line by line
 * - Converts each line to User object
 * - Handles header row automatically
 */


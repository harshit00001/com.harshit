package com.harshit.batch.processor;

import com.harshit.batch.model.User;
import org.springframework.batch.item.ItemProcessor;

/**
 * ITEM PROCESSOR - Interview Explanation:
 * 
 * Technical Definition:
 * - Transforms or validates items between reading and writing
 * - Implements ItemProcessor<I, O> interface
 * - Input type (I) and Output type (O) can be different
 * - Can filter items (return null to skip)
 * 
 * Simple Explanation:
 * - Like editing/cleaning data
 * - Takes raw data, makes it better
 * - Can skip bad data (return null)
 * 
 * In our example:
 * - Converts names to uppercase
 * - Validates email format
 * - Filters invalid records
 */
public class UserItemProcessor implements ItemProcessor<User, User> {

    /**
     * Interview Point: process() method
     * 
     * Technical: Transforms input item to output item
     * Simple: "Take this data, clean it up, return it"
     * 
     * Can return null to skip/filter the item
     */
    @Override
    public User process(User user) throws Exception {
        System.out.println("  Processing: " + user);
        
        // Interview Point: Transform data
        // Convert name to uppercase
        if (user.getName() != null) {
            user.setName(user.getName().toUpperCase());
        }
        
        // Interview Point: Validate data
        // Skip invalid emails
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            System.out.println("    ✗ Invalid email, skipping: " + user.getEmail());
            return null; // Interview Point: Return null to skip this item
        }
        
        // Interview Point: Business logic
        // Set default department if missing
        if (user.getDepartment() == null || user.getDepartment().isEmpty()) {
            user.setDepartment("General");
        }
        
        System.out.println("    ✓ Processed: " + user);
        return user;
    }
}

/**
 * INTERVIEW SUMMARY: ItemProcessor
 * 
 * Key Points:
 * 1. Transforms/validates data
 * 2. Input and output can be different types
 * 3. Return null to skip/filter item
 * 4. Optional (can skip processor if not needed)
 * 
 * Common Use Cases:
 * - Data transformation (uppercase, formatting)
 * - Data validation (check rules)
 * - Data enrichment (add calculated fields)
 * - Data filtering (skip invalid records)
 */


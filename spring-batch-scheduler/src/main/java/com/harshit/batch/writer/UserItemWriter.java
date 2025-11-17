package com.harshit.batch.writer;

import com.harshit.batch.model.User;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ITEM WRITER - Interview Explanation:
 * 
 * Technical Definition:
 * - Writes processed items to destination (database, file, etc.)
 * - Implements ItemWriter<T> interface
 * - Receives a list of items (chunk size)
 * - Writes all items in one transaction
 * 
 * Simple Explanation:
 * - Like saving data
 * - Takes processed data, saves it somewhere
 * - Writes multiple items at once (chunk)
 * 
 * In our example:
 * - Writes User objects to database
 * - Uses JDBC for database operations
 */
@Component
public class UserItemWriter implements ItemWriter<User> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Interview Point: write() method
     * 
     * Technical: Writes list of items to destination
     * Simple: "Save these items to database"
     * 
     * Receives chunk of items (e.g., 10 items at a time)
     * All items in chunk are written in one transaction
     */
    @Override
    public void write(List<? extends User> users) throws Exception {
        System.out.println("  Writing chunk of " + users.size() + " users to database");
        
        // Interview Point: Batch insert for efficiency
        String sql = "INSERT INTO users (id, name, email, age, department) VALUES (?, ?, ?, ?, ?)";
        
        for (User user : users) {
            jdbcTemplate.update(sql,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getDepartment()
            );
            System.out.println("    ✓ Saved: " + user.getName());
        }
        
        System.out.println("  → Chunk written successfully");
    }
}

/**
 * INTERVIEW SUMMARY: ItemWriter
 * 
 * Key Points:
 * 1. Writes data to destination (DB, file, etc.)
 * 2. Receives chunk of items (not one at a time)
 * 3. All items in chunk written in one transaction
 * 4. Can write to database, file, message queue, etc.
 * 
 * Transaction Management:
 * - If one item fails, entire chunk is rolled back
 * - Ensures data consistency
 */


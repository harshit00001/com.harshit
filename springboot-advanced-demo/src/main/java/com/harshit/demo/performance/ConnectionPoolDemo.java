package com.harshit.demo.performance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * STRATEGY 1: Connection Pooling - Complete Implementation
 * 
 * This class shows actual code for using connection pooling
 * Connections are reused from the pool, not created each time
 */
@Component
public class ConnectionPoolDemo {
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * Get connection from pool and use it
     * Connection is automatically returned to pool when closed
     */
    public void useConnectionFromPool() throws SQLException {
        // Get connection from HikariCP pool
        // This connection is reused, not newly created
        Connection conn = dataSource.getConnection();
        
        try {
            // Use connection for database operations
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Total users: " + count);
            }
            
            rs.close();
            stmt.close();
        } finally {
            // Return connection to pool
            // HikariCP reuses this connection for next request
            conn.close();
        }
    }
    
    /**
     * Multiple connections from pool
     * Shows how pool manages multiple concurrent connections
     */
    public void useMultipleConnections() throws SQLException {
        // Get first connection
        Connection conn1 = dataSource.getConnection();
        System.out.println("Connection 1: " + conn1.getClass().getName());
        
        // Get second connection (from pool)
        Connection conn2 = dataSource.getConnection();
        System.out.println("Connection 2: " + conn2.getClass().getName());
        
        // Both connections are from the pool
        // Pool size is configured in application.properties:
        // spring.datasource.hikari.maximum-pool-size=20
        // spring.datasource.hikari.minimum-idle=5
        
        // Return connections to pool
        conn1.close();
        conn2.close();
    }
    
    /**
     * Connection pool configuration is in application.properties:
     * 
     * spring.datasource.hikari.maximum-pool-size=20  # Max connections
     * spring.datasource.hikari.minimum-idle=5        # Min idle connections
     * spring.datasource.hikari.connection-timeout=30000  # Wait time
     * spring.datasource.hikari.idle-timeout=600000   # Idle timeout
     * spring.datasource.hikari.max-lifetime=1800000  # Max connection lifetime
     */
}


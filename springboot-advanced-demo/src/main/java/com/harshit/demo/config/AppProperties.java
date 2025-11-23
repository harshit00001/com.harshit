package com.harshit.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * QUESTION 8: What is @ConfigurationProperties and how is it used?
 * 
 * @ConfigurationProperties binds properties from application.properties to Java objects
 * Provides type-safe, structured configuration management
 */
@ConfigurationProperties(prefix = "app")
@Component
@Validated
public class AppProperties {
    
    @NotNull
    private String name;
    
    private String version;
    
    private Database database;
    
    private List<String> features;
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public Database getDatabase() { return database; }
    public void setDatabase(Database database) { this.database = database; }
    
    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
    
    /**
     * NESTED CONFIGURATION CLASS
     * Represents app.database.* properties
     */
    public static class Database {
        @NotNull
        private String url;
        
        private String username;
        
        private String password;
        
        @Min(1)
        private Integer maxConnections;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public Integer getMaxConnections() { return maxConnections; }
        public void setMaxConnections(Integer maxConnections) { 
            this.maxConnections = maxConnections; 
        }
    }
}


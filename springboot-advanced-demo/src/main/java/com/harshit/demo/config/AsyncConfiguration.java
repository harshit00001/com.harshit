package com.harshit.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * STRATEGY 8: Thread Pool Configuration
 * 
 * Custom thread pool configuration for @Async methods
 * This ensures optimal performance for asynchronous operations
 */
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
    
    /**
     * Configure custom thread pool for async operations
     * This provides better control over thread management
     */
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Core number of threads that are always active
        executor.setCorePoolSize(10);
        
        // Maximum number of threads that can be created
        executor.setMaxPoolSize(20);
        
        // Queue capacity for pending tasks
        executor.setQueueCapacity(100);
        
        // Thread name prefix for easier debugging
        executor.setThreadNamePrefix("async-");
        
        // Wait for tasks to complete on shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // Maximum time to wait for task completion on shutdown
        executor.setAwaitTerminationSeconds(60);
        
        // Initialize the executor
        executor.initialize();
        
        return executor;
    }
}


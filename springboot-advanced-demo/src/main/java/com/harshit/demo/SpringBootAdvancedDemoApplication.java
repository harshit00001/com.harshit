package com.harshit.demo;

import com.harshit.demo.performance.PerformanceOptimizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class SpringBootAdvancedDemoApplication implements CommandLineRunner {

    @Autowired
    private PerformanceOptimizedService performanceService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdvancedDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=========================================");
        System.out.println("🚀 Spring Boot Performance Demo Started!");
        System.out.println("=========================================");
        System.out.println("📊 Actuator: http://localhost:8080/actuator");
        System.out.println("💚 Health: http://localhost:8080/actuator/health");
        System.out.println("📈 Metrics: http://localhost:8080/actuator/metrics");
        System.out.println("=========================================\n");
        
        // Run performance demonstration
        Thread.sleep(2000); // Wait for application to fully start
        performanceService.demonstratePerformance();
    }
}


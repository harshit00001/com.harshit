package com.harshit.demo;

import com.harshit.demo.service.StepByStepDemoService;
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
    private StepByStepDemoService demoService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdvancedDemoApplication.class, args);
    }

    @Override
    @org.springframework.core.annotation.Order(2)
    public void run(String... args) throws Exception {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🚀 SPRING BOOT PERFORMANCE OPTIMIZATION DEMO");
        System.out.println("=".repeat(70));
        System.out.println("\n📚 Available Endpoints:");
        System.out.println("   GET  /api/demo/cache/{id}              - Caching demonstration");
        System.out.println("   POST /api/demo/async?email=test@ex.com - Async processing");
        System.out.println("   GET  /api/demo/pagination?page=0&size=10 - Pagination");
        System.out.println("   GET  /api/demo/connection-pool         - Connection pooling");
        System.out.println("   GET  /api/demo/lazy-loading            - Lazy loading");
        System.out.println("   GET  /api/demo/cache-operations/{id}   - Cache operations");
        System.out.println("   GET  /api/demo/all                     - All strategies");
        System.out.println("\n💾 Database Console:");
        System.out.println("   http://localhost:8080/h2-console");
        System.out.println("   JDBC URL: jdbc:h2:mem:testdb");
        System.out.println("   Username: sa, Password: (empty)");
        System.out.println("\n🔍 Hibernate Info:");
        System.out.println("   http://localhost:8080/api/hibernate/info - Dialect & SQL info");
        System.out.println("   SQL queries are logged in console (check terminal)");
        System.out.println("\n📊 Monitoring:");
        System.out.println("   http://localhost:8080/actuator");
        System.out.println("   http://localhost:8080/actuator/health");
        System.out.println("   http://localhost:8080/actuator/metrics");
        System.out.println("\n" + "=".repeat(70));
        System.out.println("\n⏳ Starting automatic demonstration in 3 seconds...\n");
        
        Thread.sleep(3000);
        
        // Run complete step-by-step demonstration
        demoService.demonstrateAllStrategies();
        
        System.out.println("\n✅ Application ready! Use the endpoints above to see each strategy in action.");
        System.out.println("💡 Tip: Call the same endpoint multiple times to see caching in action!\n");
    }
}


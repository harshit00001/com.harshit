package com.harshit.demo.config;

import com.harshit.demo.entity.User;
import com.harshit.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Initialize test data for demonstrations
 * Runs before the main demo
 */
@Component
@Order(1)
public class DataInitializer implements ApplicationRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Only initialize if database is empty
        if (userRepository.count() == 0) {
            System.out.println("\n📝 Initializing test data...");
            
            // Create test users
            for (int i = 1; i <= 25; i++) {
                User user = new User();
                user.setName("User " + i);
                user.setEmail("user" + i + "@example.com");
                user.setAge(20 + i);
                userRepository.save(user);
            }
            
            System.out.println("✅ Created 25 test users for demonstration");
            System.out.println("   You can now test caching, pagination, etc.\n");
        }
    }
}


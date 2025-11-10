package com.harshit.springbeans.demo;

/**
 * ============================================
 * COMPLETE BEAN DEMONSTRATION
 * ============================================
 * 
 * This is the MAIN DEMO class that shows:
 * 1. How to create Spring ApplicationContext
 * 2. How to retrieve beans
 * 3. How different bean scopes work
 * 4. How bean lifecycle works
 * 5. How dependency injection works
 * 
 * RUN THIS CLASS TO SEE EVERYTHING IN ACTION!
 */

import com.harshit.springbeans.basic.*;
import com.harshit.springbeans.scopes.*;
import com.harshit.springbeans.lifecycle.*;
import com.harshit.springbeans.dependencyinjection.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * STEP 1: Create Configuration Class
 * 
 * @Configuration tells Spring this is a configuration class
 * @ComponentScan tells Spring where to look for @Component beans
 * 
 * basePackages = "com.harshit.springbeans" means:
 * - Scan all packages starting with com.harshit.springbeans
 * - Find all classes with @Component, @Service, @Repository, etc.
 * - Create beans for those classes
 */
@Configuration
@ComponentScan(basePackages = "com.harshit.springbeans")
public class BeanDemoApplication {
    
    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("     SPRING BEAN DEMONSTRATION - FOR INTERVIEWS");
        System.out.println("================================================================");
        System.out.println();
        
        // ============================================
        // STEP 2: CREATE APPLICATION CONTEXT
        // ============================================
        System.out.println("================================================================");
        System.out.println("STEP 1: CREATING APPLICATION CONTEXT");
        System.out.println("================================================================");
        System.out.println("ApplicationContext is the Spring IoC Container");
        System.out.println("It manages all beans - creation, storage, injection");
        System.out.println();
        
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanDemoApplication.class);
        
        System.out.println();
        System.out.println("[OK] ApplicationContext created!");
        System.out.println("[OK] All beans have been created and initialized");
        System.out.println();
        
        // ============================================
        // STEP 3: RETRIEVE BEANS BY TYPE
        // ============================================
        System.out.println("================================================================");
        System.out.println("STEP 2: RETRIEVING BEANS BY TYPE");
        System.out.println("================================================================");
        
        UserService userService = context.getBean(UserService.class);
        ProductService productService = context.getBean(ProductService.class);
        OrderRepository orderRepository = context.getBean(OrderRepository.class);
        
        System.out.println("[OK] Retrieved UserService bean");
        System.out.println("[OK] Retrieved ProductService bean");
        System.out.println("[OK] Retrieved OrderRepository bean");
        System.out.println();
        
        userService.processUser("John Doe");
        productService.addProduct("Laptop");
        orderRepository.saveOrder("ORD-001");
        System.out.println();
        
        // ============================================
        // STEP 4: RETRIEVE BEANS BY NAME
        // ============================================
        System.out.println("================================================================");
        System.out.println("STEP 3: RETRIEVING BEANS BY NAME");
        System.out.println("================================================================");
        
        CustomBeanName customBean = (CustomBeanName) context.getBean("myCustomService");
        System.out.println("[OK] Retrieved bean by custom name: 'myCustomService'");
        customBean.doSomething();
        System.out.println();
        
        // ============================================
        // STEP 5: BEAN SCOPES DEMONSTRATION
        // ============================================
        System.out.println("================================================================");
        System.out.println("STEP 4: BEAN SCOPES - SINGLETON vs PROTOTYPE");
        System.out.println("================================================================");
        
        // Singleton - Same instance
        SingletonBean singleton1 = context.getBean(SingletonBean.class);
        SingletonBean singleton2 = context.getBean(SingletonBean.class);
        
        System.out.println("SINGLETON SCOPE:");
        System.out.println("  Getting bean first time...");
        singleton1.increment();
        System.out.println("  Getting bean second time...");
        singleton2.increment();
        System.out.println("  Counter value: " + singleton2.getCounter());
        System.out.println("  [OK] Same instance? " + (singleton1 == singleton2));
        System.out.println();
        
        // Prototype - Different instances
        System.out.println("PROTOTYPE SCOPE:");
        System.out.println("  Getting bean first time...");
        PrototypeBean prototype1 = context.getBean(PrototypeBean.class);
        prototype1.doSomething();
        
        System.out.println("  Getting bean second time...");
        PrototypeBean prototype2 = context.getBean(PrototypeBean.class);
        prototype2.doSomething();
        
        System.out.println("  [OK] Same instance? " + (prototype1 == prototype2));
        System.out.println("  [OK] Different instances created!");
        System.out.println();
        
        // ============================================
        // STEP 6: DEPENDENCY INJECTION DEMONSTRATION
        // ============================================
        System.out.println("================================================================");
        System.out.println("STEP 5: DEPENDENCY INJECTION");
        System.out.println("================================================================");
        
        UserController userController = context.getBean(UserController.class);
        System.out.println("[OK] UserController bean retrieved");
        System.out.println("  -> Dependencies were automatically injected!");
        userController.registerUser("Alice");
        System.out.println();
        
        OrderController orderController = context.getBean(OrderController.class);
        orderController.processOrder("ORD-123");
        System.out.println();
        
        ProductController productController = context.getBean(ProductController.class);
        productController.addProduct("iPhone");
        System.out.println();
        
        // ============================================
        // STEP 7: BEANS FROM @CONFIGURATION CLASS
        // ============================================
        System.out.println("================================================================");
        System.out.println("STEP 6: BEANS FROM @CONFIGURATION CLASS");
        System.out.println("================================================================");
        
        // These beans are created via @Bean methods in AppConfig
        // We need to import AppConfig first
        System.out.println("Note: @Bean methods create beans from AppConfig class");
        System.out.println("  -> See AppConfig.java for @Bean method examples");
        System.out.println();
        
        // ============================================
        // STEP 8: CLOSE APPLICATION CONTEXT
        // ============================================
        System.out.println("================================================================");
        System.out.println("STEP 7: CLOSING APPLICATION CONTEXT");
        System.out.println("================================================================");
        System.out.println("When context closes, @PreDestroy methods are called");
        System.out.println();
        
        ((AnnotationConfigApplicationContext) context).close();
        
        System.out.println();
        System.out.println("================================================================");
        System.out.println("           DEMONSTRATION COMPLETE!");
        System.out.println("================================================================");
    }
}


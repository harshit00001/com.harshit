package com.harshit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Boot Main Application Class
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: What is @SpringBootApplication annotation and what does it do?
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * @SpringBootApplication is a convenience annotation that combines three important
 * annotations:
 * 
 * 1. @SpringBootConfiguration:
 *    - Indicates that this class provides Spring Boot configuration
 *    - It's a specialized form of @Configuration
 *    - Allows the class to be a source of bean definitions
 * 
 * 2. @EnableAutoConfiguration:
 *    - Enables Spring Boot's auto-configuration mechanism
 *    - Automatically configures beans based on classpath dependencies
 *    - For example, if H2 is on classpath, it auto-configures H2 datasource
 *    - If JPA is on classpath, it auto-configures EntityManagerFactory, etc.
 *    - This is the "magic" of Spring Boot - convention over configuration
 * 
 * 3. @ComponentScan:
 *    - Scans the package and sub-packages for Spring components
 *    - Finds @Component, @Service, @Repository, @Controller, etc.
 *    - Registers them as Spring beans
 *    - By default, scans from the package of this class downwards
 * 
 * HOW SPRING BOOT AUTO-CONFIGURATION WORKS:
 * 
 * Spring Boot uses "starter" dependencies that bring in auto-configuration classes.
 * These classes use @ConditionalOnClass, @ConditionalOnMissingBean, etc. to
 * conditionally configure beans.
 * 
 * Example: When you add spring-boot-starter-data-jpa:
 * - Spring Boot detects Hibernate on classpath
 * - Auto-configures DataSource (H2, MySQL, etc. based on classpath)
 * - Auto-configures EntityManagerFactory
 * - Auto-configures TransactionManager
 * - Auto-configures JPA repositories
 * 
 * This means you don't need to manually configure:
 * - hibernate.cfg.xml
 * - persistence.xml
 * - EntityManagerFactory bean
 * - TransactionManager bean
 * 
 * All configuration is done through application.properties/application.yml!
 * 
 * ====================================================================================
 * ADDITIONAL ANNOTATIONS EXPLAINED:
 * ====================================================================================
 * 
 * @EnableJpaAuditing:
 * - Enables JPA auditing features
 * - Allows automatic population of @CreatedDate, @LastModifiedDate fields
 * - Requires @EntityListeners(AuditingEntityListener.class) on entities
 * - Useful for tracking when entities were created/modified
 * 
 * @EnableTransactionManagement:
 * - Enables Spring's annotation-driven transaction management
 * - Allows use of @Transactional annotation
 * - Configures transaction proxy creation
 * - This is actually enabled by default in Spring Boot, but explicit is better
 * 
 * ====================================================================================
 * REAL-WORLD SCENARIO:
 * ====================================================================================
 * 
 * In a production Spring Boot application:
 * 
 * 1. This class is the entry point - Spring Boot starts here
 * 2. It scans all packages for components
 * 3. Auto-configures everything based on dependencies
 * 4. Starts embedded Tomcat server (if spring-boot-starter-web is present)
 * 5. Makes application available at http://localhost:8080
 * 
 * You can customize behavior through:
 * - application.properties/application.yml
 * - @Configuration classes
 * - @Bean methods
 * - Profile-specific configurations
 * 
 * ====================================================================================
 * MAIN METHOD EXPLANATION:
 * ====================================================================================
 * 
 * SpringApplication.run() does the following:
 * 1. Creates ApplicationContext (Spring container)
 * 2. Registers all @Configuration classes
 * 3. Scans and registers all @Component classes
 * 4. Applies auto-configuration
 * 5. Starts embedded web server (if web starter is present)
 * 6. Runs CommandLineRunner and ApplicationRunner beans
 * 
 * The args parameter can be used to:
 * - Pass command-line arguments
 * - Override properties
 * - Control application behavior
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class HibernateJpaInterviewPrepApplication {

    /**
     * Main method - Entry point of the Spring Boot application
     * 
     * When you run this application:
     * 1. Spring Boot starts the application context
     * 2. All @Component, @Service, @Repository classes are scanned and registered
     * 3. Auto-configuration is applied based on dependencies
     * 4. Embedded Tomcat server starts (if web starter is present)
     * 5. Application is ready to handle requests
     * 
     * You can run this application in several ways:
     * 1. IDE: Right-click -> Run
     * 2. Maven: mvn spring-boot:run
     * 3. JAR: java -jar target/hibernate-jpa-interview-prep-1.0-SNAPSHOT.jar
     * 4. Command line: java -cp ... com.harshit.HibernateJpaInterviewPrepApplication
     */
    public static void main(String[] args) {
        // SpringApplication.run() bootstraps the entire Spring Boot application
        // It creates the ApplicationContext, applies auto-configuration,
        // and starts the embedded server
        SpringApplication.run(HibernateJpaInterviewPrepApplication.class, args);
        
        // After this line, the application is running and ready to handle requests
        // You can access:
        // - REST APIs at http://localhost:8080/api/...
        // - H2 Console at http://localhost:8080/h2-console (if enabled)
        // - Swagger UI at http://localhost:8080/swagger-ui.html
    }
}


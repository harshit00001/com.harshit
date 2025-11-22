package com.harshit.springboot.advanced;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * MULTIPLE DATABASE CONFIGURATION
 * 
 * Configuring multiple databases in a single Spring Boot application requires careful
 * setup to avoid conflicts. You need to create separate configuration classes for
 * each database, each with its own DataSource, EntityManagerFactory, and
 * TransactionManager.
 * 
 * The key steps are:
 * 1. Define connection properties for both databases in application.properties
 * 2. Create configuration classes for each database with @Configuration,
 *    @EnableTransactionManagement, and @EnableJpaRepositories
 * 3. Mark the primary database beans with @Primary
 * 4. Use @Qualifier to inject the correct beans where needed
 * 5. Separate entities and repositories into different packages
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.harshit.springboot.advanced.primary.repository",
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDatabaseConfig {
    
    /**
     * PRIMARY DATASOURCE
     * 
     * The primary database configuration. Marked with @Primary so Spring knows
     * which DataSource to use by default when autowiring.
     */
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        // Interview Point: Primary database DataSource
        // Configuration comes from spring.datasource.primary.* in application.properties
        return DataSourceBuilder.create().build();
    }
    
    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSource());
        em.setPackagesToScan("com.harshit.springboot.advanced.primary.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        em.setJpaProperties(properties);
        
        return em;
    }
    
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}

/**
 * SECONDARY DATABASE CONFIGURATION
 * 
 * The secondary database configuration. Uses @Qualifier to avoid conflicts with
 * the primary database beans. Repositories and entities are in separate packages.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.harshit.springboot.advanced.secondary.repository",
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "secondaryTransactionManager"
)
class SecondaryDatabaseConfig {
    
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        // Interview Point: Secondary database DataSource
        // Configuration comes from spring.datasource.secondary.* in application.properties
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(secondaryDataSource());
        em.setPackagesToScan("com.harshit.springboot.advanced.secondary.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        em.setJpaProperties(properties);
        
        return em;
    }
    
    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}


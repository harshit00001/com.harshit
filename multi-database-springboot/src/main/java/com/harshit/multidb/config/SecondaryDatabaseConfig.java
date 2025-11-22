package com.harshit.multidb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Secondary Database Configuration
 * 
 * This class configures the SECONDARY database (PostgreSQL).
 * 
 * Key Points:
 * 1. No @Primary annotation (only one can be primary)
 * 2. Uses @Qualifier to distinguish from primary
 * 3. Separate package for entities and repositories
 * 4. Independent configuration from primary database
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "secondaryTransactionManager",
    basePackages = {"com.harshit.multidb.secondary.repository"}  // Repository package
)
public class SecondaryDatabaseConfig {
    
    /**
     * Secondary DataSource Bean
     * 
     * Note: No @Primary annotation
     * Must use @Qualifier("secondaryDataSource") to inject this
     */
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    /**
     * Secondary EntityManagerFactory Bean
     */
    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        
        return builder
                .dataSource(dataSource)
                .packages("com.harshit.multidb.secondary.entity")  // Entity package
                .persistenceUnit("secondary")
                .build();
    }
    
    /**
     * Secondary TransactionManager Bean
     */
    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}


package com.harshit.multidb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Primary Database Configuration
 * 
 * This class configures the PRIMARY database (MySQL).
 * 
 * Key Points:
 * 1. @Primary: Marks this as the default DataSource
 * 2. @ConfigurationProperties: Binds properties from application.properties
 * 3. @EnableJpaRepositories: Configures JPA repositories for this database
 * 4. Separate EntityManagerFactory and TransactionManager
 * 
 * Why separate configuration?
 * - Each database needs its own:
 *   - DataSource
 *   - EntityManagerFactory
 *   - TransactionManager
 *   - Package for entities and repositories
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager",
    basePackages = {"com.harshit.multidb.primary.repository"}  // Repository package
)
public class PrimaryDatabaseConfig {
    
    /**
     * Primary DataSource Bean
     * 
     * @Primary annotation makes this the default DataSource
     * If no qualifier is specified, Spring will use this one
     */
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    /**
     * Primary EntityManagerFactory Bean
     * 
     * EntityManagerFactory is responsible for creating EntityManager instances
     * Each database needs its own EntityManagerFactory
     */
    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource dataSource) {
        
        return builder
                .dataSource(dataSource)
                .packages("com.harshit.multidb.primary.entity")  // Entity package
                .persistenceUnit("primary")
                .build();
    }
    
    /**
     * Primary TransactionManager Bean
     * 
     * TransactionManager manages database transactions
     * Each database needs its own TransactionManager
     */
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}


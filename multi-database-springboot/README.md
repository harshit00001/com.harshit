# 🗄️ Multi-Database Spring Boot - Complete Guide

A comprehensive guide to connecting multiple databases in a single Spring Boot application with detailed explanations, code examples, and interview questions.

---

## 📚 Table of Contents

1. [Introduction](#introduction)
2. [Why Multiple Databases?](#why-multiple-databases)
3. [Architecture Overview](#architecture-overview)
4. [Implementation Steps](#implementation-steps)
5. [Code Walkthrough](#code-walkthrough)
6. [Configuration Details](#configuration-details)
7. [Transaction Management](#transaction-management)
8. [Best Practices](#best-practices)
9. [Interview Questions & Answers](#interview-questions--answers)
10. [Common Issues & Solutions](#common-issues--solutions)

---

## Introduction

### What is Multi-Database Configuration?

**Multi-database configuration** allows a single Spring Boot application to connect to and work with multiple databases simultaneously. Each database has its own:
- DataSource
- EntityManagerFactory
- TransactionManager
- Package structure for entities and repositories

**Simple Explanation**:
Think of it like having two separate filing cabinets. One for user information (MySQL) and another for order information (PostgreSQL). Your application can access both, but they're completely independent.

---

## Why Multiple Databases?

### Use Cases

1. **Microservices Migration**: Gradually moving from monolith to microservices
2. **Data Separation**: Separate databases for different domains (users, orders, payments)
3. **Performance**: Different databases optimized for different workloads
4. **Compliance**: Regulatory requirements for data separation
5. **Legacy Integration**: Integrating with existing systems

### Advantages

- **Data Isolation**: Clear separation of concerns
- **Independent Scaling**: Scale databases independently
- **Technology Flexibility**: Use different DB technologies
- **Security**: Different access controls per database

### Disadvantages

- **Complexity**: More configuration and management
- **No Cross-DB Transactions**: Can't have ACID transactions across databases
- **Data Consistency**: Harder to maintain consistency
- **Performance**: Network overhead for cross-database queries

---

## Architecture Overview

```
┌─────────────────────────────────────────┐
│     Spring Boot Application             │
│                                         │
│  ┌──────────────────────────────────┐  │
│  │   Primary Database Config        │  │
│  │   - DataSource (MySQL)           │  │
│  │   - EntityManagerFactory        │  │
│  │   - TransactionManager           │  │
│  └──────────────────────────────────┘  │
│              │                          │
│              ▼                          │
│  ┌──────────────────────────────────┐  │
│  │   Secondary Database Config      │  │
│  │   - DataSource (PostgreSQL)      │  │
│  │   - EntityManagerFactory         │  │
│  │   - TransactionManager           │  │
│  └──────────────────────────────────┘  │
│              │                          │
└──────────────┼──────────────────────────┘
               │
       ┌───────┴───────┐
       │               │
       ▼               ▼
   ┌────────┐     ┌──────────┐
   │ MySQL  │     │PostgreSQL│
   │(Primary)│     │(Secondary)│
   └────────┘     └──────────┘
```

---

## Implementation Steps

### Step 1: Add Dependencies (pom.xml)

```xml
<!-- MySQL Driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Step 2: Configure DataSources (application.properties)

```properties
# Primary Database
spring.datasource.primary.url=jdbc:mysql://localhost:3306/userdb
spring.datasource.primary.username=root
spring.datasource.primary.password=root
spring.datasource.primary.driver-class-name=com.mysql.cj.jdbc.Driver

# Secondary Database
spring.datasource.secondary.url=jdbc:postgresql://localhost:5432/orderdb
spring.datasource.secondary.username=postgres
spring.datasource.secondary.password=postgres
spring.datasource.secondary.driver-class-name=org.postgresql.Driver
```

### Step 3: Create Configuration Classes

**PrimaryDatabaseConfig**:
- Creates primary DataSource
- Creates primary EntityManagerFactory
- Creates primary TransactionManager
- Configures JPA repositories for primary DB

**SecondaryDatabaseConfig**:
- Creates secondary DataSource
- Creates secondary EntityManagerFactory
- Creates secondary TransactionManager
- Configures JPA repositories for secondary DB

### Step 4: Organize Packages

```
com.harshit.multidb/
├── primary/
│   ├── entity/          # Entities for primary DB
│   └── repository/       # Repositories for primary DB
├── secondary/
│   ├── entity/          # Entities for secondary DB
│   └── repository/      # Repositories for secondary DB
└── config/              # Database configurations
```

---

## Code Walkthrough

### 1. Primary Database Configuration

```java
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager",
    basePackages = {"com.harshit.multidb.primary.repository"}
)
public class PrimaryDatabaseConfig {
    
    @Primary  // Marks as default
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.harshit.multidb.primary.entity")
                .persistenceUnit("primary")
                .build();
    }
    
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
```

**Key Points**:
- `@Primary`: Marks this as the default (used when no qualifier specified)
- `@ConfigurationProperties`: Binds properties from application.properties
- `basePackages`: Tells Spring where to find repositories for this DB
- `packages`: Tells Spring where to find entities for this DB

### 2. Secondary Database Configuration

```java
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "secondaryTransactionManager",
    basePackages = {"com.harshit.multidb.secondary.repository"}
)
public class SecondaryDatabaseConfig {
    
    // No @Primary annotation
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    // Similar structure to primary...
}
```

**Key Points**:
- No `@Primary` annotation (only one can be primary)
- Must use `@Qualifier` to inject this DataSource
- Separate package for entities and repositories

### 3. Service Layer with Transaction Management

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Specify transaction manager explicitly
    @Transactional(transactionManager = "primaryTransactionManager")
    public User createUser(String username, String email, String fullName) {
        User user = new User(username, email, fullName);
        return userRepository.save(user);
    }
}
```

**Key Points**:
- `transactionManager = "primaryTransactionManager"`: Explicitly specify which transaction manager to use
- Without specification, Spring uses the `@Primary` transaction manager

---

## Configuration Details

### DataSource Configuration

**Primary DataSource**:
```java
@Primary
@Bean(name = "primaryDataSource")
@ConfigurationProperties(prefix = "spring.datasource.primary")
public DataSource primaryDataSource() {
    return DataSourceBuilder.create().build();
}
```

**What happens**:
1. Spring reads properties with prefix `spring.datasource.primary`
2. Creates DataSource with those properties
3. `@Primary` makes it the default DataSource

### EntityManagerFactory Configuration

**Purpose**: Creates EntityManager instances for JPA operations

**Configuration**:
```java
@Bean(name = "primaryEntityManagerFactory")
public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(...) {
    return builder
            .dataSource(dataSource)           // Which database
            .packages("com.harshit...")       // Where entities are
            .persistenceUnit("primary")       // Unique name
            .build();
}
```

### TransactionManager Configuration

**Purpose**: Manages database transactions

**Configuration**:
```java
@Bean(name = "primaryTransactionManager")
public PlatformTransactionManager primaryTransactionManager(
        @Qualifier("primaryEntityManagerFactory") EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
}
```

---

## Transaction Management

### Single Database Transactions

```java
@Transactional(transactionManager = "primaryTransactionManager")
public void createUser() {
    // All operations in this method use primary DB transaction
    userRepository.save(user1);
    userRepository.save(user2);
    // If any fails, both rollback
}
```

### Cross-Database Operations

**Problem**: Can't have ACID transaction across databases by default

**Solution 1: Separate Transactions**
```java
public void createUserAndOrder() {
    // Separate transactions
    userService.createUser(...);  // Transaction 1 (Primary DB)
    orderService.createOrder(...);  // Transaction 2 (Secondary DB)
    // If order fails, user is still created
}
```

**Solution 2: Manual Rollback**
```java
public void createUserAndOrderWithRollback() {
    User user = null;
    try {
        user = userService.createUser(...);
        orderService.createOrder(...);
    } catch (Exception e) {
        if (user != null) {
            userService.deleteUser(user.getId());  // Manual rollback
        }
        throw e;
    }
}
```

**Solution 3: JTA (Java Transaction API)**
- Use distributed transaction manager
- Supports true ACID across databases
- More complex setup
- Examples: Atomikos, Bitronix

---

## Best Practices

### 1. Package Organization

✅ **GOOD**: Separate packages per database
```
primary/
  ├── entity/
  └── repository/
secondary/
  ├── entity/
  └── repository/
```

❌ **BAD**: Mixed packages
```
entity/
  ├── User.java
  └── Order.java
```

### 2. Explicit Transaction Managers

✅ **GOOD**: Specify transaction manager
```java
@Transactional(transactionManager = "primaryTransactionManager")
```

❌ **BAD**: Rely on @Primary
```java
@Transactional  // Might use wrong transaction manager
```

### 3. Clear Naming

✅ **GOOD**: Descriptive names
```java
@Qualifier("primaryDataSource")
@Qualifier("secondaryDataSource")
```

### 4. Configuration Properties

✅ **GOOD**: Separate property prefixes
```properties
spring.datasource.primary.url=...
spring.datasource.secondary.url=...
```

---

## Interview Questions & Answers

### Q1: How do you connect to multiple databases in Spring Boot?

**Answer**:

**Step-by-step approach**:

1. **Create separate DataSource beans**:
```java
@Primary
@Bean(name = "primaryDataSource")
@ConfigurationProperties(prefix = "spring.datasource.primary")
public DataSource primaryDataSource() {
    return DataSourceBuilder.create().build();
}

@Bean(name = "secondaryDataSource")
@ConfigurationProperties(prefix = "spring.datasource.secondary")
public DataSource secondaryDataSource() {
    return DataSourceBuilder.create().build();
}
```

2. **Create separate EntityManagerFactory beans**:
```java
@Primary
@Bean(name = "primaryEntityManagerFactory")
public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(...) {
    return builder
            .dataSource(primaryDataSource)
            .packages("com.example.primary.entity")
            .build();
}
```

3. **Create separate TransactionManager beans**:
```java
@Primary
@Bean(name = "primaryTransactionManager")
public PlatformTransactionManager primaryTransactionManager(...) {
    return new JpaTransactionManager(primaryEntityManagerFactory);
}
```

4. **Configure @EnableJpaRepositories**:
```java
@EnableJpaRepositories(
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager",
    basePackages = {"com.example.primary.repository"}
)
```

5. **Organize packages**:
- Separate packages for entities and repositories per database
- Prevents Spring from mixing configurations

---

### Q2: What is @Primary annotation and why is it important?

**Answer**:

**@Primary** indicates that a bean should be given preference when multiple candidates are qualified to autowire a single-valued dependency.

**Why important**:
- Spring needs a default DataSource when no qualifier is specified
- Only ONE DataSource can be @Primary
- Other DataSources must use @Qualifier

**Example**:
```java
@Primary
@Bean
public DataSource primaryDataSource() { ... }

@Bean
public DataSource secondaryDataSource() { ... }

// Usage
@Autowired
private DataSource dataSource;  // Uses primaryDataSource

@Autowired
@Qualifier("secondaryDataSource")
private DataSource secondaryDataSource;  // Uses secondaryDataSource
```

**Without @Primary**:
- Spring throws exception: "No qualifying bean of type DataSource"
- Must always use @Qualifier

---

### Q3: How do you handle transactions across multiple databases?

**Answer**:

**Challenge**: Spring's default transaction management doesn't support distributed transactions across multiple databases.

**Solutions**:

**1. Separate Transactions (Default)**:
```java
@Transactional(transactionManager = "primaryTransactionManager")
public void createUser() { ... }

@Transactional(transactionManager = "secondaryTransactionManager")
public void createOrder() { ... }

// These are separate transactions
public void createUserAndOrder() {
    createUser();  // Transaction 1
    createOrder();  // Transaction 2
    // If order fails, user is still created
}
```

**2. Manual Rollback**:
```java
public void createUserAndOrder() {
    User user = null;
    try {
        user = userService.createUser(...);
        orderService.createOrder(...);
    } catch (Exception e) {
        if (user != null) {
            userService.deleteUser(user.getId());  // Manual rollback
        }
        throw e;
    }
}
```

**3. JTA (Java Transaction API)**:
```java
// Use distributed transaction manager (Atomikos, Bitronix)
@Transactional
public void createUserAndOrder() {
    // True distributed transaction
    // Both commit or both rollback
}
```

**Trade-offs**:
- **Separate transactions**: Simple, but no ACID guarantee
- **Manual rollback**: More control, but complex
- **JTA**: True ACID, but complex setup and performance overhead

---

### Q4: What is the difference between EntityManagerFactory and TransactionManager?

**Answer**:

**EntityManagerFactory**:
- **Purpose**: Factory for creating EntityManager instances
- **Responsibility**: Manages entity metadata, database connection
- **Scope**: One per database
- **Usage**: Used by JPA repositories

**TransactionManager**:
- **Purpose**: Manages database transactions
- **Responsibility**: Transaction boundaries, commit/rollback
- **Scope**: One per database
- **Usage**: Used by @Transactional annotation

**Relationship**:
```
EntityManagerFactory → Creates → EntityManager
                                    ↓
TransactionManager → Manages → Transactions
```

**Example**:
```java
// EntityManagerFactory creates EntityManager
EntityManager em = entityManagerFactory.createEntityManager();

// TransactionManager manages transactions
@Transactional(transactionManager = "primaryTransactionManager")
public void save() {
    // TransactionManager handles begin/commit/rollback
    entityManager.persist(entity);
}
```

---

### Q5: How do you organize entities and repositories for multiple databases?

**Answer**:

**Best Practice**: Separate packages per database

**Structure**:
```
com.example.multidb/
├── primary/
│   ├── entity/
│   │   └── User.java
│   └── repository/
│       └── UserRepository.java
├── secondary/
│   ├── entity/
│   │   └── Order.java
│   └── repository/
│       └── OrderRepository.java
└── config/
    ├── PrimaryDatabaseConfig.java
    └── SecondaryDatabaseConfig.java
```

**Configuration**:
```java
// Primary config
@EnableJpaRepositories(
    basePackages = {"com.example.multidb.primary.repository"},
    entityManagerFactoryRef = "primaryEntityManagerFactory"
)
public class PrimaryDatabaseConfig {
    // ...
    .packages("com.example.multidb.primary.entity")
}

// Secondary config
@EnableJpaRepositories(
    basePackages = {"com.example.multidb.secondary.repository"},
    entityManagerFactoryRef = "secondaryEntityManagerFactory"
)
public class SecondaryDatabaseConfig {
    // ...
    .packages("com.example.multidb.secondary.entity")
}
```

**Why this matters**:
- Spring uses package scanning to find entities/repositories
- Prevents mixing entities from different databases
- Clear separation of concerns

---

### Q6: Can you have a foreign key relationship between entities in different databases?

**Answer**:

**Short Answer**: **No**, not directly.

**Why**:
- Foreign keys are database-level constraints
- Different databases can't enforce cross-database foreign keys
- Each database is independent

**Alternatives**:

**1. Application-Level Validation**:
```java
@Service
public class OrderService {
    @Autowired
    private UserService userService;  // Primary DB
    
    @Transactional(transactionManager = "secondaryTransactionManager")
    public Order createOrder(Long userId, ...) {
        // Validate user exists in primary DB
        if (!userService.userExists(userId)) {
            throw new RuntimeException("User not found");
        }
        
        // Create order in secondary DB
        Order order = new Order(userId, ...);
        return orderRepository.save(order);
    }
}
```

**2. Reference by ID**:
```java
@Entity
public class Order {
    private Long userId;  // Reference, not foreign key
    // Application ensures user exists
}
```

**3. Eventual Consistency**:
- Accept that data might be temporarily inconsistent
- Use eventual consistency patterns
- Reconcile periodically

---

### Q7: How do you handle database migrations with multiple databases?

**Answer**:

**Option 1: Separate Flyway/Liquibase Configurations**

**Flyway Example**:
```properties
# Primary DB migrations
spring.flyway.primary.enabled=true
spring.flyway.primary.locations=classpath:db/migration/primary
spring.flyway.primary.url=${spring.datasource.primary.url}

# Secondary DB migrations
spring.flyway.secondary.enabled=true
spring.flyway.secondary.locations=classpath:db/migration/secondary
spring.flyway.secondary.url=${spring.datasource.secondary.url}
```

**Option 2: Manual Migration Scripts**
- Separate SQL scripts per database
- Run migrations independently
- Version control separately

**Option 3: JPA ddl-auto**
```properties
# Primary DB
spring.jpa.primary.hibernate.ddl-auto=update

# Secondary DB
spring.jpa.secondary.hibernate.ddl-auto=update
```
⚠️ **Warning**: Not recommended for production

---

### Q8: What are the performance implications of multiple databases?

**Answer**:

**Advantages**:
- **Independent Scaling**: Scale databases based on load
- **Optimization**: Optimize each database for its workload
- **Reduced Contention**: Less lock contention

**Disadvantages**:
- **Network Overhead**: Multiple database connections
- **No Joins**: Can't join tables across databases
- **Multiple Queries**: Need multiple queries for related data
- **Connection Pooling**: More connection pools to manage

**Optimization Strategies**:

1. **Connection Pooling**:
```properties
spring.datasource.primary.hikari.maximum-pool-size=10
spring.datasource.secondary.hikari.maximum-pool-size=10
```

2. **Caching**:
```java
@Cacheable("users")
public User getUser(Long id) { ... }
```

3. **Batch Operations**:
```java
// Batch inserts
@Transactional
public void saveAll(List<User> users) {
    userRepository.saveAll(users);
}
```

4. **Read Replicas**: Use read replicas for read-heavy operations

---

### Q9: How do you test applications with multiple databases?

**Answer**:

**Option 1: Test Containers**
```java
@Testcontainers
class MultiDatabaseTest {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");
    
    @Test
    void testMultiDatabase() {
        // Test with real databases
    }
}
```

**Option 2: In-Memory Databases**
```properties
# Test profile
spring.datasource.primary.url=jdbc:h2:mem:testdb1
spring.datasource.secondary.url=jdbc:h2:mem:testdb2
```

**Option 3: Mock Repositories**
```java
@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private OrderRepository orderRepository;
}
```

---

### Q10: When should you use multiple databases vs. a single database?

**Answer**:

**Use Multiple Databases When**:
- ✅ **Microservices**: Different services need different databases
- ✅ **Data Isolation**: Regulatory/compliance requirements
- ✅ **Performance**: Different databases optimized for different workloads
- ✅ **Legacy Integration**: Integrating with existing systems
- ✅ **Technology Requirements**: Need different database technologies

**Use Single Database When**:
- ✅ **Simplicity**: Easier to manage and maintain
- ✅ **ACID Transactions**: Need cross-table transactions
- ✅ **Joins**: Need to join tables frequently
- ✅ **Small Scale**: Application doesn't need separation
- ✅ **Consistency**: Strong consistency requirements

**Decision Matrix**:
| Factor | Single DB | Multiple DBs |
|--------|-----------|--------------|
| Complexity | Low | High |
| Transactions | Easy | Complex |
| Scaling | Vertical | Horizontal |
| Consistency | Strong | Eventual |
| Performance | Good for small | Better for large |

---

## Common Issues & Solutions

### Issue 1: "No qualifying bean of type DataSource"

**Problem**: Spring can't determine which DataSource to use

**Solution**: Add @Primary to one DataSource
```java
@Primary
@Bean
public DataSource primaryDataSource() { ... }
```

### Issue 2: Entities from wrong database

**Problem**: EntityManager trying to save entity to wrong database

**Solution**: Ensure correct package structure
```java
// Primary entities in primary.entity package
// Secondary entities in secondary.entity package
```

### Issue 3: Transaction not working

**Problem**: @Transactional not working correctly

**Solution**: Specify transaction manager explicitly
```java
@Transactional(transactionManager = "primaryTransactionManager")
```

### Issue 4: Repository not found

**Problem**: Spring can't find repository

**Solution**: Check basePackages in @EnableJpaRepositories
```java
@EnableJpaRepositories(
    basePackages = {"com.example.primary.repository"}
)
```

---

## Running the Application

### Prerequisites
- Java 11+
- Maven 3.6+
- MySQL 8.0+ (for primary DB)
- PostgreSQL 13+ (for secondary DB)

### Setup Databases

**MySQL**:
```sql
CREATE DATABASE userdb;
```

**PostgreSQL**:
```sql
CREATE DATABASE orderdb;
```

### Run Application
```bash
mvn spring-boot:run
```

### Test Endpoints

**Create User** (Primary DB):
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "fullName": "John Doe"
  }'
```

**Create Order** (Secondary DB):
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productName": "Laptop",
    "amount": 999.99,
    "quantity": 1
  }'
```

---

## Key Takeaways

1. **Separate Configuration**: Each database needs its own DataSource, EntityManagerFactory, and TransactionManager
2. **Package Organization**: Separate packages for entities and repositories per database
3. **@Primary Annotation**: Mark one DataSource as primary
4. **Transaction Management**: Specify transaction manager explicitly
5. **No Cross-DB Transactions**: Can't have ACID transactions across databases by default
6. **JTA for Distributed Transactions**: Use JTA for true distributed transactions
7. **Clear Naming**: Use descriptive names and qualifiers
8. **Testing**: Use test containers or in-memory databases for testing

---

**Happy Learning! 🗄️**

*Remember: Multiple databases add complexity. Use them when benefits outweigh costs!*


# Spring Boot Performance Optimization Demo

## 📋 Overview

This project demonstrates **Interview Question 1: How can you improve the performance of a Spring Boot application?**

All code examples are organized by performance strategy for easy understanding.

---

## 📁 Project Structure

### Main Application
- **`SpringBootAdvancedDemoApplication.java`** - Main application class with `@EnableCaching` and `@EnableAsync`

### Performance Strategies (10 Strategies)

#### Strategy 1: Connection Pooling
- **`src/main/resources/application.properties`** - HikariCP configuration
- **`performance/ConnectionPoolDemo.java`** - Demonstrates connection pooling

#### Strategy 2: Lazy Loading
- **`entity/User.java`** - JPA entity (use `FetchType.LAZY` for relationships)
- **`performance/LazyLoadingDemo.java`** - Explains lazy loading benefits

#### Strategy 3: Caching
- **`performance/PerformanceOptimizedService.java`** - `getUserById()` method with `@Cacheable`
- **`application.properties`** - Cache configuration (`spring.cache.type=simple`)

#### Strategy 4: Asynchronous Processing
- **`performance/PerformanceOptimizedService.java`** - `sendEmailAsync()` method with `@Async`
- **`config/AsyncConfiguration.java`** - Custom thread pool configuration

#### Strategy 5: Database Query Optimization
- **`performance/PerformanceOptimizedService.java`** - `getUsersPaginated()` and `findUsersByEmail()` methods
- **`repository/UserRepository.java`** - Optimized repository queries

#### Strategy 6: HTTP Compression
- **`application.properties`** - `server.compression.enabled=true`

#### Strategy 7: Spring Boot Actuator
- **`application.properties`** - Actuator endpoints configuration

#### Strategy 8: Thread Pool Configuration
- **`config/AsyncConfiguration.java`** - Custom `ThreadPoolTaskExecutor` configuration

#### Strategy 9: Connection Pooling for HTTP
- Mentioned in documentation (use WebClient)

#### Strategy 10: JVM Optimizations
- Mentioned in documentation (JVM flags)

---

## 🗂️ Complete File List

### Core Files
```
src/main/java/com/harshit/demo/
├── SpringBootAdvancedDemoApplication.java    # Main app with @EnableCaching, @EnableAsync
│
├── performance/
│   ├── PerformanceOptimizedService.java      # Strategies 3, 4, 5 - Caching, Async, Pagination
│   ├── ConnectionPoolDemo.java               # Strategy 1 - Connection Pooling Demo
│   └── LazyLoadingDemo.java                  # Strategy 2 - Lazy Loading Demo
│
├── config/
│   ├── AsyncConfiguration.java              # Strategy 8 - Thread Pool Configuration
│   └── AppProperties.java                    # @ConfigurationProperties example
│
├── entity/
│   └── User.java                             # Strategy 2 - JPA Entity with lazy loading
│
└── repository/
    └── UserRepository.java                   # Strategy 5 - Optimized queries

src/main/resources/
└── application.properties                    # All configurations (Strategies 1, 3, 6, 7)

pom.xml                                       # Maven dependencies
```

---

## 🎯 Key Java Files to Study

### 1. PerformanceOptimizedService.java
**Location:** `src/main/java/com/harshit/demo/performance/PerformanceOptimizedService.java`

**What it demonstrates:**
- **Strategy 3:** `@Cacheable` annotation for caching
- **Strategy 4:** `@Async` annotation for asynchronous processing
- **Strategy 5:** Pagination with `Pageable` and optimized queries
- Complete demonstration method showing all optimizations

**Key Methods:**
```java
@Cacheable(value = "users", key = "#id")
public User getUserById(Long id)  // Caching example

@Async
public CompletableFuture<String> sendEmailAsync(String email)  // Async example

@Transactional(readOnly = true)
public Page<User> getUsersPaginated(Pageable pageable)  // Pagination example
```

---

### 2. AsyncConfiguration.java
**Location:** `src/main/java/com/harshit/demo/config/AsyncConfiguration.java`

**What it demonstrates:**
- **Strategy 8:** Custom thread pool configuration for `@Async` methods
- `ThreadPoolTaskExecutor` with optimal pool sizes
- Thread pool lifecycle management

**Key Features:**
- Core pool size: 10
- Max pool size: 20
- Queue capacity: 100
- Proper shutdown handling

---

### 3. ConnectionPoolDemo.java
**Location:** `src/main/java/com/harshit/demo/performance/ConnectionPoolDemo.java`

**What it demonstrates:**
- **Strategy 1:** How connection pooling works
- Connection reuse from pool
- Monitoring connection pool usage

---

### 4. LazyLoadingDemo.java
**Location:** `src/main/java/com/harshit/demo/performance/LazyLoadingDemo.java`

**What it demonstrates:**
- **Strategy 2:** Benefits of lazy loading
- How to avoid N+1 query problem
- When to use `FetchType.LAZY`

---

### 5. User.java
**Location:** `src/main/java/com/harshit/demo/entity/User.java`

**What it demonstrates:**
- **Strategy 2:** JPA entity structure
- How to use `FetchType.LAZY` for relationships
- Entity validation annotations

---

### 6. UserRepository.java
**Location:** `src/main/java/com/harshit/demo/repository/UserRepository.java`

**What it demonstrates:**
- **Strategy 5:** Optimized repository queries
- Custom query methods
- Efficient data access patterns

---

### 7. application.properties
**Location:** `src/main/resources/application.properties`

**What it demonstrates:**
- **Strategy 1:** HikariCP connection pool configuration
- **Strategy 3:** Cache configuration
- **Strategy 6:** HTTP compression
- **Strategy 7:** Actuator configuration

---

## 🚀 How to Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will automatically demonstrate all performance optimizations in the console.

---

## 📊 What Each File Shows

| File | Strategy | What It Demonstrates |
|------|----------|---------------------|
| `PerformanceOptimizedService.java` | 3, 4, 5 | Caching, Async, Pagination |
| `AsyncConfiguration.java` | 8 | Thread Pool Configuration |
| `ConnectionPoolDemo.java` | 1 | Connection Pooling |
| `LazyLoadingDemo.java` | 2 | Lazy Loading Benefits |
| `User.java` | 2 | JPA Entity with Lazy Loading |
| `UserRepository.java` | 5 | Optimized Queries |
| `application.properties` | 1, 3, 6, 7 | All Configurations |
| `SpringBootAdvancedDemoApplication.java` | All | Main App with Enable Annotations |

---

## 💡 Learning Path

1. **Start with:** `SpringBootAdvancedDemoApplication.java` - See how caching and async are enabled
2. **Then study:** `PerformanceOptimizedService.java` - Main service with 3 strategies
3. **Check config:** `application.properties` - All configuration settings
4. **Explore:** `AsyncConfiguration.java` - Thread pool setup
5. **Review:** Demo classes for additional explanations

---

## 🎓 Interview Answer Summary

**Question:** How can you improve the performance of a Spring Boot application?

**10 Strategies:**
1. Connection Pooling (HikariCP) - `application.properties`
2. Lazy Loading - `User.java`, `LazyLoadingDemo.java`
3. Caching - `PerformanceOptimizedService.getUserById()`
4. Async Processing - `PerformanceOptimizedService.sendEmailAsync()`
5. Query Optimization - `PerformanceOptimizedService.getUsersPaginated()`
6. HTTP Compression - `application.properties`
7. Actuator Monitoring - `application.properties`
8. Thread Pool Config - `AsyncConfiguration.java`
9. HTTP Connection Pooling - Use WebClient
10. JVM Optimizations - JVM flags

All code examples are in this project! 🚀

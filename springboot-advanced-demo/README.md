# Spring Boot Performance Optimization Demo

This project demonstrates **Interview Question 1: How can you improve the performance of a Spring Boot application?**

## 📋 Overview

This demo covers 10 key performance optimization strategies:

1. **Connection Pooling** - Configured in application.properties
2. **Lazy Loading** - JPA entity relationships
3. **Caching** - Using @Cacheable annotation
4. **Asynchronous Processing** - Using @Async annotation
5. **Database Query Optimization** - Pagination and optimized queries
6. **HTTP Compression** - Configured in application.properties
7. **Spring Boot Actuator** - Performance monitoring
8. **Thread Pool Configuration** - For async operations
9. **Connection Pooling for HTTP** - WebClient configuration
10. **JVM Optimizations** - Garbage collection and heap settings

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Run the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will:
- Start on port 8080
- Automatically demonstrate performance optimizations
- Show caching, async processing, and pagination examples

### Access Endpoints

- **Actuator Health**: http://localhost:8080/actuator/health
- **Actuator Metrics**: http://localhost:8080/actuator/metrics
- **All Actuator Endpoints**: http://localhost:8080/actuator

## 📁 Project Structure

```
src/main/java/com/harshit/demo/
├── performance/
│   └── PerformanceOptimizedService.java  # Main service with all optimizations
├── entity/
│   └── User.java                         # JPA entity with lazy loading
├── repository/
│   └── UserRepository.java              # Repository with optimized queries
└── SpringBootAdvancedDemoApplication.java # Main application class
```

## 📚 Documentation

- **VIDEO_SCRIPT.md** - Complete script for video explanation
- **STEPS.md** - Step-by-step guide to understand the code

## 🎯 Key Features Demonstrated

### 1. Caching
```java
@Cacheable(value = "users", key = "#id")
public User getUserById(Long id) {
    // First call hits database, subsequent calls use cache
}
```

### 2. Async Processing
```java
@Async
public CompletableFuture<String> sendEmailAsync(String email) {
    // Runs in separate thread, doesn't block caller
}
```

### 3. Pagination
```java
public Page<User> getUsersPaginated(Pageable pageable) {
    // Only loads requested page, not all records
}
```

## ⚙️ Configuration

Key configurations in `application.properties`:

```properties
# Connection Pooling
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# HTTP Compression
server.compression.enabled=true

# Caching
spring.cache.type=simple
spring.cache.cache-names=users

# Actuator
management.endpoints.web.exposure.include=health,info,metrics
```

## 📊 Performance Monitoring

Use Spring Boot Actuator to monitor:
- Connection pool metrics
- Cache statistics
- Thread pool usage
- Response times
- Memory usage

## 🎓 Learning Path

1. Read **VIDEO_SCRIPT.md** for detailed explanations
2. Follow **STEPS.md** for hands-on practice
3. Run the application and observe the console output
4. Experiment with different configurations
5. Monitor performance using Actuator endpoints

## 🔍 What to Observe

When you run the application, watch for:

1. **Caching**: First database call vs cached call timing
2. **Async Processing**: Main thread continues while email sends in background
3. **Pagination**: Only requested page is loaded
4. **Connection Pool**: Monitor via Actuator metrics
5. **Performance Metrics**: Check Actuator endpoints

## 📝 Interview Answer Summary

**Question**: How can you improve the performance of a Spring Boot application?

**Answer**: There are 10 key strategies:
1. Enable connection pooling (HikariCP)
2. Use lazy loading for JPA entities
3. Implement caching with @Cacheable
4. Use async processing with @Async
5. Optimize database queries with pagination
6. Enable HTTP compression
7. Use Actuator for monitoring
8. Configure thread pools properly
9. Use connection pooling for HTTP calls
10. Enable JVM optimizations

See **VIDEO_SCRIPT.md** for the complete conversational answer.

---

Happy Learning! 🚀


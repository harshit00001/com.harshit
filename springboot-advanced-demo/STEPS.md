# Steps to Follow - Performance Optimization Demo

This guide will walk you through the performance optimization code step by step.

---

## Prerequisites

1. Java 11 or higher installed
2. Maven installed
3. IDE (IntelliJ IDEA, Eclipse, or VS Code)

---

## Step 1: Project Setup (3 minutes)

### 1.1 Navigate to Project
```bash
cd "c:\Java code\com.harshit\springboot-advanced-demo"
```

### 1.2 Build the Project
```bash
mvn clean install
```

### 1.3 Run the Application
```bash
mvn spring-boot:run
```

**What to observe:**
- Application starts on port 8080
- Performance demonstration runs automatically
- Check console output for timing differences

---

## Step 2: Understand Connection Pooling (5 minutes)

### 2.1 Check Application Properties
**File:** `src/main/resources/application.properties`

**What to do:**
1. Find the HikariCP configuration section
2. Read each property and understand what it does:
   - `maximum-pool-size=20` - Maximum connections in pool
   - `minimum-idle=5` - Minimum idle connections
   - `connection-timeout=30000` - Wait time for connection

**Key Learning:**
- Connection pooling reuses database connections
- Reduces overhead of creating new connections
- Proper pool size is crucial for performance

**Test it:**
1. Monitor connections via Actuator: `http://localhost:8080/actuator/metrics/hikari.connections.active`
2. Make multiple concurrent requests
3. Observe connection pool usage

---

## Step 3: Explore Caching (10 minutes)

### 3.1 Open PerformanceOptimizedService
**File:** `src/main/java/com/harshit/demo/performance/PerformanceOptimizedService.java`

### 3.2 Find the getUserById Method
**What to do:**
1. Notice the `@Cacheable(value = "users", key = "#id")` annotation
2. Read the comments explaining how caching works
3. Understand that first call hits database, subsequent calls use cache

**Test it:**
1. Run the application
2. Watch console output:
   - First call: "Fetching user from database" - takes time
   - Second call: No database message - instant response from cache
3. Compare the timing shown in console

**Key Learning:**
- Caching avoids repeated database queries
- Significant performance improvement for frequently accessed data
- Cache is configured in application.properties

**Experiment:**
1. Call `getUserById(1L)` multiple times
2. Notice only first call hits database
3. Try calling with different IDs - each new ID hits database once

---

## Step 4: Understand Async Processing (10 minutes)

### 4.1 Find the sendEmailAsync Method
**What to do:**
1. Notice the `@Async` annotation
2. See it returns `CompletableFuture<String>`
3. Understand it runs in a separate thread

**Test it:**
1. Run the application
2. Watch console output:
   - Main thread continues immediately
   - Email sending happens in background thread
   - Different thread names shown in console

**Key Learning:**
- Async methods don't block the calling thread
- Allows handling more concurrent requests
- Perfect for time-consuming operations

**Experiment:**
1. Call `sendEmailAsync` multiple times
2. Notice all calls return immediately
3. Watch background threads processing emails

---

## Step 5: Explore Pagination (8 minutes)

### 5.1 Find the getUsersPaginated Method
**What to do:**
1. Notice it uses `Pageable` parameter
2. See `@Transactional(readOnly = true)` annotation
3. Understand it only loads requested page

**Test it:**
1. The demonstration shows pagination in action
2. Notice only 10 users loaded, not all
3. Check console for pagination details

**Key Learning:**
- Pagination reduces memory usage
- Only loads what's needed
- Essential for large datasets

**Experiment:**
1. Modify page size in the code
2. See how it affects memory usage
3. Try different page numbers

---

## Step 6: Check Application Configuration (5 minutes)

### 6.1 Review application.properties
**File:** `src/main/resources/application.properties`

**What to do:**
1. Find HTTP compression: `server.compression.enabled=true`
2. Find cache configuration: `spring.cache.type=simple`
3. Find actuator configuration
4. Understand each setting

**Key Learning:**
- HTTP compression reduces response size
- Cache type determines caching implementation
- Actuator enables performance monitoring

---

## Step 7: Monitor with Actuator (10 minutes)

### 7.1 Access Actuator Endpoints
**URLs to visit:**
- `http://localhost:8080/actuator` - All endpoints
- `http://localhost:8080/actuator/health` - Health status
- `http://localhost:8080/actuator/metrics` - All metrics
- `http://localhost:8080/actuator/metrics/hikari.connections.active` - Connection pool

**What to do:**
1. Visit each endpoint
2. Explore the metrics available
3. Understand what each metric tells you

**Key Learning:**
- Actuator provides performance insights
- Monitor connection pool usage
- Track response times and throughput

**Experiment:**
1. Make multiple requests to your application
2. Check metrics before and after
3. See how metrics change

---

## Step 8: Run Complete Demonstration (5 minutes)

### 8.1 Watch the Automatic Demo
**What happens:**
When you run the application, it automatically:
1. Demonstrates caching (first vs second call)
2. Shows async processing (main thread vs background)
3. Demonstrates pagination
4. Shows timing differences

**What to observe:**
1. Console output shows clear timing differences
2. Caching improvement is visible
3. Async processing doesn't block
4. Pagination loads only needed data

---

## Step 9: Experiment and Modify (15 minutes)

### 9.1 Try These Modifications

**Modify Cache:**
1. Change cache type in application.properties
2. Add more cache names
3. See how it affects performance

**Modify Connection Pool:**
1. Change `maximum-pool-size` to a smaller value
2. Run application and make many requests
3. See connection pool behavior
4. Change back to larger value

**Modify Async:**
1. Remove `@Async` from a method
2. See how it blocks the caller
3. Add it back and see the difference

**Add More Methods:**
1. Create your own cached method
2. Create your own async method
3. Test them and see performance

---

## Step 10: Understand the Complete Flow (10 minutes)

### 10.1 Trace Through the Code
**What to do:**
1. Start from `SpringBootAdvancedDemoApplication`
2. See how `@EnableCaching` and `@EnableAsync` are enabled
3. Follow the `CommandLineRunner` that runs the demo
4. Trace through each optimization strategy

**Key Understanding:**
- All optimizations work together
- Each addresses a different performance aspect
- Combined effect is significant

---

## Quick Reference

### Key Files to Study:
1. `PerformanceOptimizedService.java` - Main service with all optimizations
2. `application.properties` - All configuration
3. `SpringBootAdvancedDemoApplication.java` - Main class with enable annotations
4. `User.java` - Entity with lazy loading (when you add relationships)

### Key Annotations:
- `@Cacheable` - Caches method results
- `@Async` - Executes method asynchronously
- `@Transactional(readOnly = true)` - Optimizes for reads
- `@EnableCaching` - Enables caching
- `@EnableAsync` - Enables async processing

### Key Properties:
- `spring.datasource.hikari.*` - Connection pool configuration
- `server.compression.enabled` - HTTP compression
- `spring.cache.type` - Cache implementation
- `management.endpoints.*` - Actuator configuration

---

## Troubleshooting

**Application won't start:**
- Check Java version (need 11+)
- Verify Maven dependencies
- Check port 8080 is available

**Cache not working:**
- Verify `@EnableCaching` is present
- Check cache configuration in properties
- Clear cache and try again

**Async not working:**
- Verify `@EnableAsync` is present
- Check thread pool configuration
- Look for exceptions in console

**Actuator endpoints not accessible:**
- Check `management.endpoints.web.exposure.include` in properties
- Verify actuator dependency is present

---

## Next Steps

1. Read **VIDEO_SCRIPT.md** for detailed explanations
2. Watch the console output carefully
3. Experiment with different configurations
4. Monitor performance using Actuator
5. Apply these techniques to your own projects

---

## Performance Tips

1. **Measure First**: Use Actuator to identify bottlenecks
2. **Cache Wisely**: Cache frequently accessed, rarely changed data
3. **Async Appropriately**: Use async for I/O operations, not CPU-bound tasks
4. **Pool Sizes**: Monitor and adjust connection and thread pool sizes
5. **Monitor Always**: Keep Actuator enabled in production (with security)

Happy Learning! 🚀

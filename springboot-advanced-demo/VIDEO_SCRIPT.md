# Video Script - Spring Boot Performance Optimization

## Introduction (30 seconds)

Hello everyone! Welcome to this tutorial on Spring Boot Performance Optimization. Today, we'll answer a very common interview question: "How can you improve the performance of a Spring Boot application?"

I'll show you 10 practical strategies with real code examples that you can run and see in action. Each technique will be demonstrated with actual working code, so you can understand not just the theory, but see the performance improvements yourself.

Let's dive in!

---

## Main Content (8-10 minutes)

### Opening (30 seconds)

Performance is crucial for any Spring Boot application. Whether you're building a REST API, a web application, or a microservice, users expect fast response times and efficient resource usage. In this video, I'll show you 10 proven strategies to optimize your Spring Boot application's performance.

---

### Strategy 1: Connection Pooling (1 minute)

Let me start by showing you the application.properties file. Open `src/main/resources/application.properties`.

Here you can see HikariCP connection pool configuration. Spring Boot uses HikariCP by default, which is already a high-performance connection pool. But we can tune it for better performance.

Look at these properties:
- `spring.datasource.hikari.maximum-pool-size=20` - This sets the maximum number of connections in the pool
- `spring.datasource.hikari.minimum-idle=5` - This keeps some connections ready, reducing connection acquisition time
- `spring.datasource.hikari.connection-timeout=30000` - How long to wait for a connection

Connection pooling is crucial because establishing a database connection is expensive. Instead of creating a new connection for each request, we reuse connections from the pool. This significantly reduces overhead and improves response times.

You can monitor your connection pool using Spring Boot Actuator. Visit `http://localhost:8080/actuator/metrics/hikari.connections.active` to see how many connections are currently in use.

---

### Strategy 2: Lazy Loading for JPA Entities (1 minute)

Now let's look at the User entity. Open `src/main/java/com/harshit/demo/entity/User.java`.

Notice that this is a simple entity now, but in a real application, you might have relationships like `@OneToMany` or `@ManyToOne`. The key point here is to always use `FetchType.LAZY` instead of `FetchType.EAGER` for relationships.

Eager loading can cause the N+1 query problem. For example, if you load 100 users and each user has orders, eager loading would execute 1 query for users and 100 additional queries for orders - that's 101 queries total!

With lazy loading, related entities are only loaded when you actually access them. This reduces unnecessary database queries and improves performance.

---

### Strategy 3: Caching (2 minutes)

This is one of the most impactful optimizations. Let's look at the PerformanceOptimizedService class. Open `src/main/java/com/harshit/demo/performance/PerformanceOptimizedService.java`.

Look at the `getUserById` method. It's annotated with `@Cacheable(value = "users", key = "#id")`. This annotation tells Spring to cache the result of this method.

Here's how it works: The first time you call `getUserById(1L)`, Spring executes the method, hits the database, and stores the result in the cache. When you call it again with the same ID, Spring returns the cached value without hitting the database.

Let me run the application to show you this in action. When I run it, you'll see in the console that the first call says "Fetching user from database" and takes some time. The second call with the same ID returns immediately from cache - you won't see the database message, and it's much faster.

This is especially useful for data that doesn't change frequently, like user profiles, product catalogs, or configuration data. You can see the cache configuration in application.properties where we set `spring.cache.type=simple` for in-memory caching. In production, you'd use Redis or another distributed cache.

---

### Strategy 4: Asynchronous Processing (2 minutes)

Now look at the `sendEmailAsync` method. It's annotated with `@Async`. This is a game-changer for operations that don't need immediate results.

When you call this method, Spring doesn't wait for it to complete. Instead, it executes the method in a separate thread from a thread pool and returns immediately. The calling thread can continue with other work.

In the console output, you'll see that the main thread continues immediately while the email sending happens in a background thread. This is perfect for operations like sending emails, generating reports, calling external APIs, or any time-consuming task that doesn't need to block the main request.

Notice that the method returns a `CompletableFuture`. This allows you to get the result later if needed, or you can use it for fire-and-forget operations where you don't need the result.

To enable async processing, we added `@EnableAsync` to the main application class. Spring automatically creates a thread pool for async operations.

---

### Strategy 5: Database Query Optimization (1.5 minutes)

Look at the `getUsersPaginated` method. Instead of loading all users into memory, it uses pagination with `Pageable`. This is crucial for large datasets.

If you have 10,000 users, you don't want to load all of them at once. With pagination, you load only the requested page - maybe 10 or 20 records at a time. This reduces memory usage and improves response times.

The method is also annotated with `@Transactional(readOnly = true)`. This tells Spring and the database that this is a read-only operation, allowing them to apply optimizations.

We also have the `findUsersByEmail` method that uses a custom repository method. Instead of loading all users and filtering in Java, we filter in the database query, which is much more efficient.

---

### Strategy 6: HTTP Compression (30 seconds)

Back in the application.properties, you can see `server.compression.enabled=true`. This compresses HTTP responses before sending them to clients.

For example, if your API returns a 100KB JSON response, compression might reduce it to 20KB. This reduces network bandwidth, improves response times, especially for mobile clients or slow connections, and reduces server load.

Spring Boot automatically handles this - you just enable it, and it works for all responses.

---

### Strategy 7: Spring Boot Actuator (1 minute)

Actuator is built into Spring Boot and provides endpoints to monitor your application's performance. You can see memory usage, thread pool statistics, database connection pool status, and request response times.

Visit `http://localhost:8080/actuator` to see all available endpoints. The metrics endpoint shows various performance metrics that help you identify bottlenecks.

For example, you can see:
- How many requests per second your application handles
- Average response times
- Database connection pool usage
- Cache hit rates
- Thread pool statistics

This monitoring is essential for identifying performance issues in production.

---

### Strategy 8: Thread Pool Configuration (1 minute)

When using `@Async`, Spring uses a default thread pool. But for better control and performance, you should configure a custom thread pool.

You can create a `ThreadPoolTaskExecutor` bean with appropriate core pool size and maximum pool size based on your workload. This ensures you have enough threads to handle concurrent async operations without creating too many threads that waste resources.

The optimal thread pool size depends on your application. A common formula is: number of CPU cores multiplied by 2, plus the number of disk spindles. But you should monitor and adjust based on your actual workload.

---

### Strategy 9: Connection Pooling for HTTP Calls (30 seconds)

If your application makes HTTP calls to external services, you should use connection pooling for those calls too. If you're using RestTemplate, configure it with a connection pool.

Even better, use WebClient from Spring WebFlux for non-blocking, reactive HTTP calls. WebClient is more efficient for high-concurrency scenarios because it doesn't block threads while waiting for HTTP responses.

---

### Strategy 10: JVM Optimizations (30 seconds)

Finally, enable JVM optimizations. Use appropriate JVM flags like `-XX:+UseG1GC` for garbage collection, which is better for applications with large heaps and low latency requirements.

Set proper heap sizes based on your application's memory requirements. Too small, and you'll get OutOfMemoryErrors. Too large, and you'll waste resources and have longer garbage collection pauses.

You can set these when starting your application:
```bash
java -XX:+UseG1GC -Xmx2g -Xms2g -jar app.jar
```

---

## Demonstration (2 minutes)

Now let me run the application and show you these optimizations in action.

[Run the application]

Watch the console output. You'll see:

1. First, the application starts and shows actuator endpoints
2. Then the performance demonstration runs automatically
3. For caching: Notice the first call takes time, the second call is instant
4. For async: Notice the main thread continues immediately while email sends in background
5. For pagination: Notice only the requested page is loaded

The timing differences are clearly visible in the console output. Caching can improve response times by 10x or more for frequently accessed data. Async processing allows your application to handle many more concurrent requests.

---

## Summary (1 minute)

So to summarize, here are the 10 strategies to improve Spring Boot application performance:

1. Enable and tune connection pooling with HikariCP
2. Use lazy loading for JPA entity relationships
3. Implement caching with @Cacheable for frequently accessed data
4. Use async processing with @Async for non-blocking operations
5. Optimize database queries with pagination and proper indexing
6. Enable HTTP compression to reduce bandwidth
7. Use Spring Boot Actuator to monitor and identify bottlenecks
8. Configure thread pools appropriately for async operations
9. Use connection pooling for external HTTP calls, preferably WebClient
10. Enable JVM optimizations like G1GC and proper heap sizes

Each of these strategies addresses a different aspect of performance. Used together, they can dramatically improve your application's speed and efficiency.

The key is to measure first - use Actuator to identify bottlenecks, then apply the appropriate optimizations. Don't optimize blindly - measure, optimize, measure again.

---

## Conclusion (30 seconds)

Thank you for watching! I hope this tutorial helped you understand how to optimize Spring Boot application performance. 

All the code is available in this project, so you can run it yourself and experiment with different configurations. Try modifying the cache settings, thread pool sizes, or connection pool configuration to see how they affect performance.

Don't forget to like this video if it helped you, subscribe for more Spring Boot tutorials, and check out the code on GitHub. Happy coding!

# Multithreading in Production: Important Scenarios and Solutions

## 📋 Table of Contents
1. [Race Conditions](#1-race-conditions)
2. [Deadlocks](#2-deadlocks)
3. [Thread Safety Issues](#3-thread-safety-issues)
4. [Shared Resource Access](#4-shared-resource-access)
5. [Producer-Consumer Problem](#5-producer-consumer-problem)
6. [Async Processing Pitfalls](#6-async-processing-pitfalls)
7. [Thread Pool Exhaustion](#7-thread-pool-exhaustion)
8. [Transaction Management in Multithreading](#8-transaction-management-in-multithreading)
9. [Cache Invalidation Race Conditions](#9-cache-invalidation-race-conditions)
10. [Concurrent Collection Issues](#10-concurrent-collection-issues)
11. [Memory Visibility Problems](#11-memory-visibility-problems)
12. [Thread Interruption Handling](#12-thread-interruption-handling)

---

## 1. Race Conditions

### Scenario
Multiple threads access and modify shared data simultaneously without proper synchronization, leading to unpredictable results.

### Problem Example
```java
@Service
public class CounterService {
    private int count = 0;  // ❌ NOT THREAD-SAFE
    
    public void increment() {
        count++;  // This is NOT atomic! (read-modify-write)
    }
    
    public int getCount() {
        return count;
    }
}
```

**Problem:** `count++` is actually 3 operations:
1. Read current value
2. Increment it
3. Write back

Two threads can read the same value, both increment, and both write back, causing lost updates.

### Solutions

#### Solution 1: Synchronized Method
```java
@Service
public class CounterService {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

#### Solution 2: AtomicInteger (Recommended)
```java
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CounterService {
    private final AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet();
    }
    
    public int getCount() {
        return count.get();
    }
}
```

#### Solution 3: ReentrantLock
```java
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CounterService {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();
    
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
}
```

**Best Practice:** Use `AtomicInteger`, `AtomicLong`, `AtomicReference` for simple counters. They're faster and lock-free.

---

## 2. Deadlocks

### Scenario
Two or more threads are blocked forever, waiting for each other to release locks.

### Problem Example
```java
@Service
public class DeadlockExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void method1() {
        synchronized (lock1) {
            System.out.println("Thread 1: Holding lock1");
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            synchronized (lock2) {  // ❌ Waiting for lock2
                System.out.println("Thread 1: Holding lock1 and lock2");
            }
        }
    }
    
    public void method2() {
        synchronized (lock2) {
            System.out.println("Thread 2: Holding lock2");
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            synchronized (lock1) {  // ❌ Waiting for lock1
                System.out.println("Thread 2: Holding lock1 and lock2");
            }
        }
    }
}
```

**Problem:** If Thread 1 holds lock1 and waits for lock2, while Thread 2 holds lock2 and waits for lock1 → **DEADLOCK**

### Solutions

#### Solution 1: Always Acquire Locks in Same Order
```java
@Service
public class DeadlockSolution {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void method1() {
        synchronized (lock1) {  // Always lock1 first
            synchronized (lock2) {  // Then lock2
                // Critical section
            }
        }
    }
    
    public void method2() {
        synchronized (lock1) {  // Same order: lock1 first
            synchronized (lock2) {  // Then lock2
                // Critical section
            }
        }
    }
}
```

#### Solution 2: TryLock with Timeout
```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

@Service
public class DeadlockSolution {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    
    public void method1() {
        boolean acquiredLock1 = false;
        boolean acquiredLock2 = false;
        
        try {
            acquiredLock1 = lock1.tryLock(5, TimeUnit.SECONDS);
            if (acquiredLock1) {
                acquiredLock2 = lock2.tryLock(5, TimeUnit.SECONDS);
                if (acquiredLock2) {
                    // Critical section
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquiredLock2) lock2.unlock();
            if (acquiredLock1) lock1.unlock();
        }
    }
}
```

#### Solution 3: Use Single Lock for Related Operations
```java
@Service
public class DeadlockSolution {
    private final Object sharedLock = new Object();
    
    public void method1() {
        synchronized (sharedLock) {
            // All operations use same lock
        }
    }
    
    public void method2() {
        synchronized (sharedLock) {
            // All operations use same lock
        }
    }
}
```

**Best Practice:** 
- Always acquire locks in a consistent order
- Use timeout when acquiring locks
- Avoid nested locks when possible
- Use lock-free data structures when applicable

---

## 3. Thread Safety Issues

### Scenario
Non-thread-safe collections or operations accessed by multiple threads.

### Problem Example
```java
@Service
public class UserService {
    private List<String> users = new ArrayList<>();  // ❌ NOT THREAD-SAFE
    
    public void addUser(String user) {
        users.add(user);  // ConcurrentModificationException possible
    }
    
    public List<String> getAllUsers() {
        return users;  // ❌ Exposes internal state
    }
}
```

### Solutions

#### Solution 1: Use Concurrent Collections
```java
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    // Option 1: CopyOnWriteArrayList (good for read-heavy)
    private final List<String> users = new CopyOnWriteArrayList<>();
    
    // Option 2: Synchronized wrapper
    // private final List<String> users = Collections.synchronizedList(new ArrayList<>());
    
    public void addUser(String user) {
        users.add(user);  // ✅ Thread-safe
    }
    
    public List<String> getAllUsers() {
        return new ArrayList<>(users);  // ✅ Return copy, not reference
    }
}
```

#### Solution 2: Use ConcurrentHashMap
```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<Long, String> userCache = new ConcurrentHashMap<>();
    
    public void addUser(Long id, String name) {
        userCache.put(id, name);  // ✅ Thread-safe
    }
    
    public String getUser(Long id) {
        return userCache.get(id);
    }
    
    // Atomic operations
    public String getUserOrCreate(Long id, String defaultName) {
        return userCache.computeIfAbsent(id, k -> defaultName);
    }
}
```

**Best Practice:**
- Use `ConcurrentHashMap` instead of `HashMap` + `synchronized`
- Use `CopyOnWriteArrayList` for read-heavy scenarios
- Use `BlockingQueue` for producer-consumer scenarios
- Always return defensive copies when exposing collections

---

## 4. Shared Resource Access

### Scenario
Multiple threads accessing shared resources like files, databases, or external services.

### Problem Example
```java
@Service
public class FileService {
    public void writeToFile(String data) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(data + "\n");  // ❌ Multiple threads can corrupt file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### Solutions

#### Solution 1: Synchronized Access
```java
@Service
public class FileService {
    private final Object fileLock = new Object();
    
    public void writeToFile(String data) {
        synchronized (fileLock) {
            try (FileWriter writer = new FileWriter("log.txt", true)) {
                writer.write(data + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

#### Solution 2: Use Thread-Safe Writer
```java
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileService {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    public void writeToFile(String data) {
        lock.writeLock().lock();
        try {
            try (FileWriter writer = new FileWriter("log.txt", true)) {
                writer.write(data + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

#### Solution 3: Use Queue-Based Approach (Best for High Concurrency)
```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class FileService {
    private final BlockingQueue<String> writeQueue = new LinkedBlockingQueue<>();
    
    @PostConstruct
    public void init() {
        // Single writer thread
        new Thread(() -> {
            try (FileWriter writer = new FileWriter("log.txt", true)) {
                while (true) {
                    String data = writeQueue.take();  // Blocks until data available
                    writer.write(data + "\n");
                    writer.flush();
                }
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    public void writeToFile(String data) {
        writeQueue.offer(data);  // ✅ Non-blocking, thread-safe
    }
}
```

**Best Practice:**
- Use single writer thread with queue for file operations
- Use connection pooling for database access
- Use rate limiting for external API calls
- Implement retry logic with exponential backoff

---

## 5. Producer-Consumer Problem

### Scenario
Multiple producers adding items to a queue, multiple consumers processing them.

### Problem Example
```java
@Service
public class TaskProcessor {
    private final Queue<String> taskQueue = new LinkedList<>();  // ❌ NOT THREAD-SAFE
    
    public void addTask(String task) {
        taskQueue.offer(task);
    }
    
    public String processTask() {
        return taskQueue.poll();  // ❌ Can return null unexpectedly
    }
}
```

### Solutions

#### Solution 1: BlockingQueue (Recommended)
```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class TaskProcessor {
    private final BlockingQueue<String> taskQueue = new LinkedBlockingQueue<>(100);
    
    public void addTask(String task) throws InterruptedException {
        taskQueue.put(task);  // Blocks if queue is full
    }
    
    public String processTask() throws InterruptedException {
        return taskQueue.take();  // Blocks until task available
    }
    
    // With timeout
    public String processTaskWithTimeout() throws InterruptedException {
        return taskQueue.poll(5, TimeUnit.SECONDS);  // Returns null if timeout
    }
}
```

#### Solution 2: Multiple Consumers with Thread Pool
```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TaskProcessor {
    private final BlockingQueue<String> taskQueue = new LinkedBlockingQueue<>(100);
    private final ExecutorService consumerPool = Executors.newFixedThreadPool(5);
    
    @PostConstruct
    public void startConsumers() {
        for (int i = 0; i < 5; i++) {
            consumerPool.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String task = taskQueue.take();
                        processTask(task);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }
    
    public void addTask(String task) throws InterruptedException {
        taskQueue.put(task);
    }
    
    private void processTask(String task) {
        // Process the task
        System.out.println("Processing: " + task);
    }
    
    @PreDestroy
    public void shutdown() {
        consumerPool.shutdown();
    }
}
```

**Best Practice:**
- Use `BlockingQueue` for producer-consumer scenarios
- Set appropriate queue capacity to prevent memory issues
- Use thread pools for multiple consumers
- Implement graceful shutdown

---

## 6. Async Processing Pitfalls

### Scenario
Using `@Async` incorrectly, leading to unexpected behavior.

### Problem Example
```java
@Service
public class AsyncService {
    @Async
    public void processAsync() {
        // ❌ Calling another @Async method in same class
        anotherAsyncMethod();  // Won't work! Not proxied
    }
    
    @Async
    public void anotherAsyncMethod() {
        // This runs synchronously!
    }
    
    // ❌ No exception handling
    @Async
    public void riskyOperation() {
        throw new RuntimeException("Error!");  // Exception is lost!
    }
}
```

### Solutions

#### Solution 1: Proper Async Configuration
```java
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
    
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
    
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            // ✅ Handle exceptions properly
            System.err.println("Async exception in " + method.getName() + ": " + ex.getMessage());
            // Log to monitoring system
        };
    }
}
```

#### Solution 2: Proper Async Usage
```java
@Service
public class AsyncService {
    
    @Autowired
    private AsyncService self;  // Self-injection for same-class calls
    
    @Async
    public CompletableFuture<String> processAsync(String data) {
        try {
            // Long-running operation
            Thread.sleep(1000);
            return CompletableFuture.completedFuture("Processed: " + data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }
    
    public void callAsyncFromSameClass() {
        // ✅ Use self-injection
        self.processAsync("data");
    }
    
    // ✅ Proper exception handling
    @Async
    public CompletableFuture<String> riskyOperation() {
        try {
            // Risky operation
            return CompletableFuture.completedFuture("Success");
        } catch (Exception e) {
            // Log and handle
            return CompletableFuture.failedFuture(e);
        }
    }
}
```

#### Solution 3: Handle CompletableFuture Properly
```java
@Service
public class AsyncService {
    
    @Async
    public CompletableFuture<String> fetchData(String url) {
        // Simulate API call
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                return "Data from " + url;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }
    
    public void handleMultipleAsync() {
        CompletableFuture<String> future1 = fetchData("url1");
        CompletableFuture<String> future2 = fetchData("url2");
        
        // ✅ Wait for all
        CompletableFuture.allOf(future1, future2)
            .thenRun(() -> {
                try {
                    String result1 = future1.get();
                    String result2 = future2.get();
                    // Process results
                } catch (Exception e) {
                    // Handle exception
                }
            });
        
        // ✅ Or with timeout
        try {
            CompletableFuture.allOf(future1, future2)
                .get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // Handle timeout
        }
    }
}
```

**Best Practice:**
- Always configure `AsyncUncaughtExceptionHandler`
- Use `CompletableFuture` for exception handling
- Use self-injection for same-class async calls
- Set appropriate thread pool sizes
- Always handle timeouts

---

## 7. Thread Pool Exhaustion

### Scenario
Too many tasks submitted to thread pool, causing tasks to be rejected or application to hang.

### Problem Example
```java
@Service
public class TaskService {
    @Async
    public void processTask(String task) {
        // Long-running task
        try {
            Thread.sleep(10000);  // 10 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ❌ Submitting 1000 tasks to pool of 20 threads
    public void processManyTasks() {
        for (int i = 0; i < 1000; i++) {
            processTask("task-" + i);  // Will exhaust thread pool!
        }
    }
}
```

### Solutions

#### Solution 1: Proper Thread Pool Configuration
```java
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);  // ✅ Increase max pool size
        executor.setQueueCapacity(200);  // ✅ Larger queue
        executor.setKeepAliveSeconds(60);
        
        // ✅ Rejection policy: Caller runs (backpressure)
        executor.setRejectedExecutionHandler(
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}
```

#### Solution 2: Use Semaphore for Rate Limiting
```java
import java.util.concurrent.Semaphore;

@Service
public class TaskService {
    private final Semaphore semaphore = new Semaphore(50);  // Max 50 concurrent
    
    @Async
    public CompletableFuture<String> processTask(String taskId) {
        try {
            semaphore.acquire();  // ✅ Rate limiting
            try {
                System.out.println("Processing task: " + taskId);
                Thread.sleep(1000);
                return CompletableFuture.completedFuture("Completed: " + taskId);
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }
}
```

#### Solution 3: Use Bounded Queue with Monitoring
```java
@Service
public class TaskService {
    private final ThreadPoolTaskExecutor executor;
    
    public TaskService() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setRejectedExecutionHandler((r, executor) -> {
            // ✅ Log rejection
            System.err.println("Task rejected! Queue full.");
            // Send alert to monitoring system
        });
        executor.initialize();
    }
    
    public void submitTask(Runnable task) {
        try {
            executor.submit(task);
        } catch (RejectedExecutionException e) {
            // ✅ Handle rejection gracefully
            // Maybe save to database for later processing
        }
    }
    
    public void monitorPool() {
        // ✅ Monitor pool status
        System.out.println("Active threads: " + executor.getActiveCount());
        System.out.println("Queue size: " + executor.getQueueSize());
        System.out.println("Pool size: " + executor.getPoolSize());
    }
}
```

**Best Practice:**
- Monitor thread pool metrics (active threads, queue size)
- Set appropriate rejection policies
- Use semaphores for rate limiting
- Implement circuit breakers for external calls
- Use bounded queues to prevent memory issues

---

## 8. Transaction Management in Multithreading

### Scenario
Database transactions and Spring's `@Transactional` don't work across threads.

### Problem Example
```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public void processUsers() {
        // ❌ Transaction context doesn't propagate to async threads
        CompletableFuture.runAsync(() -> {
            userRepository.save(new User("John"));  // No transaction!
        });
    }
}
```

### Solutions

#### Solution 1: Use Programmatic Transaction Management
```java
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionTemplate transactionTemplate;
    
    @Async
    public CompletableFuture<Void> saveUserAsync(User user) {
        return CompletableFuture.runAsync(() -> {
            // ✅ Use TransactionTemplate for async transactions
            transactionTemplate.execute(status -> {
                userRepository.save(user);
                return null;
            });
        });
    }
}
```

#### Solution 2: Use @TransactionalEventListener
```java
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Service
public class UserService {
    
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
        // ✅ Publish event after transaction commits
        applicationEventPublisher.publishEvent(new UserSavedEvent(user));
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserSaved(UserSavedEvent event) {
        // ✅ Runs after transaction commits, in async thread
        // Process user (send email, etc.)
    }
}
```

#### Solution 3: Separate Transaction Boundaries
```java
@Service
public class UserService {
    
    @Transactional
    public void saveUser(User user) {
        // ✅ Save in transaction
        userRepository.save(user);
    }
    
    @Async
    public CompletableFuture<Void> processUserAsync(Long userId) {
        // ✅ Start new transaction in async thread
        return CompletableFuture.runAsync(() -> {
            transactionTemplate.execute(status -> {
                User user = userRepository.findById(userId).orElseThrow();
                // Process user
                return null;
            });
        });
    }
}
```

**Best Practice:**
- Don't expect `@Transactional` to work across threads
- Use `TransactionTemplate` for programmatic transactions in async methods
- Use `@TransactionalEventListener` for post-commit processing
- Keep transaction boundaries small and clear

---

## 9. Cache Invalidation Race Conditions

### Scenario
Multiple threads updating cache simultaneously, causing stale data or cache corruption.

### Problem Example
```java
@Service
public class CacheService {
    private final Map<String, String> cache = new HashMap<>();  // ❌ NOT THREAD-SAFE
    
    @Cacheable("users")
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
    
    @CacheEvict("users")
    public void updateUser(User user) {
        userRepository.save(user);
        // ❌ Race condition: Another thread might read stale cache
    }
}
```

### Solutions

#### Solution 1: Use Spring Cache with Proper Configuration
```java
@Configuration
@EnableCaching
public class CacheConfiguration {
    
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("users");
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }
}
```

#### Solution 2: Use Cache-Aside Pattern with Locking
```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class CacheService {
    private final Map<String, User> cache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    public User getUser(Long id) {
        String key = "user:" + id;
        
        // Try read lock first
        lock.readLock().lock();
        try {
            User user = cache.get(key);
            if (user != null) {
                return user;
            }
        } finally {
            lock.readLock().unlock();
        }
        
        // Upgrade to write lock
        lock.writeLock().lock();
        try {
            // Double-check (another thread might have loaded it)
            User user = cache.get(key);
            if (user != null) {
                return user;
            }
            
            // Load from database
            user = userRepository.findById(id).orElseThrow();
            cache.put(key, user);
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

#### Solution 3: Use Caffeine Cache (Recommended)
```java
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;

@Service
public class CacheService {
    private final Cache<Long, User> cache = Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .recordStats()  // ✅ Enable statistics
        .build();
    
    public User getUser(Long id) {
        return cache.get(id, key -> 
            userRepository.findById(key).orElseThrow()
        );
    }
    
    public void invalidateUser(Long id) {
        cache.invalidate(id);  // ✅ Thread-safe invalidation
    }
}
```

**Best Practice:**
- Use thread-safe cache implementations
- Implement proper cache invalidation strategies
- Use cache statistics to monitor hit rates
- Set appropriate TTL (Time To Live) for cache entries
- Consider using distributed cache (Redis) for multi-instance deployments

---

## 10. Concurrent Collection Issues

### Scenario
Using regular collections in concurrent scenarios, leading to `ConcurrentModificationException` or data corruption.

### Problem Example
```java
@Service
public class UserService {
    private final Map<Long, User> activeUsers = new HashMap<>();  // ❌ NOT THREAD-SAFE
    
    public void addActiveUser(Long id, User user) {
        activeUsers.put(id, user);  // ❌ Can cause data corruption
    }
    
    public void iterateUsers() {
        for (User user : activeUsers.values()) {  // ❌ ConcurrentModificationException
            processUser(user);
        }
    }
}
```

### Solutions

#### Solution 1: Use ConcurrentHashMap
```java
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final ConcurrentHashMap<Long, User> activeUsers = new ConcurrentHashMap<>();
    
    public void addActiveUser(Long id, User user) {
        activeUsers.put(id, user);  // ✅ Thread-safe
    }
    
    public void iterateUsers() {
        // ✅ Safe iteration (snapshot view)
        activeUsers.values().forEach(this::processUser);
    }
    
    // ✅ Atomic operations
    public User getOrCreateUser(Long id) {
        return activeUsers.computeIfAbsent(id, 
            key -> userRepository.findById(key).orElseThrow()
        );
    }
}
```

#### Solution 2: Use CopyOnWriteArrayList for Read-Heavy Scenarios
```java
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class UserService {
    private final CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
    
    public void addUser(User user) {
        users.add(user);  // ✅ Thread-safe, creates copy on write
    }
    
    public void iterateUsers() {
        // ✅ Safe iteration (snapshot)
        users.forEach(this::processUser);
    }
}
```

#### Solution 3: Synchronized Collections with Defensive Copying
```java
import java.util.Collections;

@Service
public class UserService {
    private final List<User> users = Collections.synchronizedList(new ArrayList<>());
    
    public List<User> getAllUsers() {
        // ✅ Return defensive copy
        synchronized (users) {
            return new ArrayList<>(users);
        }
    }
}
```

**Best Practice:**
- Use `ConcurrentHashMap` instead of `HashMap` + `synchronized`
- Use `CopyOnWriteArrayList` for read-heavy, write-light scenarios
- Always return defensive copies when exposing collections
- Use atomic operations (`computeIfAbsent`, `merge`, etc.) when possible

---

## 11. Memory Visibility Problems

### Scenario
Changes made by one thread not visible to other threads due to CPU caching.

### Problem Example
```java
@Service
public class FlagService {
    private boolean running = false;  // ❌ No visibility guarantee
    
    public void start() {
        running = true;  // Might not be visible to other threads
    }
    
    public boolean isRunning() {
        return running;  // Might see stale value
    }
}
```

### Solutions

#### Solution 1: Use Volatile
```java
@Service
public class FlagService {
    private volatile boolean running = false;  // ✅ Ensures visibility
    
    public void start() {
        running = true;
    }
    
    public boolean isRunning() {
        return running;
    }
}
```

#### Solution 2: Use AtomicBoolean
```java
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FlagService {
    private final AtomicBoolean running = new AtomicBoolean(false);
    
    public void start() {
        running.set(true);
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    // ✅ Atomic compare-and-set
    public boolean tryStart() {
        return running.compareAndSet(false, true);
    }
}
```

#### Solution 3: Use Synchronized
```java
@Service
public class FlagService {
    private boolean running = false;
    
    public synchronized void start() {
        running = true;  // ✅ Synchronized ensures visibility
    }
    
    public synchronized boolean isRunning() {
        return running;
    }
}
```

**Best Practice:**
- Use `volatile` for simple flags (single writer, multiple readers)
- Use `AtomicBoolean`, `AtomicInteger`, etc. for atomic operations
- Use `synchronized` or locks for complex state changes
- Remember: `volatile` ensures visibility, not atomicity

---

## 12. Thread Interruption Handling

### Scenario
Long-running tasks not responding to interruption, causing threads to hang.

### Problem Example
```java
@Service
public class TaskService {
    @Async
    public void longRunningTask() {
        while (true) {  // ❌ Never checks interruption
            // Do work
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ❌ Doesn't restore interrupt status
            }
        }
    }
}
```

### Solutions

#### Solution 1: Proper Interruption Handling
```java
@Service
public class TaskService {
    @Async
    public void longRunningTask() {
        while (!Thread.currentThread().isInterrupted()) {  // ✅ Check interruption
            try {
                // Do work
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // ✅ Restore interrupt status
                break;  // Exit loop
            }
        }
    }
}
```

#### Solution 2: Use CompletableFuture with Cancellation
```java
@Service
public class TaskService {
    private final Map<String, CompletableFuture<Void>> runningTasks = new ConcurrentHashMap<>();
    
    public CompletableFuture<Void> startTask(String taskId) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Do work
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        runningTasks.put(taskId, future);
        return future;
    }
    
    public void cancelTask(String taskId) {
        CompletableFuture<Void> future = runningTasks.remove(taskId);
        if (future != null) {
            future.cancel(true);  // ✅ Interrupt if running
        }
    }
}
```

#### Solution 3: Use ExecutorService with Shutdown
```java
@Service
public class TaskService {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    
    public void submitTask(Runnable task) {
        executor.submit(task);
    }
    
    @PreDestroy
    public void shutdown() {
        executor.shutdown();  // ✅ Stop accepting new tasks
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();  // ✅ Force interrupt running tasks
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

**Best Practice:**
- Always check `Thread.currentThread().isInterrupted()` in loops
- Restore interrupt status after catching `InterruptedException`
- Use `CompletableFuture.cancel(true)` to interrupt tasks
- Implement graceful shutdown for thread pools
- Don't swallow `InterruptedException` - handle it properly

---

## 🎯 Summary: Best Practices for Production Multithreading

1. **Use Thread-Safe Collections**
   - `ConcurrentHashMap` instead of `HashMap`
   - `CopyOnWriteArrayList` for read-heavy scenarios
   - `BlockingQueue` for producer-consumer

2. **Prefer Atomic Classes**
   - `AtomicInteger`, `AtomicLong`, `AtomicBoolean`
   - Faster than synchronized blocks
   - Lock-free implementations

3. **Configure Thread Pools Properly**
   - Set appropriate core and max pool sizes
   - Use bounded queues
   - Implement rejection policies
   - Monitor pool metrics

4. **Handle Exceptions in Async Code**
   - Configure `AsyncUncaughtExceptionHandler`
   - Use `CompletableFuture` for exception handling
   - Never let exceptions be silently swallowed

5. **Avoid Deadlocks**
   - Always acquire locks in consistent order
   - Use timeout when acquiring locks
   - Prefer lock-free data structures

6. **Ensure Memory Visibility**
   - Use `volatile` for flags
   - Use `synchronized` or locks for complex state
   - Understand happens-before relationships

7. **Handle Interruptions Properly**
   - Check `isInterrupted()` in loops
   - Restore interrupt status
   - Implement graceful shutdown

8. **Monitor and Measure**
   - Use Spring Boot Actuator
   - Monitor thread pool metrics
   - Track cache hit rates
   - Set up alerts for thread pool exhaustion

9. **Test Concurrent Code**
   - Write unit tests with multiple threads
   - Use stress testing
   - Test failure scenarios

10. **Document Thread Safety**
    - Document which classes are thread-safe
    - Document which methods require external synchronization
    - Use `@ThreadSafe` annotations where applicable

---

## 📚 Additional Resources

- Java Concurrency in Practice (Book)
- Java `java.util.concurrent` package documentation
- Spring Framework Async documentation
- Caffeine Cache documentation
- Project Loom (Virtual Threads) - Future of Java concurrency

---

**Remember:** Multithreading is powerful but complex. Always test thoroughly, monitor in production, and prefer proven patterns over custom solutions.


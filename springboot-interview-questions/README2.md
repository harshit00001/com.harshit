## 📌 **INTERVIEW QUESTION 1: How can you improve the performance of a Spring Boot application?**

**Answer:**

There are several strategies you can use to improve the performance of a Spring Boot application, and I'll explain the most important ones.

First, you should enable connection pooling for your database. By default, Spring Boot uses HikariCP, which is already a high-performance connection pool, but you can tune it by setting properties like `spring.datasource.hikari.maximum-pool-size` and `spring.datasource.hikari.minimum-idle`. Connection pooling reuses database connections instead of creating new ones for each request, which significantly reduces overhead.

Second, use lazy loading for JPA entities. By default, you should use `FetchType.LAZY` for relationships instead of `FetchType.EAGER`. Eager loading can cause the N+1 query problem, where loading one entity triggers multiple additional queries. With lazy loading, related entities are only loaded when accessed, which reduces unnecessary database queries.

Third, implement caching. Spring Boot provides excellent caching support through the `@Cacheable` annotation. You can cache method results so that expensive operations don't need to run every time. For example, if you have a method that fetches user data from the database, you can cache it so subsequent calls return the cached value instead of hitting the database again.

Fourth, use asynchronous processing with `@Async` for operations that don't need immediate results. If you have operations like sending emails, generating reports, or calling external APIs, you can make them asynchronous so they don't block the main thread. This allows your application to handle more requests concurrently.

Fifth, optimize your database queries. Use `@Query` with proper joins instead of multiple separate queries. Use pagination with `Pageable` for large datasets instead of loading everything into memory. Create appropriate database indexes for frequently queried columns.

Sixth, enable HTTP compression by setting `server.compression.enabled=true` in your application.properties. This compresses responses before sending them to clients, reducing network bandwidth and improving response times.

Seventh, use Spring Boot Actuator to monitor your application's performance. Actuator provides metrics endpoints that help you identify bottlenecks. You can see memory usage, thread pool statistics, database connection pool status, and request response times.

Eighth, configure proper thread pool sizes for your application. If you're using `@Async`, configure a custom `ThreadPoolTaskExecutor` with appropriate core pool size and maximum pool size based on your workload.

Ninth, use connection pooling for external HTTP calls. If you're using RestTemplate, configure it with a connection pool. Better yet, use WebClient for non-blocking, reactive HTTP calls, which is more efficient for high-concurrency scenarios.

Tenth, enable JVM optimizations. Use appropriate JVM flags like `-XX:+UseG1GC` for garbage collection, and set proper heap sizes based on your application's memory requirements.

Here's a code example showing caching and async processing:

```java
@Service
public class PerformanceOptimizedService {
    
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        // This method result will be cached
        // Subsequent calls with same ID will return cached value
        return userRepository.findById(id).orElse(null);
    }
    
    @Async
    public CompletableFuture<String> sendEmailAsync(String email) {
        // This runs in a separate thread, not blocking the caller
        emailService.send(email);
        return CompletableFuture.completedFuture("Email sent");
    }
}
```

---

## 📌 **INTERVIEW QUESTION 2: What is the @Transactional annotation and how does it work?**

**Answer:**

The `@Transactional` annotation in Spring is used to declare that a method or class should be executed within a database transaction. When you annotate a method with `@Transactional`, Spring automatically manages the transaction lifecycle - it begins a transaction before the method executes, commits it if the method completes successfully, or rolls it back if an exception is thrown.

The annotation works by creating a proxy around your bean. When you call a transactional method, Spring intercepts the call, starts a transaction, executes your method, and then either commits or rolls back based on whether an exception occurred. This is transparent to you - you just write your business logic, and Spring handles the transaction management.

You can configure several properties on `@Transactional`. The `propagation` property controls how transactions behave when one transactional method calls another. For example, `REQUIRED` means if a transaction already exists, use it; otherwise, create a new one. `REQUIRES_NEW` always creates a new transaction, even if one already exists.

The `isolation` property controls transaction isolation level, which determines how transactions interact with each other. Common levels include `READ_COMMITTED`, which prevents dirty reads, and `SERIALIZABLE`, which provides the highest isolation but can impact performance.

The `readOnly` property, when set to `true`, indicates that the method only reads data and doesn't modify it. This allows Spring and the database to optimize the transaction for read operations.

The `timeout` property sets how long a transaction can run before timing out, and `rollbackFor` specifies which exceptions should trigger a rollback.

Here's an example:

```java
@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private PaymentService paymentService;
    
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Order order) {
        // If any exception occurs, entire transaction rolls back
        orderRepository.save(order);
        paymentService.processPayment(order.getAmount());
        // Transaction commits automatically if no exception
    }
    
    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        // Optimized for read-only operations
        return orderRepository.findById(id).orElse(null);
    }
}
```

---

## 📌 **INTERVIEW QUESTION 3: What is the difference between @Autowired and @Resource annotations?**

**Answer:**

Both `@Autowired` and `@Resource` are used for dependency injection in Spring, but they work differently and come from different specifications.

`@Autowired` is a Spring-specific annotation that uses type-based injection by default. When Spring encounters `@Autowired`, it looks for a bean that matches the type of the field, parameter, or method. If multiple beans of the same type exist, Spring will throw an exception unless you use `@Qualifier` to specify which one to use. `@Autowired` can also inject by name if you combine it with `@Qualifier`, but type matching is the default behavior.

`@Resource` is part of the Java EE specification, specifically JSR-250, and it uses name-based injection by default. When you use `@Resource`, Spring first tries to find a bean with a matching name. If no bean with that name exists, it falls back to type-based injection. The name used is typically the field name or the name specified in the `name` attribute of the annotation.

Another key difference is that `@Autowired` supports the `required` attribute. If you set `@Autowired(required = false)`, Spring won't throw an exception if no matching bean is found - it will simply leave the field as null. `@Resource` doesn't have this feature - if no bean is found, it will always throw an exception.

`@Autowired` also supports constructor injection, setter injection, and field injection, while `@Resource` is typically used for field and setter injection.

In modern Spring applications, `@Autowired` is more commonly used because it's Spring-native and provides more flexibility. However, `@Resource` can be useful when you want explicit name-based injection or when working in environments that prefer Java EE standards.

Here's an example showing both:

```java
@Service
public class UserService {
    
    // @Autowired - type-based injection
    @Autowired
    private UserRepository userRepository;
    
    // @Autowired with @Qualifier for name-based
    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource dataSource;
    
    // @Resource - name-based injection
    @Resource(name = "emailService")
    private EmailService emailService;
    
    // @Autowired with required = false
    @Autowired(required = false)
    private OptionalService optionalService; // Won't fail if bean doesn't exist
}
```

---

## 📌 **INTERVIEW QUESTION 4: What is @ConditionalOnProperty and when would you use it?**

**Answer:**

`@ConditionalOnProperty` is a Spring Boot conditional annotation that allows you to conditionally create beans or enable configurations based on property values in your application.properties or application.yml file. This is extremely useful for creating environment-specific configurations or feature flags.

When you use `@ConditionalOnProperty`, Spring checks if a specific property exists and has a certain value. If the condition is met, the bean is created or the configuration is enabled. If not, it's skipped entirely.

You specify the property name using the `name` attribute, and optionally specify the expected value using the `havingValue` attribute. If you don't specify `havingValue`, the condition is true if the property exists and is not false. You can also use `matchIfMissing` to specify what should happen if the property doesn't exist at all.

This annotation is commonly used for enabling or disabling features based on environment. For example, you might want to enable certain beans only in development, or disable them in production. You can also use it to switch between different implementations of the same interface based on configuration.

Here's a practical example:

```java
@Configuration
public class FeatureConfiguration {
    
    @Bean
    @ConditionalOnProperty(name = "feature.email.enabled", havingValue = "true", matchIfMissing = false)
    public EmailService emailService() {
        // This bean is only created if feature.email.enabled=true in properties
        return new EmailService();
    }
    
    @Bean
    @ConditionalOnProperty(name = "app.mode", havingValue = "dev")
    public DevelopmentService devService() {
        // Only created in development mode
        return new DevelopmentService();
    }
    
    @Bean
    @ConditionalOnProperty(name = "cache.enabled", matchIfMissing = true)
    public CacheManager cacheManager() {
        // Created if cache.enabled=true or if property doesn't exist
        return new SimpleCacheManager();
    }
}
```

In your application.properties, you would have:

```properties
feature.email.enabled=true
app.mode=dev
cache.enabled=true
```

---

## 📌 **INTERVIEW QUESTION 5: What is @Profile and how does it differ from @ConditionalOnProperty?**

**Answer:**

`@Profile` is a Spring annotation that allows you to conditionally register beans based on which Spring profile is active. Profiles are a way to group configuration and beans that should be available only in certain environments, like development, testing, or production.

When you use `@Profile`, you specify one or more profile names. The bean is only created if at least one of those profiles is active. You activate profiles by setting the `spring.profiles.active` property or by using the `SPRING_PROFILES_ACTIVE` environment variable.

The key difference between `@Profile` and `@ConditionalOnProperty` is that `@Profile` is profile-based, meaning it's tied to the concept of Spring profiles, while `@ConditionalOnProperty` is property-based and can check any property value, not just profiles.

`@Profile` is simpler and more declarative - you just say "this bean is for development" or "this bean is for production." `@ConditionalOnProperty` is more flexible and can check any property with any value, making it suitable for feature flags or more complex conditional logic.

You can also use `@Profile` at the class level on `@Configuration` classes, which means all beans in that configuration class are only created when the specified profile is active.

Here's an example:

```java
@Configuration
@Profile("dev")
public class DevConfiguration {
    // All beans in this class only exist in dev profile
    @Bean
    public DevDataSource devDataSource() {
        return new DevDataSource();
    }
}

@Service
@Profile({"dev", "test"})
public class MockEmailService implements EmailService {
    // This service is used in dev and test, but not in production
    @Override
    public void sendEmail(String to, String message) {
        System.out.println("Mock: Sending email to " + to);
    }
}

@Service
@Profile("prod")
public class RealEmailService implements EmailService {
    // This service is used only in production
    @Override
    public void sendEmail(String to, String message) {
        // Actual email sending logic
    }
}
```

You activate profiles in application.properties:

```properties
spring.profiles.active=dev
```

Or via command line:

```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## 📌 **INTERVIEW QUESTION 6: What is @Scheduled and how do you use it for periodic tasks?**

**Answer:**

`@Scheduled` is a Spring annotation that allows you to execute methods periodically at fixed intervals or at specific times. It's useful for tasks like cleanup jobs, data synchronization, report generation, or any recurring operation that needs to run automatically.

To use `@Scheduled`, you first need to enable scheduling by adding `@EnableScheduling` to your configuration class or main application class. Then, you annotate any method with `@Scheduled` and specify when it should run.

There are several ways to specify the schedule. You can use `fixedRate` to run the method at a fixed interval in milliseconds, regardless of how long the previous execution took. You can use `fixedDelay` to run the method with a fixed delay after the previous execution completes. You can use `cron` expressions for more complex schedules, like "every day at midnight" or "every Monday at 9 AM."

The `fixedRate` is useful when you want consistent timing - the method runs every X milliseconds regardless of execution time. `fixedDelay` is useful when you want to ensure a certain amount of time passes between the end of one execution and the start of the next. `cron` expressions give you the most flexibility for complex schedules.

Here's an example:

```java
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    // Enables scheduling in the application
}

@Component
public class ScheduledTasks {
    
    // Runs every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("Current time: " + new Date());
    }
    
    // Runs 2 seconds after previous execution completes
    @Scheduled(fixedDelay = 2000)
    public void cleanupTask() {
        System.out.println("Cleaning up temporary files...");
    }
    
    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyReport() {
        System.out.println("Generating daily report...");
    }
    
    // Runs every Monday at 9 AM
    @Scheduled(cron = "0 0 9 ? * MON")
    public void weeklyMeeting() {
        System.out.println("Weekly meeting reminder");
    }
    
    // Runs every 30 seconds, but only if property is enabled
    @Scheduled(fixedRate = 30000)
    @ConditionalOnProperty(name = "task.sync.enabled", havingValue = "true")
    public void syncData() {
        System.out.println("Syncing data...");
    }
}
```

The cron expression format is: `second minute hour day month weekday`

---

## 📌 **INTERVIEW QUESTION 7: What is @Valid and @Validated and what's the difference between them?**

**Answer:**

Both `@Valid` and `@Validated` are used for validation in Spring, but they come from different specifications and have some differences in behavior.

`@Valid` is part of the Java Bean Validation specification, specifically JSR-303 and JSR-380. It's a standard Java annotation that triggers validation of the annotated object. When you use `@Valid` on a method parameter, Spring validates that object using the validation constraints defined on its fields, like `@NotNull`, `@Size`, `@Email`, and so on.

`@Validated` is a Spring-specific annotation that extends `@Valid` with additional features. It supports validation groups, which allows you to validate different sets of constraints in different scenarios. For example, you might have different validation rules for creating a user versus updating a user, and you can use validation groups to specify which rules apply.

Another key difference is that `@Validated` can be used at the class level to enable method-level validation. When you put `@Validated` on a class, Spring will validate method parameters that have validation annotations, even if they don't have `@Valid` or `@Validated` on them.

`@Valid` is typically used on method parameters in REST controllers to validate request bodies or path variables. `@Validated` is more commonly used on service classes when you want to validate method parameters using validation groups.

Here's an example:

```java
@RestController
public class UserController {
    
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        // @Valid triggers validation of User object
        // If validation fails, MethodArgumentNotValidException is thrown
        return ResponseEntity.ok(userService.save(user));
    }
}

public class User {
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    @Email(message = "Email should be valid")
    @NotNull
    private String email;
    
    @Min(value = 18, message = "Age must be at least 18")
    private Integer age;
}

@Service
@Validated
public class UserService {
    
    public User findById(@Min(1) Long id) {
        // @Validated on class enables validation of method parameters
        return userRepository.findById(id).orElse(null);
    }
    
    public void updateUser(@Valid User user, @NotNull String reason) {
        // Both parameters are validated
        userRepository.save(user);
    }
}
```

---

## 📌 **INTERVIEW QUESTION 8: What is @ConfigurationProperties and how is it used for externalized configuration?**

**Answer:**

`@ConfigurationProperties` is a Spring Boot annotation that allows you to bind properties from your application.properties or application.yml file directly to a Java object. This is a powerful way to manage configuration in a type-safe, structured manner instead of using `@Value` annotations everywhere.

When you use `@ConfigurationProperties`, you create a class with fields that match property names, and Spring automatically binds the property values to those fields. You can nest objects, use lists, maps, and other complex types. Spring handles type conversion automatically.

To use it, you annotate a class with `@ConfigurationProperties` and specify a prefix. The prefix corresponds to the property keys in your configuration file. For example, if your prefix is `app.database`, Spring will look for properties like `app.database.url`, `app.database.username`, and so on.

You also need to enable configuration properties processing. You can do this by adding `@EnableConfigurationProperties` to a configuration class, or by annotating your properties class with `@Component` so Spring can discover it.

This approach has several advantages over using `@Value`. First, it's type-safe - you get compile-time checking instead of runtime errors. Second, it groups related properties together, making configuration more organized. Third, IDEs can provide autocomplete and validation for your configuration classes. Fourth, you can use validation annotations like `@NotNull` or `@Min` to validate configuration values at startup.

Here's an example:

```java
@Configuration
@EnableConfigurationProperties
public class AppConfiguration {
}

@ConfigurationProperties(prefix = "app")
@Component
@Validated
public class AppProperties {
    
    @NotNull
    private String name;
    
    private String version;
    
    private Database database;
    
    private List<String> features;
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public Database getDatabase() { return database; }
    public void setDatabase(Database database) { this.database = database; }
    
    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
    
    // Nested configuration class
    public static class Database {
        @NotNull
        private String url;
        
        private String username;
        
        private String password;
        
        @Min(1)
        private Integer maxConnections;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public Integer getMaxConnections() { return maxConnections; }
        public void setMaxConnections(Integer maxConnections) { 
            this.maxConnections = maxConnections; 
        }
    }
}
```

In your application.properties:

```properties
app.name=MyApplication
app.version=1.0.0
app.database.url=jdbc:mysql://localhost:3306/mydb
app.database.username=root
app.database.password=secret
app.database.max-connections=10
app.features[0]=feature1
app.features[1]=feature2
```

---

## 📌 **INTERVIEW QUESTION 9: What is @Primary annotation and when should you use it?**

**Answer:**

`@Primary` is a Spring annotation that indicates a bean should be given preference when multiple beans of the same type are available and Spring needs to choose one for autowiring. When you have multiple beans that implement the same interface or have the same type, and you try to autowire that type without specifying which one, Spring will throw a `NoUniqueBeanDefinitionException` unless one of them is marked with `@Primary`.

The `@Primary` annotation essentially tells Spring, "If you're not sure which bean to use, use this one." It's a way to specify a default choice when multiple candidates exist.

You should use `@Primary` when you have a clear default implementation that should be used in most cases, but you also have other implementations that might be used in specific scenarios with `@Qualifier`. For example, you might have a primary database configuration and a secondary database configuration. The primary one would be marked with `@Primary` and used by default, while the secondary one would be injected explicitly using `@Qualifier` where needed.

Another common use case is when you have a default implementation of an interface and one or more alternative implementations. The default one gets `@Primary`, and the alternatives are used with `@Qualifier` when needed.

Here's an example:

```java
@Configuration
public class DatabaseConfiguration {
    
    @Bean
    @Primary
    public DataSource primaryDataSource() {
        // This is the default datasource
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/primarydb");
        return dataSource;
    }
    
    @Bean
    public DataSource secondaryDataSource() {
        // This is used only when explicitly qualified
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/secondarydb");
        return dataSource;
    }
}

@Service
public class UserService {
    
    // Uses primaryDataSource because of @Primary
    @Autowired
    private DataSource dataSource;
    
    // Uses secondaryDataSource explicitly
    @Autowired
    @Qualifier("secondaryDataSource")
    private DataSource secondaryDataSource;
}

// Another example with interfaces
public interface PaymentProcessor {
    void processPayment(double amount);
}

@Service
@Primary
public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment: " + amount);
    }
}

@Service
public class PayPalProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing PayPal payment: " + amount);
    }
}

@Service
public class OrderService {
    // Uses CreditCardProcessor by default because of @Primary
    @Autowired
    private PaymentProcessor paymentProcessor;
    
    // Can explicitly use PayPalProcessor when needed
    @Autowired
    @Qualifier("payPalProcessor")
    private PaymentProcessor payPalProcessor;
}
```

---

## 📌 **INTERVIEW QUESTION 10: What is @Lazy annotation and how does lazy initialization work?**

**Answer:**

`@Lazy` is a Spring annotation that delays the initialization of a bean until it's actually needed. By default, Spring creates all singleton beans when the application context starts. However, if you annotate a bean with `@Lazy`, Spring won't create it until the first time it's requested.

Lazy initialization can improve application startup time because beans that aren't immediately needed aren't created during startup. This is particularly useful for beans that are expensive to create or that might not be used in every run of the application.

You can use `@Lazy` in several ways. You can put it on a `@Bean` method in a configuration class, which makes that specific bean lazy. You can put it on a `@Component` or other stereotype annotation, which makes that component lazy. You can also put it on an injection point, like a constructor parameter or field, which makes only that specific injection lazy.

When you use `@Lazy` on an injection point, Spring creates a proxy for the dependency. The actual bean is only created when you first call a method on it. This is useful for breaking circular dependencies, because the proxy can be injected immediately, but the actual bean creation is deferred.

Here's an example:

```java
@Configuration
public class LazyConfiguration {
    
    @Bean
    @Lazy
    public ExpensiveService expensiveService() {
        // This bean is created only when first requested
        System.out.println("Creating ExpensiveService...");
        return new ExpensiveService();
    }
    
    @Bean
    public RegularService regularService() {
        // This bean is created at startup
        System.out.println("Creating RegularService...");
        return new RegularService();
    }
}

@Component
@Lazy
public class LazyComponent {
    public LazyComponent() {
        System.out.println("LazyComponent created");
    }
}

@Service
public class UserService {
    
    // Regular injection - bean created at startup
    @Autowired
    private RegularService regularService;
    
    // Lazy injection - bean created only when first used
    @Autowired
    @Lazy
    private ExpensiveService expensiveService;
    
    public void doSomething() {
        // ExpensiveService is created here, not at startup
        expensiveService.process();
    }
}

// Using @Lazy to break circular dependencies
@Service
public class ServiceA {
    private final ServiceB serviceB;
    
    public ServiceA(@Lazy ServiceB serviceB) {
        // ServiceB proxy is injected, actual bean created later
        this.serviceB = serviceB;
    }
}

@Service
public class ServiceB {
    private final ServiceA serviceA;
    
    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
```

You can also enable lazy initialization for all beans globally by setting `spring.main.lazy-initialization=true` in your application.properties.

---

## 📌 **INTERVIEW QUESTION 11: What is @Order annotation and how does it control bean ordering?**

**Answer:**

`@Order` is a Spring annotation that specifies the order in which beans should be processed or injected when there are multiple beans of the same type. It's particularly useful for aspects, event listeners, and when you need to control the order of bean initialization or method execution.

The `@Order` annotation takes an integer value. Lower values have higher priority, meaning beans with lower order values are processed first. The default order is `Integer.MAX_VALUE`, which is the lowest priority.

One common use case is with AOP aspects. When you have multiple aspects that apply to the same method, `@Order` determines which aspect's advice runs first. The aspect with the lowest order value executes first.

Another use case is with event listeners. When multiple listeners handle the same event, `@Order` determines the execution order. This is useful when you have listeners that depend on each other or when you need to ensure certain listeners run before others.

`@Order` is also useful when implementing interfaces like `ApplicationListener` or `CommandLineRunner`, where you want to control the order of execution.

Here's an example:

```java
@Aspect
@Component
@Order(1)
public class LoggingAspect {
    // This aspect runs first (lower order value = higher priority)
    
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Logging: " + joinPoint.getSignature());
    }
}

@Aspect
@Component
@Order(2)
public class SecurityAspect {
    // This aspect runs after LoggingAspect
    
    @Before("execution(* com.example.service.*.*(..))")
    public void checkSecurity(JoinPoint joinPoint) {
        System.out.println("Security check: " + joinPoint.getSignature());
    }
}

@Component
@Order(1)
public class FirstCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("First runner executes");
    }
}

@Component
@Order(2)
public class SecondCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("Second runner executes");
    }
}

@Component
@Order(1)
public class FirstEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("First listener handles event");
    }
}

@Component
@Order(2)
public class SecondEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("Second listener handles event");
    }
}
```

---

## 📌 **INTERVIEW QUESTION 12: What is @ControllerAdvice and how is it used for global exception handling?**

**Answer:**

`@ControllerAdvice` is a Spring annotation that allows you to define global exception handlers, model attributes, and data binding that apply to all controllers in your application. It's a centralized way to handle cross-cutting concerns across multiple controllers.

The most common use of `@ControllerAdvice` is for global exception handling. Instead of putting try-catch blocks in every controller method, you can create a class annotated with `@ControllerAdvice` and define methods annotated with `@ExceptionHandler` that handle specific exceptions. When any controller throws an exception, Spring routes it to the appropriate handler method in your `@ControllerAdvice` class.

You can make your exception handling more specific by using the `basePackages` attribute to limit which controllers the advice applies to. You can also use `assignableTypes` to target specific controller classes, or `annotations` to target controllers with specific annotations.

In addition to exception handling, `@ControllerAdvice` can be used with `@ModelAttribute` to add common model attributes to all controllers, or with `@InitBinder` to configure data binding for all controllers.

Here's an example:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    // Handle specific exception
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "USER_NOT_FOUND",
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    // Handle validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed",
            errors,
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_ERROR",
            "An unexpected error occurred",
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    // Add common model attribute to all controllers
    @ModelAttribute("appName")
    public String appName() {
        return "MyApplication";
    }
}

// Error response DTO
public class ErrorResponse {
    private String code;
    private String message;
    private Object details;
    private long timestamp;
    
    // Constructors, getters, setters
}

// Usage in controller
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        // If user not found, exception is handled by GlobalExceptionHandler
        return userService.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }
}
```

---

## 📌 **INTERVIEW QUESTION 13: What is @Cacheable, @CacheEvict, and @CachePut and how do they work together?**

**Answer:**

These three annotations are part of Spring's caching abstraction and work together to manage cached data in your application.

`@Cacheable` is used to cache the result of a method. When you call a method annotated with `@Cacheable`, Spring first checks if the result is already in the cache. If it is, it returns the cached value without executing the method. If not, it executes the method, stores the result in the cache, and returns it. You specify the cache name and optionally a key using SpEL expressions.

`@CachePut` is used to update the cache with the result of a method execution. Unlike `@Cacheable`, `@CachePut` always executes the method and then updates the cache with the result. This is useful when you want to refresh the cache after updating data.

`@CacheEvict` is used to remove entries from the cache. You can evict a specific entry by key, or evict all entries in a cache. This is useful when you delete or update data and want to ensure the cache doesn't contain stale data.

To use these annotations, you need to enable caching with `@EnableCaching` and configure a cache manager, like `ConcurrentMapCacheManager` for simple in-memory caching, or `RedisCacheManager` for distributed caching.

Here's an example:

```java
@Configuration
@EnableCaching
public class CacheConfiguration {
    
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users", "products");
    }
}

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Cache the result - subsequent calls with same ID return cached value
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        System.out.println("Fetching user from database: " + id);
        return userRepository.findById(id).orElse(null);
    }
    
    // Always execute method and update cache
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        System.out.println("Updating user in database");
        User updated = userRepository.save(user);
        // Cache is updated with new value
        return updated;
    }
    
    // Remove specific entry from cache
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        System.out.println("Deleting user from database");
        userRepository.deleteById(id);
        // Cache entry for this ID is removed
    }
    
    // Remove all entries from cache
    @CacheEvict(value = "users", allEntries = true)
    public void clearUserCache() {
        System.out.println("Clearing all user cache entries");
    }
}
```

---

## 📌 **INTERVIEW QUESTION 14: What is @PreAuthorize and @PostAuthorize and how do they provide method-level security?**

**Answer:**

`@PreAuthorize` and `@PostAuthorize` are Spring Security annotations that provide method-level security by controlling access to methods based on security expressions. They allow you to secure individual methods in your service layer, not just HTTP endpoints.

`@PreAuthorize` checks the authorization before the method executes. If the security expression evaluates to false, the method is not executed and a `AccessDeniedException` is thrown. This is useful for checking if a user has permission to perform an action before it happens.

`@PostAuthorize` checks the authorization after the method executes. This is less common but useful when you need to check permissions based on the return value of the method. For example, you might want to allow access only if the returned object belongs to the current user.

To use these annotations, you need to enable method security by adding `@EnableMethodSecurity` or `@EnableGlobalMethodSecurity` to your configuration class. You also need Spring Security on your classpath.

The security expressions use SpEL, Spring Expression Language, which allows you to reference method parameters, return values, and Spring Security's built-in objects like `authentication` and `principal`.

Here's an example:

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    // Enables method-level security
}

@Service
public class UserService {
    
    // Only users with ADMIN role can access this method
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // Only the user themselves or an admin can access their own data
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    // Check permission after method execution based on return value
    @PostAuthorize("returnObject.owner == authentication.principal.username")
    public Document getDocument(Long id) {
        return documentRepository.findById(id).orElse(null);
    }
    
    // Complex expression checking multiple conditions
    @PreAuthorize("hasRole('USER') and #user.email != null")
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
```

---

## 📌 **INTERVIEW QUESTION 15: What is @EventListener and how does it work with Spring's event mechanism?**

**Answer:**

`@EventListener` is a Spring annotation that allows you to handle application events in a decoupled way. Spring's event mechanism follows the observer pattern, where components can publish events and other components can listen to and react to those events without tight coupling.

When you annotate a method with `@EventListener`, Spring automatically registers it as an event listener. When an event of the specified type is published, Spring invokes your listener method. This is useful for implementing cross-cutting concerns, integrating with external systems, or handling side effects that shouldn't be part of the main business logic.

You can listen to Spring's built-in events, like `ContextRefreshedEvent` which fires when the application context is refreshed, or you can create custom events by extending `ApplicationEvent`. You can also listen to multiple event types by specifying them in the annotation.

The event mechanism is synchronous by default, meaning the publisher waits for all listeners to complete. However, you can make it asynchronous by using `@Async` on the listener method, which allows the publisher to continue immediately while listeners process the event in separate threads.

Here's an example:

```java
// Custom event
public class UserCreatedEvent extends ApplicationEvent {
    private final User user;
    
    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
}

@Service
public class UserService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public User createUser(User user) {
        User saved = userRepository.save(user);
        
        // Publish event - all listeners will be notified
        eventPublisher.publishEvent(new UserCreatedEvent(this, saved));
        
        return saved;
    }
}

// Event listener
@Component
public class UserEventListener {
    
    // Listen to custom event
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        User user = event.getUser();
        System.out.println("User created: " + user.getEmail());
        // Send welcome email, create audit log, etc.
    }
    
    // Listen to multiple event types
    @EventListener({UserCreatedEvent.class, UserUpdatedEvent.class})
    public void handleUserEvents(ApplicationEvent event) {
        System.out.println("User event occurred: " + event.getClass().getSimpleName());
    }
    
    // Async event listener
    @Async
    @EventListener
    public void sendWelcomeEmail(UserCreatedEvent event) {
        // This runs in a separate thread
        emailService.sendWelcomeEmail(event.getUser().getEmail());
    }
    
    // Listen to Spring's built-in events
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        System.out.println("Application context refreshed");
    }
}

// Conditional event listener
@Component
public class ConditionalEventListener {
    
    // Only handle event if condition is met
    @EventListener(condition = "#event.user.email.endsWith('@company.com')")
    public void handleCompanyUser(UserCreatedEvent event) {
        System.out.println("Company user created: " + event.getUser().getEmail());
    }
}
```

---

## 📌 **INTERVIEW QUESTION 16: What is @RequestHeader and @CookieValue and when would you use them?**

**Answer:**

`@RequestHeader` and `@CookieValue` are Spring annotations used to extract values from HTTP request headers and cookies, respectively. They're useful when you need to access metadata about the request that isn't part of the request body or URL path.

`@RequestHeader` allows you to extract a specific header value from the HTTP request. You can specify the header name, and optionally provide a default value if the header is missing. This is commonly used for authentication tokens, API keys, content negotiation, or any custom headers your application needs.

`@CookieValue` allows you to extract a specific cookie value from the HTTP request. Like `@RequestHeader`, you can specify the cookie name and provide a default value. This is useful for session management, tracking, or storing user preferences.

Both annotations support optional parameters, meaning you can mark them as not required if the header or cookie might not always be present. If a required header or cookie is missing, Spring will throw an exception.

Here's an example:

```java
@RestController
public class ApiController {
    
    // Extract Authorization header
    @GetMapping("/api/data")
    public ResponseEntity<Data> getData(
            @RequestHeader("Authorization") String authToken) {
        // Validate token and return data
        return ResponseEntity.ok(dataService.getData(authToken));
    }
    
    // Extract header with default value
    @GetMapping("/api/content")
    public ResponseEntity<String> getContent(
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String language) {
        // Use language for content negotiation
        return ResponseEntity.ok(contentService.getContent(language));
    }
    
    // Extract multiple headers
    @PostMapping("/api/upload")
    public ResponseEntity<String> uploadFile(
            @RequestHeader("Content-Type") String contentType,
            @RequestHeader("Content-Length") Long contentLength,
            @RequestBody byte[] fileData) {
        // Process file with metadata from headers
        return ResponseEntity.ok("File uploaded");
    }
    
    // Extract optional header
    @GetMapping("/api/user")
    public ResponseEntity<User> getUser(
            @RequestHeader(value = "X-Request-ID", required = false) String requestId) {
        // Request ID is optional - won't fail if missing
        return ResponseEntity.ok(userService.getCurrentUser());
    }
    
    // Extract cookie value
    @GetMapping("/api/profile")
    public ResponseEntity<Profile> getProfile(
            @CookieValue("sessionId") String sessionId) {
        // Validate session and return profile
        return ResponseEntity.ok(profileService.getProfile(sessionId));
    }
    
    // Extract cookie with default value
    @GetMapping("/api/preferences")
    public ResponseEntity<Preferences> getPreferences(
            @CookieValue(value = "theme", defaultValue = "light") String theme) {
        // Use theme preference
        return ResponseEntity.ok(preferencesService.getPreferences(theme));
    }
    
    // Extract all headers as a map
    @GetMapping("/api/debug")
    public ResponseEntity<Map<String, String>> getAllHeaders(
            @RequestHeader Map<String, String> headers) {
        // Access all headers as a map
        return ResponseEntity.ok(headers);
    }
}
```

---

## 📌 **INTERVIEW QUESTION 17: What is @PathVariable and @RequestParam and what's the difference between them?**

**Answer:**

`@PathVariable` and `@RequestParam` are both Spring annotations used to extract values from HTTP requests, but they work with different parts of the URL.

`@PathVariable` extracts values from the URL path itself. These are part of the route definition, like `/users/{id}` where `{id}` is a path variable. Path variables are required by default - if they're missing, the route won't match. They're typically used for resource identifiers, like user IDs, product IDs, or other entities that are part of the resource path.

`@RequestParam` extracts values from query parameters, which come after the question mark in the URL, like `/users?page=1&size=10`. Query parameters are optional by default unless you set `required = true`. They're typically used for filtering, pagination, sorting, or optional configuration.

The key difference is that path variables are part of the resource path and are required for routing, while query parameters are optional modifiers to the request. Path variables identify which resource you're working with, while query parameters specify how you want to work with it.

Here's an example:

```java
@RestController
public class UserController {
    
    // Path variable - part of URL path
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        // URL: /users/123
        // id = 123
        return ResponseEntity.ok(userService.findById(id));
    }
    
    // Multiple path variables
    @GetMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        // URL: /users/123/orders/456
        return ResponseEntity.ok(orderService.findOrder(userId, orderId));
    }
    
    // Path variable with custom name
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(
            @PathVariable("productId") Long id) {
        // Variable name in method can differ from path variable name
        return ResponseEntity.ok(productService.findById(id));
    }
    
    // Request param - query parameter
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        // URL: /users?page=1&size=20
        // page = 1, size = 20
        // If params missing, uses defaults: page=0, size=10
        return ResponseEntity.ok(userService.findAll(page, size));
    }
    
    // Multiple request params
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer age) {
        // URL: /search?name=John&email=john@example.com&age=30
        // name is required, email and age are optional
        return ResponseEntity.ok(userService.search(name, email, age));
    }
    
    // Request param with default value
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        // URL: /products?sortBy=price&order=desc
        // If params missing, uses defaults
        return ResponseEntity.ok(productService.findAll(sortBy, order));
    }
    
    // All query params as a map
    @GetMapping("/filter")
    public ResponseEntity<List<User>> filterUsers(
            @RequestParam Map<String, String> filters) {
        // URL: /filter?name=John&city=NYC&age=30
        // Gets all query params as a map
        return ResponseEntity.ok(userService.filter(filters));
    }
}
```

---

## 📌 **INTERVIEW QUESTION 18: How can you optimize database queries in Spring Boot applications?**

**Answer:**

Optimizing database queries is crucial for application performance, and there are several strategies you can use in Spring Boot applications.

First, use lazy loading for JPA relationships. By default, use `FetchType.LAZY` instead of `FetchType.EAGER`. Eager loading can cause the N+1 query problem, where loading one entity triggers multiple additional queries. With lazy loading, related entities are only loaded when accessed, reducing unnecessary queries.

Second, use `@EntityGraph` or `JOIN FETCH` in queries when you know you'll need related data. This allows you to fetch related entities in a single query instead of multiple queries. For example, if you're loading an order and you know you'll need the order items, use a join fetch to get everything in one query.

Third, implement pagination for large datasets. Instead of loading all records, use `Pageable` and `Page` to load data in chunks. This reduces memory usage and improves response times. Spring Data JPA makes pagination easy with repository methods that accept `Pageable`.

Fourth, use projection interfaces or DTOs to fetch only the fields you need. Instead of loading entire entities with all their relationships, create projection interfaces that specify exactly which fields to fetch. This reduces the amount of data transferred and processed.

Fifth, create appropriate database indexes for frequently queried columns. Indexes significantly speed up queries, especially for WHERE clauses and JOIN operations. You can create indexes using `@Index` on entity classes or by defining them in your database schema.

Sixth, use batch processing for bulk operations. Instead of saving entities one by one, use batch inserts and updates. Configure JPA batch size with `spring.jpa.properties.hibernate.jdbc.batch_size` to enable batch processing.

Seventh, avoid the N+1 query problem by using `@Query` with proper joins. Write custom queries that fetch all needed data in a single query instead of letting Hibernate generate multiple queries.

Eighth, use second-level caching for frequently accessed, rarely changed data. Configure Hibernate's second-level cache to cache entities, reducing database hits for the same data.

Here's an example:

```java
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    
    // Use LAZY loading by default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> items;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
}

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Use EntityGraph to fetch related data in one query
    @EntityGraph(attributePaths = {"items", "customer"})
    Optional<Order> findById(Long id);
    
    // Use JOIN FETCH in custom query
    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);
    
    // Pagination - only loads requested page
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    
    // Projection - only fetch specific fields
    @Query("SELECT o.id as id, o.totalAmount as totalAmount FROM Order o WHERE o.customer.id = :customerId")
    List<OrderSummary> findOrderSummariesByCustomerId(@Param("customerId") Long customerId);
    
    // Batch query - fetch multiple orders with items in one query
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id IN :ids")
    List<Order> findByIdsWithItems(@Param("ids") List<Long> ids);
}

// Projection interface - only fetch needed fields
public interface OrderSummary {
    Long getId();
    BigDecimal getTotalAmount();
}

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    // Optimized: Uses EntityGraph to avoid N+1 queries
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }
    
    // Optimized: Pagination reduces memory usage
    public Page<Order> getCustomerOrders(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return orderRepository.findByCustomerId(customerId, pageable);
    }
    
    // Optimized: Projection reduces data transfer
    public List<OrderSummary> getOrderSummaries(Long customerId) {
        return orderRepository.findOrderSummariesByCustomerId(customerId);
    }
}
```

In your application.properties, configure batch processing:

```properties
# Enable batch processing
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Enable second-level cache
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
```

---

## 📌 **INTERVIEW QUESTION 19: What is @EnableJpaRepositories and how do you configure custom repository base classes?**

**Answer:**

`@EnableJpaRepositories` is a Spring Data JPA annotation that enables JPA repository support in your application. It tells Spring to scan for repository interfaces and automatically create implementations for them. While Spring Boot auto-configures this for you, you might need to use it explicitly when you want to customize the repository configuration.

When you use `@EnableJpaRepositories`, you can specify several configuration options. The `basePackages` attribute tells Spring which packages to scan for repository interfaces. The `basePackageClasses` attribute allows you to specify marker classes in the packages you want to scan, which is type-safe and refactoring-friendly.

You can also configure a custom repository base class using the `repositoryBaseClass` attribute. This is useful when you want to add custom methods to all your repositories without having to implement them in each repository interface. You create a base repository class that extends `SimpleJpaRepository` and add your custom methods there.

Another useful feature is the `entityManagerFactoryRef` and `transactionManagerRef` attributes, which allow you to specify which EntityManagerFactory and TransactionManager to use when you have multiple data sources.

Here's an example:

```java
@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.repository",
    basePackageClasses = UserRepository.class,
    repositoryBaseClass = CustomRepositoryImpl.class,
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
public class JpaConfiguration {
    // Custom JPA repository configuration
}

// Custom repository base class
public class CustomRepositoryImpl<T, ID extends Serializable> 
        extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID> {
    
    private final EntityManager entityManager;
    
    public CustomRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, 
                                EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }
    
    // Custom method available to all repositories
    @Override
    public List<T> findAllActive() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getDomainClass());
        Root<T> root = query.from(getDomainClass());
        
        // Add custom logic - e.g., filter by active status
        query.select(root).where(cb.equal(root.get("active"), true));
        
        return entityManager.createQuery(query).getResultList();
    }
    
    // Another custom method
    @Override
    public void softDelete(ID id) {
        T entity = findById(id).orElseThrow();
        // Custom soft delete logic
        entityManager.remove(entity);
    }
}

// Custom repository interface
public interface CustomRepository<T, ID extends Serializable> {
    List<T> findAllActive();
    void softDelete(ID id);
}

// Your repository extends both JpaRepository and CustomRepository
public interface UserRepository extends JpaRepository<User, Long>, CustomRepository<User, Long> {
    // Standard JPA methods + custom methods from CustomRepository
    List<User> findByEmail(String email);
}

// Usage
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getActiveUsers() {
        // Uses custom method from CustomRepository
        return userRepository.findAllActive();
    }
    
    public void deactivateUser(Long id) {
        // Uses custom soft delete method
        userRepository.softDelete(id);
    }
}
```

---

## 📌 **INTERVIEW QUESTION 20: What is @EnableWebMvc and when would you use it versus Spring Boot's auto-configuration?**

**Answer:**

`@EnableWebMvc` is a Spring annotation that enables Spring MVC configuration. However, in Spring Boot applications, you typically don't need to use it because Spring Boot provides auto-configuration for Spring MVC. Understanding when to use it versus relying on auto-configuration is important.

When you use `@EnableWebMvc` in a Spring Boot application, you're essentially telling Spring Boot to disable its auto-configuration for Spring MVC and use your custom configuration instead. This gives you full control over the MVC configuration, but you lose the convenience of Spring Boot's sensible defaults.

Spring Boot's auto-configuration automatically configures things like view resolvers, message converters, exception handlers, and other MVC components with sensible defaults. If you're happy with these defaults, you don't need `@EnableWebMvc`.

You would use `@EnableWebMvc` when you need complete control over the MVC configuration, such as when you want to customize view resolvers, add custom interceptors, configure CORS differently, or when you're migrating a traditional Spring MVC application to Spring Boot and want to preserve existing configuration.

Instead of using `@EnableWebMvc`, Spring Boot recommends using `WebMvcConfigurer` interface. You can create a configuration class that implements `WebMvcConfigurer` and override specific methods to customize the configuration while still benefiting from Spring Boot's auto-configuration.

Here's an example:

```java
// DON'T use @EnableWebMvc in Spring Boot (disables auto-configuration)
// @Configuration
// @EnableWebMvc  // This disables Spring Boot's MVC auto-configuration
// public class WebMvcConfig {
// }

// DO use WebMvcConfigurer to customize while keeping auto-configuration
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    // Add custom interceptors
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
        registry.addInterceptor(new SecurityInterceptor());
    }
    
    // Configure CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
    
    // Add custom argument resolvers
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CustomArgumentResolver());
    }
    
    // Configure view resolvers (if using views)
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }
    
    // Add formatters
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
    }
    
    // Configure message converters
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new CustomMessageConverter());
    }
    
    // Extend existing message converters
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Modify existing converters without replacing them
    }
}

// Custom interceptor
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
                           Object handler) {
        System.out.println("Request: " + request.getRequestURI());
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) {
        System.out.println("Response status: " + response.getStatus());
    }
}
```

The key takeaway is that in Spring Boot, you should use `WebMvcConfigurer` to customize MVC configuration rather than `@EnableWebMvc`, unless you have a specific need to completely override the auto-configuration.

---

## 📌 **INTERVIEW QUESTION 21: What is @ConditionalOnClass and @ConditionalOnMissingBean and how do they control auto-configuration?**

**Answer:**

`@ConditionalOnClass` and `@ConditionalOnMissingBean` are Spring Boot conditional annotations that control when auto-configuration classes are applied. They're the building blocks of Spring Boot's auto-configuration mechanism, allowing Spring Boot to automatically configure beans only when certain conditions are met.

`@ConditionalOnClass` checks if specific classes are present on the classpath. If the specified classes are found, the configuration is applied. This is how Spring Boot knows to configure things like JPA when Hibernate is on the classpath, or to configure MongoDB when the MongoDB driver is available. If the classes aren't present, the configuration is skipped.

`@ConditionalOnMissingBean` checks if a bean of a certain type already exists in the application context. If no such bean exists, the configuration creates one. If a bean already exists, the auto-configuration is skipped. This allows you to override Spring Boot's auto-configuration by providing your own bean definition.

These annotations work together to provide intelligent auto-configuration. Spring Boot checks if required classes are available, and if you haven't already provided your own configuration, it provides sensible defaults. This is why Spring Boot applications "just work" with minimal configuration - the framework automatically configures what you need based on what's on your classpath.

Here's an example:

```java
@Configuration
@ConditionalOnClass(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class CustomJpaConfiguration {
    
    // This bean is only created if DataSource class is on classpath
    // AND if no EntityManagerFactory bean already exists
    @Bean
    @ConditionalOnMissingBean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.entity");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        return em;
    }
    
    // Only create if TransactionManager doesn't exist
    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

// Example: Conditional configuration based on class presence
@Configuration
@ConditionalOnClass(name = "com.redis.clients.jedis.Jedis")
public class RedisAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // Only configured if Jedis is on classpath
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }
}
```

---

## 📌 **INTERVIEW QUESTION 22: What is connection pooling and how do you configure it for optimal performance in Spring Boot?**

**Answer:**

Connection pooling is a technique where a pool of database connections is maintained and reused across multiple requests, rather than creating a new connection for each database operation. This significantly improves performance because establishing a database connection is expensive - it involves network overhead, authentication, and resource allocation.

Spring Boot uses HikariCP as the default connection pool, which is one of the fastest and most efficient connection pool implementations available. HikariCP is automatically configured when you include a database driver in your dependencies.

To optimize connection pooling, you need to configure several properties. The `maximum-pool-size` determines the maximum number of connections in the pool. This should be set based on your application's concurrency needs - too low and you'll have connection wait times, too high and you'll waste resources. A common formula is to set it to the number of CPU cores multiplied by two, plus the number of disk spindles.

The `minimum-idle` property sets the minimum number of idle connections maintained in the pool. Having some idle connections ready reduces connection acquisition time. The `connection-timeout` sets how long to wait for a connection from the pool before timing out.

The `idle-timeout` determines how long an idle connection can remain in the pool before being removed. The `max-lifetime` sets the maximum lifetime of a connection in the pool, after which it's retired and replaced.

You should also configure `leak-detection-threshold` to detect connection leaks, which helps identify when connections aren't being properly returned to the pool.

Here's an example configuration:

```java
@Configuration
public class DataSourceConfiguration {
    
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }
}
```

In your application.properties:

```properties
# HikariCP Connection Pool Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.leak-detection-threshold=60000

# Connection pool name for monitoring
spring.datasource.hikari.pool-name=MyAppHikariPool

# Connection test query
spring.datasource.hikari.connection-test-query=SELECT 1

# Auto-commit setting
spring.datasource.hikari.auto-commit=false
```

For high-performance applications, you might want to tune these values based on your specific workload. Monitor connection pool metrics using Spring Boot Actuator to see connection usage patterns and adjust accordingly.

---

## 📌 **INTERVIEW QUESTION 23: What is @EnableScheduling and how do you configure a custom TaskScheduler?**

**Answer:**

`@EnableScheduling` is a Spring annotation that enables support for scheduled tasks using the `@Scheduled` annotation. When you add `@EnableScheduling` to a configuration class, Spring creates a default `TaskScheduler` that uses a single thread to execute all scheduled tasks.

However, for better performance and control, you might want to configure a custom `TaskScheduler` with a thread pool. This allows multiple scheduled tasks to run concurrently instead of being queued on a single thread.

You configure a custom `TaskScheduler` by creating a bean that implements the `SchedulingConfigurer` interface or by defining a `TaskScheduler` bean. You can specify the thread pool size, thread name prefix, and other properties.

Here's an example:

```java
@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }
    
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        return scheduler;
    }
}

// Alternative approach using @Bean
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("scheduled-");
        scheduler.initialize();
        return scheduler;
    }
}

@Component
public class ScheduledTasks {
    
    // These tasks can now run concurrently thanks to thread pool
    @Scheduled(fixedRate = 5000)
    public void task1() {
        System.out.println("Task 1 running on: " + Thread.currentThread().getName());
    }
    
    @Scheduled(fixedRate = 3000)
    public void task2() {
        System.out.println("Task 2 running on: " + Thread.currentThread().getName());
    }
    
    @Scheduled(cron = "0 0 * * * ?")
    public void hourlyTask() {
        System.out.println("Hourly task running on: " + Thread.currentThread().getName());
    }
}
```

---

## 📌 **INTERVIEW QUESTION 24: What is @EnableCaching and how do you configure Redis as a cache provider?**

**Answer:**

`@EnableCaching` is a Spring annotation that enables Spring's caching abstraction. It allows you to use caching annotations like `@Cacheable`, `@CachePut`, and `@CacheEvict` in your application. However, `@EnableCaching` alone doesn't provide a cache implementation - you need to configure a cache manager.

Spring Boot can auto-configure various cache providers, including Redis, Caffeine, EhCache, and simple in-memory caches. Redis is particularly useful for distributed caching in microservices architectures, where multiple application instances can share the same cache.

To use Redis as a cache provider, you need to add the Spring Boot Redis starter dependency and configure Redis connection properties. Spring Boot will automatically configure a `RedisCacheManager` if Redis is available.

Here's an example:

```java
@Configuration
@EnableCaching
public class CacheConfiguration {
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setHostName("localhost");
        factory.setPort(6379);
        factory.setPassword("password");
        return factory;
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();
        
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("users", 
                    config.entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration("products", 
                    config.entryTtl(Duration.ofHours(1)))
                .transactionAware()
                .build();
    }
}
```

In your application.properties:

```properties
# Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=yourpassword
spring.redis.timeout=2000ms

# Cache Configuration
spring.cache.type=redis
spring.cache.redis.time-to-live=600000
spring.cache.redis.cache-null-values=false
```

Usage in your service:

```java
@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        // Result cached in Redis for 5 minutes
        return userRepository.findById(id).orElse(null);
    }
    
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        // Updates both database and Redis cache
        return userRepository.save(user);
    }
    
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        // Removes from both database and Redis cache
        userRepository.deleteById(id);
    }
}
```

---

## 📌 **INTERVIEW QUESTION 25: What is the difference between @ComponentScan and @SpringBootApplication's component scanning?**

**Answer:**

`@ComponentScan` is a Spring annotation that tells Spring which packages to scan for components like `@Component`, `@Service`, `@Repository`, and `@Controller`. `@SpringBootApplication` is a convenience annotation that includes `@ComponentScan` along with `@Configuration` and `@EnableAutoConfiguration`.

When you use `@SpringBootApplication`, it automatically enables component scanning starting from the package of the class where it's applied. This means if your main class is in `com.example.application`, Spring will scan `com.example.application` and all its sub-packages for components.

If you need to scan packages outside of your main package, or if you want more control over component scanning, you can use `@ComponentScan` explicitly. You can specify `basePackages` to list the exact packages to scan, or `basePackageClasses` to use marker classes in the packages you want to scan.

You can also use both together - `@SpringBootApplication` for the default scanning, and `@ComponentScan` to add additional packages. However, if you use `@ComponentScan` explicitly, it overrides the default scanning behavior from `@SpringBootApplication`.

Here's an example:

```java
// Main application class
@SpringBootApplication
// By default, scans com.example.application and sub-packages
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// If you need to scan additional packages
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.application", "com.example.shared"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// Using basePackageClasses for type-safe scanning
@SpringBootApplication
@ComponentScan(basePackageClasses = {
    Application.class,  // Scans package containing Application
    SharedComponent.class  // Scans package containing SharedComponent
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// Excluding specific components from scanning
@SpringBootApplication
@ComponentScan(
    basePackages = "com.example",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com.example.excluded.*"
    )
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

The key difference is that `@SpringBootApplication` provides sensible defaults for component scanning based on your main class location, while `@ComponentScan` gives you explicit control over which packages are scanned.

---

This README2 covers additional important Spring Boot interview questions focusing on performance optimization, annotations, and advanced topics. Each answer is written in a natural, conversational style that you can speak confidently in front of anyone.
# Spring Bean Interview Guide

## 🎯 Quick Reference for Interviews

### 1. What is a Spring Bean?
**Answer:** A Spring Bean is an object that is instantiated, assembled, and managed by the Spring IoC (Inversion of Control) container. Beans are the backbone of a Spring application.

**Key Points:**
- Object managed by Spring container
- Created, configured, and wired by Spring
- Stored in ApplicationContext (IoC container)

---

### 2. How are Beans Created?

#### Method 1: Using @Component (and stereotypes)
```java
@Component
public class UserService {
    // Spring automatically creates bean
}
```

**Stereotypes:**
- `@Component` - Generic component
- `@Service` - Business logic layer
- `@Repository` - Data access layer
- `@Controller` - Web layer (MVC)
- `@RestController` - REST API layer

#### Method 2: Using @Bean in @Configuration
```java
@Configuration
public class AppConfig {
    @Bean
    public DatabaseConnection databaseConnection() {
        return new DatabaseConnection();
    }
}
```

**When to use @Bean:**
- Third-party classes (can't add @Component)
- Complex initialization logic
- Conditional bean creation
- Multiple beans of same type

---

### 3. Bean Scopes (Types)

| Scope | Description | Use Case |
|-------|-------------|----------|
| **Singleton** (Default) | One instance per container | Stateless services, utilities |
| **Prototype** | New instance every time | Stateful beans, user sessions |
| **Request** | One per HTTP request | Web applications |
| **Session** | One per HTTP session | User-specific data |
| **Application** | One per ServletContext | Web applications |

**Example:**
```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SingletonBean { }

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeBean { }
```

---

### 4. Bean Lifecycle

**Creation Steps:**
1. **Instantiation** - Constructor called
2. **Populate Properties** - Dependencies injected
3. **BeanNameAware** - Bean name set
4. **BeanFactoryAware** - BeanFactory provided
5. **ApplicationContextAware** - ApplicationContext provided
6. **@PostConstruct** - Initialization callback
7. **InitializingBean.afterPropertiesSet()** - After properties set
8. **Custom init method** - If specified
9. **Bean Ready** - Can be used

**Destruction Steps:**
10. **@PreDestroy** - Cleanup callback
11. **DisposableBean.destroy()** - Cleanup
12. **Custom destroy method** - If specified

**Common Approach:**
```java
@Component
public class MyBean {
    @PostConstruct
    public void init() {
        // Initialize resources
    }
    
    @PreDestroy
    public void cleanup() {
        // Release resources
    }
}
```

---

### 5. Dependency Injection Types

#### 1. Constructor Injection (Recommended)
```java
@Component
public class UserController {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

**Benefits:**
- Immutable dependencies (final)
- Required at construction
- Easy to test
- No @Autowired needed (Spring 4.3+)

#### 2. Setter Injection
```java
@Component
public class OrderController {
    private UserService userService;
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

**Use when:**
- Optional dependencies
- Dependencies might change
- Circular dependencies

#### 3. Field Injection (Not Recommended)
```java
@Component
public class ProductController {
    @Autowired
    private UserService userService;
}
```

**Problems:**
- Hard to test
- Dependencies not visible
- Can't use final

---

### 6. Common Interview Questions

#### Q: What is the default scope of a Spring Bean?
**A:** Singleton - one instance per Spring container.

#### Q: What is the difference between @Component and @Bean?
**A:** 
- @Component: Used on classes, Spring auto-discovers
- @Bean: Used in @Configuration, manual bean definition

#### Q: When would you use Prototype scope?
**A:** When each request needs a fresh instance, like stateful beans or user sessions.

#### Q: What is ApplicationContext?
**A:** The Spring IoC container that manages beans - creates, stores, and wires them together.

#### Q: What is @Autowired?
**A:** Annotation that tells Spring to automatically inject dependencies.

#### Q: What is the difference between BeanFactory and ApplicationContext?
**A:** 
- BeanFactory: Basic container, lazy loading
- ApplicationContext: Advanced container, eager loading, more features

---

### 7. Bean Creation Process (Step by Step)

1. **Component Scanning**
   - Spring scans packages for @Component, @Service, etc.
   - Identifies classes to create as beans

2. **Bean Definition**
   - Spring creates BeanDefinition for each bean
   - Stores metadata (scope, dependencies, etc.)

3. **Instantiation**
   - Spring calls constructor
   - Creates object instance

4. **Dependency Injection**
   - Spring injects dependencies
   - Resolves bean references

5. **Initialization**
   - @PostConstruct called
   - Bean is ready to use

6. **Storage**
   - Bean stored in ApplicationContext
   - Available for injection into other beans

---

### 8. Code Examples Location

- **Basic Bean Creation:** `com.harshit.springbeans.basic.*`
- **Bean Scopes:** `com.harshit.springbeans.scopes.*`
- **Bean Lifecycle:** `com.harshit.springbeans.lifecycle.*`
- **Dependency Injection:** `com.harshit.springbeans.dependencyinjection.*`
- **Configuration:** `com.harshit.springbeans.configuration.*`
- **Demo Application:** `com.harshit.springbeans.demo.BeanDemoApplication`

---

### 9. Run the Demo

Run `BeanDemoApplication.main()` to see all concepts in action!

```bash
mvn compile exec:java -Dexec.mainClass="com.harshit.springbeans.demo.BeanDemoApplication"
```

---

## 📝 Key Takeaways for Interviews

1. **Bean = Object managed by Spring**
2. **Default scope = Singleton**
3. **Constructor injection = Best practice**
4. **@PostConstruct = Initialization**
5. **@PreDestroy = Cleanup**
6. **ApplicationContext = Bean container**
7. **@Component = Auto-discovery**
8. **@Bean = Manual definition**

Good luck with your interview! 🚀



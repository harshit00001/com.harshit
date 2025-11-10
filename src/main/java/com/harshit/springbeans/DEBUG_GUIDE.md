# Debugging Spring Bean Creation - Step by Step Guide

## 🎯 How to Debug Bean Creation

### Method 1: Using IDE Debugger (Recommended)

#### **Step 1: Set Breakpoints**

Set breakpoints at these key locations to see bean creation:

**1. Bean Constructors (See instantiation)**
- `UserService.java` - Line 46 (constructor)
- `ProductService.java` - Line 20 (constructor)
- `OrderRepository.java` - Line 20 (constructor)
- `LifecycleBean.java` - Line 60 (constructor)

**2. Dependency Injection Points**
- `UserController.java` - Line 83 (constructor - Constructor Injection)
- `OrderController.java` - Line 129 (setter - Setter Injection)
- `ProductController.java` - Line 180 (constructor - Field Injection happens after)

**3. Lifecycle Callbacks**
- `LifecycleBean.java`:
  - Line 95: `setBeanName()` - Bean name aware
  - Line 130: `postConstruct()` - @PostConstruct
  - Line 144: `afterPropertiesSet()` - InitializingBean
  - Line 178: `preDestroy()` - @PreDestroy

**4. Bean Factory Methods**
- `AppConfig.java`:
  - Line 40: `databaseConnection()` - @Bean method
  - Line 60: `orderService()` - @Bean with dependency injection

**5. Bean Retrieval**
- `BeanDemoApplication.java`:
  - Line 58: `new AnnotationConfigApplicationContext()` - Container creation
  - Line 72: `context.getBean(UserService.class)` - Bean retrieval

#### **Step 2: Start Debugging**

1. **In IntelliJ IDEA:**
   - Right-click on `BeanDemoApplication.java`
   - Select "Debug 'BeanDemoApplication.main()'"
   - Or press `Shift + F9`

2. **In Eclipse/VS Code:**
   - Right-click on `BeanDemoApplication.java`
   - Select "Debug As" → "Java Application"
   - Or press `F11`

3. **In Cursor:**
   - Open `BeanDemoApplication.java`
   - Click the debug icon in the gutter next to `main()` method
   - Or press `F5` to start debugging

#### **Step 3: Step Through the Code**

Use these debug controls:
- **F8** (Step Over) - Execute current line
- **F7** (Step Into) - Go into method calls
- **Shift+F8** (Step Out) - Exit current method
- **F9** (Resume) - Continue to next breakpoint

---

### Method 2: Using Maven Debug

#### **Step 1: Run with Debug Port**

```bash
mvn clean compile exec:java -Dexec.mainClass="com.harshit.springbeans.demo.BeanDemoApplication" -Dmaven.surefire.debug="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

#### **Step 2: Attach Debugger**

1. In your IDE, create a "Remote Debug" configuration
2. Set host: `localhost`, port: `5005`
3. Start debugging

---

### Method 3: Add Debug Logging

Add `System.out.println()` statements at key points:

```java
// In UserService constructor
public UserService() {
    System.out.println("[DEBUG] UserService constructor - Thread: " + Thread.currentThread().getName());
    // ... rest of code
}
```

---

## 🔍 Key Debugging Scenarios

### Scenario 1: Watch Bean Creation Order

**Breakpoints:**
- All constructor methods

**What to observe:**
1. Order of bean creation
2. Dependencies are created before dependent beans
3. Singleton beans created only once
4. Prototype beans created each time they're requested

### Scenario 2: Watch Dependency Injection

**Breakpoints:**
- `UserController` constructor (Constructor Injection)
- `OrderController.setEmailService()` (Setter Injection)
- `ProductController` constructor + field access (Field Injection)

**What to observe:**
1. Constructor injection happens during construction
2. Setter injection happens after construction
3. Field injection happens after construction via reflection

### Scenario 3: Watch Bean Lifecycle

**Breakpoints:**
- `LifecycleBean` constructor
- `setBeanName()`
- `postConstruct()`
- `afterPropertiesSet()`
- `preDestroy()`

**What to observe:**
1. Lifecycle order: Constructor → BeanName → BeanFactory → ApplicationContext → @PostConstruct → afterPropertiesSet
2. @PreDestroy is called when context closes

### Scenario 4: Watch Bean Scopes

**Breakpoints:**
- `SingletonBean` constructor
- `PrototypeBean` constructor

**What to observe:**
1. Singleton: Constructor called ONCE, same instance returned
2. Prototype: Constructor called MULTIPLE times, different instances

---

## 📊 Debug Variables to Watch

### In ApplicationContext Creation:

```java
ApplicationContext context = new AnnotationConfigApplicationContext(...);
```

Watch these:
- `context` - The Spring container
- Check `context.getBeanDefinitionNames()` - See all registered beans

### In Bean Retrieval:

```java
UserService userService = context.getBean(UserService.class);
```

Watch these:
- `userService` - The bean instance
- Check `userService == context.getBean(UserService.class)` - Verify singleton

### In Dependency Injection:

```java
public UserController(EmailService emailService, ...)
```

Watch these:
- `emailService` - Injected dependency
- Check `this.emailService` - Field assignment

---

## 🎯 Recommended Debugging Flow

1. **Start with ApplicationContext creation** (Line 58 in BeanDemoApplication)
   - Step into `AnnotationConfigApplicationContext`
   - See how Spring scans for components

2. **Watch first bean creation** (UserService constructor)
   - Step through constructor
   - See bean instantiation

3. **Watch dependency injection** (UserController constructor)
   - Step into constructor
   - See how Spring passes dependencies

4. **Watch lifecycle callbacks** (LifecycleBean)
   - Step through each lifecycle method
   - Understand the order

5. **Watch bean retrieval** (getBean calls)
   - See singleton vs prototype behavior
   - Check instance equality

6. **Watch context shutdown** (context.close())
   - See @PreDestroy being called
   - Understand cleanup

---

## 💡 Debug Tips

1. **Use "Evaluate Expression"** in debugger:
   ```java
   context.getBeanDefinitionNames()
   context.getBean(UserService.class)
   ```

2. **Watch Variables:**
   - Add all bean variables to watch list
   - Compare instance equality

3. **Conditional Breakpoints:**
   - Set breakpoint only when bean name equals "userService"
   - Right-click breakpoint → Add condition

4. **Log Points:**
   - Instead of breakpoint, use log point
   - Log message: "Bean created: {beanName}"

---

## 🚀 Quick Start Debug Session

1. Open `BeanDemoApplication.java`
2. Set breakpoint at line 58: `ApplicationContext context = ...`
3. Set breakpoint at line 72: `UserService userService = ...`
4. Set breakpoint at line 106: `SingletonBean singleton1 = ...`
5. Start debugging (F5 or Debug button)
6. Step through each breakpoint
7. Observe the console output between breakpoints

---

## 📝 Common Debugging Questions

**Q: Why is my bean null?**
- Check if bean is properly annotated (@Component, @Service, etc.)
- Check if component scanning is enabled
- Check if bean is in the correct package

**Q: Why is dependency injection not working?**
- Check if dependency is also a bean
- Check constructor/setter signature
- Check if @Autowired is present (for setter/field injection)

**Q: Why is singleton not working?**
- Check @Scope annotation
- Verify same ApplicationContext instance
- Check if prototype scope is set

---

Happy Debugging! 🐛🔍






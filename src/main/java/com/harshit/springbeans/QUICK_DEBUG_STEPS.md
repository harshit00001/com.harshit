# Quick Debug Steps - Visual Guide

## 🎯 Step-by-Step Debugging Instructions

### **Step 1: Open BeanDemoApplication.java**

The file you have open: `BeanDemoApplication.java`

### **Step 2: Set These Breakpoints (Click on line numbers)**

#### **Breakpoint 1: Line 58** - ApplicationContext Creation
```java
ApplicationContext context = new AnnotationConfigApplicationContext(BeanDemoApplication.class);
```
**Why:** This is where Spring starts creating all beans. Step into this to see bean scanning.

#### **Breakpoint 2: Line 72** - Bean Retrieval
```java
UserService userService = context.getBean(UserService.class);
```
**Why:** See how Spring retrieves a bean from the container.

#### **Breakpoint 3: Line 106** - Singleton Bean Test
```java
SingletonBean singleton1 = context.getBean(SingletonBean.class);
```
**Why:** See singleton behavior - same instance returned.

#### **Breakpoint 4: Line 121** - Prototype Bean Test
```java
PrototypeBean prototype1 = context.getBean(PrototypeBean.class);
```
**Why:** See prototype behavior - new instance created.

#### **Breakpoint 5: Line 139** - Dependency Injection
```java
UserController userController = context.getBean(UserController.class);
```
**Why:** See how dependencies are injected.

---

### **Step 3: Start Debugging**

#### **In Cursor/VS Code:**
1. Press `F5` OR
2. Click the "Run and Debug" icon in the left sidebar
3. Click the green play button next to "Java"
4. Select "Debug Java"

#### **In IntelliJ IDEA:**
1. Right-click on `BeanDemoApplication.java`
2. Select "Debug 'BeanDemoApplication.main()'"
3. OR press `Shift + F9`

#### **In Eclipse:**
1. Right-click on `BeanDemoApplication.java`
2. Select "Debug As" → "Java Application"
3. OR press `F11`

---

### **Step 4: Use Debug Controls**

When you hit a breakpoint:
- **F8** (Step Over) - Execute current line, don't go into methods
- **F7** (Step Into) - Go inside method calls to see what happens
- **Shift+F8** (Step Out) - Exit current method
- **F9** (Resume) - Continue to next breakpoint
- **Ctrl+F8** - Toggle breakpoint on/off

---

### **Step 5: What to Watch For**

#### **At Breakpoint 1 (Line 58):**
- **Step Into** (`F7`) to see Spring's internal bean creation
- Watch the console - you'll see beans being created!
- Look at Variables panel - see `context` object

#### **At Breakpoint 2 (Line 72):**
- Before executing: `userService` is null
- After executing: `userService` has an instance
- **Step Into** to see how `getBean()` works internally

#### **At Breakpoint 3 (Line 106):**
- Execute line 106: `singleton1` is created
- Execute line 107: `singleton2` is created
- **Check in Variables:** `singleton1 == singleton2` should be `true`
- **Step Into** SingletonBean constructor - you'll see it's called ONCE

#### **At Breakpoint 4 (Line 121):**
- Execute line 121: `prototype1` is created
- Execute line 125: `prototype2` is created
- **Check in Variables:** `prototype1 == prototype2` should be `false`
- **Step Into** PrototypeBean constructor - you'll see it's called MULTIPLE times

#### **At Breakpoint 5 (Line 139):**
- **Step Into** - You'll see UserController constructor
- **Step Into** constructor parameters - You'll see EmailService and NotificationService being injected
- Watch how dependencies are automatically provided!

---

## 🔍 Additional Breakpoints to Set

Open these files and set breakpoints:

### **UserService.java** - Line 46 (Constructor)
```java
public UserService() {
    this.serviceName = "UserService";
    System.out.println("STEP 2: UserService constructor called - Bean being created!");
}
```
**Why:** See bean instantiation

### **UserController.java** - Line 83 (Constructor with DI)
```java
public UserController(EmailService emailService, NotificationService notificationService) {
```
**Why:** See constructor dependency injection

### **LifecycleBean.java** - Line 130 (@PostConstruct)
```java
@PostConstruct
public void postConstruct() {
```
**Why:** See initialization callback

### **AppConfig.java** - Line 40 (@Bean method)
```java
@Bean
public DatabaseConnection databaseConnection() {
```
**Why:** See @Bean method execution

---

## 💡 Pro Debugging Tips

### **1. Use "Evaluate Expression"**

When at a breakpoint, use the debugger's "Evaluate Expression" feature:

**Try these expressions:**
```java
// See all bean names
context.getBeanDefinitionNames()

// Get a specific bean
context.getBean("userService")

// Check if beans are same instance (singleton)
context.getBean(UserService.class) == context.getBean(UserService.class)

// Check bean count
context.getBeanDefinitionCount()
```

### **2. Watch Variables**

Add these to your Watch list:
- `context` - The Spring container
- `userService` - A bean instance
- `singleton1 == singleton2` - Singleton check
- `prototype1 == prototype2` - Prototype check

### **3. Conditional Breakpoints**

Right-click on a breakpoint → Add condition:

**Example:**
```java
beanName.equals("userService")
```
This will only break when the bean name is "userService"

### **4. Log Points (VS Code/Cursor)**

Instead of breakpoint, use Log Point:
1. Right-click on line number
2. Select "Add Logpoint"
3. Enter: `Bean created: {beanName}`
4. No pause, just logs!

---

## 📊 Expected Debug Flow

1. **Start** → Breakpoint at line 58
2. **Step Into** → See Spring scan for components
3. **Console shows** → Beans being created (UserService, ProductService, etc.)
4. **Resume (F9)** → Breakpoint at line 72
5. **Step Into** → See getBean() retrieve the bean
6. **Resume (F9)** → Breakpoint at line 106
7. **Execute** → singleton1 created
8. **Execute** → singleton2 created (same instance!)
9. **Resume (F9)** → Breakpoint at line 121
10. **Execute** → prototype1 created
11. **Execute** → prototype2 created (different instance!)
12. **Resume (F9)** → Breakpoint at line 139
13. **Step Into** → See dependency injection in action!

---

## 🎬 Quick Start (30 seconds)

1. **Open** `BeanDemoApplication.java` (you already have it open!)
2. **Click** on line 58 to set breakpoint (red dot appears)
3. **Press F5** (or click Debug button)
4. **Press F7** when it stops to step into Spring's code
5. **Press F9** to continue and see beans being created!

---

## ❓ Troubleshooting

**Problem:** Breakpoint not hitting
- **Solution:** Make sure you're in Debug mode, not Run mode
- Check if code compiled successfully

**Problem:** Can't step into Spring code
- **Solution:** Download Spring source code in IDE
- Or just Step Over (F8) to see the flow

**Problem:** Variables panel is empty
- **Solution:** Make sure you're at a breakpoint
- Variables appear when execution is paused

---

**Happy Debugging! 🐛✨**






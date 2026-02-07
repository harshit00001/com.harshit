# How to Run Hibernate and JPA Interview Preparation Project

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Project Setup](#project-setup)
3. [Running Examples](#running-examples)
4. [Understanding the Output](#understanding-the-output)
5. [Troubleshooting](#troubleshooting)
6. [Project Structure](#project-structure)

---

## 🔧 Prerequisites

Before running this project, ensure you have the following installed:

### Required Software

1. **Java Development Kit (JDK)**
   - **Version**: JDK 11 or higher
   - **Check Installation**: 
     ```bash
     java -version
     javac -version
     ```
   - **Download**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)

2. **Apache Maven**
   - **Version**: Maven 3.6 or higher
   - **Check Installation**:
     ```bash
     mvn -version
     ```
   - **Download**: [Apache Maven](https://maven.apache.org/download.cgi)
   - **Installation Guide**: 
     - Windows: Add Maven bin directory to PATH
     - Mac/Linux: Use package manager or download binary

3. **IDE (Optional but Recommended)**
   - **IntelliJ IDEA** (Community or Ultimate)
   - **Eclipse IDE for Enterprise Java**
   - **VS Code** with Java extensions

### System Requirements

- **Operating System**: Windows, macOS, or Linux
- **RAM**: Minimum 2GB (4GB recommended)
- **Disk Space**: At least 500MB free space

---

## 🚀 Project Setup

### Step 1: Navigate to Project Directory

Open your terminal/command prompt and navigate to the project root:

```bash
# Windows
cd "C:\Java code\hibernate-jpa-interview-prep"

# Mac/Linux
cd ~/Java\ code/hibernate-jpa-interview-prep
```

### Step 2: Verify Project Structure

Ensure you have the following structure:

```
hibernate-jpa-interview-prep/
├── pom.xml
├── README.md
├── HOW_TO_RUN.md
├── HIBERNATE_INTERVIEW_QA.md
├── JPA_INTERVIEW_QA.md
└── src/
    └── main/
        ├── java/
        │   └── com/harshit/
        │       ├── hibernate/
        │       └── jpa/
        └── resources/
            ├── hibernate.cfg.xml
            ├── logback.xml
            └── META-INF/
                └── persistence.xml
```

### Step 3: Download Dependencies

Maven will automatically download all required dependencies. Run:

```bash
mvn clean install
```

**What this does:**
- Downloads all dependencies from Maven Central Repository
- Compiles the project
- Runs tests (if any)
- Packages the project

**Expected Output:**
```
[INFO] Scanning for projects...
[INFO] 
[INFO] -----------------< com.harshit:hibernate-jpa-interview-prep >-----------------
[INFO] Building Hibernate and JPA Interview Preparation 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ hibernate-jpa-interview-prep ---
[INFO] Deleting C:\Java code\hibernate-jpa-interview-prep\target
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:resources (default-resources) @ hibernate-jpa-interview-prep ---
[INFO] Copying 3 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:compile (default-compile) @ hibernate-jpa-interview-prep ---
[INFO] Changes detected - recompiling the module
[INFO] Compiling 15 source files to C:\Java code\hibernate-jpa-interview-prep\target\classes
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:testResources (default-testResources) @ hibernate-jpa-interview-prep ---
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:testCompile (default-testCompile) @ hibernate-jpa-interview-prep ---
[INFO] 
[INFO] --- maven-surefire-plugin:2.22.2:test (default-test) @ hibernate-jpa-interview-prep ---
[INFO] 
[INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) @ hibernate-jpa-interview-prep ---
[INFO] Building jar: C:\Java code\hibernate-jpa-interview-prep\target\hibernate-jpa-interview-prep-1.0-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

**If you see "BUILD SUCCESS"**, you're ready to run examples!

**If you see errors:**
- Check your Java version: `java -version` (should be 11+)
- Check your Maven version: `mvn -version`
- Ensure you have internet connection (Maven needs to download dependencies)
- See [Troubleshooting](#troubleshooting) section

---

## 🎯 Running Examples

### Method 1: Using Maven Exec Plugin (Recommended)

This is the easiest way to run individual examples.

#### Running Hibernate Examples

**Example 1: Basic Hibernate Setup**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.basic.Example1BasicSetup"
```

**Example 2: CRUD Operations**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.basic.Example2CRUDOperations"
```

**Example 3: Relationships**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.intermediate.Example3Relationships"
```

**Example 4: HQL and Criteria API**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.advanced.Example4HQLAndCriteria"
```

**Example 5: Caching**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.advanced.Example5Caching"
```

**Example 6: Transactions and Isolation**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.advanced.Example6TransactionsAndIsolation"
```

**Real-World Problem 1: N+1 Query Problem**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.realworld.Problem1NPlusOne"
```

**Real-World Problem 2: LazyInitializationException**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.realworld.Problem2LazyInitialization"
```

#### Running JPA Examples

**Example 1: Basic JPA Setup**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.jpa.basic.Example1JPABasicSetup"
```

**Example 2: JPA CRUD Operations**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.jpa.basic.Example2JPACRUD"
```

**Example 3: JPA Relationships**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.jpa.intermediate.Example3JPARelationships"
```

**Example 4: JPQL and Criteria API**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.jpa.advanced.Example4JPQLAndCriteria"
```

**Example 5: Entity Lifecycle**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.jpa.advanced.Example5EntityLifecycle"
```

**Real-World Problem 1: Optimistic Locking**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.jpa.realworld.Problem1OptimisticLocking"
```

**Real-World Problem 2: Persistence Context**
```bash
mvn exec:java -Dexec.mainClass="com.harshit.jpa.realworld.Problem2PersistenceContext"
```

### Method 2: Using IDE (IntelliJ IDEA)

1. **Open Project in IntelliJ IDEA**
   - File → Open → Select project folder
   - Wait for Maven to import dependencies (bottom right corner)

2. **Run Configuration**
   - Right-click on any example class (e.g., `Example1BasicSetup.java`)
   - Select "Run 'Example1BasicSetup.main()'"
   - Or use keyboard shortcut: `Ctrl+Shift+F10` (Windows/Linux) or `Cmd+Shift+R` (Mac)

3. **View Output**
   - Output will appear in the "Run" tool window at the bottom
   - You'll see detailed logs explaining each step

### Method 3: Compile and Run Manually

1. **Compile the Project**
   ```bash
   mvn compile
   ```

2. **Run Using Java Command**
   ```bash
   # Windows
   java -cp "target/classes;target/dependency/*" com.harshit.hibernate.basic.Example1BasicSetup
   
   # Mac/Linux
   java -cp "target/classes:target/dependency/*" com.harshit.hibernate.basic.Example1BasicSetup
   ```

   **Note**: This method requires copying dependencies to `target/dependency/` first:
   ```bash
   mvn dependency:copy-dependencies
   ```

---

## 📊 Understanding the Output

### What You'll See

When you run an example, you'll see output like this:

```
🔄 === HIBERNATE BASIC SETUP ===

1. Loading Hibernate configuration...
   Explanation: This step loads hibernate.cfg.xml from resources folder
   - Loads database connection settings
   - Configures Hibernate properties
   - Validates entity mappings

2. Building SessionFactory...
   ✅ SessionFactory created successfully

3. Opening Session...
   ✅ Session opened

4. Beginning transaction...
   ✅ Transaction started

5. Ready to perform database operations
   (See other examples for CRUD operations)

6. Committing transaction...
   ✅ Transaction committed

✅ Basic setup completed successfully!

🔒 Session closed
🔒 SessionFactory closed
```

### Key Information in Output

1. **Step-by-Step Progress**: Each step is clearly labeled and explained
2. **Success Indicators**: ✅ marks successful operations
3. **Error Messages**: ❌ marks errors with detailed explanations
4. **SQL Queries**: If `hibernate.show_sql=true`, you'll see generated SQL
5. **Transaction Status**: Shows when transactions begin, commit, or rollback

### SQL Output

If you see SQL queries in the output, that's normal! The configuration has `show_sql=true` enabled for learning purposes. You'll see:

```
Hibernate: 
    insert 
    into
        users
        (username, email, first_name, last_name, age, created_at, status, id) 
    values
        (?, ?, ?, ?, ?, ?, ?, ?)
```

This helps you understand what Hibernate/JPA is doing behind the scenes.

---

## 🔍 Troubleshooting

### Problem 1: "mvn: command not found"

**Solution:**
- Maven is not installed or not in PATH
- **Windows**: Add Maven bin directory to System PATH
- **Mac/Linux**: Install via Homebrew: `brew install maven`
- Verify: `mvn -version`

### Problem 2: "Java version error" or "Unsupported class file version"

**Solution:**
- You're using Java version less than 11
- Check version: `java -version`
- Update to JDK 11 or higher
- Set JAVA_HOME environment variable

### Problem 3: "Could not find or load main class"

**Solution:**
- Project not compiled: Run `mvn compile` first
- Wrong class name: Check exact package and class name
- Classpath issue: Use Maven exec plugin instead (Method 1)

### Problem 4: "Connection refused" or Database errors

**Solution:**
- This project uses H2 in-memory database (no setup needed)
- If errors occur, check `hibernate.cfg.xml` and `persistence.xml`
- Ensure H2 dependency is in `pom.xml`
- Try: `mvn clean install` to rebuild

### Problem 5: "ClassNotFoundException" or "NoClassDefFoundError"

**Solution:**
- Dependencies not downloaded: Run `mvn clean install`
- Check internet connection (Maven needs to download dependencies)
- Clear Maven cache: Delete `~/.m2/repository` (Mac/Linux) or `C:\Users\YourName\.m2\repository` (Windows)

### Problem 6: "Persistence unit not found"

**Solution:**
- Check `persistence.xml` exists in `src/main/resources/META-INF/`
- Verify persistence unit name matches in code
- Ensure resources are copied: `mvn clean compile`

### Problem 7: Output is too verbose or not showing

**Solution:**
- Adjust logging level in `logback.xml`
- Change `hibernate.show_sql` in `hibernate.cfg.xml`
- Check console output settings in your IDE

### Problem 8: Port already in use (if using external database)

**Solution:**
- H2 in-memory database doesn't use ports
- If using MySQL/PostgreSQL, check if database is running
- Change port in configuration if needed

---

## 📁 Project Structure Explained

```
hibernate-jpa-interview-prep/
│
├── pom.xml                          # Maven configuration and dependencies
│
├── README.md                        # Project overview and learning guide
├── HOW_TO_RUN.md                    # This file - step-by-step running guide
├── HIBERNATE_INTERVIEW_QA.md        # Hibernate interview questions and answers
├── JPA_INTERVIEW_QA.md              # JPA interview questions and answers
│
└── src/
    └── main/
        ├── java/
        │   └── com/harshit/
        │       │
        │       ├── hibernate/              # Hibernate examples
        │       │   ├── basic/               # Basic concepts
        │       │   │   ├── Example1BasicSetup.java
        │       │   │   ├── Example2CRUDOperations.java
        │       │   │   └── entity/
        │       │   │       └── User.java
        │       │   │
        │       │   ├── intermediate/        # Intermediate concepts
        │       │   │   ├── Example3Relationships.java
        │       │   │   └── entity/
        │       │   │       ├── Department.java
        │       │   │       └── Employee.java
        │       │   │
        │       │   ├── advanced/           # Advanced concepts
        │       │   │   ├── Example4HQLAndCriteria.java
        │       │   │   ├── Example5Caching.java
        │       │   │   └── Example6TransactionsAndIsolation.java
        │       │   │
        │       │   └── realworld/          # Real-world problems
        │       │       ├── Problem1NPlusOne.java
        │       │       └── Problem2LazyInitialization.java
        │       │
        │       └── jpa/                    # JPA examples
        │           ├── basic/
        │           │   ├── Example1JPABasicSetup.java
        │           │   ├── Example2JPACRUD.java
        │           │   └── entity/
        │           │       └── Product.java
        │           │
        │           ├── intermediate/
        │           │   ├── Example3JPARelationships.java
        │           │   └── entity/
        │           │       ├── Customer.java
        │           │       ├── Order.java
        │           │       └── OrderItem.java
        │           │
        │           ├── advanced/
        │           │   ├── Example4JPQLAndCriteria.java
        │           │   └── Example5EntityLifecycle.java
        │           │
        │           └── realworld/
        │               ├── Problem1OptimisticLocking.java
        │               └── Problem2PersistenceContext.java
        │
        └── resources/
            ├── hibernate.cfg.xml            # Hibernate configuration
            ├── logback.xml                  # Logging configuration
            └── META-INF/
                └── persistence.xml         # JPA configuration
```

### Key Files Explained

- **pom.xml**: Maven project file containing dependencies and build configuration
- **hibernate.cfg.xml**: Hibernate-specific configuration (database, dialect, etc.)
- **persistence.xml**: JPA configuration (persistence units, database settings)
- **logback.xml**: Logging configuration (what gets logged and where)
- **Example*.java**: Learning examples with detailed explanations
- **Problem*.java**: Real-world problem scenarios and solutions
- **entity/*.java**: Entity classes representing database tables

---

## 🎓 Learning Path

### For Beginners

1. **Start with Basic Setup**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.hibernate.basic.Example1BasicSetup"
   mvn exec:java -Dexec.mainClass="com.harshit.jpa.basic.Example1JPABasicSetup"
   ```

2. **Learn CRUD Operations**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.hibernate.basic.Example2CRUDOperations"
   mvn exec:java -Dexec.mainClass="com.harshit.jpa.basic.Example2JPACRUD"
   ```

3. **Understand Relationships**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.hibernate.intermediate.Example3Relationships"
   mvn exec:java -Dexec.mainClass="com.harshit.jpa.intermediate.Example3JPARelationships"
   ```

### For Intermediate Learners

4. **Master Queries**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.hibernate.advanced.Example4HQLAndCriteria"
   mvn exec:java -Dexec.mainClass="com.harshit.jpa.advanced.Example4JPQLAndCriteria"
   ```

5. **Understand Caching and Transactions**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.hibernate.advanced.Example5Caching"
   mvn exec:java -Dexec.mainClass="com.harshit.hibernate.advanced.Example6TransactionsAndIsolation"
   ```

### For Advanced Learners

6. **Solve Real-World Problems**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.hibernate.realworld.Problem1NPlusOne"
   mvn exec:java -Dexec.mainClass="com.harshit.hibernate.realworld.Problem2LazyInitialization"
   mvn exec:java -Dexec.mainClass="com.harshit.jpa.realworld.Problem1OptimisticLocking"
   mvn exec:java -Dexec.mainClass="com.harshit.jpa.realworld.Problem2PersistenceContext"
   ```

---

## 💡 Tips for Best Learning Experience

1. **Read the Code**: Open each example file and read the detailed comments
2. **Run Examples**: Execute each example to see it in action
3. **Modify Code**: Try changing values, adding operations, see what happens
4. **Read Interview Q&A**: Check `HIBERNATE_INTERVIEW_QA.md` and `JPA_INTERVIEW_QA.md`
5. **Practice Speaking**: Explain concepts out loud as if in an interview
6. **Take Notes**: Write down key concepts and differences
7. **Experiment**: Don't be afraid to break things - that's how you learn!

---

## 🔄 Quick Reference Commands

### Build and Compile
```bash
mvn clean install          # Clean, compile, test, and package
mvn clean compile          # Just compile
mvn clean                  # Clean target directory
```

### Run Examples
```bash
# Hibernate
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.basic.Example1BasicSetup"
mvn exec:java -Dexec.mainClass="com.harshit.hibernate.basic.Example2CRUDOperations"

# JPA
mvn exec:java -Dexec.mainClass="com.harshit.jpa.basic.Example1JPABasicSetup"
mvn exec:java -Dexec.mainClass="com.harshit.jpa.basic.Example2JPACRUD"
```

### Check Dependencies
```bash
mvn dependency:tree        # Show dependency tree
mvn dependency:list        # List all dependencies
```

### IDE Integration
- **IntelliJ**: File → Open → Select project folder
- **Eclipse**: File → Import → Existing Maven Projects
- **VS Code**: Open folder, install Java extensions

---

## ✅ Verification Checklist

Before running examples, verify:

- [ ] Java 11+ installed (`java -version`)
- [ ] Maven 3.6+ installed (`mvn -version`)
- [ ] Project dependencies downloaded (`mvn clean install` succeeds)
- [ ] No compilation errors
- [ ] Configuration files exist (`hibernate.cfg.xml`, `persistence.xml`)
- [ ] IDE properly configured (if using IDE)

---

## 🆘 Getting Help

If you encounter issues:

1. **Check Troubleshooting Section**: See [Troubleshooting](#troubleshooting) above
2. **Verify Prerequisites**: Ensure Java and Maven are correctly installed
3. **Check Logs**: Look at error messages carefully - they usually tell you what's wrong
4. **Clean and Rebuild**: Try `mvn clean install` again
5. **Check Internet**: Maven needs internet to download dependencies

---

## 🎉 Success!

If you can run examples and see output like:

```
✅ Basic setup completed successfully!
```

Congratulations! You're ready to start learning Hibernate and JPA! 🚀

---

**Happy Learning!** 📚

For interview preparation, make sure to:
1. Run all examples
2. Read the detailed explanations in code
3. Study the interview Q&A documents
4. Practice explaining concepts out loud
5. Experiment with the code

Good luck with your interviews! 💪


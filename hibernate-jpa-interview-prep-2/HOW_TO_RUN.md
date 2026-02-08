# How to Run Hibernate and JPA Interview Preparation Project - Spring Boot Edition

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Project Setup](#project-setup)
3. [Running the Application](#running-the-application)
4. [Accessing the Application](#accessing-the-application)
5. [Testing REST API](#testing-rest-api)
6. [Understanding the Output](#understanding-the-output)
7. [Troubleshooting](#troubleshooting)

---

## 🔧 Prerequisites

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

3. **IDE (Optional but Recommended)**
   - **IntelliJ IDEA** (Community or Ultimate) - Best for Spring Boot
   - **Eclipse IDE for Enterprise Java**
   - **VS Code** with Java and Spring Boot extensions

### System Requirements

- **Operating System**: Windows, macOS, or Linux
- **RAM**: Minimum 2GB (4GB recommended)
- **Disk Space**: At least 500MB free space
- **Internet**: Required for first-time dependency download

---

## 🚀 Project Setup

### Step 1: Navigate to Project Directory

```bash
# Windows
cd "C:\Java code\hibernate-jpa-interview-prep"

# Mac/Linux
cd ~/Java\ code/hibernate-jpa-interview-prep
```

### Step 2: Verify Project Structure

Ensure you have:
- `pom.xml` (Maven configuration)
- `src/main/java/com/harshit/HibernateJpaInterviewPrepApplication.java` (Spring Boot main class)
- `src/main/resources/application.properties` (Configuration)

### Step 3: Download Dependencies

Maven will automatically download all Spring Boot dependencies:

```bash
mvn clean install
```

**What this does:**
- Downloads Spring Boot and all dependencies
- Compiles the project
- Runs tests (if any)
- Packages the project

**Expected Output:**
```
[INFO] Scanning for projects...
[INFO] 
[INFO] -----------------< com.harshit:hibernate-jpa-interview-prep >-----------------
[INFO] Building Hibernate and JPA Interview Preparation - Spring Boot 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
...
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

**If you see "BUILD SUCCESS"**, you're ready to run!

---

## 🎯 Running the Application

### Method 1: Using Maven (Recommended)

**Run Spring Boot application:**
```bash
mvn spring-boot:run
```

**What happens:**
1. Spring Boot starts embedded Tomcat server
2. Application context loads
3. All @Component, @Service, @Repository beans are created
4. CommandLineRunner executes (Example1SpringBootSetup)
5. Application is ready at http://localhost:8080

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.14)

2024-01-01 10:00:00 - Starting HibernateJpaInterviewPrepApplication
2024-01-01 10:00:01 - Started HibernateJpaInterviewPrepApplication in 2.5 seconds
🔄 === SPRING BOOT HIBERNATE/JPA SETUP EXAMPLE ===
...
✅ Spring Boot setup example completed successfully!
```

### Method 2: Using IDE (IntelliJ IDEA)

1. **Open Project:**
   - File → Open → Select project folder
   - Wait for Maven import (bottom right)

2. **Run Application:**
   - Find `HibernateJpaInterviewPrepApplication.java`
   - Right-click → Run 'HibernateJpaInterviewPrepApplication'
   - Or click green play button
   - Or use shortcut: `Shift+F10` (Windows/Linux) or `Ctrl+R` (Mac)

3. **View Output:**
   - Output appears in "Run" tool window
   - Shows Spring Boot startup logs
   - Shows example execution logs

### Method 3: Run as JAR

1. **Build JAR:**
   ```bash
   mvn clean package
   ```

2. **Run JAR:**
   ```bash
   java -jar target/hibernate-jpa-interview-prep-1.0-SNAPSHOT.jar
   ```

---

## 🌐 Accessing the Application

Once the application is running, you can access:

### 1. REST API Endpoints

**Base URL:** `http://localhost:8080/api/users`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get user by username |
| GET | `/api/users/active` | Get active users |
| GET | `/api/users/stats` | Get user statistics |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| PUT | `/api/users/{id}/deactivate` | Deactivate user |
| DELETE | `/api/users/{id}` | Delete user |

### 2. H2 Database Console

**URL:** http://localhost:8080/h2-console

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

**What you can do:**
- View all tables
- Run SQL queries
- See data inserted by examples
- Understand database structure

### 3. Swagger UI (API Documentation)

**URL:** http://localhost:8080/swagger-ui.html

**Features:**
- Interactive API documentation
- Test endpoints directly
- See request/response schemas
- No need for Postman!

---

## 🧪 Testing REST API

### Using curl

**Create User:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test_user",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "age": 25
  }'
```

**Get All Users:**
```bash
curl http://localhost:8080/api/users
```

**Get User by ID:**
```bash
curl http://localhost:8080/api/users/1
```

**Update User:**
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Updated",
    "lastName": "Name",
    "age": 26
  }'
```

**Delete User:**
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### Using Postman

1. **Import Collection:**
   - Create new collection
   - Add requests for each endpoint
   - Use base URL: `http://localhost:8080/api/users`

2. **Test Endpoints:**
   - Set method (GET, POST, PUT, DELETE)
   - Add headers: `Content-Type: application/json`
   - Add request body for POST/PUT
   - Send request and view response

### Using Browser

**GET requests only:**
- Open browser
- Navigate to: http://localhost:8080/api/users
- See JSON response

---

## 📊 Understanding the Output

### Spring Boot Startup Logs

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.14)
```

This is the Spring Boot banner - shows version and startup.

### Application Context Loading

```
2024-01-01 10:00:00 - Starting HibernateJpaInterviewPrepApplication
2024-01-01 10:00:01 - The following profiles are active: default
2024-01-01 10:00:01 - HikariPool-1 - Starting...
2024-01-01 10:00:01 - HikariPool-1 - Start completed.
2024-01-01 10:00:01 - HHH000227: Running hbm2ddl schema export
2024-01-01 10:00:01 - HHH000230: Schema export complete
2024-01-01 10:00:01 - Started HibernateJpaInterviewPrepApplication in 2.5 seconds
```

**What's happening:**
- Spring Boot is starting
- DataSource (HikariCP connection pool) is created
- Hibernate is creating database schema
- Application is ready!

### SQL Queries (if enabled)

```
Hibernate: 
    insert 
    into
        users
        (username, email, first_name, last_name, age, created_at, status, id) 
    values
        (?, ?, ?, ?, ?, ?, ?, ?)
```

This shows the actual SQL Hibernate executes. Great for learning!

### Example Execution Logs

```
🔄 === SPRING BOOT HIBERNATE/JPA SETUP EXAMPLE ===

📝 === DEMONSTRATION 1: SPRING BOOT AUTO-CONFIGURATION ===
✅ DataSource - H2 in-memory database
✅ EntityManagerFactory - JPA entity manager factory
✅ TransactionManager - Transaction management
...

✅ Spring Boot setup example completed successfully!
```

These are custom logs from Example1SpringBootSetup showing what's happening.

---

## 🔍 Troubleshooting

### Problem 1: Port 8080 already in use

**Error:**
```
Web server failed to start. Port 8080 was already in use.
```

**Solution:**
1. Change port in `application.properties`:
   ```properties
   server.port=8081
   ```
2. Or stop the application using port 8080
3. Or find and kill the process:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <pid> /F
   
   # Mac/Linux
   lsof -i :8080
   kill -9 <pid>
   ```

### Problem 2: "mvn: command not found"

**Solution:**
- Maven not installed or not in PATH
- Install Maven and add to PATH
- Or use IDE to run (IntelliJ/Eclipse)

### Problem 3: "Java version error"

**Solution:**
- Check Java version: `java -version` (should be 11+)
- Update JAVA_HOME environment variable
- Restart terminal/IDE

### Problem 4: Dependencies not downloading

**Solution:**
- Check internet connection
- Check Maven settings.xml
- Try: `mvn clean install -U` (force update)
- Clear Maven cache: Delete `~/.m2/repository`

### Problem 5: "Bean not found" or "No qualifying bean"

**Solution:**
- Ensure class has @Component, @Service, or @Repository
- Check package is under main application package
- Verify @ComponentScan includes your package
- Restart application

### Problem 6: H2 Console not accessible

**Solution:**
- Check `spring.h2.console.enabled=true` in application.properties
- Access at: http://localhost:8080/h2-console
- Use correct JDBC URL: `jdbc:h2:mem:testdb`

### Problem 7: SQL not showing in console

**Solution:**
- Check `spring.jpa.show-sql=true` in application.properties
- Check logging level: `logging.level.org.hibernate.SQL=DEBUG`
- Restart application

---

## ✅ Verification Checklist

Before running, verify:

- [ ] Java 11+ installed (`java -version`)
- [ ] Maven 3.6+ installed (`mvn -version`)
- [ ] Dependencies downloaded (`mvn clean install` succeeds)
- [ ] Port 8080 available (or change in application.properties)
- [ ] No compilation errors
- [ ] application.properties exists

---

## 🎓 Next Steps

After successfully running the application:

1. **Explore REST API:**
   - Use Postman or curl to test endpoints
   - Create, read, update, delete users
   - See how Spring Boot handles everything

2. **View Database:**
   - Open H2 Console
   - See tables created by Hibernate
   - Query data inserted by examples

3. **Read the Code:**
   - Each file has detailed comments
   - Understand Spring Boot annotations
   - Learn Spring Data JPA

4. **Study Interview Q&A:**
   - Read HIBERNATE_INTERVIEW_QA.md
   - Read JPA_INTERVIEW_QA.md
   - Practice explaining concepts

5. **Experiment:**
   - Modify code
   - Add new endpoints
   - Create new entities
   - Learn by doing!

---

## 🆘 Getting Help

If you encounter issues:

1. **Check Troubleshooting Section** above
2. **Check Logs** - Error messages usually tell you what's wrong
3. **Verify Prerequisites** - Java and Maven versions
4. **Clean and Rebuild:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
5. **Check Spring Boot Documentation:** https://spring.io/projects/spring-boot

---

## 🎉 Success!

If you see:
```
Started HibernateJpaInterviewPrepApplication in X seconds
✅ Spring Boot setup example completed successfully!
```

**Congratulations!** Your Spring Boot application is running! 🚀

**Access:**
- REST API: http://localhost:8080/api/users
- H2 Console: http://localhost:8080/h2-console
- Swagger UI: http://localhost:8080/swagger-ui.html

**Happy Learning!** 📚


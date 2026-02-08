# Hibernate and JPA Interview Preparation - Spring Boot Edition

A comprehensive Spring Boot-based interview preparation guide for Hibernate and JPA, featuring REST APIs, Spring Data JPA, detailed explanations, and real-world examples from basic to advanced levels.

## 🎯 What's New in Spring Boot Edition

- ✅ **Spring Boot Auto-Configuration** - No manual setup needed!
- ✅ **Spring Data JPA Repositories** - Zero boilerplate code
- ✅ **REST API Controllers** - Ready-to-use endpoints
- ✅ **Service Layer** - Proper layered architecture
- ✅ **Transaction Management** - Automatic with @Transactional
- ✅ **Embedded Server** - Run as standalone JAR
- ✅ **H2 Console** - View database in browser
- ✅ **Comprehensive Documentation** - Detailed explanations everywhere

## 📚 Project Structure

```
hibernate-jpa-interview-prep/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/harshit/
│   │   │       ├── HibernateJpaInterviewPrepApplication.java  # Spring Boot main class
│   │   │       ├── hibernate/
│   │   │       │   ├── basic/
│   │   │       │   │   ├── entity/          # JPA entities
│   │   │       │   │   ├── repository/      # Spring Data JPA repositories
│   │   │       │   │   ├── service/          # Service layer
│   │   │       │   │   ├── controller/      # REST API controllers
│   │   │       │   │   └── Example1SpringBootSetup.java
│   │   │       │   ├── intermediate/        # Intermediate examples
│   │   │       │   ├── advanced/            # Advanced examples
│   │   │       │   └── realworld/           # Real-world problems
│   │   │       └── jpa/                     # JPA examples
│   │   └── resources/
│   │       └── application.properties       # Spring Boot configuration
│   └── test/                                 # Test files
├── pom.xml                                    # Maven dependencies
├── README.md                                  # This file
├── HOW_TO_RUN.md                              # Step-by-step running guide
├── HIBERNATE_INTERVIEW_QA.md                  # Hibernate interview Q&A
└── JPA_INTERVIEW_QA.md                        # JPA interview Q&A
```

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone/Navigate to project:**
   ```bash
   cd "C:\Java code\hibernate-jpa-interview-prep"
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application:**
   - REST API: http://localhost:8080/api/users
   - H2 Console: http://localhost:8080/h2-console
   - Swagger UI: http://localhost:8080/swagger-ui.html

## 📖 Key Features

### 1. Spring Boot Auto-Configuration
- No `hibernate.cfg.xml` or `persistence.xml` needed
- All configuration in `application.properties`
- Automatic DataSource, EntityManagerFactory, TransactionManager setup

### 2. Spring Data JPA
- Repository interfaces with automatic implementation
- Query methods from method names
- Custom queries with @Query
- Pagination and sorting support

### 3. REST API
- Full CRUD operations
- Proper HTTP status codes
- Request/Response DTOs
- Exception handling

### 4. Service Layer
- Business logic separation
- Transaction management
- Reusable components

### 5. Comprehensive Examples
- Basic setup and configuration
- CRUD operations
- Relationships (One-to-Many, Many-to-One)
- Advanced queries (JPQL, Criteria API)
- Caching strategies
- Transaction management
- Real-world problem solutions

## 🎓 Learning Path

### For Beginners
1. **Spring Boot Basics**
   - Understand @SpringBootApplication
   - Learn auto-configuration
   - Understand dependency injection

2. **JPA Entities**
   - Entity annotations
   - Primary keys
   - Relationships

3. **Spring Data JPA**
   - Repository interfaces
   - Query methods
   - Basic CRUD

### For Intermediate
4. **Service Layer**
   - Business logic
   - Transaction management
   - Exception handling

5. **REST API**
   - Controller design
   - HTTP methods
   - Request/Response handling

6. **Relationships**
   - One-to-Many
   - Many-to-One
   - Fetch strategies

### For Advanced
7. **Advanced Queries**
   - JPQL
   - Criteria API
   - Native queries

8. **Performance**
   - Caching
   - Lazy vs Eager loading
   - N+1 problem solutions

9. **Real-World Problems**
   - Optimistic locking
   - Persistence context issues
   - Transaction propagation

## 📝 REST API Endpoints

### User Management API

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

### Example Requests

**Create User:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "age": 30
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

## 🔧 Configuration

All configuration is in `src/main/resources/application.properties`:

- **Database:** H2 in-memory (no setup needed)
- **JPA:** Auto-configured by Spring Boot
- **Logging:** Configured for learning (shows SQL)
- **H2 Console:** Enabled for database viewing

### Switching to MySQL/PostgreSQL

Uncomment and configure in `application.properties`:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## 📚 Documentation

- **HOW_TO_RUN.md** - Detailed step-by-step running guide
- **HIBERNATE_INTERVIEW_QA.md** - Hibernate interview questions and answers
- **JPA_INTERVIEW_QA.md** - JPA interview questions and answers

## 🎯 Interview Topics Covered

### Spring Boot Topics
- ✅ @SpringBootApplication and auto-configuration
- ✅ Dependency Injection (@Autowired, @Component, @Service, @Repository)
- ✅ Spring Data JPA repositories
- ✅ @Transactional annotation
- ✅ REST API with @RestController
- ✅ Application properties configuration
- ✅ CommandLineRunner for initialization

### Hibernate/JPA Topics
- ✅ Entity mapping and annotations
- ✅ Relationships (One-to-Many, Many-to-One, One-to-One, Many-to-Many)
- ✅ CRUD operations
- ✅ JPQL and Criteria API
- ✅ Caching (First Level, Second Level)
- ✅ Transaction management
- ✅ Lazy vs Eager loading
- ✅ N+1 query problem
- ✅ Optimistic locking
- ✅ Entity lifecycle

## 💡 Key Differences: Standalone vs Spring Boot

| Feature | Standalone Java | Spring Boot |
|---------|----------------|-------------|
| Configuration | hibernate.cfg.xml, persistence.xml | application.properties |
| EntityManagerFactory | Manual creation | Auto-configured |
| Transaction Management | Manual begin/commit | @Transactional |
| Repository | Manual EntityManager code | Spring Data JPA interface |
| Resource Management | Try-finally blocks | Automatic |
| Server | External (Tomcat, etc.) | Embedded Tomcat |
| Setup Complexity | High | Low (zero config) |

## 🛠️ Development Tips

1. **Use H2 Console** to view database:
   - URL: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (empty)

2. **View SQL Queries:**
   - SQL is logged to console (configured in application.properties)
   - Great for learning what Hibernate/JPA does

3. **Test REST API:**
   - Use Postman or curl
   - Or use Swagger UI: http://localhost:8080/swagger-ui.html

4. **Hot Reload:**
   - Spring Boot DevTools enables automatic restart
   - Changes are picked up automatically

## 🐛 Troubleshooting

See **HOW_TO_RUN.md** for detailed troubleshooting guide.

Common issues:
- Port 8080 already in use → Change `server.port` in application.properties
- Database connection errors → Check H2 is on classpath
- Bean not found → Ensure @Component, @Service, or @Repository annotations

## 📦 Dependencies

Key dependencies (managed by Spring Boot parent):
- `spring-boot-starter-web` - Web and REST API support
- `spring-boot-starter-data-jpa` - JPA and Hibernate
- `h2` - In-memory database
- `lombok` - Reduces boilerplate code
- `springdoc-openapi-ui` - API documentation

## 🎓 Study Guide

1. **Read the Code** - Each file has detailed comments
2. **Run Examples** - Execute and see output
3. **Modify Code** - Experiment and learn
4. **Read Interview Q&A** - Prepare for interviews
5. **Practice REST API** - Use Postman/curl
6. **View Database** - Use H2 Console

## 🤝 Contributing

Feel free to add more examples, improve existing code, or add more interview questions!

## 📄 License

This project is for educational purposes.

---

**Happy Learning! 🚀**

Start with `mvn spring-boot:run` and explore the REST API at http://localhost:8080/api/users


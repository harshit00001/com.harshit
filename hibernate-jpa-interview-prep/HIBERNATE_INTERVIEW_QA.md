# Hibernate Interview Questions and Answers

## Table of Contents
1. [Basic Questions](#basic-questions)
2. [Intermediate Questions](#intermediate-questions)
3. [Advanced Questions](#advanced-questions)
4. [Real-World Scenarios](#real-world-scenarios)

---

## Basic Questions

### Q1: What is Hibernate?
**Answer:**
Hibernate is an open-source ORM (Object-Relational Mapping) framework for Java. It simplifies database operations by mapping Java objects to database tables and vice versa.

**Key Features:**
- Eliminates need for writing SQL queries
- Database-independent
- Automatic table creation
- Lazy loading support
- Caching mechanisms

**Example:**
```java
// Instead of writing SQL:
// INSERT INTO users (name, email) VALUES ('John', 'john@example.com')

// You write:
User user = new User("John", "john@example.com");
session.save(user);
```

**See:** `Example1BasicSetup.java`

---

### Q2: What is the difference between Session and SessionFactory?
**Answer:**

| Session | SessionFactory |
|---------|---------------|
| Lightweight, not thread-safe | Heavyweight, thread-safe |
| Short-lived (one transaction) | Long-lived (application lifetime) |
| Created from SessionFactory | Created once per application |
| Represents database connection | Factory for creating Sessions |
| Must be closed after use | Should be closed on application shutdown |

**Code Example:**
```java
// SessionFactory (created once)
SessionFactory sessionFactory = new Configuration()
    .configure()
    .buildSessionFactory();

// Session (created per operation)
Session session = sessionFactory.openSession();
try {
    // Use session
} finally {
    session.close();
}
```

**See:** `Example1BasicSetup.java`

---

### Q3: What are the core interfaces of Hibernate?
**Answer:**
1. **Configuration**: Loads configuration and creates SessionFactory
2. **SessionFactory**: Factory for creating Session instances (singleton)
3. **Session**: Main interface for database operations
4. **Transaction**: Manages database transactions
5. **Query**: Represents HQL queries
6. **Criteria**: Type-safe query API

---

### Q4: Explain Hibernate entity mapping annotations.
**Answer:**

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks class as Hibernate entity |
| `@Table` | Specifies table name |
| `@Id` | Marks field as primary key |
| `@GeneratedValue` | Auto-generates primary key |
| `@Column` | Maps field to column |
| `@OneToMany` | One-to-many relationship |
| `@ManyToOne` | Many-to-one relationship |
| `@OneToOne` | One-to-one relationship |
| `@ManyToMany` | Many-to-many relationship |

**Example:**
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false)
    private String username;
}
```

**See:** `com.harshit.hibernate.basic.entity.User.java`

---

### Q5: What is the difference between save(), persist(), and saveOrUpdate()?
**Answer:**

| Method | Returns | Behavior |
|--------|---------|----------|
| `save()` | Returns generated ID | Saves entity, returns ID immediately |
| `persist()` | void | Saves entity, ID set after flush |
| `saveOrUpdate()` | void | Saves if new, updates if exists |

**When to use:**
- `save()`: When you need the ID immediately
- `persist()`: For new entities (JPA standard)
- `saveOrUpdate()`: When unsure if entity is new or existing

**See:** `Example2CRUDOperations.java`

---

## Intermediate Questions

### Q6: Explain Hibernate relationships (One-to-Many, Many-to-One).
**Answer:**

**One-to-Many:**
- One parent entity has many child entities
- Example: One Department has many Employees
- Use `@OneToMany` on parent side
- Use `mappedBy` on inverse side

**Many-to-One:**
- Many child entities belong to one parent
- Example: Many Employees belong to one Department
- Use `@ManyToOne` on child side
- Child side is the owning side (has foreign key)

**Code Example:**
```java
// Parent (Department)
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
private List<Employee> employees;

// Child (Employee)
@ManyToOne
@JoinColumn(name = "department_id")
private Department department;
```

**See:** `Example3Relationships.java`

---

### Q7: What is the difference between LAZY and EAGER loading?
**Answer:**

| LAZY Loading | EAGER Loading |
|--------------|---------------|
| Loads on-demand | Loads immediately |
| Default for @OneToMany | Default for @ManyToOne |
| Better performance | Can cause N+1 problem |
| May cause LazyInitializationException | Always available |

**Example:**
```java
// LAZY (default for @OneToMany)
@OneToMany(fetch = FetchType.LAZY)
private List<Employee> employees; // Loaded when accessed

// EAGER (default for @ManyToOne)
@ManyToOne(fetch = FetchType.EAGER)
private Department department; // Loaded immediately
```

**Best Practice:** Use LAZY by default, use JOIN FETCH when needed.

**See:** `Example3Relationships.java`, `Problem2LazyInitialization.java`

---

### Q8: What is HQL (Hibernate Query Language)?
**Answer:**
HQL is an object-oriented query language similar to SQL, but works with entities and properties instead of tables and columns.

**Key Differences from SQL:**
- Uses entity names (User) instead of table names (users)
- Uses property names (firstName) instead of column names (first_name)
- Database-independent
- Supports polymorphism

**Examples:**
```java
// HQL
String hql = "FROM User WHERE age > :age";
Query query = session.createQuery(hql);
query.setParameter("age", 25);

// SQL equivalent
SELECT * FROM users WHERE age > 25
```

**See:** `Example4HQLAndCriteria.java`

---

### Q9: Explain Hibernate caching (First Level and Second Level).
**Answer:**

**First Level Cache (Session Cache):**
- Associated with Session object
- Enabled by default, cannot be disabled
- Lifecycle tied to Session
- Reduces database hits within same session

**Second Level Cache (SessionFactory Cache):**
- Shared across all sessions
- Optional, needs configuration
- Requires cache provider (EhCache, Hazelcast)
- Entity must be marked as @Cacheable

**Example:**
```java
// First level cache
Session session = sessionFactory.openSession();
User user1 = session.get(User.class, 1L); // DB hit
User user2 = session.get(User.class, 1L); // Cache hit (no DB)

// Second level cache (if configured)
Session session1 = sessionFactory.openSession();
User u1 = session1.get(User.class, 1L); // DB hit
session1.close();

Session session2 = sessionFactory.openSession();
User u2 = session2.get(User.class, 1L); // Second level cache hit
```

**See:** `Example5Caching.java`

---

### Q10: What is cascade in Hibernate?
**Answer:**
Cascade defines how operations on parent entity affect child entities.

**Cascade Types:**
- `CascadeType.ALL`: All operations cascade
- `CascadeType.PERSIST`: Save operation cascades
- `CascadeType.MERGE`: Merge operation cascades
- `CascadeType.REMOVE`: Delete operation cascades
- `CascadeType.REFRESH`: Refresh operation cascades

**Example:**
```java
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
private List<Employee> employees;

// When you delete department, all employees are deleted
session.delete(department); // Employees also deleted
```

---

## Advanced Questions

### Q11: What is the N+1 query problem and how do you solve it?
**Answer:**

**Problem:**
When fetching a collection of entities and their relationships, Hibernate executes:
- 1 query for parent entities
- N queries for each child entity
- Total: N+1 queries

**Example:**
```java
List<Department> departments = session.createQuery("FROM Department").list();
// 1 query executed

for (Department dept : departments) {
    dept.getEmployees().size(); // N queries (one per department)
}
// Total: 1 + N queries
```

**Solutions:**
1. **JOIN FETCH:**
   ```java
   List<Department> depts = session.createQuery(
       "SELECT DISTINCT d FROM Department d JOIN FETCH d.employees",
       Department.class).list();
   // Only 1 query executed
   ```

2. **@BatchSize:**
   ```java
   @OneToMany(mappedBy = "department")
   @BatchSize(size = 10)
   private List<Employee> employees;
   ```

3. **Subselect fetching:**
   ```java
   @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
   @Fetch(FetchMode.SUBSELECT)
   private List<Employee> employees;
   ```

**See:** `Problem1NPlusOne.java`

---

### Q12: What is LazyInitializationException and how do you handle it?
**Answer:**

**Problem:**
Trying to access a lazy-loaded collection after the session is closed results in `LazyInitializationException`.

**Cause:**
- @OneToMany uses LAZY loading by default
- Session is closed before accessing collection
- Hibernate cannot fetch data without active session

**Solutions:**
1. **Initialize before closing session:**
   ```java
   Hibernate.initialize(department.getEmployees());
   session.close();
   // Now can access employees
   ```

2. **Use JOIN FETCH:**
   ```java
   Department dept = session.createQuery(
       "SELECT d FROM Department d JOIN FETCH d.employees WHERE d.id = :id",
       Department.class)
       .setParameter("id", 1L)
       .uniqueResult();
   ```

3. **Open Session in View pattern** (Spring)

**See:** `Problem2LazyInitialization.java`

---

### Q13: Explain Hibernate transaction management.
**Answer:**

**Transaction Management:**
- Hibernate doesn't manage transactions itself
- Delegates to underlying JDBC/JTA
- Use `session.beginTransaction()` and `commit()`/`rollback()`

**Isolation Levels:**
1. READ UNCOMMITTED: Can read uncommitted data
2. READ COMMITTED: Only read committed data (default)
3. REPEATABLE READ: Same query returns same results
4. SERIALIZABLE: Highest isolation

**Example:**
```java
Session session = sessionFactory.openSession();
Transaction tx = session.beginTransaction();
try {
    // Operations
    tx.commit();
} catch (Exception e) {
    tx.rollback();
} finally {
    session.close();
}
```

**See:** `Example6TransactionsAndIsolation.java`

---

### Q14: What is the difference between get() and load()?
**Answer:**

| get() | load() |
|-------|--------|
| Returns null if not found | Throws exception if not found |
| Eager loading | Lazy loading (proxy) |
| Always hits database | May return proxy |
| Safe to use | Use when sure entity exists |

**Example:**
```java
// get() - returns null if not found
User user = session.get(User.class, 999L); // Returns null

// load() - returns proxy, throws exception on access if not found
User user = session.load(User.class, 999L); // Returns proxy
String name = user.getName(); // Throws exception if not found
```

**See:** `Example2CRUDOperations.java`

---

### Q15: What is the difference between update() and merge()?
**Answer:**

| update() | merge() |
|----------|---------|
| Throws exception if detached | Creates new if detached |
| Updates existing entity | Merges detached entity |
| Use for managed entities | Use for detached entities |

**Example:**
```java
// update() - fails if entity is detached
User detachedUser = ...; // Detached
session.update(detachedUser); // May throw exception

// merge() - works with detached entities
User mergedUser = session.merge(detachedUser); // Works fine
```

**See:** `Example2CRUDOperations.java`

---

## Real-World Scenarios

### Scenario 1: E-commerce Product Catalog
**Problem:** Displaying product list with categories causes performance issues.

**Solution:**
- Use JOIN FETCH to load products with categories
- Implement second-level cache for frequently accessed products
- Use pagination for large result sets

**See:** `Problem1NPlusOne.java`

---

### Scenario 2: User Management System
**Problem:** LazyInitializationException when accessing user roles in view layer.

**Solution:**
- Use DTOs to transfer data outside session
- Or use JOIN FETCH in service layer
- Or implement Open Session in View pattern

**See:** `Problem2LazyInitialization.java`

---

### Scenario 3: Banking Transaction System
**Problem:** Need to ensure data consistency and handle concurrent updates.

**Solution:**
- Use proper transaction isolation levels
- Implement optimistic locking
- Use database constraints

**See:** `Example6TransactionsAndIsolation.java`

---

## Additional Resources

- Review all example files in `src/main/java/com/harshit/hibernate/`
- Practice coding each example
- Understand the real-world scenarios
- Prepare to explain concepts in your own words

---

**Good luck with your interview! 🚀**


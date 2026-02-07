# JPA Interview Questions and Answers

## Table of Contents
1. [Basic Questions](#basic-questions)
2. [Intermediate Questions](#intermediate-questions)
3. [Advanced Questions](#advanced-questions)
4. [Real-World Scenarios](#real-world-scenarios)

---

## Basic Questions

### Q1: What is JPA and how is it different from Hibernate?
**Answer:**

**JPA (Java Persistence API):**
- Specification/standard for ORM in Java
- Part of Java EE (now Jakarta EE)
- Defines interfaces and annotations
- Multiple implementations available

**Hibernate:**
- Implementation of JPA specification
- Most popular JPA implementation
- Provides additional features beyond JPA

**Key Differences:**

| JPA | Hibernate |
|-----|-----------|
| Specification | Implementation |
| Uses EntityManager | Uses Session (native) |
| Uses JPQL | Uses HQL (native) |
| Standard API | Extended features |
| Can switch implementations | Specific to Hibernate |

**Example:**
```java
// JPA way
EntityManager em = emf.createEntityManager();
em.persist(entity);

// Hibernate native way
Session session = sessionFactory.openSession();
session.save(entity);
```

**See:** `Example1JPABasicSetup.java`

---

### Q2: What is EntityManager and EntityManagerFactory?
**Answer:**

**EntityManagerFactory:**
- Factory for creating EntityManager instances
- Heavyweight, thread-safe
- Created once per application
- Expensive to create (should be singleton)
- Loads persistence.xml configuration

**EntityManager:**
- Interface for interacting with persistence context
- Lightweight, not thread-safe
- Short-lived (one transaction typically)
- Represents persistence context
- Created from EntityManagerFactory

**Code Example:**
```java
// EntityManagerFactory (created once)
EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-examples");

// EntityManager (created per operation)
EntityManager em = emf.createEntityManager();
try {
    em.getTransaction().begin();
    // Operations
    em.getTransaction().commit();
} finally {
    em.close();
}
```

**See:** `Example1JPABasicSetup.java`

---

### Q3: What is persistence.xml and what does it contain?
**Answer:**

`persistence.xml` is the configuration file for JPA, located in `META-INF/` directory.

**Contains:**
- Persistence unit name
- Database connection details
- JPA provider (Hibernate, EclipseLink, etc.)
- Entity classes or packages
- Properties (dialect, show_sql, etc.)

**Example:**
```xml
<persistence-unit name="jpa-examples">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <properties>
        <property name="javax.persistence.jdbc.url" value="jdbc:h2:mem:testdb"/>
        <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
        <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
        <property name="hibernate.hbm2ddl.auto" value="create-drop"/>
    </properties>
</persistence-unit>
```

**See:** `src/main/resources/META-INF/persistence.xml`

---

### Q4: Explain JPA entity lifecycle states.
**Answer:**

JPA entities have 4 lifecycle states:

1. **NEW (Transient):**
   - Entity just created
   - Not associated with persistence context
   - No database representation

2. **MANAGED (Persistent):**
   - Entity in persistence context
   - Has database representation
   - Changes automatically tracked

3. **DETACHED:**
   - Was managed, but persistence context closed
   - Has database representation
   - Changes not automatically tracked

4. **REMOVED:**
   - Marked for deletion
   - Will be deleted on commit

**State Transitions:**
```
NEW → persist() → MANAGED
MANAGED → close() → DETACHED
MANAGED → remove() → REMOVED
DETACHED → merge() → MANAGED
```

**See:** `Example5EntityLifecycle.java`

---

### Q5: What is the difference between persist() and merge()?
**Answer:**

| persist() | merge() |
|-----------|---------|
| For new entities | For detached entities |
| Makes entity managed | Merges detached entity |
| Throws exception if entity exists | Creates new if detached |
| Returns void | Returns managed entity |

**Example:**
```java
// persist() - for new entities
Product product = new Product("Laptop", "Description", price);
em.persist(product); // Makes it managed

// merge() - for detached entities
Product detached = ...; // Detached entity
Product managed = em.merge(detached); // Returns managed entity
```

**See:** `Example2JPACRUD.java`, `Example5EntityLifecycle.java`

---

## Intermediate Questions

### Q6: What is JPQL and how is it different from SQL?
**Answer:**

**JPQL (Java Persistence Query Language):**
- Object-oriented query language
- Works with entities and properties
- Database-independent
- Type-safe with TypedQuery

**Key Differences:**

| JPQL | SQL |
|------|-----|
| `FROM Product p` | `FROM products` |
| `WHERE p.price > 50` | `WHERE price > 50` |
| Entity names | Table names |
| Property names | Column names |
| Database-independent | Database-specific |

**Example:**
```java
// JPQL
TypedQuery<Product> query = em.createQuery(
    "SELECT p FROM Product p WHERE p.price > :minPrice", 
    Product.class);
query.setParameter("minPrice", new BigDecimal("50.00"));

// SQL equivalent
SELECT * FROM products WHERE price > 50.00
```

**See:** `Example4JPQLAndCriteria.java`

---

### Q7: Explain JPA relationships (One-to-Many, Many-to-One).
**Answer:**

**One-to-Many:**
- One parent has many children
- Use `@OneToMany` on parent
- Use `mappedBy` on inverse side
- Default fetch: LAZY

**Many-to-One:**
- Many children belong to one parent
- Use `@ManyToOne` on child
- Child is owning side (has foreign key)
- Default fetch: EAGER

**Example:**
```java
// Parent (Order)
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
private List<OrderItem> orderItems;

// Child (OrderItem)
@ManyToOne
@JoinColumn(name = "order_id")
private Order order;
```

**See:** `Example3JPARelationships.java`

---

### Q8: What is the Criteria API and when should you use it?
**Answer:**

**Criteria API:**
- Programmatic, type-safe way to build queries
- Compile-time checking
- Better for dynamic queries
- More verbose but safer

**When to use:**
- Dynamic queries based on user input
- Complex queries built programmatically
- Type safety is important

**Example:**
```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Product> cq = cb.createQuery(Product.class);
Root<Product> product = cq.from(Product.class);

// Build predicates dynamically
Predicate predicate = cb.gt(product.get("price"), new BigDecimal("50.00"));
cq.where(predicate);

TypedQuery<Product> query = em.createQuery(cq);
List<Product> results = query.getResultList();
```

**See:** `Example4JPQLAndCriteria.java`

---

### Q9: What is cascade in JPA?
**Answer:**

Cascade defines how operations on parent entity affect child entities.

**Cascade Types:**
- `CascadeType.ALL`: All operations cascade
- `CascadeType.PERSIST`: Persist operation cascades
- `CascadeType.MERGE`: Merge operation cascades
- `CascadeType.REMOVE`: Remove operation cascades
- `CascadeType.REFRESH`: Refresh operation cascades
- `CascadeType.DETACH`: Detach operation cascades

**Example:**
```java
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
private List<OrderItem> orderItems;

// When you persist order, items are also persisted
em.persist(order); // OrderItems also persisted
```

**See:** `Example3JPARelationships.java`

---

### Q10: Explain fetch strategies (LAZY vs EAGER).
**Answer:**

**LAZY Loading:**
- Loads on-demand
- Default for @OneToMany, @ManyToMany
- Better performance
- May cause LazyInitializationException

**EAGER Loading:**
- Loads immediately
- Default for @ManyToOne, @OneToOne
- Always available
- Can cause N+1 problem

**Best Practice:**
- Use LAZY by default
- Use JOIN FETCH when needed
- Avoid EAGER on @OneToMany

**Example:**
```java
// LAZY (default)
@OneToMany(fetch = FetchType.LAZY)
private List<OrderItem> orderItems; // Loaded when accessed

// EAGER
@ManyToOne(fetch = FetchType.EAGER)
private Customer customer; // Loaded immediately
```

---

## Advanced Questions

### Q11: What is optimistic locking and how does it work?
**Answer:**

**Optimistic Locking:**
- Assumes conflicts are rare
- Uses version field to detect conflicts
- Throws OptimisticLockException on conflict
- Better for high-concurrency scenarios

**How it works:**
1. Add `@Version` field to entity
2. JPA increments version on each update
3. Before update, JPA checks if version matches
4. If version differs, throws OptimisticLockException

**Example:**
```java
@Entity
public class Product {
    @Id
    private Long id;
    
    @Version
    private Long version; // Auto-incremented on update
    
    // ...
}

// User 1 loads (version = 1)
Product p1 = em.find(Product.class, 1L);

// User 2 loads (version = 1)
Product p2 = em.find(Product.class, 1L);

// User 1 updates (version = 2)
p1.setPrice(new BigDecimal("99.99"));
em.merge(p1); // Version becomes 2

// User 2 tries to update (version mismatch)
p2.setPrice(new BigDecimal("89.99"));
em.merge(p2); // OptimisticLockException!
```

**See:** `Problem1OptimisticLocking.java`

---

### Q12: What is persistence context?
**Answer:**

**Persistence Context:**
- Set of managed entity instances
- Associated with EntityManager
- Tracks changes to managed entities
- Synchronizes with database on commit

**Key Points:**
- Entity is managed only within active EntityManager
- Changes to managed entities are automatically tracked
- Detached entities are not tracked
- One EntityManager = One Persistence Context

**Example:**
```java
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

Product product = em.find(Product.class, 1L); // Managed
product.setPrice(new BigDecimal("99.99")); // Change tracked

em.getTransaction().commit(); // Changes saved
em.close(); // Product becomes detached
```

**See:** `Problem2PersistenceContext.java`

---

### Q13: What is the difference between find() and getReference()?
**Answer:**

| find() | getReference() |
|--------|----------------|
| Returns null if not found | Throws exception if not found |
| Eager loading | Lazy loading (proxy) |
| Always hits database | Returns proxy immediately |
| Safe to use | Use when sure entity exists |

**Example:**
```java
// find() - eager, returns null if not found
Product product = em.find(Product.class, 999L); // Returns null

// getReference() - lazy, throws exception on access if not found
Product product = em.getReference(Product.class, 999L); // Returns proxy
String name = product.getName(); // Throws exception if not found
```

---

### Q14: Explain @NamedQuery and @NamedQueries.
**Answer:**

**@NamedQuery:**
- Predefined query with a name
- Can be reused
- Compiled once
- Better performance

**Example:**
```java
@Entity
@NamedQuery(
    name = "Product.findByPriceRange",
    query = "SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max"
)
public class Product {
    // ...
}

// Usage
TypedQuery<Product> query = em.createNamedQuery(
    "Product.findByPriceRange", 
    Product.class);
query.setParameter("min", minPrice);
query.setParameter("max", maxPrice);
```

**@NamedQueries:**
- Container for multiple @NamedQuery annotations

---

### Q15: What are entity callbacks (@PrePersist, @PreUpdate, etc.)?
**Answer:**

Entity callbacks are methods invoked at specific lifecycle events.

**Callback Annotations:**
- `@PrePersist`: Before entity is persisted
- `@PostPersist`: After entity is persisted
- `@PreUpdate`: Before entity is updated
- `@PostUpdate`: After entity is updated
- `@PreRemove`: Before entity is removed
- `@PostRemove`: After entity is removed
- `@PostLoad`: After entity is loaded

**Example:**
```java
@Entity
public class Product {
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
```

**See:** `com.harshit.jpa.basic.entity.Product.java`

---

## Real-World Scenarios

### Scenario 1: E-commerce Order System
**Problem:** Need to handle orders with multiple items efficiently.

**Solution:**
- Use @OneToMany for Order -> OrderItems
- Use JOIN FETCH to avoid N+1 problem
- Implement optimistic locking for concurrent updates
- Use DTOs for data transfer

**See:** `Example3JPARelationships.java`

---

### Scenario 2: Product Catalog with Search
**Problem:** Dynamic search with multiple filters.

**Solution:**
- Use Criteria API for dynamic queries
- Build predicates based on user input
- Implement pagination
- Use caching for frequently accessed products

**See:** `Example4JPQLAndCriteria.java`

---

### Scenario 3: Concurrent User Updates
**Problem:** Multiple users updating same entity simultaneously.

**Solution:**
- Implement optimistic locking with @Version
- Handle OptimisticLockException
- Reload entity and retry on conflict
- Show user-friendly error messages

**See:** `Problem1OptimisticLocking.java`

---

### Scenario 4: Web Application with Detached Entities
**Problem:** Entities become detached when passed to view layer.

**Solution:**
- Use DTOs (Data Transfer Objects)
- Convert entities to DTOs in service layer
- Convert DTOs back to entities when updating
- Use merge() for detached entities

**See:** `Problem2PersistenceContext.java`

---

## Additional Resources

- Review all example files in `src/main/java/com/harshit/jpa/`
- Practice coding each example
- Understand entity lifecycle states
- Master JPQL and Criteria API
- Study real-world problem solutions

---

## Quick Reference

### Common JPA Annotations
- `@Entity` - Marks class as entity
- `@Table` - Specifies table name
- `@Id` - Primary key
- `@GeneratedValue` - Auto-generation
- `@Column` - Column mapping
- `@OneToMany` - One-to-many relationship
- `@ManyToOne` - Many-to-one relationship
- `@OneToOne` - One-to-one relationship
- `@ManyToMany` - Many-to-many relationship
- `@JoinColumn` - Foreign key column
- `@Version` - Optimistic locking
- `@NamedQuery` - Named query

### Entity Lifecycle States
1. NEW (Transient)
2. MANAGED (Persistent)
3. DETACHED
4. REMOVED

### Key Methods
- `persist()` - Make entity managed
- `find()` - Find by ID
- `merge()` - Merge detached entity
- `remove()` - Remove entity
- `createQuery()` - Create JPQL query
- `getCriteriaBuilder()` - Get Criteria API builder

---

**Good luck with your interview! 🚀**


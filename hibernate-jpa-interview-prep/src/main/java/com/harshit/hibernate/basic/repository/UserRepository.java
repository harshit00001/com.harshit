package com.harshit.hibernate.basic.repository;

import com.harshit.hibernate.basic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harshit.hibernate.basic.entity.UserStatus;
import java.util.List;
import java.util.Optional;

/**
 * User Repository - Spring Data JPA Repository Example
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: What is Spring Data JPA and how does it work?
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * Spring Data JPA is a powerful abstraction layer built on top of JPA that significantly
 * reduces boilerplate code. Instead of writing EntityManager operations manually, you
 * just define an interface, and Spring Data JPA provides the implementation automatically.
 * 
 * HOW IT WORKS:
 * 
 * 1. INTERFACE DEFINITION:
 *    - You create an interface extending JpaRepository<Entity, ID>
 *    - JpaRepository provides common CRUD operations out of the box
 *    - Spring Data JPA automatically creates a proxy implementation at runtime
 * 
 * 2. METHOD NAMING CONVENTION:
 *    - Spring Data JPA uses method names to generate queries
 *    - Example: findByUsername(String username) -> SELECT * FROM users WHERE username = ?
 *    - Example: findByAgeGreaterThan(int age) -> SELECT * FROM users WHERE age > ?
 *    - No need to write SQL/JPQL - Spring generates it from method name!
 * 
 * 3. QUERY GENERATION:
 *    - Spring parses method names and generates queries
 *    - Keywords: findBy, find, get, read, query, count, exists, delete
 *    - Conditions: And, Or, Between, LessThan, GreaterThan, Like, In, etc.
 *    - Example: findByFirstNameAndLastName -> WHERE first_name = ? AND last_name = ?
 * 
 * 4. CUSTOM QUERIES:
 *    - Use @Query annotation for custom JPQL/SQL queries
 *    - Use @Modifying for update/delete queries
 *    - Use @Param for named parameters
 * 
 * BENEFITS:
 * 
 * 1. Less Boilerplate:
 *    - No need to write EntityManager code
 *    - No need to write common CRUD operations
 *    - No need to write simple queries
 * 
 * 2. Type Safety:
 *    - Compile-time checking
 *    - IDE autocomplete support
 *    - Refactoring-friendly
 * 
 * 3. Consistency:
 *    - Standardized way to access data
 *    - Consistent error handling
 *    - Consistent transaction management
 * 
 * 4. Testability:
 *    - Easy to mock repositories
 *    - Easy to test with in-memory database
 * 
 * ====================================================================================
 * REPOSITORY HIERARCHY:
 * ====================================================================================
 * 
 * Repository (marker interface)
 *   └── CrudRepository (basic CRUD operations)
 *       └── PagingAndSortingRepository (adds pagination and sorting)
 *           └── JpaRepository (adds JPA-specific methods, flush, batch operations)
 * 
 * JpaRepository provides:
 * - save(entity) - Save or update
 * - findById(id) - Find by primary key
 * - findAll() - Find all entities
 * - delete(entity) - Delete entity
 * - count() - Count entities
 * - existsById(id) - Check if exists
 * - flush() - Flush pending changes
 * - saveAll(entities) - Batch save
 * - deleteAll() - Delete all
 * - And many more...
 * 
 * ====================================================================================
 * METHOD NAMING CONVENTIONS:
 * ====================================================================================
 * 
 * Pattern: [action][Distinct][TopN][By][Property][Condition][OrderBy]
 * 
 * Examples:
 * - findByUsername(String username)
 * - findByAgeGreaterThan(int age)
 * - findByFirstNameAndLastName(String first, String last)
 * - findByEmailContaining(String email)
 * - findTop10ByAgeOrderByCreatedAtDesc(int age)
 * - countByStatus(UserStatus status)
 * - existsByEmail(String email)
 * - deleteByStatus(UserStatus status)
 * 
 * Keywords:
 * - And, Or
 * - Between, LessThan, LessThanEqual, GreaterThan, GreaterThanEqual
 * - After, Before
 * - IsNull, IsNotNull, NotNull
 * - Like, NotLike, StartingWith, EndingWith, Containing
 * - In, NotIn
 * - True, False
 * - IgnoreCase
 * - OrderBy...Asc/Desc
 * 
 * ====================================================================================
 * REAL-WORLD SCENARIO:
 * ====================================================================================
 * 
 * In a user management system, this repository would be used for:
 * - User registration: save(newUser)
 * - User login: findByUsername(username) or findByEmail(email)
 * - User search: findByFirstNameContaining(name)
 * - User statistics: countByStatus(ACTIVE)
 * - User management: findAll(), delete(user)
 * 
 * All without writing a single line of SQL or EntityManager code!
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     * 
     * Spring Data JPA automatically generates:
     * SELECT * FROM users WHERE username = ?
     * 
     * Returns Optional<User> - safe way to handle null
     * - Optional.empty() if not found
     * - Optional.of(user) if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     * 
     * Generated query:
     * SELECT * FROM users WHERE email = ?
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find users by first name and last name
     * 
     * Generated query:
     * SELECT * FROM users WHERE first_name = ? AND last_name = ?
     * 
     * Demonstrates multiple conditions with "And"
     */
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    
    /**
     * Find users older than specified age
     * 
     * Generated query:
     * SELECT * FROM users WHERE age > ?
     * 
     * Demonstrates comparison operators
     */
    List<User> findByAgeGreaterThan(Integer age);
    
    /**
     * Find users by status
     * 
     * Generated query:
     * SELECT * FROM users WHERE status = ?
     */
    List<User> findByStatus(UserStatus status);
    
    /**
     * Find users by first name containing (case-insensitive)
     * 
     * Generated query:
     * SELECT * FROM users WHERE UPPER(first_name) LIKE UPPER(?)
     * 
     * Demonstrates:
     * - Containing: LIKE %?%
     * - IgnoreCase: Case-insensitive search
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Count users by status
     * 
     * Generated query:
     * SELECT COUNT(*) FROM users WHERE status = ?
     * 
     * Returns number of users with given status
     */
    long countByStatus(UserStatus status);
    
    /**
     * Check if user exists by email
     * 
     * Generated query:
     * SELECT COUNT(*) > 0 FROM users WHERE email = ?
     * 
     * More efficient than findByEmail().isPresent()
     */
    boolean existsByEmail(String email);
    
    /**
     * Custom JPQL Query
     * 
     * When method naming is not enough, use @Query annotation
     * - Write JPQL (Java Persistence Query Language)
     * - Works with entities and properties, not tables and columns
     * - Database-independent
     * 
     * This query finds active users older than specified age
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND u.age > :minAge")
    List<User> findActiveUsersOlderThan(@Param("minAge") Integer minAge);
    
    /**
     * Custom Native SQL Query
     * 
     * Use nativeQuery = true for database-specific SQL
     * - Works with actual table and column names
     * - Database-specific
     * - Use when JPQL is not sufficient
     * 
     * This query uses native SQL (H2/MySQL syntax)
     */
    @Query(value = "SELECT * FROM users WHERE age BETWEEN :minAge AND :maxAge", nativeQuery = true)
    List<User> findUsersByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    /**
     * Custom Query with Projection
     * 
     * Returns only specific fields (projection)
     * - More efficient than loading entire entity
     * - Returns Object[] array with selected fields
     * 
     * This query returns only username and email
     */
    @Query("SELECT u.username, u.email FROM User u WHERE u.status = :status")
    List<Object[]> findUsernamesAndEmailsByStatus(@Param("status") UserStatus status);
}


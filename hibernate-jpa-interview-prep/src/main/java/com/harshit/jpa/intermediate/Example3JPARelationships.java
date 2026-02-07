package com.harshit.jpa.intermediate;

import com.harshit.jpa.intermediate.entity.Customer;
import com.harshit.jpa.intermediate.entity.Order;
import com.harshit.jpa.intermediate.entity.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

/**
 * Example 3: JPA Relationships - Comprehensive Guide
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: How do you handle relationships in JPA? Explain in detail.
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * JPA (Java Persistence API) provides a powerful and flexible way to map relationships
 * between entities. Understanding relationships is crucial for building efficient and
 * maintainable database applications. Let me explain this comprehensively.
 * 
 * 1. TYPES OF RELATIONSHIPS IN JPA:
 * 
 *    a) ONE-TO-MANY (@OneToMany):
 *       - Represents a relationship where one entity instance can be associated with
 *         multiple instances of another entity
 *       - Example: One Customer can have many Orders
 *       - The parent entity uses @OneToMany annotation
 *       - Default fetch type is LAZY (important for performance)
 *       - The inverse side (non-owning side) uses mappedBy attribute
 * 
 *    b) MANY-TO-ONE (@ManyToOne):
 *       - Represents a relationship where many instances of one entity can be
 *         associated with a single instance of another entity
 *       - Example: Many Orders belong to one Customer
 *       - The child entity uses @ManyToOne annotation
 *       - Default fetch type is EAGER (be careful with this!)
 *       - This is the owning side and has the foreign key column
 * 
 *    c) ONE-TO-ONE (@OneToOne):
 *       - Represents a relationship where one instance of an entity is associated
 *         with exactly one instance of another entity
 *       - Example: One User has one Profile
 *       - Can be bidirectional or unidirectional
 *       - One side must be the owning side with @JoinColumn
 * 
 *    d) MANY-TO-MANY (@ManyToMany):
 *       - Represents a relationship where many instances of one entity can be
 *         associated with many instances of another entity
 *       - Example: Many Students can enroll in many Courses
 *       - Requires a join table in the database
 *       - Default fetch type is LAZY
 * 
 * 2. OWNING SIDE VS INVERSE SIDE:
 * 
 *    - OWNING SIDE:
 *      * Has the foreign key column in the database
 *      * Uses @JoinColumn annotation to specify the foreign key column name
 *      * Changes to the relationship are persisted to the database
 *      * In @ManyToOne and @OneToOne, the side with @JoinColumn is the owning side
 * 
 *    - INVERSE SIDE:
 *      * Does NOT have the foreign key column
 *      * Uses mappedBy attribute to reference the owning side
 *      * Changes to the relationship are ignored (only owning side matters)
 *      * In @OneToMany, the side with the collection is typically the inverse side
 * 
 * 3. CASCADE OPERATIONS:
 * 
 *    Cascade defines how operations on the parent entity affect child entities:
 * 
 *    - CascadeType.ALL: All operations (persist, merge, remove, refresh, detach) cascade
 *    - CascadeType.PERSIST: When parent is persisted, children are also persisted
 *    - CascadeType.MERGE: When parent is merged, children are also merged
 *    - CascadeType.REMOVE: When parent is removed, children are also removed
 *    - CascadeType.REFRESH: When parent is refreshed, children are also refreshed
 *    - CascadeType.DETACH: When parent is detached, children are also detached
 * 
 *    IMPORTANT: Be careful with CascadeType.REMOVE as it can delete child records
 *    unintentionally. Always consider business requirements.
 * 
 * 4. FETCH STRATEGIES:
 * 
 *    - LAZY LOADING (FetchType.LAZY):
 *      * Data is loaded only when accessed
 *      * Default for @OneToMany and @ManyToMany
 *      * Better performance as it avoids loading unnecessary data
 *      * Can cause LazyInitializationException if accessed outside persistence context
 *      * Use JOIN FETCH in queries when you know you'll need the data
 * 
 *    - EAGER LOADING (FetchType.EAGER):
 *      * Data is loaded immediately with the parent entity
 *      * Default for @ManyToOne and @OneToOne
 *      * Always available, no risk of LazyInitializationException
 *      * Can cause N+1 query problem and performance issues
 *      * Generally avoid EAGER on @OneToMany relationships
 * 
 * 5. BEST PRACTICES FOR RELATIONSHIPS:
 * 
 *    a) Always use LAZY loading by default, especially for collections
 *    b) Use JOIN FETCH in JPQL queries when you need related data:
 *       "SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :id"
 *    c) Maintain bidirectional relationships properly using helper methods
 *    d) Be careful with cascade operations - understand what will be deleted
 *    e) Use @JoinColumn on the owning side to specify foreign key details
 *    f) Consider using DTOs (Data Transfer Objects) to avoid lazy loading issues
 *    g) Use @BatchSize to reduce N+1 queries when loading collections
 * 
 * 6. COMMON PITFALLS AND SOLUTIONS:
 * 
 *    a) N+1 Query Problem:
 *       - Problem: Loading a collection causes N additional queries
 *       - Solution: Use JOIN FETCH in queries or @BatchSize annotation
 * 
 *    b) LazyInitializationException:
 *       - Problem: Accessing lazy-loaded data outside persistence context
 *       - Solution: Use JOIN FETCH, initialize before closing, or use DTOs
 * 
 *    c) Detached Entity Issues:
 *       - Problem: Entities become detached when EntityManager closes
 *       - Solution: Use merge() to reattach or use DTOs for data transfer
 * 
 * 7. REAL-WORLD SCENARIO - E-COMMERCE ORDER SYSTEM:
 * 
 *    In a real e-commerce application, you would have:
 *    
 *    - Customer Entity: Represents a customer who can place multiple orders
 *      * Has @OneToMany relationship with Order
 *      * Uses LAZY loading for orders (customers can have many orders)
 *    
 *    - Order Entity: Represents a single order placed by a customer
 *      * Has @ManyToOne relationship with Customer (many orders to one customer)
 *      * Has @OneToMany relationship with OrderItem (one order has many items)
 *      * Contains order-level information (order number, date, total amount)
 *    
 *    - OrderItem Entity: Represents individual items in an order
 *      * Has @ManyToOne relationship with Order (many items to one order)
 *      * Contains item-level information (product name, quantity, price)
 *    
 *    When fetching an order, you typically want to:
 *    1. Load the order with its customer (EAGER or JOIN FETCH)
 *    2. Load the order with its items (JOIN FETCH to avoid N+1)
 *    3. Calculate totals efficiently
 *    4. Handle concurrent updates with optimistic locking
 * 
 * This example demonstrates all these concepts with working code that you can
 * run and experiment with. Each section includes detailed comments explaining
 * what's happening and why certain approaches are used.
 */
public class Example3JPARelationships {
    
    private static final Logger logger = LoggerFactory.getLogger(Example3JPARelationships.class);
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            logger.info("🔄 === JPA RELATIONSHIPS ===\n");
            
            emf = Persistence.createEntityManagerFactory("jpa-examples");
            em = emf.createEntityManager();
            
            // ============================================
            // CREATE RELATIONSHIPS
            // ============================================
            // This section demonstrates how to create entities with relationships
            // and how JPA handles the persistence of related entities.
            logger.info("📝 === CREATING RELATIONSHIPS ===\n");
            
            // Begin a transaction - all operations within this transaction will be
            // atomic (all succeed or all fail). This is important for maintaining
            // data consistency when creating related entities.
            em.getTransaction().begin();
            
            // STEP 1: Create Customer Entity
            // The Customer entity is the parent in a One-to-Many relationship with Order.
            // We create it first because Order has a foreign key reference to Customer.
            logger.info("1. Creating customer...");
            logger.info("   Explanation: Customer is the parent entity. We create it first");
            logger.info("   because Order entities will reference it via foreign key.\n");
            
            Customer customer = new Customer("John", "Doe", "john.doe@example.com");
            customer.setPhone("123-456-7890");
            
            // persist() makes the entity managed and schedules it for insertion into database.
            // At this point, the entity is in MANAGED state and changes are tracked.
            em.persist(customer);
            logger.info("   ✅ Customer created: {}", customer);
            logger.info("   Note: Customer ID is auto-generated by database (IDENTITY strategy)\n");
            
            // STEP 2: Create Order with OrderItems
            // This demonstrates a One-to-Many relationship (Order -> OrderItems)
            // and a Many-to-One relationship (Order -> Customer).
            logger.info("2. Creating order with items...");
            logger.info("   Explanation: Order has two relationships:");
            logger.info("   - Many-to-One with Customer (many orders belong to one customer)");
            logger.info("   - One-to-Many with OrderItem (one order has many items)\n");
            
            Order order = new Order("ORD-001");
            
            // Setting the customer establishes the Many-to-One relationship.
            // Since Customer is already persisted, JPA will use its ID as foreign key.
            order.setCustomer(customer);
            logger.info("   Setting customer relationship (Many-to-One)...");
            logger.info("   JPA will create foreign key: order.customer_id = customer.id\n");
            
            // Create OrderItem entities. These will be in a One-to-Many relationship
            // with the Order. Notice we're creating them but not persisting them yet.
            // The cascade configuration in Order entity will handle persistence.
            OrderItem item1 = new OrderItem("Laptop", 1, new BigDecimal("999.99"));
            OrderItem item2 = new OrderItem("Mouse", 2, new BigDecimal("29.99"));
            OrderItem item3 = new OrderItem("Keyboard", 1, new BigDecimal("79.99"));
            
            // Using the helper method addOrderItem() which:
            // 1. Adds the item to the order's orderItems collection
            // 2. Sets the order reference in the item (maintains bidirectional relationship)
            // 3. Recalculates the order total
            // This is a best practice for maintaining bidirectional relationships.
            logger.info("   Adding order items (One-to-Many relationship)...");
            order.addOrderItem(item1);
            order.addOrderItem(item2);
            order.addOrderItem(item3);
            logger.info("   Using helper method ensures bidirectional relationship is maintained\n");
            
            // Persist the order. Because Order has cascade = CascadeType.ALL on orderItems,
            // all OrderItem entities will also be persisted automatically. This is one of
            // the key benefits of cascade operations - you don't need to persist each child.
            em.persist(order);
            logger.info("   ✅ Order created: {}", order);
            logger.info("   Total amount: ${}", order.getTotalAmount());
            logger.info("   Note: OrderItems persisted automatically due to CASCADE.ALL\n");
            
            // STEP 3: Create another order for the same customer
            // This demonstrates that one customer can have multiple orders (One-to-Many).
            logger.info("3. Creating second order for same customer...");
            logger.info("   This demonstrates One-to-Many: one customer, many orders\n");
            
            Order order2 = new Order("ORD-002");
            order2.setCustomer(customer); // Same customer, different order
            OrderItem item4 = new OrderItem("Monitor", 1, new BigDecimal("299.99"));
            order2.addOrderItem(item4);
            em.persist(order2);
            logger.info("   ✅ Second order created: {}", order2);
            logger.info("   Customer now has {} orders\n", customer.getOrders().size());
            
            // Commit the transaction. This is when all the SQL INSERT statements are
            // actually executed against the database. If any error occurs, the entire
            // transaction is rolled back, maintaining data integrity.
            logger.info("4. Committing transaction...");
            logger.info("   All entities (Customer, Orders, OrderItems) will be saved to database");
            logger.info("   Foreign key relationships will be established\n");
            em.getTransaction().commit();
            
            // ============================================
            // READ RELATIONSHIPS
            // ============================================
            // This section demonstrates different ways to fetch entities with their
            // relationships, including LAZY loading, EAGER loading, and JOIN FETCH.
            logger.info("📖 === READING RELATIONSHIPS ===\n");
            
            // EXAMPLE 1: LAZY Loading (Default for @OneToMany)
            // When you fetch a Customer, the orders collection is NOT loaded immediately.
            // It's loaded only when you access it. This is the default behavior for
            // @OneToMany relationships and is generally preferred for performance.
            logger.info("1. Fetching customer with orders (LAZY loading)...");
            logger.info("   Explanation: LAZY is default for @OneToMany relationships.");
            logger.info("   Orders collection is NOT loaded until accessed.\n");
            
            Customer foundCustomer = em.find(Customer.class, customer.getId());
            logger.info("   Customer loaded: {}", foundCustomer);
            logger.info("   At this point, orders are NOT yet loaded from database\n");
            
            // Accessing the orders collection triggers the LAZY load.
            // JPA executes a SELECT query to fetch the orders at this moment.
            // This is called "lazy initialization" and happens transparently.
            logger.info("   Accessing orders collection (triggers LAZY load)...");
            logger.info("   JPA will now execute: SELECT * FROM orders WHERE customer_id = ?");
            logger.info("   Orders for {}:", foundCustomer.getFirstName());
            foundCustomer.getOrders().forEach(o -> 
                logger.info("      - {}: ${}", o.getOrderNumber(), o.getTotalAmount())
            );
            logger.info("   ✅ Orders loaded on-demand (LAZY loading)\n");
            
            // EXAMPLE 2: JOIN FETCH (Efficient Eager Loading)
            // JOIN FETCH is a powerful JPQL feature that allows you to eagerly load
            // related entities in a single query, avoiding the N+1 query problem.
            // This is better than EAGER fetch type because you control when to use it.
            logger.info("2. Fetching order with items using JOIN FETCH...");
            logger.info("   Explanation: JOIN FETCH loads related entities in one query.");
            logger.info("   This avoids N+1 query problem and is more efficient than");
            logger.info("   separate queries for order and its items.\n");
            
            // The JOIN FETCH syntax tells JPA to:
            // 1. Join the Order and OrderItem tables
            // 2. Fetch both in a single SELECT query
            // 3. Return Order with OrderItems already loaded
            // This is the recommended way to load related entities when you know you'll need them.
            TypedQuery<Order> query = em.createQuery(
                    "SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :id", 
                    Order.class);
            query.setParameter("id", order.getId());
            
            logger.info("   Executing JPQL: SELECT o FROM Order o JOIN FETCH o.orderItems...");
            logger.info("   This generates SQL: SELECT o.*, oi.* FROM orders o");
            logger.info("   LEFT JOIN order_items oi ON o.id = oi.order_id WHERE o.id = ?\n");
            
            Order orderWithItems = query.getSingleResult();
            
            logger.info("   ✅ Order loaded with items in single query (no N+1 problem)");
            logger.info("   Order: {}", orderWithItems.getOrderNumber());
            logger.info("   Items (already loaded, no additional query needed):");
            orderWithItems.getOrderItems().forEach(item -> {
                BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
                logger.info("      - {} x {} = ${}", 
                    item.getQuantity(), 
                    item.getProductName(), 
                    itemTotal);
            });
            logger.info("   Total order amount: ${}\n", orderWithItems.getTotalAmount());
            logger.info("");
            
            // ============================================
            // QUERY WITH RELATIONSHIPS
            // ============================================
            // This section demonstrates advanced JPQL queries that work with relationships,
            // including aggregate functions, grouping, and joining across entities.
            logger.info("📖 === QUERYING WITH RELATIONSHIPS ===\n");
            
            // EXAMPLE 3: Aggregate Queries with Relationships
            // This query demonstrates how to use aggregate functions (COUNT, SUM, AVG, etc.)
            // with relationships. We're counting orders per customer using a LEFT JOIN
            // to include customers with no orders (they'll have count = 0).
            logger.info("3. Finding customers with order count using aggregate query...");
            logger.info("   Explanation: This query uses:");
            logger.info("   - LEFT JOIN to include customers without orders");
            logger.info("   - COUNT() aggregate function to count orders");
            logger.info("   - GROUP BY to group results by customer");
            logger.info("   - Returns Object[] array with Customer and count\n");
            
            // The LEFT JOIN ensures that customers without orders are still included
            // in the result set (with a count of 0). If we used INNER JOIN, customers
            // without orders would be excluded.
            TypedQuery<Object[]> customerOrderQuery = em.createQuery(
                    "SELECT c, COUNT(o) FROM Customer c LEFT JOIN c.orders o GROUP BY c.id",
                    Object[].class);
            
            logger.info("   JPQL Query: SELECT c, COUNT(o) FROM Customer c");
            logger.info("              LEFT JOIN c.orders o GROUP BY c.id");
            logger.info("   Generated SQL: SELECT c.*, COUNT(o.id) FROM customers c");
            logger.info("                 LEFT JOIN orders o ON c.id = o.customer_id");
            logger.info("                 GROUP BY c.id\n");
            
            List<Object[]> results = customerOrderQuery.getResultList();
            
            logger.info("   ✅ Customer order statistics:");
            results.forEach(result -> {
                Customer c = (Customer) result[0];
                Long orderCount = (Long) result[1];
                logger.info("      - {} {}: {} orders", 
                    c.getFirstName(), c.getLastName(), orderCount);
            });
            logger.info("");
            
            // ADDITIONAL QUERY EXAMPLES (for reference):
            logger.info("4. Additional relationship query examples you can try:\n");
            logger.info("   a) Find orders with total amount > $500:");
            logger.info("      SELECT o FROM Order o WHERE o.totalAmount > 500\n");
            logger.info("   b) Find customers who placed orders in last 30 days:");
            logger.info("      SELECT DISTINCT c FROM Customer c JOIN c.orders o");
            logger.info("      WHERE o.orderDate >= :date\n");
            logger.info("   c) Find average order value per customer:");
            logger.info("      SELECT c, AVG(o.totalAmount) FROM Customer c");
            logger.info("      JOIN c.orders o GROUP BY c.id\n");
            logger.info("   d) Find customers with more than 5 orders:");
            logger.info("      SELECT c FROM Customer c WHERE SIZE(c.orders) > 5\n");
            logger.info("");
            
            logger.info("✅ JPA relationship operations completed!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
}


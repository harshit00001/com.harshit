package com.harshit.jpa.advanced;

import com.harshit.jpa.basic.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

/**
 * Example 4: JPQL and Criteria API
 * 
 * INTERVIEW QUESTION: What is JPQL and how is it different from SQL?
 * 
 * ANSWER:
 * JPQL (Java Persistence Query Language) is similar to SQL but:
 * - Works with entities and properties, not tables and columns
 * - Database-independent
 * - Supports polymorphism
 * - Type-safe when used with TypedQuery
 * 
 * CRITERIA API:
 * - Programmatic, type-safe way to build queries
 * - Compile-time checking
 * - Better for dynamic queries
 * - More verbose but safer
 * 
 * REAL-WORLD SCENARIO:
 * Building a product search feature with dynamic filters:
 * - Search by name, price range, category
 * - Sort by price, name, date
 * - Pagination support
 * - Criteria API is perfect for dynamic queries
 */
public class Example4JPQLAndCriteria {
    
    private static final Logger logger = LoggerFactory.getLogger(Example4JPQLAndCriteria.class);
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            logger.info("🔄 === JPQL AND CRITERIA API ===\n");
            
            emf = Persistence.createEntityManagerFactory("jpa-examples");
            em = emf.createEntityManager();
            
            createSampleData(em);
            
            // ============================================
            // JPQL EXAMPLES
            // ============================================
            logger.info("📝 === JPQL QUERIES ===\n");
            
            // 1. Simple SELECT
            logger.info("1. Simple SELECT query...");
            TypedQuery<Product> query1 = em.createQuery(
                    "SELECT p FROM Product p WHERE p.price > :minPrice", Product.class);
            query1.setParameter("minPrice", new BigDecimal("50.00"));
            List<Product> expensiveProducts = query1.getResultList();
            logger.info("   ✅ Found {} products with price > $50\n", expensiveProducts.size());
            
            // 2. Aggregate functions
            logger.info("2. Aggregate functions...");
            Double avgPrice = em.createQuery(
                    "SELECT AVG(p.price) FROM Product p", Double.class)
                    .getSingleResult();
            logger.info("   ✅ Average price: ${}\n", avgPrice);
            
            BigDecimal maxPrice = em.createQuery(
                    "SELECT MAX(p.price) FROM Product p", BigDecimal.class)
                    .getSingleResult();
            logger.info("   ✅ Maximum price: ${}\n", maxPrice);
            
            Long productCount = em.createQuery(
                    "SELECT COUNT(p) FROM Product p", Long.class)
                    .getSingleResult();
            logger.info("   ✅ Total products: {}\n", productCount);
            
            // 3. GROUP BY
            logger.info("3. GROUP BY (products by status)...");
            List<Object[]> statusStats = em.createQuery(
                    "SELECT p.status, COUNT(p), AVG(p.price) " +
                    "FROM Product p GROUP BY p.status", Object[].class)
                    .getResultList();
            logger.info("   ✅ Status statistics:");
            statusStats.forEach(stat -> 
                logger.info("      - {}: {} products, Avg price: ${}", 
                    stat[0], stat[1], stat[2])
            );
            logger.info("");
            
            // 4. Pagination
            logger.info("4. Pagination example...");
            TypedQuery<Product> paginatedQuery = em.createQuery(
                    "SELECT p FROM Product p ORDER BY p.price DESC", Product.class);
            paginatedQuery.setFirstResult(0); // offset
            paginatedQuery.setMaxResults(3);  // limit
            List<Product> top3 = paginatedQuery.getResultList();
            logger.info("   ✅ Top 3 products by price:");
            top3.forEach(p -> logger.info("      - {}: ${}", p.getName(), p.getPrice()));
            logger.info("");
            
            // 5. Named Parameters
            logger.info("5. Named parameters with multiple conditions...");
            TypedQuery<Product> complexQuery = em.createQuery(
                    "SELECT p FROM Product p " +
                    "WHERE p.price BETWEEN :minPrice AND :maxPrice " +
                    "AND p.stockQuantity > :minStock " +
                    "ORDER BY p.price ASC", Product.class);
            complexQuery.setParameter("minPrice", new BigDecimal("20.00"));
            complexQuery.setParameter("maxPrice", new BigDecimal("100.00"));
            complexQuery.setParameter("minStock", 10);
            List<Product> filteredProducts = complexQuery.getResultList();
            logger.info("   ✅ Found {} products matching criteria\n", filteredProducts.size());
            
            // ============================================
            // CRITERIA API EXAMPLES
            // ============================================
            logger.info("📝 === CRITERIA API ===\n");
            
            logger.info("1. Criteria query (products with price > $50)...");
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> product = cq.from(Product.class);
            
            Predicate pricePredicate = cb.gt(product.get("price"), new BigDecimal("50.00"));
            cq.where(pricePredicate);
            
            TypedQuery<Product> criteriaQuery = em.createQuery(cq);
            List<Product> criteriaResults = criteriaQuery.getResultList();
            logger.info("   ✅ Found {} products using Criteria API\n", criteriaResults.size());
            
            // 2. Dynamic Criteria Query
            logger.info("2. Dynamic Criteria query (build query based on conditions)...");
            CriteriaBuilder cb2 = em.getCriteriaBuilder();
            CriteriaQuery<Product> cq2 = cb2.createQuery(Product.class);
            Root<Product> product2 = cq2.from(Product.class);
            
            // Build predicates dynamically
            Predicate predicate = cb2.conjunction(); // Always true
            
            // Add conditions if needed
            predicate = cb2.and(predicate, 
                    cb2.greaterThan(product2.get("price"), new BigDecimal("30.00")));
            predicate = cb2.and(predicate, 
                    cb2.greaterThan(product2.get("stockQuantity"), 5));
            
            cq2.where(predicate);
            cq2.orderBy(cb2.asc(product2.get("price")));
            
            TypedQuery<Product> dynamicQuery = em.createQuery(cq2);
            List<Product> dynamicResults = dynamicQuery.getResultList();
            logger.info("   ✅ Found {} products using dynamic Criteria API\n", dynamicResults.size());
            
            logger.info("✅ JPQL and Criteria API examples completed!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
    
    private static void createSampleData(EntityManager em) {
        em.getTransaction().begin();
        
        Product p1 = new Product("Laptop", "Gaming laptop", new BigDecimal("999.99"));
        p1.setSku("LAP-001");
        p1.setStockQuantity(25);
        em.persist(p1);
        
        Product p2 = new Product("Mouse", "Wireless mouse", new BigDecimal("29.99"));
        p2.setSku("MOU-001");
        p2.setStockQuantity(100);
        em.persist(p2);
        
        Product p3 = new Product("Keyboard", "Mechanical keyboard", new BigDecimal("79.99"));
        p3.setSku("KEY-001");
        p3.setStockQuantity(50);
        em.persist(p3);
        
        Product p4 = new Product("Monitor", "4K monitor", new BigDecimal("299.99"));
        p4.setSku("MON-001");
        p4.setStockQuantity(15);
        em.persist(p4);
        
        Product p5 = new Product("Headphones", "Noise-cancelling", new BigDecimal("149.99"));
        p5.setSku("HEA-001");
        p5.setStockQuantity(30);
        em.persist(p5);
        
        em.getTransaction().commit();
    }
}


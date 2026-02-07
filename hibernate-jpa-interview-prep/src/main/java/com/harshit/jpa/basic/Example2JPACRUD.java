package com.harshit.jpa.basic;

import com.harshit.jpa.basic.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

/**
 * Example 2: JPA CRUD Operations
 * 
 * INTERVIEW QUESTION: How do you perform CRUD operations using JPA?
 * 
 * ANSWER:
 * CREATE: em.persist(entity) - adds entity to persistence context
 * READ: em.find(Class, id) - finds by primary key
 *       em.createQuery(JPQL) - creates JPQL query
 * UPDATE: em.merge(entity) - merges detached entity
 * DELETE: em.remove(entity) - removes entity
 * 
 * KEY METHODS:
 * - persist(): Makes entity managed (INSERT)
 * - find(): Retrieves entity by ID (SELECT)
 * - merge(): Merges detached entity (UPDATE)
 * - remove(): Removes entity (DELETE)
 * - createQuery(): Creates JPQL query
 * 
 * REAL-WORLD SCENARIO:
 * Product management in e-commerce:
 * - Add new products
 * - Search products by criteria
 * - Update product details and prices
 * - Remove discontinued products
 */
public class Example2JPACRUD {
    
    private static final Logger logger = LoggerFactory.getLogger(Example2JPACRUD.class);
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            logger.info("🔄 === JPA CRUD OPERATIONS ===\n");
            
            emf = Persistence.createEntityManagerFactory("jpa-examples");
            em = emf.createEntityManager();
            
            // ============================================
            // CREATE OPERATIONS
            // ============================================
            logger.info("📝 === CREATE OPERATIONS ===\n");
            
            em.getTransaction().begin();
            
            logger.info("1. Creating products using persist()...");
            Product product1 = new Product("Laptop", "Gaming laptop", new BigDecimal("1299.99"));
            product1.setSku("LAP-001");
            product1.setStockQuantity(25);
            em.persist(product1);
            logger.info("   ✅ Product 1 persisted: {}\n", product1);
            
            Product product2 = new Product("Mouse", "Wireless mouse", new BigDecimal("29.99"));
            product2.setSku("MOU-001");
            product2.setStockQuantity(100);
            em.persist(product2);
            logger.info("   ✅ Product 2 persisted: {}\n", product2);
            
            Product product3 = new Product("Keyboard", "Mechanical keyboard", new BigDecimal("79.99"));
            product3.setSku("KEY-001");
            product3.setStockQuantity(50);
            em.persist(product3);
            logger.info("   ✅ Product 3 persisted: {}\n", product3);
            
            em.getTransaction().commit();
            
            // ============================================
            // READ OPERATIONS
            // ============================================
            logger.info("📖 === READ OPERATIONS ===\n");
            
            // Read by ID
            logger.info("1. Reading product by ID using find()...");
            Product foundProduct = em.find(Product.class, product1.getId());
            logger.info("   ✅ Found product: {}\n", foundProduct);
            
            // Read using JPQL
            logger.info("2. Reading all products using JPQL...");
            TypedQuery<Product> query1 = em.createQuery("SELECT p FROM Product p", Product.class);
            List<Product> allProducts = query1.getResultList();
            logger.info("   ✅ Found {} products:", allProducts.size());
            allProducts.forEach(p -> logger.info("      - {}", p));
            logger.info("");
            
            // Read with WHERE clause
            logger.info("3. Reading products with price > $50 using JPQL...");
            TypedQuery<Product> query2 = em.createQuery(
                    "SELECT p FROM Product p WHERE p.price > :minPrice", Product.class);
            query2.setParameter("minPrice", new BigDecimal("50.00"));
            List<Product> expensiveProducts = query2.getResultList();
            logger.info("   ✅ Found {} products:", expensiveProducts.size());
            expensiveProducts.forEach(p -> logger.info("      - {}: ${}", p.getName(), p.getPrice()));
            logger.info("");
            
            // Read with named query (would be defined with @NamedQuery)
            logger.info("4. Reading products by status...");
            TypedQuery<Product> query3 = em.createQuery(
                    "SELECT p FROM Product p WHERE p.status = :status", Product.class);
            query3.setParameter("status", com.harshit.jpa.basic.entity.ProductStatus.ACTIVE);
            List<Product> activeProducts = query3.getResultList();
            logger.info("   ✅ Found {} active products\n", activeProducts.size());
            
            // ============================================
            // UPDATE OPERATIONS
            // ============================================
            logger.info("✏️  === UPDATE OPERATIONS ===\n");
            
            em.getTransaction().begin();
            
            // Update managed entity (in persistence context)
            logger.info("1. Updating managed entity...");
            foundProduct.setPrice(new BigDecimal("1099.99"));
            foundProduct.setStockQuantity(20);
            // No need to call merge() - entity is already managed
            logger.info("   ✅ Product updated: {}\n", foundProduct);
            
            // Update detached entity using merge()
            logger.info("2. Updating detached entity using merge()...");
            Product detachedProduct = new Product();
            detachedProduct.setId(product2.getId());
            detachedProduct.setName("Wireless Mouse Pro");
            detachedProduct.setPrice(new BigDecimal("39.99"));
            Product mergedProduct = em.merge(detachedProduct);
            logger.info("   ✅ Product merged: {}\n", mergedProduct);
            
            em.getTransaction().commit();
            
            // ============================================
            // DELETE OPERATIONS
            // ============================================
            logger.info("🗑️  === DELETE OPERATIONS ===\n");
            
            em.getTransaction().begin();
            
            // Delete using remove()
            logger.info("1. Deleting product using remove()...");
            Product productToDelete = em.find(Product.class, product3.getId());
            if (productToDelete != null) {
                em.remove(productToDelete);
                logger.info("   ✅ Product deleted: {}\n", productToDelete.getName());
            }
            
            // Delete using JPQL
            logger.info("2. Deleting products with stock = 0 using JPQL...");
            int deletedCount = em.createQuery("DELETE FROM Product p WHERE p.stockQuantity = 0")
                    .executeUpdate();
            logger.info("   ✅ {} products deleted\n", deletedCount);
            
            em.getTransaction().commit();
            
            logger.info("✅ JPA CRUD operations completed successfully!");
            
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


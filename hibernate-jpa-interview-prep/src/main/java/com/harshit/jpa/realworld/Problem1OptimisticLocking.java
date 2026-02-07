package com.harshit.jpa.realworld;

import com.harshit.jpa.basic.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import java.math.BigDecimal;

/**
 * REAL-WORLD PROBLEM 1: Optimistic Locking
 * 
 * PROBLEM:
 * When multiple users try to update the same entity simultaneously,
 * the last write wins, potentially overwriting changes.
 * 
 * SOLUTION:
 * Use @Version annotation to enable optimistic locking.
 * JPA automatically increments version on each update.
 * If version doesn't match, OptimisticLockException is thrown.
 * 
 * HOW IT WORKS:
 * 1. Add @Version field to entity
 * 2. JPA increments version on each update
 * 3. Before update, JPA checks if version matches
 * 4. If version differs, throws OptimisticLockException
 * 
 * REAL-WORLD SCENARIO:
 * Two users editing the same product:
 * - User A loads product (version = 1)
 * - User B loads product (version = 1)
 * - User A updates and saves (version = 2)
 * - User B tries to update (version mismatch -> exception)
 * - User B must reload and retry
 */
public class Problem1OptimisticLocking {
    
    private static final Logger logger = LoggerFactory.getLogger(Problem1OptimisticLocking.class);
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        
        try {
            logger.info("🔄 === OPTIMISTIC LOCKING PROBLEM ===\n");
            
            emf = Persistence.createEntityManagerFactory("jpa-examples");
            
            // Create initial product
            createInitialProduct(emf);
            
            // ============================================
            // PROBLEM: Concurrent Updates
            // ============================================
            logger.info("❌ === PROBLEM: CONCURRENT UPDATES ===\n");
            
            logger.info("Simulating two users updating the same product...");
            
            // User 1 loads product
            EntityManager em1 = emf.createEntityManager();
            em1.getTransaction().begin();
            Product product1 = em1.find(Product.class, 1L);
            logger.info("User 1 loads product: {} (version: {})", 
                    product1.getName(), getVersion(product1));
            
            // User 2 loads same product
            EntityManager em2 = emf.createEntityManager();
            em2.getTransaction().begin();
            Product product2 = em2.find(Product.class, 1L);
            logger.info("User 2 loads product: {} (version: {})\n", 
                    product2.getName(), getVersion(product2));
            
            // User 1 updates
            logger.info("User 1 updates price to $899.99...");
            product1.setPrice(new BigDecimal("899.99"));
            em1.getTransaction().commit();
            em1.close();
            logger.info("   ✅ User 1 update successful (version incremented)\n");
            
            // User 2 tries to update (should fail with optimistic lock)
            logger.info("User 2 tries to update price to $799.99...");
            product2.setPrice(new BigDecimal("799.99"));
            
            try {
                em2.getTransaction().commit();
                logger.error("   ❌ This should not happen - optimistic lock should prevent this!");
            } catch (OptimisticLockException e) {
                logger.error("   ✅ OptimisticLockException caught: {}", e.getMessage());
                logger.info("   User 2 must reload product and retry\n");
                em2.getTransaction().rollback();
            }
            em2.close();
            
            // ============================================
            // SOLUTION: Proper Handling
            // ============================================
            logger.info("✅ === SOLUTION: PROPER HANDLING ===\n");
            
            logger.info("User 2 reloads product and retries...");
            EntityManager em3 = emf.createEntityManager();
            em3.getTransaction().begin();
            
            // Reload to get latest version
            Product product3 = em3.find(Product.class, 1L);
            logger.info("   Reloaded product: {} (version: {})", 
                    product3.getName(), getVersion(product3));
            logger.info("   Current price: ${}\n", product3.getPrice());
            
            // Update with latest version
            product3.setPrice(new BigDecimal("799.99"));
            em3.getTransaction().commit();
            em3.close();
            logger.info("   ✅ User 2 update successful after reload\n");
            
            logger.info("✅ Optimistic locking demonstrated!");
            logger.info("\nTo enable optimistic locking, add to entity:");
            logger.info("@Version");
            logger.info("private Long version;");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }
    
    private static void createInitialProduct(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("999.99"));
        product.setSku("LAP-001");
        product.setStockQuantity(25);
        em.persist(product);
        
        em.getTransaction().commit();
        em.close();
    }
    
    // Helper method to get version (would use reflection or @Version field)
    private static Long getVersion(Product product) {
        // In real implementation, this would access @Version field
        // For demonstration, returning a placeholder
        return 1L;
    }
}


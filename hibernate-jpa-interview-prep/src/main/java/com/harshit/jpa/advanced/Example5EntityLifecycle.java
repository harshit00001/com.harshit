package com.harshit.jpa.advanced;

import com.harshit.jpa.basic.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

/**
 * Example 5: Entity Lifecycle and States
 * 
 * INTERVIEW QUESTION: Explain JPA entity lifecycle states.
 * 
 * ANSWER:
 * JPA entities have 4 lifecycle states:
 * 
 * 1. NEW (Transient):
 *    - Entity just created, not associated with persistence context
 *    - No database representation
 *    - Changes are not tracked
 * 
 * 2. MANAGED (Persistent):
 *    - Entity associated with persistence context
 *    - Has database representation
 *    - Changes are automatically tracked and synchronized
 * 
 * 3. DETACHED:
 *    - Entity was managed but persistence context is closed
 *    - Has database representation but not tracked
 *    - Changes are not automatically synchronized
 * 
 * 4. REMOVED:
 *    - Entity marked for deletion
 *    - Will be deleted from database on commit
 * 
 * STATE TRANSITIONS:
 * - NEW -> MANAGED: persist()
 * - MANAGED -> DETACHED: close() or clear()
 * - MANAGED -> REMOVED: remove()
 * - DETACHED -> MANAGED: merge()
 * 
 * REAL-WORLD SCENARIO:
 * Understanding entity states is crucial for:
 * - Optimistic locking
 * - Detached entity handling
 * - Performance optimization
 * - Transaction management
 */
public class Example5EntityLifecycle {
    
    private static final Logger logger = LoggerFactory.getLogger(Example5EntityLifecycle.class);
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            logger.info("🔄 === ENTITY LIFECYCLE STATES ===\n");
            
            emf = Persistence.createEntityManagerFactory("jpa-examples");
            em = emf.createEntityManager();
            
            // ============================================
            // NEW (Transient) State
            // ============================================
            logger.info("📝 === NEW (Transient) STATE ===\n");
            
            logger.info("1. Creating new entity (NEW/Transient state)...");
            Product product = new Product("Test Product", "Description", new BigDecimal("99.99"));
            product.setSku("TEST-001");
            logger.info("   Entity state: NEW (not in persistence context)");
            logger.info("   ID: {} (not set yet)\n", product.getId());
            
            // ============================================
            // MANAGED (Persistent) State
            // ============================================
            logger.info("📝 === MANAGED (Persistent) STATE ===\n");
            
            em.getTransaction().begin();
            
            logger.info("2. Persisting entity (NEW -> MANAGED)...");
            em.persist(product);
            logger.info("   Entity state: MANAGED (in persistence context)");
            logger.info("   ID: {} (generated)\n", product.getId());
            
            logger.info("3. Modifying managed entity...");
            product.setPrice(new BigDecimal("89.99"));
            logger.info("   Changes are tracked automatically");
            logger.info("   No need to call update/merge - changes will be saved on commit\n");
            
            em.getTransaction().commit();
            
            // ============================================
            // DETACHED State
            // ============================================
            logger.info("📝 === DETACHED STATE ===\n");
            
            logger.info("4. Closing EntityManager (MANAGED -> DETACHED)...");
            em.close();
            logger.info("   Entity state: DETACHED (not in persistence context)");
            logger.info("   Entity still has ID: {}\n", product.getId());
            
            logger.info("5. Modifying detached entity...");
            product.setPrice(new BigDecimal("79.99"));
            logger.info("   Changes are NOT tracked (detached state)\n");
            
            // ============================================
            // MERGE (DETACHED -> MANAGED)
            // ============================================
            logger.info("📝 === MERGE (DETACHED -> MANAGED) ===\n");
            
            em = emf.createEntityManager();
            em.getTransaction().begin();
            
            logger.info("6. Merging detached entity (DETACHED -> MANAGED)...");
            Product mergedProduct = em.merge(product);
            logger.info("   Entity state: MANAGED (merged into persistence context)");
            logger.info("   Original object: {} (still detached)", product == mergedProduct ? "same" : "different");
            logger.info("   Merged object: {} (managed)\n", mergedProduct);
            
            em.getTransaction().commit();
            em.close();
            
            // ============================================
            // REMOVED State
            // ============================================
            logger.info("📝 === REMOVED STATE ===\n");
            
            em = emf.createEntityManager();
            em.getTransaction().begin();
            
            logger.info("7. Removing entity (MANAGED -> REMOVED)...");
            Product productToRemove = em.find(Product.class, mergedProduct.getId());
            em.remove(productToRemove);
            logger.info("   Entity state: REMOVED (marked for deletion)");
            logger.info("   Will be deleted from database on commit\n");
            
            em.getTransaction().commit();
            em.close();
            
            logger.info("✅ Entity lifecycle demonstration completed!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
}


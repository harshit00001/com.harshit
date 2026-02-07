package com.harshit.jpa.realworld;

import com.harshit.jpa.basic.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

/**
 * REAL-WORLD PROBLEM 2: Persistence Context Issues
 * 
 * PROBLEM:
 * Understanding when entities are managed vs detached can be confusing.
 * Common issues:
 * - Modifying entities outside transaction
 * - Detached entity exceptions
 * - Changes not being persisted
 * 
 * KEY CONCEPTS:
 * - Persistence Context: Set of managed entity instances
 * - Entity is managed only within active EntityManager
 * - Changes to managed entities are automatically tracked
 * - Detached entities need merge() to become managed again
 * 
 * REAL-WORLD SCENARIO:
 * In a web application:
 * - Entity loaded in service layer (managed)
 * - Session closes (entity becomes detached)
 * - Try to update in controller (fails - entity is detached)
 * - Solution: Use DTOs or merge() detached entity
 */
public class Problem2PersistenceContext {
    
    private static final Logger logger = LoggerFactory.getLogger(Problem2PersistenceContext.class);
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        
        try {
            logger.info("🔄 === PERSISTENCE CONTEXT ISSUES ===\n");
            
            emf = Persistence.createEntityManagerFactory("jpa-examples");
            
            // ============================================
            // PROBLEM: Detached Entity
            // ============================================
            logger.info("❌ === PROBLEM: DETACHED ENTITY ===\n");
            
            EntityManager em1 = emf.createEntityManager();
            em1.getTransaction().begin();
            
            logger.info("1. Loading entity (entity is MANAGED)...");
            Product product = em1.find(Product.class, 1L);
            if (product == null) {
                // Create if doesn't exist
                product = new Product("Test Product", "Description", new BigDecimal("99.99"));
                product.setSku("TEST-001");
                em1.persist(product);
                em1.getTransaction().commit();
                em1.close();
                
                em1 = emf.createEntityManager();
                em1.getTransaction().begin();
                product = em1.find(Product.class, product.getId());
            }
            logger.info("   ✅ Product loaded: {} (MANAGED state)\n", product.getName());
            
            em1.getTransaction().commit();
            em1.close();
            logger.info("2. EntityManager closed (entity is now DETACHED)\n");
            
            logger.info("3. Trying to modify detached entity...");
            product.setPrice(new BigDecimal("89.99"));
            logger.info("   Changes made, but entity is DETACHED\n");
            
            // Try to persist detached entity (will fail or create duplicate)
            EntityManager em2 = emf.createEntityManager();
            em2.getTransaction().begin();
            
            logger.info("4. Trying to persist detached entity...");
            try {
                em2.persist(product); // This might fail or create duplicate
                logger.error("   ⚠️  This could cause issues!\n");
            } catch (Exception e) {
                logger.error("   ❌ Error: {}\n", e.getMessage());
            }
            em2.getTransaction().rollback();
            em2.close();
            
            // ============================================
            // SOLUTION: Use merge()
            // ============================================
            logger.info("✅ === SOLUTION: USE MERGE() ===\n");
            
            EntityManager em3 = emf.createEntityManager();
            em3.getTransaction().begin();
            
            logger.info("1. Merging detached entity (DETACHED -> MANAGED)...");
            Product mergedProduct = em3.merge(product);
            logger.info("   ✅ Entity merged: {} (now MANAGED)\n", mergedProduct.getName());
            
            logger.info("2. Modifying merged entity...");
            mergedProduct.setPrice(new BigDecimal("79.99"));
            logger.info("   Changes are tracked (entity is MANAGED)\n");
            
            em3.getTransaction().commit();
            em3.close();
            logger.info("3. Changes persisted successfully!\n");
            
            // ============================================
            // BEST PRACTICE: Use DTOs
            // ============================================
            logger.info("✅ === BEST PRACTICE: USE DTOs ===\n");
            logger.info("Instead of passing entities across layers:");
            logger.info("1. Load entity in service layer");
            logger.info("2. Convert to DTO (Data Transfer Object)");
            logger.info("3. Pass DTO to controller/presentation layer");
            logger.info("4. Convert DTO back to entity when updating");
            logger.info("5. Merge entity in service layer\n");
            
            logger.info("✅ Persistence context issues demonstrated!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }
}


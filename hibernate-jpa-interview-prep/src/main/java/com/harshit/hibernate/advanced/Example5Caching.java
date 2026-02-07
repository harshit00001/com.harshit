package com.harshit.hibernate.advanced;

import com.harshit.hibernate.intermediate.entity.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example 5: Hibernate Caching (First Level and Second Level Cache)
 * 
 * INTERVIEW QUESTION: Explain Hibernate caching mechanisms.
 * 
 * ANSWER:
 * Hibernate has two levels of caching:
 * 
 * 1. FIRST LEVEL CACHE (Session Cache):
 *    - Associated with Session object
 *    - Enabled by default, cannot be disabled
 *    - Lifecycle tied to Session
 *    - Reduces database hits within same session
 * 
 * 2. SECOND LEVEL CACHE (SessionFactory Cache):
 *    - Shared across all sessions
 *    - Optional, needs to be configured
 *    - Requires cache provider (EhCache, Hazelcast, etc.)
 *    - Configured in hibernate.cfg.xml
 *    - Entity must be marked as @Cacheable
 * 
 * CACHE REGIONS:
 * - Entity cache: Caches entity instances
 * - Query cache: Caches query results
 * - Collection cache: Caches collections
 * 
 * REAL-WORLD SCENARIO:
 * In a high-traffic e-commerce site:
 * - Product catalog is frequently accessed (good candidate for 2nd level cache)
 * - User sessions access same products multiple times (1st level cache helps)
 * - Reduces database load significantly
 */
public class Example5Caching {
    
    private static final Logger logger = LoggerFactory.getLogger(Example5Caching.class);
    
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        
        try {
            logger.info("🔄 === HIBERNATE CACHING ===\n");
            
            // Setup
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Department.class);
            sessionFactory = configuration.buildSessionFactory();
            
            // ============================================
            // FIRST LEVEL CACHE DEMONSTRATION
            // ============================================
            logger.info("📝 === FIRST LEVEL CACHE (Session Cache) ===\n");
            
            Session session1 = sessionFactory.openSession();
            session1.beginTransaction();
            
            logger.info("1. First query - will hit database...");
            Department dept1 = session1.get(Department.class, 1L);
            logger.info("   ✅ Department loaded: {}\n", dept1 != null ? dept1.getName() : "Not found");
            
            logger.info("2. Second query for same entity - will use cache (no DB hit)...");
            Department dept2 = session1.get(Department.class, 1L);
            logger.info("   ✅ Department loaded from cache: {}\n", dept2 != null ? dept2.getName() : "Not found");
            
            logger.info("3. Both references point to same object (identity):");
            logger.info("   dept1 == dept2: {}\n", dept1 == dept2);
            
            session1.getTransaction().commit();
            session1.close();
            
            // ============================================
            // NEW SESSION (First Level Cache is lost)
            // ============================================
            logger.info("📝 === NEW SESSION (Cache is lost) ===\n");
            
            Session session2 = sessionFactory.openSession();
            session2.beginTransaction();
            
            logger.info("4. Query in new session - will hit database again...");
            Department dept3 = session2.get(Department.class, 1L);
            logger.info("   ✅ Department loaded: {}\n", dept3 != null ? dept3.getName() : "Not found");
            
            logger.info("5. Different object instance (new session = new cache):");
            logger.info("   dept1 == dept3: {}\n", dept1 == dept3);
            
            session2.getTransaction().commit();
            session2.close();
            
            // ============================================
            // SECOND LEVEL CACHE (if configured)
            // ============================================
            logger.info("📝 === SECOND LEVEL CACHE ===\n");
            logger.info("To enable Second Level Cache:");
            logger.info("1. Add cache provider dependency (EhCache, Hazelcast, etc.)");
            logger.info("2. Configure in hibernate.cfg.xml:");
            logger.info("   - hibernate.cache.use_second_level_cache=true");
            logger.info("   - hibernate.cache.region.factory_class=...");
            logger.info("3. Mark entity as @Cacheable");
            logger.info("4. Use @Cache annotation for cache strategy\n");
            
            logger.info("✅ Caching demonstration completed!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
}


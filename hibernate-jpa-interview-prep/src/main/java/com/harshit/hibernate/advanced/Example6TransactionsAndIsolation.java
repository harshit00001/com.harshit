package com.harshit.hibernate.advanced;

import com.harshit.hibernate.intermediate.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Example 6: Transactions and Isolation Levels
 * 
 * INTERVIEW QUESTION: Explain transaction management and isolation levels in Hibernate.
 * 
 * ANSWER:
 * TRANSACTION MANAGEMENT:
 * - Hibernate doesn't manage transactions itself, delegates to underlying JDBC/JTA
 * - Use session.beginTransaction() and commit()/rollback()
 * - Transactions ensure ACID properties
 * 
 * ISOLATION LEVELS (from least to most strict):
 * 1. READ UNCOMMITTED: Can read uncommitted data (dirty reads)
 * 2. READ COMMITTED: Can only read committed data (default in most DBs)
 * 3. REPEATABLE READ: Same query returns same results within transaction
 * 4. SERIALIZABLE: Highest isolation, prevents all concurrency issues
 * 
 * CONCURRENCY ISSUES:
 * - Dirty Read: Reading uncommitted data
 * - Non-Repeatable Read: Different values on re-read
 * - Phantom Read: New rows appear on re-read
 * 
 * REAL-WORLD SCENARIO:
 * In a banking system:
 * - Money transfer must be atomic (all or nothing)
 * - Balance updates must be isolated (no dirty reads)
 * - Need proper isolation to prevent double-spending
 */
public class Example6TransactionsAndIsolation {
    
    private static final Logger logger = LoggerFactory.getLogger(Example6TransactionsAndIsolation.class);
    
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        
        try {
            logger.info("🔄 === TRANSACTIONS AND ISOLATION ===\n");
            
            // Setup
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Employee.class);
            sessionFactory = configuration.buildSessionFactory();
            
            // ============================================
            // BASIC TRANSACTION
            // ============================================
            logger.info("📝 === BASIC TRANSACTION ===\n");
            
            Session session = sessionFactory.openSession();
            
            try {
                session.beginTransaction();
                
                logger.info("1. Performing operations within transaction...");
                Employee emp = new Employee("Test", "User", "test@company.com");
                emp.setSalary(new BigDecimal("50000"));
                session.save(emp);
                
                logger.info("2. Committing transaction...");
                session.getTransaction().commit();
                logger.info("   ✅ Transaction committed successfully\n");
                
            } catch (Exception e) {
                logger.error("3. Error occurred, rolling back...");
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                    logger.info("   ✅ Transaction rolled back\n");
                }
                throw e;
            } finally {
                session.close();
            }
            
            // ============================================
            // ISOLATION LEVELS
            // ============================================
            logger.info("📝 === ISOLATION LEVELS ===\n");
            logger.info("To set isolation level, configure in hibernate.cfg.xml:");
            logger.info("<property name=\"hibernate.connection.isolation\">2</property>");
            logger.info("");
            logger.info("Isolation Level Values:");
            logger.info("1 = READ UNCOMMITTED");
            logger.info("2 = READ COMMITTED (default)");
            logger.info("4 = REPEATABLE READ");
            logger.info("8 = SERIALIZABLE");
            logger.info("");
            logger.info("Or set at connection level:");
            logger.info("session.doWork(connection -> connection.setTransactionIsolation(...));\n");
            
            // ============================================
            // TRANSACTION PROPAGATION (Spring context)
            // ============================================
            logger.info("📝 === TRANSACTION PROPAGATION (Spring) ===\n");
            logger.info("In Spring Framework:");
            logger.info("@Transactional(propagation = Propagation.REQUIRED) - Default");
            logger.info("@Transactional(propagation = Propagation.REQUIRES_NEW) - New transaction");
            logger.info("@Transactional(propagation = Propagation.SUPPORTS) - Use if exists");
            logger.info("@Transactional(propagation = Propagation.MANDATORY) - Must exist");
            logger.info("@Transactional(propagation = Propagation.NEVER) - Must not exist");
            logger.info("@Transactional(propagation = Propagation.NOT_SUPPORTED) - Suspend if exists");
            logger.info("@Transactional(propagation = Propagation.NESTED) - Nested transaction\n");
            
            logger.info("✅ Transaction examples completed!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
}


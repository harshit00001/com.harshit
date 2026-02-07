package com.harshit.hibernate.realworld;

import com.harshit.hibernate.intermediate.entity.Department;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REAL-WORLD PROBLEM 2: LazyInitializationException
 * 
 * PROBLEM:
 * Trying to access a lazy-loaded collection after the session is closed
 * results in LazyInitializationException.
 * 
 * CAUSE:
 * - @OneToMany uses LAZY loading by default
 * - Session is closed before accessing the collection
 * - Hibernate cannot fetch data without an active session
 * 
 * SOLUTIONS:
 * 1. Keep session open (Open Session in View pattern)
 * 2. Use JOIN FETCH to eagerly load data
 * 3. Initialize collection before closing session (Hibernate.initialize())
 * 4. Use DTOs to transfer data outside session
 * 
 * REAL-WORLD SCENARIO:
 * In a web application, you fetch an entity in service layer,
 * close the session, then try to access relationships in the view layer.
 * This causes LazyInitializationException.
 */
public class Problem2LazyInitialization {
    
    private static final Logger logger = LoggerFactory.getLogger(Problem2LazyInitialization.class);
    
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        
        try {
            logger.info("🔄 === LAZY INITIALIZATION EXCEPTION ===\n");
            
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Department.class);
            sessionFactory = configuration.buildSessionFactory();
            
            createSampleData(sessionFactory);
            
            // ============================================
            // PROBLEM: LazyInitializationException
            // ============================================
            logger.info("❌ === PROBLEM: LAZY INITIALIZATION ===\n");
            
            Department dept = null;
            Session session1 = sessionFactory.openSession();
            session1.beginTransaction();
            
            logger.info("1. Fetching department (employees are lazy-loaded)...");
            dept = session1.get(Department.class, 1L);
            logger.info("   ✅ Department loaded: {}\n", dept.getName());
            
            session1.getTransaction().commit();
            session1.close();
            logger.info("2. Session closed.\n");
            
            try {
                logger.info("3. Trying to access employees after session closed...");
                int empCount = dept.getEmployees().size(); // This will fail!
                logger.info("   Employee count: {}\n", empCount);
            } catch (LazyInitializationException e) {
                logger.error("   ❌ LazyInitializationException: {}", e.getMessage());
                logger.error("   Could not initialize lazy collection - no session available\n");
            }
            
            // ============================================
            // SOLUTION 1: Initialize before closing session
            // ============================================
            logger.info("✅ === SOLUTION 1: Initialize before closing ===\n");
            
            Session session2 = sessionFactory.openSession();
            session2.beginTransaction();
            
            Department dept2 = session2.get(Department.class, 1L);
            logger.info("1. Fetching department...");
            
            // Initialize the collection while session is open
            logger.info("2. Initializing employees collection...");
            org.hibernate.Hibernate.initialize(dept2.getEmployees());
            logger.info("   ✅ Collection initialized\n");
            
            session2.getTransaction().commit();
            session2.close();
            logger.info("3. Session closed.\n");
            
            logger.info("4. Accessing employees after session closed (now works)...");
            int empCount = dept2.getEmployees().size();
            logger.info("   ✅ Employee count: {}\n", empCount);
            
            // ============================================
            // SOLUTION 2: Use JOIN FETCH
            // ============================================
            logger.info("✅ === SOLUTION 2: JOIN FETCH ===\n");
            
            Session session3 = sessionFactory.openSession();
            session3.beginTransaction();
            
            logger.info("1. Fetching department with employees using JOIN FETCH...");
            Department dept3 = session3.createQuery(
                    "SELECT d FROM Department d JOIN FETCH d.employees WHERE d.id = :id",
                    Department.class)
                    .setParameter("id", 1L)
                    .uniqueResult();
            logger.info("   ✅ Department and employees loaded together\n");
            
            session3.getTransaction().commit();
            session3.close();
            
            logger.info("2. Accessing employees after session closed (works because eagerly loaded)...");
            int empCount3 = dept3.getEmployees().size();
            logger.info("   ✅ Employee count: {}\n", empCount3);
            
            logger.info("✅ LazyInitializationException solutions demonstrated!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
    
    private static void createSampleData(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        
        Department dept = new Department("IT", "Building A");
        session.save(dept);
        
        com.harshit.hibernate.intermediate.entity.Employee emp1 = 
                new com.harshit.hibernate.intermediate.entity.Employee("John", "Doe", "john@company.com");
        com.harshit.hibernate.intermediate.entity.Employee emp2 = 
                new com.harshit.hibernate.intermediate.entity.Employee("Jane", "Smith", "jane@company.com");
        
        dept.addEmployee(emp1);
        dept.addEmployee(emp2);
        
        session.save(emp1);
        session.save(emp2);
        
        session.getTransaction().commit();
        session.close();
    }
}


package com.harshit.hibernate.realworld;

import com.harshit.hibernate.intermediate.entity.Department;
import com.harshit.hibernate.intermediate.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * REAL-WORLD PROBLEM 1: N+1 Query Problem
 * 
 * PROBLEM:
 * When fetching a collection of entities and their relationships,
 * Hibernate executes 1 query for the parent entities and N queries
 * for each child entity, resulting in N+1 queries total.
 * 
 * EXAMPLE:
 * Fetching 10 departments with their employees:
 * - 1 query to get all departments
 * - 10 queries (one per department) to get employees
 * - Total: 11 queries instead of 1!
 * 
 * SOLUTION:
 * 1. Use JOIN FETCH in HQL
 * 2. Use @BatchSize annotation
 * 3. Use fetch join in Criteria API
 * 4. Configure fetch strategy at entity level
 * 
 * REAL-WORLD SCENARIO:
 * In an e-commerce application, displaying a product list with categories.
 * Without proper fetching, this can cause hundreds of database queries
 * and severe performance issues.
 */
public class Problem1NPlusOne {
    
    private static final Logger logger = LoggerFactory.getLogger(Problem1NPlusOne.class);
    
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        
        try {
            logger.info("🔄 === N+1 QUERY PROBLEM ===\n");
            
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Department.class);
            configuration.addAnnotatedClass(Employee.class);
            sessionFactory = configuration.buildSessionFactory();
            
            createSampleData(sessionFactory);
            
            // ============================================
            // PROBLEM: N+1 Query Issue
            // ============================================
            logger.info("❌ === PROBLEM: N+1 QUERIES ===\n");
            
            Session session1 = sessionFactory.openSession();
            session1.beginTransaction();
            
            logger.info("Fetching all departments (will trigger N+1 queries)...");
            List<Department> departments = session1.createQuery(
                    "FROM Department", Department.class).list();
            
            logger.info("Now accessing employees for each department...");
            for (Department dept : departments) {
                // This triggers a separate query for each department!
                logger.info("Department: {} has {} employees", 
                        dept.getName(), dept.getEmployees().size());
            }
            logger.info("⚠️  Problem: Executed {} queries (1 for departments + {} for employees)\n",
                    departments.size() + 1, departments.size());
            
            session1.getTransaction().commit();
            session1.close();
            
            // ============================================
            // SOLUTION 1: JOIN FETCH
            // ============================================
            logger.info("✅ === SOLUTION 1: JOIN FETCH ===\n");
            
            Session session2 = sessionFactory.openSession();
            session2.beginTransaction();
            
            logger.info("Using JOIN FETCH to load departments with employees in one query...");
            List<Department> deptsWithEmployees = session2.createQuery(
                    "SELECT DISTINCT d FROM Department d JOIN FETCH d.employees", 
                    Department.class).list();
            
            logger.info("Accessing employees (already loaded, no additional queries)...");
            for (Department dept : deptsWithEmployees) {
                logger.info("Department: {} has {} employees", 
                        dept.getName(), dept.getEmployees().size());
            }
            logger.info("✅ Solution: Executed only 1 query using JOIN FETCH\n");
            
            session2.getTransaction().commit();
            session2.close();
            
            // ============================================
            // SOLUTION 2: @BatchSize Annotation
            // ============================================
            logger.info("✅ === SOLUTION 2: @BatchSize ===\n");
            logger.info("Add @BatchSize(size = 10) to @OneToMany annotation:");
            logger.info("@OneToMany(mappedBy = \"department\")");
            logger.info("@BatchSize(size = 10)");
            logger.info("private List<Employee> employees;");
            logger.info("");
            logger.info("This loads employees in batches of 10, reducing queries significantly.\n");
            
            logger.info("✅ N+1 Problem solutions demonstrated!");
            
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
        
        // Create multiple departments with employees
        for (int i = 1; i <= 5; i++) {
            Department dept = new Department("Department " + i, "Location " + i);
            session.save(dept);
            
            for (int j = 1; j <= 3; j++) {
                Employee emp = new Employee("Employee", "E" + j, "emp" + j + "@dept" + i + ".com");
                dept.addEmployee(emp);
                session.save(emp);
            }
        }
        
        session.getTransaction().commit();
        session.close();
    }
}


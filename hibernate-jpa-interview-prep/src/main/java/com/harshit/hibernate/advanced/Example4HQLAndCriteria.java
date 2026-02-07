package com.harshit.hibernate.advanced;

import com.harshit.hibernate.intermediate.entity.Department;
import com.harshit.hibernate.intermediate.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Example 4: HQL (Hibernate Query Language) and Criteria API
 * 
 * INTERVIEW QUESTION: What is HQL and how is it different from SQL?
 * 
 * ANSWER:
 * HQL (Hibernate Query Language) is an object-oriented query language similar to SQL,
 * but it works with entities and their properties instead of tables and columns.
 * 
 * KEY DIFFERENCES:
 * - HQL uses entity names and properties (FROM Employee WHERE salary > 50000)
 * - SQL uses table names and columns (SELECT * FROM employees WHERE salary > 50000)
 * - HQL is database-independent
 * - HQL supports polymorphism
 * 
 * CRITERIA API:
 * - Type-safe, programmatic way to build queries
 * - Compile-time checking
 * - Better for dynamic queries
 * 
 * REAL-WORLD SCENARIO:
 * In a reporting system, you need to:
 * - Find employees by salary range
 * - Calculate average salary per department
 * - Find departments with more than N employees
 * - Generate dynamic reports based on user filters
 */
public class Example4HQLAndCriteria {
    
    private static final Logger logger = LoggerFactory.getLogger(Example4HQLAndCriteria.class);
    
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        Session session = null;
        
        try {
            logger.info("🔄 === HQL AND CRITERIA API ===\n");
            
            // Setup
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Department.class);
            configuration.addAnnotatedClass(Employee.class);
            sessionFactory = configuration.buildSessionFactory();
            session = sessionFactory.openSession();
            
            // Create sample data
            createSampleData(session);
            
            session.beginTransaction();
            
            // ============================================
            // HQL EXAMPLES
            // ============================================
            logger.info("📝 === HQL QUERIES ===\n");
            
            // 1. Simple SELECT
            logger.info("1. Simple SELECT query...");
            Query<Employee> query1 = session.createQuery(
                    "FROM Employee WHERE salary > :minSalary", Employee.class);
            query1.setParameter("minSalary", new BigDecimal("70000"));
            List<Employee> highEarners = query1.list();
            logger.info("   ✅ Found {} employees with salary > $70,000\n", highEarners.size());
            
            // 2. SELECT with JOIN
            logger.info("2. SELECT with JOIN...");
            Query<Employee> query2 = session.createQuery(
                    "SELECT e FROM Employee e JOIN e.department d WHERE d.name = :deptName", 
                    Employee.class);
            query2.setParameter("deptName", "IT");
            List<Employee> itEmployees = query2.list();
            logger.info("   ✅ Found {} employees in IT department\n", itEmployees.size());
            
            // 3. Aggregate functions
            logger.info("3. Aggregate functions (AVG, COUNT, MAX)...");
            Double avgSalary = session.createQuery(
                    "SELECT AVG(e.salary) FROM Employee e", Double.class)
                    .uniqueResult();
            logger.info("   ✅ Average salary: ${}\n", avgSalary);
            
            Long empCount = session.createQuery(
                    "SELECT COUNT(e) FROM Employee e", Long.class)
                    .uniqueResult();
            logger.info("   ✅ Total employees: {}\n", empCount);
            
            BigDecimal maxSalary = session.createQuery(
                    "SELECT MAX(e.salary) FROM Employee e", BigDecimal.class)
                    .uniqueResult();
            logger.info("   ✅ Maximum salary: ${}\n", maxSalary);
            
            // 4. GROUP BY
            logger.info("4. GROUP BY (employees per department)...");
            List<Object[]> deptStats = session.createQuery(
                    "SELECT d.name, COUNT(e), AVG(e.salary) " +
                    "FROM Department d LEFT JOIN d.employees e " +
                    "GROUP BY d.name", Object[].class)
                    .list();
            logger.info("   ✅ Department statistics:");
            deptStats.forEach(stat -> 
                logger.info("      - {}: {} employees, Avg salary: ${}", 
                    stat[0], stat[1], stat[2])
            );
            logger.info("");
            
            // 5. Subquery
            logger.info("5. Subquery (employees above average salary)...");
            List<Employee> aboveAvg = session.createQuery(
                    "SELECT e FROM Employee e " +
                    "WHERE e.salary > (SELECT AVG(emp.salary) FROM Employee emp)", 
                    Employee.class)
                    .list();
            logger.info("   ✅ Found {} employees above average salary\n", aboveAvg.size());
            
            // 6. Named Query (would be defined in entity with @NamedQuery)
            logger.info("6. Pagination example...");
            Query<Employee> paginatedQuery = session.createQuery(
                    "FROM Employee ORDER BY salary DESC", Employee.class);
            paginatedQuery.setFirstResult(0); // offset
            paginatedQuery.setMaxResults(3);  // limit
            List<Employee> top3 = paginatedQuery.list();
            logger.info("   ✅ Top 3 employees by salary:");
            top3.forEach(emp -> 
                logger.info("      - {}: ${}", emp.getFirstName(), emp.getSalary())
            );
            logger.info("");
            
            session.getTransaction().commit();
            
            // ============================================
            // CRITERIA API EXAMPLES
            // ============================================
            logger.info("📝 === CRITERIA API ===\n");
            
            session.beginTransaction();
            
            logger.info("1. Criteria query (employees with salary > $70,000)...");
            javax.persistence.criteria.CriteriaBuilder cb = session.getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            javax.persistence.criteria.Root<Employee> root = cq.from(Employee.class);
            
            cq.select(root).where(cb.gt(root.get("salary"), new BigDecimal("70000")));
            Query<Employee> criteriaQuery = session.createQuery(cq);
            List<Employee> criteriaResults = criteriaQuery.list();
            logger.info("   ✅ Found {} employees using Criteria API\n", criteriaResults.size());
            
            session.getTransaction().commit();
            
            logger.info("✅ HQL and Criteria API examples completed!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
    
    private static void createSampleData(Session session) {
        session.beginTransaction();
        
        Department it = new Department("IT", "Building A");
        Department hr = new Department("HR", "Building B");
        Department finance = new Department("Finance", "Building C");
        
        session.save(it);
        session.save(hr);
        session.save(finance);
        
        Employee e1 = new Employee("John", "Doe", "john@company.com");
        e1.setSalary(new BigDecimal("75000"));
        it.addEmployee(e1);
        
        Employee e2 = new Employee("Jane", "Smith", "jane@company.com");
        e2.setSalary(new BigDecimal("80000"));
        it.addEmployee(e2);
        
        Employee e3 = new Employee("Bob", "Johnson", "bob@company.com");
        e3.setSalary(new BigDecimal("65000"));
        hr.addEmployee(e3);
        
        Employee e4 = new Employee("Alice", "Williams", "alice@company.com");
        e4.setSalary(new BigDecimal("90000"));
        finance.addEmployee(e4);
        
        session.save(e1);
        session.save(e2);
        session.save(e3);
        session.save(e4);
        
        session.getTransaction().commit();
    }
}


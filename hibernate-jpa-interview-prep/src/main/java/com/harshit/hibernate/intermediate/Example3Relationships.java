package com.harshit.hibernate.intermediate;

import com.harshit.hibernate.intermediate.entity.Department;
import com.harshit.hibernate.intermediate.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Example 3: Entity Relationships (One-to-Many, Many-to-One)
 * 
 * INTERVIEW QUESTION: Explain different types of relationships in Hibernate.
 * 
 * ANSWER:
 * 1. One-to-Many: One parent has many children (Department -> Employees)
 * 2. Many-to-One: Many children belong to one parent (Employees -> Department)
 * 3. One-to-One: One-to-one relationship (User -> Profile)
 * 4. Many-to-Many: Many-to-many relationship (Students <-> Courses)
 * 
 * KEY CONCEPTS:
 * - Owning Side: Has foreign key column (@ManyToOne, @JoinColumn)
 * - Inverse Side: No foreign key (@OneToMany with mappedBy)
 * - Cascade: Operations propagate to related entities
 * - Fetch Type: LAZY (default for @OneToMany) vs EAGER (default for @ManyToOne)
 * 
 * REAL-WORLD SCENARIO:
 * In an HR system:
 * - A department has many employees (One-to-Many)
 * - An employee belongs to one department (Many-to-One)
 * - When deleting a department, decide: cascade delete employees or reassign them
 */
public class Example3Relationships {
    
    private static final Logger logger = LoggerFactory.getLogger(Example3Relationships.class);
    
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        Session session = null;
        
        try {
            logger.info("🔄 === HIBERNATE RELATIONSHIPS ===\n");
            
            // Setup
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Department.class);
            configuration.addAnnotatedClass(Employee.class);
            sessionFactory = configuration.buildSessionFactory();
            session = sessionFactory.openSession();
            
            session.beginTransaction();
            
            // ============================================
            // CREATE RELATIONSHIPS
            // ============================================
            logger.info("📝 === CREATING RELATIONSHIPS ===\n");
            
            // Create Department
            logger.info("1. Creating department...");
            Department itDepartment = new Department("IT", "Building A, Floor 3");
            session.save(itDepartment);
            logger.info("   ✅ Department created: {}\n", itDepartment);
            
            // Create Employees and associate with Department
            logger.info("2. Creating employees and associating with department...");
            Employee emp1 = new Employee("John", "Doe", "john.doe@company.com");
            emp1.setSalary(new BigDecimal("75000.00"));
            itDepartment.addEmployee(emp1);
            
            Employee emp2 = new Employee("Jane", "Smith", "jane.smith@company.com");
            emp2.setSalary(new BigDecimal("80000.00"));
            itDepartment.addEmployee(emp2);
            
            Employee emp3 = new Employee("Bob", "Johnson", "bob.johnson@company.com");
            emp3.setSalary(new BigDecimal("70000.00"));
            itDepartment.addEmployee(emp3);
            
            // Save employees (cascade will handle department relationship)
            session.save(emp1);
            session.save(emp2);
            session.save(emp3);
            logger.info("   ✅ {} employees created and associated with IT department\n", 
                    itDepartment.getEmployees().size());
            
            // Create another department
            Department hrDepartment = new Department("HR", "Building B, Floor 2");
            session.save(hrDepartment);
            
            Employee hrEmp = new Employee("Alice", "Williams", "alice.williams@company.com");
            hrEmp.setSalary(new BigDecimal("65000.00"));
            hrDepartment.addEmployee(hrEmp);
            session.save(hrEmp);
            
            session.getTransaction().commit();
            
            // ============================================
            // READ RELATIONSHIPS
            // ============================================
            logger.info("📖 === READING RELATIONSHIPS ===\n");
            
            session.beginTransaction();
            
            // Fetch department with employees (LAZY loading)
            logger.info("1. Fetching department with employees (LAZY)...");
            Department dept = session.get(Department.class, itDepartment.getId());
            logger.info("   Department: {}", dept.getName());
            
            // Access employees (triggers LAZY load)
            logger.info("   Employees in {} department:", dept.getName());
            dept.getEmployees().forEach(emp -> 
                logger.info("      - {} {} (${})", 
                    emp.getFirstName(), emp.getLastName(), emp.getSalary())
            );
            logger.info("");
            
            // Fetch employee with department (EAGER loading)
            logger.info("2. Fetching employee with department (EAGER)...");
            Employee employee = session.get(Employee.class, emp1.getId());
            logger.info("   Employee: {}", employee);
            logger.info("   Department: {} (loaded automatically)\n", 
                    employee.getDepartment().getName());
            
            // Query with JOIN
            logger.info("3. Querying employees with department using HQL...");
            List<Employee> employeesWithDept = session.createQuery(
                    "SELECT e FROM Employee e JOIN FETCH e.department", Employee.class)
                    .list();
            logger.info("   ✅ Found {} employees with department info\n", 
                    employeesWithDept.size());
            
            session.getTransaction().commit();
            
            // ============================================
            // UPDATE RELATIONSHIPS
            // ============================================
            logger.info("✏️  === UPDATING RELATIONSHIPS ===\n");
            
            session.beginTransaction();
            
            // Move employee to different department
            logger.info("1. Moving employee to different department...");
            Employee empToMove = session.get(Employee.class, emp2.getId());
            Department newDept = session.get(Department.class, hrDepartment.getId());
            empToMove.setDepartment(newDept);
            session.update(empToMove);
            logger.info("   ✅ {} moved from {} to {}\n", 
                    empToMove.getFirstName(), 
                    itDepartment.getName(), 
                    newDept.getName());
            
            session.getTransaction().commit();
            
            logger.info("✅ Relationship operations completed successfully!");
            
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
}


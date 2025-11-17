package com.harshit.inheritance.advanced;

import java.util.List;
import java.util.ArrayList;

/**
 * ASSOCIATION TYPES - Interview Explanation:
 * 
 * In Object-Oriented Programming, there are three types of relationships:
 * 
 * 1. ASSOCIATION: "Uses" relationship - objects can exist independently
 * 2. AGGREGATION: "Has-a" relationship - weak ownership, part can exist without whole
 * 3. COMPOSITION: "Part-of" relationship - strong ownership, part cannot exist without whole
 * 
 * Technical Definition:
 * - Association: Objects are related but independent
 * - Aggregation: Weak "has-a" relationship (hollow diamond in UML)
 * - Composition: Strong "has-a" relationship (filled diamond in UML)
 * 
 * Simple Explanation:
 * - Association: "Uses" - like Student uses Library
 * - Aggregation: "Has" - like Department has Employees (employees can exist without department)
 * - Composition: "Owns" - like House has Rooms (rooms cannot exist without house)
 */
public class AssociationTypes {

    public static void main(String[] args) {
        System.out.println("=== ASSOCIATION TYPES ===\n");
        
        demonstrateAssociation();
        demonstrateAggregation();
        demonstrateComposition();
        demonstrateComparison();
    }
    
    /**
     * Interview Point: ASSOCIATION
     * 
     * Technical Explanation:
     * - Association is a relationship where objects are related but can exist independently
     * - It represents a "uses" or "knows about" relationship
     * - There is no ownership between the objects - neither owns the other
     * - Both objects have their own independent lifecycle - one can exist without the other
     * - In UML, it's represented by a simple line between classes
     * - It's the weakest form of relationship - just a connection, no ownership
     * - Objects can be associated temporarily or permanently
     * 
     * Simple Explanation:
     * - Think of it like a person using a tool - the person and tool are separate things
     * - The person can use the tool, but doesn't own it, and both can exist independently
     * - Like a Student using a Library - student can use library, but student doesn't own library,
     *   and both student and library can exist without each other
     * - It's like a friendship - two people know each other and interact, but are independent
     * 
     * Interview Answer:
     * "Association is a relationship where objects are related but independent. It represents
     * a 'uses' relationship with no ownership. Both objects have their own lifecycle and can
     * exist without each other. For example, a Student uses a Library - they're related (student
     * borrows books), but student doesn't own library and both can exist independently. In code,
     * this is typically represented by one object having a reference to another object, but not
     * creating or owning it."
     */
    private static void demonstrateAssociation() {
        System.out.println("--- ASSOCIATION (Uses Relationship) ---");
        
        // Interview Point: Objects created independently
        // Student and Library are created separately - neither creates or owns the other
        // They are independent entities that can exist on their own
        Student student = new Student("John", 101);
        Library library = new Library("Central Library");
        
        // Interview Point: Student uses Library (association)
        // Student has a method that takes Library as parameter - this is association
        // Student uses Library, but doesn't own it. Library is passed in, not created by Student
        // Both objects exist independently - if Student is deleted, Library still exists
        // If Library is deleted, Student still exists (just can't borrow books anymore)
        student.borrowBook(library, "Java Programming");
        
        System.out.println("  Characteristics:");
        System.out.println("    → Objects are independent - can exist separately");
        System.out.println("    → No ownership - neither owns the other");
        System.out.println("    → 'Uses' relationship - one object uses another");
        System.out.println("    → Both have their own lifecycle - one can be deleted without affecting other");
        System.out.println("    → Weakest relationship - just a connection, no dependency");
        System.out.println("    → In UML: Simple line (no diamond)");
        System.out.println();
    }
    
    /**
     * Interview Point: AGGREGATION
     * 
     * Technical Explanation:
     * - Aggregation is a weak "has-a" relationship where the whole contains parts
     * - The part can exist independently of the whole - if the whole is deleted, parts still exist
     * - The whole doesn't own the parts - it just contains references to them
     * - Parts can belong to multiple wholes (like an employee can be in multiple departments)
     * - In UML, it's represented by a line with a hollow diamond on the whole side
     * - It's stronger than association (there's a container relationship) but weaker than composition
     * - The whole is responsible for managing the collection of parts, but doesn't create or destroy them
     * 
     * Simple Explanation:
     * - Think of it like a department having employees - department "has" employees
     * - But employees can exist without the department - if department closes, employees still exist
     * - Employees can also belong to multiple departments (part-time in two departments)
     * - It's like a container and its contents - container holds contents, but contents can exist
     *   outside the container
     * - Like a shopping cart has items - items exist independently, cart just holds them
     * 
     * Interview Answer:
     * "Aggregation is a weak 'has-a' relationship where the whole contains parts. The key
     * characteristic is that parts can exist independently of the whole. For example, a
     * Department has Employees, but if the department is deleted, employees still exist.
     * Employees can also belong to multiple departments. The whole doesn't own the parts -
     * it just contains references to them. In UML, this is shown with a hollow diamond on
     * the whole side. It's stronger than association but weaker than composition."
     */
    private static void demonstrateAggregation() {
        System.out.println("--- AGGREGATION (Weak Has-a Relationship) ---");
        
        // Interview Point: Parts can exist independently
        // Employees are created independently - they don't need a department to exist
        // This demonstrates that parts have their own lifecycle
        Employee emp1 = new Employee("Alice", "E001");
        Employee emp2 = new Employee("Bob", "E002");
        
        // Interview Point: Whole contains parts
        // Department is created, then employees are added to it
        // Department doesn't create employees - it just holds references to them
        // This is aggregation - department "has" employees, but doesn't own them
        Department dept = new Department("Engineering");
        dept.addEmployee(emp1);  // Adding existing employee to department
        dept.addEmployee(emp2);  // Adding existing employee to department
        
        dept.showEmployees();
        
        // Interview Point: Parts can exist without whole
        // We can create another department and add the same employee
        // This shows that employees can exist independently and can belong to multiple departments
        // If we delete the first department, employees still exist
        Department dept2 = new Department("Marketing");
        dept2.addEmployee(emp1); // Same employee in different department - part can belong to multiple wholes
        
        System.out.println("  Characteristics:");
        System.out.println("    → Weak ownership - whole doesn't own parts");
        System.out.println("    → Part can exist without whole - independent lifecycle");
        System.out.println("    → Part can belong to multiple wholes - shared ownership");
        System.out.println("    → Whole contains references to parts, doesn't create them");
        System.out.println("    → Hollow diamond in UML (on whole side)");
        System.out.println("    → Stronger than association, weaker than composition");
        System.out.println();
    }
    
    /**
     * Interview Point: COMPOSITION
     * 
     * Technical Explanation:
     * - Composition is a strong "has-a" relationship where the whole owns the parts
     * - The part cannot exist without the whole - if the whole is deleted, parts are also deleted
     * - The whole is responsible for creating and destroying the parts
     * - Parts are tightly coupled to the whole - they have no independent existence
     * - In UML, it's represented by a line with a filled (solid) diamond on the whole side
     * - It's the strongest form of "has-a" relationship
     * - The whole controls the lifecycle of the parts - parts are created when whole is created,
     *   and destroyed when whole is destroyed
     * 
     * Simple Explanation:
     * - Think of it like a house and its rooms - house "owns" the rooms
     * - Rooms cannot exist without the house - if house is demolished, rooms are gone too
     * - House creates the rooms (when house is built, rooms are created)
     * - It's like a body and its organs - organs cannot exist without the body
     * - Like a car and its engine - engine is part of car, can't exist independently
     * - Strong ownership - whole completely controls parts
     * 
     * Interview Answer:
     * "Composition is a strong 'has-a' relationship where the whole owns the parts. The key
     * characteristic is that parts cannot exist without the whole. For example, a House has
     * Rooms, but rooms cannot exist independently - if the house is destroyed, rooms are
     * destroyed too. The whole creates and controls the lifecycle of parts. In UML, this
     * is shown with a filled diamond on the whole side. It's the strongest relationship -
     * parts are tightly coupled to the whole and have no independent existence."
     */
    private static void demonstrateComposition() {
        System.out.println("--- COMPOSITION (Strong Has-a Relationship) ---");
        
        // Interview Point: Whole creates and owns parts
        // House is created first - it will create and own its rooms
        // House controls the lifecycle of rooms
        House house = new House("123 Main St");
        
        // Interview Point: Parts are created inside whole
        // When we call addRoom(), the House creates Room objects internally
        // Rooms are created by House, owned by House, and cannot exist without House
        // This is composition - House creates and owns Rooms
        house.addRoom("Living Room", 200);  // House creates this room
        house.addRoom("Bedroom", 150);      // House creates this room
        house.addRoom("Kitchen", 100);      // House creates this room
        
        house.showRooms();
        
        // Interview Point: Parts cannot exist without whole
        // We cannot meaningfully create a Room without a House
        // Even if we could create a Room object, it wouldn't make sense in isolation
        // Rooms are part of a house - they have no independent existence
        // Room room = new Room("Test", 50); // Not meaningful without house
        // This would be conceptually wrong - rooms are always part of a house
        
        System.out.println("  Characteristics:");
        System.out.println("    → Strong ownership - whole owns parts completely");
        System.out.println("    → Part cannot exist without whole - no independent lifecycle");
        System.out.println("    → Part's lifecycle depends on whole - created/destroyed with whole");
        System.out.println("    → Whole creates and destroys parts");
        System.out.println("    → Filled diamond in UML (on whole side)");
        System.out.println("    → Strongest relationship - tight coupling");
        System.out.println("    → If whole is deleted, parts are also deleted");
        System.out.println();
    }
    
    /**
     * Interview Point: Comparison of all three types
     */
    private static void demonstrateComparison() {
        System.out.println("--- COMPARISON ---");
        System.out.println();
        System.out.println("  ASSOCIATION:");
        System.out.println("    → Independent objects");
        System.out.println("    → 'Uses' relationship");
        System.out.println("    → Example: Student uses Library");
        System.out.println();
        System.out.println("  AGGREGATION:");
        System.out.println("    → Weak ownership");
        System.out.println("    → Part can exist without whole");
        System.out.println("    → Example: Department has Employees");
        System.out.println();
        System.out.println("  COMPOSITION:");
        System.out.println("    → Strong ownership");
        System.out.println("    → Part cannot exist without whole");
        System.out.println("    → Example: House has Rooms");
        System.out.println();
    }
}

// ==================== ASSOCIATION EXAMPLE ====================

/**
 * Interview Point: Association - Student uses Library
 * Both are independent
 */
class Student {
    private String name;
    private int studentId;
    
    public Student(String name, int studentId) {
        this.name = name;
        this.studentId = studentId;
    }
    
    // Interview Point: Student uses Library (association)
    public void borrowBook(Library library, String bookName) {
        System.out.println("  " + name + " borrowing " + bookName + " from " + library.getName());
        library.issueBook(bookName);
    }
}

class Library {
    private String name;
    
    public Library(String name) {
        this.name = name;
    }
    
    public void issueBook(String bookName) {
        System.out.println("  Library issuing: " + bookName);
    }
    
    public String getName() {
        return name;
    }
}

// ==================== AGGREGATION EXAMPLE ====================

/**
 * Interview Point: Aggregation - Department has Employees
 * Employees can exist without department
 */
class Department {
    private String name;
    private List<Employee> employees; // Interview Point: Weak reference
    
    public Department(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
    }
    
    // Interview Point: Adding employee (weak ownership)
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
    
    public void showEmployees() {
        System.out.println("  Department: " + name);
        for (Employee emp : employees) {
            System.out.println("    Employee: " + emp.getName());
        }
    }
}

class Employee {
    private String name;
    private String employeeId;
    
    public Employee(String name, String employeeId) {
        this.name = name;
        this.employeeId = employeeId;
    }
    
    // Interview Point: Employee can exist independently
    public String getName() {
        return name;
    }
}

// ==================== COMPOSITION EXAMPLE ====================

/**
 * Interview Point: Composition - House has Rooms
 * Rooms cannot exist without house
 */
class House {
    private String address;
    private List<Room> rooms; // Interview Point: Strong ownership
    
    public House(String address) {
        this.address = address;
        this.rooms = new ArrayList<>();
    }
    
    // Interview Point: Room created and owned by House
    public void addRoom(String name, int area) {
        Room room = new Room(name, area); // Interview Point: Created inside
        rooms.add(room);
    }
    
    public void showRooms() {
        System.out.println("  House at: " + address);
        for (Room room : rooms) {
            System.out.println("    Room: " + room.getName() + " (" + room.getArea() + " sq ft)");
        }
    }
}

class Room {
    private String name;
    private int area;
    
    // Interview Point: Room is part of House (composition)
    public Room(String name, int area) {
        this.name = name;
        this.area = area;
    }
    
    public String getName() {
        return name;
    }
    
    public int getArea() {
        return area;
    }
}

/**
 * INTERVIEW SUMMARY: Association Types
 * 
 * Key Points:
 * 1. ASSOCIATION: Independent objects, "uses" relationship
 * 2. AGGREGATION: Weak ownership, part can exist without whole
 * 3. COMPOSITION: Strong ownership, part cannot exist without whole
 * 
 * UML Notation:
 * - Association: Simple line
 * - Aggregation: Hollow diamond
 * - Composition: Filled diamond
 * 
 * Lifecycle:
 * - Association: Independent
 * - Aggregation: Part independent of whole
 * - Composition: Part depends on whole
 */


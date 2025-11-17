package com.harshit.inheritance.basics;

/**
 * BASIC INHERITANCE - Interview Explanation:
 * 
 * Inheritance: Mechanism where one class acquires properties and methods of another class.
 * 
 * Technical Definition:
 * - Allows a class (child/subclass) to inherit fields and methods from another class (parent/superclass)
 * - Promotes code reusability
 * - Establishes "is-a" relationship
 * - Uses 'extends' keyword in Java
 * 
 * Simple Explanation:
 * - Like a child inheriting traits from parents
 * - Child class gets everything from parent class
 * - Can add new features or modify existing ones
 * - "Dog is an Animal" relationship
 * 
 * Key Points:
 * - Java supports single inheritance (one parent only)
 * - Child can access parent's public and protected members
 * - Private members are not inherited
 */
public class BasicInheritance {

    public static void main(String[] args) {
        System.out.println("=== BASIC INHERITANCE ===\n");
        
        demonstrateBasicInheritance();
        demonstrateAccessModifiers();
        demonstrateInheritanceHierarchy();
    }
    
    /**
     * Interview Point: Basic Inheritance Example
     * 
     * Technical Explanation:
     * - When a child class extends a parent class, it automatically inherits all public and protected
     *   members (fields and methods) from the parent class
     * - The child class can use these inherited members as if they were defined in the child class itself
     * - The child class can also add its own new members (fields and methods) that are specific to it
     * - This promotes code reusability - you don't need to rewrite code that's already in the parent class
     * 
     * Simple Explanation:
     * - Think of inheritance like a family tree - a child inherits traits from parents
     * - Dog class inherits from Animal class, so Dog gets all the abilities of Animal (like eat, sleep)
     * - Dog can also have its own unique abilities (like bark) that Animal doesn't have
     * - It's like saying "Dog is a type of Animal" - this is called "is-a" relationship
     * 
     * Interview Answer:
     * "Inheritance allows a child class to inherit properties and methods from a parent class.
     * For example, Dog extends Animal, so Dog automatically gets methods like eat() and sleep()
     * from Animal. Dog can also add its own methods like bark(). This promotes code reusability
     * and establishes an 'is-a' relationship - Dog is an Animal."
     */
    private static void demonstrateBasicInheritance() {
        System.out.println("--- Basic Inheritance ---");
        
        // Interview Point: Create child class object
        // When we create a Dog object, it has access to both Animal's methods and Dog's own methods
        // This is because Dog extends Animal, so it inherits everything from Animal
        Dog dog = new Dog("Buddy", "Golden Retriever");
        
        // Interview Point: Can call parent class methods
        // These methods are not defined in Dog class, but Dog can use them because they're inherited
        // from Animal class. This demonstrates code reusability - we don't need to rewrite eat() and sleep()
        // in Dog class, we just inherit them from Animal
        dog.eat();      // Inherited from Animal - Dog can use Animal's eat() method
        dog.sleep();    // Inherited from Animal - Dog can use Animal's sleep() method
        
        // Interview Point: Can call child class specific methods
        // This method is defined only in Dog class, not in Animal. This shows that child class
        // can have its own unique methods in addition to inherited ones
        dog.bark();     // Specific to Dog - This method exists only in Dog class, not in Animal
        
        // Interview Point: Can access parent class fields (if accessible)
        // The 'name' field is defined in Animal class with 'protected' access, so Dog can access it
        // getName() method is also inherited from Animal, so we can call it on Dog object
        System.out.println("  Dog name: " + dog.getName()); // Inherited field - name comes from Animal class
        
        System.out.println();
    }
    
    /**
     * Interview Point: Access Modifiers in Inheritance
     * 
     * Technical Explanation:
     * - Access modifiers control what members (fields and methods) can be inherited and accessed
     * - public: Members are inherited and can be accessed from anywhere, including child classes
     * - protected: Members are inherited and can be accessed in child classes (even in different packages)
     *   and in the same package. This is specifically designed for inheritance scenarios
     * - default (package-private): Members are inherited only if parent and child are in the same package.
     *   If in different packages, they are NOT inherited
     * - private: Members are NOT inherited at all. They remain private to the parent class only
     * 
     * Simple Explanation:
     * - Think of access modifiers like security levels in a building
     * - public: Everyone can access (like a public park)
     * - protected: Family members can access (like a family house - children can access)
     * - default: People in same building can access (like an apartment building)
     * - private: Only the owner can access (like a private room - even children can't access)
     * 
     * Interview Answer:
     * "Access modifiers determine what gets inherited. public and protected members are inherited
     * and accessible in child classes. default members are inherited only if parent and child
     * are in the same package. private members are never inherited - they remain exclusive
     * to the parent class. This is important for encapsulation - private members maintain
     * data hiding even in inheritance."
     */
    private static void demonstrateAccessModifiers() {
        System.out.println("--- Access Modifiers in Inheritance ---");
        
        Dog dog = new Dog("Max", "Labrador");
        
        // Interview Point: Public - accessible
        // Public members are always inherited and can be accessed from anywhere
        // This is the most permissive access level - child classes can freely use public members
        dog.eat(); // Public method - accessible from anywhere, including child classes
        
        // Interview Point: Protected - accessible in child class
        // Protected members are inherited and can be accessed in child classes
        // This is specifically designed for inheritance - it allows parent to share members
        // with children while still restricting access from outside the inheritance hierarchy
        dog.displayInfo(); // Uses protected method from parent - protected allows child access
        
        // Interview Point: Private - NOT accessible
        // Private members are never inherited. Even though Dog extends Animal, it cannot
        // access Animal's private members. This maintains encapsulation - private members
        // remain exclusive to the class that defines them
        // dog.privateMethod(); // Compilation error - private not inherited
        // This would cause a compilation error because privateMethod() is private in Animal
        // and private members are not accessible outside their defining class, even in child classes
        
        System.out.println();
    }
    
    /**
     * Interview Point: Inheritance Hierarchy
     * 
     * Technical: Can have multiple levels of inheritance
     * Simple: Grandparent → Parent → Child
     */
    private static void demonstrateInheritanceHierarchy() {
        System.out.println("--- Inheritance Hierarchy ---");
        
        // Interview Point: Multi-level inheritance
        Puppy puppy = new Puppy("Tiny", "Poodle");
        
        // Interview Point: Can call methods from all levels
        puppy.eat();    // From Animal (grandparent)
        puppy.sleep();  // From Animal (grandparent)
        puppy.bark();   // From Dog (parent)
        puppy.play();   // From Puppy (itself)
        
        System.out.println();
    }
}

/**
 * Interview Point: Parent Class (Superclass)
 * 
 * Technical: Base class that other classes inherit from
 * Simple: The parent that gives features to children
 */
class Animal {
    // Interview Point: Protected field - accessible in child classes
    protected String name;
    
    // Interview Point: Public constructor
    public Animal(String name) {
        this.name = name;
        System.out.println("  Animal constructor called");
    }
    
    // Interview Point: Public method - inherited by child classes
    public void eat() {
        System.out.println("  " + name + " is eating");
    }
    
    // Interview Point: Public method - inherited by child classes
    public void sleep() {
        System.out.println("  " + name + " is sleeping");
    }
    
    // Interview Point: Protected method - accessible in child classes
    protected void displayInfo() {
        System.out.println("  Animal: " + name);
    }
    
    // Interview Point: Private method - NOT inherited
    private void privateMethod() {
        System.out.println("  This is private");
    }
    
    // Interview Point: Getter method
    public String getName() {
        return name;
    }
}

/**
 * Interview Point: Child Class (Subclass)
 * 
 * Technical: Class that inherits from parent class
 * Simple: The child that gets features from parent
 * 
 * Uses 'extends' keyword to inherit
 */
class Dog extends Animal {
    private String breed;
    
    // Interview Point: Child class constructor
    // Must call parent constructor using super()
    public Dog(String name, String breed) {
        super(name); // Interview Point: Call parent constructor
        this.breed = breed;
        System.out.println("  Dog constructor called");
    }
    
    // Interview Point: Child-specific method
    public void bark() {
        System.out.println("  " + name + " is barking");
    }
    
    // Interview Point: Can access protected members from parent
    public void showDogInfo() {
        displayInfo(); // Can call protected method
        System.out.println("  Breed: " + breed);
    }
    
    public String getBreed() {
        return breed;
    }
}

/**
 * Interview Point: Multi-level Inheritance
 * 
 * Technical: Child class can also be a parent
 * Simple: Puppy inherits from Dog, Dog inherits from Animal
 */
class Puppy extends Dog {
    public Puppy(String name, String breed) {
        super(name, breed); // Interview Point: Call parent (Dog) constructor
        System.out.println("  Puppy constructor called");
    }
    
    // Interview Point: Puppy-specific method
    public void play() {
        System.out.println("  " + name + " is playing");
    }
}

/**
 * INTERVIEW SUMMARY: Basic Inheritance
 * 
 * Key Points:
 * 1. Uses 'extends' keyword
 * 2. Child inherits public and protected members
 * 3. Private members are NOT inherited
 * 4. Child can add new methods/fields
 * 5. Java supports single inheritance only
 * 6. Constructor chaining: child must call parent constructor
 * 
 * Access Modifiers:
 * - public: Inherited ✓
 * - protected: Inherited ✓
 * - default: Inherited if same package ✓
 * - private: NOT inherited ✗
 */


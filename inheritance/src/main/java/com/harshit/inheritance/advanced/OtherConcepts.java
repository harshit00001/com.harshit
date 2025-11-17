package com.harshit.inheritance.advanced;

/**
 * OTHER INHERITANCE CONCEPTS - Interview Explanation:
 * 
 * This file covers additional important inheritance concepts:
 * 
 * 1. Method Hiding (Static Methods)
 * 2. Shadowing (Fields)
 * 3. Constructor Inheritance
 * 4. Multiple Inheritance through Interfaces
 * 5. Diamond Problem
 * 6. instanceof Operator
 * 7. getClass() vs instanceof
 */
public class OtherConcepts {

    public static void main(String[] args) {
        System.out.println("=== OTHER INHERITANCE CONCEPTS ===\n");
        
        demonstrateMethodHiding();
        demonstrateFieldShadowing();
        demonstrateConstructorInheritance();
        demonstrateMultipleInheritance();
        demonstrateDiamondProblem();
        demonstrateInstanceof();
        demonstrateGetClassVsInstanceof();
    }
    
    /**
     * Interview Point: Method Hiding (Static Methods)
     * 
     * Technical Explanation:
     * - Static methods belong to the class, not to instances (objects) of the class
     * - Static methods CANNOT be overridden - they are resolved at compile time, not runtime
     * - When a child class defines a static method with the same signature as parent's static method,
     *   it's called "method hiding" (not overriding)
     * - The method call is resolved based on the REFERENCE TYPE, not the actual object type
     * - This is different from instance method overriding, where the actual object type matters
     * - Static methods are bound at compile time, instance methods are bound at runtime
     * 
     * Simple Explanation:
     * - Think of static methods as belonging to the class itself, not to individual objects
     * - When you "override" a static method, you're actually just hiding it - the parent's
     *   static method still exists, but child has its own version
     * - Which version gets called depends on what type the variable is, not what object it points to
     * - It's like having two different tools with the same name - which one you use depends on
     *   which toolbox (class) you're looking at, not which specific tool (object) you have
     * 
     * Interview Answer:
     * "Static methods cannot be overridden - they can only be hidden. When a child class defines
     * a static method with the same signature as parent's static method, it hides the parent's
     * method. The method call is resolved at compile time based on the reference type, not the
     * actual object type. This is different from instance method overriding where runtime
     * polymorphism applies. For example, if you have 'Parent p = new Child()' and call
     * p.staticMethod(), it calls Parent's static method, not Child's, because the reference
     * type is Parent."
     */
    private static void demonstrateMethodHiding() {
        System.out.println("--- Method Hiding (Static Methods) ---");
        
        // Interview Point: Create different reference types
        ParentClass parent = new ParentClass();              // Reference and object both Parent
        ParentClass childAsParent = new ChildClass();        // Reference is Parent, object is Child
        ChildClass child = new ChildClass();                  // Reference and object both Child
        
        // Interview Point: Static method call depends on reference type, NOT object type
        // This is the key difference from instance method overriding
        // When calling static methods, Java looks at the type of the variable, not the type of the object
        ParentClass.staticMethod();      // Calls Parent's static method - reference type is ParentClass
        ChildClass.staticMethod();       // Calls Child's static method - reference type is ChildClass
        
        // Interview Point: Even though childAsParent points to a Child object, it calls Parent's method
        // This is because the reference type is ParentClass, and static methods are resolved based on
        // reference type, not object type. This is different from instance methods where Child's method
        // would be called (runtime polymorphism)
        childAsParent.staticMethod();    // Calls Parent's static method - reference type is ParentClass
        // Notice: Even though the actual object is Child, Parent's static method is called
        // This demonstrates that static methods don't follow runtime polymorphism
        
        System.out.println("  → Static methods cannot be overridden (only hidden)");
        System.out.println("  → Method hiding (not overriding) - different from instance methods");
        System.out.println("  → Call depends on REFERENCE TYPE, not object type");
        System.out.println("  → Resolved at COMPILE TIME, not runtime");
        System.out.println("  → This is why static methods don't support polymorphism");
        System.out.println();
    }
    
    /**
     * Interview Point: Field Shadowing
     * 
     * Technical Explanation:
     * - When a child class declares a field with the same name as a field in the parent class,
     *   the child's field "shadows" (hides) the parent's field
     * - This is different from overriding - fields cannot be overridden, only shadowed
     * - When you access the field name directly in child class, you get the child's field
     * - To access the parent's field, you must use 'super.fieldName'
     * - Both fields exist in memory - the child's field hides the parent's field by name
     * - This can lead to confusion, so it's generally not recommended to shadow fields
     * 
     * Simple Explanation:
     * - Think of it like having two boxes with the same label - one in parent's room, one in child's room
     * - When child looks for the box, they find their own box first (child's field)
     * - To get parent's box, child must specifically say "get parent's box" (super.field)
     * - Both boxes exist, but child's box hides parent's box by default
     * - It's like having a local variable that hides an instance variable - same concept
     * 
     * Interview Answer:
     * "Field shadowing occurs when a child class declares a field with the same name as a field
     * in the parent class. The child's field hides the parent's field. When you access the field
     * name in the child class, you get the child's field. To access the parent's field, you must
     * use 'super.fieldName'. Both fields exist in memory - the child's field doesn't replace
     * the parent's field, it just hides it by name. This is different from method overriding
     * where the child's method replaces the parent's method."
     */
    private static void demonstrateFieldShadowing() {
        System.out.println("--- Field Shadowing ---");
        
        // Interview Point: Create child object that has a field shadowing parent's field
        // Both ParentClassWithField and ChildClassWithShadow have a field named 'name'
        // When we access 'name' in ChildClassWithShadow, we get child's 'name' by default
        ChildClassWithShadow child = new ChildClassWithShadow();
        
        // Interview Point: This method demonstrates both this.name (child's field) and super.name (parent's field)
        // It shows that both fields exist, but child's field is accessed by default
        child.showFields();
        // Output will show:
        // "this.name: ChildName" (child's field)
        // "super.name: ParentName" (parent's field - accessed using super)
        
        System.out.println("  → Child field shadows parent field (hides it by name)");
        System.out.println("  → Both fields exist in memory - child's doesn't replace parent's");
        System.out.println("  → Use 'this.field' or just 'field' to access child's field");
        System.out.println("  → Use 'super.field' to access parent's field");
        System.out.println("  → Fields cannot be overridden, only shadowed (different from methods)");
        System.out.println("  → Generally not recommended - can cause confusion");
        System.out.println();
    }
    
    /**
     * Interview Point: Constructor Inheritance
     * 
     * Technical Explanation:
     * - Constructors are NOT inherited from parent class to child class
     * - This is different from methods and fields which are inherited
     * - Child class must define its own constructors - it doesn't automatically get parent's constructors
     * - However, child constructor MUST call parent constructor using super()
     * - super() must be the first statement in child constructor (if used)
     * - If parent has a no-argument constructor, compiler automatically adds super() if you don't write it
     * - If parent only has parameterized constructors, you MUST explicitly call super() with arguments
     * - This ensures proper initialization order - parent is initialized before child
     * - This is called "constructor chaining" - parent initializes first, then child
     * 
     * Simple Explanation:
     * - Think of constructors as special methods that are not passed down
     * - Child doesn't automatically get parent's constructors
     * - But child must initialize parent first before initializing itself
     * - It's like building a house - you must build foundation (parent) before walls (child)
     * - Child must create its own constructors, but must call parent's constructor first
     * - Like saying "initialize parent first, then initialize me"
     * - If parent needs information (like a name), you pass it via super(name)
     * 
     * Interview Answer:
     * "Constructors are not inherited. Child class must define its own constructors. However,
     * child constructor must call parent constructor using super() as the first statement.
     * This ensures parent is initialized before child. If parent has no-arg constructor, compiler
     * adds super() automatically. If parent only has parameterized constructors, you must
     * explicitly call super() with arguments. This is constructor chaining - ensuring proper
     * initialization order up the inheritance hierarchy."
     */
    private static void demonstrateConstructorInheritance() {
        System.out.println("--- Constructor Inheritance ---");
        
        // Interview Point: Child must define its own constructor
        // ChildWithConstructor doesn't inherit ParentWithConstructor's constructor
        // It must define its own constructor, but must call parent's constructor first
        // When this executes:
        // 1. ChildWithConstructor constructor is called
        // 2. First statement is super("ChildName") - calls ParentWithConstructor constructor
        // 3. ParentWithConstructor initializes parent's fields
        // 4. Then ChildWithConstructor continues and initializes child's fields
        // This is constructor chaining - parent initializes first, then child
        ChildWithConstructor child = new ChildWithConstructor("ChildName");
        
        System.out.println("  → Constructors are NOT inherited - child must define own");
        System.out.println("  → Child constructor must call super() to initialize parent");
        System.out.println("  → super() must be first statement (if used)");
        System.out.println("  → If parent has no-arg constructor, compiler adds super() automatically");
        System.out.println("  → If parent has only parameterized constructor, must call super() with arguments");
        System.out.println("  → This ensures proper initialization order - parent before child");
        System.out.println("  → This is called constructor chaining");
        System.out.println();
    }
    
    /**
     * Interview Point: Multiple Inheritance through Interfaces
     * 
     * Technical Explanation:
     * - Java does NOT support multiple inheritance for classes (a class can extend only one class)
     * - However, Java DOES support multiple inheritance through interfaces
     * - A class can implement multiple interfaces using: class MyClass implements Interface1, Interface2
     * - The class must provide implementations for all methods from all interfaces it implements
     * - This is how Java achieves multiple inheritance - through interfaces, not classes
     * - Interfaces can also extend multiple interfaces: interface A extends B, C
     * - This solves the "diamond problem" that would occur with multiple class inheritance
     * - With interfaces, if there's a conflict (same default method in multiple interfaces),
     *   the implementing class must explicitly resolve it
     * 
     * Simple Explanation:
     * - Think of it like signing multiple contracts
     * - A class can implement Interface1 AND Interface2 AND Interface3
     * - It's like a person having multiple jobs - they must fulfill all job requirements
     * - The class must provide all methods from all interfaces
     * - This is different from class inheritance where you can only have one parent
     * - Like a student can be in multiple clubs (interfaces) but has only one school (parent class)
     * - Each interface is like a different role or capability the class must have
     * 
     * Interview Answer:
     * "Java doesn't support multiple inheritance for classes, but supports it through interfaces.
     * A class can implement multiple interfaces and must provide implementations for all methods
     * from all interfaces. This allows a class to have multiple 'contracts' while avoiding the
     * diamond problem that would occur with multiple class inheritance. Interfaces can also extend
     * multiple interfaces. If there's a conflict with default methods, the implementing class
     * must explicitly resolve it."
     */
    private static void demonstrateMultipleInheritance() {
        System.out.println("--- Multiple Inheritance through Interfaces ---");
        
        // Interview Point: Class implementing multiple interfaces
        // MultiInterfaceClass implements Interface1, Interface2, and Interface3
        // It must provide implementations for all methods from all three interfaces
        // This demonstrates multiple inheritance - one class, multiple interfaces
        MultiInterfaceClass obj = new MultiInterfaceClass();
        
        // Interview Point: Can call methods from all implemented interfaces
        // The class has implemented all methods, so we can call them
        // Each method comes from a different interface, showing multiple inheritance
        obj.method1(); // From Interface1 - implemented in MultiInterfaceClass
        obj.method2(); // From Interface2 - implemented in MultiInterfaceClass
        obj.method3(); // From Interface3 - implemented in MultiInterfaceClass
        
        System.out.println("  → Class can implement multiple interfaces (multiple inheritance)");
        System.out.println("  → Must implement all methods from all interfaces");
        System.out.println("  → This is how Java achieves multiple inheritance");
        System.out.println("  → Avoids diamond problem (no implementation conflicts with abstract methods)");
        System.out.println("  → Different from class inheritance (only one parent class allowed)");
        System.out.println("  → Each interface adds a different capability or contract");
        System.out.println();
    }
    
    /**
     * Interview Point: Diamond Problem
     * 
     * Technical Explanation:
     * - Diamond problem occurs when a class inherits from two sources that have the same method
     * - If both parents have the same method, which one should the child use? This creates ambiguity
     * - Java prevents this with classes by allowing only single inheritance (one parent class)
     * - However, with interfaces, if two interfaces have default methods with same signature,
     *   it can cause diamond problem
     * - Solution: Class must explicitly override the conflicting method
     * - In the override, you can call specific interface's method using InterfaceName.super.method()
     * - Or provide your own implementation
     * - This is why Java 8+ allows default methods but requires explicit resolution of conflicts
     * - The name "diamond" comes from the shape of the inheritance diagram (like a diamond)
     * 
     * Simple Explanation:
     * - Think of it like having two parents who both tell you to do something differently
     * - Which parent's instruction do you follow? This is the diamond problem
     * - Java prevents this with classes by saying "you can only have one parent class"
     * - With interfaces, if two interfaces have same default method, you must explicitly choose
     * - It's like two job descriptions saying "do X" differently - you must decide how to do it
     * - You can follow one parent's way, the other's way, or do it your own way
     * - The inheritance diagram looks like a diamond shape, hence the name
     * 
     * Interview Answer:
     * "Diamond problem occurs when a class inherits from two sources with the same method,
     * creating ambiguity about which method to use. Java prevents this with classes through
     * single inheritance. With interfaces, if two interfaces have default methods with same
     * signature, the implementing class must explicitly override the method. You can call
     * a specific interface's method using InterfaceName.super.method() or provide your own
     * implementation. This is why Java requires explicit resolution of default method conflicts."
     */
    private static void demonstrateDiamondProblem() {
        System.out.println("--- Diamond Problem ---");
        
        // Interview Point: Class implementing two interfaces with same default method
        // InterfaceA and InterfaceB both have default method commonMethod()
        // DiamondClass implements both, so it has a conflict - which commonMethod() should it use?
        // This creates the diamond problem - ambiguity about which method to use
        DiamondClass obj = new DiamondClass();
        
        // Interview Point: Must explicitly override
        // DiamondClass must override commonMethod() to resolve the conflict
        // In the override, it can call InterfaceA.super.commonMethod() to use InterfaceA's version
        // Or call InterfaceB.super.commonMethod() to use InterfaceB's version
        // Or provide its own implementation (which is what it does)
        obj.commonMethod(); // Interview Point: Must explicitly override - calls DiamondClass's implementation
        
        System.out.println("  → Diamond problem: Conflict when two interfaces have same default method");
        System.out.println("  → Java prevents this with classes (single inheritance)");
        System.out.println("  → With interfaces, must explicitly override to resolve conflict");
        System.out.println("  → Can use InterfaceName.super.method() to call specific interface's method");
        System.out.println("  → Or provide own implementation");
        System.out.println("  → Named 'diamond' because inheritance diagram looks like a diamond");
        System.out.println();
    }
    
    /**
     * Interview Point: instanceof Operator
     * 
     * Technical Explanation:
     * - instanceof is a binary operator that checks if an object is an instance of a specific class or interface
     * - Returns true if object is instance of the specified type (or its subclass), false otherwise
     * - Works with classes, interfaces, and arrays
     * - Returns true if object is instance of the type OR any of its subclasses
     * - Returns false if object is null (null is not instance of any type)
     * - Primarily used for safe downcasting - check type before casting to avoid ClassCastException
     * - Also used for type checking in polymorphic scenarios
     * - The operator is: object instanceof Type
     * 
     * Simple Explanation:
     * - Think of instanceof as asking "is this object of this type?"
     * - Like asking "is this animal a dog?" - returns true or false
     * - Used before downcasting to make sure it's safe
     * - Like checking a label before opening a box - "is this box labeled 'Dog'?"
     * - If true, you know it's safe to treat it as that type
     * - If false, you know it's not that type, so don't try to use it as that type
     * - It's like a safety check before doing something risky
     * 
     * Interview Answer:
     * "instanceof operator checks if an object is an instance of a specific class or interface.
     * It returns true if the object is an instance of the type or any of its subclasses, false
     * otherwise. It also returns false if the object is null. It's primarily used for safe
     * downcasting - check the type before casting to avoid ClassCastException. It also works
     * with interfaces and arrays."
     */
    private static void demonstrateInstanceof() {
        System.out.println("--- instanceof Operator ---");
        
        // Interview Point: Create Animal reference pointing to Dog object
        // The reference type is Animal, but the actual object is Dog
        Animal animal = new Dog("Buddy");
        
        // Interview Point: instanceof checks type
        // instanceof returns true if object is instance of the type or any subclass
        // animal is actually a Dog, and Dog extends Animal, so both checks return true
        System.out.println("  animal instanceof Animal: " + (animal instanceof Animal));
        // Returns true because Dog is a subclass of Animal - instanceof considers inheritance
        
        System.out.println("  animal instanceof Dog: " + (animal instanceof Dog));
        // Returns true because the actual object is a Dog
        
        System.out.println("  animal instanceof Cat: " + (animal instanceof Cat));
        // Returns false because the actual object is a Dog, not a Cat
        // Even though both Dog and Cat extend Animal, animal is specifically a Dog
        
        // Interview Point: Safe downcasting with instanceof
        // Before downcasting, we check if the object is actually the type we want to cast to
        // This prevents ClassCastException - if we tried to cast without checking, we'd get an error
        // This is a best practice - always check with instanceof before downcasting
        if (animal instanceof Dog) {
            // Interview Point: Safe to cast because we verified it's a Dog
            // We know animal is actually a Dog, so we can safely cast it
            // Without this check, if animal was actually a Cat, we'd get ClassCastException
            Dog dog = (Dog) animal;
            // Now we can call Dog-specific methods safely
            dog.bark();
        }
        // If we didn't check and animal was actually a Cat, we'd get ClassCastException
        // This is why instanceof is crucial for safe downcasting
        
        System.out.println("  → instanceof checks if object is instance of type or subclass");
        System.out.println("  → Returns true for type and all subclasses (considers inheritance)");
        System.out.println("  → Returns false for null");
        System.out.println("  → Used for safe downcasting - prevents ClassCastException");
        System.out.println("  → Best practice: Always check with instanceof before downcasting");
        System.out.println();
    }
    
    /**
     * Interview Point: getClass() vs instanceof
     * 
     * Technical Explanation:
     * - getClass(): Returns the exact runtime class of the object
     *   - Returns Class object representing the exact type
     *   - Does NOT consider inheritance hierarchy
     *   - animal.getClass() == Dog.class returns true only if object is exactly Dog
     *   - animal.getClass() == Animal.class returns false even if Dog extends Animal
     *   - More strict - checks exact type only, no inheritance consideration
     *   - Useful when you need to know the precise class, not just if it's a type of something
     * 
     * - instanceof: Checks if object is instance of type or any subclass
     *   - Returns true if object is instance of the type OR any of its subclasses
     *   - Considers inheritance hierarchy
     *   - animal instanceof Dog returns true if object is Dog
     *   - animal instanceof Animal returns true if object is Animal OR any Animal subclass (like Dog)
     *   - More flexible - checks type hierarchy, considers inheritance
     *   - Useful for polymorphic checks - "is this some kind of Animal?"
     * 
     * Simple Explanation:
     * - getClass(): "What is the exact type?" - like asking "what species is this exactly?"
     *   - Returns the precise type, not considering inheritance
     *   - Like asking "what is this exactly?" - you get the exact answer
     *   - More specific - only matches exact type
     * 
     * - instanceof: "Is it this type or any subclass?" - like asking "is this a type of X?"
     *   - Considers inheritance - returns true for type and all subclasses
     *   - Like asking "is this an animal?" - returns true for Dog, Cat, etc.
     *   - More general - matches type and all subclasses
     * 
     * Interview Answer:
     * "getClass() returns the exact runtime class of the object - it doesn't consider inheritance.
     * instanceof checks if the object is an instance of the type or any of its subclasses - it
     * does consider inheritance. getClass() is more strict (exact type only), instanceof is more
     * flexible (type hierarchy). Use getClass() when you need exact type, instanceof when you need
     * to check type hierarchy."
     */
    private static void demonstrateGetClassVsInstanceof() {
        System.out.println("--- getClass() vs instanceof ---");
        
        // Interview Point: Create Animal reference pointing to Dog object
        Animal animal = new Dog("Max");
        
        // Interview Point: getClass() returns exact class
        // getClass() returns the exact runtime class - in this case, Dog.class
        // It doesn't consider that Dog extends Animal - it just returns the exact type
        System.out.println("  getClass(): " + animal.getClass().getSimpleName());
        // Returns "Dog" - the exact class of the object, not considering inheritance
        
        System.out.println("  getClass() == Dog.class: " + (animal.getClass() == Dog.class));
        // Returns true because the exact class is Dog - getClass() matches exact type
        
        System.out.println("  getClass() == Animal.class: " + (animal.getClass() == Animal.class));
        // Returns false because the exact class is Dog, not Animal
        // Even though Dog extends Animal, getClass() returns exact type only
        // This is the key difference - getClass() doesn't consider inheritance
        
        // Interview Point: instanceof checks type hierarchy
        // instanceof considers inheritance - returns true for type and all subclasses
        System.out.println("  instanceof Dog: " + (animal instanceof Dog));
        // Returns true because object is a Dog - instanceof matches exact type
        
        System.out.println("  instanceof Animal: " + (animal instanceof Animal));
        // Returns true because Dog extends Animal - instanceof considers inheritance hierarchy
        // Even though exact class is Dog, instanceof Animal returns true because Dog IS an Animal
        // This is the key difference - instanceof considers inheritance
        
        System.out.println("  → getClass(): Exact type only - doesn't consider inheritance");
        System.out.println("  → instanceof: Type and all subclasses - considers inheritance");
        System.out.println("  → getClass() is strict (exact match), instanceof is flexible (hierarchy)");
        System.out.println("  → Use getClass() for exact type, instanceof for type hierarchy checks");
        System.out.println();
    }
}

// ==================== METHOD HIDING ====================

class ParentClass {
    // Interview Point: Static method
    public static void staticMethod() {
        System.out.println("  ParentClass staticMethod()");
    }
}

class ChildClass extends ParentClass {
    // Interview Point: Method hiding (not overriding)
    public static void staticMethod() {
        System.out.println("  ChildClass staticMethod()");
    }
}

// ==================== FIELD SHADOWING ====================

class ParentClassWithField {
    protected String name = "ParentName";
}

class ChildClassWithShadow extends ParentClassWithField {
    private String name = "ChildName"; // Interview Point: Shadows parent field
    
    public void showFields() {
        System.out.println("  this.name: " + this.name);
        System.out.println("  super.name: " + super.name);
    }
}

// ==================== CONSTRUCTOR INHERITANCE ====================

class ParentWithConstructor {
    private String name;
    
    public ParentWithConstructor(String name) {
        this.name = name;
        System.out.println("  ParentWithConstructor: " + name);
    }
}

class ChildWithConstructor extends ParentWithConstructor {
    public ChildWithConstructor(String name) {
        super(name); // Interview Point: Must call parent constructor
        System.out.println("  ChildWithConstructor: " + name);
    }
}

// ==================== MULTIPLE INHERITANCE ====================

interface Interface1 {
    void method1();
}

interface Interface2 {
    void method2();
}

interface Interface3 {
    void method3();
}

// Interview Point: Class implementing multiple interfaces
class MultiInterfaceClass implements Interface1, Interface2, Interface3 {
    @Override
    public void method1() {
        System.out.println("  Method1 from Interface1");
    }
    
    @Override
    public void method2() {
        System.out.println("  Method2 from Interface2");
    }
    
    @Override
    public void method3() {
        System.out.println("  Method3 from Interface3");
    }
}

// ==================== DIAMOND PROBLEM ====================

interface InterfaceA {
    default void commonMethod() {
        System.out.println("  InterfaceA commonMethod");
    }
}

interface InterfaceB {
    default void commonMethod() {
        System.out.println("  InterfaceB commonMethod");
    }
}

// Interview Point: Diamond problem - both interfaces have same default method
class DiamondClass implements InterfaceA, InterfaceB {
    // Interview Point: Must override to resolve conflict
    @Override
    public void commonMethod() {
        // Interview Point: Can call specific interface's method
        InterfaceA.super.commonMethod();
        // Or provide own implementation
        System.out.println("  DiamondClass implementation");
    }
}

// ==================== instanceof EXAMPLES ====================

class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
}

class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    public void bark() {
        System.out.println("  " + name + " is barking");
    }
}

class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }
}

/**
 * INTERVIEW SUMMARY: Other Concepts
 * 
 * Method Hiding:
 * - Static methods cannot be overridden
 * - Child can hide parent's static method
 * - Call depends on reference type
 * 
 * Field Shadowing:
 * - Child field with same name hides parent field
 * - Use super.field to access parent field
 * 
 * Constructor Inheritance:
 * - Constructors are NOT inherited
 * - Child must call super() first
 * 
 * Multiple Inheritance:
 * - Through interfaces only
 * - Class can implement multiple interfaces
 * 
 * Diamond Problem:
 * - Resolved by explicit override
 * - Use InterfaceName.super.method()
 * 
 * instanceof:
 * - Type checking operator
 * - Returns true for type and subclasses
 * - Used for safe downcasting
 */


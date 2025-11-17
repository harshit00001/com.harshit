package com.harshit.inheritance.advanced;

/**
 * SUPER KEYWORD - Interview Explanation:
 * 
 * super: Reference variable used to refer to immediate parent class object.
 * 
 * Technical Definition:
 * - Used to access parent class members (methods, fields, constructors)
 * - Must be first statement in constructor
 * - Can call parent class methods
 * - Can access parent class fields
 * 
 * Simple Explanation:
 * - Like saying "use parent's version"
 * - "super.method()" = call parent's method
 * - "super.field" = access parent's field
 * - "super()" = call parent's constructor
 * 
 * Uses:
 * 1. Call parent constructor: super()
 * 2. Call parent method: super.method()
 * 3. Access parent field: super.field
 */
public class SuperKeyword {

    public static void main(String[] args) {
        System.out.println("=== SUPER KEYWORD ===\n");
        
        demonstrateSuperInConstructor();
        demonstrateSuperForMethods();
        demonstrateSuperForFields();
        demonstrateSuperVsThis();
    }
    
    /**
     * Interview Point: super() in Constructor
     * 
     * Technical Explanation:
     * - super() is used to call the parent class constructor from a child class constructor
     * - It MUST be the first statement in the child constructor (if you use it)
     * - This ensures the parent class is properly initialized before the child class
     * - If you don't explicitly write super(), the compiler automatically adds super() with no arguments
     * - This is called "constructor chaining" - child constructor calls parent constructor,
     *   which may call its parent constructor, and so on up the inheritance chain
     * - If parent doesn't have a no-argument constructor, you MUST explicitly call super() with arguments
     * 
     * Simple Explanation:
     * - Think of building a house - you must build the foundation (parent) before building
     *   the walls (child)
     * - super() is like saying "initialize the parent first, then I'll initialize myself"
     * - It ensures proper initialization order - parent must be ready before child can use it
     * - If parent needs information (like a name), you pass it via super(name)
     * 
     * Interview Answer:
     * "super() is used to call the parent class constructor. It must be the first statement
     * in the child constructor. This ensures the parent is initialized before the child.
     * If parent has a parameterized constructor and no default constructor, you must call
     * super() with the required arguments. If you don't write super(), compiler adds
     * super() automatically, but this only works if parent has a no-argument constructor."
     */
    private static void demonstrateSuperInConstructor() {
        System.out.println("--- super() in Constructor ---");
        
        // Interview Point: When creating ChildClass object, constructor chain is executed
        // 1. ChildClass constructor is called
        // 2. First statement is super("ChildName") - this calls ParentClass constructor
        // 3. ParentClass constructor initializes parent's fields
        // 4. Then ChildClass constructor continues and initializes child's fields
        // This is constructor chaining - ensuring proper initialization order
        ChildClass child = new ChildClass("ChildName", "ChildValue");
        
        System.out.println("  → super() must be first statement in constructor");
        System.out.println("  → This ensures parent is initialized before child");
        System.out.println("  → If not written, compiler adds super() automatically (if parent has no-arg constructor)");
        System.out.println("  → If parent has only parameterized constructor, you MUST call super() with arguments");
        System.out.println("  → This is called constructor chaining - parent initializes first, then child");
        System.out.println();
    }
    
    /**
     * Interview Point: super for Methods
     * 
     * Technical Explanation:
     * - super.methodName() is used to call a method from the parent class
     * - This is useful when you've overridden a method in the child class but still want to
     *   call the parent's version of that method
     * - You can call parent's method from within the overridden method in child class
     * - This allows you to extend parent's functionality rather than completely replace it
     * - super can be used to call any accessible method from parent, not just overridden ones
     * 
     * Simple Explanation:
     * - Think of it like this: You've replaced parent's method with your own, but you still
     *   want to do what parent did, plus add your own stuff
     * - super.method() says "do what parent would do, then I'll add my own code"
     * - It's like saying "use parent's version, then add my improvements"
     * - This is common pattern: call super.method() first, then add child-specific code
     * 
     * Interview Answer:
     * "super.methodName() is used to call a parent class method from a child class. This is
     * especially useful when you've overridden a method but still want to use parent's
     * implementation. For example, in an overridden method, you can call super.method() to
     * execute parent's code, then add your own code. This allows extending functionality
     * rather than completely replacing it."
     */
    private static void demonstrateSuperForMethods() {
        System.out.println("--- super for Methods ---");
        
        // Interview Point: Create child object
        ChildClass child = new ChildClass("Test", "Value");
        
        // Interview Point: Calls child's display method
        // Child's display() method overrides parent's display() method
        // Inside child's display(), it first calls super.display() to execute parent's version,
        // then executes its own code. This is a common pattern - extend parent's behavior
        // rather than completely replace it
        child.display(); // Calls child's display, which internally calls parent's display using super.display()
        // Output will show:
        // "ParentClass display() method" (from super.display())
        // "ChildClass display() method" (from child's own code)
        
        System.out.println("  → super.method() calls parent's method");
        System.out.println("  → Useful when overriding but still want parent's functionality");
        System.out.println("  → Common pattern: super.method() then add child-specific code");
        System.out.println("  → Allows extending functionality rather than replacing it");
        System.out.println();
    }
    
    /**
     * Interview Point: super for Fields
     * 
     * Technical: Accesses parent class field
     * Simple: "Get value from parent, not child"
     */
    private static void demonstrateSuperForFields() {
        System.out.println("--- super for Fields ---");
        
        ChildClass child = new ChildClass("ParentName", "ChildName");
        child.showNames();
        
        System.out.println();
    }
    
    /**
     * Interview Point: super vs this
     * 
     * Technical: super refers to parent, this refers to current object
     * Simple: super = parent, this = myself
     */
    private static void demonstrateSuperVsThis() {
        System.out.println("--- super vs this ---");
        
        ChildClass child = new ChildClass("Parent", "Child");
        child.compareSuperAndThis();
        
        System.out.println();
    }
}

/**
 * Interview Point: Parent class for super examples
 */
class ParentClass {
    protected String name;
    protected String value = "ParentValue";
    
    public ParentClass(String name) {
        this.name = name;
        System.out.println("  ParentClass constructor: " + name);
    }
    
    public void display() {
        System.out.println("  ParentClass display() method");
    }
    
    public void showValue() {
        System.out.println("  Parent value: " + value);
    }
}

/**
 * Interview Point: Child class using super keyword
 */
class ChildClass extends ParentClass {
    private String childName;
    protected String value = "ChildValue"; // Interview Point: Hides parent's value
    
    // Interview Point: super() calls parent constructor
    public ChildClass(String parentName, String childName) {
        super(parentName); // Interview Point: Must be first statement
        this.childName = childName;
        System.out.println("  ChildClass constructor: " + childName);
    }
    
    // Interview Point: Override method but call parent's version too
    @Override
    public void display() {
        super.display(); // Interview Point: Call parent's display method
        System.out.println("  ChildClass display() method");
    }
    
    // Interview Point: Access parent's field using super
    public void showNames() {
        System.out.println("  Parent name (super.name): " + super.name);
        System.out.println("  Child name (this.childName): " + this.childName);
    }
    
    // Interview Point: Access parent's field when child has same name
    public void showValues() {
        System.out.println("  Parent value (super.value): " + super.value);
        System.out.println("  Child value (this.value): " + this.value);
    }
    
    // Interview Point: Compare super and this
    public void compareSuperAndThis() {
        System.out.println("  Using 'this': " + this.value);
        System.out.println("  Using 'super': " + super.value);
        System.out.println("  → 'this' refers to current object");
        System.out.println("  → 'super' refers to parent object");
    }
}

/**
 * INTERVIEW SUMMARY: super Keyword
 * 
 * Key Points:
 * 1. super() - Call parent constructor (must be first statement)
 * 2. super.method() - Call parent method
 * 3. super.field - Access parent field
 * 4. Used to avoid name conflicts
 * 5. Can't use super in static context
 * 
 * super vs this:
 * - super: Refers to parent class
 * - this: Refers to current object
 * 
 * Constructor Chaining:
 * - Child constructor must call parent constructor
 * - If not written, compiler adds super() automatically
 * - super() must be first statement
 */


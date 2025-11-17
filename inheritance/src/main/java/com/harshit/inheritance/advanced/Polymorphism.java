package com.harshit.inheritance.advanced;

/**
 * POLYMORPHISM - Interview Explanation:
 * 
 * Polymorphism: "Many forms" - ability of an object to take many forms.
 * 
 * Technical Definition:
 * - Same interface, different implementations
 * - Two types: Compile-time and Runtime
 * - Runtime polymorphism: Method overriding
 * - Compile-time polymorphism: Method overloading
 * 
 * Simple Explanation:
 * - Same name, different behavior
 * - Like "drive" - car drives differently than bike
 * - One interface, multiple implementations
 * 
 * Types:
 * 1. Runtime Polymorphism (Dynamic): Method overriding
 * 2. Compile-time Polymorphism (Static): Method overloading
 */
public class Polymorphism {

    public static void main(String[] args) {
        System.out.println("=== POLYMORPHISM ===\n");
        
        demonstrateRuntimePolymorphism();
        demonstrateCompileTimePolymorphism();
        demonstratePolymorphismWithCollections();
        demonstrateUpcastingDowncasting();
    }
    
    /**
     * Interview Point: Runtime Polymorphism
     * 
     * Technical: Method call resolved at runtime based on object type
     * Simple: Which method runs depends on actual object, not reference
     */
    private static void demonstrateRuntimePolymorphism() {
        System.out.println("--- Runtime Polymorphism (Method Overriding) ---");
        
        // Interview Point: Reference type is Animal, object type is Dog
        Animal animal1 = new Dog("Buddy");
        animal1.makeSound(); // Interview Point: Calls Dog's makeSound()
        
        // Interview Point: Reference type is Animal, object type is Cat
        Animal animal2 = new Cat("Whiskers");
        animal2.makeSound(); // Interview Point: Calls Cat's makeSound()
        
        System.out.println("  → Method called depends on actual object type");
        System.out.println("  → Resolved at runtime");
        System.out.println();
    }
    
    /**
     * Interview Point: Compile-time Polymorphism
     * 
     * Technical: Method call resolved at compile time based on parameters
     * Simple: Which method runs depends on arguments passed
     */
    private static void demonstrateCompileTimePolymorphism() {
        System.out.println("--- Compile-time Polymorphism (Method Overloading) ---");
        
        Calculator calc = new Calculator();
        
        // Interview Point: Different methods called based on parameters
        System.out.println("  add(5, 3): " + calc.add(5, 3));
        System.out.println("  add(5.5, 3.2): " + calc.add(5.5, 3.2));
        System.out.println("  add(5, 3, 2): " + calc.add(5, 3, 2));
        
        System.out.println("  → Method called depends on parameters");
        System.out.println("  → Resolved at compile time");
        System.out.println();
    }
    
    /**
     * Interview Point: Polymorphism with Collections
     * 
     * Technical: Can store different types in same collection
     * Simple: List can hold different animal types
     */
    private static void demonstratePolymorphismWithCollections() {
        System.out.println("--- Polymorphism with Collections ---");
        
        // Interview Point: List can hold different Animal types
        java.util.List<Animal> animals = new java.util.ArrayList<>();
        animals.add(new Dog("Max"));
        animals.add(new Cat("Fluffy"));
        animals.add(new Dog("Rocky"));
        
        // Interview Point: Each calls its own makeSound() method
        for (Animal animal : animals) {
            animal.makeSound(); // Runtime polymorphism
        }
        
        System.out.println();
    }
    
    /**
     * Interview Point: Upcasting and Downcasting
     * 
     * Technical Explanation:
     * - Upcasting: Converting a child class reference to a parent class reference
     *   - This is automatic and always safe - no explicit cast needed
     *   - Child "is-a" parent, so this conversion is guaranteed to work
     *   - You lose access to child-specific methods, but gain ability to use parent reference
     *   - Example: Dog is an Animal, so Dog reference can be assigned to Animal reference
     * 
     * - Downcasting: Converting a parent class reference to a child class reference
     *   - This requires explicit cast: (ChildType) parentReference
     *   - This is NOT always safe - parent reference might not point to child object
     *   - Must check with instanceof before downcasting to avoid ClassCastException
     *   - Example: Animal reference might point to Dog, Cat, or other Animal subclass
     *   - Only safe if you know the actual object type is the child type
     * 
     * Simple Explanation:
     * - Upcasting: "Treat child as parent" - always safe because child IS a parent
     *   - Like saying "this Dog is an Animal" - always true
     *   - Automatic - Java does it for you
     *   - You can use parent's methods, but not child-specific methods
     * 
     * - Downcasting: "Treat parent as child" - need to check first
     *   - Like saying "this Animal is a Dog" - might be true, might not be
     *   - Need to check with instanceof first
     *   - If wrong, you get ClassCastException
     * 
     * Interview Answer:
     * "Upcasting is converting child to parent reference - it's automatic and safe because
     * child is always a parent. Downcasting is converting parent to child reference - it
     * requires explicit cast and instanceof check because parent might not be that specific
     * child type. Upcasting loses access to child-specific methods. Downcasting regains access
     * to child-specific methods but is risky without instanceof check."
     */
    private static void demonstrateUpcastingDowncasting() {
        System.out.println("--- Upcasting and Downcasting ---");
        
        // Interview Point: Upcasting (automatic, safe)
        // We create a Dog object, then assign it to an Animal reference
        // This is upcasting - going up the inheritance hierarchy (Dog → Animal)
        // It's automatic - no cast needed, and always safe because Dog IS an Animal
        Dog dog = new Dog("Buddy");
        Animal animal = dog; // Upcasting - Dog to Animal (automatic, no cast needed)
        System.out.println("  Upcasting: Dog → Animal (automatic and safe)");
        // Interview Point: Can call parent methods through parent reference
        // Even though actual object is Dog, we're using Animal reference
        // We can call Animal's methods, but not Dog-specific methods like bark()
        animal.makeSound(); // Calls Dog's makeSound() due to polymorphism
        
        // Interview Point: Downcasting (explicit, need to check)
        // We want to convert Animal reference back to Dog reference
        // This is downcasting - going down the inheritance hierarchy (Animal → Dog)
        // It's NOT automatic - requires explicit cast, and NOT always safe
        // We must check with instanceof first to ensure the Animal is actually a Dog
        if (animal instanceof Dog) {
            // Interview Point: Explicit cast required for downcasting
            // We're telling Java "I know this Animal is actually a Dog, convert it"
            // This is safe because we checked with instanceof first
            Dog dog2 = (Dog) animal; // Downcasting - Animal to Dog (explicit cast required)
            System.out.println("  Downcasting: Animal → Dog (explicit with instanceof check)");
            // Interview Point: Can now call Dog-specific methods
            // After downcasting, we have Dog reference, so we can call Dog-specific methods
            dog2.bark(); // Can call Dog-specific method (bark() doesn't exist in Animal)
        }
        
        System.out.println("  → Upcasting: Automatic, safe, loses child-specific access");
        System.out.println("  → Downcasting: Explicit cast, risky, needs instanceof check");
        System.out.println("  → Downcasting without instanceof can cause ClassCastException");
        System.out.println();
    }
}

/**
 * Interview Point: Parent class for polymorphism
 */
class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public void makeSound() {
        System.out.println("  Animal makes a sound");
    }
}

/**
 * Interview Point: Child class overriding method
 */
class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println("  " + name + " barks: Woof! Woof!");
    }
    
    // Interview Point: Dog-specific method
    public void bark() {
        System.out.println("  " + name + " is barking");
    }
}

class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println("  " + name + " meows: Meow! Meow!");
    }
}

/**
 * Interview Point: Method overloading example
 */
class Calculator {
    // Interview Point: Method overloading - same name, different parameters
    public int add(int a, int b) {
        return a + b;
    }
    
    public double add(double a, double b) {
        return a + b;
    }
    
    public int add(int a, int b, int c) {
        return a + b + c;
    }
}

/**
 * INTERVIEW SUMMARY: Polymorphism
 * 
 * Key Points:
 * 1. Runtime Polymorphism: Method overriding (resolved at runtime)
 * 2. Compile-time Polymorphism: Method overloading (resolved at compile time)
 * 3. Upcasting: Child to Parent (automatic, safe)
 * 4. Downcasting: Parent to Child (explicit, needs instanceof)
 * 
 * Runtime Polymorphism:
 * - Method call depends on actual object type
 * - Achieved through method overriding
 * - Resolved at runtime
 * 
 * Compile-time Polymorphism:
 * - Method call depends on parameters
 * - Achieved through method overloading
 * - Resolved at compile time
 */


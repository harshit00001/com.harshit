# Inheritance - Complete Guide

A comprehensive Java project covering inheritance from basics to advanced topics, with interview-friendly explanations and code examples.

## 📚 Topics Covered

### 1. **Basics** (`basics/`)
- What is inheritance
- extends keyword
- Access modifiers in inheritance
- Multi-level inheritance
- Constructor chaining

### 2. **Advanced** (`advanced/`)
- Method overriding
- super keyword
- Abstract classes
- Interfaces (Before and After Java 8)
- Polymorphism
- final keyword
- Association, Aggregation, Composition
- Method hiding and field shadowing
- Multiple inheritance through interfaces
- Diamond problem
- instanceof operator

## 🎯 Interview Questions & Answers

### Q: What is Inheritance in Java?

**Technical:**
- Mechanism where one class acquires properties and methods of another class
- Uses 'extends' keyword
- Establishes "is-a" relationship
- Promotes code reusability

**Simple:**
- Like a child inheriting traits from parents
- Child class gets everything from parent class
- Can add new features or modify existing ones

**Example:**
```java
class Animal { }
class Dog extends Animal { } // Dog inherits from Animal
```

---

### Q: What is the difference between Method Overloading and Method Overriding?

**Technical:**

**Method Overloading:**
- Same method name, different parameters
- Compile-time polymorphism
- Can be in same class or parent-child
- Return type can be different

**Method Overriding:**
- Same method name, same parameters
- Runtime polymorphism
- Must be in parent-child relationship
- Return type must be same or covariant

**Simple:**
- **Overloading**: Same name, different parameters (like different tools with same name)
- **Overriding**: Same name, same parameters, different implementation (child replaces parent's method)

**Example:**
```java
// Overloading
class Calculator {
    int add(int a, int b) { }
    double add(double a, double b) { } // Different parameters
}

// Overriding
class Animal {
    void makeSound() { }
}
class Dog extends Animal {
    @Override
    void makeSound() { } // Same signature, different implementation
}
```

---

### Q: What is the super keyword?

**Technical:**
- Reference variable to refer to immediate parent class
- Used to access parent class members (methods, fields, constructors)
- super() calls parent constructor (must be first statement)
- super.method() calls parent method
- super.field accesses parent field

**Simple:**
- Like saying "use parent's version"
- super() = "call parent's constructor"
- super.method() = "call parent's method"

**Example:**
```java
class Child extends Parent {
    Child() {
        super(); // Call parent constructor
    }
    
    void display() {
        super.display(); // Call parent method
    }
}
```

---

### Q: What is the difference between Abstract Class and Interface?

**Technical:**

**Abstract Class:**
- Can have abstract and concrete methods
- Can have fields
- Can have constructors
- Single inheritance
- Can have access modifiers

**Interface:**
- All methods abstract (before Java 8)
- Can only have constants
- No constructors
- Multiple inheritance
- All methods are public

**Simple:**
- **Abstract Class**: Partial template, can have implementation
- **Interface**: Pure contract, no implementation (before Java 8)

**Example:**
```java
// Abstract Class
abstract class Animal {
    abstract void makeSound(); // Abstract
    void eat() { } // Concrete
}

// Interface
interface Drawable {
    void draw(); // Abstract (implicitly)
}
```

---

### Q: What is Polymorphism?

**Technical:**
- "Many forms" - ability of object to take many forms
- Two types: Runtime and Compile-time
- Runtime: Method overriding (resolved at runtime)
- Compile-time: Method overloading (resolved at compile time)

**Simple:**
- Same name, different behavior
- Like "drive" - car drives differently than bike
- One interface, multiple implementations

**Example:**
```java
// Runtime Polymorphism
Animal animal = new Dog();
animal.makeSound(); // Calls Dog's makeSound()

// Compile-time Polymorphism
calc.add(5, 3);     // int version
calc.add(5.5, 3.2); // double version
```

---

### Q: What is the difference between Runtime and Compile-time Polymorphism?

**Technical:**

**Runtime Polymorphism:**
- Method overriding
- Resolved at runtime
- Depends on actual object type
- Uses method overriding

**Compile-time Polymorphism:**
- Method overloading
- Resolved at compile time
- Depends on parameters
- Uses method overloading

**Simple:**
- **Runtime**: Which method runs decided when program runs
- **Compile-time**: Which method runs decided when code compiles

---

### Q: What is Upcasting and Downcasting?

**Technical:**

**Upcasting:**
- Child to Parent
- Automatic, safe
- No explicit cast needed

**Downcasting:**
- Parent to Child
- Explicit cast required
- Need instanceof check for safety

**Simple:**
- **Upcasting**: "Treat child as parent" (always safe)
- **Downcasting**: "Treat parent as child" (need to check first)

**Example:**
```java
// Upcasting (automatic)
Dog dog = new Dog();
Animal animal = dog; // Upcasting

// Downcasting (explicit)
if (animal instanceof Dog) {
    Dog dog2 = (Dog) animal; // Downcasting
}
```

---

### Q: What is the final keyword in inheritance?

**Technical:**
- final class: Cannot be extended
- final method: Cannot be overridden
- final variable: Cannot be reassigned

**Simple:**
- final class: "No inheritance allowed"
- final method: "No overriding allowed"
- final variable: "Constant value"

**Example:**
```java
final class MyClass { } // Cannot extend

class Parent {
    final void method() { } // Cannot override
}
```

---

### Q: Can a class extend multiple classes in Java?

**Technical:**
- No, Java supports single inheritance only
- A class can extend only one parent class
- But can implement multiple interfaces

**Simple:**
- Java: "One parent only"
- But can sign multiple contracts (interfaces)

**Example:**
```java
// Single inheritance
class Child extends Parent { } // Only one parent

// Multiple interfaces
class Child implements Interface1, Interface2 { } // Multiple interfaces
```

---

### Q: What is Constructor Chaining?

**Technical:**
- Child constructor must call parent constructor
- super() must be first statement
- If not written, compiler adds super() automatically
- Ensures parent is initialized before child

**Simple:**
- "Initialize parent first, then child"
- Like building foundation before house

**Example:**
```java
class Child extends Parent {
    Child() {
        super(); // Must call parent constructor first
        // Child initialization
    }
}
```

---

## 📝 Key Concepts Summary

### Inheritance Basics:
- **extends**: Keyword for inheritance
- **Parent/Superclass**: Class being inherited from
- **Child/Subclass**: Class that inherits
- **Single Inheritance**: One parent only

### Method Overriding:
- **@Override**: Annotation for overriding
- **Runtime Polymorphism**: Method call resolved at runtime
- **Covariant Return Type**: Can return subclass

### super Keyword:
- **super()**: Call parent constructor
- **super.method()**: Call parent method
- **super.field**: Access parent field

### Abstract Classes:
- **abstract**: Keyword for abstract class/method
- **Cannot instantiate**: Must extend
- **Partial implementation**: Some methods defined

### Interfaces:
- **implements**: Keyword for implementing interface
- **Multiple inheritance**: Can implement multiple interfaces
- **Contract**: Defines what class must do

### Polymorphism:
- **Runtime**: Method overriding
- **Compile-time**: Method overloading
- **Upcasting**: Child to Parent
- **Downcasting**: Parent to Child

---

## 🚀 Best Practices

1. **Use @Override**: Always use when overriding
2. **Call super()**: In constructor, call parent constructor
3. **Use interfaces**: For contracts, multiple inheritance
4. **Use abstract classes**: For shared code
5. **Avoid deep inheritance**: Keep hierarchy shallow
6. **Use final**: When inheritance not needed
7. **Prefer composition**: Over inheritance when possible

---

### Q: What are Association, Aggregation, and Composition?

**Technical:**

**Association:**
- Objects are related but independent
- "Uses" relationship
- No ownership
- Both have their own lifecycle

**Aggregation:**
- Weak "has-a" relationship
- Part can exist without whole
- Hollow diamond in UML
- Part can belong to multiple wholes

**Composition:**
- Strong "has-a" relationship
- Part cannot exist without whole
- Filled diamond in UML
- Part's lifecycle depends on whole

**Simple:**
- **Association**: "Uses" - Student uses Library
- **Aggregation**: "Has" (weak) - Department has Employees
- **Composition**: "Owns" (strong) - House has Rooms

**Example:**
```java
// Association
Student student = new Student();
Library library = new Library();
student.borrowBook(library); // Uses relationship

// Aggregation
Department dept = new Department();
dept.addEmployee(emp); // Employee can exist without department

// Composition
House house = new House();
house.addRoom(room); // Room cannot exist without house
```

---

### Q: What changed in Interfaces after Java 8?

**Technical (Before Java 8):**
- All methods were public abstract (implicitly)
- Could only have constants (public static final)
- No method implementations
- No static methods

**Technical (After Java 8):**
- Default methods (with implementation)
- Static methods (with implementation)
- Functional interfaces
- Backward compatible

**Simple:**
- **Before**: Pure contract, no code
- **After**: Can have code (default/static methods)

**Example:**
```java
// Before Java 8
interface OldInterface {
    void method(); // Abstract only
}

// After Java 8
interface NewInterface {
    void method(); // Abstract
    default void defaultMethod() { } // Implementation
    static void staticMethod() { } // Static
}
```

---

### Q: What is Method Hiding?

**Technical:**
- Static methods cannot be overridden
- Child can define static method with same signature
- Called "method hiding" (not overriding)
- Method call depends on reference type, not object type

**Simple:**
- Static methods belong to class, not object
- Child can "hide" parent's static method
- Which method runs depends on reference type

**Example:**
```java
Parent.staticMethod(); // Parent's method
Child.staticMethod();  // Child's method
Parent ref = new Child();
ref.staticMethod();    // Parent's (reference type matters)
```

---

### Q: What is the Diamond Problem?

**Technical:**
- Problem when class inherits from two sources with same method
- Java prevents with classes (single inheritance)
- With interfaces, default methods can cause conflict
- Solved by explicit override or InterfaceName.super.method()

**Simple:**
- What if two parents have same method?
- Java prevents this with classes
- With interfaces, must explicitly choose

**Example:**
```java
interface A { default void method() { } }
interface B { default void method() { } }
class C implements A, B {
    @Override
    public void method() {
        A.super.method(); // Explicitly choose
    }
}
```

---

**Happy Learning! 🎓**


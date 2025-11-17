/**
 * ============================================
 * LESSON 8: INHERITANCE AND POLYMORPHISM
 * ============================================
 * 
 * Inheritance allows classes to extend other classes.
 * Polymorphism allows objects of different types to be treated uniformly.
 */

// ========== BASIC INHERITANCE ==========

class Vehicle {
    protected brand: string;
    protected year: number;
    
    constructor(brand: string, year: number) {
        this.brand = brand;
        this.year = year;
    }
    
    start(): void {
        console.log(`${this.brand} vehicle started.`);
    }
    
    stop(): void {
        console.log(`${this.brand} vehicle stopped.`);
    }
    
    getInfo(): string {
        return `${this.year} ${this.brand}`;
    }
}

class Car extends Vehicle {
    private doors: number;
    
    constructor(brand: string, year: number, doors: number) {
        super(brand, year); // Call parent constructor
        this.doors = doors;
    }
    
    // Override parent method
    start(): void {
        console.log(`${this.brand} car with ${this.doors} doors started.`);
    }
    
    // New method specific to Car
    honk(): void {
        console.log("Beep beep!");
    }
}

class Motorcycle extends Vehicle {
    private hasSidecar: boolean;
    
    constructor(brand: string, year: number, hasSidecar: boolean) {
        super(brand, year);
        this.hasSidecar = hasSidecar;
    }
    
    // Override parent method
    start(): void {
        console.log(`${this.brand} motorcycle started.`);
    }
    
    wheelie(): void {
        console.log("Doing a wheelie!");
    }
}

// ========== POLYMORPHISM ==========
// Different types treated uniformly through a common interface

function driveVehicle(vehicle: Vehicle): void {
    vehicle.start();
    vehicle.stop();
    console.log(`Driving: ${vehicle.getInfo()}`);
}

let car = new Car("Toyota", 2023, 4);
let motorcycle = new Motorcycle("Honda", 2022, false);

// Both can be used with the same function
driveVehicle(car);
driveVehicle(motorcycle);

// ========== METHOD OVERRIDING ==========

class Animal {
    name: string;
    
    constructor(name: string) {
        this.name = name;
    }
    
    makeSound(): void {
        console.log(`${this.name} makes a sound.`);
    }
    
    move(): void {
        console.log(`${this.name} moves.`);
    }
}

class Dog extends Animal {
    breed: string;
    
    constructor(name: string, breed: string) {
        super(name);
        this.breed = breed;
    }
    
    // Override parent method
    makeSound(): void {
        console.log(`${this.name} (${this.breed}) barks: Woof!`);
    }
    
    // Override with additional behavior
    move(): void {
        super.move(); // Call parent method
        console.log(`${this.name} runs on four legs.`);
    }
    
    // New method
    fetch(): void {
        console.log(`${this.name} fetches the ball.`);
    }
}

// ========== ABSTRACT CLASSES ==========

abstract class Shape {
    protected color: string;
    
    constructor(color: string) {
        this.color = color;
    }
    
    // Abstract method - must be implemented by subclasses
    abstract getArea(): number;
    
    // Abstract method
    abstract getPerimeter(): number;
    
    // Regular method
    getColor(): string {
        return this.color;
    }
}

class Circle extends Shape {
    private radius: number;
    
    constructor(color: string, radius: number) {
        super(color);
        this.radius = radius;
    }
    
    getArea(): number {
        return Math.PI * this.radius * this.radius;
    }
    
    getPerimeter(): number {
        return 2 * Math.PI * this.radius;
    }
}

class Rectangle extends Shape {
    private width: number;
    private height: number;
    
    constructor(color: string, width: number, height: number) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    getArea(): number {
        return this.width * this.height;
    }
    
    getPerimeter(): number {
        return 2 * (this.width + this.height);
    }
}

// Polymorphism with abstract classes
function printShapeInfo(shape: Shape): void {
    console.log(`Color: ${shape.getColor()}`);
    console.log(`Area: ${shape.getArea().toFixed(2)}`);
    console.log(`Perimeter: ${shape.getPerimeter().toFixed(2)}`);
}

let circle = new Circle("red", 5);
let rectangle = new Rectangle("blue", 4, 6);

printShapeInfo(circle);
printShapeInfo(rectangle);

// ========== INTERFACES VS ABSTRACT CLASSES ==========

// Interface: Contract that classes must follow
interface Flyable {
    fly(): void;
    maxAltitude: number;
}

interface Swimmable {
    swim(): void;
    maxDepth: number;
}

// Abstract class: Can have implementation
abstract class Bird {
    name: string;
    
    constructor(name: string) {
        this.name = name;
    }
    
    abstract makeSound(): void;
    
    eat(): void {
        console.log(`${this.name} is eating.`);
    }
}

// Class can extend one class and implement multiple interfaces
class Duck extends Bird implements Flyable, Swimmable {
    maxAltitude: number = 1000;
    maxDepth: number = 5;
    
    constructor(name: string) {
        super(name);
    }
    
    makeSound(): void {
        console.log(`${this.name} quacks.`);
    }
    
    fly(): void {
        console.log(`${this.name} flies up to ${this.maxAltitude} feet.`);
    }
    
    swim(): void {
        console.log(`${this.name} swims up to ${this.maxDepth} feet deep.`);
    }
}

// ========== PROTECTED ACCESS MODIFIER ==========

class Base {
    public publicProp: string = "public";
    protected protectedProp: string = "protected";
    private privateProp: string = "private";
    
    protected getProtected(): string {
        return this.protectedProp;
    }
}

class Derived extends Base {
    // Can access protected members
    accessProtected(): void {
        console.log(this.protectedProp); // ✅
        console.log(this.getProtected()); // ✅
        // console.log(this.privateProp); // ❌ Error: Property 'privateProp' is private
    }
}

// ========== PRACTICAL EXAMPLE: EMPLOYEE HIERARCHY ==========

abstract class Employee {
    protected name: string;
    protected id: number;
    
    constructor(name: string, id: number) {
        this.name = name;
        this.id = id;
    }
    
    abstract calculateSalary(): number;
    
    getInfo(): string {
        return `Employee ${this.id}: ${this.name}`;
    }
}

class FullTimeEmployee extends Employee {
    private monthlySalary: number;
    
    constructor(name: string, id: number, monthlySalary: number) {
        super(name, id);
        this.monthlySalary = monthlySalary;
    }
    
    calculateSalary(): number {
        return this.monthlySalary * 12;
    }
}

class PartTimeEmployee extends Employee {
    private hourlyRate: number;
    private hoursPerWeek: number;
    
    constructor(name: string, id: number, hourlyRate: number, hoursPerWeek: number) {
        super(name, id);
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
    }
    
    calculateSalary(): number {
        return this.hourlyRate * this.hoursPerWeek * 52;
    }
}

// Polymorphism in action
function printEmployeeSalary(employee: Employee): void {
    console.log(`${employee.getInfo()}`);
    console.log(`Annual Salary: $${employee.calculateSalary()}`);
}

let fullTime = new FullTimeEmployee("John", 1, 5000);
let partTime = new PartTimeEmployee("Jane", 2, 25, 20);

printEmployeeSalary(fullTime);
printEmployeeSalary(partTime);

// ========== EXPORT ==========
export { 
    Vehicle, 
    Car, 
    Motorcycle,
    Animal,
    Dog,
    Shape,
    Circle,
    Rectangle,
    Employee,
    FullTimeEmployee,
    PartTimeEmployee
};







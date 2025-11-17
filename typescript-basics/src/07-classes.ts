/**
 * ============================================
 * LESSON 7: CLASSES
 * ============================================
 * 
 * Classes in TypeScript are similar to JavaScript classes but with type annotations.
 * They support access modifiers, readonly properties, and more.
 */

// ========== BASIC CLASS ==========

class Person {
    name: string;
    age: number;
    
    constructor(name: string, age: number) {
        this.name = name;
        this.age = age;
    }
    
    greet(): string {
        return `Hello, I'm ${this.name} and I'm ${this.age} years old.`;
    }
}

let person = new Person("John", 30);
console.log(person.greet());

// ========== ACCESS MODIFIERS ==========

class BankAccount {
    // Public: accessible from anywhere (default)
    public accountNumber: string;
    
    // Private: only accessible within this class
    private balance: number;
    
    // Protected: accessible in this class and subclasses
    protected owner: string;
    
    constructor(accountNumber: string, owner: string, initialBalance: number) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = initialBalance;
    }
    
    // Public method
    public getBalance(): number {
        return this.balance;
    }
    
    // Private method
    private validateAmount(amount: number): boolean {
        return amount > 0;
    }
    
    public deposit(amount: number): void {
        if (this.validateAmount(amount)) {
            this.balance += amount;
        }
    }
    
    public withdraw(amount: number): boolean {
        if (this.validateAmount(amount) && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
}

let account = new BankAccount("12345", "John", 1000);
account.deposit(500);
// account.balance; // ❌ Error: Property 'balance' is private
// account.validateAmount(100); // ❌ Error: Method 'validateAmount' is private

// ========== READONLY PROPERTIES ==========

class Circle {
    readonly radius: number;
    readonly PI: number = 3.14159;
    
    constructor(radius: number) {
        this.radius = radius;
    }
    
    getArea(): number {
        return this.PI * this.radius * this.radius;
    }
}

let circle = new Circle(5);
// circle.radius = 10; // ❌ Error: Cannot assign to 'radius' because it is a read-only property

// ========== GETTERS AND SETTERS ==========

class Temperature {
    private _celsius: number = 0;
    
    get celsius(): number {
        return this._celsius;
    }
    
    set celsius(value: number) {
        if (value < -273.15) {
            throw new Error("Temperature cannot be below absolute zero");
        }
        this._celsius = value;
    }
    
    get fahrenheit(): number {
        return (this._celsius * 9/5) + 32;
    }
    
    set fahrenheit(value: number) {
        this._celsius = (value - 32) * 5/9;
    }
}

let temp = new Temperature();
temp.celsius = 25;
console.log(temp.fahrenheit); // 77

// ========== STATIC MEMBERS ==========

class MathHelper {
    static PI: number = 3.14159;
    
    static add(a: number, b: number): number {
        return a + b;
    }
    
    static multiply(a: number, b: number): number {
        return a * b;
    }
}

// Access static members without creating an instance
console.log(MathHelper.PI);
console.log(MathHelper.add(5, 3));

// ========== ABSTRACT CLASSES ==========

abstract class Animal {
    name: string;
    
    constructor(name: string) {
        this.name = name;
    }
    
    // Abstract method - must be implemented by subclasses
    abstract makeSound(): void;
    
    // Regular method
    move(): void {
        console.log(`${this.name} is moving.`);
    }
}

class Dog extends Animal {
    makeSound(): void {
        console.log(`${this.name} barks: Woof! Woof!`);
    }
}

class Cat extends Animal {
    makeSound(): void {
        console.log(`${this.name} meows: Meow!`);
    }
}

// let animal = new Animal("Generic"); // ❌ Error: Cannot create instance of abstract class
let dog = new Dog("Buddy");
dog.makeSound(); // Buddy barks: Woof! Woof!
dog.move(); // Buddy is moving.

// ========== IMPLEMENTING INTERFACES ==========

interface Flyable {
    fly(): void;
}

interface Swimmable {
    swim(): void;
}

class Duck implements Flyable, Swimmable {
    name: string;
    
    constructor(name: string) {
        this.name = name;
    }
    
    fly(): void {
        console.log(`${this.name} is flying.`);
    }
    
    swim(): void {
        console.log(`${this.name} is swimming.`);
    }
}

// ========== PROPERTY INITIALIZATION ==========

class User {
    // Initialize in declaration
    id: number = 0;
    
    // Initialize in constructor
    name: string;
    
    // Optional property
    email?: string;
    
    constructor(name: string, email?: string) {
        this.name = name;
        this.email = email;
    }
}

// ========== PARAMETER PROPERTIES ==========
// Shorthand for declaring and initializing properties

class Point {
    constructor(
        public x: number,
        public y: number,
        private readonly id: string
    ) {
        // x, y, and id are automatically assigned
    }
    
    getPosition(): string {
        return `Point ${this.id}: (${this.x}, ${this.y})`;
    }
}

let point = new Point(10, 20, "P1");
console.log(point.getPosition()); // Point P1: (10, 20)

// ========== PRACTICAL EXAMPLE: SHOPPING CART ==========

class Product {
    constructor(
        public id: number,
        public name: string,
        public price: number
    ) {}
}

class CartItem {
    constructor(
        public product: Product,
        public quantity: number
    ) {}
    
    getTotal(): number {
        return this.product.price * this.quantity;
    }
}

class ShoppingCart {
    private items: CartItem[] = [];
    
    addItem(product: Product, quantity: number = 1): void {
        const existingItem = this.items.find(
            item => item.product.id === product.id
        );
        
        if (existingItem) {
            existingItem.quantity += quantity;
        } else {
            this.items.push(new CartItem(product, quantity));
        }
    }
    
    removeItem(productId: number): void {
        this.items = this.items.filter(item => item.product.id !== productId);
    }
    
    getTotal(): number {
        return this.items.reduce((total, item) => total + item.getTotal(), 0);
    }
    
    getItemCount(): number {
        return this.items.reduce((count, item) => count + item.quantity, 0);
    }
    
    clear(): void {
        this.items = [];
    }
}

// Usage
const cart = new ShoppingCart();
const laptop = new Product(1, "Laptop", 999);
const mouse = new Product(2, "Mouse", 25);

cart.addItem(laptop, 1);
cart.addItem(mouse, 2);
console.log(`Total: $${cart.getTotal()}`); // Total: $1049
console.log(`Items: ${cart.getItemCount()}`); // Items: 3

// ========== EXPORT ==========
export { 
    Person, 
    BankAccount, 
    Circle, 
    Temperature,
    Animal,
    Dog,
    ShoppingCart,
    Product
};



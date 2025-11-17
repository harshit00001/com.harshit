/**
 * ============================================
 * LESSON 3: INTERFACES
 * ============================================
 * 
 * Interfaces define the shape/structure of objects.
 * They're contracts that objects must follow.
 */

// ========== BASIC INTERFACE ==========

interface User {
    name: string;
    age: number;
    email: string;
}

// Creating an object that follows the interface
let user1: User = {
    name: "John Doe",
    age: 30,
    email: "john@example.com"
};

// ❌ This would error - missing required property
// let user2: User = {
//     name: "Jane",
//     age: 25
//     // Missing 'email'
// };

// ========== OPTIONAL PROPERTIES ==========

interface Product {
    id: number;
    name: string;
    price: number;
    description?: string; // Optional property (can be undefined)
    inStock?: boolean;
}

let product1: Product = {
    id: 1,
    name: "Laptop",
    price: 999
    // description and inStock are optional, so we can omit them
};

let product2: Product = {
    id: 2,
    name: "Mouse",
    price: 25,
    description: "Wireless mouse",
    inStock: true
};

// ========== READONLY PROPERTIES ==========

interface Config {
    readonly apiKey: string; // Cannot be changed after initialization
    readonly baseUrl: string;
    timeout: number;
}

let config: Config = {
    apiKey: "abc123",
    baseUrl: "https://api.example.com",
    timeout: 5000
};

// config.apiKey = "new-key"; // ❌ Error: Cannot assign to 'apiKey' because it is a read-only property
config.timeout = 10000; // ✅ This is fine

// ========== FUNCTION PROPERTIES ==========

interface Calculator {
    add(a: number, b: number): number;
    subtract(a: number, b: number): number;
    multiply?: (a: number, b: number) => number; // Optional method
}

let calc: Calculator = {
    add: (a, b) => a + b,
    subtract: (a, b) => a - b
    // multiply is optional
};

// ========== INDEX SIGNATURES ==========
// Allows objects to have additional properties

interface Dictionary {
    [key: string]: string; // Any string key with string value
}

let colors: Dictionary = {
    red: "#FF0000",
    green: "#00FF00",
    blue: "#0000FF"
};

// Can add more properties dynamically
colors["yellow"] = "#FFFF00";

// ========== EXTENDING INTERFACES ==========

interface Animal {
    name: string;
    age: number;
}

interface Dog extends Animal {
    breed: string;
    bark(): void;
}

let myDog: Dog = {
    name: "Buddy",
    age: 3,
    breed: "Golden Retriever",
    bark: () => console.log("Woof!")
};

// ========== INTERFACE MERGING ==========
// TypeScript automatically merges interfaces with the same name

interface Window {
    title: string;
}

interface Window {
    width: number;
}

// Now Window has both title and width
let window: Window = {
    title: "My Window",
    width: 800
};

// ========== INTERFACES VS TYPE ALIASES ==========
// Interfaces can be extended and merged
// Type aliases can represent unions, intersections, and primitives

// Interface (can be extended)
interface Point {
    x: number;
    y: number;
}

// Type alias (more flexible)
type PointType = {
    x: number;
    y: number;
};

// ========== PRACTICAL EXAMPLE: API RESPONSE ==========

interface ApiResponse<T> {
    success: boolean;
    data: T;
    message?: string;
    timestamp: number;
}

interface UserData {
    id: number;
    username: string;
    email: string;
}

let apiResponse: ApiResponse<UserData> = {
    success: true,
    data: {
        id: 1,
        username: "johndoe",
        email: "john@example.com"
    },
    message: "User retrieved successfully",
    timestamp: Date.now()
};

// ========== COMPLEX EXAMPLE: E-COMMERCE ==========

interface Address {
    street: string;
    city: string;
    zipCode: string;
    country: string;
}

interface Customer {
    id: number;
    name: string;
    email: string;
    address: Address;
    phone?: string;
}

interface OrderItem {
    productId: number;
    quantity: number;
    price: number;
}

interface Order {
    id: number;
    customer: Customer;
    items: OrderItem[];
    total: number;
    status: "pending" | "processing" | "shipped" | "delivered";
    createdAt: Date;
}

let order: Order = {
    id: 1001,
    customer: {
        id: 1,
        name: "John Doe",
        email: "john@example.com",
        address: {
            street: "123 Main St",
            city: "New York",
            zipCode: "10001",
            country: "USA"
        }
    },
    items: [
        { productId: 1, quantity: 2, price: 50 },
        { productId: 2, quantity: 1, price: 100 }
    ],
    total: 200,
    status: "pending",
    createdAt: new Date()
};

// ========== EXPORT ==========
export { User, Product, Calculator, Dog, Order, order };







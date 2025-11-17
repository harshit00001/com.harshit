/**
 * ============================================
 * LESSON 2: ARRAYS AND TUPLES
 * ============================================
 * 
 * Arrays store multiple values of the same type.
 * Tuples store fixed-length arrays with specific types at each position.
 */

// ========== ARRAY TYPES ==========

// Method 1: Using square brackets (preferred)
let numbers: number[] = [1, 2, 3, 4, 5];
let fruits: string[] = ["apple", "banana", "orange"];

// Method 2: Using Array<T> generic syntax
let scores: Array<number> = [95, 87, 92, 88];
let colors: Array<string> = ["red", "green", "blue"];

// ========== ARRAY OPERATIONS ==========

// Adding elements
numbers.push(6);
fruits.push("grape");

// Accessing elements
let firstNumber: number = numbers[0]; // 1
let lastFruit: string = fruits[fruits.length - 1]; // "grape"

// Iterating arrays
for (let num of numbers) {
    console.log(num);
}

// Array methods with type safety
let doubled: number[] = numbers.map(n => n * 2);
let filtered: number[] = numbers.filter(n => n > 3);

// ========== READONLY ARRAYS ==========
// Prevents modification of the array

let readonlyNumbers: readonly number[] = [1, 2, 3];
// readonlyNumbers.push(4); // ❌ Error: Property 'push' does not exist on type 'readonly number[]'
// readonlyNumbers[0] = 10; // ❌ Error: Index signature in type 'readonly number[]' only permits reading

// ========== MULTI-DIMENSIONAL ARRAYS ==========

let matrix: number[][] = [
    [1, 2, 3],
    [4, 5, 6],
    [7, 8, 9]
];

let threeD: number[][][] = [
    [[1, 2], [3, 4]],
    [[5, 6], [7, 8]]
];

// ========== TUPLES ==========
// Fixed-length arrays with specific types at each position

// Basic tuple
let person: [string, number] = ["John", 25];
// person = [25, "John"]; // ❌ Error: Type 'number' is not assignable to type 'string'

// Accessing tuple elements
let name: string = person[0]; // "John"
let age: number = person[1]; // 25

// Named tuple (TypeScript 4.0+)
let coordinates: [x: number, y: number] = [10, 20];

// Optional tuple elements
let optionalTuple: [string, number?] = ["hello"];
optionalTuple = ["hello", 42]; // Also valid

// Rest elements in tuples
let tupleWithRest: [string, ...number[]] = ["numbers", 1, 2, 3, 4];

// ========== PRACTICAL EXAMPLES ==========

// Example 1: RGB Color
type RGB = [number, number, number];
let red: RGB = [255, 0, 0];
let green: RGB = [0, 255, 0];
let blue: RGB = [0, 0, 255];

// Example 2: API Response
type ApiResponse = [boolean, string, any?];
let success: ApiResponse = [true, "Data loaded", { id: 1, name: "John" }];
let error: ApiResponse = [false, "Failed to load data"];

// Example 3: Function returning tuple
function getUserInfo(): [string, number, boolean] {
    return ["Alice", 30, true];
}

let [userName, userAge, isActive] = getUserInfo();
console.log(`${userName} is ${userAge} years old and ${isActive ? "active" : "inactive"}`);

// ========== ARRAY DESTRUCTURING ==========

let [first, second, ...rest] = numbers;
console.log(`First: ${first}, Second: ${second}, Rest: ${rest}`);

// ========== TYPE-SAFE ARRAY METHODS ==========

interface Product {
    name: string;
    price: number;
}

let products: Product[] = [
    { name: "Laptop", price: 999 },
    { name: "Mouse", price: 25 },
    { name: "Keyboard", price: 75 }
];

// TypeScript knows the type in callbacks!
let expensiveProducts = products.filter(product => product.price > 50);
let productNames = products.map(product => product.name);
let totalPrice = products.reduce((sum, product) => sum + product.price, 0);

console.log(`Total price: $${totalPrice}`);

// ========== EXPORT ==========
export { numbers, fruits, person, getUserInfo, products };



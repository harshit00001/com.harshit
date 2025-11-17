/**
 * ============================================
 * LESSON 1: BASIC TYPES
 * ============================================
 * 
 * TypeScript adds type annotations to JavaScript.
 * This helps catch errors before your code runs!
 */

// ========== PRIMITIVE TYPES ==========

// 1. String Type
let userName: string = "John";
let greeting: string = `Hello, ${userName}!`; // Template literals work too

// 2. Number Type (includes integers, floats, Infinity, NaN)
let age: number = 25;
let price: number = 99.99;
let temperature: number = -10;

// 3. Boolean Type
let isActive: boolean = true;
let isComplete: boolean = false;

// ========== TYPE INFERENCE ==========
// TypeScript can infer types automatically!
let city = "New York"; // TypeScript knows this is a string
// city = 123; // ❌ Error: Type 'number' is not assignable to type 'string'

// ========== SPECIAL TYPES ==========

// 4. Null and Undefined
let data: null = null;
let value: undefined = undefined;

// 5. Void (used for functions that don't return anything)
function logMessage(message: string): void {
    console.log(message);
    // No return statement needed
}

// ========== ANY TYPE (Use Sparingly!) ==========
// 'any' disables type checking - use only when necessary
let dynamicValue: any = "Hello";
dynamicValue = 42; // ✅ No error (but not recommended!)
dynamicValue = true; // ✅ No error

// ========== UNKNOWN TYPE (Better than 'any') ==========
// 'unknown' requires type checking before use
let userInput: unknown = "Hello World";

// ❌ This would error:
// let strLength: number = userInput.length;

// ✅ This works (type narrowing):
if (typeof userInput === "string") {
    let strLength: number = userInput.length; // Now TypeScript knows it's a string
}

// ========== TYPE ANNOTATIONS ==========
// Explicitly tell TypeScript what type a variable should be

let count: number = 10;
let message: string = "TypeScript is awesome!";
let isReady: boolean = false;

// ========== PRACTICAL EXAMPLE ==========

function calculateTotal(price: number, quantity: number): number {
    return price * quantity;
}

let total: number = calculateTotal(10.50, 3);
console.log(`Total: $${total}`); // Total: $31.50

// ========== TYPE ERRORS (What TypeScript Prevents) ==========

// ❌ Uncomment these to see TypeScript errors:
// let name: string = 123; // Error: Type 'number' is not assignable to type 'string'
// let age: number = "twenty"; // Error: Type 'string' is not assignable to type 'number'
// let isActive: boolean = "yes"; // Error: Type 'string' is not assignable to type 'boolean'

// ========== EXPORT FOR USE IN OTHER FILES ==========
export { userName, age, isActive, calculateTotal };


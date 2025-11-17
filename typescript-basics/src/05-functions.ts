/**
 * ============================================
 * LESSON 5: FUNCTIONS
 * ============================================
 * 
 * Functions in TypeScript have type annotations for parameters and return values.
 */

// ========== BASIC FUNCTION TYPES ==========

// Function with explicit return type
function add(a: number, b: number): number {
    return a + b;
}

// Function without return (void)
function greet(name: string): void {
    console.log(`Hello, ${name}!`);
}

// Function that never returns (throws error or infinite loop)
function throwError(message: string): never {
    throw new Error(message);
}

// ========== OPTIONAL PARAMETERS ==========

function createUser(name: string, age?: number): string {
    if (age) {
        return `${name} is ${age} years old`;
    }
    return `${name}`;
}

createUser("John"); // ✅
createUser("Jane", 25); // ✅

// ========== DEFAULT PARAMETERS ==========

function multiply(a: number, b: number = 1): number {
    return a * b;
}

multiply(5); // Returns 5 (b defaults to 1)
multiply(5, 3); // Returns 15

// ========== REST PARAMETERS ==========

function sum(...numbers: number[]): number {
    return numbers.reduce((total, num) => total + num, 0);
}

sum(1, 2, 3); // Returns 6
sum(1, 2, 3, 4, 5); // Returns 15

// ========== FUNCTION OVERLOADS ==========
// Multiple function signatures for the same function

function format(value: string): string;
function format(value: number): string;
function format(value: boolean): string;
function format(value: string | number | boolean): string {
    if (typeof value === "string") {
        return value.toUpperCase();
    } else if (typeof value === "number") {
        return value.toFixed(2);
    } else {
        return value ? "YES" : "NO";
    }
}

format("hello"); // Returns "HELLO"
format(3.14159); // Returns "3.14"
format(true); // Returns "YES"

// ========== ARROW FUNCTIONS ==========

const subtract = (a: number, b: number): number => {
    return a - b;
};

// Shorthand (single expression)
const divide = (a: number, b: number): number => a / b;

// ========== FUNCTION TYPES ==========
// Type for a function itself

type MathOperation = (a: number, b: number) => number;

const addOp: MathOperation = (a, b) => a + b;
const multiplyOp: MathOperation = (a, b) => a * b;

function calculate(a: number, b: number, operation: MathOperation): number {
    return operation(a, b);
}

calculate(10, 5, addOp); // Returns 15
calculate(10, 5, multiplyOp); // Returns 50

// ========== GENERIC FUNCTIONS ==========

function identity<T>(arg: T): T {
    return arg;
}

let output1 = identity<string>("hello"); // Type is string
let output2 = identity<number>(42); // Type is number
let output3 = identity("world"); // TypeScript infers string

// ========== ASYNC FUNCTIONS ==========

async function fetchUser(id: number): Promise<{ id: number; name: string }> {
    // Simulate API call
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({ id, name: "John Doe" });
        }, 1000);
    });
}

// Using async/await
async function getUserData(id: number): Promise<void> {
    const user = await fetchUser(id);
    console.log(user);
}

// ========== HIGHER-ORDER FUNCTIONS ==========

function createMultiplier(factor: number): (value: number) => number {
    return (value: number) => value * factor;
}

const double = createMultiplier(2);
const triple = createMultiplier(3);

console.log(double(5)); // 10
console.log(triple(5)); // 15

// ========== CALLBACK FUNCTIONS ==========

type Callback = (result: number) => void;

function processNumbers(numbers: number[], callback: Callback): void {
    const sum = numbers.reduce((a, b) => a + b, 0);
    callback(sum);
}

processNumbers([1, 2, 3, 4], (result) => {
    console.log(`Sum is: ${result}`);
});

// ========== PRACTICAL EXAMPLE: VALIDATOR ==========

type Validator = (value: string) => boolean;

function validateEmail(email: string): boolean {
    return email.includes("@");
}

function validatePassword(password: string): boolean {
    return password.length >= 8;
}

function validate(value: string, validators: Validator[]): boolean {
    return validators.every(validator => validator(value));
}

const isValid = validate("user@example.com", [validateEmail]);
const isStrongPassword = validate("mypassword123", [validatePassword]);

// ========== EXPORT ==========
export { 
    add, 
    greet, 
    sum, 
    format, 
    MathOperation, 
    calculate,
    fetchUser,
    validate
};







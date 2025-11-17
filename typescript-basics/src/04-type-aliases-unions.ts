/**
 * ============================================
 * LESSON 4: TYPE ALIASES AND UNIONS
 * ============================================
 * 
 * Type aliases create custom names for types.
 * Union types allow a value to be one of several types.
 */

// ========== TYPE ALIASES ==========
// Create a new name for a type

type ID = number | string;
type Status = "active" | "inactive" | "pending";

let userId: ID = 123;
userId = "abc-123"; // Can be either number or string

let userStatus: Status = "active";
// userStatus = "deleted"; // ❌ Error: Type '"deleted"' is not assignable to type 'Status'

// ========== UNION TYPES ==========
// A value can be one of several types (using |)

type StringOrNumber = string | number;

function printValue(value: StringOrNumber): void {
    console.log(value);
}

printValue("Hello"); // ✅
printValue(42); // ✅
// printValue(true); // ❌ Error

// ========== TYPE NARROWING ==========
// TypeScript narrows types based on conditions

function processValue(value: string | number): void {
    if (typeof value === "string") {
        // TypeScript knows 'value' is string here
        console.log(value.toUpperCase());
    } else {
        // TypeScript knows 'value' is number here
        console.log(value.toFixed(2));
    }
}

// ========== LITERAL TYPES ==========
// A type that represents a specific value

type Direction = "up" | "down" | "left" | "right";
type HttpMethod = "GET" | "POST" | "PUT" | "DELETE";
type Theme = "light" | "dark";

function move(direction: Direction): void {
    console.log(`Moving ${direction}`);
}

move("up"); // ✅
// move("diagonal"); // ❌ Error

// ========== INTERSECTION TYPES ==========
// Combines multiple types (using &)

interface Person {
    name: string;
    age: number;
}

interface Employee {
    employeeId: number;
    department: string;
}

// Intersection: must have properties from both types
type Staff = Person & Employee;

let staff: Staff = {
    name: "Alice",
    age: 30,
    employeeId: 12345,
    department: "Engineering"
};

// ========== COMPLEX TYPE ALIASES ==========

type Callback = (error: Error | null, data?: any) => void;

function fetchData(callback: Callback): void {
    // Simulate async operation
    setTimeout(() => {
        callback(null, { id: 1, name: "Data" });
    }, 1000);
}

// ========== DISCRIMINATED UNIONS ==========
// Pattern for working with union types

type SuccessResponse = {
    status: "success";
    data: any;
};

type ErrorResponse = {
    status: "error";
    message: string;
};

type ApiResponse = SuccessResponse | ErrorResponse;

function handleResponse(response: ApiResponse): void {
    if (response.status === "success") {
        // TypeScript knows this is SuccessResponse
        console.log("Data:", response.data);
    } else {
        // TypeScript knows this is ErrorResponse
        console.error("Error:", response.message);
    }
}

// ========== PRACTICAL EXAMPLES ==========

// Example 1: Event Handler
type EventType = "click" | "hover" | "focus";
type EventHandler = (event: EventType) => void;

function addEventListener(type: EventType, handler: EventHandler): void {
    handler(type);
}

// Example 2: Configuration
type Environment = "development" | "staging" | "production";

interface Config {
    env: Environment;
    apiUrl: string;
    timeout: number;
}

// Example 3: Result Type (Common Pattern)
type Result<T, E = Error> = 
    | { success: true; data: T }
    | { success: false; error: E };

function divide(a: number, b: number): Result<number> {
    if (b === 0) {
        return { success: false, error: new Error("Division by zero") };
    }
    return { success: true, data: a / b };
}

// Example 4: Optional Chaining with Union
type Maybe<T> = T | null | undefined;

function getLength(value: Maybe<string>): number {
    return value?.length ?? 0; // Returns 0 if value is null/undefined
}

// ========== TEMPLATE LITERAL TYPES ==========
// Create types from string templates

type HttpStatus = `HTTP_${number}`;
type ApiEndpoint = `/api/${string}`;

let status: HttpStatus = "HTTP_200"; // ✅
// let status2: HttpStatus = "HTTP_OK"; // ❌ Error

let endpoint: ApiEndpoint = "/api/users"; // ✅
// let endpoint2: ApiEndpoint = "/users"; // ❌ Error

// ========== MAPPED TYPES WITH UNIONS ==========

type Keys = "name" | "age" | "email";
type Optional<T> = {
    [K in keyof T]?: T[K];
};

interface User {
    name: string;
    age: number;
    email: string;
}

type OptionalUser = Optional<User>;
// Equivalent to: { name?: string; age?: number; email?: string; }

// ========== EXPORT ==========
export { 
    ID, 
    Status, 
    Direction, 
    Staff, 
    ApiResponse, 
    Result, 
    divide,
    handleResponse 
};







/**
 * ============================================
 * LESSON 6: GENERICS
 * ============================================
 * 
 * Generics allow you to create reusable components that work with multiple types.
 * Think of them as "type variables" that get filled in when you use the component.
 */

// ========== BASIC GENERIC FUNCTION ==========

// Without generics (not flexible)
function getFirstItem(items: any[]): any {
    return items[0];
}

// With generics (type-safe and flexible)
function getFirst<T>(items: T[]): T | undefined {
    return items[0];
}

let numbers = [1, 2, 3];
let firstNumber = getFirst(numbers); // Type: number | undefined

let strings = ["a", "b", "c"];
let firstString = getFirst(strings); // Type: string | undefined

// ========== MULTIPLE TYPE PARAMETERS ==========

function pair<T, U>(first: T, second: U): [T, U] {
    return [first, second];
}

let result = pair<string, number>("hello", 42);
// result is [string, number]

// ========== GENERIC INTERFACES ==========

interface Box<T> {
    value: T;
    getValue(): T;
    setValue(value: T): void;
}

class NumberBox implements Box<number> {
    value: number;
    
    constructor(value: number) {
        this.value = value;
    }
    
    getValue(): number {
        return this.value;
    }
    
    setValue(value: number): void {
        this.value = value;
    }
}

class StringBox implements Box<string> {
    value: string;
    
    constructor(value: string) {
        this.value = value;
    }
    
    getValue(): string {
        return this.value;
    }
    
    setValue(value: string): void {
        this.value = value;
    }
}

// ========== GENERIC CONSTRAINTS ==========
// Using 'extends' to limit what types can be used

interface HasLength {
    length: number;
}

function getLength<T extends HasLength>(item: T): number {
    return item.length;
}

getLength("hello"); // ✅ string has length
getLength([1, 2, 3]); // ✅ array has length
// getLength(42); // ❌ number doesn't have length

// ========== KEYOF CONSTRAINT ==========

function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
    return obj[key];
}

let person = { name: "John", age: 30, city: "NYC" };
let name = getProperty(person, "name"); // ✅
let age = getProperty(person, "age"); // ✅
// let invalid = getProperty(person, "invalid"); // ❌ Error

// ========== UTILITY TYPES ==========

// Partial<T> - Makes all properties optional
interface User {
    name: string;
    age: number;
    email: string;
}

type PartialUser = Partial<User>;
// Equivalent to: { name?: string; age?: number; email?: string; }

function updateUser(user: User, updates: Partial<User>): User {
    return { ...user, ...updates };
}

// Required<T> - Makes all properties required
type RequiredUser = Required<PartialUser>; // Back to all required

// Pick<T, K> - Select specific properties
type UserName = Pick<User, "name">; // { name: string }

// Omit<T, K> - Remove specific properties
type UserWithoutEmail = Omit<User, "email">; // { name: string; age: number }

// Readonly<T> - Makes all properties readonly
type ReadonlyUser = Readonly<User>;

// Record<K, V> - Create object type with specific keys and values
type UserRoles = Record<string, "admin" | "user" | "guest">;
let roles: UserRoles = {
    "john": "admin",
    "jane": "user"
};

// ========== GENERIC CLASSES ==========

class Stack<T> {
    private items: T[] = [];
    
    push(item: T): void {
        this.items.push(item);
    }
    
    pop(): T | undefined {
        return this.items.pop();
    }
    
    peek(): T | undefined {
        return this.items[this.items.length - 1];
    }
    
    isEmpty(): boolean {
        return this.items.length === 0;
    }
}

let numberStack = new Stack<number>();
numberStack.push(1);
numberStack.push(2);
numberStack.push(3);

let stringStack = new Stack<string>();
stringStack.push("a");
stringStack.push("b");

// ========== CONDITIONAL TYPES ==========

type NonNullable<T> = T extends null | undefined ? never : T;

type StringOrNumber = string | number;
type NonNullStringOrNumber = NonNullable<StringOrNumber>; // string | number

// ========== MAPPED TYPES ==========

type Optional<T> = {
    [P in keyof T]?: T[P];
};

type Readonly<T> = {
    readonly [P in keyof T]: T[P];
};

// ========== PRACTICAL EXAMPLE: API CLIENT ==========

interface ApiResponse<T> {
    data: T;
    status: number;
    message: string;
}

class ApiClient {
    async get<T>(url: string): Promise<ApiResponse<T>> {
        // Simulate API call
        return {
            data: {} as T,
            status: 200,
            message: "Success"
        };
    }
    
    async post<T, U>(url: string, body: U): Promise<ApiResponse<T>> {
        // Simulate API call
        return {
            data: {} as T,
            status: 201,
            message: "Created"
        };
    }
}

// Usage
interface User {
    id: number;
    name: string;
}

const client = new ApiClient();
const response = await client.get<User[]>("/users");
// response.data is User[]

// ========== EXPORT ==========
export { 
    getFirst, 
    Box, 
    Stack, 
    updateUser,
    ApiClient,
    ApiResponse
};







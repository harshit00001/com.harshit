# TypeScript Learning Roadmap 🗺️

## Overview
This roadmap will guide you through learning TypeScript from basics to advanced concepts. Follow the order and practice with the provided code examples.

---

## Phase 1: Foundation (Week 1-2)

### 1.1 What is TypeScript?
- **What**: TypeScript is JavaScript with static type checking
- **Why**: Catches errors at compile-time, improves code quality, better IDE support
- **How**: Transpiles to JavaScript

### 1.2 Basic Types
- ✅ Primitive types: `string`, `number`, `boolean`
- ✅ Special types: `null`, `undefined`, `void`
- ✅ Type annotations and type inference
- ✅ `any` and `unknown` types

**Practice**: See `src/01-basic-types.ts`

---

## Phase 2: Type System (Week 2-3)

### 2.1 Arrays and Tuples
- ✅ Array types: `number[]`, `Array<number>`
- ✅ Tuple types: `[string, number]`
- ✅ Readonly arrays

**Practice**: See `src/02-arrays-tuples.ts`

### 2.2 Objects and Interfaces
- ✅ Object types
- ✅ Interface declaration
- ✅ Optional and readonly properties
- ✅ Index signatures

**Practice**: See `src/03-interfaces.ts`

### 2.3 Type Aliases and Unions
- ✅ Type aliases
- ✅ Union types (`|`)
- ✅ Intersection types (`&`)
- ✅ Literal types

**Practice**: See `src/04-type-aliases-unions.ts`

---

## Phase 3: Functions (Week 3-4)

### 3.1 Function Types
- ✅ Function declarations with types
- ✅ Optional and default parameters
- ✅ Rest parameters
- ✅ Function overloads

**Practice**: See `src/05-functions.ts`

### 3.2 Generics
- ✅ Generic functions
- ✅ Generic interfaces and classes
- ✅ Constraints with `extends`
- ✅ Utility types: `Partial`, `Pick`, `Omit`

**Practice**: See `src/06-generics.ts`

---

## Phase 4: Object-Oriented Programming (Week 4-5)

### 4.1 Classes
- ✅ Class syntax
- ✅ Access modifiers: `public`, `private`, `protected`
- ✅ Readonly properties
- ✅ Getters and setters

**Practice**: See `src/07-classes.ts`

### 4.2 Inheritance and Polymorphism
- ✅ Class inheritance (`extends`)
- ✅ Method overriding
- ✅ Abstract classes
- ✅ Interfaces vs Abstract classes

**Practice**: See `src/08-inheritance.ts`

---

## Phase 5: Advanced Types (Week 5-6)

### 5.1 Utility Types
- ✅ `Partial<T>`, `Required<T>`, `Readonly<T>`
- ✅ `Pick<T, K>`, `Omit<T, K>`
- ✅ `Record<K, V>`
- ✅ `Exclude<T, U>`, `Extract<T, U>`

**Practice**: See `src/09-utility-types.ts`

### 5.2 Conditional Types and Mapped Types
- ✅ Conditional types (`T extends U ? X : Y`)
- ✅ Mapped types
- ✅ Template literal types

**Practice**: See `src/10-advanced-types.ts`

---

## Phase 6: Modules and Namespaces (Week 6)

### 6.1 ES Modules
- ✅ `import` and `export`
- ✅ Default exports
- ✅ Named exports
- ✅ Re-exports

**Practice**: See `src/11-modules/`

---

## Phase 7: Real-World Patterns (Week 7-8)

### 7.1 Error Handling
- ✅ Custom error types
- ✅ Type guards
- ✅ Assertion functions

**Practice**: See `src/12-error-handling.ts`

### 7.2 Decorators (Advanced)
- ✅ Class decorators
- ✅ Method decorators
- ✅ Property decorators

**Practice**: See `src/13-decorators.ts`

---

## Learning Tips 💡

1. **Practice Daily**: Code along with examples, modify them
2. **Read Error Messages**: TypeScript errors are helpful - learn from them
3. **Start Strict**: Use `strict: true` in tsconfig.json
4. **Type Everything**: Avoid `any` - use proper types
5. **Build Projects**: Apply concepts in real projects

---

## Next Steps After Basics

- TypeScript with React/Vue/Angular
- TypeScript with Node.js/Express
- TypeScript Design Patterns
- TypeScript Testing (Jest, Vitest)
- TypeScript Performance Optimization

---

## Resources

- [Official TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html)
- [TypeScript Deep Dive](https://basarat.gitbook.io/typescript/)
- [Type Challenges](https://github.com/type-challenges/type-challenges)

---

**Happy Learning! 🚀**



# Day 5: Advanced Hooks - Custom Hooks, useContext, useReducer

## 📚 Learning Objectives

By the end of this day, you will understand:
- How to create custom hooks
- Using useContext for global state
- useReducer for complex state management
- useMemo and useCallback for optimization
- When to use which hook

---

## 🎯 Interview Questions Covered

1. **What are Custom Hooks?**
2. **What is useContext?**
3. **What is useReducer?**
4. **When to use useReducer vs useState?**
5. **What is useMemo and useCallback?**
6. **How to share state between components?**

---

## 🚀 Execution Steps

### Step 1: Navigate to Day 5 Folder

```bash
cd day5
```

### Step 2: Run the Development Server

```bash
npm run dev
```

---

## 📖 Simple Explanation

### Custom Hooks

**Simple:** Custom hooks are like reusable functions that can use React hooks. They let you share logic between components, like a recipe you can use in different dishes.

**Technical:** Custom hooks are JavaScript functions that start with "use" and can call other hooks. They enable logic reuse and follow the rules of hooks.

### useContext

**Simple:** useContext lets you share data with many components without passing it through every level. Like a bulletin board everyone can read.

**Technical:** useContext provides a way to pass data through the component tree without prop drilling. It consumes a context created by createContext.

### useReducer

**Simple:** useReducer is like useState but for more complex state. Instead of setting values directly, you send "actions" that describe what happened, and a "reducer" function decides how to update the state.

**Technical:** useReducer is an alternative to useState for complex state logic. It uses a reducer function and actions, similar to Redux pattern.

---

## 💻 Code Examples

### Example 1: Custom Hook - useCounter

**File:** `day5/Example1-CustomHook.jsx`

Shows how to create and use a custom hook.

### Example 2: useContext

**File:** `day5/Example2-UseContext.jsx`

Demonstrates context API for global state.

### Example 3: useReducer

**File:** `day5/Example3-UseReducer.jsx`

Complex state management with useReducer.

### Example 4: useMemo and useCallback

**File:** `day5/Example4-OptimizationHooks.jsx`

Performance optimization with useMemo and useCallback.

### Example 5: Combining Hooks

**File:** `day5/Example5-CombiningHooks.jsx`

Real-world example combining multiple hooks.

---

## 🎓 Interview Answers

### Q1: What are Custom Hooks?

**Simple Answer:**
Custom hooks are functions you create that use React hooks. They let you reuse logic between components.

**Technical Answer:**
Custom hooks are JavaScript functions that start with "use" and can call other hooks. They enable logic reuse, follow rules of hooks, and can return values or functions.

---

### Q2: When to use useReducer vs useState?

**Simple Answer:**
Use useState for simple state. Use useReducer when you have complex state with multiple values or when state updates depend on previous state.

**Technical Answer:**
- useState: Simple state, independent updates
- useReducer: Complex state, multiple sub-values, state logic, predictable updates

---

## ✅ Practice Exercises

1. Create a custom useFetch hook
2. Build a theme context with useContext
3. Create a todo app with useReducer
4. Optimize a component with useMemo

---

## 🔍 Key Takeaways

1. ✅ Custom hooks start with "use"
2. ✅ useContext avoids prop drilling
3. ✅ useReducer for complex state
4. ✅ useMemo/useCallback for optimization
5. ✅ Follow rules of hooks

---

**🎉 Ready for Day 6: Routing & Navigation!**










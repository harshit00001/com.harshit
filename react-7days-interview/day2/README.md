# Day 2: State Management - useState and useEffect

## 📚 Learning Objectives

By the end of this day, you will understand:
- What is State in React
- How to use useState hook
- How to use useEffect hook
- When to use state vs props
- Component lifecycle and side effects

---

## 🎯 Interview Questions Covered

1. **What is State in React?**
2. **What is useState hook?**
3. **What is useEffect hook?**
4. **Difference between State and Props?**
5. **When does useEffect run?**
6. **How to handle component cleanup?**

---

## 🚀 Execution Steps

### Step 1: Navigate to Day 2 Folder

```bash
cd day2
```

### Step 2: Run the Development Server

```bash
# From project root
npm run dev
```

### Step 3: Open Examples

Each example file can be imported and used in your App.jsx

---

## 📖 Simple Explanation

### What is State?

**Simple:** State is like a component's memory. It remembers information that can change, like a counter number or whether a button is clicked.

**Technical:** State is a JavaScript object that stores component's data that can change over time. When state changes, React re-renders the component.

### What is useState?

**Simple:** useState is a tool that lets you add memory to your component. You tell it the starting value, and it gives you the current value and a way to change it.

**Technical:** useState is a React Hook that returns a stateful value and a function to update it. It triggers a re-render when the state changes.

### What is useEffect?

**Simple:** useEffect is like a helper that does things after your component appears on screen, like fetching data or setting up timers.

**Technical:** useEffect is a React Hook that lets you perform side effects in functional components. It runs after render and can handle cleanup.

---

## 💻 Code Examples

### Example 1: Basic useState

**File:** `day2/Example1-BasicState.jsx`

Shows how to use useState for a simple counter.

### Example 2: Multiple State Variables

**File:** `day2/Example2-MultipleState.jsx`

Shows managing multiple pieces of state.

### Example 3: useState with Objects

**File:** `day2/Example3-StateObjects.jsx`

Shows how to update state objects correctly.

### Example 4: Basic useEffect

**File:** `day2/Example4-BasicUseEffect.jsx`

Shows useEffect for side effects like API calls.

### Example 5: useEffect with Dependencies

**File:** `day2/Example5-UseEffectDependencies.jsx`

Shows when useEffect runs based on dependencies.

### Example 6: useEffect Cleanup

**File:** `day2/Example6-UseEffectCleanup.jsx`

Shows how to clean up resources in useEffect.

---

## 🎓 Interview Answers

### Q1: What is State?

**Simple Answer:**
State is data that belongs to a component and can change. When state changes, the component updates to show the new information.

**Technical Answer:**
State is a JavaScript object that stores component's mutable data. Unlike props, state is managed within the component and can be updated using setState (class) or useState hook (functional). State changes trigger re-renders.

---

### Q2: What is useState?

**Simple Answer:**
useState is a React hook that gives you a value that can change and a function to update it. You call it once, and it remembers the value.

**Technical Answer:**
useState is a Hook that returns an array with two elements: the current state value and a function to update it. It takes an initial value as argument and preserves state between re-renders.

---

### Q3: What is useEffect?

**Simple Answer:**
useEffect lets you do things after your component renders, like fetching data, setting up subscriptions, or updating the document title.

**Technical Answer:**
useEffect is a Hook that accepts a function and an optional dependency array. It runs after render and can return a cleanup function. It replaces componentDidMount, componentDidUpdate, and componentWillUnmount.

---

## ✅ Practice Exercises

1. Create a toggle button that shows/hides content
2. Build a form with multiple input fields using state
3. Create a timer that updates every second using useEffect
4. Build a component that fetches and displays data

---

## 🔍 Key Takeaways

1. ✅ State is mutable data within a component
2. ✅ useState returns [value, setValue]
3. ✅ useEffect runs after render
4. ✅ Always use setState function, never mutate state directly
5. ✅ useEffect dependencies control when it runs

---

**🎉 Ready for Day 3: Event Handling & Forms!**


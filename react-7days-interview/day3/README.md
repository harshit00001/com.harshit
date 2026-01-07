# Day 3: Event Handling & Forms

## 📚 Learning Objectives

By the end of this day, you will understand:
- How to handle events in React
- Controlled vs Uncontrolled components
- Form handling and validation
- Input types and their handling
- Event object and synthetic events

---

## 🎯 Interview Questions Covered

1. **How do you handle events in React?**
2. **What are Synthetic Events?**
3. **What are Controlled Components?**
4. **How do you handle form submission?**
5. **Difference between Controlled and Uncontrolled components?**
6. **How to prevent default behavior?**

---

## 🚀 Execution Steps

### Step 1: Navigate to Day 3 Folder

```bash
cd day3
```

### Step 2: Run the Development Server

```bash
# From project root
npm run dev
```

### Step 3: Open Examples

Import and test each example in your App.jsx

---

## 📖 Simple Explanation

### Event Handling

**Simple:** When a user clicks a button or types in an input, React lets you respond to those actions with event handlers. It's like having a button that does something when you press it.

**Technical:** React uses SyntheticEvents, a wrapper around native browser events. Event handlers are passed as props (onClick, onChange, etc.) and receive the event object.

### Controlled Components

**Simple:** A controlled component is like a form where React is in charge. Every change goes through React's state, so React always knows what the value is.

**Technical:** Controlled components have their value controlled by React state. The input value is set by state, and onChange updates the state, creating a single source of truth.

### Form Handling

**Simple:** Forms let users enter information. In React, you handle form submission and validate the data before sending it.

**Technical:** Forms use controlled components, handle onSubmit event, prevent default submission, validate data, and update state accordingly.

---

## 💻 Code Examples

### Example 1: Basic Event Handling

**File:** `day3/Example1-BasicEvents.jsx`

Shows onClick, onChange, and other basic event handlers.

### Example 2: Controlled Input

**File:** `day3/Example2-ControlledInput.jsx`

Shows how to create controlled input components.

### Example 3: Form Handling

**File:** `day3/Example3-FormHandling.jsx`

Complete form with validation and submission.

### Example 4: Multiple Inputs

**File:** `day3/Example4-MultipleInputs.jsx`

Handling multiple form inputs efficiently.

### Example 5: Form Validation

**File:** `day3/Example5-FormValidation.jsx`

Real-time form validation with error messages.

---

## 🎓 Interview Answers

### Q1: How do you handle events in React?

**Simple Answer:**
You pass a function to event props like onClick. When the event happens, your function runs.

**Technical Answer:**
React uses SyntheticEvents. Event handlers are camelCase props (onClick, onChange). They receive a SyntheticEvent object. Use preventDefault() to stop default behavior.

---

### Q2: What are Controlled Components?

**Simple Answer:**
Controlled components are inputs where React controls the value through state. Every change updates state, and state controls the value.

**Technical Answer:**
Controlled components have their value controlled by React state via value prop and onChange handler. This creates a single source of truth and enables validation and transformation.

---

## ✅ Practice Exercises

1. Create a login form with email and password
2. Build a search input with debouncing
3. Create a multi-step form
4. Build a form with file upload

---

## 🔍 Key Takeaways

1. ✅ Events use camelCase (onClick, not onclick)
2. ✅ Controlled components use value + onChange
3. ✅ Always prevent default form submission
4. ✅ Validate data before submission
5. ✅ Use event.target.value for input values

---

**🎉 Ready for Day 4: Lists & Conditional Rendering!**


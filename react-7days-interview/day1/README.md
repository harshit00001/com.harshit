# Day 1: React Basics - Components, JSX, and Props

## 📚 Learning Objectives

By the end of this day, you will understand:
- What React is and why we use it
- How to create React components
- Understanding JSX syntax
- How to use Props to pass data
- Difference between functional and class components

---

## 🎯 Interview Questions Covered

1. **What is React?**
2. **What is JSX?**
3. **What are Components?**
4. **What are Props?**
5. **Difference between Props and State?**
6. **Functional vs Class Components?**

---

## 🚀 Execution Steps

### Step 1: Navigate to Day 1 Folder

```bash
cd day1
```

### Step 2: Install Dependencies (if not already installed)

```bash
# From project root
npm install
```

### Step 3: Run the Development Server

```bash
# From project root
npm run dev
```

### Step 4: Open Browser

Navigate to: `http://localhost:3000`

### Step 5: View Day 1 Examples

Open the browser console to see component lifecycle logs.

---

## 📖 Simple Explanation

### What is React?

**Simple:** React is like building blocks for websites. Instead of writing everything from scratch, you create small reusable pieces (components) and put them together.

**Technical:** React is a JavaScript library for building user interfaces. It uses a virtual DOM to efficiently update the UI when data changes.

### What is JSX?

**Simple:** JSX looks like HTML but it's actually JavaScript. It lets you write HTML-like code inside your JavaScript files.

**Technical:** JSX (JavaScript XML) is a syntax extension that allows you to write HTML-like code in JavaScript. It gets transpiled to `React.createElement()` calls.

### What are Components?

**Simple:** Components are like LEGO blocks. Each block (component) does one thing, and you combine them to build your website.

**Technical:** Components are reusable pieces of code that return JSX. They can be functional (functions) or class-based (ES6 classes).

### What are Props?

**Simple:** Props are like passing notes between components. A parent component can send data to a child component.

**Technical:** Props (properties) are read-only data passed from parent to child components. They make components reusable and configurable.

---

## 💻 Code Examples

### Example 1: Basic Functional Component

**File:** `day1/Example1-BasicComponent.jsx`

```jsx
// Simple: This is a basic component that shows a greeting
// Technical: Functional component that returns JSX

function Greeting() {
  return <h1>Hello, React!</h1>;
}

export default Greeting;
```

**How to use:**
```jsx
import Greeting from './day1/Example1-BasicComponent';

function App() {
  return <Greeting />;
}
```

---

### Example 2: Component with Props

**File:** `day1/Example2-Props.jsx`

```jsx
// Simple: This component receives a name and displays it
// Technical: Functional component accepting props as parameter

function Welcome({ name, age }) {
  return (
    <div>
      <h2>Welcome, {name}!</h2>
      <p>You are {age} years old.</p>
    </div>
  );
}

export default Welcome;
```

**How to use:**
```jsx
import Welcome from './day1/Example2-Props';

function App() {
  return (
    <div>
      <Welcome name="John" age={25} />
      <Welcome name="Sarah" age={30} />
    </div>
  );
}
```

**Output:**
- Welcome, John! You are 25 years old.
- Welcome, Sarah! You are 30 years old.

---

### Example 3: JSX Expressions

**File:** `day1/Example3-JSXExpressions.jsx`

```jsx
// Simple: You can use JavaScript inside JSX with curly braces {}
// Technical: JSX allows embedding JavaScript expressions using {}

function Calculator() {
  const a = 10;
  const b = 5;
  const name = "React";
  
  return (
    <div>
      <h2>JSX Expressions Example</h2>
      <p>Addition: {a} + {b} = {a + b}</p>
      <p>Multiplication: {a} × {b} = {a * b}</p>
      <p>Hello, {name.toUpperCase()}!</p>
      <p>Current time: {new Date().toLocaleTimeString()}</p>
    </div>
  );
}

export default Calculator;
```

---

### Example 4: Component Composition

**File:** `day1/Example4-Composition.jsx`

```jsx
// Simple: Building bigger components from smaller ones
// Technical: Component composition - combining multiple components

function Button({ text, onClick }) {
  return <button onClick={onClick}>{text}</button>;
}

function Card({ title, content }) {
  return (
    <div style={{ border: '1px solid #ccc', padding: '20px', margin: '10px' }}>
      <h3>{title}</h3>
      <p>{content}</p>
    </div>
  );
}

function App() {
  return (
    <div>
      <Card 
        title="React Basics" 
        content="Learning components and props"
      />
      <Button 
        text="Click Me" 
        onClick={() => alert('Button clicked!')}
      />
    </div>
  );
}

export default App;
```

---

### Example 5: Props with Default Values

**File:** `day1/Example5-DefaultProps.jsx`

```jsx
// Simple: Setting default values if props are not provided
// Technical: Using default parameters in function components

function UserProfile({ name = "Guest", role = "User", isActive = false }) {
  return (
    <div>
      <h3>User Profile</h3>
      <p>Name: {name}</p>
      <p>Role: {role}</p>
      <p>Status: {isActive ? "Active" : "Inactive"}</p>
    </div>
  );
}

export default UserProfile;
```

**How to use:**
```jsx
// With all props
<UserProfile name="John" role="Admin" isActive={true} />

// With some props (others use defaults)
<UserProfile name="Sarah" />

// With no props (all use defaults)
<UserProfile />
```

---

## 🎓 Interview Answers

### Q1: What is React?

**Simple Answer:**
React is a JavaScript library that helps you build websites by creating reusable pieces called components. It makes websites fast and easy to update.

**Technical Answer:**
React is an open-source JavaScript library developed by Facebook for building user interfaces, particularly web applications. It uses a virtual DOM to optimize rendering performance and follows a component-based architecture.

---

### Q2: What is JSX?

**Simple Answer:**
JSX is like writing HTML inside JavaScript. It looks like HTML but it's actually JavaScript code.

**Technical Answer:**
JSX (JavaScript XML) is a syntax extension for JavaScript that allows you to write HTML-like code. It gets transpiled by Babel into `React.createElement()` calls. JSX makes code more readable and allows embedding JavaScript expressions using curly braces.

---

### Q3: What are Props?

**Simple Answer:**
Props are like messages you send from a parent component to a child component. They're read-only and can't be changed by the child.

**Technical Answer:**
Props (properties) are immutable data passed from parent components to child components. They enable component reusability and data flow in a unidirectional manner. Props are read-only and should not be modified by the receiving component.

---

### Q4: Difference between Props and State?

**Simple Answer:**
- Props: Data passed FROM parent TO child (can't be changed by child)
- State: Data that belongs TO the component (can be changed by the component)

**Technical Answer:**
- Props: Immutable, passed from parent, used for configuration
- State: Mutable, managed within component, triggers re-renders when changed

---

## ✅ Practice Exercises

1. Create a `ProductCard` component that displays product name, price, and description using props
2. Create a `Header` component that accepts a title prop
3. Create a `Button` component that accepts text and onClick handler as props
4. Build a `UserCard` component that shows user information with default values

---

## 🔍 Key Takeaways

1. ✅ Components are reusable pieces of UI
2. ✅ JSX allows writing HTML-like syntax in JavaScript
3. ✅ Props pass data from parent to child
4. ✅ Props are read-only
5. ✅ Functional components are preferred in modern React

---

## 📝 Next Steps

After completing Day 1, you should:
- Understand how to create basic components
- Know how to use props
- Be comfortable with JSX syntax
- Ready to move to Day 2: State Management

---

**🎉 Congratulations on completing Day 1!**


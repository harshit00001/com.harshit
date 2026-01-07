# Day 7: API Integration & Advanced Patterns

## 📚 Learning Objectives

By the end of this day, you will understand:
- How to fetch data from APIs
- Using async/await with React
- Handling loading and error states
- Using Axios for HTTP requests
- Advanced React patterns

---

## 🎯 Interview Questions Covered

1. **How do you fetch data in React?**
2. **What is the difference between fetch and Axios?**
3. **How to handle async operations in useEffect?**
4. **What are loading and error states?**
5. **How to prevent memory leaks in async operations?**
6. **What are React patterns?**

---

## 🚀 Execution Steps

### Step 1: Install Axios (optional)

```bash
npm install axios
```

### Step 2: Navigate to Day 7 Folder

```bash
cd day7
```

### Step 3: Run the Development Server

```bash
npm run dev
```

---

## 📖 Simple Explanation

### API Integration

**Simple:** APIs are like menus at restaurants - you ask for data, and the server gives it to you. In React, you use fetch() or Axios to ask for data, then show it in your component.

**Technical:** API integration involves making HTTP requests (GET, POST, PUT, DELETE) to external services. Use fetch API or libraries like Axios, handle promises with async/await.

### Loading States

**Simple:** While waiting for data to arrive, you show a loading message or spinner. Once data arrives, you show the actual content.

**Technical:** Loading states indicate async operations in progress. Use state to track loading status and conditionally render loading UI.

---

## 💻 Code Examples

### Example 1: Basic Fetch

**File:** `day7/Example1-BasicFetch.jsx`

Simple data fetching with fetch API.

### Example 2: Fetch with Loading & Error

**File:** `day7/Example2-FetchWithStates.jsx`

Handling loading and error states.

### Example 3: Using Axios

**File:** `day7/Example3-UsingAxios.jsx`

HTTP requests with Axios library.

### Example 4: Custom useFetch Hook

**File:** `day7/Example4-CustomUseFetch.jsx`

Reusable data fetching hook.

### Example 5: POST Request

**File:** `day7/Example5-POSTRequest.jsx`

Sending data to API.

---

## 🎓 Interview Answers

### Q1: How do you fetch data in React?

**Simple Answer:**
Use fetch() or Axios in useEffect. Set loading state while fetching, then update state with the data when it arrives.

**Technical Answer:**
Use fetch() or Axios in useEffect hook. Handle async operations with async/await or .then(). Update state with fetched data. Include cleanup to prevent memory leaks.

---

## ✅ Practice Exercises

1. Create a weather app with API
2. Build a todo app with backend API
3. Implement search with API
4. Create a form that submits to API

---

## 🔍 Key Takeaways

1. ✅ Fetch data in useEffect
2. ✅ Handle loading and error states
3. ✅ Clean up async operations
4. ✅ Use async/await or promises
5. ✅ Axios simplifies HTTP requests

---

**🎉 Congratulations! You've completed 7 days of React learning!**


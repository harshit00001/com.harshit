/**
 * Example 2: Component with Props
 * 
 * SIMPLE EXPLANATION:
 * Props are like passing information to a component. Just like you pass
 * arguments to a function, you pass props to a component. This makes the
 * component reusable - you can use the same component with different data.
 * 
 * TECHNICAL EXPLANATION:
 * Props (properties) are read-only data passed from parent to child components.
 * They're passed as attributes in JSX and received as function parameters.
 * Props enable component reusability and unidirectional data flow.
 * 
 * INTERVIEW POINT:
 * - Props are immutable (can't be changed by child)
 * - Props enable component reusability
 * - Props flow down from parent to child (unidirectional)
 */

function Welcome({ name, age, city }) {
  return (
    <div style={{ 
      padding: '20px', 
      border: '2px solid #4CAF50', 
      borderRadius: '8px',
      margin: '10px',
      backgroundColor: '#f0f8f0'
    }}>
      <h2>Welcome, {name}!</h2>
      <p>Age: {age} years old</p>
      <p>City: {city}</p>
      <p>Status: Active User</p>
    </div>
  );
}

// Usage Example:
// <Welcome name="John" age={25} city="New York" />
// <Welcome name="Sarah" age={30} city="London" />

export default Welcome;


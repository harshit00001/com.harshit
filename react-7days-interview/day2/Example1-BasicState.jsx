/**
 * Example 1: Basic useState Hook
 * 
 * SIMPLE EXPLANATION:
 * useState lets you remember a value that can change. When you change it,
 * React automatically updates what you see on screen. It's like having
 * a variable that, when changed, updates your webpage automatically.
 * 
 * TECHNICAL EXPLANATION:
 * useState is a React Hook that adds state to functional components.
 * It returns an array with two elements: [currentState, setStateFunction].
 * Calling setState triggers a re-render with the new state value.
 * 
 * INTERVIEW POINT:
 * - useState is a Hook (must be called at top level)
 * - Returns [value, setValue]
 * - State updates are asynchronous
 * - Multiple useState calls for multiple state values
 */

import { useState } from 'react';

function Counter() {
  // Simple: We're creating a counter that starts at 0
  // Technical: useState(0) returns [count, setCount] where count=0 initially
  const [count, setCount] = useState(0);
  
  return (
    <div style={{
      padding: '30px',
      textAlign: 'center',
      backgroundColor: '#f0f8ff',
      borderRadius: '10px',
      maxWidth: '400px',
      margin: '20px auto',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Counter Example</h2>
      <div style={{
        fontSize: '48px',
        fontWeight: 'bold',
        color: '#2196F3',
        margin: '20px 0'
      }}>
        {count}
      </div>
      
      <div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
        {/* Simple: When clicked, decrease count by 1 */}
        {/* Technical: setCount updates state, triggering re-render */}
        <button 
          onClick={() => setCount(count - 1)}
          style={{
            padding: '10px 20px',
            fontSize: '18px',
            backgroundColor: '#f44336',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Decrease (-)
        </button>
        
        {/* Reset to 0 */}
        <button 
          onClick={() => setCount(0)}
          style={{
            padding: '10px 20px',
            fontSize: '18px',
            backgroundColor: '#ff9800',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Reset
        </button>
        
        {/* Increase by 1 */}
        <button 
          onClick={() => setCount(count + 1)}
          style={{
            padding: '10px 20px',
            fontSize: '18px',
            backgroundColor: '#4CAF50',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Increase (+)
        </button>
      </div>
      
      <div style={{ marginTop: '20px', padding: '15px', backgroundColor: '#e3f2fd', borderRadius: '5px' }}>
        <p style={{ margin: '5px 0', fontSize: '14px' }}>
          <strong>Current State:</strong> {count}
        </p>
        <p style={{ margin: '5px 0', fontSize: '14px' }}>
          <strong>State Type:</strong> {typeof count}
        </p>
      </div>
    </div>
  );
}

export default Counter;


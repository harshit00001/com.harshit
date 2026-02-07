/**
 * Example 5: useEffect with Dependencies
 * 
 * SIMPLE EXPLANATION:
 * useEffect can run at different times depending on what you put in the
 * dependency array. No array = runs every time. Empty array [] = runs once.
 * [value] = runs when value changes.
 * 
 * TECHNICAL EXPLANATION:
 * useEffect accepts a dependency array as second argument. It controls
 * when the effect runs:
 * - No array: runs after every render
 * - []: runs once after mount (like componentDidMount)
 * - [deps]: runs when dependencies change (like componentDidUpdate)
 * 
 * INTERVIEW POINT:
 * - Dependency array controls effect execution
 * - Missing dependencies cause bugs
 * - ESLint warns about missing dependencies
 */

import { useState, useEffect } from 'react';

function EffectDependencies() {
  const [count, setCount] = useState(0);
  const [name, setName] = useState('');
  const [renderCount, setRenderCount] = useState(0);
  
  // Track renders
  useEffect(() => {
    setRenderCount(prev => prev + 1);
  }); // No dependency array - runs after EVERY render
  
  // Effect 1: Runs only once (on mount)
  useEffect(() => {
    console.log('Effect 1: Runs only once on mount');
    document.title = 'Component Mounted';
  }, []); // Empty array = runs once
  
  // Effect 2: Runs when count changes
  useEffect(() => {
    console.log('Effect 2: Count changed to', count);
  }, [count]); // Runs when count changes
  
  // Effect 3: Runs when name changes
  useEffect(() => {
    console.log('Effect 3: Name changed to', name);
  }, [name]); // Runs when name changes
  
  // Effect 4: Runs when count OR name changes
  useEffect(() => {
    console.log('Effect 4: Count or name changed');
  }, [count, name]); // Runs when either changes
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '600px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>useEffect Dependencies Example</h2>
      <p style={{ color: '#666' }}>Open browser console to see effect logs</p>
      
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px'
      }}>
        <p><strong>Render Count:</strong> {renderCount}</p>
        <p style={{ fontSize: '14px', color: '#666' }}>
          This effect has no dependencies, so it runs on every render
        </p>
      </div>
      
      <div style={{ marginTop: '20px' }}>
        <h3>Count: {count}</h3>
        <button
          onClick={() => setCount(count + 1)}
          style={{
            padding: '10px 20px',
            backgroundColor: '#2196F3',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer',
            marginRight: '10px'
          }}
        >
          Increment Count
        </button>
        <span style={{ fontSize: '14px', color: '#666' }}>
          (Triggers effects with [count] dependency)
        </span>
      </div>
      
      <div style={{ marginTop: '20px' }}>
        <h3>Name: {name || '(empty)'}</h3>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Type your name"
          style={{
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px',
            width: '200px'
          }}
        />
        <span style={{ fontSize: '14px', color: '#666', marginLeft: '10px' }}>
          (Triggers effects with [name] dependency)
        </span>
      </div>
      
      <div style={{
        marginTop: '30px',
        padding: '20px',
        backgroundColor: '#f5f5f5',
        borderRadius: '5px'
      }}>
        <h3>💡 Dependency Array Rules:</h3>
        <ul style={{ lineHeight: '1.8' }}>
          <li><strong>No array:</strong> Runs after every render</li>
          <li><strong>[]:</strong> Runs once after mount</li>
          <li><strong>[count]:</strong> Runs when count changes</li>
          <li><strong>[count, name]:</strong> Runs when count OR name changes</li>
        </ul>
      </div>
    </div>
  );
}

export default EffectDependencies;









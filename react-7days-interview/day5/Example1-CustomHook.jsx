/**
 * Example 1: Custom Hooks
 * 
 * SIMPLE EXPLANATION:
 * Custom hooks are like reusable functions that can use React hooks.
 * Instead of writing the same logic in multiple components, you create
 * a custom hook once and use it everywhere. Like a recipe you can
 * use in different dishes.
 * 
 * TECHNICAL EXPLANATION:
 * Custom hooks are JavaScript functions that:
 * - Start with "use" (naming convention)
 * - Can call other hooks
 * - Enable logic reuse between components
 * - Must follow rules of hooks
 * - Can return values, functions, or objects
 */

import { useState, useEffect } from 'react';

// Custom Hook 1: useCounter
// Simple: A hook that manages a counter with increment/decrement
// Technical: Encapsulates counter logic for reuse
function useCounter(initialValue = 0, step = 1) {
  const [count, setCount] = useState(initialValue);
  
  const increment = () => setCount(prev => prev + step);
  const decrement = () => setCount(prev => prev - step);
  const reset = () => setCount(initialValue);
  
  return { count, increment, decrement, reset };
}

// Custom Hook 2: useLocalStorage
// Simple: Saves and loads data from browser storage
// Technical: Syncs state with localStorage
function useLocalStorage(key, initialValue) {
  const [storedValue, setStoredValue] = useState(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      return initialValue;
    }
  });
  
  const setValue = (value) => {
    try {
      setStoredValue(value);
      window.localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error('Error saving to localStorage:', error);
    }
  };
  
  return [storedValue, setValue];
}

// Custom Hook 3: useWindowSize
// Simple: Tracks window size changes
// Technical: Uses useEffect to listen to resize events
function useWindowSize() {
  const [windowSize, setWindowSize] = useState({
    width: window.innerWidth,
    height: window.innerHeight
  });
  
  useEffect(() => {
    const handleResize = () => {
      setWindowSize({
        width: window.innerWidth,
        height: window.innerHeight
      });
    };
    
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);
  
  return windowSize;
}

// Component using useCounter hook
function CounterComponent() {
  const { count, increment, decrement, reset } = useCounter(0, 1);
  
  return (
    <div style={{
      padding: '20px',
      backgroundColor: '#e3f2fd',
      borderRadius: '8px',
      marginBottom: '20px'
    }}>
      <h3>Counter using Custom Hook</h3>
      <p style={{ fontSize: '24px', fontWeight: 'bold' }}>Count: {count}</p>
      <div style={{ display: 'flex', gap: '10px' }}>
        <button onClick={decrement} style={buttonStyle}>-</button>
        <button onClick={reset} style={buttonStyle}>Reset</button>
        <button onClick={increment} style={buttonStyle}>+</button>
      </div>
    </div>
  );
}

// Component using useLocalStorage hook
function LocalStorageComponent() {
  const [name, setName] = useLocalStorage('userName', '');
  
  return (
    <div style={{
      padding: '20px',
      backgroundColor: '#f3e5f5',
      borderRadius: '8px',
      marginBottom: '20px'
    }}>
      <h3>LocalStorage using Custom Hook</h3>
      <input
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="Enter your name"
        style={{
          padding: '10px',
          border: '1px solid #ddd',
          borderRadius: '5px',
          width: '200px',
          marginRight: '10px'
        }}
      />
      <p style={{ marginTop: '10px' }}>
        Your name is saved: <strong>{name || '(empty)'}</strong>
      </p>
      <p style={{ fontSize: '12px', color: '#666' }}>
        Refresh the page - your name will still be here!
      </p>
    </div>
  );
}

// Component using useWindowSize hook
function WindowSizeComponent() {
  const windowSize = useWindowSize();
  
  return (
    <div style={{
      padding: '20px',
      backgroundColor: '#fff3cd',
      borderRadius: '8px',
      marginBottom: '20px'
    }}>
      <h3>Window Size using Custom Hook</h3>
      <p>Width: <strong>{windowSize.width}px</strong></p>
      <p>Height: <strong>{windowSize.height}px</strong></p>
      <p style={{ fontSize: '12px', color: '#666' }}>
        Resize your browser window to see it update!
      </p>
    </div>
  );
}

// Main component combining all custom hooks
function CustomHooksExample() {
  return (
    <div style={{
      padding: '30px',
      maxWidth: '600px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Custom Hooks Examples</h2>
      <p style={{ color: '#666', marginBottom: '30px' }}>
        Custom hooks let you reuse logic across components
      </p>
      
      <CounterComponent />
      <LocalStorageComponent />
      <WindowSizeComponent />
      
      <div style={{
        marginTop: '30px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Key Points:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li>Custom hooks start with "use"</li>
          <li>Can use other hooks inside</li>
          <li>Enable logic reuse</li>
          <li>Must follow rules of hooks</li>
          <li>Can return values, functions, or objects</li>
        </ul>
      </div>
    </div>
  );
}

const buttonStyle = {
  padding: '8px 15px',
  backgroundColor: '#2196F3',
  color: 'white',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  fontSize: '16px'
};

export default CustomHooksExample;









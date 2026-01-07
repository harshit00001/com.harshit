/**
 * Example 4: Basic useEffect Hook
 * 
 * SIMPLE EXPLANATION:
 * useEffect lets you do things after your component appears on screen.
 * Like fetching data from the internet, updating the page title, or
 * setting up a timer. It runs automatically after React draws your
 * component on screen.
 * 
 * TECHNICAL EXPLANATION:
 * useEffect is a Hook that lets you perform side effects in functional
 * components. It runs after every render by default. It replaces
 * componentDidMount, componentDidUpdate, and componentWillUnmount
 * from class components.
 * 
 * INTERVIEW POINT:
 * - useEffect runs after render
 * - Can perform side effects (API calls, subscriptions, etc.)
 * - Returns cleanup function (optional)
 * - Dependency array controls when it runs
 */

import { useState, useEffect } from 'react';

function Timer() {
  const [seconds, setSeconds] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  
  // Simple: This runs after the component appears and whenever seconds changes
  // Technical: useEffect with [seconds] dependency runs when seconds changes
  useEffect(() => {
    if (isRunning) {
      // Set up a timer that runs every 1000ms (1 second)
      const interval = setInterval(() => {
        setSeconds(prevSeconds => prevSeconds + 1);
      }, 1000);
      
      // Cleanup: Remove the timer when component unmounts or isRunning changes
      return () => clearInterval(interval);
    }
  }, [isRunning]); // Only run when isRunning changes
  
  return (
    <div style={{
      padding: '30px',
      textAlign: 'center',
      maxWidth: '400px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Timer with useEffect</h2>
      <div style={{
        fontSize: '48px',
        fontWeight: 'bold',
        color: '#2196F3',
        margin: '20px 0'
      }}>
        {seconds}s
      </div>
      
      <div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
        <button
          onClick={() => setIsRunning(!isRunning)}
          style={{
            padding: '10px 20px',
            fontSize: '16px',
            backgroundColor: isRunning ? '#f44336' : '#4CAF50',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          {isRunning ? 'Stop' : 'Start'}
        </button>
        
        <button
          onClick={() => {
            setSeconds(0);
            setIsRunning(false);
          }}
          style={{
            padding: '10px 20px',
            fontSize: '16px',
            backgroundColor: '#ff9800',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Reset
        </button>
      </div>
      
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#f5f5f5',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 useEffect:</strong> Manages the timer interval</p>
        <p>Runs when isRunning changes</p>
      </div>
    </div>
  );
}

// Example: Updating document title
function DocumentTitleUpdater() {
  const [count, setCount] = useState(0);
  
  // Simple: This changes the browser tab title
  // Technical: Side effect - updating document.title
  useEffect(() => {
    document.title = `Count: ${count}`;
  }, [count]); // Run whenever count changes
  
  return (
    <div style={{
      padding: '20px',
      textAlign: 'center',
      backgroundColor: '#fff3cd',
      borderRadius: '10px',
      margin: '20px auto',
      maxWidth: '400px'
    }}>
      <h3>Document Title Updater</h3>
      <p>Check your browser tab title!</p>
      <p style={{ fontSize: '24px', fontWeight: 'bold' }}>Count: {count}</p>
      <button
        onClick={() => setCount(count + 1)}
        style={{
          padding: '10px 20px',
          fontSize: '16px',
          backgroundColor: '#2196F3',
          color: 'white',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer'
        }}
      >
        Increment
      </button>
    </div>
  );
}

// Combined example
function App() {
  return (
    <div style={{ padding: '20px' }}>
      <Timer />
      <DocumentTitleUpdater />
    </div>
  );
}

export default App;


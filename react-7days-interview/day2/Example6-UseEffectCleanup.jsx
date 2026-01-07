/**
 * Example 6: useEffect Cleanup
 * 
 * SIMPLE EXPLANATION:
 * Sometimes your effect sets up something that needs to be cleaned up,
 * like a timer or subscription. The cleanup function runs before the
 * effect runs again, or when the component is removed from the screen.
 * 
 * TECHNICAL EXPLANATION:
 * useEffect can return a cleanup function. This function runs:
 * - Before the effect runs again (if dependencies changed)
 * - When the component unmounts
 * - Prevents memory leaks and unwanted side effects
 * 
 * INTERVIEW POINT:
 * - Cleanup prevents memory leaks
 * - Always cleanup subscriptions, timers, event listeners
 * - Cleanup runs before next effect or on unmount
 */

import { useState, useEffect } from 'react';

function TimerWithCleanup() {
  const [seconds, setSeconds] = useState(0);
  const [isActive, setIsActive] = useState(false);
  
  useEffect(() => {
    let interval = null;
    
    if (isActive) {
      // Set up interval
      interval = setInterval(() => {
        setSeconds(seconds => seconds + 1);
      }, 1000);
      console.log('Timer started');
    }
    
    // Cleanup function
    // Simple: This runs when component is removed or isActive changes
    // Technical: Cleanup runs before effect re-runs or on unmount
    return () => {
      if (interval) {
        clearInterval(interval);
        console.log('Timer cleaned up');
      }
    };
  }, [isActive]); // Effect runs when isActive changes
  
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
      <h2>Timer with Cleanup</h2>
      <p style={{ color: '#666' }}>Check console for cleanup logs</p>
      
      <div style={{
        fontSize: '48px',
        fontWeight: 'bold',
        color: '#2196F3',
        margin: '20px 0'
      }}>
        {seconds}s
      </div>
      
      <button
        onClick={() => setIsActive(!isActive)}
        style={{
          padding: '10px 20px',
          fontSize: '16px',
          backgroundColor: isActive ? '#f44336' : '#4CAF50',
          color: 'white',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer',
          marginRight: '10px'
        }}
      >
        {isActive ? 'Stop' : 'Start'}
      </button>
      
      <button
        onClick={() => {
          setSeconds(0);
          setIsActive(false);
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
      
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Cleanup:</strong> Interval is cleared when:</p>
        <ul style={{ textAlign: 'left', lineHeight: '1.8' }}>
          <li>Component unmounts</li>
          <li>isActive changes</li>
          <li>Prevents memory leaks</li>
        </ul>
      </div>
    </div>
  );
}

// Example: Window resize listener with cleanup
function WindowSizeTracker() {
  const [windowSize, setWindowSize] = useState({
    width: window.innerWidth,
    height: window.innerHeight
  });
  
  useEffect(() => {
    // Simple: This function runs when window size changes
    // Technical: Event listener for resize event
    const handleResize = () => {
      setWindowSize({
        width: window.innerWidth,
        height: window.innerHeight
      });
    };
    
    // Add event listener
    window.addEventListener('resize', handleResize);
    console.log('Resize listener added');
    
    // Cleanup: Remove event listener
    // Simple: This removes the listener when component is removed
    // Technical: Prevents memory leaks and unwanted event handlers
    return () => {
      window.removeEventListener('resize', handleResize);
      console.log('Resize listener removed');
    };
  }, []); // Empty array = runs once on mount
  
  return (
    <div style={{
      padding: '20px',
      margin: '20px auto',
      maxWidth: '400px',
      backgroundColor: '#fff3cd',
      borderRadius: '10px'
    }}>
      <h3>Window Size Tracker</h3>
      <p>Resize your browser window!</p>
      <p><strong>Width:</strong> {windowSize.width}px</p>
      <p><strong>Height:</strong> {windowSize.height}px</p>
      <p style={{ fontSize: '12px', color: '#666' }}>
        Check console - listener is cleaned up on unmount
      </p>
    </div>
  );
}

// Combined example
function App() {
  return (
    <div style={{ padding: '20px' }}>
      <TimerWithCleanup />
      <WindowSizeTracker />
    </div>
  );
}

export default App;


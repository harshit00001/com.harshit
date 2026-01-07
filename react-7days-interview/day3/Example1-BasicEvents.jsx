/**
 * Example 1: Basic Event Handling
 * 
 * SIMPLE EXPLANATION:
 * Events are things that happen when users interact with your app - like
 * clicking a button, typing in an input, or hovering over something.
 * React lets you respond to these events with functions called event handlers.
 * 
 * TECHNICAL EXPLANATION:
 * React uses SyntheticEvents, a cross-browser wrapper around native events.
 * Event handlers are passed as props (onClick, onChange, onMouseOver, etc.)
 * and receive a SyntheticEvent object. React pools events for performance.
 * 
 * INTERVIEW POINT:
 * - Events use camelCase (onClick, not onclick)
 * - Pass function reference, not function call (onClick={handleClick}, not onClick={handleClick()})
 * - Event object has preventDefault() and stopPropagation()
 */

import { useState } from 'react';

function BasicEvents() {
  const [message, setMessage] = useState('Click a button!');
  const [clickCount, setClickCount] = useState(0);
  
  // Simple: This function runs when button is clicked
  // Technical: Event handler function that receives SyntheticEvent
  const handleClick = (e) => {
    console.log('Button clicked!', e);
    setMessage('Button was clicked!');
    setClickCount(prev => prev + 1);
  };
  
  // Handler with parameter
  const handleButtonClick = (buttonName) => {
    setMessage(`You clicked ${buttonName}!`);
  };
  
  // Handler that uses event object
  const handleMouseOver = (e) => {
    console.log('Mouse position:', e.clientX, e.clientY);
    setMessage('Mouse is over the button!');
  };
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '600px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Basic Event Handling</h2>
      
      <div style={{
        padding: '20px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        marginBottom: '20px',
        textAlign: 'center'
      }}>
        <p style={{ fontSize: '18px', fontWeight: 'bold' }}>{message}</p>
        <p style={{ fontSize: '14px', color: '#666' }}>Click Count: {clickCount}</p>
      </div>
      
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px', marginBottom: '20px' }}>
        {/* onClick event */}
        <button
          onClick={handleClick}
          style={{
            padding: '10px 20px',
            backgroundColor: '#2196F3',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer',
            fontSize: '16px'
          }}
        >
          Click Me
        </button>
        
        {/* onClick with inline function */}
        <button
          onClick={() => handleButtonClick('Button 1')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#4CAF50',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer',
            fontSize: '16px'
          }}
        >
          Button 1
        </button>
        
        {/* onClick with parameter */}
        <button
          onClick={() => handleButtonClick('Button 2')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#ff9800',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer',
            fontSize: '16px'
          }}
        >
          Button 2
        </button>
        
        {/* onMouseOver event */}
        <button
          onMouseOver={handleMouseOver}
          onMouseLeave={() => setMessage('Mouse left the button')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#9c27b0',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer',
            fontSize: '16px'
          }}
        >
          Hover Me
        </button>
        
        {/* onDoubleClick event */}
        <button
          onDoubleClick={() => setMessage('Double clicked!')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#f44336',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer',
            fontSize: '16px'
          }}
        >
          Double Click Me
        </button>
      </div>
      
      <div style={{
        padding: '15px',
        backgroundColor: '#f5f5f5',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <h3>💡 Event Types:</h3>
        <ul style={{ lineHeight: '1.8' }}>
          <li><strong>onClick:</strong> Mouse click</li>
          <li><strong>onChange:</strong> Input value change</li>
          <li><strong>onMouseOver:</strong> Mouse enters element</li>
          <li><strong>onMouseLeave:</strong> Mouse leaves element</li>
          <li><strong>onDoubleClick:</strong> Double mouse click</li>
          <li><strong>onSubmit:</strong> Form submission</li>
        </ul>
      </div>
    </div>
  );
}

export default BasicEvents;


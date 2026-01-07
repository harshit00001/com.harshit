/**
 * Example 2: Controlled Input
 * 
 * SIMPLE EXPLANATION:
 * A controlled input is like a form where React is the boss. React stores
 * the value in state, and every time you type, it updates the state. React
 * always knows exactly what's in the input.
 * 
 * TECHNICAL EXPLANATION:
 * Controlled components have their value controlled by React state. The input
 * receives its value from state via the value prop, and onChange updates the
 * state. This creates a single source of truth and enables validation and
 * transformation of input values.
 * 
 * INTERVIEW POINT:
 * - Controlled: value + onChange (React controls)
 * - Uncontrolled: ref or defaultValue (DOM controls)
 * - Controlled is preferred for forms
 */

import { useState } from 'react';

function ControlledInput() {
  // Simple: We store what the user types in state
  // Technical: State controls the input value
  const [inputValue, setInputValue] = useState('');
  const [textareaValue, setTextareaValue] = useState('');
  const [selectedOption, setSelectedOption] = useState('');
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '600px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Controlled Input Components</h2>
      
      {/* Text Input */}
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Text Input (Controlled):
        </label>
        <input
          type="text"
          value={inputValue}  // Simple: Value comes from state
          onChange={(e) => setInputValue(e.target.value)}  // Simple: Update state when typing
          placeholder="Type something..."
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px',
            fontSize: '16px'
          }}
        />
        <p style={{ marginTop: '5px', color: '#666', fontSize: '14px' }}>
          You typed: <strong>{inputValue}</strong>
        </p>
        <p style={{ marginTop: '5px', color: '#666', fontSize: '14px' }}>
          Character count: {inputValue.length}
        </p>
      </div>
      
      {/* Textarea */}
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Textarea (Controlled):
        </label>
        <textarea
          value={textareaValue}
          onChange={(e) => setTextareaValue(e.target.value)}
          placeholder="Type a message..."
          rows={4}
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px',
            fontSize: '16px',
            fontFamily: 'inherit'
          }}
        />
        <p style={{ marginTop: '5px', color: '#666', fontSize: '14px' }}>
          Word count: {textareaValue.trim().split(/\s+/).filter(Boolean).length}
        </p>
      </div>
      
      {/* Select Dropdown */}
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Select Dropdown (Controlled):
        </label>
        <select
          value={selectedOption}
          onChange={(e) => setSelectedOption(e.target.value)}
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px',
            fontSize: '16px'
          }}
        >
          <option value="">Choose an option...</option>
          <option value="react">React</option>
          <option value="vue">Vue</option>
          <option value="angular">Angular</option>
          <option value="svelte">Svelte</option>
        </select>
        {selectedOption && (
          <p style={{ marginTop: '5px', color: '#666', fontSize: '14px' }}>
            Selected: <strong>{selectedOption}</strong>
          </p>
        )}
      </div>
      
      {/* Checkbox */}
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <input
            type="checkbox"
            checked={selectedOption === 'react'}
            onChange={(e) => setSelectedOption(e.target.checked ? 'react' : '')}
            style={{ width: '20px', height: '20px' }}
          />
          <span>I love React!</span>
        </label>
      </div>
      
      {/* Display all values */}
      <div style={{
        marginTop: '30px',
        padding: '20px',
        backgroundColor: '#f5f5f5',
        borderRadius: '5px'
      }}>
        <h3>Current State Values:</h3>
        <pre style={{
          backgroundColor: '#fff',
          padding: '15px',
          borderRadius: '5px',
          overflow: 'auto',
          fontSize: '14px'
        }}>
          {JSON.stringify({
            inputValue,
            textareaValue,
            selectedOption
          }, null, 2)}
        </pre>
      </div>
      
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Key Points:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li>Value comes from state (value={state})</li>
          <li>onChange updates state (onChange={(e) => setState(e.target.value)})</li>
          <li>React controls the input completely</li>
          <li>Enables validation and transformation</li>
        </ul>
      </div>
    </div>
  );
}

export default ControlledInput;


/**
 * Example 3: useState with Objects
 * 
 * SIMPLE EXPLANATION:
 * When you store an object in state, you need to be careful. You can't
 * just change one property - you need to create a new object with all
 * the properties. It's like making a copy of a form and filling in the changes.
 * 
 * TECHNICAL EXPLANATION:
 * React state should be treated as immutable. When updating object state,
 * you must create a new object using spread operator or Object.assign.
 * Direct mutation won't trigger re-renders.
 * 
 * INTERVIEW POINT:
 * - Never mutate state directly
 * - Use spread operator to create new objects
 * - React uses Object.is() for comparison
 */

import { useState } from 'react';

function UserForm() {
  // Simple: We're storing an object with user information
  // Technical: State object that will be updated immutably
  const [user, setUser] = useState({
    name: '',
    email: '',
    age: 0,
    city: ''
  });
  
  // Simple: When input changes, we create a new object with the updated value
  // Technical: Using spread operator to maintain immutability
  const handleChange = (field, value) => {
    setUser({
      ...user,        // Copy all existing properties
      [field]: value  // Update only the changed field
    });
  };
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '500px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>User Form with Object State</h2>
      
      <div style={{ marginBottom: '15px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Name:
        </label>
        <input
          type="text"
          value={user.name}
          onChange={(e) => handleChange('name', e.target.value)}
          placeholder="Enter name"
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px'
          }}
        />
      </div>
      
      <div style={{ marginBottom: '15px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Email:
        </label>
        <input
          type="email"
          value={user.email}
          onChange={(e) => handleChange('email', e.target.value)}
          placeholder="Enter email"
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px'
          }}
        />
      </div>
      
      <div style={{ marginBottom: '15px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Age:
        </label>
        <input
          type="number"
          value={user.age}
          onChange={(e) => handleChange('age', Number(e.target.value))}
          placeholder="Enter age"
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px'
          }}
        />
      </div>
      
      <div style={{ marginBottom: '15px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          City:
        </label>
        <input
          type="text"
          value={user.city}
          onChange={(e) => handleChange('city', e.target.value)}
          placeholder="Enter city"
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px'
          }}
        />
      </div>
      
      {/* Display current state */}
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#f5f5f5',
        borderRadius: '5px'
      }}>
        <h3>Current State:</h3>
        <pre style={{
          backgroundColor: '#fff',
          padding: '10px',
          borderRadius: '5px',
          overflow: 'auto'
        }}>
          {JSON.stringify(user, null, 2)}
        </pre>
      </div>
      
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#fff3cd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>⚠️ Important:</strong> Always create a new object when updating state!</p>
        <p><strong>❌ Wrong:</strong> user.name = 'John' (direct mutation)</p>
        <p><strong>✅ Correct:</strong> setUser({...user, name: 'John'})</p>
      </div>
    </div>
  );
}

export default UserForm;


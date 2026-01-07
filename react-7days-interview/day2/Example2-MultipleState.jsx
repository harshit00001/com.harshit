/**
 * Example 2: Multiple State Variables
 * 
 * SIMPLE EXPLANATION:
 * You can have multiple pieces of state in one component. Each useState
 * call creates its own separate memory. Like having multiple boxes,
 * each storing different things.
 * 
 * TECHNICAL EXPLANATION:
 * You can call useState multiple times in a component. Each call is
 * independent and manages its own state. This is preferred over storing
 * all state in a single object when values are unrelated.
 * 
 * INTERVIEW POINT:
 * - Multiple useState calls for unrelated state
 * - Each state is independent
 * - Can update them separately
 */

import { useState } from 'react';

function UserProfile() {
  // Multiple independent state variables
  const [name, setName] = useState('');
  const [age, setAge] = useState(0);
  const [email, setEmail] = useState('');
  const [isActive, setIsActive] = useState(false);
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '500px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>User Profile with Multiple State</h2>
      
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Name:
        </label>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Enter your name"
          style={{
            width: '100%',
            padding: '10px',
            fontSize: '16px',
            border: '1px solid #ddd',
            borderRadius: '5px'
          }}
        />
      </div>
      
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Age:
        </label>
        <input
          type="number"
          value={age}
          onChange={(e) => setAge(Number(e.target.value))}
          placeholder="Enter your age"
          style={{
            width: '100%',
            padding: '10px',
            fontSize: '16px',
            border: '1px solid #ddd',
            borderRadius: '5px'
          }}
        />
      </div>
      
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Email:
        </label>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Enter your email"
          style={{
            width: '100%',
            padding: '10px',
            fontSize: '16px',
            border: '1px solid #ddd',
            borderRadius: '5px'
          }}
        />
      </div>
      
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <input
            type="checkbox"
            checked={isActive}
            onChange={(e) => setIsActive(e.target.checked)}
            style={{ width: '20px', height: '20px' }}
          />
          <span>Active User</span>
        </label>
      </div>
      
      {/* Display current state values */}
      <div style={{
        marginTop: '30px',
        padding: '20px',
        backgroundColor: '#f5f5f5',
        borderRadius: '5px'
      }}>
        <h3>Current State Values:</h3>
        <p><strong>Name:</strong> {name || 'Not set'}</p>
        <p><strong>Age:</strong> {age || 'Not set'}</p>
        <p><strong>Email:</strong> {email || 'Not set'}</p>
        <p><strong>Status:</strong> {isActive ? '✅ Active' : '❌ Inactive'}</p>
      </div>
      
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Key Point:</strong> Each useState manages independent state.</p>
        <p>Changing one doesn't affect the others.</p>
      </div>
    </div>
  );
}

export default UserProfile;


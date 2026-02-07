/**
 * Example 3: Conditional Rendering
 * 
 * SIMPLE EXPLANATION:
 * Sometimes you want to show something only if a condition is true.
 * Like showing a message if there are no items, or showing a button
 * only if the user is logged in.
 * 
 * TECHNICAL EXPLANATION:
 * Use JavaScript operators (&&, ||, ? :) or if statements to
 * conditionally render JSX. React will only render the JSX if the
 * condition evaluates to true.
 */

import { useState } from 'react';

function ConditionalRendering() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [items, setItems] = useState([]);
  const [userRole, setUserRole] = useState('guest');
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '600px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Conditional Rendering Examples</h2>
      
      {/* Method 1: Using && operator */}
      <div style={{ marginBottom: '30px', padding: '15px', backgroundColor: '#f5f5f5', borderRadius: '5px' }}>
        <h3>Method 1: && Operator</h3>
        <p>Show message only if user is logged in:</p>
        {isLoggedIn && (
          <div style={{
            padding: '10px',
            backgroundColor: '#4CAF50',
            color: 'white',
            borderRadius: '5px',
            marginTop: '10px'
          }}>
            ✅ Welcome! You are logged in.
          </div>
        )}
        <button
          onClick={() => setIsLoggedIn(!isLoggedIn)}
          style={{
            marginTop: '10px',
            padding: '8px 15px',
            backgroundColor: isLoggedIn ? '#f44336' : '#2196F3',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          {isLoggedIn ? 'Logout' : 'Login'}
        </button>
      </div>
      
      {/* Method 2: Using ternary operator */}
      <div style={{ marginBottom: '30px', padding: '15px', backgroundColor: '#f5f5f5', borderRadius: '5px' }}>
        <h3>Method 2: Ternary Operator (? :)</h3>
        <p>Show different content based on condition:</p>
        {items.length === 0 ? (
          <div style={{
            padding: '15px',
            backgroundColor: '#ff9800',
            color: 'white',
            borderRadius: '5px',
            marginTop: '10px',
            textAlign: 'center'
          }}>
            📭 No items in the list
          </div>
        ) : (
          <div style={{
            padding: '15px',
            backgroundColor: '#4CAF50',
            color: 'white',
            borderRadius: '5px',
            marginTop: '10px',
            textAlign: 'center'
          }}>
            📦 You have {items.length} item(s)
          </div>
        )}
        <div style={{ marginTop: '10px' }}>
          <button
            onClick={() => setItems([...items, `Item ${items.length + 1}`])}
            style={{
              padding: '8px 15px',
              backgroundColor: '#2196F3',
              color: 'white',
              border: 'none',
              borderRadius: '5px',
              cursor: 'pointer',
              marginRight: '10px'
            }}
          >
            Add Item
          </button>
          <button
            onClick={() => setItems([])}
            style={{
              padding: '8px 15px',
              backgroundColor: '#f44336',
              color: 'white',
              border: 'none',
              borderRadius: '5px',
              cursor: 'pointer'
            }}
          >
            Clear
          </button>
        </div>
      </div>
      
      {/* Method 3: Multiple conditions */}
      <div style={{ marginBottom: '30px', padding: '15px', backgroundColor: '#f5f5f5', borderRadius: '5px' }}>
        <h3>Method 3: Multiple Conditions</h3>
        <p>Show content based on user role:</p>
        <select
          value={userRole}
          onChange={(e) => setUserRole(e.target.value)}
          style={{
            padding: '8px',
            marginTop: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px',
            width: '200px'
          }}
        >
          <option value="guest">Guest</option>
          <option value="user">User</option>
          <option value="admin">Admin</option>
        </select>
        
        <div style={{ marginTop: '15px' }}>
          {userRole === 'admin' && (
            <div style={{
              padding: '10px',
              backgroundColor: '#f44336',
              color: 'white',
              borderRadius: '5px',
              marginBottom: '10px'
            }}>
              🔐 Admin Panel - Full Access
            </div>
          )}
          {userRole === 'user' && (
            <div style={{
              padding: '10px',
              backgroundColor: '#2196F3',
              color: 'white',
              borderRadius: '5px',
              marginBottom: '10px'
            }}>
              👤 User Dashboard - Limited Access
            </div>
          )}
          {userRole === 'guest' && (
            <div style={{
              padding: '10px',
              backgroundColor: '#9e9e9e',
              color: 'white',
              borderRadius: '5px',
              marginBottom: '10px'
            }}>
              🚪 Guest - Please login
            </div>
          )}
        </div>
      </div>
      
      {/* Method 4: Early return */}
      <div style={{ padding: '15px', backgroundColor: '#f5f5f5', borderRadius: '5px' }}>
        <h3>Method 4: Early Return Pattern</h3>
        <p>Return null to render nothing:</p>
        {!isLoggedIn && (
          <div style={{
            padding: '10px',
            backgroundColor: '#fff3cd',
            borderRadius: '5px',
            marginTop: '10px'
          }}>
            ⚠️ This content only shows when logged out
          </div>
        )}
      </div>
      
      <div style={{
        marginTop: '30px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Conditional Rendering Methods:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li><strong>&&:</strong> Show if true, nothing if false</li>
          <li><strong>? ::</strong> Show one thing if true, another if false</li>
          <li><strong>if/return:</strong> Early return pattern</li>
          <li><strong>null:</strong> Render nothing</li>
        </ul>
      </div>
    </div>
  );
}

export default ConditionalRendering;









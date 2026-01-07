/**
 * Example 2: List with Keys
 * 
 * SIMPLE EXPLANATION:
 * Keys are like ID tags for list items. They help React remember which
 * item is which when you add, remove, or reorder items. Without keys,
 * React gets confused and might update the wrong items.
 * 
 * TECHNICAL EXPLANATION:
 * Keys help React's reconciliation algorithm identify which items have
 * changed. They should be unique among siblings and stable across
 * re-renders. Using index as key is acceptable only for static lists.
 */

import { useState } from 'react';

function ListWithKeys() {
  const [users, setUsers] = useState([
    { id: 1, name: 'John Doe', email: 'john@example.com' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com' },
    { id: 3, name: 'Bob Wilson', email: 'bob@example.com' }
  ]);
  
  // Simple: Remove a user from the list
  const removeUser = (id) => {
    setUsers(users.filter(user => user.id !== id));
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
      <h2>List with Keys</h2>
      <p style={{ color: '#666', marginBottom: '20px' }}>
        Each item has a unique key (user.id) - React uses this to track items
      </p>
      
      <div style={{ marginBottom: '20px' }}>
        {users.map((user) => (
          <div
            key={user.id}  // Simple: Using unique ID as key
                          // Technical: Stable, unique identifier for React reconciliation
            style={{
              padding: '15px',
              margin: '10px 0',
              backgroundColor: '#f5f5f5',
              borderRadius: '8px',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              border: '1px solid #ddd'
            }}
          >
            <div>
              <h4 style={{ margin: '0 0 5px 0' }}>{user.name}</h4>
              <p style={{ margin: 0, color: '#666', fontSize: '14px' }}>{user.email}</p>
            </div>
            <button
              onClick={() => removeUser(user.id)}
              style={{
                padding: '8px 15px',
                backgroundColor: '#f44336',
                color: 'white',
                border: 'none',
                borderRadius: '5px',
                cursor: 'pointer'
              }}
            >
              Remove
            </button>
          </div>
        ))}
      </div>
      
      {users.length === 0 && (
        <p style={{ textAlign: 'center', color: '#999', padding: '20px' }}>
          No users remaining
        </p>
      )}
      
      <div style={{
        marginTop: '30px',
        padding: '15px',
        backgroundColor: '#fff3cd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Why Keys Matter:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li>Keys help React identify which items changed</li>
          <li>Without keys, React might update wrong elements</li>
          <li>Use unique IDs when possible (not index for dynamic lists)</li>
          <li>Keys should be stable across re-renders</li>
        </ul>
      </div>
    </div>
  );
}

export default ListWithKeys;


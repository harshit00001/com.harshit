/**
 * Example 2: useContext Hook
 * 
 * SIMPLE EXPLANATION:
 * useContext lets you share data with many components without passing
 * it through every level. Instead of passing props down through many
 * components (prop drilling), you create a "context" that any component
 * can access directly.
 * 
 * TECHNICAL EXPLANATION:
 * useContext provides a way to pass data through the component tree
 * without prop drilling. You create a context with createContext(),
 * provide values with Context.Provider, and consume with useContext().
 */

import { createContext, useContext, useState } from 'react';

// Step 1: Create a Context
// Simple: Create a "box" to store shared data
// Technical: createContext creates a context object
const ThemeContext = createContext();
const UserContext = createContext();

// Custom hook to use theme context
function useTheme() {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useTheme must be used within ThemeProvider');
  }
  return context;
}

// Custom hook to use user context
function useUser() {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within UserProvider');
  }
  return context;
}

// Theme Provider Component
function ThemeProvider({ children }) {
  const [theme, setTheme] = useState('light');
  
  const toggleTheme = () => {
    setTheme(prev => prev === 'light' ? 'dark' : 'light');
  };
  
  const value = { theme, toggleTheme };
  
  return (
    <ThemeContext.Provider value={value}>
      {children}
    </ThemeContext.Provider>
  );
}

// User Provider Component
function UserProvider({ children }) {
  const [user, setUser] = useState(null);
  
  const login = (userData) => {
    setUser(userData);
  };
  
  const logout = () => {
    setUser(null);
  };
  
  const value = { user, login, logout };
  
  return (
    <UserContext.Provider value={value}>
      {children}
    </UserContext.Provider>
  );
}

// Component that uses theme context (deep in component tree)
function ThemedButton() {
  const { theme, toggleTheme } = useTheme();
  
  return (
    <button
      onClick={toggleTheme}
      style={{
        padding: '10px 20px',
        backgroundColor: theme === 'light' ? '#333' : '#fff',
        color: theme === 'light' ? '#fff' : '#333',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer'
      }}
    >
      Toggle Theme (Current: {theme})
    </button>
  );
}

// Component that uses theme context
function ThemedCard() {
  const { theme } = useTheme();
  
  return (
    <div style={{
      padding: '20px',
      backgroundColor: theme === 'light' ? '#fff' : '#333',
      color: theme === 'light' ? '#333' : '#fff',
      borderRadius: '8px',
      border: `2px solid ${theme === 'light' ? '#ddd' : '#555'}`
    }}>
      <h3>Themed Card</h3>
      <p>This card uses the theme from context!</p>
    </div>
  );
}

// Component that uses user context
function UserProfile() {
  const { user, login, logout } = useUser();
  
  if (!user) {
    return (
      <div style={{
        padding: '20px',
        backgroundColor: '#f5f5f5',
        borderRadius: '8px'
      }}>
        <p>Not logged in</p>
        <button
          onClick={() => login({ name: 'John Doe', email: 'john@example.com' })}
          style={{
            padding: '8px 15px',
            backgroundColor: '#4CAF50',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Login
        </button>
      </div>
    );
  }
  
  return (
    <div style={{
      padding: '20px',
      backgroundColor: '#e3f2fd',
      borderRadius: '8px'
    }}>
      <h3>User Profile</h3>
      <p><strong>Name:</strong> {user.name}</p>
      <p><strong>Email:</strong> {user.email}</p>
      <button
        onClick={logout}
        style={{
          padding: '8px 15px',
          backgroundColor: '#f44336',
          color: 'white',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer',
          marginTop: '10px'
        }}
      >
        Logout
      </button>
    </div>
  );
}

// Main App Component
function UseContextExample() {
  return (
    <ThemeProvider>
      <UserProvider>
        <div style={{
          padding: '30px',
          maxWidth: '600px',
          margin: '20px auto',
          backgroundColor: 'white',
          borderRadius: '10px',
          boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
        }}>
          <h2>useContext Examples</h2>
          <p style={{ color: '#666', marginBottom: '30px' }}>
            Context lets you share data without prop drilling
          </p>
          
          <div style={{ marginBottom: '20px' }}>
            <h3>Theme Context:</h3>
            <ThemedButton />
            <div style={{ marginTop: '15px' }}>
              <ThemedCard />
            </div>
          </div>
          
          <div>
            <h3>User Context:</h3>
            <UserProfile />
          </div>
          
          <div style={{
            marginTop: '30px',
            padding: '15px',
            backgroundColor: '#fff3cd',
            borderRadius: '5px',
            fontSize: '14px'
          }}>
            <p><strong>💡 Key Points:</strong></p>
            <ul style={{ lineHeight: '1.8' }}>
              <li>createContext() creates context</li>
              <li>Provider wraps components to share data</li>
              <li>useContext() consumes context</li>
              <li>Avoids prop drilling</li>
              <li>Use for global state (theme, user, etc.)</li>
            </ul>
          </div>
        </div>
      </UserProvider>
    </ThemeProvider>
  );
}

export default UseContextExample;


/**
 * Example 1: Basic API Fetch
 * 
 * SIMPLE EXPLANATION:
 * Fetching data means asking a server for information. You use fetch()
 * to make a request, wait for the response, then use the data in your
 * component. Like ordering food - you order, wait, then eat.
 * 
 * TECHNICAL EXPLANATION:
 * fetch() is a browser API for making HTTP requests. It returns a Promise.
 * Use async/await or .then() to handle the response. Parse JSON with .json().
 * Always handle errors with try/catch or .catch().
 */

import { useState, useEffect } from 'react';

function BasicFetch() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Simple: When component appears, fetch data from API
  // Technical: useEffect runs on mount, async function fetches data
  useEffect(() => {
    // Simple: This function gets data from an API
    // Technical: Async function makes HTTP GET request
    const fetchData = async () => {
      try {
        setLoading(true);
        // Simple: Ask the server for data
        // Technical: fetch() makes HTTP GET request, returns Promise
        const response = await fetch('https://jsonplaceholder.typicode.com/posts/1');
        
        // Simple: Check if request was successful
        // Technical: Check response.ok status
        if (!response.ok) {
          throw new Error('Failed to fetch data');
        }
        
        // Simple: Convert response to JavaScript object
        // Technical: Parse JSON response body
        const jsonData = await response.json();
        
        setData(jsonData);
        setError(null);
      } catch (err) {
        // Simple: If something goes wrong, save the error
        // Technical: Catch and handle errors
        setError(err.message);
        setData(null);
      } finally {
        setLoading(false);
      }
    };
    
    fetchData();
  }, []); // Empty array = run once on mount
  
  if (loading) {
    return (
      <div style={{
        padding: '30px',
        textAlign: 'center',
        maxWidth: '600px',
        margin: '20px auto'
      }}>
        <p>Loading...</p>
      </div>
    );
  }
  
  if (error) {
    return (
      <div style={{
        padding: '30px',
        textAlign: 'center',
        maxWidth: '600px',
        margin: '20px auto',
        backgroundColor: '#ffebee',
        borderRadius: '10px',
        color: '#c62828'
      }}>
        <p><strong>Error:</strong> {error}</p>
      </div>
    );
  }
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '600px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Basic API Fetch Example</h2>
      <p style={{ color: '#666', marginBottom: '20px' }}>
        Fetched from: jsonplaceholder.typicode.com
      </p>
      
      {data && (
        <div style={{
          padding: '20px',
          backgroundColor: '#f5f5f5',
          borderRadius: '8px'
        }}>
          <h3 style={{ marginTop: 0 }}>Post #{data.id}</h3>
          <p style={{ fontWeight: 'bold', marginBottom: '10px' }}>{data.title}</p>
          <p style={{ color: '#666', lineHeight: '1.6' }}>{data.body}</p>
        </div>
      )}
      
      <div style={{
        marginTop: '30px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Key Points:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li>Use fetch() in useEffect</li>
          <li>Handle loading state</li>
          <li>Handle error state</li>
          <li>Parse JSON with .json()</li>
          <li>Use async/await or .then()</li>
        </ul>
      </div>
    </div>
  );
}

export default BasicFetch;










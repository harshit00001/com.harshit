/**
 * Example 1: Basic List Rendering
 * 
 * SIMPLE EXPLANATION:
 * To show a list of items, you use map(). It goes through each item in
 * an array and turns it into JSX. Like making a list of cards from a
 * stack of data.
 * 
 * TECHNICAL EXPLANATION:
 * Array.map() transforms each element of an array into JSX. It returns
 * a new array of React elements. Each element should have a unique key
 * prop for React's reconciliation.
 */

function BasicList() {
  // Simple: An array of names we want to display
  // Technical: Data array that will be transformed to JSX
  const fruits = ['Apple', 'Banana', 'Orange', 'Grape', 'Mango'];
  
  const numbers = [1, 2, 3, 4, 5];
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '600px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Basic List Rendering</h2>
      
      {/* Simple: map() goes through each fruit and makes a list item */}
      {/* Technical: map() transforms array to array of JSX elements */}
      <div style={{ marginBottom: '30px' }}>
        <h3>Fruits List:</h3>
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {fruits.map((fruit, index) => (
            <li
              key={index}  // Simple: Each item needs a unique key
              style={{
                padding: '10px',
                margin: '5px 0',
                backgroundColor: '#e3f2fd',
                borderRadius: '5px'
              }}
            >
              {fruit}
            </li>
          ))}
        </ul>
      </div>
      
      {/* Numbers list */}
      <div style={{ marginBottom: '30px' }}>
        <h3>Numbers List:</h3>
        <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
          {numbers.map((num) => (
            <div
              key={num}
              style={{
                padding: '15px',
                backgroundColor: '#4CAF50',
                color: 'white',
                borderRadius: '50%',
                width: '50px',
                height: '50px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontSize: '20px',
                fontWeight: 'bold'
              }}
            >
              {num}
            </div>
          ))}
        </div>
      </div>
      
      {/* Cards from array */}
      <div>
        <h3>Product Cards:</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))', gap: '15px' }}>
          {fruits.map((fruit, index) => (
            <div
              key={index}
              style={{
                padding: '20px',
                backgroundColor: '#f5f5f5',
                borderRadius: '8px',
                textAlign: 'center',
                border: '2px solid #2196F3'
              }}
            >
              <h4>{fruit}</h4>
              <p style={{ color: '#666', fontSize: '14px' }}>Item #{index + 1}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default BasicList;










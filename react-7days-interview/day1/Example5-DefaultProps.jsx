/**
 * Example 5: Props with Default Values
 * 
 * SIMPLE EXPLANATION:
 * Sometimes you want to give props a default value if they're not provided.
 * It's like having a backup plan - if someone doesn't give you information,
 * you use the default instead.
 * 
 * TECHNICAL EXPLANATION:
 * Default props can be set using default parameters in function components
 * (ES6 feature) or using the defaultProps property in class components.
 * This prevents errors when props are undefined and makes components
 * more flexible.
 * 
 * INTERVIEW POINT:
 * - Use default parameters for default props in functional components
 * - Makes components more flexible and prevents undefined errors
 * - Can combine with destructuring for clean code
 */

function UserProfile({ 
  name = "Guest",           // Default: "Guest"
  role = "User",            // Default: "User"
  isActive = false,         // Default: false
  email = "No email provided",
  avatar = "👤"            // Default emoji
}) {
  return (
    <div style={{
      border: '2px solid #2196F3',
      borderRadius: '10px',
      padding: '20px',
      margin: '10px',
      backgroundColor: isActive ? '#e3f2fd' : '#f5f5f5',
      maxWidth: '300px'
    }}>
      <div style={{ fontSize: '48px', textAlign: 'center', marginBottom: '10px' }}>
        {avatar}
      </div>
      <h3 style={{ margin: '10px 0', color: '#333' }}>{name}</h3>
      <div style={{ 
        display: 'inline-block',
        padding: '5px 10px',
        backgroundColor: role === 'Admin' ? '#f44336' : '#4CAF50',
        color: 'white',
        borderRadius: '5px',
        fontSize: '12px',
        marginBottom: '10px'
      }}>
        {role}
      </div>
      <p style={{ margin: '5px 0', color: '#666' }}>
        <strong>Email:</strong> {email}
      </p>
      <p style={{ margin: '5px 0', color: '#666' }}>
        <strong>Status:</strong> 
        <span style={{ 
          color: isActive ? '#4CAF50' : '#999',
          marginLeft: '5px'
        }}>
          {isActive ? '✅ Active' : '❌ Inactive'}
        </span>
      </p>
    </div>
  );
}

// Usage Examples:
function App() {
  return (
    <div style={{ padding: '20px' }}>
      <h1>Default Props Examples</h1>
      
      <div style={{ display: 'flex', flexWrap: 'wrap' }}>
        {/* All props provided */}
        <UserProfile 
          name="John Doe"
          role="Admin"
          isActive={true}
          email="john@example.com"
          avatar="👨‍💼"
        />
        
        {/* Some props provided (others use defaults) */}
        <UserProfile 
          name="Jane Smith"
          role="Manager"
          isActive={true}
        />
        
        {/* Only name provided */}
        <UserProfile 
          name="Bob Wilson"
        />
        
        {/* No props provided (all use defaults) */}
        <UserProfile />
      </div>
      
      <div style={{ marginTop: '30px', padding: '15px', backgroundColor: '#fff3cd', borderRadius: '5px' }}>
        <h3>💡 Key Points:</h3>
        <ul style={{ lineHeight: '1.8' }}>
          <li>Default values are used when props are not provided</li>
          <li>You can provide some props and let others use defaults</li>
          <li>Makes components more flexible and prevents errors</li>
          <li>Default parameters are ES6 feature (modern approach)</li>
        </ul>
      </div>
    </div>
  );
}

export default App;


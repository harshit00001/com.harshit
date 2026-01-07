/**
 * Example 3: JSX Expressions
 * 
 * SIMPLE EXPLANATION:
 * In JSX, you can use JavaScript inside curly braces {}. You can do math,
 * call functions, use variables, and more. It's like mixing JavaScript
 * with HTML.
 * 
 * TECHNICAL EXPLANATION:
 * JSX allows embedding JavaScript expressions using curly braces {}.
 * You can use variables, function calls, ternary operators, and any
 * valid JavaScript expression. However, you cannot use statements
 * (like if/else, for loops) directly - you need to use expressions.
 * 
 * INTERVIEW POINT:
 * - Use {} for JavaScript expressions in JSX
 * - Can use variables, functions, calculations
 * - Cannot use statements directly (use ternary or map instead)
 */

function Calculator() {
  // Variables
  const a = 10;
  const b = 5;
  const name = "React";
  const isActive = true;
  
  // Function
  const multiply = (x, y) => x * y;
  
  // Array
  const numbers = [1, 2, 3, 4, 5];
  
  return (
    <div style={{ 
      padding: '20px', 
      backgroundColor: '#f5f5f5',
      borderRadius: '8px',
      margin: '10px'
    }}>
      <h2>JSX Expressions Examples</h2>
      
      {/* Basic Math */}
      <div style={{ margin: '10px 0' }}>
        <p><strong>Math Operations:</strong></p>
        <p>Addition: {a} + {b} = {a + b}</p>
        <p>Subtraction: {a} - {b} = {a - b}</p>
        <p>Multiplication: {a} × {b} = {multiply(a, b)}</p>
        <p>Division: {a} ÷ {b} = {a / b}</p>
      </div>
      
      {/* String Operations */}
      <div style={{ margin: '10px 0' }}>
        <p><strong>String Operations:</strong></p>
        <p>Original: {name}</p>
        <p>Uppercase: {name.toUpperCase()}</p>
        <p>Lowercase: {name.toLowerCase()}</p>
        <p>Length: {name.length} characters</p>
      </div>
      
      {/* Date/Time */}
      <div style={{ margin: '10px 0' }}>
        <p><strong>Date & Time:</strong></p>
        <p>Current Date: {new Date().toLocaleDateString()}</p>
        <p>Current Time: {new Date().toLocaleTimeString()}</p>
      </div>
      
      {/* Conditional (Ternary Operator) */}
      <div style={{ margin: '10px 0' }}>
        <p><strong>Conditional Rendering:</strong></p>
        <p>Status: {isActive ? '✅ Active' : '❌ Inactive'}</p>
        <p>User Type: {a > 5 ? 'Premium' : 'Basic'}</p>
      </div>
      
      {/* Array Operations */}
      <div style={{ margin: '10px 0' }}>
        <p><strong>Array Operations:</strong></p>
        <p>Numbers: {numbers.join(', ')}</p>
        <p>Sum: {numbers.reduce((sum, num) => sum + num, 0)}</p>
        <p>Count: {numbers.length}</p>
      </div>
    </div>
  );
}

export default Calculator;


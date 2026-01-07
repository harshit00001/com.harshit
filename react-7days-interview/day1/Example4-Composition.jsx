/**
 * Example 4: Component Composition
 * 
 * SIMPLE EXPLANATION:
 * Component composition means building bigger components by combining
 * smaller components. It's like building with LEGO blocks - you use
 * small pieces to make something bigger.
 * 
 * TECHNICAL EXPLANATION:
 * Component composition is a pattern where you build complex UIs by
 * combining simpler, reusable components. This follows the principle
 * of separation of concerns and makes code more maintainable and
 * testable.
 * 
 * INTERVIEW POINT:
 * - Composition over inheritance
 * - Build complex UIs from simple components
 * - Each component has a single responsibility
 */

// Small, reusable Button component
function Button({ text, onClick, variant = 'primary' }) {
  const styles = {
    primary: { backgroundColor: '#4CAF50', color: 'white' },
    secondary: { backgroundColor: '#2196F3', color: 'white' },
    danger: { backgroundColor: '#f44336', color: 'white' }
  };
  
  return (
    <button 
      onClick={onClick}
      style={{
        ...styles[variant],
        padding: '10px 20px',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        margin: '5px',
        fontSize: '16px'
      }}
    >
      {text}
    </button>
  );
}

// Small, reusable Card component
function Card({ title, content, footer }) {
  return (
    <div style={{
      border: '1px solid #ddd',
      borderRadius: '8px',
      padding: '20px',
      margin: '10px',
      boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
      backgroundColor: 'white'
    }}>
      <h3 style={{ marginTop: 0, color: '#333' }}>{title}</h3>
      <p style={{ color: '#666', lineHeight: '1.6' }}>{content}</p>
      {footer && <div style={{ marginTop: '15px', borderTop: '1px solid #eee', paddingTop: '15px' }}>
        {footer}
      </div>}
    </div>
  );
}

// Composed component using smaller components
function ProductCard({ product, onAddToCart, onViewDetails }) {
  return (
    <Card
      title={product.name}
      content={`${product.description} - $${product.price}`}
      footer={
        <div>
          <Button 
            text="Add to Cart" 
            onClick={onAddToCart}
            variant="primary"
          />
          <Button 
            text="View Details" 
            onClick={onViewDetails}
            variant="secondary"
          />
        </div>
      }
    />
  );
}

// Main App component composing everything
function App() {
  const product = {
    name: "React Course",
    description: "Learn React from scratch",
    price: 99.99
  };
  
  const handleAddToCart = () => {
    alert('Added to cart!');
  };
  
  const handleViewDetails = () => {
    alert('Viewing product details...');
  };
  
  return (
    <div style={{ padding: '20px', backgroundColor: '#f5f5f5', minHeight: '100vh' }}>
      <h1>Component Composition Example</h1>
      <p>This example shows how to build complex components from simple ones.</p>
      
      <ProductCard
        product={product}
        onAddToCart={handleAddToCart}
        onViewDetails={handleViewDetails}
      />
      
      <div style={{ marginTop: '20px' }}>
        <h3>Individual Components:</h3>
        <Button text="Primary Button" onClick={() => alert('Primary!')} variant="primary" />
        <Button text="Secondary Button" onClick={() => alert('Secondary!')} variant="secondary" />
        <Button text="Danger Button" onClick={() => alert('Danger!')} variant="danger" />
      </div>
    </div>
  );
}

export default App;


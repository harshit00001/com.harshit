/**
 * Example 4: Filtering Lists
 * 
 * SIMPLE EXPLANATION:
 * Filtering lets you show only certain items from a list. Like showing
 * only completed tasks, or only products under a certain price. You use
 * the filter() function to create a new list with only the items that
 * match your condition.
 * 
 * TECHNICAL EXPLANATION:
 * Array.filter() creates a new array with elements that pass a test.
 * Combine filter() with map() to filter and render. Use state to manage
 * filter criteria.
 */

import { useState } from 'react';

function FilteringLists() {
  const [products] = useState([
    { id: 1, name: 'Laptop', price: 999, category: 'Electronics' },
    { id: 2, name: 'Phone', price: 699, category: 'Electronics' },
    { id: 3, name: 'Book', price: 19, category: 'Books' },
    { id: 4, name: 'Headphones', price: 149, category: 'Electronics' },
    { id: 5, name: 'Notebook', price: 5, category: 'Stationery' },
    { id: 6, name: 'Tablet', price: 399, category: 'Electronics' },
    { id: 7, name: 'Pen', price: 2, category: 'Stationery' }
  ]);
  
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [maxPrice, setMaxPrice] = useState(1000);
  
  // Simple: Filter products based on search, category, and price
  // Technical: Chain filter() methods to apply multiple filters
  const filteredProducts = products.filter(product => {
    const matchesSearch = product.name.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = selectedCategory === 'all' || product.category === selectedCategory;
    const matchesPrice = product.price <= maxPrice;
    
    return matchesSearch && matchesCategory && matchesPrice;
  });
  
  // Get unique categories
  const categories = ['all', ...new Set(products.map(p => p.category))];
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '800px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Filtering Lists Example</h2>
      
      {/* Search Input */}
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Search Products:
        </label>
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="Search by name..."
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px',
            fontSize: '16px'
          }}
        />
      </div>
      
      {/* Category Filter */}
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Category:
        </label>
        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px',
            fontSize: '16px'
          }}
        >
          {categories.map(cat => (
            <option key={cat} value={cat}>
              {cat.charAt(0).toUpperCase() + cat.slice(1)}
            </option>
          ))}
        </select>
      </div>
      
      {/* Price Filter */}
      <div style={{ marginBottom: '20px' }}>
        <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
          Max Price: ${maxPrice}
        </label>
        <input
          type="range"
          min="0"
          max="1000"
          value={maxPrice}
          onChange={(e) => setMaxPrice(Number(e.target.value))}
          style={{ width: '100%' }}
        />
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '12px', color: '#666' }}>
          <span>$0</span>
          <span>$1000</span>
        </div>
      </div>
      
      {/* Results Count */}
      <div style={{
        padding: '10px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        marginBottom: '20px',
        textAlign: 'center'
      }}>
        <strong>{filteredProducts.length}</strong> product(s) found
      </div>
      
      {/* Filtered Products List */}
      <div style={{ display: 'grid', gap: '15px' }}>
        {filteredProducts.length === 0 ? (
          <div style={{
            padding: '30px',
            textAlign: 'center',
            backgroundColor: '#f5f5f5',
            borderRadius: '5px',
            color: '#999'
          }}>
            No products match your filters
          </div>
        ) : (
          filteredProducts.map(product => (
            <div
              key={product.id}
              style={{
                padding: '20px',
                backgroundColor: '#f9f9f9',
                borderRadius: '8px',
                border: '1px solid #ddd',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center'
              }}
            >
              <div>
                <h3 style={{ margin: '0 0 5px 0' }}>{product.name}</h3>
                <p style={{ margin: 0, color: '#666', fontSize: '14px' }}>
                  {product.category} • ${product.price}
                </p>
              </div>
              <div style={{
                padding: '5px 15px',
                backgroundColor: '#2196F3',
                color: 'white',
                borderRadius: '20px',
                fontSize: '14px',
                fontWeight: 'bold'
              }}>
                ${product.price}
              </div>
            </div>
          ))
        )}
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
          <li>Use filter() to create filtered array</li>
          <li>Combine multiple filters with && operator</li>
          <li>Store filter criteria in state</li>
          <li>Map over filtered array to render</li>
        </ul>
      </div>
    </div>
  );
}

export default FilteringLists;


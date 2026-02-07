/**
 * Example 5: Form Validation
 * 
 * SIMPLE EXPLANATION:
 * Validation checks if the information users enter is correct before they
 * submit the form. Like checking if an email looks like an email, or if
 * a password is long enough.
 * 
 * TECHNICAL EXPLANATION:
 * Form validation can be done:
 * 1. Client-side: Real-time validation as user types
 * 2. On submit: Validate all fields before submission
 * 3. HTML5 validation: Using required, pattern, type attributes
 * 4. Custom validation: Using JavaScript functions
 */

import { useState } from 'react';

function FormWithValidation() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    age: ''
  });
  
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});
  
  // Validation functions
  const validateEmail = (email) => {
    if (!email) return 'Email is required';
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) return 'Invalid email format';
    return '';
  };
  
  const validatePassword = (password) => {
    if (!password) return 'Password is required';
    if (password.length < 8) return 'Password must be at least 8 characters';
    if (!/(?=.*[A-Z])/.test(password)) return 'Password must contain uppercase letter';
    if (!/(?=.*[0-9])/.test(password)) return 'Password must contain a number';
    return '';
  };
  
  const validateAge = (age) => {
    if (!age) return 'Age is required';
    const ageNum = Number(age);
    if (isNaN(ageNum)) return 'Age must be a number';
    if (ageNum < 18) return 'Must be 18 or older';
    if (ageNum > 120) return 'Please enter a valid age';
    return '';
  };
  
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    
    // Validate on change
    let error = '';
    if (name === 'email') error = validateEmail(value);
    else if (name === 'password') error = validatePassword(value);
    else if (name === 'confirmPassword') {
      error = value !== formData.password ? 'Passwords do not match' : '';
    }
    else if (name === 'age') error = validateAge(value);
    
    setErrors(prev => ({ ...prev, [name]: error }));
  };
  
  const handleBlur = (e) => {
    const { name } = e.target;
    setTouched(prev => ({ ...prev, [name]: true }));
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Validate all fields
    const newErrors = {
      email: validateEmail(formData.email),
      password: validatePassword(formData.password),
      confirmPassword: formData.confirmPassword !== formData.password ? 'Passwords do not match' : '',
      age: validateAge(formData.age)
    };
    
    setErrors(newErrors);
    setTouched({
      email: true,
      password: true,
      confirmPassword: true,
      age: true
    });
    
    // Check if form is valid
    const isValid = Object.values(newErrors).every(error => error === '');
    
    if (isValid) {
      alert('Form is valid! Submission would proceed.');
      console.log('Valid form data:', formData);
    } else {
      alert('Please fix the errors before submitting.');
    }
  };
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '500px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Form with Validation</h2>
      
      <form onSubmit={handleSubmit}>
        {/* Email */}
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
            Email: *
          </label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            onBlur={handleBlur}
            placeholder="your.email@example.com"
            style={{
              width: '100%',
              padding: '10px',
              border: errors.email && touched.email ? '2px solid #f44336' : '1px solid #ddd',
              borderRadius: '5px',
              fontSize: '16px'
            }}
          />
          {errors.email && touched.email && (
            <p style={{ color: '#f44336', fontSize: '14px', marginTop: '5px' }}>
              {errors.email}
            </p>
          )}
        </div>
        
        {/* Password */}
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
            Password: *
          </label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            onBlur={handleBlur}
            placeholder="At least 8 characters"
            style={{
              width: '100%',
              padding: '10px',
              border: errors.password && touched.password ? '2px solid #f44336' : '1px solid #ddd',
              borderRadius: '5px',
              fontSize: '16px'
            }}
          />
          {errors.password && touched.password && (
            <p style={{ color: '#f44336', fontSize: '14px', marginTop: '5px' }}>
              {errors.password}
            </p>
          )}
        </div>
        
        {/* Confirm Password */}
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
            Confirm Password: *
          </label>
          <input
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            onBlur={handleBlur}
            placeholder="Re-enter password"
            style={{
              width: '100%',
              padding: '10px',
              border: errors.confirmPassword && touched.confirmPassword ? '2px solid #f44336' : '1px solid #ddd',
              borderRadius: '5px',
              fontSize: '16px'
            }}
          />
          {errors.confirmPassword && touched.confirmPassword && (
            <p style={{ color: '#f44336', fontSize: '14px', marginTop: '5px' }}>
              {errors.confirmPassword}
            </p>
          )}
        </div>
        
        {/* Age */}
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
            Age: *
          </label>
          <input
            type="number"
            name="age"
            value={formData.age}
            onChange={handleChange}
            onBlur={handleBlur}
            placeholder="Must be 18+"
            style={{
              width: '100%',
              padding: '10px',
              border: errors.age && touched.age ? '2px solid #f44336' : '1px solid #ddd',
              borderRadius: '5px',
              fontSize: '16px'
            }}
          />
          {errors.age && touched.age && (
            <p style={{ color: '#f44336', fontSize: '14px', marginTop: '5px' }}>
              {errors.age}
            </p>
          )}
        </div>
        
        <button
          type="submit"
          style={{
            width: '100%',
            padding: '12px',
            backgroundColor: '#2196F3',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            fontSize: '16px',
            fontWeight: 'bold',
            cursor: 'pointer'
          }}
        >
          Submit
        </button>
      </form>
      
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Validation Features:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li>Real-time validation as you type</li>
          <li>Shows errors only after field is touched</li>
          <li>Validates on blur (when you leave the field)</li>
          <li>Validates all fields on submit</li>
        </ul>
      </div>
    </div>
  );
}

export default FormWithValidation;









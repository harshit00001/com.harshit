/**
 * Example 3: Form Handling
 * 
 * SIMPLE EXPLANATION:
 * Forms let users enter information. In React, you handle form submission,
 * prevent the page from refreshing, get all the form data, and do something
 * with it (like send it to a server or validate it).
 * 
 * TECHNICAL EXPLANATION:
 * Form handling involves:
 * 1. Controlled inputs (value + onChange)
 * 2. onSubmit handler on form element
 * 3. e.preventDefault() to prevent default form submission
 * 4. Access form data from state
 * 5. Validate and process the data
 * 
 * INTERVIEW POINT:
 * - Always use e.preventDefault() in form submission
 * - Use controlled components for form inputs
 * - Validate data before processing
 */

import { useState } from 'react';

function ContactForm() {
  // Simple: We store all form data in state
  // Technical: State object holds all form field values
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    message: '',
    subject: ''
  });
  
  const [submitted, setSubmitted] = useState(false);
  
  // Simple: When user types, update the specific field in our state object
  // Technical: Handle input change, update nested state property
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };
  
  // Simple: When form is submitted, prevent page refresh and handle the data
  // Technical: onSubmit handler, preventDefault, process form data
  const handleSubmit = (e) => {
    e.preventDefault(); // Simple: Stop page from refreshing
                      // Technical: Prevent default form submission behavior
    
    console.log('Form submitted:', formData);
    
    // Simple: Do something with the form data (like send to server)
    // Technical: Process form data, validate, make API call, etc.
    alert(`Thank you, ${formData.name}! Your message has been submitted.`);
    
    // Reset form
    setFormData({
      name: '',
      email: '',
      message: '',
      subject: ''
    });
    
    setSubmitted(true);
    setTimeout(() => setSubmitted(false), 3000);
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
      <h2>Contact Form</h2>
      
      {submitted && (
        <div style={{
          padding: '15px',
          backgroundColor: '#4CAF50',
          color: 'white',
          borderRadius: '5px',
          marginBottom: '20px',
          textAlign: 'center'
        }}>
          ✅ Form submitted successfully!
        </div>
      )}
      
      <form onSubmit={handleSubmit}>
        {/* Name Input */}
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
            Name: *
          </label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
            placeholder="Your name"
            style={{
              width: '100%',
              padding: '10px',
              border: '1px solid #ddd',
              borderRadius: '5px',
              fontSize: '16px'
            }}
          />
        </div>
        
        {/* Email Input */}
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
            Email: *
          </label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            placeholder="your.email@example.com"
            style={{
              width: '100%',
              padding: '10px',
              border: '1px solid #ddd',
              borderRadius: '5px',
              fontSize: '16px'
            }}
          />
        </div>
        
        {/* Subject Select */}
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
            Subject:
          </label>
          <select
            name="subject"
            value={formData.subject}
            onChange={handleChange}
            style={{
              width: '100%',
              padding: '10px',
              border: '1px solid #ddd',
              borderRadius: '5px',
              fontSize: '16px'
            }}
          >
            <option value="">Select a subject...</option>
            <option value="question">Question</option>
            <option value="feedback">Feedback</option>
            <option value="support">Support</option>
            <option value="other">Other</option>
          </select>
        </div>
        
        {/* Message Textarea */}
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>
            Message: *
          </label>
          <textarea
            name="message"
            value={formData.message}
            onChange={handleChange}
            required
            placeholder="Your message..."
            rows={5}
            style={{
              width: '100%',
              padding: '10px',
              border: '1px solid #ddd',
              borderRadius: '5px',
              fontSize: '16px',
              fontFamily: 'inherit'
            }}
          />
        </div>
        
        {/* Submit Button */}
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
          Submit Form
        </button>
      </form>
      
      {/* Display form data (for debugging) */}
      <div style={{
        marginTop: '30px',
        padding: '15px',
        backgroundColor: '#f5f5f5',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <h3>Form Data (Debug):</h3>
        <pre style={{
          backgroundColor: '#fff',
          padding: '10px',
          borderRadius: '5px',
          overflow: 'auto'
        }}>
          {JSON.stringify(formData, null, 2)}
        </pre>
      </div>
      
      <div style={{
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#fff3cd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Key Points:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li>Always use <code>e.preventDefault()</code> in onSubmit</li>
          <li>Use <code>name</code> attribute matching state keys</li>
          <li>Controlled inputs with value + onChange</li>
          <li>Validate before submission</li>
        </ul>
      </div>
    </div>
  );
}

export default ContactForm;


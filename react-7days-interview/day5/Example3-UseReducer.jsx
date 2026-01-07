/**
 * Example 3: useReducer Hook
 * 
 * SIMPLE EXPLANATION:
 * useReducer is like useState but for more complex state. Instead of
 * setting values directly, you send "actions" (like messages) that
 * describe what happened, and a "reducer" function decides how to
 * update the state based on that action.
 * 
 * TECHNICAL EXPLANATION:
 * useReducer is an alternative to useState for complex state logic.
 * It uses a reducer function (state, action) => newState, similar to
 * Redux pattern. Better for state with multiple sub-values or when
 * next state depends on previous state.
 */

import { useReducer, useState } from 'react';

// Reducer function
// Simple: This function decides how to update state based on actions
// Technical: Pure function that takes current state and action, returns new state
function todoReducer(state, action) {
  switch (action.type) {
    case 'ADD_TODO':
      return {
        ...state,
        todos: [...state.todos, {
          id: Date.now(),
          text: action.payload,
          completed: false
        }]
      };
    
    case 'TOGGLE_TODO':
      return {
        ...state,
        todos: state.todos.map(todo =>
          todo.id === action.payload
            ? { ...todo, completed: !todo.completed }
            : todo
        )
      };
    
    case 'DELETE_TODO':
      return {
        ...state,
        todos: state.todos.filter(todo => todo.id !== action.payload)
      };
    
    case 'SET_FILTER':
      return {
        ...state,
        filter: action.payload
      };
    
    default:
      return state;
  }
}

// Initial state
const initialState = {
  todos: [],
  filter: 'all' // all, active, completed
};

function TodoApp() {
  // Simple: useReducer gives you state and a dispatch function
  // Technical: useReducer(reducer, initialState) returns [state, dispatch]
  const [state, dispatch] = useReducer(todoReducer, initialState);
  
  const [inputValue, setInputValue] = useState('');
  
  // Simple: Dispatch sends an action to update state
  // Technical: dispatch(action) calls reducer with current state and action
  const addTodo = () => {
    if (inputValue.trim()) {
      dispatch({ type: 'ADD_TODO', payload: inputValue });
      setInputValue('');
    }
  };
  
  const toggleTodo = (id) => {
    dispatch({ type: 'TOGGLE_TODO', payload: id });
  };
  
  const deleteTodo = (id) => {
    dispatch({ type: 'DELETE_TODO', payload: id });
  };
  
  // Filter todos
  const filteredTodos = state.todos.filter(todo => {
    if (state.filter === 'active') return !todo.completed;
    if (state.filter === 'completed') return todo.completed;
    return true;
  });
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '600px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Todo App with useReducer</h2>
      
      {/* Add Todo */}
      <div style={{ marginBottom: '20px', display: 'flex', gap: '10px' }}>
        <input
          type="text"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && addTodo()}
          placeholder="Add a todo..."
          style={{
            flex: 1,
            padding: '10px',
            border: '1px solid #ddd',
            borderRadius: '5px',
            fontSize: '16px'
          }}
        />
        <button
          onClick={addTodo}
          style={{
            padding: '10px 20px',
            backgroundColor: '#4CAF50',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Add
        </button>
      </div>
      
      {/* Filter Buttons */}
      <div style={{ marginBottom: '20px', display: 'flex', gap: '10px' }}>
        {['all', 'active', 'completed'].map(filter => (
          <button
            key={filter}
            onClick={() => dispatch({ type: 'SET_FILTER', payload: filter })}
            style={{
              padding: '8px 15px',
              backgroundColor: state.filter === filter ? '#2196F3' : '#f5f5f5',
              color: state.filter === filter ? 'white' : '#333',
              border: 'none',
              borderRadius: '5px',
              cursor: 'pointer'
            }}
          >
            {filter.charAt(0).toUpperCase() + filter.slice(1)}
          </button>
        ))}
      </div>
      
      {/* Todos List */}
      <div style={{ marginBottom: '20px' }}>
        {filteredTodos.length === 0 ? (
          <p style={{ textAlign: 'center', color: '#999', padding: '20px' }}>
            No todos {state.filter !== 'all' && `(${state.filter})`}
          </p>
        ) : (
          filteredTodos.map(todo => (
            <div
              key={todo.id}
              style={{
                padding: '15px',
                marginBottom: '10px',
                backgroundColor: todo.completed ? '#e8f5e9' : '#fff3e0',
                borderRadius: '8px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                border: `2px solid ${todo.completed ? '#4CAF50' : '#ff9800'}`
              }}
            >
              <div style={{ flex: 1 }}>
                <input
                  type="checkbox"
                  checked={todo.completed}
                  onChange={() => toggleTodo(todo.id)}
                  style={{ marginRight: '10px', width: '20px', height: '20px' }}
                />
                <span style={{
                  textDecoration: todo.completed ? 'line-through' : 'none',
                  color: todo.completed ? '#999' : '#333'
                }}>
                  {todo.text}
                </span>
              </div>
              <button
                onClick={() => deleteTodo(todo.id)}
                style={{
                  padding: '5px 10px',
                  backgroundColor: '#f44336',
                  color: 'white',
                  border: 'none',
                  borderRadius: '5px',
                  cursor: 'pointer'
                }}
              >
                Delete
              </button>
            </div>
          ))
        )}
      </div>
      
      {/* Stats */}
      <div style={{
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        textAlign: 'center'
      }}>
        <p>
          Total: {state.todos.length} | 
          Active: {state.todos.filter(t => !t.completed).length} | 
          Completed: {state.todos.filter(t => t.completed).length}
        </p>
      </div>
      
      <div style={{
        marginTop: '30px',
        padding: '15px',
        backgroundColor: '#fff3cd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 useReducer vs useState:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li>useReducer: Complex state, multiple actions</li>
          <li>useState: Simple state, direct updates</li>
          <li>useReducer: Predictable state updates</li>
          <li>useReducer: Better for state machines</li>
        </ul>
      </div>
    </div>
  );
}

export default TodoApp;


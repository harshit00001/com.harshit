/**
 * Example 5: Complex List Operations
 * 
 * SIMPLE EXPLANATION:
 * Sometimes you need to do multiple things with a list - like filter it,
 * sort it, and then display it. You can chain these operations together
 * or do them step by step.
 * 
 * TECHNICAL EXPLANATION:
 * Combine array methods: filter(), map(), sort(), reduce(). Chain them
 * for functional programming style. Each method returns a new array.
 */

import { useState } from 'react';

function ComplexLists() {
  const [tasks] = useState([
    { id: 1, title: 'Learn React', completed: true, priority: 'high', time: 120 },
    { id: 2, title: 'Build Project', completed: false, priority: 'high', time: 180 },
    { id: 3, title: 'Write Tests', completed: false, priority: 'medium', time: 60 },
    { id: 4, title: 'Code Review', completed: true, priority: 'low', time: 30 },
    { id: 5, title: 'Deploy App', completed: false, priority: 'high', time: 45 },
    { id: 6, title: 'Update Docs', completed: true, priority: 'medium', time: 90 }
  ]);
  
  const [showCompleted, setShowCompleted] = useState(true);
  const [sortBy, setSortBy] = useState('priority');
  
  // Simple: Filter, then sort, then display
  // Technical: Chain array methods for functional programming
  let processedTasks = tasks;
  
  // Filter completed tasks
  if (!showCompleted) {
    processedTasks = processedTasks.filter(task => !task.completed);
  }
  
  // Sort tasks
  processedTasks = [...processedTasks].sort((a, b) => {
    if (sortBy === 'priority') {
      const priorityOrder = { high: 3, medium: 2, low: 1 };
      return priorityOrder[b.priority] - priorityOrder[a.priority];
    } else if (sortBy === 'time') {
      return b.time - a.time;
    } else if (sortBy === 'title') {
      return a.title.localeCompare(b.title);
    }
    return 0;
  });
  
  // Calculate statistics using reduce
  const stats = tasks.reduce((acc, task) => {
    acc.total += task.time;
    if (task.completed) acc.completed += task.time;
    acc.totalTasks++;
    if (task.completed) acc.completedTasks++;
    return acc;
  }, { total: 0, completed: 0, totalTasks: 0, completedTasks: 0 });
  
  return (
    <div style={{
      padding: '30px',
      maxWidth: '800px',
      margin: '20px auto',
      backgroundColor: 'white',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    }}>
      <h2>Complex List Operations</h2>
      
      {/* Statistics */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(2, 1fr)',
        gap: '15px',
        marginBottom: '30px'
      }}>
        <div style={{
          padding: '15px',
          backgroundColor: '#e3f2fd',
          borderRadius: '5px',
          textAlign: 'center'
        }}>
          <h3 style={{ margin: '0 0 5px 0', fontSize: '24px' }}>{stats.completedTasks}/{stats.totalTasks}</h3>
          <p style={{ margin: 0, color: '#666' }}>Tasks Completed</p>
        </div>
        <div style={{
          padding: '15px',
          backgroundColor: '#f3e5f5',
          borderRadius: '5px',
          textAlign: 'center'
        }}>
          <h3 style={{ margin: '0 0 5px 0', fontSize: '24px' }}>{stats.completed}/{stats.total} min</h3>
          <p style={{ margin: 0, color: '#666' }}>Time Completed</p>
        </div>
      </div>
      
      {/* Controls */}
      <div style={{
        display: 'flex',
        gap: '15px',
        marginBottom: '20px',
        flexWrap: 'wrap'
      }}>
        <label style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <input
            type="checkbox"
            checked={showCompleted}
            onChange={(e) => setShowCompleted(e.target.checked)}
            style={{ width: '20px', height: '20px' }}
          />
          <span>Show Completed</span>
        </label>
        
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <label style={{ fontWeight: 'bold' }}>Sort by:</label>
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            style={{
              padding: '8px',
              border: '1px solid #ddd',
              borderRadius: '5px'
            }}
          >
            <option value="priority">Priority</option>
            <option value="time">Time</option>
            <option value="title">Title</option>
          </select>
        </div>
      </div>
      
      {/* Tasks List */}
      <div style={{ display: 'grid', gap: '10px' }}>
        {processedTasks.map(task => (
          <div
            key={task.id}
            style={{
              padding: '15px',
              backgroundColor: task.completed ? '#e8f5e9' : '#fff3e0',
              borderRadius: '8px',
              border: `2px solid ${task.completed ? '#4CAF50' : '#ff9800'}`,
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center'
            }}
          >
            <div style={{ flex: 1 }}>
              <h3 style={{
                margin: '0 0 5px 0',
                textDecoration: task.completed ? 'line-through' : 'none',
                color: task.completed ? '#999' : '#333'
              }}>
                {task.title}
              </h3>
              <div style={{ display: 'flex', gap: '15px', fontSize: '14px', color: '#666' }}>
                <span>Priority: <strong>{task.priority}</strong></span>
                <span>Time: <strong>{task.time} min</strong></span>
              </div>
            </div>
            <div style={{
              padding: '5px 15px',
              backgroundColor: task.completed ? '#4CAF50' : '#ff9800',
              color: 'white',
              borderRadius: '20px',
              fontSize: '12px',
              fontWeight: 'bold'
            }}>
              {task.completed ? '✓ Done' : 'Pending'}
            </div>
          </div>
        ))}
      </div>
      
      {processedTasks.length === 0 && (
        <div style={{
          padding: '30px',
          textAlign: 'center',
          backgroundColor: '#f5f5f5',
          borderRadius: '5px',
          color: '#999'
        }}>
          No tasks to display
        </div>
      )}
      
      <div style={{
        marginTop: '30px',
        padding: '15px',
        backgroundColor: '#e3f2fd',
        borderRadius: '5px',
        fontSize: '14px'
      }}>
        <p><strong>💡 Array Methods Used:</strong></p>
        <ul style={{ lineHeight: '1.8' }}>
          <li><strong>filter():</strong> Show/hide completed tasks</li>
          <li><strong>sort():</strong> Sort by priority, time, or title</li>
          <li><strong>map():</strong> Render each task</li>
          <li><strong>reduce():</strong> Calculate statistics</li>
        </ul>
      </div>
    </div>
  );
}

export default ComplexLists;










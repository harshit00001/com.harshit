import { useState } from 'react'
import './App.css'

function App() {
  const [currentDay, setCurrentDay] = useState(1)

  const days = [
    { id: 1, title: 'Day 1: React Basics', path: '/day1' },
    { id: 2, title: 'Day 2: State Management', path: '/day2' },
    { id: 3, title: 'Day 3: Event Handling & Forms', path: '/day3' },
    { id: 4, title: 'Day 4: Lists & Conditional Rendering', path: '/day4' },
    { id: 5, title: 'Day 5: Advanced Hooks', path: '/day5' },
    { id: 6, title: 'Day 6: Routing & Navigation', path: '/day6' },
    { id: 7, title: 'Day 7: API Integration', path: '/day7' },
  ]

  return (
    <div className="app">
      <header className="app-header">
        <h1>🚀 React 7 Days Interview Learning</h1>
        <p>Practical Projects with Detailed Examples</p>
      </header>
      
      <main className="app-main">
        <div className="days-grid">
          {days.map(day => (
            <div 
              key={day.id} 
              className="day-card"
              onClick={() => setCurrentDay(day.id)}
            >
              <h2>{day.title}</h2>
              <p>Click to view Day {day.id} examples</p>
            </div>
          ))}
        </div>

        <div className="current-day">
          <h2>Currently Selected: Day {currentDay}</h2>
          <p>Navigate to each day's folder to see detailed examples</p>
          <p>Each day has its own README with execution steps</p>
        </div>
      </main>
    </div>
  )
}

export default App


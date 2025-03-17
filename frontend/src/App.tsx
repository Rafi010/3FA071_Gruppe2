import React from 'react';
import logo from './logo.svg';
import './App.css';
import MyComponent from './components/CustomerGrid';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>People List</h1>
        <MyComponent /> {/* Use MyComponent to display the list */}
      </header>
    </div>
  );
}

export default App;

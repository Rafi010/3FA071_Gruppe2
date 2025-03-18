import React, { useState } from 'react';
import logo from './logo.svg';
import './App.css';
import DataSelection from './components/DataSelection';
import CustomerPage from './components/CustomerPage';
import ReadingPage from './components/ReadingPage';

function App() {

  const [alignment, setAlignment] = useState("customers");
  return (
    <div className="App">
      <header className="App-header">
        <DataSelection alignment={alignment} setAlignment={setAlignment}/>
        {alignment === "customers" ? (
        <CustomerPage/>
      ) : (
        <ReadingPage/>
      )}
      </header>
    </div>
  );
}

export default App;

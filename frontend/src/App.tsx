import React, { useState } from 'react';
import logo from './logo.svg';
import './App.css';
import Visualization, { DataType } from './components/Visualization';
import DataSelection from './components/DataSelection';

function App() {

  const [alignment, setAlignment] = useState("customers");
  return (
    <div className="App">
      <header className="App-header">
        <h1>People List</h1>
        <DataSelection alignment={alignment} setAlignment={setAlignment}/>
        <Visualization dataType={alignment as DataType}/>
      </header>
    </div>
  );
}

export default App;

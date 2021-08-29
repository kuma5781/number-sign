import React, { useState, useEffect } from 'react';
import logo from './logo.svg';
import './App.css';

const BackendUrl = process.env.REACT_APP_BACKEND_URL as string;

const App: React.FC = () => {
  const [name, setName] = useState('');

  useEffect(() => {
    fetch(BackendUrl)
      .then((response) => response.json())
      .then((json) => console.log(json.name))
      .catch((err) => console.error(err));
  });

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit
          {' '}
          {() => setName(name)}
          <code>src/App.tsx</code>
          {' '}
          and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
};

export default App;

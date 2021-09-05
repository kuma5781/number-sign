import React, { useState, useEffect } from 'react';
import './App.css';
import { GoogleButton, IAuthorizationOptions } from 'react-google-oauth2';
import logo from './logo.svg';

const backendUrl = process.env.REACT_APP_BACKEND_URL as string;

const App: React.FC = () => {
  const [name, setName] = useState('');

  useEffect(() => {
    console.log(process.env.REACT_APP_BACKEND_URL as string);
    console.log(process.env.REACT_APP_CLIENT_ID as string);

    fetch(backendUrl)
      .then((response) => response.json())
      .then((json) => console.log(json.name))
      .catch((err) => console.error(err));
  });

  const options: IAuthorizationOptions = {
    clientId: (process.env.REACT_APP_CLIENT_ID as string),
    redirectUri: 'http://localhost:3000/login/oauth2/code/google',
    scopes: ['openid', 'profile', 'email'],
    includeGrantedScopes: true,
    accessType: 'offline',
  };

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
        <GoogleButton
          // placeholder="demo/search.png" // Optional
          options={options}
          apiUrl="http://localhost:5000/google_login"
          defaultStyle
        />
      </header>
    </div>
  );
};

export default App;

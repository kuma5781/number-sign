import * as React from 'react';
import {
  GoogleButton,
  IAuthorizationOptions,
} from 'react-google-oauth2';

const Login: React.FC = () => {
  const options: IAuthorizationOptions = {
    clientId: (process.env.REACT_APP_CLIENT_ID as string),
    redirectUri: 'http://localhost:3000/login/oauth2/code/google',
    scopes: ['openid', 'profile', 'email'],
    includeGrantedScopes: true,
    accessType: 'offline',
  };

  return (
    <GoogleButton
      // placeholder="demo/search.png" // Optional
      options={options}
      apiUrl="http://localhost:5000/google_login"
      defaultStyle
    />
  );
};

export default Login;

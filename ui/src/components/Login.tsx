import React from 'react';
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
      options={options}
      apiUrl="http://localhost:5000/google_login"
      defaultStyle
    >
      ログイン
    </GoogleButton>
  );
};

export default Login;

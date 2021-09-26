import React from 'react';
import { BrowserRouter, Switch } from 'react-router-dom';
import Login from '../login/Login';
import Top from '../Top';
import SignUp from '../signup/SignUp';
import { AuthProvider } from '../context/AuthContext';
import PrivateRoute from './PrivateRoute';
import PublicRoute from './PublicRoute';

const Router: React.FC = () => (
  <AuthProvider>
    <BrowserRouter>
      <Switch>
        <PrivateRoute exact path="/" component={Top} />
        <PublicRoute path="/signup" component={SignUp} />
        <PublicRoute path="/login" component={Login} />
      </Switch>
    </BrowserRouter>
  </AuthProvider>
);

export default Router;

import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Login from '../pages/login/Login';
import Top from '../pages/top/Top';
import SignUp from '../pages/signup/SignUp';
import { AuthProvider } from '../components/context/AuthContext';
import PrivateRoute from './PrivateRoute';
import PublicRoute from './PublicRoute';
import NotFound from '../pages/notFound/NotFound';

const Router: React.FC = () => (
  <AuthProvider>
    <BrowserRouter>
      <Switch>
        <PrivateRoute exact path="/" component={Top} />
        <PublicRoute path="/signup" component={SignUp} />
        <PublicRoute path="/login" component={Login} />
        <Route component={NotFound} />
      </Switch>
    </BrowserRouter>
  </AuthProvider>
);

export default Router;

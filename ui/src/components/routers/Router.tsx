import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Login from '../login/Login';
import TopRouter from '../top/TopRouter';
import SignUp from '../signup/SignUp';
import { AuthProvider } from '../context/AuthContext';
import PrivateRoute from './PrivateRoute';
import PublicRoute from './PublicRoute';
import NotFound from '../notFound/NotFound';

const Router: React.FC = () => (
  <AuthProvider>
    <BrowserRouter>
      <Switch>
        <PrivateRoute exact path="/" component={TopRouter} />
        <PublicRoute path="/signup" component={SignUp} />
        <PublicRoute path="/login" component={Login} />
        <Route component={NotFound} />
      </Switch>
    </BrowserRouter>
  </AuthProvider>
);

export default Router;

import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Top from './Top';
import Login from './Login';
import App from '../App';

// 各ページの呼び出し画面
const Router: React.FC = () => (
  <BrowserRouter>
    <Switch>
      <Route exact path="/App" component={App} />
      <Route exact path="/" component={Login} />
      <Route exact path="/login/oauth2/code/google" component={Top} />
    </Switch>
  </BrowserRouter>
);

export default Router;

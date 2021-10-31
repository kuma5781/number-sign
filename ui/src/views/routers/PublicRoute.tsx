import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { useAuthContext } from '../components/context/AuthContext';

// PublicRoute: 未ログインユーザでも入れるページ
const PublicRoute: React.FC<{
    path:string;
    component:React.FC;
    }> = (props) => {
      const { path, component } = props;
      const { user } = useAuthContext() || {};
      return !user ? (<Route path={path} component={component} />) : (<Redirect to="/" />);
    };

export default PublicRoute;

import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { useAuthContext } from '../context/AuthContext';

const PrivateRoute: React.FC<{
    exact:boolean;
    path:string;
    component:React.FC;
    }> = (props) => {
      const { exact, path, component } = props;
      const { user } = useAuthContext() || {};
      return user ? (<Route exact={exact} path={path} component={component} />) : (<Redirect to="/login" />);
    };

export default PrivateRoute;

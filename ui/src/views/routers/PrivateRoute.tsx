import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { useAuthContext } from '../components/context/AuthContext';
import RequestError from '../pages/requestError/RequestError';

// PrivateRoute: ログイン済みのユーザが入れるページ
const PrivateRoute: React.FC<{
    exact:boolean;
    path:string;
    component:React.FC;
    }> = (props) => {
      const { exact, path, component } = props;
      const { user, error } = useAuthContext() || {};
      if (!user) {
        return <Redirect to="/login" />;
      }
      if (error) {
        return <RequestError />;
      }
      return <Route exact={exact} path={path} component={component} />;
    };

export default PrivateRoute;

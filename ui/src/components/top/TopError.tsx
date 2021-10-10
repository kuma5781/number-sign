import React from 'react';
import { useHistory } from 'react-router-dom';
import { auth } from '../../firebase';
import { useAuthContext } from '../context/AuthContext';
import './TopError.css';

const TopError: React.FC = () => {
  const history = useHistory();
  const handleLogout = () => {
    auth.signOut();
    history.push('/login');
  };
  const { error } = useAuthContext() || {};
  return (
    <div className="topErrorContainer">
      <h1 className="topErrorMessage">
        {error}
      </h1>
      <button className="topErrorButton" onClick={handleLogout}>ログインページに戻る</button>
    </div>
  );
};

export default TopError;
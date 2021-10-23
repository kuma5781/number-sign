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
    <div className="top-error-container">
      <h1 className="top-error-message">
        {error}
      </h1>
      <button className="top-error-button" onClick={handleLogout}>ログインページに戻る</button>
    </div>
  );
};

export default TopError;

import React from 'react';
import { useHistory } from 'react-router-dom';
import { auth } from '../../../firebase';
import { useAuthContext } from '../../components/context/AuthContext';
import './RequestError.css';

const TopError: React.FC = () => {
  const history = useHistory();
  const handleLogout = () => {
    auth.signOut();
    history.push('/login');
  };
  const { error } = useAuthContext() || {};
  return (
    <div className="request-error-container">
      <h1 className="request-error-message">
        {error}
      </h1>
      <button className="request-error-button" onClick={handleLogout}>ログインページに戻る</button>
    </div>
  );
};

export default TopError;

import React from 'react';
import { useHistory } from 'react-router-dom';
import { auth } from '../../firebase';
import { useAuthContext } from '../context/AuthContext';

const TopError: React.FC = () => {
  const history = useHistory();
  const handleLogout = () => {
    auth.signOut();
    history.push('/login');
  };
  const { error } = useAuthContext() || {};
  return (
    <div>
      <div>
        {error}
      </div>
      <button onClick={handleLogout}>ログアウト</button>
    </div>
  );
};

export default TopError;

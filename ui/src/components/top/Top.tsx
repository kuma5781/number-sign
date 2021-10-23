import React from 'react';
import { useHistory } from 'react-router-dom';
import { auth } from '../../firebase';
import { useAuthContext } from '../context/AuthContext';

const Top: React.FC = () => {
  const history = useHistory();
  const handleLogout = () => {
    auth.signOut();
    history.push('/login');
  };
  const { userInfo } = useAuthContext() || {};
  return (
    <div>
      <h1>トップページ</h1>
      <p>{userInfo.id}</p>
      <p>{userInfo.name}</p>
      <p>{userInfo.email}</p>
      <button onClick={handleLogout}>ログアウト</button>
    </div>
  );
};

export default Top;

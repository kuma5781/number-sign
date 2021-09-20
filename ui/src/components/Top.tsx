import React from 'react';
import { useHistory } from 'react-router-dom';
import { auth } from '../firebase';

const Top: React.FC = () => {
  const history = useHistory();
  const handleLogout = () => {
    auth.signOut();
    history.push('/login');
  };
  return (
    <div>
      <h1>トップページ</h1>
      <button onClick={handleLogout}>ログアウト</button>
    </div>
  );
};

export default Top;

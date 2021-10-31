import React from 'react';
import { useHistory } from 'react-router-dom';
import { auth } from '../../../firebase';
import { useAuthContext } from '../../components/context/AuthContext';
import Header from '../../components/header/Header';

const Top: React.FC = () => {
  const { userInfo } = useAuthContext() || {};
  return (
    <div>
      <Header />
      <h1>トップページ</h1>
      <p>{userInfo.id}</p>
      <p>{userInfo.name}</p>
      <p>{userInfo.email}</p>
    </div>
  );
};

export default Top;

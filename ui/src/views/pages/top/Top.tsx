import React from 'react';
import { useAuthContext } from '../../components/context/AuthContext';
import Header from '../../components/header/Header';
import Side from '../../components/side/Side';
import './Top.css';

const Top: React.FC = () => {
  const { userInfo } = useAuthContext() || {};
  return (
    <div className="top-container">
      <Header />
      <Side />
      <div className="top-box-text">
        <p>
          <span className="top-text-hand">👏</span>
          <span className="top-text-hello">こんにちは。</span>
        </p>
        <p>Number Signはマークダウン記法で書くことができるメモアプリです。</p>
        <p>{`新しくフォルダやノートを作成するには、サイドバーの「${userInfo.name}さんのワークスペース」またはフォルダを右クリックします。`}</p>
        <p>ログアウトするには、右上のアカウント名をクリックします。</p>
      </div>
    </div>
  );
};

export default Top;

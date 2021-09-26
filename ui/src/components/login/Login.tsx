import React, { useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { auth } from '../../firebase';
import './Login.css';

// Todo: any型なくす

const Login: React.FC = () => {
  const history = useHistory();
  const [error, setError] = useState('');
  const handleSubmit = async (event: any) => {
    event.preventDefault();
    const { email, password } = event.target.elements;
    try {
      await auth.signInWithEmailAndPassword(email.value, password.value);
      history.push('/');
    } catch (e: any) {
      switch (e.code) {
        case 'network-request-failed':
          setError('ネットワークエラーです。再度やり直してください。');
          break;
        case 'auth/too-many-requests':
          setError('アクセス回数の上限を超えました。しばらくして再度ログインしてください。');
          break;
        case 'auth/invalid-email':
          setError('メールアドレスの形式が正しくありません。');
          break;
        case 'auth/internal-error':
          setError('認証サーバエラー。リクエスト時エラーが発生しました。');
          break;
        case 'auth/user-not-found':
          setError('ユーザ名またはパスワードが違います。');
          break;
        case 'auth/wrong-password':
          setError('ユーザ名またはパスワードが違います。');
          break;
        default:
          setError(`アカウントの作成に失敗しました。再度やり直してください。${e.message}`);
      }
    }
  };

  return (
    <div>
      <h1>ログイン</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>メールアドレス</label>
          <input name="email" type="email" placeholder="email" />
        </div>
        <div>
          <label>パスワード</label>
          <input name="password" type="password" placeholder="password" />
        </div>
        <div>
          <button>ログイン</button>
        </div>
        <div>
          ユーザ登録は
          <Link to="/signup">こちら</Link>
          から
        </div>
      </form>
    </div>
  );
};

export default Login;

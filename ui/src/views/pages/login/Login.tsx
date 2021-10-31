import React, { useState, FormEvent } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { auth } from '../../../firebase';
import './Login.css';

const Login: React.FC = () => {
  const history = useHistory();
  const [error, setError] = useState('');
  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const { email, password } = event.currentTarget;
    try {
      await auth.signInWithEmailAndPassword(email.value, password.value);
      history.push('/');
      // firebaseのログインエラーの型が分からなかったためany型にしている、分かり次第変更
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
    <div className="login-container">
      <div className="login-inner-container">
        <form onSubmit={handleSubmit}>
          <div className="login-box-email">
            <label htmlFor="email" className="login-label-email">メールアドレス</label>
            <br />
            <input id="email" className="login-input-email" name="email" type="email" />
          </div>
          <div className="login-box-password">
            <label htmlFor="password" className="login-label-password">パスワード</label>
            <br />
            <input id="password" className="login-input-password" name="password" type="password" />
          </div>
          <p className="login-box-error">
            {error && <p style={{ color: 'red' }}>{error}</p>}
          </p>
          <div className="login-box-button">
            <button className="login-button">ログイン</button>
          </div>
          <div className="login-box-to-signup">
            <Link to="/signup" className="login-box-to-signup-link">
              <span>アカウントをお持ちでない方はこちら</span>
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;

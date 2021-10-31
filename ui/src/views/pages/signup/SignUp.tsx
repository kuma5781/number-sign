import React, { useState, FormEvent } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { auth } from '../../../firebase';
import './SignUp.css';

const SignUp: React.FC = () => {
  const history = useHistory();
  const [error, setError] = useState('');
  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const { email, password } = event.currentTarget;
    try {
      await auth.createUserWithEmailAndPassword(email.value, password.value);
      history.push('/');
      // firebaseのログインエラーの型が分からなかったためany型にしている、分かり次第変更
    } catch (e: any) {
      switch (e.code) {
        case 'network-request-failed':
          setError('ネットワークエラーです。再度やり直してください。');
          break;
        case 'auth/missing-email':
          setError('メールアドレスを入力してください。');
          break;
        case 'auth/invalid-email':
          setError('メールアドレスの形式が正しくありません。');
          break;
        case 'auth/internal-error':
          setError('認証サーバエラー。リクエスト時エラーが発生しました。');
          break;
        case 'auth/email-already-in-use':
          setError('メールアドレスが既に使用されています。');
          break;
        case 'auth/weak-password':
          setError('パスワードは6文字以上で登録してください。');
          break;
        default:
          setError(`ログインに失敗しました。再度やり直してください。${e.message}`);
      }
    }
  };

  return (
    <div className="sign-up-container">
      <div className="sign-up-inner-container">
        <form onSubmit={handleSubmit}>
          <div className="sign-up-box-email">
            <label htmlFor="email" className="sign-up-label-email">メールアドレス</label>
            <input id="email" className="sign-up-input-email" name="email" type="email" />
          </div>
          <div className="sign-up-box-password">
            <label htmlFor="password" className="sign-up-label-password">パスワード</label>
            <input id="password" className="sign-up-input-password" name="password" type="password" />
          </div>
          <p className="sign-up-box-error">
            {error && <p style={{ color: 'red' }}>{error}</p>}
          </p>
          <div className="sign-up-box-button">
            <button className="sign-up-button">アカウント登録</button>
          </div>
          <div className="sign-up-box-to-login">
            <Link to="/login" className="sign-up-box-to-login-link">
              <span>アカウント登録お済みの方はこちら</span>
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SignUp;

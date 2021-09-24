import React, { useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { auth } from '../firebase';

// Todo: any型なくす

const SignUp: React.FC = () => {
  const history = useHistory();
  const [error, setError] = useState('');
  const handleSubmit = async (event: any) => {
    event.preventDefault();
    const { email, password } = event.target.elements;
    try {
      await auth.createUserWithEmailAndPassword(email.value, password.value);
      history.push('/');
    } catch (e: any) {
      console.log(typeof e);
      switch (e.code) {
        case 'network-request-failed':
          setError('ネットワークエラーです。再度やり直してください。');
          break;
        case 'auth/weak-password':
          setError('パスワードは6文字以上で登録してください。');
          break;
        case 'auth/email-already-in-use':
          setError('メールアドレスが既に使用されています。');
          break;
        default:
          setError('アカウントの作成に失敗しました。再度やり直してください。');
      }
    }
  };

  return (
    <div>
      <h1>ユーザ登録</h1>
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
          <button>登録</button>
        </div>
        <div>
          ユーザ登録済の場合は
          <Link to="/login">こちら</Link>
          から
        </div>
      </form>
    </div>
  );
};

export default SignUp;

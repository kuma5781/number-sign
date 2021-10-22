import React, {
  createContext, useState, useContext, useEffect,
} from 'react';
import firebase from 'firebase/compat/app';
import { auth } from '../../firebase';

const backendUrl = process.env.REACT_APP_BACKEND_URL as string;

type createContextType = {
  user: firebase.User | null,
  loading: boolean,
  userInfo: {
    id: number | null,
    name: string,
    email: string,
  },
  error: string
};

type userInfoType = {
  id: number | null,
  name: string,
  email: string
};

function contextFilteringByType<ContextType>() {
  const AuthContext = createContext<ContextType | undefined>(undefined);
  const useAuthContext = () => {
    const context = useContext(AuthContext);
    if (!context) {
      throw new Error('useContext must be inside a Provider with a value');
    }
    return context;
  };
  return [useAuthContext, AuthContext.Provider] as const;
}

export const [useAuthContext, SetAuthProvider] = contextFilteringByType<createContextType>();

export const AuthProvider: React.FC = ({ children }) => {
  const [user, setUser] = useState < firebase.User | null >(null);
  const [loading, setLoading] = useState(true);
  const [userInfo, setUserInfo] = useState<userInfoType>({ id: null, name: '', email: '' });
  const [error, setError] = useState('');

  useEffect(() => {
    const unsubscribed = auth.onAuthStateChanged((loginedUser) => {
      setUser(loginedUser);
      setLoading(false);

      // response.json()の返り値の型がPromise型のためasyncとawaitを使ってPromise解決後の値を取得
      const getUserInfo = async () => {
        try {
          if (typeof (loginedUser?.email) === 'string') {
            const response = await fetch(`${backendUrl}/user/email/${loginedUser.email}`, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
              },
            });
            if (!response.ok) {
              throw Error(`${response.status} ${response.statusText}`);
            }
            const result = await response.json();
            setUserInfo({ id: result.id, name: result.name, email: result.email });
          }
        } catch (e: unknown) {
          if (e instanceof Error) {
            setError(e.message);
          }
        }
      };
      getUserInfo();
    });

    return () => {
      unsubscribed();
    };
  }, []);

  if (loading) {
    return <p>loading...</p>;
  }
  return (
    <SetAuthProvider value={{
      user, loading, userInfo, error,
    }}
    >
      {!loading && children}
    </SetAuthProvider>
  );
};

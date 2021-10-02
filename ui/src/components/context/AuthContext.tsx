import React, {
  createContext, useState, useContext, useEffect,
} from 'react';
import { auth } from '../../firebase';

const backendUrl = process.env.REACT_APP_BACKEND_URL as string;

// Todo: any型をなくす

type createContextType = {
  user: any,
  loading: boolean
};

// Todo: Missing return type on function の警告を解消
export function contextFilteringByType<ContextType>() {
  const AuthContext = createContext<ContextType | undefined>(undefined);
  const useAuthContext = () => {
    const context = useContext(AuthContext);
    if (!context) {
      throw new Error('useCtx must be inside a Provider with a value');
    }
    return context;
  };
  return [useAuthContext, AuthContext.Provider] as const;
}

export const [useAuthContext, SetAuthProvider] = contextFilteringByType<createContextType>();

export const AuthProvider: React.FC = ({ children }: any) => {
  const [user, setUser] = useState<any>('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribed = auth.onAuthStateChanged((loginedUser) => {
      setUser(loginedUser);
      console.log(loginedUser?.email);
      setLoading(false);
      fetch(`${backendUrl}/user`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: loginedUser?.email }),
      })
        .then((response) => response.json())
        .catch((err) => console.error(err));
    });

    return () => {
      unsubscribed();
    };
  }, []);

  if (loading) {
    return <p>loading...</p>;
  }
  return (
    <SetAuthProvider value={{ user, loading }}>
      {!loading && children}
    </SetAuthProvider>
  );
};

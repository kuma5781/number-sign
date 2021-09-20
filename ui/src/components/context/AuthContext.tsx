import React, {
  createContext, useState, useContext, useEffect,
} from 'react';
import { auth } from '../../firebase';

// Todo: any型をなくす

type postContextType = {
  user: any,
  loading: boolean
};

// Todo: Missing return type on function の警告を解消
export function createCtx<ContextType>() {
  const AuthContext = createContext<ContextType | undefined>(undefined);
  const useAuthContext = () => {
    const maybeUseContext = useContext(AuthContext);
    if (!maybeUseContext) {
      throw new Error('useCtx must be inside a Provider with a value');
    }
    return maybeUseContext;
  };
  return [useAuthContext, AuthContext.Provider] as const;
}

export const [useAuthContext, SetAuthProvider] = createCtx<postContextType>();

export const AuthProvider: React.FC = ({ children }: any) => {
  const [user, setUser] = useState<any>('');
  const [loading, setLoading] = useState(true);

  const value = {
    user,
    loading,
  };
  useEffect(() => {
    const unsubscribed = auth.onAuthStateChanged((loginedUser) => {
      setUser(loginedUser);
      console.log(loginedUser?.email);
      setLoading(false);
    });

    return () => {
      unsubscribed();
    };
  }, []);

  if (loading) {
    return <p>loading...</p>;
  }
  return (
    <SetAuthProvider value={value}>
      {!loading && children}
    </SetAuthProvider>
  );
};

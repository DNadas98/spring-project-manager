import React, {createContext, ReactNode, useState} from "react";

interface AuthProviderProps {
  children: ReactNode;
}

interface AuthContextType {
  auth: any;
  setAuth: React.Dispatch<React.SetStateAction<any>>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({children}: AuthProviderProps) {
  const [auth, setAuth] = useState({});

  return (
    <AuthContext.Provider value={{auth, setAuth}}>
      {children}
    </AuthContext.Provider>
  );
}

export default AuthContext;

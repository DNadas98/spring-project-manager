import {createContext, ReactNode, useContext, useState} from "react";

interface AuthenticationProviderProps {
  children: ReactNode;
}

export enum GlobalRole {
  USER, ADMIN
}

export interface UserInfo {
  username: string;
  email: string;
  roles: Set<GlobalRole>;
}

export interface IAuthentication {
  userInfo?: UserInfo,
  accessToken?: string
}

export interface AuthenticationContextType {
  login: (authentication: IAuthentication) => void;
  logout: () => void;
  getUsername: () => string | undefined;
  getEmail: () => string | undefined;
  getRoles: () => Set<GlobalRole> | undefined;
  getAccessToken: () => string | undefined;
}

export const AuthenticationContext = createContext<AuthenticationContextType | undefined>(undefined);

export function AuthenticationProvider({children}: AuthenticationProviderProps) {
  const [authentication, setAuthentication] = useState<IAuthentication>({});

  const login = (authentication: IAuthentication) => {
    setAuthentication(authentication);
  };

  const logout = () => {
    setAuthentication({});
  };

  const getUsername = () => {
    return authentication.userInfo?.username;
  };

  const getEmail = () => {
    return authentication.userInfo?.email;
  };

  const getRoles = () => {
    return authentication.userInfo?.roles;
  };

  const getAccessToken = () => {
    return authentication.accessToken;
  };

  return (
    <AuthenticationContext.Provider
      value={{login, logout, getUsername, getEmail, getRoles, getAccessToken}}>
      {children}
    </AuthenticationContext.Provider>
  );
}

export function useAuthentication() {
  return useContext(AuthenticationContext);
}

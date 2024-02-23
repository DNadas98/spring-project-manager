import {createContext, ReactNode, useContext, useState} from "react";
import {AuthenticationDto} from "../dto/AuthenticationDto.ts";
import {IAuthenticationContext} from "./IAuthenticationContext.ts";

interface AuthenticationProviderProps {
    children: ReactNode;
}

export const AuthenticationContext = createContext<IAuthenticationContext | undefined>(undefined);

export function AuthenticationProvider({children}: AuthenticationProviderProps) {
    const [authentication, setAuthentication] = useState<AuthenticationDto>({});

    const login = (authentication: AuthenticationDto) => {
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

export function useAuthentication(): IAuthenticationContext | undefined {
    return useContext(AuthenticationContext);
}

import {AuthenticationDto} from "../dto/AuthenticationDto.ts";
import {GlobalRole} from "../dto/userInfo/GlobalRole.ts";

export interface IAuthenticationContext {
    login: (authentication: AuthenticationDto) => void;
    logout: () => void;
    getUsername: () => string | undefined;
    getEmail: () => string | undefined;
    getRoles: () => Set<GlobalRole> | undefined;
    getAccessToken: () => string | undefined;
}

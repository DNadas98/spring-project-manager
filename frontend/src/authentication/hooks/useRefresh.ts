import * as apiService from "../../common/api/service/apiService.ts";
import {AuthenticationDto} from "../dto/AuthenticationDto.ts";
import {useNotification} from "../../common/notification/context/NotificationProvider.tsx";
import useLogout from "./useLogout.ts";
import {useAuthentication} from "./useAuthentication.ts";

export default function useRefresh() {
    const authentication = useAuthentication();
    const notification = useNotification();
    const logout = useLogout();
    const refresh = async () => {
        try {
            const refreshResponse = await apiService.publicJsonFetch({
                path: "auth/refresh", method: "GET"
            });
            if (!refreshResponse
                || refreshResponse.status > 399
                || !refreshResponse.data
                || refreshResponse.error) {
                notification.openNotification({
                    type: "error",
                    message: refreshResponse.error ??
                        "Unauthorized",
                    vertical: "top",
                    horizontal: "center"
                });
                await logout();
            }
            const newAuthentication = refreshResponse.data as AuthenticationDto;
            authentication.authenticate(newAuthentication);
        } catch (e) {
            console.error("Failed to refresh authentication");
            notification.openNotification({
                type: "error",
                message: "Unauthorized",
                vertical: "top",
                horizontal: "center"
            });
            await logout();
        }
    };
    return refresh;
}

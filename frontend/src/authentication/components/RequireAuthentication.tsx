import {Outlet} from "react-router-dom";

import {GlobalRole} from "../dto/userInfo/GlobalRole.ts";
import useLogout from "../hooks/useLogout.ts";
import {useEffect, useState} from "react";
import useRefresh from "../hooks/useRefresh.ts";
import LoadingSpinner from "../../common/utils/components/LoadingSpinner.tsx";
import {useAuthentication} from "../hooks/useAuthentication.ts";
import {useNotification} from "../../common/notification/context/NotificationProvider.tsx";

interface RequireAuthProps {
    allowedRoles: Array<GlobalRole>;
}

export default function RequireAuthentication({allowedRoles}: RequireAuthProps) {
    const [loading, setLoading] = useState(true);
    const [allowed, setAllowed] = useState(false);
    const authentication = useAuthentication();
    const notification = useNotification();
    const refresh = useRefresh();
    const logout = useLogout();

    useEffect(() => {
        if (!authentication.getAccessToken()?.length) {
            refresh().then();
        }
    }, []);

    useEffect(() => {
        async function verifyAllowed() {
            const roles = authentication.getRoles();
            if (roles?.some(role => allowedRoles.includes(role))) {
                setAllowed(true);
            } else {
                notification.openNotification({
                    type: "error", message: "Unauthorized", vertical: "top", horizontal: "center"
                });
                await logout();
            }
        }

        if (authentication.getAccessToken()?.length) {
            verifyAllowed().finally(() => {
                setLoading(false);
            });
        }
    }, [authentication]);

    if (loading) {
        return (<LoadingSpinner/>);
    } else if (allowed) {
        return (<Outlet/>);
    }
    return null;
}

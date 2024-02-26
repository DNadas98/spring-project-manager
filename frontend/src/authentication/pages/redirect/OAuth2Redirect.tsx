import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import LoadingSpinner from "../../../common/utils/components/LoadingSpinner.tsx";
import {useNotification} from "../../../common/notification/context/NotificationProvider.tsx";
import useRefresh from "../../hooks/useRefresh.ts";
import useLogout from "../../hooks/useLogout.ts";
import {useAuthentication} from "../../hooks/useAuthentication.ts";

export default function OAuth2Redirect() {
    const navigate = useNavigate();
    const notification = useNotification();
  const refresh = useRefresh();
  const logout = useLogout();
    const authentication = useAuthentication();

  useEffect(() => {
    async function handleOauth2Login() {
        const searchParams = new URLSearchParams(location.search);
        const errorMessage = searchParams.get("error");
        if (errorMessage) {
            notification.openNotification({
                type: "error", message: errorMessage, vertical: "top", horizontal: "center"
            });
            return await logout();
        }
        return await refresh();
    }

      if (!authentication?.getAccessToken?.length) {
          handleOauth2Login().then(() => {
              return navigate("/user");
          }).catch(() => {
              notification.openNotification({
                  type: "error", message: "Failed to log in via OAuth2", vertical: "top", horizontal: "center"
              });
              logout().then();
          });
      } else {
          throw new Error();
      }
  }, []);

  return <LoadingSpinner/>;
}

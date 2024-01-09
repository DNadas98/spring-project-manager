import React, {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import useAuth from "../../auth/hooks/useAuth.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import useRefresh from "../hooks/useRefresh.js";
import useLogout from "../hooks/useLogout.js";

function OAuth2Redirect() {
  const {setAuth} = useAuth();
  const refresh = useRefresh();
  const logout = useLogout();
  const navigate = useNavigate();

  useEffect(() => {
    async function handleOauth2Login() {
      try {
        const responseObject = await refresh();
        if (!responseObject?.data) {
          throw new Error(responseObject?.error ?? "Failed to log in via OAuth2");
        }
        const accessToken = responseObject.data?.accessToken;
        const receivedUsername = responseObject.data?.userInfo?.username;
        const receivedEmail = responseObject.data?.userInfo?.email;
        const receivedRoles = responseObject.data?.userInfo?.roles;
        if (accessToken && receivedUsername && receivedEmail && receivedRoles) {
          setAuth({
            "username": receivedUsername, "email": receivedEmail, "roles": receivedRoles, "accessToken": accessToken
          });
          navigate("/companies");
        } else {
          throw new Error("OAuth2 login failed");
        }
      } catch (e) {
        console.error(e);
        await logout();
      }
    }

    handleOauth2Login();
  }, []);


  return <LoadingSpinner/>;
}

export default OAuth2Redirect;

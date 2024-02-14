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
        const searchParams = new URLSearchParams(location.search);
        const error = searchParams.get("error");
        if (error) {
          throw new Error(decodeURIComponent(error));
        }
        const responseObject = await refresh();
        if (!responseObject?.data) {
          throw new Error(responseObject?.error ?? "Failed to log in via OAuth2");
        }
        handleAuthentication(responseObject);
        navigate("/companies");
      } catch (e) {
        window.alert(e);
        await logout();
      }
    }

    function handleAuthentication(responseObject) {
      const accessToken = responseObject.data?.accessToken;
      const receivedUsername = responseObject.data?.userInfo?.username;
      const receivedEmail = responseObject.data?.userInfo?.email;
      const receivedRoles = responseObject.data?.userInfo?.roles;
      if (accessToken && receivedUsername && receivedEmail && receivedRoles) {
        setAuth({
          "username": receivedUsername,
          "email": receivedEmail,
          "roles": receivedRoles,
          "accessToken": accessToken
        });
      } else {
        throw new Error("Failed to log in via OAuth2");
      }
    }

    handleOauth2Login();
  }, []);


  return <LoadingSpinner/>;
}

export default OAuth2Redirect;

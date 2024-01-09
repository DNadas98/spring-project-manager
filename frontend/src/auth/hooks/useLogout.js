import {useCallback} from "react";
import useAuth from "./useAuth";
import {useLocation, useNavigate} from "react-router-dom";

function useLogout() {
  const {setAuth} = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const logout = useCallback(
    async (willfulLogout = false) => {
      try {
        const baseUrl = import.meta.env.VITE_API_BASE_URL;        const path = "auth/logout";
        await fetch(`${baseUrl}/${path}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json"
          },
          credentials: "include"
        });
      } catch (e) {
        console.error(e);
      } finally {
        const path = willfulLogout ? "/" : "/login";
        navigate(path, {state: {from: location}, replace: true});
        setAuth({});
      }
    },
    [setAuth, location, navigate]
  );

  return logout;
}

export default useLogout;

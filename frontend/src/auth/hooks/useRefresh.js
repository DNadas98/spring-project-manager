import useAuth from "./useAuth.js";
import useLogout from "./useLogout.js";

function useRefresh() {
  const {setAuth} = useAuth();
  const logout = useLogout();

  async function refresh() {
    try {
      const baseUrl = import.meta.env.VITE_API_BASE_URL;
      const path = "auth/refresh";
      const httpResponse = await fetch(`${baseUrl}/${path}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: "include"
      });
      const responseObject = await httpResponse.json();
      if (
        httpResponse?.status === 200 &&
        responseObject?.data?.userInfo?.username &&
        responseObject?.data?.userInfo?.email &&
        responseObject?.data?.userInfo?.roles &&
        responseObject?.data?.accessToken
      ) {
        setAuth({
          "username": responseObject.data.userInfo.username,
          "email": responseObject.data.userInfo.email,
          "roles": responseObject.data.userInfo.roles,
          "accessToken": responseObject.data.accessToken
        });
        return responseObject;
      } else {
        return await logout();
      }
    } catch (e) {
      console.error(e);
      return await logout();
    }
  }

  return refresh;
}

export default useRefresh;

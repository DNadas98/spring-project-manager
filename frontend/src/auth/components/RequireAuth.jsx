import React from "react";
import { Outlet } from "react-router-dom";
import RefreshHandler from "./RefreshHandler";
import Unauthorized from "../pages/Unauthorized.jsx";
import useAuth from "../hooks/useAuth.js";

// eslint-disable-next-line react/prop-types
function RequireAuth({ allowedRoles }) {
  const { auth } = useAuth();
  const hasAuthState = auth?.username && auth?.accessToken && auth?.roles;
  const hasRequiredRoles = auth.roles?.find((role) => allowedRoles?.includes(role));
  // eslint-disable-next-line react/prop-types
  if (hasAuthState && hasRequiredRoles) {
    return <Outlet />;
  } else if (hasAuthState) {
    return <Unauthorized />;
  }else {
    return <RefreshHandler allowedRoles={allowedRoles} />;
  }
}

export default RequireAuth;

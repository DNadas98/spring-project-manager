import React, { useEffect, useState } from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import useRefresh from "../hooks/useRefresh";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";

function RefreshHandler({ allowedRoles }) {
  const location = useLocation();
  const refresh = useRefresh();
  const [loading, setLoading] = useState(true);
  const [authorized, setAuthorized] = useState(null);

  useEffect(() => {
    async function handleRefresh() {
      try {
        const refreshResponse = await refresh();
        if (
          refreshResponse?.data?.accessToken
        ) {
          setAuthorized(true);
        } else {
          setAuthorized(false);
        }
      } catch (err) {
        setAuthorized(false);
      } finally {
        setLoading(false);
      }
    }
    handleRefresh();
  }, [allowedRoles, refresh]);
  return loading ? (
    <LoadingSpinner />
  ) : authorized ? (
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
}

export default RefreshHandler;

import React from "react";
import { Navigate, useLocation } from "react-router-dom";

const ProtectedRoute = ({ children, roles }) => {
  const token = localStorage.getItem("token");
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const location = useLocation();

  if (!token) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  if (roles && Array.isArray(roles) && roles.length > 0) {
    const userRoles = Array.isArray(user?.roles) ? user.roles : [];
    const isAuthorized = roles.some((r) => userRoles.includes(r));
    if (!isAuthorized) {
      // Not authorized: send non-admins to account, admins to admin dashboard
      return <Navigate to={userRoles.includes("ADMIN") ? "/admin/dashboard" : "/account"} replace />;
    }
  }

  return children;
};

export default ProtectedRoute;
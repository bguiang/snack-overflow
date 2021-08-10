import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { Route, Redirect } from "react-router-dom";
import jwt_decode from "jwt-decode";

// https://reactrouter.com/web/example/auth-workflow

// A wrapper for <Route> that redirects to the login
// screen if you're not yet authenticated.
function AdminRoute({ children, ...rest }) {
  let { currentUser } = useAuth();

  const isAdmin = () => {
    if (currentUser === null) return false;

    var decoded = jwt_decode(currentUser.authenticationToken);
    console.log(decoded);
    let auth = [];
    decoded.authorities.map((authority) => {
      auth.push(authority.authority);
    });
    console.log(auth);
    if (auth.includes("ROLE_ADMIN")) return true;
    else return false;
  };

  return (
    <Route
      {...rest}
      render={({ location }) =>
        currentUser && isAdmin() ? ( // check if authenticated and user is an admin
          children
        ) : (
          <Redirect
            to={{
              pathname: "/login",
              state: { from: location },
            }}
          />
        )
      }
    />
  );
}

export default AdminRoute;

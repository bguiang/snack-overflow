import React, { useContext, useState, createContext, useEffect } from "react";
import SnackOverflow from "../api/SnackOverflow";
import jwt_decode from "jwt-decode";

// Create context
const AuthContext = createContext();

// Custom context provider
export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);

  // Token Check on mount
  // - Call refresh in a useEffect with empty array arg IF currentUser is null OR currentUser.token is expired
  // - This will be called if the user refreshes the page or re-/enters the website
  // - This + the timed refresh guarantees that the JWT is always either null or non-expired???
  // - If refresh fails, setCurrentUser(null) in AuthContext
  useEffect(() => {
    if (currentUser) {
      if (currentUser.accessToken) {
        setRefreshTimer(currentUser.accessToken);
      } else {
        refresh(); // should never really happen
      }
    } else {
      refresh();
    }
  }, []);

  // Timed Background Refresh
  // - When getting an authentication response back from either /login or /refresh, set up a function timer to call /refresh 1 min before the current token's expiration
  const setRefreshTimer = (token) => {
    let decoded = jwt_decode(token);
    const expirationEpochMS = decoded.exp * 1000;
    const nowEpochMS = Date.now();

    const msToExpire = expirationEpochMS - nowEpochMS;

    if (msToExpire < 60000) {
      // If jwt is already within 1 minute of expiring, just call refresh. Includes less than zero or expired
      refresh();
    } else {
      // otherwise, set timer to one minute of expiring
      const oneMinuteToExpireMS = msToExpire - 60000;
      const oneMinuteToExpire = oneMinuteToExpireMS / 1000;
      setTimeout(() => {
        refresh();
      }, oneMinuteToExpireMS);
    }
  };

  const login = async (username, password) => {
    try {
      let loginRequest = { username: username, password: password };
      const response = await SnackOverflow.post("/auth/login", loginRequest);
      if (response.status === 200) {
        // Set CurrentUser
        setCurrentUser(response.data);
        // Set refresh timer function
        setRefreshTimer(response.data.authenticationToken);
        return true;
      }
      return false;
    } catch (error) {
      return false;
    }
  };
  const refresh = async () => {
    try {
      //browser has the refresh token cookie
      const response = await SnackOverflow.get("/auth/refresh");
      if (response.status === 200) {
        // Set CurrentUser
        setCurrentUser(response.data);
        // Set refresh timer function
        setRefreshTimer(response.data.authenticationToken);
      }
    } catch (error) {
      setCurrentUser(null);
    }
  };
  const logout = async () => {
    setCurrentUser(null);
    try {
      //browser has the refresh token cookie
      const response = await SnackOverflow.get("/auth/logout");
      if (response.status === 200) {
        setCurrentUser(null);
      }
    } catch (error) {}
  };

  // Context Value that we're passing down to child components of the provider
  // TODO: optimize by memoization
  const auth = {
    currentUser,
    login,
    logout,
  };

  return <AuthContext.Provider value={auth}>{children}</AuthContext.Provider>;
}

// expose context
export function useAuth() {
  return useContext(AuthContext);
}

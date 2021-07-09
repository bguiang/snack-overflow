import "./App.css";
import "@fontsource/roboto";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Snacks from "./components/Snacks";
import Snack from "./components/Snack";
import LoginSignup from "./components/LoginSignup";
import Account from "./components/Account";
import { useAuth } from "./context/AuthContext";

export const Routes = () => {
  const { currentUser } = useAuth();

  if (currentUser) return <AuthenticatedRoutes />;
  else return <UnauthenticatedRoutes />;
};

const AuthenticatedRoutes = () => {
  return (
    <Switch>
      <Route path="/snacks/:id">
        <Snack />
      </Route>
      <Route path="/about">
        <Snack />
      </Route>
      <Route path="/contact">
        <Snack />
      </Route>
      <Route path="/account">
        <Account />
      </Route>
      <Route path="/">
        <Snacks />
      </Route>
    </Switch>
  );
};

const UnauthenticatedRoutes = () => {
  return (
    <Switch>
      <Route path="/snacks/:id">
        <Snack />
      </Route>
      <Route path="/about">
        <Snack />
      </Route>
      <Route path="/contact">
        <Snack />
      </Route>
      <Route path="/account">
        <LoginSignup />
      </Route>
      <Route path="/">
        <Snacks />
      </Route>
    </Switch>
  );
};

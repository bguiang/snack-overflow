import { Button } from "@material-ui/core";
import React from "react";
import { useAuth } from "../context/AuthContext";
import useStyles from "../styles";

const Account = () => {
  const { logout } = useAuth();
  const classes = useStyles();
  return (
    <div>
      <h1>You are logged in, congrats!</h1>
      <h1>You are logged in, congrats!</h1>
      <h1>You are logged in, congrats!</h1>
      <h1>You are logged in, congrats!</h1>
      <Button onClick={() => logout()}>Logout</Button>
    </div>
  );
};

export default Account;

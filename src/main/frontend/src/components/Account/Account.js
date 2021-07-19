import React from "react";
import { Button } from "@material-ui/core";
import { Grid } from "@material-ui/core";
import { useAuth } from "../../context/AuthContext";
import useStyles from "../../styles";
import useOrders from "../../hooks/useOrders";

const Account = () => {
  const { currentUser, logout } = useAuth();
  const [orders] = useOrders();
  const classes = useStyles();
  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} md={10} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>
            Hello, {currentUser.username}
            <Button onClick={() => logout()}>Logout</Button>
          </h2>
        </Grid>
        <Grid
          item
          xs={12}
          md={10}
          key="orderInfo"
          className={classes.cartHeader}
        >
          <h2 className={classes.cartHeaderTitle}>Order Info</h2>
        </Grid>
      </Grid>
    </div>
  );
};

export default Account;

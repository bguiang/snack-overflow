import React from "react";
import { Button, Card, CardActionArea, Typography } from "@material-ui/core";
import { Grid } from "@material-ui/core";
import { useAuth } from "../../context/AuthContext";
import useStyles from "../../styles";
import useOrders from "../../hooks/useOrders";
import OrderItem from "./OrderItem";

const Account = () => {
  const { currentUser, logout } = useAuth();
  const [orders] = useOrders();
  const classes = useStyles();
  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>
            Hello, {currentUser.username}
          </h2>
          <Button variant="contained" color="primary" onClick={() => logout()}>
            Sign Out
          </Button>
        </Grid>

        <Grid item xs={12} key="orderInfo">
          <Typography variant="h6" className={classes.orderListTitleMobile}>
            Orders
          </Typography>
          <Grid
            item
            xs={12}
            key={"orderListTitle"}
            className={classes.orderListTitle}
          >
            <div className={classes.orderCard}>
              <div className={classes.orderCardActionArea}>
                <Typography
                  variant="h6"
                  className={classes.orderCardActionAreaItem}
                >
                  Order
                </Typography>
                <Typography
                  variant="h6"
                  className={classes.orderCardActionAreaItem2}
                >
                  Created
                </Typography>
                <Typography
                  variant="h6"
                  className={classes.orderCardActionAreaItem2}
                >
                  Status
                </Typography>
                <Typography
                  variant="h6"
                  className={classes.orderCardActionAreaItem}
                >
                  Total
                </Typography>
              </div>
            </div>
          </Grid>
          {orders.map((order) => (
            <OrderItem order={order} />
          ))}
        </Grid>
      </Grid>
    </div>
  );
};

export default Account;

import React from "react";
import { Button, Card, CardActionArea, Typography } from "@material-ui/core";
import { Grid } from "@material-ui/core";
import useStyles from "../../styles";
import useOrdersAsAdmin from "../../hooks/useOrdersAsAdmin";
import OrderCardAdmin from "./OrderCardAdmin";

const Orders = () => {
  const [orders, getOrders] = useOrdersAsAdmin();
  const classes = useStyles();
  return (
    <Grid container spacing={2} justifyContent="center" alignItems="center">
      <Grid item xs={12} key="pageTitle" className={classes.cartHeader}>
        <h2 className={classes.cartHeaderTitle}>Orders</h2>
      </Grid>
      <Grid container xs={12} spacing={1} key="orderInfo">
        <Grid item xs={12} key={"listTitle"} className={classes.orderListTitle}>
          <div className={classes.orderCard}>
            <div className={classes.orderCardActionArea}>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem}
              >
                Order
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem2}
              >
                Username
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem2}
              >
                Created
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem2}
              >
                Status
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem}
              >
                Total
              </Typography>
              <div className={classes.orderTitleFiller}></div>
            </div>
          </div>
        </Grid>
        {orders.map((order) => (
          <OrderCardAdmin order={order} />
        ))}
      </Grid>
    </Grid>
  );
};

export default Orders;

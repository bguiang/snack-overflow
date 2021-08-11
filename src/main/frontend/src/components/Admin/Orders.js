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
    <div className={classes.content}>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
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
              </div>
            </div>
          </Grid>
          {orders.map((order) => (
            <OrderCardAdmin order={order} />
          ))}
        </Grid>
      </Grid>
    </div>
  );
};

export default Orders;

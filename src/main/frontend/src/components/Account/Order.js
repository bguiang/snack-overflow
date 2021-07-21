import React, { useState, useEffect } from "react";
import { Button, Card, CardActionArea, Typography } from "@material-ui/core";
import { Grid } from "@material-ui/core";
import { useAuth } from "../../context/AuthContext";
import useStyles from "../../styles";
import OrderItem from "./OrderItem";
import SnackOverflow from "../../api/SnackOverflow";
import { useHistory, useParams } from "react-router-dom";

const Order = () => {
  const { currentUser, logout } = useAuth();
  const history = useHistory();
  const classes = useStyles();
  const [token, setToken] = useState(null);

  const [order, setOrder] = useState({
    id: null,
    items: [],
    total: 0,
    createdDate: null,
    billingDetails: null,
    shippingDetails: null,
    isShippingSameAsBilling: false,
    userId: null,
    status: null,
  });
  const { id } = useParams();

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  useEffect(() => {
    const getOrder = async () => {
      try {
        console.log("Order ID: " + id);
        const response = await SnackOverflow.get(`/orders/${id}`, {
          headers: { Authorization: token },
        });

        if (response.status === 200) {
          setOrder(response.data);
        } else {
          history.push("/account");
        }
      } catch (error) {
        console.log(error);
        history.push("/account");
      }
    };

    if (token !== null && id !== null) {
      getOrder();
    }
  }, [id, token]);

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
          <Typography variant="h6">Order #{order.id}</Typography>
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
                  #{order.id}
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem2}
                >
                  {new Date(order.createdDate).toLocaleDateString("en-US")}{" "}
                  {new Date(order.createdDate).toLocaleTimeString("en-US")}
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem2}
                >
                  {order.status}
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem}
                >
                  ${order.total.toFixed(2)}
                </Typography>
              </div>
            </div>
          </Grid>
        </Grid>
      </Grid>
    </div>
  );
};

export default Order;

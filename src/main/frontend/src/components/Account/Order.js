import React, { useState, useEffect } from "react";
import {
  Card,
  CardActionArea,
  CardContent,
  Typography,
} from "@material-ui/core";
import { Grid } from "@material-ui/core";
import { useAuth } from "../../context/AuthContext";
import useStyles from "../../styles";
import SnackOverflow from "../../api/SnackOverflow";
import { useHistory, useParams } from "react-router-dom";
import OrderItem from "./OrderItem";

const Order = () => {
  const { currentUser, logout } = useAuth();
  const history = useHistory();
  const classes = useStyles();
  const [token, setToken] = useState(null);

  const [order, setOrder] = useState(null);

  const { id } = useParams();

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  useEffect(() => {
    const getOrder = async () => {
      try {
        const response = await SnackOverflow.get(`/orders/${id}`, {
          headers: { Authorization: token },
        });

        if (response.status === 200) {
          setOrder(response.data);
        } else {
          history.push("/account");
        }
      } catch (error) {
        history.push("/account");
      }
    };

    if (token !== null && id !== null) {
      getOrder();
    }
  }, [id, token]);

  const orderItemClick = (id) => {
    history.push(`/snacks/${id}`);
  };

  if (order === null) return <></>;

  return (
    <div className={classes.content}>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} key="title">
          <Typography variant="h6">Order #{order.id}</Typography>
        </Grid>
        <Grid item xs={12} key="orderInfo">
          <Card className={classes.orderDetailsCard}>
            <CardActionArea>
              {order.items.map((item) => (
                <OrderItem
                  orderItem={item}
                  orderItemClick={orderItemClick}
                  key={item.id}
                />
              ))}
            </CardActionArea>
            <CardContent>
              <Typography variant="subtitle1" align="right">
                Created:{" "}
                {new Date(order.createdDate).toLocaleDateString("en-US")}{" "}
                {new Date(order.createdDate).toLocaleTimeString("en-US")}
              </Typography>
              <Typography variant="subtitle1" align="right">
                Status: {order.status}
              </Typography>
              <div>
                <Typography variant="subtitle1" align="right">
                  Total ${order.total.toFixed(2)}
                </Typography>
              </div>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={6} key="billing">
          <Card className={classes.orderBillingAndShippingCard}>
            <CardContent>
              <Typography variant="h6">Billing</Typography>
              <Typography variant="subtitle1">
                {order.billingDetails.name}
              </Typography>
              <Typography variant="subtitle1">
                {order.billingDetails.email}
              </Typography>
              <Typography variant="subtitle1">
                {order.billingDetails.phone}
              </Typography>
              <Typography variant="subtitle1">
                {order.billingDetails.address.addressLineOne}
              </Typography>
              {order.billingDetails.address.addressLineTwo ? (
                <Typography variant="subtitle1">
                  {order.billingDetails.address.addressLineTwo}
                </Typography>
              ) : null}
              <Typography variant="subtitle1">
                {order.billingDetails.address.city}
              </Typography>
              <Typography variant="subtitle1">
                {order.billingDetails.address.postalCode}
              </Typography>
              <Typography variant="subtitle1">
                {order.billingDetails.address.country}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={6} key="shipping">
          <Card className={classes.orderBillingAndShippingCard}>
            {order.isShippingSameAsBilling ? (
              <CardContent>
                <Typography variant="h6">Shipping</Typography>
                <Typography variant="subtitle1">
                  Shipped to the billing address
                </Typography>
              </CardContent>
            ) : (
              <CardContent>
                <Typography variant="h6">Shipping</Typography>
                <Typography variant="subtitle1">
                  {order.shippingDetails.name}
                </Typography>
                <Typography variant="subtitle1">
                  {order.shippingDetails.phone}
                </Typography>
                <Typography variant="subtitle1">
                  {order.shippingDetails.address.addressLineOne}
                </Typography>
                {order.shippingDetails.address.addressLineTwo ? (
                  <Typography variant="subtitle1">
                    {order.shippingDetails.address.addressLineTwo}
                  </Typography>
                ) : null}
                <Typography variant="subtitle1">
                  {order.shippingDetails.address.city}
                </Typography>
                <Typography variant="subtitle1">
                  {order.shippingDetails.address.postalCode}
                </Typography>
                <Typography variant="subtitle1">
                  {order.shippingDetails.address.country}
                </Typography>
              </CardContent>
            )}
          </Card>
        </Grid>
      </Grid>
    </div>
  );
};

export default Order;

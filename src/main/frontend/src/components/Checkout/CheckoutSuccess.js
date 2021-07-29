import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import useStyles from "../../styles";
import { useCart } from "../../context/CartContext";
import { useAuth } from "../../context/AuthContext";
import SnackOverflow from "../../api/SnackOverflow";
import { Elements, ElementsConsumer } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import CheckoutForm from "./CheckoutForm";

// Make sure to call `loadStripe` outside of a component’s render to avoid
// recreating the `Stripe` object on every render.
const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLIC_KEY);

const CheckoutSuccess = () => {
  const classes = useStyles();

  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} md={10} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Checkout Success!</h2>
        </Grid>
      </Grid>
    </div>
  );
};

export default CheckoutSuccess;

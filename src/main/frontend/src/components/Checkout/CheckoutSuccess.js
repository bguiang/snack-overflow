import React from "react";
import { Grid, Typography, Button, Link } from "@material-ui/core";
import useStyles from "../../styles";
import { loadStripe } from "@stripe/stripe-js";
import { useHistory } from "react-router-dom";

// Make sure to call `loadStripe` outside of a component’s render to avoid
// recreating the `Stripe` object on every render.
const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLIC_KEY);

const CheckoutSuccess = () => {
  const classes = useStyles();
  const history = useHistory();

  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} md={10} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Checkout Success!</h2>
        </Grid>
        <Grid item xs={12} md={10} key="body" className={classes.cartHeader}>
          <Typography>
            It may take a few minutes for your order to show up on your{" "}
            <Link onClick={() => history.push("/account")}>account</Link>
          </Typography>
        </Grid>
      </Grid>
    </div>
  );
};

export default CheckoutSuccess;

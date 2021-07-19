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

const Checkout = () => {
  const classes = useStyles();
  const { cart } = useCart();
  const [cartInfo, setCartInfo] = useState(null);
  const { currentUser } = useAuth();
  const [orderId, setOrderId] = useState(null);
  const [token, setToken] = useState(null);
  const [clientSecret, setClientSecret] = useState("");

  const startCheckoutAndCreatePaymentIntent = async () => {
    try {
      //TODO: use retrievePaymentIntent later so payment intents are not always created
      console.log("Cart");
      const response = await SnackOverflow.post(
        "/checkout/startCheckout",
        cart,
        {
          headers: { Authorization: token },
        }
      );
      if (201 === response.status) {
        console.log("client_secret: " + response.data.client_secret);
        console.log("orderId: " + response.data.orderId);
        console.log(response.data);
        setClientSecret(response.data.client_secret);
        setCartInfo(response.data.cart);
        setOrderId(response.data.orderId);
      } else {
        //history.push("/cart");
      }
    } catch (error) {
      console.log(error);
      //history.push("/cart");
    }
  };

  // Load/Update User Token
  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  useEffect(() => {
    if (token !== null && cart !== null && cart.length > 0)
      startCheckoutAndCreatePaymentIntent();
  }, [token, cart]);

  const InjectedCheckoutForm = () => (
    <ElementsConsumer>
      {({ stripe, elements }) => (
        <CheckoutForm
          stripe={stripe}
          elements={elements}
          clientSecret={clientSecret}
          token={token}
          orderId={orderId}
        />
      )}
    </ElementsConsumer>
  );

  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} md={10} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Checkout</h2>
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
        <Grid
          item
          xs={12}
          md={10}
          key="checkout"
          className={classes.cartHeader}
        >
          <Elements stripe={stripePromise}>
            <InjectedCheckoutForm />
          </Elements>
        </Grid>
      </Grid>
    </div>
  );
};

export default Checkout;

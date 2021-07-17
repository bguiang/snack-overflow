import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import useStyles from "../../styles";
import { useCart } from "../../context/CartContext";
import { useAuth } from "../../context/AuthContext";
import { useHistory } from "react-router-dom";
import SnackOverflow from "../../api/SnackOverflow";
import { Elements, ElementsConsumer } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import CheckoutForm from "./CheckoutForm";

// Make sure to call `loadStripe` outside of a component’s render to avoid
// recreating the `Stripe` object on every render.
const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLIC_KEY);

const Checkout = () => {
  const classes = useStyles();
  const history = useHistory();
  const { getCartInfo } = useCart();
  //const [cartInfo, setCartInfo] = useState({ items: [], total: 0 });
  const [cartInfo, setCartInfo] = useState(null); // null so its easier to detect
  const { currentUser } = useAuth();
  const [token, setToken] = useState(null);
  const [clientSecret, setClientSecret] = useState("");

  const createPaymentIntent = async () => {
    try {
      //TODO: use retrievePaymentIntent later so payment intents are not always created
      const response = await SnackOverflow.post(
        "/checkout/createPaymentIntent",
        cartInfo,
        {
          headers: { Authorization: token },
        }
      );
      if (201 === response.status) {
        console.log("client_secret: " + response.data.client_secret);
        setClientSecret(response.data.client_secret);
      } else {
        //history.push("/cart");
      }
    } catch (error) {
      console.log(error);
      //history.push("/cart");
    }
  };

  // Load Cart Info
  useEffect(() => {
    const fetchCartInfo = async () => {
      let info = await getCartInfo();
      setCartInfo(info);
    };
    if (cartInfo === null) fetchCartInfo();
  }, []);

  // Load/Update User Token
  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  useEffect(() => {
    if (token !== null && cartInfo !== null) createPaymentIntent();
  }, [cartInfo, token]);

  const InjectedCheckoutForm = () => (
    <ElementsConsumer>
      {({ stripe, elements }) => (
        <CheckoutForm stripe={stripe} elements={elements} />
      )}
    </ElementsConsumer>
  );

  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} md={10} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Checkout</h2>
        </Grid>
        <Grid item xs={12} md={10} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>
            TODO: show cart item summary and total here
          </h2>
        </Grid>
        <Grid item xs={12} md={10} key="body" className={classes.cartHeader}>
          <Elements stripe={stripePromise}>
            <InjectedCheckoutForm />
          </Elements>
        </Grid>
      </Grid>
    </div>
  );
};

export default Checkout;

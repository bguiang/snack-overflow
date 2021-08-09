import React, { useState, useEffect } from "react";
import { Grid, Card } from "@material-ui/core";
import useStyles from "../../styles";
import { useCart } from "../../context/CartContext";
import { useAuth } from "../../context/AuthContext";
import SnackOverflow from "../../api/SnackOverflow";
import { Elements, ElementsConsumer } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import CheckoutForm from "./CheckoutForm";
import CheckoutItem from "./CheckoutItem";
import { Typography } from "@material-ui/core";
import { useHistory } from "react-router-dom";

// Make sure to call `loadStripe` outside of a component’s render to avoid
// recreating the `Stripe` object on every render.
const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLIC_KEY);

const Checkout = () => {
  const classes = useStyles();
  const history = useHistory();
  const { cart } = useCart();
  const [cartInfo, setCartInfo] = useState({ items: [], total: 0 });
  const { currentUser } = useAuth();
  const [orderId, setOrderId] = useState(null);
  const [token, setToken] = useState(null);
  const [clientSecret, setClientSecret] = useState("");

  const startCheckoutAndCreatePaymentIntent = async () => {
    try {
      console.log("Cart");
      const cartInfoRequest = { items: cart };
      const response = await SnackOverflow.post(
        "/orders/start",
        cartInfoRequest,
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
        history.push("/");
      }
    } catch (error) {
      console.log(error);
      history.push("/");
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
        <Grid item xs={12} key="title" className={classes.checkoutHeader}>
          <h2 className={classes.checkoutHeaderTitle}>Checkout</h2>
        </Grid>
        <Grid
          container
          spacing={1}
          justifyContent="center"
          alignItems="flex-start"
        >
          <Grid item sm={12} md={6} key="order-info">
            <Card className={classes.checkoutOrderInfo}>
              <Typography variant="h6">Order Info</Typography>
              {cartInfo.items.map((checkoutItem) => (
                <CheckoutItem
                  checkoutItem={checkoutItem}
                  key={checkoutItem.product.id}
                />
              ))}
              <Typography variant="subtitle1" className={classes.checkoutTotal}>
                Total ${cartInfo.total.toFixed(2)}
              </Typography>
            </Card>
          </Grid>
          <Grid item sm={12} md={6} key="checkout" flex>
            <Elements stripe={stripePromise}>
              <InjectedCheckoutForm />
            </Elements>
          </Grid>
        </Grid>
      </Grid>
    </div>
  );
};

export default Checkout;

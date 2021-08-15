import React, { useState } from "react";
import useStyles from "../../styles";
import { useStripe, useElements, CardElement } from "@stripe/react-stripe-js";
import { useHistory } from "react-router-dom";
import { useCart } from "../../context/CartContext";

import CardSection from "./CardSection";
import SnackOverflow from "../../api/SnackOverflow";
import {
  Card,
  Typography,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
} from "@material-ui/core";

const CheckoutForm = ({ clientSecret, token }) => {
  const { clearItems } = useCart();
  const history = useHistory();
  const classes = useStyles();
  const stripe = useStripe();
  const elements = useElements();
  const [paymentErrorMessage, setPaymentErrorMessage] = useState("");
  const [billingName, setBillingName] = useState("");
  const [billingEmail, setBillingEmail] = useState("");
  const [billingPhone, setBillingPhone] = useState("");
  const [billingAddressLine1, setBillingAddressLine1] = useState("");
  const [billingAddressLine2, setBillingAddressLine2] = useState("");
  const [billingCity, setBillingCity] = useState("");
  const [billingState, setBillingState] = useState("");
  const [billingPostalCode, setBillingPostalCode] = useState("");
  const [billingCountry, setBillingCountry] = useState("US");
  const [isShippingSameAsBilling, setIsShippingSameAsBilling] = useState(true);
  const [shippingName, setShippingName] = useState("");
  const [shippingPhone, setShippingPhone] = useState("");
  const [shippingAddressLine1, setShippingAddressLine1] = useState("");
  const [shippingAddressLine2, setShippingAddressLine2] = useState("");
  const [shippingCity, setShippingCity] = useState("");
  const [shippingState, setShippingState] = useState("");
  const [shippingPostalCode, setShippingPostalCode] = useState("");
  const [shippingCountry, setShippingCountry] = useState("US");

  const confirmPayment = async () => {
    let shipping = {};

    if (isShippingSameAsBilling) {
      shipping = {
        address: {
          line1: billingAddressLine1,
          line2: billingAddressLine2,
          city: billingCity,
          state: billingState,
          postal_code: billingPostalCode,
          country: billingCountry,
        },
        name: billingName,
        phone: billingPhone,
      };
    } else {
      shipping = {
        address: {
          line1: shippingAddressLine1,
          line2: shippingAddressLine2,
          city: shippingCity,
          state: shippingState,
          postal_code: shippingPostalCode,
          country: shippingCountry,
        },
        name: shippingName,
        phone: shippingPhone,
      };
    }

    const result = await stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: elements.getElement(CardElement),
        billing_details: {
          address: {
            line1: billingAddressLine1,
            line2: billingAddressLine2,
            city: billingCity,
            state: billingState,
            postal_code: billingPostalCode,
            country: billingCountry,
          },
          name: billingName,
          phone: billingPhone,
          email: billingEmail,
        },
      },
      shipping: shipping,
    });

    if (result.error) {
      // Show error to your customer (e.g., insufficient funds)
      console.log(result.error.message);
      setPaymentErrorMessage(result.error.message);
    } else {
      // The payment has been processed!
      if (result.paymentIntent.status === "succeeded") {
        // Show a success message to your customer
        // There's a risk of the customer closing the window before callback
        // execution. Set up a webhook or plugin to listen for the
        // payment_intent.succeeded event that handles any business critical
        // post-payment actions.

        console.log("Payment Success");
        clearItems();
        history.push("/checkout/success");
      }
      console.log(result);
    }
  };

  const handleSubmit = async (event) => {
    // We don't want to let default form submission happen here,
    // which would refresh the page.
    event.preventDefault();

    setPaymentErrorMessage("");
    if (!stripe || !elements) {
      // Stripe.js has not yet loaded.
      // Make sure to disable form submission until Stripe.js has loaded.
      return;
    }

    confirmPayment();
  };

  return (
    <form onSubmit={handleSubmit} className={classes.checkoutForm}>
      <div>
        <Card className={classes.addressCard}>
          <Typography variant="h6">Billing Details</Typography>
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            required
            fullWidth
            label="name"
            onChange={(event) => {
              setBillingName(event.target.value);
            }}
            autoComplete="name"
          />
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            required
            fullWidth
            label="email"
            onChange={(event) => {
              setBillingEmail(event.target.value);
            }}
            autoComplete="email"
          />
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            required
            fullWidth
            label="phone"
            onChange={(event) => {
              setBillingPhone(event.target.value);
            }}
            autoComplete="phone"
          />
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            required
            fullWidth
            label="address line 1"
            onChange={(event) => {
              setBillingAddressLine1(event.target.value);
            }}
            autoComplete="address-line1"
          />
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            fullWidth
            label="address line 2"
            onChange={(event) => {
              setBillingAddressLine2(event.target.value);
            }}
            autoComplete="address-line2"
          />
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            required
            fullWidth
            label="city"
            onChange={(event) => {
              setBillingCity(event.target.value);
            }}
            autoComplete="address-level2"
          />
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            required
            fullWidth
            label="state"
            onChange={(event) => {
              setBillingState(event.target.value);
            }}
            autoComplete="address-level1"
          />
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            required
            fullWidth
            label="postal code"
            onChange={(event) => {
              setBillingPostalCode(event.target.value);
            }}
            autoComplete="postal-code"
          />
          <FormControl className={classes.formControl}>
            <InputLabel>Country</InputLabel>
            <Select
              value={billingCountry}
              onChange={(event) => setBillingCountry(event.target.value)}
            >
              <MenuItem value="AU">Australia</MenuItem>
              <MenuItem value="AT">Austria</MenuItem>
              <MenuItem value="BE">Belgium</MenuItem>
              <MenuItem value="BR">Brazil</MenuItem>
              <MenuItem value="CA">Canada</MenuItem>
              <MenuItem value="CN">China</MenuItem>
              <MenuItem value="DK">Denmark</MenuItem>
              <MenuItem value="FI">Finland</MenuItem>
              <MenuItem value="FR">France</MenuItem>
              <MenuItem value="DE">Germany</MenuItem>
              <MenuItem value="HK">Hong Kong</MenuItem>
              <MenuItem value="IE">Ireland</MenuItem>
              <MenuItem value="IT">Italy</MenuItem>
              <MenuItem value="JP">Japan</MenuItem>
              <MenuItem value="LU">Luxembourg</MenuItem>
              <MenuItem value="MY">Malaysia</MenuItem>
              <MenuItem value="MX">Mexico</MenuItem>
              <MenuItem value="NL">Netherlands</MenuItem>
              <MenuItem value="NZ">New Zealand</MenuItem>
              <MenuItem value="NO">Norway</MenuItem>
              <MenuItem value="PL">Poland</MenuItem>
              <MenuItem value="PT">Portugal</MenuItem>
              <MenuItem value="SG">Singapore</MenuItem>
              <MenuItem value="ES">Spain</MenuItem>
              <MenuItem value="SE">Sweden</MenuItem>
              <MenuItem value="CH">Switzerland</MenuItem>
              <MenuItem value="GB">United Kingdom</MenuItem>
              <MenuItem value="US">United States</MenuItem>
            </Select>
          </FormControl>
        </Card>
        <Typography variant="subtitle1" className={classes.checkoutItemName}>
          <input
            name="isGoing"
            type="checkbox"
            checked={isShippingSameAsBilling}
            onChange={(e) => setIsShippingSameAsBilling(e.target.checked)}
          />
          Ship to this address
        </Typography>
        {isShippingSameAsBilling ? null : (
          <Card className={classes.addressCard}>
            <Typography variant="h6">Shipping Details</Typography>
            <TextField
              variant="outlined"
              size="small"
              margin="dense"
              required
              fullWidth
              label="name"
              onChange={(event) => {
                setShippingName(event.target.value);
              }}
              autoComplete="name"
            />
            <TextField
              variant="outlined"
              size="small"
              margin="dense"
              required
              fullWidth
              label="phone"
              onChange={(event) => {
                setShippingPhone(event.target.value);
              }}
              autoComplete="phone"
            />
            <TextField
              variant="outlined"
              size="small"
              margin="dense"
              required
              fullWidth
              label="address line 1"
              onChange={(event) => {
                setShippingAddressLine1(event.target.value);
              }}
              autoComplete="address-line1"
            />
            <TextField
              variant="outlined"
              size="small"
              margin="dense"
              fullWidth
              label="address line 2"
              onChange={(event) => {
                setShippingAddressLine2(event.target.value);
              }}
              autoComplete="address-line2"
            />
            <TextField
              variant="outlined"
              size="small"
              margin="dense"
              required
              fullWidth
              label="city"
              onChange={(event) => {
                setShippingCity(event.target.value);
              }}
              autoComplete="address-level2"
            />
            <TextField
              variant="outlined"
              size="small"
              margin="dense"
              required
              fullWidth
              label="state"
              onChange={(event) => {
                setShippingState(event.target.value);
              }}
              autoComplete="address-level1"
            />
            <TextField
              variant="outlined"
              size="small"
              margin="dense"
              required
              fullWidth
              label="postal code"
              onChange={(event) => {
                setShippingPostalCode(event.target.value);
              }}
              autoComplete="postal-code"
            />
            <FormControl flex>
              <InputLabel>Country</InputLabel>
              <Select
                value={shippingCountry}
                onChange={(event) => setShippingCountry(event.target.value)}
              >
                <MenuItem value="AU">Australia</MenuItem>
                <MenuItem value="AT">Austria</MenuItem>
                <MenuItem value="BE">Belgium</MenuItem>
                <MenuItem value="BR">Brazil</MenuItem>
                <MenuItem value="CA">Canada</MenuItem>
                <MenuItem value="CN">China</MenuItem>
                <MenuItem value="DK">Denmark</MenuItem>
                <MenuItem value="FI">Finland</MenuItem>
                <MenuItem value="FR">France</MenuItem>
                <MenuItem value="DE">Germany</MenuItem>
                <MenuItem value="HK">Hong Kong</MenuItem>
                <MenuItem value="IE">Ireland</MenuItem>
                <MenuItem value="IT">Italy</MenuItem>
                <MenuItem value="JP">Japan</MenuItem>
                <MenuItem value="LU">Luxembourg</MenuItem>
                <MenuItem value="MY">Malaysia</MenuItem>
                <MenuItem value="MX">Mexico</MenuItem>
                <MenuItem value="NL">Netherlands</MenuItem>
                <MenuItem value="NZ">New Zealand</MenuItem>
                <MenuItem value="NO">Norway</MenuItem>
                <MenuItem value="PL">Poland</MenuItem>
                <MenuItem value="PT">Portugal</MenuItem>
                <MenuItem value="SG">Singapore</MenuItem>
                <MenuItem value="ES">Spain</MenuItem>
                <MenuItem value="SE">Sweden</MenuItem>
                <MenuItem value="CH">Switzerland</MenuItem>
                <MenuItem value="GB">United Kingdom</MenuItem>
                <MenuItem value="US">United States</MenuItem>
              </Select>
            </FormControl>
          </Card>
        )}
      </div>
      <Card>
        <CardSection />
        {paymentErrorMessage ? (
          <Typography color="error">{paymentErrorMessage}</Typography>
        ) : null}
        <Button
          type="submit"
          fullWidth
          disabled={!stripe}
          variant="contained"
          color="primary"
          className={classes.submit}
        >
          Confirm Order
        </Button>
      </Card>
    </form>
  );
};

export default CheckoutForm;

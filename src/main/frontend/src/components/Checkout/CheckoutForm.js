import React, { useState } from "react";
import useStyles from "../../styles";
import { useStripe, useElements, CardElement } from "@stripe/react-stripe-js";
import { useHistory } from "react-router-dom";
import { useCart } from "../../context/CartContext";

import CardSection from "./CardSection";
import {
  Card,
  Typography,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
  Link,
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

  const [submitting, setSubmitting] = useState(false);

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
      setPaymentErrorMessage(result.error.message);
      setSubmitting(false); // only re-activate button here. No need to do so when success
    } else {
      // The payment has been processed!
      if (result.paymentIntent.status === "succeeded") {
        // Show a success message to your customer
        // There's a risk of the customer closing the window before callback
        // execution. Set up a webhook or plugin to listen for the
        // payment_intent.succeeded event that handles any business critical
        // post-payment actions.
        clearItems();
        history.push("/checkout/success");
      }
    }
  };

  const handleSubmit = async (event) => {
    // We don't want to let default form submission happen here,
    // which would refresh the page.
    event.preventDefault();

    setSubmitting(true);

    setPaymentErrorMessage("");
    if (!stripe || !elements) {
      // Stripe.js has not yet loaded.
      // Make sure to disable form submission until Stripe.js has loaded.
      setSubmitting(false);
      return;
    }

    confirmPayment();
  };

  const autoPopulateBilling = () => {
    setBillingName("John Doe");
    setBillingEmail("jdoe@snackoverflow.bernardguiang.com");
    setBillingPhone("1234567890");
    setBillingAddressLine1("Line 1");
    setBillingAddressLine2("Line 2");
    setBillingCity("City");
    setBillingState("State");
    setBillingPostalCode("12345");
    setBillingCountry("US");
  };

  const autoPopulateShipping = () => {
    setShippingName("Jane Doe");
    setShippingPhone("1234567890");
    setShippingAddressLine1("Line 1");
    setShippingAddressLine2("Line 2");
    setShippingCity("City");
    setShippingState("State");
    setShippingPostalCode("12345");
    setShippingCountry("US");
  };

  return (
    <form onSubmit={handleSubmit} className={classes.checkoutForm}>
      <div>
        <Card className={classes.addressCard}>
          <Typography variant="h6">
            Billing Details{" "}
            <Link onClick={() => autoPopulateBilling()}>auto-populate</Link>
          </Typography>
          <TextField
            variant="outlined"
            size="small"
            margin="dense"
            required
            fullWidth
            label="name"
            value={billingName}
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
            value={billingEmail}
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
            value={billingPhone}
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
            value={billingAddressLine1}
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
            value={billingAddressLine2}
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
            value={billingCity}
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
            value={billingState}
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
            value={billingPostalCode}
            onChange={(event) => {
              setBillingPostalCode(event.target.value);
            }}
            autoComplete="postal-code"
          />
          <FormControl fullWidth>
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
            <Typography variant="h6">
              Shipping Details{" "}
              <Link onClick={() => autoPopulateShipping()}>auto-populate</Link>
            </Typography>
            <TextField
              variant="outlined"
              size="small"
              margin="dense"
              required
              fullWidth
              label="name"
              value={shippingName}
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
              value={shippingPhone}
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
              value={shippingAddressLine1}
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
              value={shippingAddressLine2}
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
              value={shippingCity}
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
              value={shippingState}
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
              value={shippingPostalCode}
              onChange={(event) => {
                setShippingPostalCode(event.target.value);
              }}
              autoComplete="postal-code"
            />
            <FormControl fullWidth>
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
      <Card className={classes.addressCard}>
        <CardSection />
        {paymentErrorMessage ? (
          <Typography color="error">{paymentErrorMessage}</Typography>
        ) : null}
        <Button
          type="submit"
          fullWidth
          disabled={!stripe && !submitting}
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

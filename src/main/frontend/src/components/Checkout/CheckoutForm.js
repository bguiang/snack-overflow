import React, { useState, useReducer } from "react";
import useStyles from "../../styles";
import { useStripe, useElements, CardElement } from "@stripe/react-stripe-js";
import { useHistory } from "react-router-dom";
import { useCart } from "../../context/CartContext";

import CardSection from "./CardSection";
import SnackOverflow from "../../api/SnackOverflow";

const CheckoutForm = ({ clientSecret, cartInfo }) => {
  const { clearItems } = useCart();
  const history = useHistory();
  const classes = useStyles();
  const stripe = useStripe();
  const elements = useElements();
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

  const handleSubmit = async (event) => {
    // We don't want to let default form submission happen here,
    // which would refresh the page.
    event.preventDefault();

    if (!stripe || !elements) {
      // Stripe.js has not yet loaded.
      // Make sure to disable form submission until Stripe.js has loaded.
      return;
    }

    try {
      const orderRequest = {
        
        billingDetails: {
          address: {
            line1: billingAddressLine1,
            line2: billingAddressLine1,
            city: billingCity,
            state: billingState,
            postal_code: billingPostalCode,
            country: billingCountry,
          },
          name: billingName,
          phone: billingPhone,
          email: billingEmail,
        },
        shippingDetails: {
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
        },
      }
      const order = await SnackOverflow.post("/checkout", orderRequest);

    }catch(error) {
      console.log(error);
    }

    const result = await stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: elements.getElement(CardElement),
        billing_details: {
          address: {
            line1: billingAddressLine1,
            line2: billingAddressLine1,
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
      shipping: {
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
      },
    });

    if (result.error) {
      // Show error to your customer (e.g., insufficient funds)
      console.log(result.error.message);
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

  return (
    <form onSubmit={handleSubmit} className={classes.checkoutForm}>
      <div>
        <h3>Shipping &amp; Billing Information</h3>
        <section>
          <h4>Billing</h4>
          <fieldset>
            <div>
              <label>
                <span>Name</span>
                <input
                  name="name"
                  className="field"
                  placeholder="Jenny Rosen"
                  required
                  value={billingName}
                  onChange={(e) => setBillingName(e.target.value)}
                />
              </label>
            </div>
            <div>
              <label>
                <span>Email</span>
                <input
                  name="email"
                  type="email"
                  className="field"
                  placeholder="jenny@example.com"
                  required
                  value={billingEmail}
                  onChange={(e) => setBillingEmail(e.target.value)}
                />
              </label>
            </div>
            <div>
              <label>
                <span>Phone</span>
                <input
                  name="phone"
                  type="phone"
                  className="field"
                  value={billingPhone}
                  onChange={(e) => setBillingPhone(e.target.value)}
                />
              </label>
            </div>
            <div>
              <label>
                <span>Address Line 1</span>
                <input
                  name="address"
                  className="field"
                  placeholder="185 Berry Street Suite 550"
                  required
                  value={billingAddressLine1}
                  onChange={(e) => setBillingAddressLine1(e.target.value)}
                />
              </label>
            </div>
            <div>
              <label>
                <span>Address Line 2</span>
                <input
                  name="address"
                  className="field"
                  value={billingAddressLine2}
                  onChange={(e) => setBillingAddressLine2(e.target.value)}
                />
              </label>
            </div>
            <div>
              <label>
                <span>City</span>
                <input
                  name="city"
                  className="field"
                  placeholder="San Francisco"
                  required
                  value={billingCity}
                  onChange={(e) => setBillingCity(e.target.value)}
                />
              </label>
            </div>
            <div>
              <label>
                <span>State</span>
                <input
                  name="state"
                  className="field"
                  placeholder="CA"
                  required
                  value={billingState}
                  onChange={(e) => setBillingState(e.target.value)}
                />
              </label>
            </div>
            <div>
              <label>
                <span>ZIP</span>
                <input
                  name="postal_code"
                  className="field"
                  placeholder="94107"
                  required
                  value={billingPostalCode}
                  onChange={(e) => setBillingPostalCode(e.target.value)}
                />
              </label>
            </div>
            <div>
              <label>
                <span>Country</span>
                <div id="country" className="field US">
                  <select
                    name="country"
                    value={billingCountry}
                    onChange={(e) => setBillingCountry(e.target.value)}
                  >
                    <option value="AU">Australia</option>
                    <option value="AT">Austria</option>
                    <option value="BE">Belgium</option>
                    <option value="BR">Brazil</option>
                    <option value="CA">Canada</option>
                    <option value="CN">China</option>
                    <option value="DK">Denmark</option>
                    <option value="FI">Finland</option>
                    <option value="FR">France</option>
                    <option value="DE">Germany</option>
                    <option value="HK">Hong Kong</option>
                    <option value="IE">Ireland</option>
                    <option value="IT">Italy</option>
                    <option value="JP">Japan</option>
                    <option value="LU">Luxembourg</option>
                    <option value="MY">Malaysia</option>
                    <option value="MX">Mexico</option>
                    <option value="NL">Netherlands</option>
                    <option value="NZ">New Zealand</option>
                    <option value="NO">Norway</option>
                    <option value="PL">Poland</option>
                    <option value="PT">Portugal</option>
                    <option value="SG">Singapore</option>
                    <option value="ES">Spain</option>
                    <option value="SE">Sweden</option>
                    <option value="CH">Switzerland</option>
                    <option value="GB">United Kingdom</option>
                    <option value="US">United States</option>
                  </select>
                </div>
              </label>
            </div>
          </fieldset>
        </section>
        <label>
          Use Billing as Shipping Address:
          <input
            name="isGoing"
            type="checkbox"
            checked={isShippingSameAsBilling}
            onChange={(e) => setIsShippingSameAsBilling(e.target.checked)}
          />
        </label>
        {isShippingSameAsBilling ? null : (
          <section>
            <h4>Shipping</h4>
            <fieldset>
              <div>
                <label>
                  <span>Name</span>
                  <input
                    name="name"
                    className="field"
                    placeholder="Jenny Rosen"
                    required
                    value={shippingName}
                    onChange={(e) => setShippingName(e.target.value)}
                  />
                </label>
              </div>
              <div>
                <label>
                  <span>Address Line 1</span>
                  <input
                    name="address"
                    className="field"
                    placeholder="185 Berry Street Suite 550"
                    required
                    value={shippingAddressLine1}
                    onChange={(e) => setShippingAddressLine1(e.target.value)}
                  />
                </label>
              </div>
              <div>
                <label>
                  <span>Address Line 2</span>
                  <input
                    name="address"
                    className="field"
                    value={shippingAddressLine2}
                    onChange={(e) => setShippingAddressLine2(e.target.value)}
                  />
                </label>
              </div>
              <div>
                <label>
                  <span>City</span>
                  <input
                    name="city"
                    className="field"
                    placeholder="San Francisco"
                    required
                    value={shippingCity}
                    onChange={(e) => setShippingCity(e.target.value)}
                  />
                </label>
              </div>
              <div>
                <label>
                  <span>State</span>
                  <input
                    name="state"
                    className="field"
                    placeholder="CA"
                    required
                    value={shippingState}
                    onChange={(e) => setShippingState(e.target.value)}
                  />
                </label>
              </div>
              <div>
                <label>
                  <span>ZIP</span>
                  <input
                    name="postal_code"
                    className="field"
                    placeholder="94107"
                    required
                    value={shippingPostalCode}
                    onChange={(e) => setShippingPostalCode(e.target.value)}
                  />
                </label>
              </div>
              <div>
                <label>
                  <span>Country</span>
                  <div id="country" className="field US">
                    <select
                      name="country"
                      value={shippingCountry}
                      onChange={(e) => setShippingCountry(e.target.value)}
                    >
                      <option value="AU">Australia</option>
                      <option value="AT">Austria</option>
                      <option value="BE">Belgium</option>
                      <option value="BR">Brazil</option>
                      <option value="CA">Canada</option>
                      <option value="CN">China</option>
                      <option value="DK">Denmark</option>
                      <option value="FI">Finland</option>
                      <option value="FR">France</option>
                      <option value="DE">Germany</option>
                      <option value="HK">Hong Kong</option>
                      <option value="IE">Ireland</option>
                      <option value="IT">Italy</option>
                      <option value="JP">Japan</option>
                      <option value="LU">Luxembourg</option>
                      <option value="MY">Malaysia</option>
                      <option value="MX">Mexico</option>
                      <option value="NL">Netherlands</option>
                      <option value="NZ">New Zealand</option>
                      <option value="NO">Norway</option>
                      <option value="PL">Poland</option>
                      <option value="PT">Portugal</option>
                      <option value="SG">Singapore</option>
                      <option value="ES">Spain</option>
                      <option value="SE">Sweden</option>
                      <option value="CH">Switzerland</option>
                      <option value="GB">United Kingdom</option>
                      <option value="US">United States</option>
                    </select>
                  </div>
                </label>
              </div>
            </fieldset>
          </section>
        )}
      </div>
      <CardSection />
      <button disabled={!stripe}>Confirm order</button>
    </form>
  );
};

export default CheckoutForm;

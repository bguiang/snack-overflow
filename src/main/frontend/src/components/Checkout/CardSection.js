/**
 * Use the CSS tab above to style your Element's container.
 */
import React from "react";
import { CardElement } from "@stripe/react-stripe-js";
import "./CardSectionStyles.css";
import { Typography, Link } from "@material-ui/core";

const CARD_ELEMENT_OPTIONS = {
  style: {
    base: {
      color: "#32325d",
      fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
      fontSmoothing: "antialiased",
      fontSize: "16px",
      "::placeholder": {
        color: "#aab7c4",
      },
    },
    invalid: {
      color: "#fa755a",
      iconColor: "#fa755a",
    },
  },
};

function CardSection() {
  return (
    <label>
      <Typography variant="h6">Payment Information</Typography>
      <Typography variant="subtitle2">
        Please use the following Stripe test card number with any valid
        expiration date and CVC. You may also use any other{" "}
        <Link href={"https://stripe.com/docs/testing"} target="_blank">
          Stripe Test card
        </Link>
      </Typography>
      <CardElement options={CARD_ELEMENT_OPTIONS} />
      <Typography variant="subtitle1">
        Test Card: 4242 4242 4242 4242
      </Typography>
    </label>
  );
}

export default CardSection;

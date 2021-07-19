import React from "react";
import { useCart } from "../../context/CartContext";
import { Route, Redirect } from "react-router-dom";

function CheckoutRoute({ children, ...rest }) {
  let { cart } = useCart();
  return (
    <Route
      {...rest}
      render={({ location }) =>
        cart.length > 0 ? (
          children
        ) : (
          <Redirect
            to={{
              pathname: "/",
              state: { from: location },
            }}
          />
        )
      }
    />
  );
}

export default CheckoutRoute;

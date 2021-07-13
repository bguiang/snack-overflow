import React, { useState, useEffect } from "react";
import useStyles from "../styles";
import {
  Grid,
  Typography,
  Card,
  CardActionArea,
  CardActions,
  CardContent,
  CardMedia,
  Button,
  TextField,
  IconButton,
} from "@material-ui/core";
import { useCart } from "../context/CartContext";
import { useHistory } from "react-router-dom";
import DeleteIcon from "@material-ui/icons/Delete";
import CartItem from "./CartItem";

const Cart = () => {
  const classes = useStyles();
  const history = useHistory();
  const { cart, getCartInfo } = useCart();

  const [cartInfo, setCartInfo] = useState({ items: [], total: 0 });

  // we need to use the cart as a dependency because the cart goes away on page refresh and has to loaded back in by a useEffect inside CartContext
  // We only want to call getCartInfo when the cart is loaded back in
  useEffect(() => {
    const fetchCartInfo = async () => {
      let info = await getCartInfo();
      setCartInfo(info);
      console.log(info);
    };
    fetchCartInfo();
  }, [cart]);

  const handleClick = (url) => {
    history.push(url);
  };
  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} md={10} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Shopping Cart</h2>
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleClick("/checkout")}
          >
            Checkout
          </Button>
        </Grid>
        {cartInfo.items.map((cartItem) => (
          <CartItem cartItem={cartItem} />
        ))}
      </Grid>
    </div>
  );
};

export default Cart;

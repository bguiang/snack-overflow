import React, { useState, useEffect } from "react";
import useStyles from "../../styles";
import {
  Grid,
  Button,
  Dialog,
  DialogTitle,
  DialogActions,
  DialogContent,
  DialogContentText,
  Slide,
} from "@material-ui/core";
import { useCart } from "../../context/CartContext";
import { useHistory } from "react-router-dom";
import CartItem from "./CartItem";
import SnackOverflow from "../../api/SnackOverflow";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

const Cart = () => {
  const classes = useStyles();
  const history = useHistory();
  const [isTotalTooLow, setIsTotalTooLow] = useState(false);
  const { cart } = useCart();
  const [cartInfo, setCartInfo] = useState({ items: [], total: 0 });

  useEffect(() => {
    const loadCartInfo = async () => {
      try {
        const response = await SnackOverflow.post("/cart/info", cart);
        if (200 === response.status) {
          setCartInfo(response.data);
        }
      } catch (error) {
        console.log(error);
      }
    };

    if (cart.length > 0) loadCartInfo();
  }, [cart]);

  const checkoutClick = () => {
    if (cartInfo.total <= 0.5) {
      setIsTotalTooLow(true);
    } else {
      history.push("/checkout");
    }
  };

  return (
    <div>
      <Dialog
        open={isTotalTooLow}
        onClose={() => setIsTotalTooLow(false)}
        TransitionComponent={Transition}
        keepMounted
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <DialogTitle id="alert-dialog-slide-title">
          {"Your cart's a little light"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-slide-description">
            All purchases must be greater than $0.50
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setIsTotalTooLow(false)} color="primary">
            Ok
          </Button>
        </DialogActions>
      </Dialog>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Shopping Cart</h2>
          <Button
            variant="contained"
            color="primary"
            onClick={() => checkoutClick()}
          >
            Checkout
          </Button>
        </Grid>
        {cartInfo.items.map((cartItem) => (
          <CartItem cartItem={cartItem} key={cartItem.product.id} />
        ))}
      </Grid>
    </div>
  );
};

export default Cart;

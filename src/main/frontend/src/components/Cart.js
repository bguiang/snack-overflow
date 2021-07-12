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

  const itemClick = (snack) => {
    history.push(`/snacks/${snack.id}`);
  };

  const updateClick = () => {};

  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={10} key="title">
          <h2>Shopping Cart</h2>
          <Button>Checkout</Button>
        </Grid>
        {cartInfo.items.map((cartItem) => (
          <Grid item xs={10} key={cartItem.product.id}>
            <Card className={classes.cartItemCard}>
              <CardActionArea
                className={classes.cartItemCardActionArea}
                onClick={() => itemClick(`/snack/${cartItem.product.id}`)}
              >
                <CardMedia
                  className={classes.cartItemCardImage}
                  image={
                    cartItem.product.images[0]
                      ? cartItem.product.images[0]
                      : null
                  }
                  title={cartItem.product.name}
                />
                <Typography
                  variant="h5"
                  component="h5"
                  className={classes.cartItemName}
                >
                  {cartItem.product.name}
                </Typography>
                <Typography variant="h6" component="h6">
                  ${cartItem.product.price.toFixed(2)}
                </Typography>
              </CardActionArea>
              <CardActions className={classes.cartItemCardActions}>
                <TextField
                  className={classes.cartItemQuantity}
                  label="Quantity"
                  variant="outlined"
                  size="large"
                  type="number"
                  min={1}
                  value={1}
                  // value={quantity}
                  // onChange={(event) => {
                  //   let val = parseInt(event.target.value);
                  //   if (isNaN(val)) val = 1;
                  //   if (val < 1) val = 1;
                  //   setQuantity(val);
                  // }}
                />
                <Button
                  size="small"
                  color="primary"
                  onClick={() => updateClick(cartItem)}
                >
                  Update
                </Button>
                <IconButton>
                  <DeleteIcon />
                </IconButton>
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
    </div>
  );
};

export default Cart;

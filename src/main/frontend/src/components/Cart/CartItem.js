import React, { useState } from "react";
import useStyles from "../../styles";
import {
  Grid,
  Typography,
  Card,
  CardActionArea,
  CardActions,
  CardMedia,
  Button,
  TextField,
  IconButton,
} from "@material-ui/core";
import { useCart } from "../../context/CartContext";
import { useHistory } from "react-router-dom";
import DeleteIcon from "@material-ui/icons/Delete";

const CartItem = ({ cartItem }) => {
  const classes = useStyles();
  const history = useHistory();
  const [quantity, setQuantity] = useState(cartItem.quantity);
  const { removeItem, updateItemQuantity } = useCart();

  const itemClick = (id) => {
    history.push(`/snacks/${id}`);
  };

  const updateClick = () => {
    updateItemQuantity({ productId: cartItem.product.id, quantity });
  };

  const deleteClick = () => {
    removeItem({ productId: cartItem.product.id });
  };

  return (
    <Grid item xs={12} md={10} key={cartItem.product.id}>
      <Card className={classes.cartItemCard}>
        <CardActionArea
          className={classes.cartItemCardActionArea}
          onClick={() => itemClick(cartItem.product.id)}
        >
          <CardMedia
            className={classes.cartItemCardImage}
            image={
              cartItem.product.images[0] ? cartItem.product.images[0] : null
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
          <Typography
            variant="h6"
            component="h6"
            className={classes.cartItemPrice}
          >
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
            value={quantity}
            onChange={(event) => {
              let val = parseInt(event.target.value);
              if (isNaN(val)) val = 1;
              if (val < 1) val = 1;
              setQuantity(val);
            }}
          />
          <Button size="small" color="primary" onClick={() => updateClick()}>
            Update
          </Button>
          <IconButton>
            <DeleteIcon onClick={() => deleteClick()} />
          </IconButton>
        </CardActions>
      </Card>
    </Grid>
  );
};

export default CartItem;

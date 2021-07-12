import React, { useEffect } from "react";
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
} from "@material-ui/core";
import { useCart } from "../context/CartContext";
import { useHistory } from "react-router-dom";

const Cart = () => {
  const classes = useStyles();
  const { getCartInfo } = useCart();

  console.log(JSON.stringify(getCartInfo()));

  const history = useHistory();

  const itemClick = (snack) => {
    history.push(`/snacks/${snack.id}`);
  };

  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        {/* {cart.map((cartItem) => (
          <Grid
            item
            className={classes.snackCardContainer}
            xs={12}
            sm={5}
            md={4}
            key={snack.id}
          >
            <Card className={classes.snackCard}>
              <CardActionArea
                onClick={() => itemClick(`/snack/${cartItem.productId}`)}
              >
                <CardMedia
                  className={classes.snackCardImage}
                  image={snack.images[0] ? snack.images[0] : null}
                  title={snack.name}
                />
                <CardContent className={classes.snackCardContent}>
                  <Typography gutterBottom variant="h5" component="h5">
                    {snack.name}
                  </Typography>
                  <Typography variant="h6" component="h6">
                    ${snack.price.toFixed(2)}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="textSecondary"
                    component="p"
                    className={classes.snackCardDescription}
                  >
                    {snack.description}
                  </Typography>
                </CardContent>
              </CardActionArea>
              <CardActions className={classes.snackCardActions}>
                <TextField
                  className={classes.snackCardQuantity}
                  label="Quantity"
                  variant="outlined"
                  size="small"
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
                <Button
                  size="small"
                  color="primary"
                  onClick={() => addToCartClick(snack)}
                >
                  Add To Cart
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))} */}
      </Grid>
    </div>
  );
};

export default Cart;

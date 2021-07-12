import React, {useState, useEffect } from "react";
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

  const [cartInfo, setCartInfo] = useState({items:[]});

  const history = useHistory();

  const itemClick = (snack) => {
    history.push(`/snacks/${snack.id}`);
  };

  useEffect(() => {
    const fetchCartInfo = async() => {
      let info = await getCartInfo(); 
      setCartInfo(info);
      console.log(info)
    }

    fetchCartInfo()
  }, [])

  console.log("RENDER")
  return (
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        {cartInfo.items.map((cartItem) => (
          <Grid
            item
            className={classes.snackCardContainer}
            xs={12}
            sm={5}
            md={4}
            key={cartItem.product.id}
          >
            <Card className={classes.snackCard}>
              <CardActionArea
                onClick={() => itemClick(`/snack/${cartItem.productId}`)}
              >
                <CardMedia
                  className={classes.snackCardImage}
                  image={cartItem.product.images[0] ? cartItem.product.images[0] : null}
                  title={cartItem.product.name}
                />
                <CardContent className={classes.snackCardContent}>
                  <Typography gutterBottom variant="h5" component="h5">
                    {cartItem.product.name}
                  </Typography>
                  <Typography variant="h6" component="h6">
                    ${cartItem.product.price.toFixed(2)}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="textSecondary"
                    component="p"
                    className={classes.snackCardDescription}
                  >
                    {cartItem.product.description}
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>
    </div>
  );
};

export default Cart;

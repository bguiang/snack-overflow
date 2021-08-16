import React from "react";

import { useState } from "react";
import { useParams } from "react-router-dom";
import useSnack from "../../hooks/useSnack";
import useStyles from "../../styles";
import {
  Grid,
  Typography,
  TextField,
  CardActions,
  CardMedia,
  Button,
  Paper,
} from "@material-ui/core";
import { useCart } from "../../context/CartContext";
import Carousel from "react-material-ui-carousel";
import CarouselItem from "../CarouselItem";

const Snack = () => {
  const classes = useStyles();
  let { id } = useParams();
  const [snack] = useSnack(id);
  const { addItem } = useCart();
  const [quantity, setQuantity] = useState(1);

  const addToCartClick = (snack) => {
    if (!isNaN(quantity) && quantity > 0)
      addItem({ quantity, productId: snack.id, productName: snack.name });
  };

  return (
    <div className={classes.content}>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} sm={10}>
          {snack.images ? (
            <Carousel
              navButtonsAlwaysVisible={true}
              next={() => {
                /* Do stuff */
              }}
              prev={() => {
                /* Do other stuff */
              }}
            >
              {snack.images.map((image, i) => (
                <CarouselItem key={i} name={snack.name} image={image} />
              ))}
            </Carousel>
          ) : null}
        </Grid>
        <Grid item xs={12} sm={10}>
          <Typography gutterBottom variant="h5" component="h5">
            {snack.name}
          </Typography>
          <Typography variant="h6" component="h6">
            ${snack.price ? snack.price.toFixed(2) : "0.00"}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
            {snack.description}
          </Typography>
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
        </Grid>
      </Grid>
    </div>
  );
};
export default Snack;

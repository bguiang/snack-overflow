import React from "react";

import { makeStyles } from "@material-ui/core/styles";
import { useState, useEffect, useReducer } from "react";
import SnackOverflow from "../api/SnackOverflow";
import { useParams } from "react-router-dom";
import useSnack from "../hooks/useSnack";
import useStyles from "../styles";
import {
  Grid,
  Typography,
  TextField,
  CardActions,
  CardContent,
  CardMedia,
  Button,
} from "@material-ui/core";
import { useCart } from "../context/CartContext";

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
    <div>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} sm={5}>
          <CardMedia
            className={classes.snackPageImage}
            image={snack.images ? snack.images[0] : null}
            title={snack.name}
          />
        </Grid>
        <Grid item xs={12} sm={5}>
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

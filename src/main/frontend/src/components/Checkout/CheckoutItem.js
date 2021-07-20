import React, { useState } from "react";
import useStyles from "../../styles";
import {
  Grid,
  Typography,
  Card,
  CardActions,
  CardContent,
  TextField,
} from "@material-ui/core";

const CheckoutItem = ({ checkoutItem }) => {
  const classes = useStyles();

  return (
    <div className={classes.checkoutItem} key={checkoutItem.product.id}>
      <Typography variant="subtitle1" className={classes.checkoutItemName}>
        {checkoutItem.product.name}
      </Typography>
      <Typography variant="subtitle1" className={classes.checkoutItemPrice}>
        ${checkoutItem.product.price.toFixed(2)}
      </Typography>
      <Typography variant="subtitle1" className={classes.checkoutItemPrice}>
        x{checkoutItem.quantity}
      </Typography>
    </div>
  );
};

export default CheckoutItem;

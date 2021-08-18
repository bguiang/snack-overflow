import React, { useState } from "react";
import useStyles from "../../styles";
import {
  Grid,
  Typography,
  Card,
  CardActionArea,
  CardActions,
  CardContent,
  CardMedia,
  Button,
  IconButton,
} from "@material-ui/core";

import { useHistory } from "react-router-dom";
import EditIcon from "@material-ui/icons/Edit";

const TopProductCard = ({ product }) => {
  const classes = useStyles();
  const history = useHistory();

  const productClick = (product) => {
    history.push(`/admin/products/${product.id}`);
  };

  return (
    <Grid item xs={12} key={product.id}>
      <div
        className={classes.verticalCard}
        onClick={() => productClick(product)}
      >
        <div>
          <CardMedia
            className={classes.productCardVerticalImage}
            image={product.images[0] ? product.images[0] : null}
            title={product.name}
          />
          <CardContent className={classes.snackCardContent}>
            <Typography gutterBottom variant="subtitle1">
              ID: #{product.id}
            </Typography>
            <Typography gutterBottom variant="subtitle1">
              Name: {product.name}
            </Typography>
            <Typography variant="subtitle1">
              Price: ${product.price.toFixed(2)}
            </Typography>
            <Typography variant="subtitle1">
              Units Sold This Month: {product.unitsSold}
            </Typography>
          </CardContent>
        </div>
      </div>

      <div
        className={classes.productCardHorizontal}
        onClick={() => productClick(product)}
      >
        <div className={classes.cartItemCardActionArea}>
          <Typography
            variant="subtitle2"
            className={classes.productCardHorizontalID}
          >
            #{product.id}
          </Typography>
          <div className={classes.productCardHorizontalMain}>
            <CardMedia
              className={classes.productCardHorizontalImage}
              image={product.images[0] ? product.images[0] : null}
              title={product.name}
            />
            <Typography
              variant="subtitle2"
              className={classes.productCardHorizontalName}
            >
              {product.name}
            </Typography>
          </div>
          <Typography
            variant="subtitle2"
            className={classes.productCardHorizontalPrice}
          >
            ${product.price.toFixed(2)}
          </Typography>
          <Typography
            variant="subtitle2"
            className={classes.productCardHorizontalUnitsSold}
          >
            {product.unitsSold}
          </Typography>
        </div>
      </div>
    </Grid>
  );
};

export default TopProductCard;

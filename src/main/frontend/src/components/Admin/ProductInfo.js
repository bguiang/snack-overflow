import React from "react";

import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import useProduct from "../../hooks/useProduct";
import useStyles from "../../styles";
import { Grid, Typography, CardMedia } from "@material-ui/core";
import ProductPurchasedCard from "./ProductPurchasedCard";
import Carousel from "react-material-ui-carousel";
import CarouselItem from "../CarouselItem";

const ProductInfo = () => {
  const classes = useStyles();
  let { id } = useParams();
  const [product] = useProduct(id);
  const [orderedItems, setOrderedItems] = useState([]);

  useEffect(() => {
    if (product !== null) {
      console.log(product.orderedItems);
      setOrderedItems(product.orderedItems);
    }
  }, [product]);

  return (
    <div className={classes.content}>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} sm={12} title="image">
          {product.images ? (
            <Carousel
              navButtonsAlwaysVisible={true}
              next={() => {
                /* Do stuff */
              }}
              prev={() => {
                /* Do other stuff */
              }}
            >
              {product.images.map((image, i) => (
                <CarouselItem key={i} name={product.name} image={image} />
              ))}
            </Carousel>
          ) : null}
        </Grid>
        <Grid item xs={12} sm={12} title="description">
          <Typography gutterBottom variant="h5" component="h5">
            {product.name}
          </Typography>
          <Typography variant="h6" component="h6">
            ${product.price ? product.price.toFixed(2) : "0.00"}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
            {product.description}
          </Typography>
        </Grid>
        <Grid item xs={12} sm={12} title="orderedItems">
          <Typography gutterBottom variant="h5" component="h5">
            Sales
          </Typography>
          <Grid
            item
            xs={12}
            key={"orderListTitle"}
            className={classes.orderListTitle}
          >
            <div className={classes.orderCard}>
              <div className={classes.orderCardActionArea}>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem}
                >
                  Order ID
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem}
                >
                  Date
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem}
                >
                  Status
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem}
                >
                  Price
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.orderCardActionAreaItem}
                >
                  Quantity
                </Typography>
              </div>
            </div>
          </Grid>
          {orderedItems
            ? orderedItems.map((orderedItem) => (
                <ProductPurchasedCard orderedItem={orderedItem} />
              ))
            : null}
        </Grid>
      </Grid>
    </div>
  );
};

export default ProductInfo;

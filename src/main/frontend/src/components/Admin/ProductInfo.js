import React from "react";

import { useState, useEffect } from "react";
import { useHistory, useParams } from "react-router-dom";
import useProduct from "../../hooks/useProduct";
import useStyles from "../../styles";
import { Grid, Typography, Button } from "@material-ui/core";
import ProductPurchasedCard from "./ProductPurchasedCard";
import Carousel from "react-material-ui-carousel";
import CarouselItem from "../CarouselItem";
import EditIcon from "@material-ui/icons/Edit";

const ProductInfo = () => {
  const classes = useStyles();
  const history = useHistory();
  let { id } = useParams();
  const [product] = useProduct(id);
  const [orderedItems, setOrderedItems] = useState([]);

  useEffect(() => {
    if (product !== null) {
      setOrderedItems(product.orderedItems);
    }
  }, [product]);

  const editProductClick = (product) => {
    history.push(`/admin/products/edit/${product.id}`);
  };

  return (
    <div className={classes.content}>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        {product.deleted ? (
          <h2 className={classes.error}>This product is no longer available</h2>
        ) : null}
        <Grid item xs={12} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Product Information</h2>
          {product.deleted ? null : (
            <Button
              size="large"
              color="primary"
              onClick={() => editProductClick(product)}
              startIcon={<EditIcon />}
            >
              Edit
            </Button>
          )}
        </Grid>
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

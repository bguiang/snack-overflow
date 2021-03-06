import React from "react";
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
} from "@material-ui/core";

import { useHistory } from "react-router-dom";
import EditIcon from "@material-ui/icons/Edit";

const ProductCard = ({ product }) => {
  const classes = useStyles();
  const history = useHistory();

  const productClick = (product) => {
    history.push(`/admin/products/${product.id}`);
  };

  const editProductClick = (product) => {
    history.push(`/admin/products/edit/${product.id}`);
  };

  return (
    <Grid item xs={12} key={product.id}>
      <Card className={classes.verticalCard}>
        <CardActionArea onClick={() => productClick(product)}>
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
              Created Date:{" "}
              {new Date(product.createdDate).toLocaleDateString("en-US")}
            </Typography>
            <Typography variant="subtitle1">
              Price: ${product.price.toFixed(2)}
            </Typography>
            <Typography variant="subtitle1">
              Units Sold This Month: {product.unitsSold}
            </Typography>
          </CardContent>
        </CardActionArea>
        <CardActions className={classes.snackCardActions}>
          <Button
            size="large"
            color="primary"
            onClick={() => editProductClick(product)}
            startIcon={<EditIcon />}
          >
            Edit
          </Button>
        </CardActions>
      </Card>

      <Card className={classes.productCardHorizontal}>
        <CardActionArea onClick={() => productClick(product)}>
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
              className={classes.productCardHorizontalDate}
            >
              {new Date(product.createdDate).toLocaleDateString("en-US")}
            </Typography>
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
        </CardActionArea>
        <CardActions className={classes.cartItemCardActions}>
          <Button
            size="small"
            color="primary"
            onClick={() => editProductClick(product)}
            startIcon={<EditIcon />}
          >
            Edit
          </Button>
        </CardActions>
      </Card>
    </Grid>
  );
};

export default ProductCard;

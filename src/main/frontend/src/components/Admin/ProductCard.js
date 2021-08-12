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
} from "@material-ui/core";

import { useHistory } from "react-router-dom";

const ProductCard = ({ snack }) => {
  const classes = useStyles();
  const history = useHistory();

  const snackClick = (snack) => {
    history.push(`/admin/products/${snack.id}`);
  };

  const editProductClick = (snack) => {
    history.push(`/admin/products/edit/${snack.id}`);
  };

  return (
    <Grid
      item
      className={classes.snackCardContainer}
      xs={12}
      sm={12}
      md={6}
      lg={4}
      key={snack.id}
    >
      <Card className={classes.snackCard}>
        <CardActionArea onClick={() => snackClick(snack)}>
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
            <Typography variant="body2" color="textSecondary" component="p">
              {snack.description}
            </Typography>
          </CardContent>
        </CardActionArea>
        <CardActions className={classes.snackCardActions}>
          <Button
            size="small"
            color="primary"
            onClick={() => editProductClick(snack)}
          >
            Edit
          </Button>
        </CardActions>
      </Card>
    </Grid>
  );
};

export default ProductCard;

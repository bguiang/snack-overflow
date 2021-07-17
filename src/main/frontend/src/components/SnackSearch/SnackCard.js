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
  TextField,
} from "@material-ui/core";

import { useHistory } from "react-router-dom";
import { useCart } from "../../context/CartContext";

const SnackCard = ({ snack }) => {
  const classes = useStyles();
  const history = useHistory();
  const { addItem } = useCart();
  const [quantity, setQuantity] = useState(1);

  const snackClick = (snack) => {
    history.push(`/snacks/${snack.id}`);
  };

  const addToCartClick = (snack) => {
    if (!isNaN(quantity) && quantity > 0)
      addItem({ quantity, productId: snack.id, productName: snack.name });
  };

  return (
    <Grid
      item
      className={classes.snackCardContainer}
      xs={12}
      sm={5}
      md={4}
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
  );
};

export default SnackCard;

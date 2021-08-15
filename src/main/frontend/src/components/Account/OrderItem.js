import React from "react";
import useStyles from "../../styles";
import { Grid, Typography, Card, CardMedia } from "@material-ui/core";
import { useHistory } from "react-router-dom";

const OrderItem = ({ orderItem, orderItemClick }) => {
  const classes = useStyles();
  const history = useHistory();

  // const itemClick = (id) => {
  //   console.log("Item Clicked: " + id);
  //   history.push(`/snacks/${id}`);
  // };
  return (
    <div
      onClick={() => orderItemClick(orderItem.product.id)}
      className={classes.orderItem}
      key={orderItem.id}
    >
      <Card className={classes.orderItemImageContainer}>
        <CardMedia
          className={classes.orderItemImage}
          image={
            orderItem.product.images[0] ? orderItem.product.images[0] : null
          }
          title={orderItem.product.name}
        />
      </Card>
      <Typography variant="subtitle1" className={classes.orderItemName}>
        {orderItem.product.name}
      </Typography>
      <Typography variant="subtitle1" className={classes.orderItemPrice}>
        Price: ${orderItem.price.toFixed(2)}
      </Typography>
      <Typography variant="subtitle1" className={classes.orderItemQuantity}>
        Quantity: {orderItem.quantity}
      </Typography>
    </div>
  );
};

export default OrderItem;

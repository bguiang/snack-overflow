import React from "react";
import useStyles from "../../styles";
import { Grid, Typography, Card, CardActionArea } from "@material-ui/core";
import { useHistory } from "react-router-dom";

const ProductPurchasedCard = ({ orderedItem }) => {
  const classes = useStyles();
  const history = useHistory();

  const itemClick = (id) => {
    history.push(`/admin/orders/${id}`);
  };
  return (
    <Grid item xs={12} key={orderedItem.id}>
      <Card className={classes.orderCardMobile}>
        <CardActionArea
          onClick={() => itemClick(orderedItem.orderId)}
          className={classes.orderCardActionAreaMobile}
        >
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            Order ID: {orderedItem.orderId}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem2}
          >
            Date:{" "}
            {new Date(orderedItem.orderCreatedDate).toLocaleDateString("en-US")}{" "}
            {new Date(orderedItem.orderCreatedDate).toLocaleTimeString("en-US")}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            Price: ${orderedItem.price.toFixed(2)}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            Quantity: {orderedItem.quantity}
          </Typography>
        </CardActionArea>
      </Card>
      <Card className={classes.orderCard}>
        <CardActionArea
          onClick={() => itemClick(orderedItem.orderId)}
          className={classes.orderCardActionArea}
        >
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            {orderedItem.orderId}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem2}
          >
            {new Date(orderedItem.orderCreatedDate).toLocaleDateString("en-US")}{" "}
            {new Date(orderedItem.orderCreatedDate).toLocaleTimeString("en-US")}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            ${orderedItem.price.toFixed(2)}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            {orderedItem.quantity}
          </Typography>
        </CardActionArea>
      </Card>
    </Grid>
  );
};

export default ProductPurchasedCard;

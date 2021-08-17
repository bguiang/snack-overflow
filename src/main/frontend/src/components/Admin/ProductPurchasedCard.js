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
            Order Status: {orderedItem.orderStatus}
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
            className={classes.orderCardActionAreaItem}
          >
            {new Date(orderedItem.orderCreatedDate).toLocaleDateString("en-US")}
          </Typography>
          {orderedItem.orderStatus === "CANCELLED" ||
          orderedItem.orderStatus === "FAILED" ||
          orderedItem.orderStatus === "REFUNDED" ? (
            <div className={classes.orderCardActionAreaItem}>
              <Typography variant="subtitle1" className={classes.error}>
                {orderedItem.orderStatus}
              </Typography>
            </div>
          ) : (
            <div className={classes.orderCardActionAreaItem}>
              <Typography variant="subtitle1" className={classes.success}>
                {orderedItem.orderStatus}
              </Typography>
            </div>
          )}

          {orderedItem.orderStatus === "CANCELLED" ||
          orderedItem.orderStatus === "FAILED" ||
          orderedItem.orderStatus === "REFUNDED" ? (
            <div className={classes.orderCardActionAreaItem}>
              <Typography variant="subtitle1" className={classes.error}>
                ${orderedItem.price.toFixed(2)}
              </Typography>
            </div>
          ) : (
            <div className={classes.orderCardActionAreaItem}>
              <Typography variant="subtitle1" className={classes.success}>
                ${orderedItem.price.toFixed(2)}
              </Typography>
            </div>
          )}

          {orderedItem.orderStatus === "CANCELLED" ||
          orderedItem.orderStatus === "FAILED" ||
          orderedItem.orderStatus === "REFUNDED" ? (
            <div className={classes.orderCardActionAreaItem}>
              <Typography variant="subtitle1" className={classes.error}>
                {orderedItem.quantity}
              </Typography>
            </div>
          ) : (
            <div className={classes.orderCardActionAreaItem}>
              <Typography variant="subtitle1" className={classes.success}>
                {orderedItem.quantity}
              </Typography>
            </div>
          )}
        </CardActionArea>
      </Card>
    </Grid>
  );
};

export default ProductPurchasedCard;

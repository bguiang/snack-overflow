import React from "react";
import useStyles from "../../styles";
import { Grid, Typography, Card, CardActionArea } from "@material-ui/core";
import { useHistory } from "react-router-dom";

const OrderCard = ({ order }) => {
  const classes = useStyles();
  const history = useHistory();

  const itemClick = (id) => {
    history.push(`/account/orders/${id}`);
  };
  return (
    <Grid item xs={12} key={order.id}>
      <Card className={classes.orderCardMobile}>
        <CardActionArea
          onClick={() => itemClick(order.id)}
          className={classes.orderCardActionAreaMobile}
        >
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            Order: #{order.id}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem2}
          >
            Created: {new Date(order.createdDate).toLocaleDateString("en-US")}{" "}
            {new Date(order.createdDate).toLocaleTimeString("en-US")}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem2}
          >
            Status: {order.status}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem}
          >
            Total: ${order.total.toFixed(2)}
          </Typography>
        </CardActionArea>
      </Card>
      <Card className={classes.orderCard}>
        <CardActionArea
          onClick={() => itemClick(order.id)}
          className={classes.orderCardActionArea}
        >
          <div className={classes.orderCardActionArea}>
            <Typography
              variant="subtitle1"
              className={classes.orderCardActionAreaItem}
            >
              #{order.id}
            </Typography>
            <Typography
              variant="subtitle1"
              className={classes.orderCardActionAreaItem2}
            >
              {new Date(order.createdDate).toLocaleDateString("en-US")}{" "}
              {new Date(order.createdDate).toLocaleTimeString("en-US")}
            </Typography>
            <Typography
              variant="subtitle1"
              className={classes.orderCardActionAreaItem2}
            >
              {order.status}
            </Typography>
            <Typography
              variant="subtitle1"
              className={classes.orderCardActionAreaItem}
            >
              ${order.total.toFixed(2)}
            </Typography>
          </div>
        </CardActionArea>
      </Card>
    </Grid>
  );
};

export default OrderCard;

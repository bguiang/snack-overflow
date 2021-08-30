import React, { useState, useEffect } from "react";
import useStyles from "../../styles";
import {
  Grid,
  Typography,
  Card,
  CardActionArea,
  CardActions,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  useMediaQuery,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from "@material-ui/core";
import { useHistory } from "react-router-dom";
import EditIcon from "@material-ui/icons/Edit";
import { useTheme } from "@material-ui/core/styles";
import SnackOverflow from "../../api/SnackOverflow";
import { useAuth } from "../../context/AuthContext";

const OrderCardAdmin = ({ order }) => {
  const classes = useStyles();
  const history = useHistory();

  const { currentUser } = useAuth();
  const [token, setToken] = useState(null);

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  const itemClick = (id) => {
    history.push(`/admin/orders/${id}`);
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
            className={classes.orderCardActionAreaItem}
          >
            User: {order.user.username}
          </Typography>
          <Typography
            variant="subtitle1"
            className={classes.orderCardActionAreaItem2}
          >
            Created: {new Date(order.createdDate).toLocaleDateString("en-US")}
          </Typography>
          {order.status === "CANCELLED" ||
          order.status === "FAILED" ||
          order.status === "REFUNDED" ? (
            <div>
              <div className={classes.orderCardActionAreaItem2}>
                <Typography variant="subtitle1" className={classes.error}>
                  Status: {order.status}
                </Typography>
              </div>
              <div className={classes.orderCardActionAreaItem}>
                <Typography variant="subtitle1" className={classes.error}>
                  Total: ${order.total.toFixed(2)}
                </Typography>
              </div>
            </div>
          ) : (
            <div>
              <div className={classes.orderCardActionAreaItem2}>
                <Typography variant="subtitle1" className={classes.success}>
                  Status: {order.status}
                </Typography>
              </div>
              <div className={classes.orderCardActionAreaItem}>
                <Typography variant="subtitle1" className={classes.success}>
                  Total: ${order.total.toFixed(2)}
                </Typography>
              </div>
            </div>
          )}
        </CardActionArea>
        <CardActions className={classes.snackCardActions}>
          <UpdateOrder order={order} buttonSize="large" token={token} />
        </CardActions>
      </Card>
      <Card className={classes.orderCard}>
        <CardActionArea onClick={() => itemClick(order.id)}>
          <div className={classes.orderCardActionArea}>
            <Typography
              variant="subtitle2"
              className={classes.orderCardActionAreaItem}
            >
              #{order.id}
            </Typography>
            <Typography
              variant="subtitle2"
              className={classes.orderCardActionAreaItem2}
            >
              {order.user.username}
            </Typography>
            <Typography
              variant="subtitle2"
              className={classes.orderCardActionAreaItem2}
            >
              {new Date(order.createdDate).toLocaleDateString("en-US")}
            </Typography>
            {order.status === "CANCELLED" ||
            order.status === "FAILED" ||
            order.status === "REFUNDED" ? (
              <div className={classes.orderCardActionAreaItem2}>
                <Typography variant="subtitle1" className={classes.error}>
                  {order.status}
                </Typography>
              </div>
            ) : (
              <div className={classes.orderCardActionAreaItem2}>
                <Typography variant="subtitle1" className={classes.success}>
                  {order.status}
                </Typography>
              </div>
            )}
            {order.status === "CANCELLED" ||
            order.status === "FAILED" ||
            order.status === "REFUNDED" ? (
              <div className={classes.orderCardActionAreaItem}>
                <Typography variant="subtitle1" className={classes.error}>
                  ${order.total.toFixed(2)}
                </Typography>
              </div>
            ) : (
              <div className={classes.orderCardActionAreaItem}>
                <Typography variant="subtitle1" className={classes.success}>
                  ${order.total.toFixed(2)}
                </Typography>
              </div>
            )}
          </div>
        </CardActionArea>
        <CardActions className={classes.snackCardActions}>
          <UpdateOrder order={order} buttonSize="small" token={token} />
        </CardActions>
      </Card>
    </Grid>
  );
};

const UpdateOrder = ({ order, buttonSize, token }) => {
  const classes = useStyles();
  const history = useHistory();
  const [status, setStatus] = useState(order.status);

  const [open, setOpen] = React.useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("xs"));

  const submitOrderUpdate = async () => {
    try {
      const updateOrderRequest = {
        id: order.id,
        status,
      };
      const response = await SnackOverflow.put(
        "/admin/orders",
        updateOrderRequest,
        {
          headers: { Authorization: token },
        }
      );

      if (200 === response.status) {
        history.push(`/admin/orders/${response.data.id}`);
      }
    } catch (error) {}
  };

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleStatusChange = (event) => {
    setStatus(event.target.value);
  };

  return (
    <div>
      <Button
        size={buttonSize}
        color="primary"
        onClick={() => handleClickOpen()}
        startIcon={<EditIcon />}
      >
        Edit
      </Button>
      <Dialog
        fullScreen={fullScreen}
        open={open}
        onClose={handleClose}
        aria-labelledby="responsive-dialog-title"
      >
        <DialogTitle id="responsive-dialog-title">
          {`Update Status for Order #${order.id}`}
        </DialogTitle>
        <DialogContent>
          <FormControl className={classes.selector}>
            <InputLabel id="status">Status</InputLabel>
            <Select
              labelId="status"
              id="statusSelect"
              value={status}
              onChange={handleStatusChange}
            >
              <MenuItem value={"PAYMENT_PENDING"}>Payment Processing</MenuItem>
              <MenuItem value={"PROCESSING"}>Processing</MenuItem>
              <MenuItem value={"COMPLETED"}>Completed</MenuItem>
              <MenuItem value={"FAILED"}>Failed</MenuItem>
              <MenuItem value={"CANCELLED"}>Cancelled</MenuItem>
              <MenuItem value={"REFUNDED"}>Refunded</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button autoFocus onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={submitOrderUpdate} color="primary" autoFocus>
            Submit
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default OrderCardAdmin;

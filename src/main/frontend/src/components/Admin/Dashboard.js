import React, { useState, useEffect } from "react";
import useStyles from "../../styles";
import {
  Button,
  Grid,
  Card,
  CardContent,
  Typography,
  CardActions,
} from "@material-ui/core";
import { useAuth } from "../../context/AuthContext";
import { useHistory } from "react-router";
import SnackOverflow from "../../api/SnackOverflow";

const Dashboard = () => {
  const classes = useStyles();
  const handleClick = () => {};

  const { currentUser } = useAuth();
  const [token, setToken] = useState(null);
  const history = useHistory();

  const [orderStats, setOrderStats] = useState({});
  const [range, setRange] = useState("month");

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  const getOrdersStats = async () => {
    try {
      let response = await SnackOverflow.get("/admin/orders/stats", {
        params: {
          range,
        },
        headers: { Authorization: token },
      });
      setOrderStats(response.data);
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };
  useEffect(() => {
    if (token !== null && range) {
      getOrdersStats();
    }
  }, [token, range]);

  const ordersThisMonthClick = () => {
    let sortBy = "createdDate";
    let direction = "DESC";
    let pageNumberUI = 1;
    history.push({
      pathname: `/admin/orders`,
      search: `?sortBy=${sortBy}&direction=${direction}&page=${pageNumberUI}`,
    });
  };

  return (
    <Grid container spacing={2} justifyContent="center" alignItems="center">
      <Grid item xs={12} md={5} key="ordersThisMonth">
        <Card className={classes.root}>
          <CardContent>
            <div className={classes.dashboardTile}>
              <div className={classes.dashboardTileItem}>
                <Typography
                  className={classes.title}
                  color="textSecondary"
                  gutterBottom
                >
                  Successfull Orders
                </Typography>
                <Typography variant="h5" className={classes.success}>
                  {orderStats.successfulOrders}
                </Typography>
              </div>
              <div className={classes.dashboardTileItem}>
                <Typography
                  className={classes.title}
                  color="textSecondary"
                  gutterBottom
                >
                  Unsuccessful Orders
                </Typography>
                <Typography variant="h5" className={classes.error}>
                  {orderStats.unsuccessfulOrders}
                </Typography>
              </div>
            </div>
          </CardContent>
          <CardActions>
            <Button size="small" onClick={ordersThisMonthClick}>
              See Details
            </Button>
          </CardActions>
        </Card>
      </Grid>
      <Grid item xs={12} md={5} key="totalIncomeThisMonth">
        <Card className={classes.root}>
          <CardContent>
            <div className={classes.dashboardTile}>
              <div className={classes.dashboardTileItem}>
                <Typography
                  className={classes.title}
                  color="textSecondary"
                  gutterBottom
                >
                  Total Income This Month
                </Typography>
                {orderStats.totalIncome ? (
                  <Typography variant="h5" className={classes.success}>
                    ${orderStats.totalIncome.toFixed(2)}
                  </Typography>
                ) : null}
              </div>
              <div className={classes.dashboardTileItem}>
                <Typography
                  className={classes.title}
                  color="textSecondary"
                  gutterBottom
                >
                  Unsuccessful Payments
                </Typography>
                {orderStats.unsuccessfulPayments ? (
                  <Typography variant="h5" className={classes.error}>
                    ${orderStats.unsuccessfulPayments.toFixed(2)}
                  </Typography>
                ) : null}
              </div>
            </div>
          </CardContent>
          <CardActions>
            <Button size="small" onClick={ordersThisMonthClick}>
              See Details
            </Button>
          </CardActions>
        </Card>
      </Grid>
      <Grid item xs={12} md={2} key="newUsersThisMonth">
        <Card className={classes.root}>
          <CardContent>
            <div className={classes.dashboardTile}>
              <div className={classes.dashboardTileItem}>
                <Typography
                  className={classes.title}
                  color="textSecondary"
                  gutterBottom
                >
                  New Users This Month
                </Typography>
                <Typography variant="h5" component="h2">
                  125
                </Typography>
              </div>
            </div>
          </CardContent>
          <CardActions>
            <Button size="small">See Details</Button>
          </CardActions>
        </Card>
      </Grid>

      <Grid item xs={12} key="topSellingProducts">
        <Card className={classes.root}>
          <CardContent>
            <Typography
              className={classes.title}
              color="textSecondary"
              gutterBottom
            >
              Top Selling Products This Month
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
            <Typography variant="body2" component="p">
              - product/image/price/units sold
            </Typography>
          </CardContent>
          <CardActions>
            <Button size="small">Learn More</Button>
          </CardActions>
        </Card>
      </Grid>
    </Grid>
  );
};

export default Dashboard;

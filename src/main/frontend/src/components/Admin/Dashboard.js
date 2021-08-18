import React, { useState, useEffect } from "react";
import useStyles from "../../styles";
import {
  Button,
  Grid,
  Card,
  CardContent,
  Typography,
  CardActions,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from "@material-ui/core";
import { useAuth } from "../../context/AuthContext";
import { useHistory } from "react-router";
import SnackOverflow from "../../api/SnackOverflow";
import TopProductCard from "./TopProductCard";

const Dashboard = () => {
  const classes = useStyles();
  const handleClick = () => {};

  const { currentUser } = useAuth();
  const [token, setToken] = useState(null);
  const history = useHistory();

  const [orderStats, setOrderStats] = useState({});
  const [newUsers, setNewUsers] = useState(0);
  const [topProducts, setTopProducts] = useState([]);
  const [range, setRange] = useState("month");

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  const getOrderStats = async () => {
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

  const getUserStats = async () => {
    try {
      let response = await SnackOverflow.get("/admin/users/stats", {
        params: {
          range,
        },
        headers: { Authorization: token },
      });
      setNewUsers(response.data.newUsers);
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getTopSellingProducts = async () => {
    try {
      let response = await SnackOverflow.get("/admin/products", {
        params: {
          search: "",
          pageSize: 5,
          pageNumber: 0,
          itemsSold: range,
          sortBy: "unitsSold",
          sortDirection: "DESC",
        },
        headers: { Authorization: token },
      });
      setTopProducts(response.data.content);
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (token !== null && range) {
      getOrderStats();
      getUserStats();
      getTopSellingProducts();
    }
  }, [token, range]);

  const orderStatsClick = () => {
    let sortBy = "createdDate";
    let direction = "DESC";
    let pageNumberUI = 1;
    history.push({
      pathname: `/admin/orders`,
      search: `?sortBy=${sortBy}&direction=${direction}&page=${pageNumberUI}`,
    });
  };

  const userStatsClick = () => {
    let sortBy = "joinDate";
    let direction = "DESC";
    let pageNumberUI = 1;
    history.push({
      pathname: `/admin/members`,
      search: `?sortBy=${sortBy}&direction=${direction}&page=${pageNumberUI}`,
    });
  };

  const topProductsClick = () => {
    let sortBy = "unitsSold";
    let direction = "DESC";
    let pageNumberUI = 1;
    history.push({
      pathname: `/admin/products`,
      search: `?&sortBy=${sortBy}&direction=${direction}&includeOrders=${range}&page=${pageNumberUI}`,
    });
  };

  return (
    <Grid container spacing={2} justifyContent="center" alignItems="center">
      <Grid item xs={12} key="pageTitle" className={classes.cartHeader}>
        <h2 className={classes.cartHeaderTitle}>Dashboard</h2>
        <FormControl className={classes.adminSelector}>
          <InputLabel id="range">Range</InputLabel>
          <Select
            labelId="range"
            id="rangeSelect"
            value={range}
            onChange={(event) => setRange(event.target.value)}
          >
            <MenuItem value={"all"}>All</MenuItem>
            <MenuItem value={"month"}>Month</MenuItem>
            <MenuItem value={"year"}>Year</MenuItem>
          </Select>
        </FormControl>
      </Grid>
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
            <Button size="small" onClick={orderStatsClick}>
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
                {orderStats.totalIncome !== null &&
                typeof orderStats.totalIncome !== "undefined" ? (
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
                {orderStats.unsuccessfulPayments !== null &&
                typeof orderStats.unsuccessfulPayments !== "undefined" ? (
                  <Typography variant="h5" className={classes.error}>
                    ${orderStats.unsuccessfulPayments.toFixed(2)}
                  </Typography>
                ) : null}
              </div>
            </div>
          </CardContent>
          <CardActions>
            <Button size="small" onClick={orderStatsClick}>
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
                  New Users
                </Typography>
                <Typography variant="h5" component="h2">
                  {newUsers}
                </Typography>
              </div>
            </div>
          </CardContent>
          <CardActions>
            <Button size="small" onClick={userStatsClick}>
              See Details
            </Button>
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
              Top Selling Products
            </Typography>
            <div className={classes.productCardHorizontalTitle}>
              <div className={classes.cartItemCardActionArea}>
                <Typography
                  variant="subtitle1"
                  className={classes.productCardHorizontalID}
                >
                  ID
                </Typography>
                <div className={classes.productCardHorizontalMain}>
                  <Typography
                    variant="subtitle1"
                    className={classes.productCardHorizontalName}
                  >
                    Product
                  </Typography>
                </div>
                <Typography
                  variant="subtitle1"
                  className={classes.productCardHorizontalPrice}
                >
                  Price
                </Typography>
                <Typography
                  variant="subtitle1"
                  className={classes.productCardHorizontalUnitsSold}
                >
                  Units Sold
                </Typography>
              </div>
            </div>
            {topProducts.map((product) => (
              <TopProductCard product={product} key={product.id} />
            ))}
          </CardContent>
          <CardActions>
            <Button size="small" onClick={topProductsClick}>
              See Details
            </Button>
          </CardActions>
        </Card>
      </Grid>
    </Grid>
  );
};

export default Dashboard;

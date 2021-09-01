import React, { useState, useEffect } from "react";
import {
  Grid,
  MenuItem,
  FormControl,
  Select,
  InputLabel,
  InputBase,
  IconButton,
  Paper,
  Typography,
} from "@material-ui/core";
import useStyles from "../../styles";
import OrderCardAdmin from "./OrderCardAdmin";
import Pagination from "@material-ui/lab/Pagination";
import { useAuth } from "../../context/AuthContext";
import { useHistory } from "react-router";
import { useLocation } from "react-router-dom";
import SnackOverflow from "../../api/SnackOverflow";
import SearchIcon from "@material-ui/icons/Search";

const Orders = () => {
  const classes = useStyles();

  const [token, setToken] = useState(null);
  const { currentUser } = useAuth();
  const location = useLocation();
  const history = useHistory();
  const [search, setSearch] = useState("");
  const [pageNumber, setPageNumber] = useState(0);
  const [pageNumberUI, setPageNumberUI] = useState(1);
  const [totalPages, setTotalPages] = useState(0);

  const [orders, setOrders] = useState([]);
  const [sortBy, setSortBy] = useState("createdDate");
  const [direction, setDirection] = useState("DESC");

  useEffect(() => {
    let urlParams = new URLSearchParams(location.search);

    if (urlParams.get("search") !== null) {
      setSearch(urlParams.get("search"));
    }

    if (urlParams.get("page")) {
      let currentPage = parseInt(urlParams.get("page"));
      setPageNumber(currentPage - 1);
      setPageNumberUI(currentPage);
    }
    if (urlParams.get("sortBy")) {
      setSortBy(urlParams.get("sortBy"));
    }
    if (urlParams.get("direction")) {
      setDirection(urlParams.get("direction"));
    }
  }, [location]);

  const handleSearchSubmit = () => {
    history.push({
      pathname: `/admin/orders`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&page=${1}`,
    });
  };

  const handleSearchChange = (event) => {
    history.push({
      pathname: `/admin/orders`,
      search: `?search=${
        event.target.value
      }&sortBy=${sortBy}&direction=${direction}&page=${1}`,
    });
  };

  const handleSortByChange = (event) => {
    history.push({
      pathname: `/admin/orders`,
      search: `?search=${search}&sortBy=${
        event.target.value
      }&direction=${direction}&page=${1}`,
    });
  };

  const handleDirectionChange = (event) => {
    history.push({
      pathname: `/admin/orders`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${
        event.target.value
      }&page=${1}`,
    });
  };

  const handlePageChange = (value) => {
    history.push({
      pathname: `/admin/orders`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&page=${value}`,
    });
  };

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  const getOrders = async () => {
    try {
      let response = await SnackOverflow.get("/admin/orders", {
        params: {
          search: search,
          pageSize: 9,
          pageNumber: pageNumber,
          sortBy: sortBy,
          sortDirection: direction,
        },
        headers: { Authorization: token },
      });
      setOrders(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {}
  };
  useEffect(() => {
    if (token !== null) {
      getOrders();
    }
  }, [search, pageNumber, token, sortBy, direction]);

  return (
    <Grid container spacing={2} justifyContent="center" alignItems="center">
      <Grid item xs={12} key="pageTitle" className={classes.cartHeader}>
        <h2 className={classes.cartHeaderTitle}>Orders</h2>
        <div className={classes.adminSelector}>
          <InputLabel id="sortBy">Sort By</InputLabel>
          <Select
            labelId="sortBy"
            id="sortBySelect"
            value={sortBy}
            onChange={handleSortByChange}
          >
            <MenuItem value={"createdDate"}>Created Date</MenuItem>
            <MenuItem value={"id"}>ID</MenuItem>
            <MenuItem value={"user.username"}>Username</MenuItem>
            <MenuItem value={"status"}>Status</MenuItem>
            <MenuItem value={"total"}>Total</MenuItem>
          </Select>
        </div>
        <div className={classes.adminSelector}>
          <InputLabel id="direction">Direction</InputLabel>
          <Select
            labelId="direction"
            id="direcitonSelect"
            value={direction}
            onChange={handleDirectionChange}
          >
            <MenuItem value={"ASC"}>Ascending</MenuItem>
            <MenuItem value={"DESC"}>Descending</MenuItem>
          </Select>
        </div>
      </Grid>
      <Grid container spacing={1} key="orderInfo">
        <Grid item xs={12} key={"listTitle"} className={classes.orderListTitle}>
          <Paper className={classes.adminSearchBar}>
            <InputBase
              className={classes.search}
              autoComplete="off"
              placeholder="Search Orders By Username"
              inputProps={{ "aria-label": "search" }}
              onChange={handleSearchChange}
            />
            <IconButton
              type="submit"
              className={classes.iconButton}
              aria-label="search"
              onClick={() => handleSearchSubmit()}
            >
              <SearchIcon />
            </IconButton>
          </Paper>
          <div className={classes.adminSelectorMobileContainer}>
            <FormControl className={classes.adminSelectorMobile}>
              <InputLabel id="sortBy">Sort By</InputLabel>
              <Select
                labelId="sortBy"
                id="sortBySelect"
                value={sortBy}
                onChange={handleSortByChange}
              >
                <MenuItem value={"createdDate"}>Created Date</MenuItem>
                <MenuItem value={"id"}>ID</MenuItem>
                <MenuItem value={"user.username"}>Username</MenuItem>
                <MenuItem value={"status"}>Status</MenuItem>
                <MenuItem value={"total"}>Total</MenuItem>
              </Select>
            </FormControl>
            <FormControl className={classes.adminSelectorMobile}>
              <InputLabel id="direction">Direction</InputLabel>
              <Select
                labelId="direction"
                id="direcitonSelect"
                value={direction}
                onChange={handleDirectionChange}
              >
                <MenuItem value={"ASC"}>Ascending</MenuItem>
                <MenuItem value={"DESC"}>Descending</MenuItem>
              </Select>
            </FormControl>
          </div>
          <div className={classes.orderCard}>
            <div className={classes.orderCardActionArea}>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem}
              >
                Order
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem2}
              >
                Username
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem2}
              >
                Created
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem2}
              >
                Status
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem}
              >
                Total
              </Typography>
              <div className={classes.orderTitleFiller}></div>
            </div>
          </div>
        </Grid>
        {orders.map((order) => (
          <OrderCardAdmin order={order} key={order.id} />
        ))}
      </Grid>
      <Grid item xs={12} key="pagination" className={classes.pagination}>
        <div className={classes.snacksPaginationContainer}>
          <Pagination
            count={totalPages}
            color="primary"
            page={pageNumberUI}
            onChange={(event, value) => {
              handlePageChange(value);
            }}
          />
        </div>
      </Grid>
    </Grid>
  );
};

export default Orders;

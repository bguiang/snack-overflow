import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import {
  Grid,
  MenuItem,
  FormControl,
  Select,
  InputLabel,
  Typography,
  Button,
  InputBase,
  IconButton,
  Paper,
} from "@material-ui/core";
import ProductCard from "./ProductCard";
import useStyles from "../../styles";
import SnackOverflow from "../../api/SnackOverflow";
import Pagination from "@material-ui/lab/Pagination";
import { useAuth } from "../../context/AuthContext";
import AddIcon from "@material-ui/icons/Add";
import { useHistory } from "react-router";
import SearchIcon from "@material-ui/icons/Search";

const ProductsAdmin = () => {
  const [token, setToken] = useState(null);
  const { currentUser } = useAuth();
  const location = useLocation();
  const history = useHistory();
  const [search, setSearch] = useState("");
  const [pageNumber, setPageNumber] = useState(0);
  const [pageNumberUI, setPageNumberUI] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const classes = useStyles();

  const [products, setProducts] = useState([]);
  const [includeOrders, setIncludeOrders] = useState("all");
  const [sortBy, setSortBy] = useState("unitsSold");
  const [direction, setDirection] = useState("DESC");

  useEffect(() => {
    console.log("Location changed");
    let urlParams = new URLSearchParams(location.search);
    console.log("urlParams: " + urlParams);

    if (urlParams.get("search") !== null) {
      setSearch(urlParams.get("search"));
      console.log("Search: " + urlParams.get("search"));
    }

    if (urlParams.get("page")) {
      let currentPage = parseInt(urlParams.get("page"));
      setPageNumber(currentPage - 1);
      setPageNumberUI(currentPage);
      console.log("PageUI: " + currentPage);
    }
    if (urlParams.get("sortBy")) {
      setSortBy(urlParams.get("sortBy"));
      console.log("SortBy: " + urlParams.get("sortBy"));
    }
    if (urlParams.get("direction")) {
      setDirection(urlParams.get("direction"));
      console.log("Direction: " + urlParams.get("direction"));
    }
    if (urlParams.get("includeOrders")) {
      setIncludeOrders(urlParams.get("includeOrders"));
      console.log("includeOrders: " + urlParams.get("includeOrders"));
    }
  }, [location]);

  const handleSearchSubmit = () => {
    history.push({
      pathname: `/admin/products`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&includeOrders=${includeOrders}&page=${pageNumberUI}`,
    });
  };

  const handleSearchChange = (event) => {
    history.push({
      pathname: `/admin/products`,
      search: `?search=${event.target.value}&sortBy=${sortBy}&direction=${direction}&includeOrders=${includeOrders}&page=${pageNumberUI}`,
    });
  };

  const handleIncludeOrdersChange = (event) => {
    history.push({
      pathname: `/admin/products`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&includeOrders=${event.target.value}&page=${pageNumberUI}`,
    });
  };

  const handleSortByChange = (event) => {
    history.push({
      pathname: `/admin/products`,
      search: `?search=${search}&sortBy=${event.target.value}&direction=${direction}&includeOrders=${includeOrders}&page=${pageNumberUI}`,
    });
  };

  const handleDirectionChange = (event) => {
    history.push({
      pathname: `/admin/products`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${event.target.value}&includeOrders=${includeOrders}&page=${pageNumberUI}`,
    });
  };

  const handlePageChange = (value) => {
    history.push({
      pathname: `/admin/products`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&includeOrders=${includeOrders}&page=${value}`,
    });
  };

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  const getProducts = async () => {
    try {
      let response = await SnackOverflow.get("/admin/products", {
        params: {
          search: search,
          pageSize: 9,
          pageNumber: pageNumber,
          itemsSold: includeOrders,
          sortBy: sortBy,
          sortDirection: direction,
        },
        headers: { Authorization: token },
      });
      setProducts(response.data.content);
      setTotalPages(response.data.totalPages);
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };
  // Call Get Snacks Once
  useEffect(() => {
    if (token !== null) getProducts();
  }, [search, pageNumber, token, sortBy, includeOrders, direction]);

  return (
    <div>
      <Grid
        container
        spacing={1}
        justifyContent="center"
        alignItems="center"
        justifyContent="flex-start"
        alignItems="flex-start"
      >
        <Grid item xs={12} key="pageTitle" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Products</h2>
          <FormControl className={classes.adminSelector}>
            <InputLabel id="includeOrders">Include Orders</InputLabel>
            <Select
              labelId="includeOrders"
              id="includeOrdersSelect"
              value={includeOrders}
              onChange={handleIncludeOrdersChange}
            >
              <MenuItem value={"all"}>All</MenuItem>
              <MenuItem value={"month"}>Month</MenuItem>
              <MenuItem value={"year"}>Year</MenuItem>
            </Select>
          </FormControl>
          <FormControl className={classes.adminSelector}>
            <InputLabel id="sortBy">Sort By</InputLabel>
            <Select
              labelId="sortBy"
              id="sortBySelect"
              value={sortBy}
              onChange={handleSortByChange}
            >
              <MenuItem value={"id"}>ID</MenuItem>
              <MenuItem value={"name"}>Name</MenuItem>
              <MenuItem value={"price"}>Price</MenuItem>
              <MenuItem value={"unitsSold"}>Units Sold</MenuItem>
            </Select>
          </FormControl>
          <FormControl className={classes.adminSelector}>
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
        </Grid>
        <Grid
          item
          xs={12}
          key="search"
          className={classes.adminSearchContainer}
        >
          <Paper className={classes.adminSearchBar}>
            <InputBase
              className={classes.search}
              autocomplete="off"
              placeholder="Search Products by Name"
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
              <InputLabel id="includeOrders">Include Orders</InputLabel>
              <Select
                labelId="includeOrders"
                id="includeOrdersSelect"
                value={includeOrders}
                onChange={handleIncludeOrdersChange}
              >
                <MenuItem value={"all"}>All</MenuItem>
                <MenuItem value={"month"}>Month</MenuItem>
                <MenuItem value={"year"}>Year</MenuItem>
              </Select>
            </FormControl>
            <FormControl className={classes.adminSelectorMobile}>
              <InputLabel id="sortBy">Sort By</InputLabel>
              <Select
                labelId="sortBy"
                id="sortBySelect"
                value={sortBy}
                onChange={handleSortByChange}
              >
                <MenuItem value={"id"}>ID</MenuItem>
                <MenuItem value={"name"}>Name</MenuItem>
                <MenuItem value={"price"}>Price</MenuItem>
                <MenuItem value={"unitsSold"}>Units Sold</MenuItem>
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
        </Grid>
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
            <div className={classes.productCardHorizontalFiller}>
              <Button
                size="large"
                color="primary"
                onClick={() => history.push("/admin/products/new")}
                startIcon={<AddIcon />}
              >
                New
              </Button>
            </div>
          </div>
        </div>
        {products.map((product) => (
          <ProductCard product={product} key={product.id} />
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
    </div>
  );
};

export default ProductsAdmin;

import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import {
  Grid,
  MenuItem,
  FormHelperText,
  FormControl,
  Select,
  InputLabel,
  Typography,
  CardActionArea,
  Card,
} from "@material-ui/core";
import ProductCard from "./ProductCard";
import useStyles from "../../styles";
import SnackOverflow from "../../api/SnackOverflow";
import Pagination from "@material-ui/lab/Pagination";
import { useAuth } from "../../context/AuthContext";

const ProductsAdmin = () => {
  const [token, setToken] = useState(null);
  const { currentUser } = useAuth();
  const location = useLocation();
  const [search, setSearch] = useState("");
  const [pageNumber, setPageNumber] = useState(0);
  const [pageNumberUI, setPageNumberUI] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const classes = useStyles();
  const [includeOrders, setIncludeOrders] = useState("all");
  const [sortBy, setSortBy] = useState("unitsSold");
  const [direction, setDirection] = useState("DESC");

  const handleIncludeOrdersChange = (event) => {
    setIncludeOrders(event.target.value);
  };

  const handleSortByChange = (event) => {
    setSortBy(event.target.value);
  };

  const handleDirectionChange = (event) => {
    setDirection(event.target.value);
  };

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  useEffect(() => {
    if (token !== null) {
      setSearch(new URLSearchParams(location.search).get("search"));
      setPageNumber(0);
      setPageNumberUI(1);
    }
  }, [location, token]);

  const [products, setProducts] = useState([]);

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
          <FormControl className={classes.selector}>
            <InputLabel id="includeOrders">Include Orders</InputLabel>
            <Select
              labelId="includeOrders"
              id="includeOrdersSelect"
              value={includeOrders}
              onChange={handleIncludeOrdersChange}
            >
              <MenuItem value={"all"}>All</MenuItem>
              <MenuItem value={"month"}>Month</MenuItem>
              <MenuItem value={"year"}>Year</MenuItem>{" "}
            </Select>
          </FormControl>
          <FormControl className={classes.selector}>
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
          <FormControl className={classes.selector}>
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
            <div className={classes.productCardHorizontalFiller}></div>
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
              setPageNumberUI(value);
              setPageNumber(value - 1);
            }}
          />
        </div>
      </Grid>
    </div>
  );
};

export default ProductsAdmin;

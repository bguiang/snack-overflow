import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import {
  Grid,
  MenuItem,
  FormControl,
  Select,
  InputLabel,
} from "@material-ui/core";

import SnackCard from "./SnackCard";
import useStyles from "../../styles";
import SnackOverflow from "../../api/SnackOverflow";
import Pagination from "@material-ui/lab/Pagination";
import { useHistory } from "react-router";

const Snacks = () => {
  const classes = useStyles();

  const location = useLocation();
  const history = useHistory();

  const [snacks, setSnacks] = useState([]);
  const [search, setSearch] = useState("");
  const [pageNumber, setPageNumber] = useState(0);
  const [pageNumberUI, setPageNumberUI] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [sortBy, setSortBy] = useState("unitsSold");
  const [direction, setDirection] = useState("DESC");

  useEffect(() => {
    let urlParams = new URLSearchParams(location.search);

    let urlSearch = "";
    let urlPageNumber = 0;
    let urlPageNumberUI = 1;
    let urlSortBy = "unitsSold";
    let urlDirection = "DESC";

    if (urlParams.get("search") !== null) {
      urlSearch = urlParams.get("search");
      setSearch(urlSearch);
    }

    if (urlParams.get("page")) {
      urlPageNumberUI = parseInt(urlParams.get("page"));
      urlPageNumber = urlPageNumberUI - 1;
      setPageNumber(urlPageNumber);
      setPageNumberUI(urlPageNumberUI);
    }
    if (urlParams.get("sortBy")) {
      urlSortBy = urlParams.get("sortBy");
      setSortBy(urlSortBy);
    }
    if (urlParams.get("direction")) {
      urlDirection = urlParams.get("direction");
      setDirection(urlDirection);
    }

    const params = {
      search: urlSearch,
      pageSize: 9,
      pageNumber: urlPageNumber,
      itemsSold: "month",
      sortBy: urlSortBy,
      sortDirection: urlDirection,
    };

    getSnacks(params);
  }, [location]);

  const getSnacks = async (params) => {
    try {
      let response = await SnackOverflow.get("/products", {
        params,
        // params: {
        //   search: search,
        //   pageSize: 9,
        //   pageNumber: pageNumber,
        //   itemsSold: "month",
        //   sortBy: sortBy,
        //   sortDirection: direction,
        // },
      });
      setSnacks(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {}
  };

  const handleSortByChange = (event) => {
    history.push({
      pathname: `/snacks`,
      search: `?search=${search}&sortBy=${
        event.target.value
      }&direction=${direction}&page=${1}`,
    });
  };

  const handleDirectionChange = (event) => {
    history.push({
      pathname: `/snacks`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${
        event.target.value
      }&page=${1}`,
    });
  };

  const handlePageChange = (value) => {
    history.push({
      pathname: `/snacks`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&page=${value}`,
    });
  };

  return (
    <div className={classes.content}>
      <Grid
        container
        spacing={5}
        justifyContent="center"
        alignItems="center"
        justifyContent="flex-start"
        alignItems="flex-start"
      >
        <Grid item xs={12} key="pageTitle" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Snacks</h2>
          <div className={classes.adminSelector}>
            <InputLabel id="sortBy">Sort By</InputLabel>
            <Select
              labelId="sortBy"
              id="sortBySelect"
              value={sortBy}
              onChange={handleSortByChange}
            >
              <MenuItem value={"createdDate"}>Newest</MenuItem>
              <MenuItem value={"name"}>Name</MenuItem>
              <MenuItem value={"price"}>Price</MenuItem>
              <MenuItem value={"unitsSold"}>Popularity</MenuItem>
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
      </Grid>
      <Grid item xs={12} key="search" className={classes.adminSearchContainer}>
        <div className={classes.adminSelectorMobileContainer}>
          <FormControl className={classes.adminSelectorMobile}>
            <InputLabel id="sortBy">Sort By</InputLabel>
            <Select
              labelId="sortBy"
              id="sortBySelect"
              value={sortBy}
              onChange={handleSortByChange}
            >
              <MenuItem value={"createdDate"}>Newest</MenuItem>
              <MenuItem value={"name"}>Name</MenuItem>
              <MenuItem value={"price"}>Price</MenuItem>
              <MenuItem value={"unitsSold"}>Popularity</MenuItem>
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
      <Grid item xs={12} key="snacks">
        <Grid container spacing={5}>
          {snacks.map((snack) => (
            <SnackCard snack={snack} key={snack.id} />
          ))}
        </Grid>
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

export default Snacks;

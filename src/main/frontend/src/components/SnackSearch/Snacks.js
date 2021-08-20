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
      pathname: `/snacks`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&&page=${1}`,
    });
  };

  const handleSearchChange = (event) => {
    history.push({
      pathname: `/snacks`,
      search: `?search=${
        event.target.value
      }&sortBy=${sortBy}&direction=${direction}&page=${1}`,
    });
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

  const getSnacks = async () => {
    try {
      let response = await SnackOverflow.get("/products", {
        params: {
          search: search,
          pageSize: 9,
          pageNumber: pageNumber,
          itemsSold: "month",
          sortBy: sortBy,
          sortDirection: direction,
        },
      });
      setSnacks(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {}
  };
  // Call Get Snacks Once
  useEffect(() => {
    getSnacks();
  }, [search, pageNumber, sortBy, direction]);

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
          <FormControl className={classes.adminSelector}>
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
        {snacks.map((snack) => (
          <SnackCard snack={snack} key={snack.id} />
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

export default Snacks;

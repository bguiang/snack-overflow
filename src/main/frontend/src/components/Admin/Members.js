import React, { useState, useEffect } from "react";
import {
  Grid,
  MenuItem,
  FormControl,
  Select,
  InputLabel,
  Typography,
  InputBase,
  IconButton,
  Paper,
} from "@material-ui/core";
import useStyles from "../../styles";
import MemberCard from "./MemberCard";
import Pagination from "@material-ui/lab/Pagination";
import { useAuth } from "../../context/AuthContext";
import { useHistory } from "react-router";
import { useLocation } from "react-router-dom";
import SnackOverflow from "../../api/SnackOverflow";
import SearchIcon from "@material-ui/icons/Search";

const Members = () => {
  const classes = useStyles();
  const [token, setToken] = useState(null);
  const { currentUser } = useAuth();
  const location = useLocation();
  const history = useHistory();
  const [search, setSearch] = useState("");
  const [pageNumber, setPageNumber] = useState(0);
  const [pageNumberUI, setPageNumberUI] = useState(1);
  const [totalPages, setTotalPages] = useState(0);

  const [members, setMembers] = useState([]);
  const [sortBy, setSortBy] = useState("joinDate");
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
  }, [location]);

  const handleSearchSubmit = () => {
    history.push({
      pathname: `/admin/members`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&page=${pageNumberUI}`,
    });
  };

  const handleSearchChange = (event) => {
    history.push({
      pathname: `/admin/members`,
      search: `?search=${event.target.value}&sortBy=${sortBy}&direction=${direction}&page=${pageNumberUI}`,
    });
  };

  const handleSortByChange = (event) => {
    history.push({
      pathname: `/admin/members`,
      search: `?search=${search}&sortBy=${event.target.value}&direction=${direction}&page=${pageNumberUI}`,
    });
  };

  const handleDirectionChange = (event) => {
    history.push({
      pathname: `/admin/members`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${event.target.value}&page=${pageNumberUI}`,
    });
  };

  const handlePageChange = (value) => {
    history.push({
      pathname: `/admin/members`,
      search: `?search=${search}&sortBy=${sortBy}&direction=${direction}&page=${value}`,
    });
  };

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  const getMembers = async () => {
    try {
      let response = await SnackOverflow.get("/admin/users", {
        params: {
          search: search,
          pageSize: 9,
          pageNumber: pageNumber,
          sortBy: sortBy,
          sortDirection: direction,
        },
        headers: { Authorization: token },
      });
      setMembers(response.data.content);
      setTotalPages(response.data.totalPages);
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };
  useEffect(() => {
    if (token !== null) {
      getMembers();
    }
  }, [search, pageNumber, token, sortBy, direction]);

  return (
    <Grid
      container
      justifyContent="center"
      alignItems="center"
      justifyContent="flex-start"
      alignItems="flex-start"
    >
      <Grid item xs={12} key="pageTitle" className={classes.cartHeader}>
        <h2 className={classes.cartHeaderTitle}>Members</h2>
        <FormControl className={classes.adminSelector}>
          <InputLabel id="sortBy">Sort By</InputLabel>
          <Select
            labelId="sortBy"
            id="sortBySelect"
            value={sortBy}
            onChange={handleSortByChange}
          >
            <MenuItem value={"email"}>Email</MenuItem>
            <MenuItem value={"joinDate"}>Join Date</MenuItem>
            <MenuItem value={"id"}>ID</MenuItem>
            <MenuItem value={"role"}>Role</MenuItem>
            <MenuItem value={"username"}>Username</MenuItem>
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
      <Grid item xs={12} key="search" className={classes.adminSearchContainer}>
        <Paper className={classes.adminSearchBar}>
          <InputBase
            className={classes.search}
            autocomplete="off"
            placeholder="Search Members by Username"
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
              <MenuItem value={"email"}>Email</MenuItem>
              <MenuItem value={"joinDate"}>Join Date</MenuItem>
              <MenuItem value={"id"}>ID</MenuItem>
              <MenuItem value={"username"}>Username</MenuItem>
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
      <Grid container xs={12} spacing={1} key="memberInfo">
        <Grid item xs={12} key={"listTitle"}>
          <div className={classes.memberCard}>
            <div className={classes.orderCardActionArea}>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem}
              >
                ID
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
                Email
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem2}
              >
                Full Name
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem2}
              >
                Role
              </Typography>
              <Typography
                variant="subtitle1"
                className={classes.orderCardActionAreaItem1}
              >
                Join Date
              </Typography>
            </div>
          </div>
        </Grid>
        {members.map((member) => (
          <MemberCard member={member} key={member.id} />
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

export default Members;

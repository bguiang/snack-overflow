import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { Grid } from "@material-ui/core";
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

  const [snacks, setSnacks] = useState([]);

  const getSnacks = async () => {
    try {
      let response = await SnackOverflow.get("/admin/products", {
        params: { search: search, pageSize: 9, pageNumber: pageNumber },
        headers: { Authorization: token },
      });
      setSnacks(response.data.content);
      setTotalPages(response.data.totalPages);
      //console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };
  // Call Get Snacks Once
  useEffect(() => {
    if (token !== null) getSnacks();
  }, [search, pageNumber, token]);

  return (
    <div>
      <Grid
        container
        spacing={5}
        justifyContent="center"
        alignItems="center"
        justifyContent="flex-start"
        alignItems="flex-start"
      >
        <Grid item xs={12} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Products</h2>
        </Grid>
        {snacks.map((snack) => (
          <ProductCard snack={snack} key={snack.id} />
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

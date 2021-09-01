import React, { useEffect, useState } from "react";
import { useHistory, useLocation } from "react-router-dom";
import { Grid, Typography, CardMedia } from "@material-ui/core";

import SnackCard from "../SnackSearch/SnackCard";
import useStyles from "../../styles";
import SnackOverflow from "../../api/SnackOverflow";
import Carousel from "react-material-ui-carousel";

const Home = () => {
  const history = useHistory();
  const classes = useStyles();

  const [popularSnacks, setPopularSnacks] = useState([]);
  const [newestSnacks, setNewestSnacks] = useState([]);

  const getPopularSnacks = async () => {
    try {
      let response = await SnackOverflow.get("/products", {
        params: {
          search: "",
          pageSize: 10,
          pageNumber: 0,
          sortBy: "unitsSold",
          sortDirection: "DESC",
          itemsSold: "month",
        },
      });
      setPopularSnacks(response.data.content);
    } catch (error) {}
  };

  const getNewestSnacks = async () => {
    try {
      let response = await SnackOverflow.get("/products", {
        params: {
          search: "",
          pageSize: 9,
          pageNumber: 0,
          sortBy: "createdDate",
          sortDirection: "DESC",
          itemsSold: "all",
        },
      });
      setNewestSnacks(response.data.content);
    } catch (error) {}
  };

  // Call Get Snacks Once
  useEffect(() => {
    getPopularSnacks();
    getNewestSnacks();
  }, []);

  const snackClick = (snack) => {
    history.push(`/snacks/${snack.id}`);
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
        <Grid item xs={12} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Most Popular This Month</h2>
        </Grid>
        <Carousel
          className={classes.homeCarousel}
          navButtonsAlwaysVisible={true}
          next={() => {
            /* Do stuff */
          }}
          prev={() => {
            /* Do other stuff */
          }}
        >
          {popularSnacks.map((snack, i) => (
            <div
              className={classes.snackCarouselItem}
              key={"popular." + snack.id}
              onClick={() => snackClick(snack)}
            >
              <div className={classes.snackCarouselItemContent}>
                <CardMedia
                  className={classes.snackItemCarouselImage}
                  image={snack.images[0] ? snack.images[0] : null}
                  title={snack.name}
                />
                <div className={classes.snackItemCarouselDescription}>
                  <Typography gutterBottom variant="h5" component="h5">
                    {snack.name}
                  </Typography>
                  <Typography variant="h6" component="h6">
                    ${snack.price.toFixed(2)}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="textSecondary"
                    component="p"
                  >
                    {snack.description}
                  </Typography>
                </div>
              </div>
            </div>
          ))}
        </Carousel>
        <Grid item xs={12} key="title2" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>New Snacks</h2>
        </Grid>
        {newestSnacks.map((snack) => (
          <SnackCard snack={snack} key={"newest." + snack.id} />
        ))}
      </Grid>
    </div>
  );
};

export default Home;

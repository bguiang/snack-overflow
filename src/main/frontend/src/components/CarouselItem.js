import React from "react";

import useStyles from "../styles";
import { CardMedia, Paper } from "@material-ui/core";

const CarouselItem = ({ name, image }) => {
  const classes = useStyles();
  return (
    <div className={classes.carouselImageContainer}>
      <CardMedia image={image} className={classes.carouselImage} title={name} />
    </div>
  );
};
export default CarouselItem;

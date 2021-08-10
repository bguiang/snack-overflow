import React from "react";
import { Grid } from "@material-ui/core";
import useStyles from "../../styles";

const Contact = () => {
  const classes = useStyles();
  return (
    <div className={classes.content}>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Contact Us</h2>
        </Grid>
      </Grid>
    </div>
  );
};

export default Contact;

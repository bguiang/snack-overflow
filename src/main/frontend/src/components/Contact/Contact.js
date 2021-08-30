import React from "react";
import { Grid, Typography, Link } from "@material-ui/core";
import useStyles from "../../styles";

const Contact = () => {
  const classes = useStyles();
  return (
    <div className={classes.content}>
      <Grid container spacing={2} justifyContent="center" alignItems="center">
        <Grid item xs={12} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Contact Us</h2>
        </Grid>
        <Grid item xs={12} key="title2" className={classes.cartHeader}>
          <Typography>
            Thank you for visiting my demo site. Feel free to contact me through{" "}
            <Link
              color="primary"
              href={"https://www.linkedin.com/in/bernard-guiang"}
              target="_blank"
            >
              LinkedIn
            </Link>{" "}
            - Bernard
          </Typography>
        </Grid>
      </Grid>
    </div>
  );
};

export default Contact;

import React from "react";
import useSnacks from "../../hooks/useSnacks";
import { Grid } from "@material-ui/core";

import SnackCard from "./SnackCard";
import useStyles from "../../styles";

const Snacks = () => {
  const [snacks] = useSnacks();
  const classes = useStyles();

  return (
    <div>
      <Grid container spacing={5} justifyContent="center" alignItems="center">
        <Grid item xs={12} key="title" className={classes.cartHeader}>
          <h2 className={classes.cartHeaderTitle}>Snacks</h2>
        </Grid>
        {snacks.map((snack) => (
          <SnackCard snack={snack} key={snack.id} />
        ))}
      </Grid>
    </div>
  );
};

export default Snacks;

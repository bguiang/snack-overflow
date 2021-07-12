import React from "react";
import useSnacks from "../hooks/useSnacks";
import { Grid } from "@material-ui/core";

import SnackCard from "./SnackCard";

const Snacks = () => {
  const [snacks] = useSnacks();

  return (
    <div>
      <Grid container spacing={5} justifyContent="center" alignItems="center">
        {snacks.map((snack) => (
          <SnackCard snack={snack} />
        ))}
      </Grid>
    </div>
  );
};

export default Snacks;

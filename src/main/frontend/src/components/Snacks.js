import React from "react";
import useSnacks from "../hooks/useSnacks";

import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import FormLabel from '@material-ui/core/FormLabel';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import Paper from '@material-ui/core/Paper';
import { useHistory } from 'react-router-dom';

const useStyles = makeStyles((theme) => ({
    root: {
      flexGrow: 1,
    },
    paper: {
      height: 200,
      width: 150,
    },
    control: {
      padding: theme.spacing(2),
    },
  }));

const Snacks = () => {
    const [snacks] = useSnacks();
    const classes = useStyles();
    const history = useHistory();

    const snackClick = (snack) => {
        history.push(`/snacks/${snack.id}`);
    };

    return (

        //There are five grid breakpoints: xs, sm, md, lg, and xl.
        <Grid container className={classes.root} spacing={2}>
            <Grid item xs={8}>
                <Grid container justify="center" spacing={2}>
                {snacks.map((snack) => (
                    <Grid key={snack} item xs={8} sm={6} md={4}>
                        <Paper className={classes.paper}  onClick={() => snackClick(snack)}>

                            <h3>{snack.name}</h3>
                        </Paper>
                    </Grid>
                ))}
                </Grid>
            </Grid>
        </Grid>
    );
}

export default Snacks;
import React from "react";
import useSnacks from "../hooks/useSnacks";

import { makeStyles } from '@material-ui/core/styles';
import { Grid, Paper, Typography, Box, TextField, Container } from "@material-ui/core";
import { useHistory } from 'react-router-dom';
import useStyles from "../styles";

const Snacks = () => {
    const classes = useStyles();
    const [snacks] = useSnacks();
    const history = useHistory();

    const snackClick = (snack) => {
        history.push(`/snacks/${snack.id}`);
    };

    return (
        //There are five grid breakpoints: xs, sm, md, lg, and xl.
        <Grid container className={classes.shop} spacing={2}>
            <Grid item xs={8}>
                <Grid container justify="center" spacing={2}>
                {snacks.map((snack) => (
                    <Grid key={snack} item xs={8} sm={6} md={4}>
                        <Paper className={classes.shopItem}  onClick={() => snackClick(snack)}>
                            <Typography variant="h6">{snack.name}</Typography>
                            <Typography variant="subtitle1">{snack.description}</Typography>
                            <Typography variant="subtitle1">${snack.price}</Typography>
                        </Paper>
                    </Grid>
                ))}
                </Grid>
            </Grid>
        </Grid>
    );
}

export default Snacks;
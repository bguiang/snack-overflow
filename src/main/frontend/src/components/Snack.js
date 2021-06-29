import React from "react";

import { makeStyles } from '@material-ui/core/styles';
import { useState, useEffect, useReducer } from "react";
import SnackOverflow from "../api/SnackOverflow";
import {
    useParams
  } from "react-router-dom";

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

  

const Snack = () => {
    const classes = useStyles();
    let { id } = useParams();
    const [snack, setSnack]  = useState({});

    const getSnacks = async () => {
        try {
            let response = await SnackOverflow.get(`/products/${id}`);
            setSnack(response.data);
            console.log(response.data);
        } 
        catch (error) {
            console.log(error);
        }
    };

    // Call Get Snacks Once
    useEffect(() => {
        getSnacks();
    },[])

    return (
        <div>
            <h3>Snacks</h3>
        </div>
    );
}

export default Snack;
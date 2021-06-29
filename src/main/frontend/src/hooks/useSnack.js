import { useState, useEffect, useReducer } from "react";
import SnackOverflow from "../api/SnackOverflow";

export default () => {
    const [snack, setSnack]  = useState({});

    const getSnacks = async () => {
        try {
            let response = await SnackOverflow.get(`/products/${id}`);
            setSnacks(response.data);
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

    return [snack];
};
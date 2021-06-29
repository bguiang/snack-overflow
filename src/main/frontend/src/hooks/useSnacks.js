import { useState, useEffect, useReducer } from "react";
import SnackOverflow from "../api/SnackOverflow";

export default () => {
    const [snacks, setSnacks]  = useState([]);

    const getSnacks = async () => {
        try {
            let response = await SnackOverflow.get('/products');
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

    return [snacks];
};
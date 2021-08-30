import { useState, useEffect } from "react";
import SnackOverflow from "../api/SnackOverflow";

export default (id) => {
  const [snack, setSnack] = useState({});

  const getSnack = async () => {
    try {
      let response = await SnackOverflow.get(`/products/${id}`);
      setSnack(response.data);
    } catch (error) {}
  };

  // Call Get Snacks Once
  useEffect(() => {
    getSnack();
  }, []);

  return [snack];
};

import { useState, useEffect } from "react";
import SnackOverflow from "../api/SnackOverflow";

export default (search) => {
  const [snacks, setSnacks] = useState([]);

  const getSnacks = async () => {
    try {
      let response = await SnackOverflow.get("/products", {
        params: { search: search, pageSize: 9 },
      });
      setSnacks(response.data.content);
      //console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  // Call Get Snacks Once
  useEffect(() => {
    getSnacks();
  }, []);

  return [snacks];
};

import { useState, useEffect } from "react";
import SnackOverflow from "../api/SnackOverflow";
import { useAuth } from "../context/AuthContext";

export default () => {
  const [orders, setOrders] = useState([]);
  const [token, setToken] = useState(null);
  const { currentUser } = useAuth();

  const getOrders = async () => {
    try {
      let response = await SnackOverflow.get("/orders", {
        headers: { Authorization: token },
      });
      if (response.status === 200) {
        setOrders(response.data);
      }
    } catch (error) {}
  };
  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  useEffect(() => {
    if (token !== null) {
      getOrders();
    }
  }, [token]);

  return [orders];
};

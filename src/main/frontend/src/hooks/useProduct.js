import { useState, useEffect } from "react";
import SnackOverflow from "../api/SnackOverflow";
import { useAuth } from "../context/AuthContext";

export default (id) => {
  const [product, setProduct] = useState({});
  const [token, setToken] = useState(null);
  const { currentUser } = useAuth();

  const getProduct = async () => {
    try {
      let response = await SnackOverflow.get(`/admin/products/${id}`, {
        headers: { Authorization: token },
      });
      setProduct(response.data);
    } catch (error) {}
  };

  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  useEffect(() => {
    if (token !== null) {
      getProduct();
    }
  }, [token]);

  return [product];
};

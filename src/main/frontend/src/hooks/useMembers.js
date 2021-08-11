import { useState, useEffect } from "react";
import SnackOverflow from "../api/SnackOverflow";
import { useAuth } from "../context/AuthContext";

export default () => {
  const [members, setMembers] = useState([]);
  const [token, setToken] = useState(null);
  const { currentUser } = useAuth();

  const getMembers = async () => {
    try {
      console.log("Calling Get Members()");
      let response = await SnackOverflow.get("/admin/users", {
        headers: { Authorization: token },
      });
      if (response.status === 200) {
        setMembers(response.data);
        console.log(response.data);
      }
    } catch (error) {
      console.log(error);
    }
  };
  useEffect(() => {
    if (currentUser) setToken("Bearer " + currentUser.authenticationToken);
  }, [currentUser]);

  useEffect(() => {
    if (token !== null) {
      getMembers();
    }
  }, [token]);

  return [members, getMembers];
};

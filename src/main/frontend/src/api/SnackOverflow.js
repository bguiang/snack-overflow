import axios from "axios";

export default axios.create({
  baseURL: "/api/v1", // spring boot backend
});

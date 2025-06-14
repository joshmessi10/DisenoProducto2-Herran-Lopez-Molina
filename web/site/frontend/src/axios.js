import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "/", // Proxy will redirect this to http://35.188.126.68
  withCredentials: true, // Include credentials if needed
});

export default axiosInstance;

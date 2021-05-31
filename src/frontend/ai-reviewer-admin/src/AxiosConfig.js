import axios from "axios";
import { REACT_APP_AI_REVIEWER_API_URL } from "EnvConfig";

const api = axios.create({
  baseURL: `${REACT_APP_AI_REVIEWER_API_URL}`,
});

api.interceptors.request.use((request) => {
  const token = localStorage.getItem("jwt");
  request.headers.Authorization = `Bearer ${token}`;
  return request;
});

export default api;

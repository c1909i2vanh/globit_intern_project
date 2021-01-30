import axios from "axios";
import ConstantList from "../../appConfig";
const API_PATH = ConstantList.API_ENPOINT + "/api/employees";
const API_PATH_EMPLOYEE = ConstantList.API_ENPOINT + "api/employee";

export const searchByPage = (searchObject) => {
  //return axios.get("/api/user/all");
  //alert( axios.defaults.headers.common["Authorization"]);
  console.log("aaaaaaaa")
  var url = API_PATH + "/searchByPage";
  return axios.post(url, searchObject);
};

export const getItemById = id => {
  var url = API_PATH + "/"+id;
  return axios.get(url);
};
export const deleteEmployee = Employee => {
  return axios.post("/api/user/delete", Employee);
};
export const addNewEmployee = Employee => {
  return axios.post(API_PATH_EMPLOYEE, Employee);
};
export const updateEmployee = Employee => {
  return axios.patch(API_PATH_EMPLOYEE, Employee);
};

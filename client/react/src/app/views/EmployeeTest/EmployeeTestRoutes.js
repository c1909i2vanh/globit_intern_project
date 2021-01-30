import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";
const EmployeeTestTable = EgretLoadable({
  loader: () => import("./EmployeeTestTable")
});

const employeeTestRoutes = [
  {
    path:  ConstantList.ROOT_PATH+"myemployees",
    exact: true,
    component: EmployeeTestTable
  }
];

export default employeeTestRoutes;

import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import Companies from "../../../companies/pages/companies/Companies.tsx";
import AddCompany from "../../../companies/pages/companies/AddCompany.tsx";

export const companyMenuRoutes: IMenuRoutes = {
  routePrefix: "/companies/",
  elements: [
    {path: "", name: "Companies", element: <Companies/>},
    {path: "create", name: "Add new company", element: <AddCompany/>}
  ]
}

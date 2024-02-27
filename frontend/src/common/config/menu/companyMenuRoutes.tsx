import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import Companies from "../../../companies/pages/companies/Companies.tsx";

export const companyMenuRoutes: IMenuRoutes = {
  routePrefix: "/companies/",
  elements: [
    {path: "", name: "Companies", element: <Companies/>}
  ]
}

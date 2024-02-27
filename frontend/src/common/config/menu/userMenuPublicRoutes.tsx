import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import Home from "../../../public/pages/home/Home.tsx";

export const userMenuPublicRoutes: IMenuRoutes = {
  routePrefix: "",
  elements: [
    {path: "", name: "Home", element: <Home/>}
  ]
}

import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import Home from "../../../public/home/Home.tsx";
import MUIExample from "../../../public/dev/MUIExample.tsx";

export const userMenuPublicRoutes: IMenuRoutes = {
  routePrefix: "",
  elements: [
    {path: "", name: "Home", element: <Home/>},
    {path: "dev", name: "Dev", element: <MUIExample/>}
  ]
}

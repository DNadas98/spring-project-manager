import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import Home from "../../../public/pages/home/Home.tsx";
import MUIExample from "../../../public/pages/dev/MUIExample.tsx";

export const userMenuPublicRoutes: IMenuRoutes = {
  routePrefix: "",
  elements: [
    {path: "", name: "Home", element: <Home/>},
    {path: "dev", name: "Dev", element: <MUIExample/>}
  ]
}

import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import Home from "../../../public/home/Home.tsx";
import MUIExample from "../../../public/dev/MUIExample.tsx";

export const publicMenuRoutes: IMenuRoutes = {
  elements: [
    {path: "/", name: "Home", element: <Home/>},
    {path: "/login", name: "Sign in", element: <Home/>},
    {path: "/register", name: "Sign up", element: <Home/>},
    {path: "/dev", name: "Dev", element: <MUIExample/>}
  ]
}

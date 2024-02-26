import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import Home from "../../../public/home/Home.tsx";
import MUIExample from "../../../public/dev/MUIExample.tsx";
import Login from "../../../authentication/pages/Login.tsx";
import Register from "../../../authentication/pages/Register.tsx";

export const publicMenuRoutes: IMenuRoutes = {
  routePrefix: "/",
  elements: [
    {path: "", name: "Home", element: <Home/>},
    {path: "login", name: "Sign in", element: <Login/>},
    {path: "register", name: "Sign up", element: <Register/>},
    {path: "dev", name: "Dev", element: <MUIExample/>}
  ]
}

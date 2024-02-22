import {IMenuRoutes} from "./IMenuRoutes.ts";
import Home from "../../public/pages/Home.tsx";
import MUIExample from "../../theme/examples/MUIExample.tsx";

export const publicMenuRoutes: IMenuRoutes = {
  elements: [
    {path: "/", name: "Home", element: <Home/>},
    {path: "/login", name: "Sign in", element: <Home/>},
    {path: "/register", name: "Sign up", element: <Home/>},
    {path: "/react-buddy-dev", name: "Dev", element: <MUIExample/>}/*
        ,{
          path: "/login",
          name: "Login",
          element: <Login/>
        },
        {
          path: "/register",
          name: "Register",
          element: <Register/>
        }*/
  ]
}

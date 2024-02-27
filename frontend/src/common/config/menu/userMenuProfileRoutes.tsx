import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import Profile from "../../../user/pages/profile/Profile.tsx";
import Logout from "../../../authentication/pages/Logout.tsx";

export const userMenuProfileRoutes: IMenuRoutes = {
  routePrefix: "/user/",
  elements: [
    {path: "", name: "Profile", element: <Profile/>},
    {path: "logout", name: "Sign out", element: <Logout/>}
  ]
}

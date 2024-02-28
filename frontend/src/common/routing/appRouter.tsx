import {createBrowserRouter} from "react-router-dom";
import Layout from "../../public/layout/Layout.tsx";
import ErrorPage from "../../public/pages/errorPages/ErrorPage.tsx";
import {publicMenuRoutes} from "../config/menu/publicMenuRoutes.tsx";
import NotFound from "../../public/pages/errorPages/NotFound.tsx";
import OAuth2Redirect from "../../authentication/pages/redirect/OAuth2Redirect.tsx";
import RequireAuthentication
  from "../../authentication/components/RequireAuthentication.tsx";
import {GlobalRole} from "../../authentication/dto/userInfo/GlobalRole.ts";
import UserLayout from "../../user/layout/UserLayout.tsx";
import {userMenuProfileRoutes} from "../config/menu/userMenuProfileRoutes.tsx";
import RegisterVerificationRedirect
  from "../../authentication/pages/redirect/RegisterVerificationRedirect.tsx";
import {companyMenuRoutes} from "../config/menu/companyMenuRoutes.tsx";
import CompanyDashboard from "../../companies/pages/dashboard/CompanyDashboard.tsx";
import CompanyLayout from "../../companies/layout/CompanyLayout.tsx";
import {PermissionProvider} from "../../authentication/context/PermissionProvider.tsx";

const appRouter = createBrowserRouter([
  /* public */
  {
    path: "",
    element: <Layout/>,
    errorElement: <ErrorPage/>,
    children: [
      ...publicMenuRoutes.elements,
      {
        path: "/*",
        element: <NotFound/>
      }
    ]
  },
  /* redirect */
  {
    path: "/redirect",
    element: <Layout/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        path: "oauth2",
        element: <OAuth2Redirect/>
      },
      {
        path: "registration",
        element: <RegisterVerificationRedirect/>
      }
    ]
  },
  /* user */
  {
    path: "/user/",
    element: <RequireAuthentication allowedRoles={[GlobalRole.USER]}/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        element: <UserLayout/>,
        children: [
          ...userMenuProfileRoutes.elements,
          {
            path: "*",
            element: <NotFound/>
          }
        ]
      }
    ]
  },
  /* companies */
  {
    path: "/companies/",
    element: <RequireAuthentication allowedRoles={[GlobalRole.USER]}/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        element: <PermissionProvider/>,
        children: [
          {
            element: <CompanyLayout/>,
            children: [
              ...companyMenuRoutes.elements,
              {
                path: ":companyId",
                element: <CompanyDashboard/>
              },
              {
                path: "*",
                element: <NotFound/>
              }
            ]
          }
        ]
      }
    ]
  }
]);

export default appRouter;

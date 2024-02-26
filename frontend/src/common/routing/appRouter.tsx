import {createBrowserRouter} from "react-router-dom";
import Layout from "../../public/layout/Layout.tsx";
import ErrorPage from "../../public/errorPages/ErrorPage.tsx";
import {publicMenuRoutes} from "../config/menu/publicMenuRoutes.tsx";
import NotFound from "../../public/errorPages/NotFound.tsx";
import LoadingSpinner from "../utils/components/LoadingSpinner.tsx";
import OAuth2Redirect from "../../authentication/pages/OAuth2Redirect.tsx";
import RequireAuthentication
  from "../../authentication/components/RequireAuthentication.tsx";
import {GlobalRole} from "../../authentication/dto/userInfo/GlobalRole.ts";
import UserLayout from "../../user/layout/UserLayout.tsx";
import {userMenuRoutes} from "../config/menu/userMenuRoutes.tsx";

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
        element: <LoadingSpinner/>
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
          ...userMenuRoutes.elements,
          {
            path: "*",
            element: <NotFound/>
          }
        ]
      }
    ]
    }
  /* companies */
  /*{
    path: "/companies",
    element: <RequireAuth allowedRoles={["USER"]}/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        element: <UserLayout/>,
        children: [
          {
            path: "",
            element: <CompanyBrowser/>
          },
          {
            path: "create",
            element: <CreateCompany/>
          },
          {
            path: "update/:companyId",
            element: <UpdateCompany/>
          },
          {
            path: ":companyId/projects/create",
            element: <CreateProject/>
          },
          {
            path: ":companyId/projects/update/:projectId",
            element: <UpdateProject/>
          },
          {
            path: ":companyId/projects/:projectId/tasks/create",
            element: <CreateTask/>
          },
          {
            path: ":companyId/projects/:projectId/tasks/update/:taskId",
            element: <UpdateTask/>
          },
          {
            path: ":companyId/projects/:projectId/tasks/:taskId",
            element: <TaskDetails/>
          },
          {
            path: ":companyId/projects/:projectId",
            element: <ProjectDashboard/>
          },
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
  }*/
]);

export default appRouter;

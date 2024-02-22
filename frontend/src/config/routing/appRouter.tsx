import {createBrowserRouter} from "react-router-dom";
import Layout from "../../public/layout/Layout.tsx";
import ErrorPage from "../../public/pages/ErrorPage.tsx";
import {publicMenuRoutes} from "./publicMenuRoutes.tsx";
import NotFound from "../../public/pages/NotFound.tsx";
import LoadingSpinner from "../../common/components/LoadingSpinner.tsx";

const oauthRedirectPath = import.meta.env.VITE_OAUTH2_REDIRECT_URL;
const registrationRedirectPath = import.meta.env.VITE_REGISTRATION_REDIRECT_URL;

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
        path: `${oauthRedirectPath}`,
        element: <LoadingSpinner/>
      },
      {
        path: `${registrationRedirectPath}`,
        element: <LoadingSpinner/>
      },
    ]
  }
  /* user */
  /*{
    path: "/user",
    element: <RequireAuth allowedRoles={["USER"]}/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        element: <UserLayout/>,
        children: [
          {
            path: "",
            element: <UserDashboard/>
          },
          {
            path: "*",
            element: <NotFound/>
          }
        ]
      }
    ]
  },*/
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

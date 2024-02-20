import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Home from "../../public/pages/Home.tsx";
import ErrorPage from "../../public/pages/ErrorPage.tsx";
import NotFound from "../../public/pages/NotFound.tsx";
import Layout from "../../public/layout/Layout.tsx";

export function AppRouterProvider() {
//  const oAuth2RedirectURL = import.meta.env.VITE_OAUTH2_REDIRECT_URL;

  const router = createBrowserRouter([
    /* public */
    {
      path: "/",
      element: <Layout/>,
      errorElement: <ErrorPage/>,
      children: [
        {
          path: "/",
          element: <Home/>
        },/*
        {
          path: "/login",
          element: <Login/>
        },
        {
          path: "/register",
          element: <Register/>
        },
        {
          path: "/redirect/registration",
          element: <RegisterVerificationRedirect/>
        },
        {
          path: `/${oAuth2RedirectURL}`,
          element: <OAuth2Redirect/>
        },*/
        {
          path: "/*",
          element: <NotFound/>
        }
      ]
    },
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


  return <RouterProvider router={router}/>
}
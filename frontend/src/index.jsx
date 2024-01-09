import React from 'react';
import {createRoot} from 'react-dom/client';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import './index.css';
import RequireAuth from "./auth/components/RequireAuth.jsx";
import {AuthProvider} from "./auth/context/AuthProvider.jsx";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Login from "./auth/pages/Login.jsx";
import Register from "./auth/pages/Register.jsx";
import Layout from "./public/layout/Layout.jsx";
import ErrorPage from "./public/pages/ErrorPage.jsx";
import Home from "./public/pages/Home.jsx";
import NotFound from "./public/pages/NotFound.jsx";
import UserDashboard from "./user/pages/UserDashboard.jsx";
import UserLayout from "./user/layout/UserLayout.jsx";
import CompanyDashboard from "./user/pages/CompanyDashboard.jsx";
import CreateCompany from "./user/pages/CreateCompany.jsx";
import UpdateCompany from "./user/pages/UpdateCompany.jsx";
import ProjectDashboard from "./user/pages/ProjectDashboard.jsx";
import UpdateProject from "./user/pages/UpdateProject.jsx";
import CreateProject from "./user/pages/CreateProject.jsx";
import OAuth2Redirect from "./auth/pages/OAuth2Redirect.jsx";
import CreateTask from "./user/pages/CreateTask.jsx";
import UpdateTask from "./user/pages/UpdateTask.jsx";
import TaskDetails from "./user/pages/TaskDetails.jsx";
import CompanyBrowser from "./user/pages/CompanyBrowser.jsx";

const theme = createTheme({
  palette: {
    primary: {
      main: '#11508d',
    },
    secondary: {
      main: '#a4cfe1',
    },
    blueish: {
      main: '#007BFF',
    },
  },
  typography: {
    h1: {
      fontSize: '3.5rem',
      fontWeight: 'bold',
    },
    h2: {
      fontSize: '2rem',
    },
    h3: {
      fontSize: '1.5rem',
    },
    body1: {
      color: "white", accentColor: "white"
    }
  }
});

const oAuth2RedirectURL = import.meta.env.VITE_OAUTH2_REDIRECT_URL;

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
      },
      {
        path: "/login",
        element: <Login/>
      },
      {
        path: "/register",
        element: <Register/>
      },
      {
        path: `/${oAuth2RedirectURL}`,
        element: <OAuth2Redirect/>
      },
      {
        path: "/*",
        element: <NotFound/>
      }
    ]
  },
  /* user */
  {
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
  },
  /* user */
  {
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
  }
]);

const rootElement = document.getElementById("root");
const root = createRoot(rootElement);

root.render(
  <ThemeProvider theme={theme}>
    <AuthProvider>
      <RouterProvider router={router}/>
    </AuthProvider>
  </ThemeProvider>
);

export default theme;


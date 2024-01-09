import React, {useEffect, useState} from "react";
import useAuthFetch from "../../api/useAuthFetch.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import {Link, useParams} from "react-router-dom";
import BackButton from "../../components/BackButton.jsx";
import useAuth from "../../auth/hooks/useAuth.js";
import {
  Alert, Button, Container, List, ListItem, ListItemText, Paper, Snackbar, Typography
} from "@mui/material";
import {styled} from "@mui/system";

const StyledPaper = styled(Paper)(({theme}) => ({
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2),
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF"
}));

const StyledTypography = styled(Typography)(({theme}) => ({
  fontWeight: "bold", fontSize: "2rem", color: "#FFFFFF", marginBottom: theme.spacing(2)
}));

function CompanyDashboard() {
  const authFetch = useAuthFetch();
  const {auth} = useAuth();

  const [loading, setLoading] = useState(true);
  const companyId = useParams()?.companyId;
  const [company, setCompany] = useState(null);
  const [joinedProjects, setJoinedProjects] = useState([]);
  const [projectsToJoin, setProjectsToJoin] = useState([]);
  const [joinRequests, setJoinRequests] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("error");

  const userIsCompanyOwner = company?.companyOwner?.username && auth?.username && company.companyOwner.username === auth.username;

  async function loadCompany() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("GET", `companies/${companyId}`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load company");
      }
      setCompany(responseObject.data);
    } catch (e) {
      console.error(e);
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load company");
      setCompany(null);
    } finally {
      setLoading(false);
    }
  }

  async function loadJoinedProjects() {
    try {
      setLoading(true);
      const {
        httpResponse,
        responseObject
      } = await authFetch("GET", `companies/${companyId}/projects/withUser`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load joined projects");
      }
      setJoinedProjects(responseObject.data);
    } catch (e) {
      console.error(e);
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load joined projects");
      setJoinedProjects([]);
    } finally {
      setLoading(false);
    }
  }

  async function loadProjectsToJoin() {
    try {
      setLoading(true);
      const {
        httpResponse,
        responseObject
      } = await authFetch("GET", `companies/${companyId}/projects/withoutUser`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load projects to join");
      }
      setProjectsToJoin(responseObject.data);
    } catch (e) {
      console.error(e);
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load projects to join");
      setProjectsToJoin([]);
    } finally {
      setLoading(false);
    }
  }

  async function handleJoinProject(projectId) {
    try {
      setLoading(true);
      const {
        httpResponse, responseObject
      } = await authFetch("POST", `companies/${companyId}/projects/${projectId}/requests`);
      if (httpResponse?.status !== 201 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to process project join request");
      } else {
        const projectName = responseObject?.data?.project?.name;
        const joinRequestStatus = responseObject?.data?.status;
        if (!projectName || !joinRequestStatus) {
          throw new Error("Invalid response received for project join request");
        }
        await loadProjectsToJoin();
        setSnackbarSeverity("success");
        setSnackbarMessage(`Request to join ${projectName} sent successfully`);
        setSnackbarOpen(true);
      }
    } catch (e) {
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to process join request");
      setSnackbarOpen(true);
      console.error(e);
    } finally {
      setLoading(false);
    }
  }

  async function loadJoinRequests() {
    try {
      setLoading(true);
      const {
        httpResponse,
        responseObject
      } = await authFetch("GET", `companies/${companyId}/requests`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load join requests");
      }
      setJoinRequests(responseObject.data);
    } catch (e) {
      console.error(e);
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load join requests");
      setJoinRequests([]);
    } finally {
      setLoading(false);
    }
  }

  async function updateJoinRequest(requestId, status) {
    try {
      setLoading(true);
      const {
        httpResponse,
        responseObject
      } = await authFetch("PUT", `companies/${companyId}/requests/${requestId}`, {status: status});
      if (httpResponse?.status !== 200 || !responseObject?.message) {
        throw new Error(responseObject?.error ?? "Failed to update join request");
      }

      setSnackbarSeverity("success");
      setSnackbarMessage(responseObject.message);
      setSnackbarOpen(true);

      await loadCompany();
      setSnackbarSeverity("success");
      setSnackbarMessage("Join request updated successfully");
      setSnackbarOpen(true);
    } catch (e) {
      console.error(e);
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to update join request");
      setSnackbarOpen(true);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadCompany();
    loadProjectsToJoin();
    loadJoinedProjects();
  }, []);

  useEffect(() => {
    if (userIsCompanyOwner) {
      loadJoinRequests();
    }
  }, [company]);

  return loading ? (<LoadingSpinner/>) : (<Container>
    <Snackbar
      open={snackbarOpen}
      autoHideDuration={6000}
      onClose={() => setSnackbarOpen(false)}
      anchorOrigin={{vertical: "top", horizontal: "center"}}
    >
      <Alert
        onClose={() => setSnackbarOpen(false)}
        severity={snackbarSeverity}
      >
        {snackbarMessage}
      </Alert>
    </Snackbar>
    <StyledPaper>
      {company ? (<div className={"companyDetails"}>
        <StyledTypography variant="h2">{company.name}</StyledTypography>
        <Typography variant="p">{company.description}</Typography>

        {!userIsCompanyOwner ? (<Typography variant="h5">
          Company owner: {company.companyOwner?.username}
        </Typography>) : (<Typography variant="h5">You own this company</Typography>)}

        {company.employees?.length ? (<>
          <Typography variant="h4">Employees:</Typography>
          <List className={"employeeList"}>
            {company.employees.map((employee) => (<ListItem key={employee.userId}>
              <ListItemText primary={employee.username}/>
            </ListItem>))}
          </List>
        </>) : (<></>)}

        <Typography variant="h4">Your Projects:</Typography>
        <List className={"projectList"}>
          {joinedProjects?.length ? (joinedProjects.map((project) => (
            <ListItem key={project.projectId}>
              <ListItemText
                primary={<Link
                  to={`/companies/${companyId}/projects/${project.projectId}`}
                  style={{color: "#FFFFFF"}}
                >
                  {project.name}
                </Link>}
                secondary={project.description}
              />
            </ListItem>))) : (<p>No projects yet</p>)}
        </List>

        <Typography variant="h4">Other projects:</Typography>
        <List className={"projectList"}>
          {projectsToJoin?.length ? (projectsToJoin.map((project) => (
            <ListItem key={project.projectId}>
              <Button variant={"contained"}
                      onClick={() => {
                        handleJoinProject(project.projectId);
                      }}
                      style={{color: "#FFFFFF"}}
              >
                {project.name}
              </Button>
              <p>Description: {project.description}</p>
            </ListItem>))) : (<p>No projects yet</p>)}
        </List>

        <Link
          to={`/companies/${companyId}/projects/create`}
          style={{color: "#FFFFFF"}}
        >
          <Typography variant="h5">Create new project</Typography>
        </Link>

        {userIsCompanyOwner ? (<>
          <Link
            to={`/companies/update/${companyId}`}
            style={{color: "#FFFFFF"}}
          >
            <Typography variant="h5" sx={{marginTop: 3}}>Update company details</Typography>
          </Link>

          {joinRequests?.length ? (<>
            <Typography variant="h2">Join requests:</Typography>
            <List className={"requestList"}>
              {joinRequests.map((request) => (<ListItem key={request.requestId}>
                          <span>
                            {request.user.username}
                            <Button
                              variant="contained"
                              onClick={() => {
                                updateJoinRequest(request.requestId, "APPROVED");
                              }}
                              style={{color: "#FFFFFF", marginLeft: 10}}
                            >
                              Accept
                            </Button>
                            <Button
                              variant="contained"
                              onClick={() => {
                                updateJoinRequest(request.requestId, "DECLINED");
                              }}
                              style={{color: "#FFFFFF", marginLeft: 10}}
                            >
                              Decline
                            </Button>
                          </span>
              </ListItem>))}
            </List>
          </>) : (<></>)}
        </>) : (<></>)}
      </div>) : (<></>)}
      <BackButton path={`/companies`}/>
    </StyledPaper>
  </Container>);
}

export default CompanyDashboard;

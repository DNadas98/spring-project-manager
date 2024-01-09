import React, {useEffect, useState} from "react";
import useAuthFetch from "../../api/useAuthFetch.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import {Link, useParams} from "react-router-dom";
import BackButton from "../../components/BackButton.jsx";
import useAuth from "../../auth/hooks/useAuth.js";
import {Alert, Button, Container, List, ListItem, Paper, Snackbar, Typography} from "@mui/material";
import {styled} from "@mui/system";
import {format, parseISO} from "date-fns";

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

const StyledSubtitle = styled(Typography)(({theme}) => ({
  fontSize: "1.2rem", color: "#FFFFFF", marginBottom: theme.spacing(2)
}));

function ProjectDashboard() {
  const authFetch = useAuthFetch();
  const {auth} = useAuth();

  const [loading, setLoading] = useState(true);
  const companyId = useParams()?.companyId;
  const projectId = useParams()?.projectId;
  const [project, setProject] = useState(null);
  const [joinRequests, setJoinRequests] = useState([]);
  const [finishedTasks, setFinishedTasks] = useState([]);
  const [unfinishedTasks, setUnfinishedTasks] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("error");

  const userIsProjectOwner = project?.projectOwner?.username && auth?.username && project.projectOwner.username === auth.username;

  async function loadProject() {
    try {
      setLoading(true);
      const {
        httpResponse, responseObject
      } = await authFetch("GET", `companies/${companyId}/projects/${projectId}`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load project");
      }
      setProject(responseObject.data);
    } catch (e) {
      console.error(e);
      setSnackbarSeverity("error");
      setSnackbarMessage("Failed to load project");
      setSnackbarOpen(true);
      setProject(null);
    } finally {
      setLoading(false);
    }
  }

  async function loadUnfinishedTasks() {
    try {
      setLoading(true);
      const {
        httpResponse,
        responseObject
      } = await authFetch("GET", `companies/${companyId}/projects/${projectId}/tasks/unfinished`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load unfinished tasks");
      }
      setUnfinishedTasks(responseObject.data);
    } catch (e) {
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load unfinished tasks");
      setUnfinishedTasks([]);
    } finally {
      setLoading(false);
    }
  }

  async function loadFinishedTasks() {
    try {
      setLoading(true);
      const {
        httpResponse,
        responseObject
      } = await authFetch("GET", `companies/${companyId}/projects/${projectId}/tasks/finished`);
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load finished tasks");
      }
      setFinishedTasks(responseObject.data);
    } catch (e) {
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load finished tasks");
      setFinishedTasks([]);
    } finally {
      setLoading(false);
    }
  }

  async function loadJoinRequests() {
    try {
      setLoading(true);
      const {
        httpResponse, responseObject
      } = await authFetch("GET", `companies/${companyId}/projects/${projectId}/requests`);
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
      debugger;
      const {
        httpResponse, responseObject
      } = await authFetch("PUT", `companies/${companyId}/projects/${projectId}/requests/${requestId}`, {status: status});

      if (httpResponse?.status !== 200 || !responseObject?.message) {
        throw new Error(responseObject?.error ?? "Failed to update join request");
      }

      await loadProject();

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
    loadProject();
    loadFinishedTasks();
    loadUnfinishedTasks();
  }, []);

  useEffect(() => {
    if (userIsProjectOwner) {
      loadJoinRequests();
    }
  }, [project]);

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
      {project ? (<div className={"projectDetails"}>
        <StyledTypography variant="h2">{project.name}</StyledTypography>

        {!userIsProjectOwner ? (<Typography variant="h5">
          Project owner: {project.projectOwner?.username}
        </Typography>) : (<Typography variant="h5">You own this project</Typography>)}

        {project.assignedEmployees?.length ? (<>
          <StyledSubtitle>Assigned employees:</StyledSubtitle>
          <ul className={"employeeList"}>
            {project.assignedEmployees.map((employee) => {
              return <li key={employee.userId}>{employee.username}</li>;
            })}
          </ul>
        </>) : (<></>)}

        <StyledSubtitle>Ongoing tasks:</StyledSubtitle>
        <ul className={"projectList"}>
          {unfinishedTasks?.length ? (unfinishedTasks.map((task) => {
            return (<li key={task.taskId}>
              <Link
                to={`/companies/${companyId}/projects/${projectId}/tasks/${task.taskId}`}
                style={{color: "#FFFFFF"}}
              >
                {task.name}
              </Link>
              <p>{task.description}</p>
            </li>);
          })) : (<p>
            No tasks found
          </p>)}
        </ul>

        <StyledSubtitle>Finished tasks:</StyledSubtitle>
        <ul className={"projectList"}>
          {finishedTasks?.length ? (finishedTasks.map((task) => {
            return (<li key={task.taskId}>
              <Link
                to={`/companies/${companyId}/projects/${projectId}/tasks/${task.taskId}`}
                style={{color: "#FFFFFF"}}
              >
                {task.name} - {task.taskStatus}
              </Link>
              <p>{task.description}</p>
            </li>);
          })) : (<p>
            No tasks found
          </p>)}
        </ul>

        <Link to={`/companies/${companyId}/projects/${projectId}/tasks/create`}
              style={{color: "#FFFFFF"}}>
          Create a new task
        </Link>

        <Typography variant="h6">
          Start date: {format(parseISO(project.startDate), "yyyy-MM-dd HH:mm")}
        </Typography>
        <Typography variant="h6">
          Deadline: {format(parseISO(project.deadline), "yyyy-MM-dd HH:mm")}
        </Typography>

        {userIsProjectOwner ? (<>
          <Typography variant="h3" sx={{marginTop: 3}}>
            <Link
              to={`/companies/${companyId}/projects/update/${projectId}`}
              style={{color: "#FFFFFF"}}
            >
              Update project details
            </Link>
          </Typography>
        </>) : (<></>)}

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

      </div>) : (<></>)}
      <div>
        <BackButton path={`/companies/${companyId}`}/>
      </div>
    </StyledPaper>
  </Container>);
}

export default ProjectDashboard;

import React, {useEffect, useState} from "react";
import useAuthFetch from "../../api/useAuthFetch.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import {Link, useParams} from "react-router-dom";
import BackButton from "../../components/BackButton.jsx";
import useAuth from "../../auth/hooks/useAuth.js";
import {Alert, Container, Paper, Snackbar, Typography} from "@mui/material";
import {styled} from "@mui/system";

const StyledPaper = styled(Paper)(({theme}) => ({
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2),
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF"
}));

const StyledTypography = styled(Typography)(({theme}) => ({
  fontWeight: "bold",
  fontSize: "2rem",
  color: "#FFFFFF",
  marginBottom: theme.spacing(2)
}));

const StyledSubtitle = styled(Typography)(({theme}) => ({
  fontSize: "1.2rem",
  color: "#FFFFFF",
  marginBottom: theme.spacing(2)
}));

function TaskDetails() {
  const authFetch = useAuthFetch();
  const {auth} = useAuth();

  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState(null);
  const {companyId, projectId, taskId} = useParams();
  const [task, setTask] = useState(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("error");

  async function loadTask() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch(
        "GET",
        `companies/${companyId}/projects/${projectId}/tasks/${taskId}`
      );
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        setErrorMessage(responseObject?.error ?? "Failed to load task");
        setTask(null);
        return;
      }
      setTask(responseObject.data);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load task");
      setTask(null);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadTask();
  }, []);

  return loading ? (
    <LoadingSpinner/>
  ) : (
    <Container>
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
        <h2>{task.name}</h2>
        <p>Points to be earned: {task.points}</p>
        <p>Start date: {task.startDate}</p>
        <p>Deadline: {task.deadline}</p>
        <h4>Importance: {task.importance.replaceAll("_", " ")}</h4>
        <h4>Status: {task.taskStatus.replaceAll("_", " ")}</h4>
        <p>Difficulty: {task.difficulty}</p>
        <p>{task.description}</p>
        <p>{auth?.username === task.owner.username
          ? "You are the task owner"
          : `Owner: ${task.owner.username}`
        }</p>
        {(task.taskStatus !== "DONE" && task.taskStatus !== "FAILED") ?
          <Link to={`/companies/${companyId}/projects/${projectId}/tasks/update/${taskId}`}>
            Update task details
          </Link> : <></>}
        <div>
          <BackButton path={`/companies/${companyId}`}/>
        </div>
      </StyledPaper>
    </Container>
  );
}

export default TaskDetails;

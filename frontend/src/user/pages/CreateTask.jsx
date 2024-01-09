import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import useAuthFetch from "../../api/useAuthFetch.js";
import TaskCreateUpdateForm from "../components/TaskCreateUpdateForm.jsx";

function CreateTask() {
  const authFetch = useAuthFetch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [nameError, setNameError] = useState(null);
  const [descriptionError, setDescriptionError] = useState(null);
  const [startDateError, setStartDateError] = useState(null);
  const [deadlineError, setDeadlineError] = useState(null);
  const [importanceOptions, setImportanceOptions] = useState([]);
  const [taskStatusOptions, setTaskStatusOptions] = useState([]);

  const {companyId, projectId} = useParams();

  useEffect(() => {
    async function loadSelectorOptions() {
      try {
        setLoading(true);
        //TODO: fetch from backend
        setImportanceOptions(["MUST_HAVE", "NICE_TO_HAVE"]);
        setTaskStatusOptions(["BACKLOG", "IN_PROGRESS"]);
      } catch (e) {
        console.error(e);
        throw new Error("Failed to load options for task creation");
      } finally {
        setLoading(false);
      }
    }

    loadSelectorOptions();
  }, []);

  async function handleSubmit(event) {
    try {
      event.preventDefault();

      const {
        name, description, importance, difficulty, startDate, deadline, taskStatus
      } = Object.fromEntries(new FormData(event.target).entries());

      const validInput = validateInput(name, description, startDate, deadline);

      if (validInput) {
        await handleCreateTask(name, description, importance, difficulty, startDate, deadline, taskStatus);
      }
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to create task");
    }
  }

  function validateInput(name, description, startDate, deadline) {
    let validInput = true;
    if (!name || !description || !startDate || !deadline) {
      setErrorMessage("All fields are required");
      validInput = false;
    } else {
      const startDateObj = new Date(startDate);
      const deadlineObj = new Date(deadline);
      if (startDateObj >= deadlineObj) {
        setErrorMessage("Deadline must be after start date");
        validInput = false;
      }
      // TODO: add regex for name and description
    }
    return validInput;
  }

  async function handleCreateTask(name, description, importance, difficulty, startDate, deadline, taskStatus) {
    try {
      setLoading(true);
      const {
        httpResponse, responseObject
      } = await authFetch("POST", `companies/${companyId}/projects/${projectId}/tasks`, {
        name, description, importance, difficulty, startDate, deadline, taskStatus
      });
      if (httpResponse?.status !== 201 || !responseObject?.data?.taskId) {
        setErrorMessage(responseObject?.error ?? "Failed to create task");
        return;
      }
      navigate(`/companies/${companyId}/projects/${projectId}/tasks/${responseObject.data.taskId}`);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to create task");
    } finally {
      setLoading(false);
    }
  }

  return loading ? <LoadingSpinner/> : <TaskCreateUpdateForm
    onSubmit={handleSubmit}
    importanceOptions={importanceOptions}
    taskStatusOptions={taskStatusOptions}
    errorMessage={errorMessage}
    nameError={nameError}
    descriptionError={descriptionError}
    startDateError={startDateError}
    deadlineError={deadlineError}
  />;
}

export default CreateTask;

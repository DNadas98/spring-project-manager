import React, {useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import useAuthFetch from "../../api/useAuthFetch.js";
import ProjectCreateUpdateForm from "../components/ProjectCreateUpdateForm.jsx";

function CreateProject() {
  const authFetch = useAuthFetch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [nameError, setNameError] = useState(null);
  const [descriptionError, setDescriptionError] = useState(null);
  const [startDateError, setStartDateError] = useState(null);
  const [deadlineError, setDeadlineError] = useState(null);

  const companyId = useParams().companyId;

  async function handleSubmit(event) {
    try {
      event.preventDefault();

      const {name, description, startDate, deadline} = Object.fromEntries(new FormData(event.target).entries());

      const validInput = validateInput(name, description, startDate, deadline);

      if (validInput) {
        await handleCreateProject(name, description, startDate, deadline);
      }
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to create company");
    }
  }

  function validateInput(name, description, startDate, deadline) {
    let validInput = true;
    if (!name || !description || !startDate || !deadline) {
      setErrorMessage("All fields are required!");
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

  async function handleCreateProject(name, description, startDate, deadline) {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("POST", `companies/${companyId}/projects`, {
        "name": name, "description": description, "startDate": startDate, "deadline": deadline
      });
      if (httpResponse?.status !== 201 || !responseObject?.data?.projectId) {
        setErrorMessage(responseObject?.error ?? "Failed to create project");
        return;
      }
      navigate(`/companies/${companyId}/projects/${responseObject.data.projectId}`);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to create project");
    } finally {
      setLoading(false);
    }
  }

  return loading ? <LoadingSpinner/> : <ProjectCreateUpdateForm
    onSubmit={handleSubmit}
    errorMessage={errorMessage}
    nameError={nameError}
    descriptionError={descriptionError}
    startDateError={startDateError}
    deadlineError={deadlineError}
  />;
}

export default CreateProject;

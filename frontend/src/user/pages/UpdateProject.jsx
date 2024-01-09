import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import useAuthFetch from "../../api/useAuthFetch.js";
import BackButton from "../../components/BackButton.jsx";
import ProjectCreateUpdateForm from "../components/ProjectCreateUpdateForm.jsx";

function UpdateProject() {
  const authFetch = useAuthFetch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState(null);
  const [nameError, setNameError] = useState(null);
  const [descriptionError, setDescriptionError] = useState(null);
  const [startDateError, setStartDateError] = useState(null);
  const [deadlineError, setDeadlineError] = useState(null);

  const companyId = useParams().companyId;
  const projectId = useParams().projectId;
  const [project, setProject] = useState(null);

  async function loadProject() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("GET", `companies/${companyId}/projects/${projectId}`);
      if (httpResponse?.status !== 200 || !responseObject?.data?.projectId) {
        setErrorMessage(responseObject?.error ?? "Failed to load project");
        return;
      }
      setProject(responseObject.data);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load project");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadProject();
  }, []);

  async function handleSubmit(event) {
    try {
      event.preventDefault();
      const {name, description, startDate, deadline} = Object.fromEntries(new FormData(event.target).entries());

      const validInput = validateInput(name, description, startDate, deadline);

      if (validInput) {
        await handleUpdateCompany(name, description, startDate, deadline);
      }
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to update project");
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

  async function handleUpdateCompany(name, description, startDate, deadline) {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("PUT", `companies/${companyId}/projects/${projectId}`, {
        "name": name, "description": description, "startDate": startDate, "deadline": deadline
      });
      if (httpResponse?.status !== 200 || !responseObject?.data?.projectId) {
        setErrorMessage(responseObject?.error ?? "Failed to update project");
        return;
      }
      navigate(`/companies/${companyId}/projects/${responseObject.data.projectId}`);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to update project");
    } finally {
      setLoading(false);
    }
  }

  return loading ? <LoadingSpinner/> : project
    ? <ProjectCreateUpdateForm
      onSubmit={handleSubmit}
      project={project}
      errorMessage={errorMessage}
      nameError={nameError}
      descriptionError={descriptionError}
      startDateError={startDateError}
      deadlineError={deadlineError}
    />
    : <>
      {errorMessage && <p>{errorMessage}</p>}
      <BackButton/>
    </>;
}

export default UpdateProject;

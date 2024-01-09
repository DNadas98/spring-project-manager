import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import useAuthFetch from "../../api/useAuthFetch.js";
import CompanyCreateUpdateForm from "../components/CompanyCreateUpdateForm.jsx";

function CreateCompany() {
  const authFetch = useAuthFetch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [nameError, setNameError] = useState(null);
  const [descriptionError, setDescriptionError] = useState(null);

  async function handleSubmit(event) {
    try {
      event.preventDefault();

      const {name, description} = Object.fromEntries(new FormData(event.target).entries());

      const validInput = validateInput(name, description);

      if (validInput) {
        await handleCreateCompany(name, description);
      }
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to create company");
    }
  }

  function validateInput(name, description) {
    let validInput = true;
    if (!name || !description) {
      setErrorMessage("All fields are required!");
      validInput = false;
    } else {
      //TODO: add regex for name, description, set errors
    }
    return validInput;
  }

  async function handleCreateCompany(name, description) {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("POST", "companies", {
        "name": name, "description": description
      });
      if (httpResponse?.status !== 201 || !responseObject?.data?.companyId) {
        setErrorMessage(responseObject?.error ?? "Failed to create company");
        return;
      }
      navigate(`/companies/${responseObject.data.companyId}`);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to create company");
    } finally {
      setLoading(false);
    }
  }

  return loading ? <LoadingSpinner/> : <CompanyCreateUpdateForm
    onSubmit={handleSubmit}
    errorMessage={errorMessage}
    nameError={nameError}
    descriptionError={descriptionError}
  />;
}

export default CreateCompany;

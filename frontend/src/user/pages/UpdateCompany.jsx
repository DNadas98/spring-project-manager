import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import useAuthFetch from "../../api/useAuthFetch.js";
import CompanyCreateUpdateForm from "../components/CompanyCreateUpdateForm.jsx";
import BackButton from "../../components/BackButton.jsx";

function UpdateCompany() {
  const authFetch = useAuthFetch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState(null);
  const [nameError, setNameError] = useState(null);
  const [descriptionError, setDescriptionError] = useState(null);

  const companyId = useParams().companyId;
  const [company, setCompany] = useState(null);

  async function loadCompany() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("GET", `companies/${companyId}`);
      if (httpResponse?.status !== 200 || !responseObject?.data?.companyId) {
        setErrorMessage(responseObject?.error ?? "Failed to load company");
        return;
      }
      setCompany(responseObject.data);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to load company");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadCompany();
  }, []);

  async function handleSubmit(event) {
    try {
      event.preventDefault();

      const {name, description} = Object.fromEntries(new FormData(event.target).entries());

      const validInput = validateInput(name, description);

      if (validInput) {
        await handleUpdateCompany(name, description);
      }
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to update company");
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

  async function handleUpdateCompany(name, description) {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("PUT", `companies/${companyId}`, {
        "name": name, "description": description
      });
      if (httpResponse?.status !== 200 || !responseObject?.data?.companyId) {
        setErrorMessage(responseObject?.error ?? "Failed to update company");
        return;
      }
      navigate(`/companies/${responseObject.data.companyId}`);
    } catch (e) {
      console.error(e);
      setErrorMessage("Failed to update company");
    } finally {
      setLoading(false);
    }
  }

  return loading ? <LoadingSpinner/> : company
    ? <CompanyCreateUpdateForm
      onSubmit={handleSubmit}
      company={company}
      errorMessage={errorMessage}
      nameError={nameError}
      descriptionError={descriptionError}
    />
    : <>
      {errorMessage && <p>{errorMessage}</p>}
      <BackButton/>
    </>;
}

export default UpdateCompany;

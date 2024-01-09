import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import {emailRegex, passwordRegex, usernameRegex} from "../../regex/regex.js";
import RegisterForm from "../components/RegisterForm.jsx";
import {publicFetch} from "../../api/publicFetch.js";

function Register() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [resultMessage, setResultMessage] = useState(null);
  const [usernameError, setUsernameError] = useState(false);
  const [emailError, setEmailError] = useState(false);
  const [passwordError, setPasswordError] = useState(false);

  async function handleSubmit(event) {
    try {
      event.preventDefault();
      setResultMessage("");
      const {username, email, password, confirmPassword} = Object.fromEntries(new FormData(event.target).entries());
      const validInput = validateInput(username, email, password, confirmPassword);
      if (validInput) {
        const confirmed = window.confirm(`Create user account "${username}"?\nYou will be redirected to login`);
        if (confirmed) {
          return await handleRegister(username, email, password);
        } else {
          return;
        }
      }
    } catch (e) {
      console.error(e);
      setResultMessage("Failed to create user");
    }
  }

  function validateInput(username, email, password, confirmPassword) {
    let validInput = true;
    if (!username || !email || !password || !confirmPassword) {
      setResultMessage("All fields are required!");
      validInput = false;
    } else if (password !== confirmPassword) {
      setResultMessage("Passwords don't match");
      validInput = false;
    } else {
      if (!usernameRegex.test(username)) {
        setUsernameError(true);
        validInput = false;
      } else {
        setUsernameError(false);
      }
      if (!emailRegex.test(email)) {
        setEmailError(true);
        validInput = false;
      } else {
        setEmailError(false);
      }
      if (!passwordRegex.test(password)) {
        setPasswordError(true);
        validInput = false;
      } else {
        setPasswordError(false);
      }
    }
    return validInput;
  }

  async function handleRegister(username, email, password) {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await publicFetch("auth/register", "POST", {
        "username": username, "email": email, "password": password
      });
      setResultMessage(responseObject.message ?? responseObject.error);
      if (httpResponse.status === 201) {
        navigate("/login");
      }
    } catch (e) {
      setResultMessage("Failed to create user account");
    } finally {
      setLoading(false);
    }
  }

  return loading ? <LoadingSpinner/> : <RegisterForm
    onSubmit={handleSubmit}
    errorMessage={resultMessage}
    usernameError={usernameError}
    emailError={emailError}
    passwordError={passwordError}
  />;
}

export default Register;

import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import useAuth from "../../auth/hooks/useAuth.js";
import {emailRegex, passwordRegex} from "../../regex/regex";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import useAuthFetch from "../../api/useAuthFetch.js";
import LoginForm from "../components/LoginForm.jsx";

function Login() {
  const {setAuth} = useAuth();
  const authFetch = useAuthFetch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [emailError, setEmailError] = useState(false);
  const [passwordError, setPasswordError] = useState(false);

  async function handleSubmit(event) {
    try {
      event.preventDefault();

      const {email, password} = Object.fromEntries(new FormData(event.target).entries());

      const validInput = validateInput(email, password);

      if (validInput) {
        handleLogin(email, password);
      }
    } catch (e) {
      console.error(e);
      setErrorMessage("Login failed");
    }
  }

  function validateInput(email, password) {
    let validInput = true;
    if (!email || !password) {
      setErrorMessage("All fields are required!");
      validInput = false;
    } else {
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

  async function handleLogin(email, password) {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("POST", "auth/login", {
        "email": email, "password": password
      });
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        setErrorMessage(responseObject?.error ?? "Failed to log in");
        return;
      }
      const accessToken = responseObject.data?.accessToken;
      const receivedUsername = responseObject.data?.userInfo?.username;
      const receivedEmail = responseObject.data?.userInfo?.email;
      const receivedRoles = responseObject.data?.userInfo?.roles;
      if (accessToken && receivedUsername && receivedEmail && receivedRoles) {
        setAuth({
          "username": receivedUsername, "email": receivedEmail, "roles": receivedRoles, "accessToken": accessToken
        });
        navigate("/companies");
      } else {
        throw new Error("Login failed");
      }
    } catch (e) {
      console.error(e);
      setErrorMessage("Login failed");
    } finally {
      setLoading(false);
    }
  }

  return loading ? <LoadingSpinner/> : <LoginForm
    onSubmit={handleSubmit}
    errorMessage={errorMessage}
    emailError={emailError}
    passwordError={passwordError}
  />;
}

export default Login;

import React from "react";
import {useLocation, useNavigate} from "react-router-dom";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";
import Container from "@mui/material/Container";
import {styled} from "@mui/material/styles";
import Typography from "@mui/material/Typography";
import BackButton from "../../components/BackButton.jsx";
import {publicFetch} from "../../api/publicFetch.js";

const StyledDiv = styled("div")(({theme}) => ({
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF",
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2)
}));

const StyledButton = styled(Button)(({theme}) => ({
  marginRight: theme.spacing(2), color: "inherit"
}));

function RegisterVerificationRedirect() {
  const location = useLocation();
  const navigate = useNavigate();

  async function verifyRegistration() {
    try {
      const params = new URLSearchParams(location.search);
      const code = params.get("code");
      const id = params.get("id");
      const {
        httpResponse,
        responseObject
      } = await publicFetch(`auth/verify-registration?code=${code}&id=${id}`, "POST");
      if (httpResponse?.status === 201 && responseObject?.message) {
        window.alert("Verification successful! Redirecting to login");
        return navigate("/login");
      } else {
        window.alert(responseObject?.error ?? "Failed to create user account");
        return navigate("/register");
      }
    } catch (error) {
      alert("Failed to create user account");
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline/>
      <StyledDiv>
        <Typography component="h1" variant="h5">
          Click the button below to verify your e-mail address and activate your account.
        </Typography>
        <StyledButton
          type="button"
          variant="contained"
          sx={{mt: 3, mb: 2}}
          onClick={verifyRegistration}
        >
          Verify
        </StyledButton>
        <BackButton path={"/login"} text={"Cancel"}/>
      </StyledDiv>
    </Container>);
}

export default RegisterVerificationRedirect;

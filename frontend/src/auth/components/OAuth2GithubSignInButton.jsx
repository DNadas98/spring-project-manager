import React from "react";
import Button from "@mui/material/Button";
import {styled} from "@mui/material/styles";

const StyledButton = styled(Button)(({theme}) => ({
  marginRight: theme.spacing(2),
  color: "inherit"
}));

function OAuth2GithubSignInButton() {
  return (
    <a href={`${import.meta.env.VITE_OAUTH2_AUTHORIZATION_URL}/github`}>
      <StyledButton
        type="button"
        fullWidth
        variant="contained"
        sx={{mt: 3, mb: 2}}
      >
        Sign In with GitHub
      </StyledButton>
    </a>);
}

export default OAuth2GithubSignInButton;

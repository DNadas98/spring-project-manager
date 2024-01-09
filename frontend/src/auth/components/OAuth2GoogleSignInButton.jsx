import React from "react";
import Button from "@mui/material/Button";
import {styled} from "@mui/material/styles";

const StyledButton = styled(Button)(({theme}) => ({
  marginRight: theme.spacing(2),
  color: "inherit"
}));

function OAuth2GoogleSignInButton() {
  return (
    <a href={`${import.meta.env.VITE_OAUTH2_AUTHORIZATION_URL}/google`}>
      <StyledButton
        type="button"
        fullWidth
        variant="contained"
        sx={{mt: 3, mb: 2}}
      >
        Sign In with Google
      </StyledButton>
    </a>);
}

export default OAuth2GoogleSignInButton;

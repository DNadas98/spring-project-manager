import React, {useEffect, useState} from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Box from "@mui/material/Box";
import Avatar from "@mui/material/Avatar";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Container from "@mui/material/Container";
import Collapse from "@mui/material/Collapse";
import {styled} from "@mui/material/styles";
import OAuth2GoogleSignInButton from "./OAuth2GoogleSignInButton.jsx";

const StyledDiv = styled("div")(({theme}) => ({
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF",
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2)
}));

const StyledButton = styled(Button)(({theme}) => ({
  marginRight: theme.spacing(2),
  color: "inherit"
}));

function RegisterForm({
                        onSubmit,
                        errorMessage,
                        usernameError,
                        emailError,
                        passwordError
                      }) {
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    setChecked(true);
  }, []);

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline/>
      <StyledDiv>
        <Avatar sx={{m: 1, bgcolor: "secondary.main"}}>
          <LockOutlinedIcon/>
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign up
        </Typography>
        {usernameError && (
          <Typography component="h5" variant="h6" color="darkred">
            Enter 3-20 characters (letters, numbers, or underscores)
          </Typography>
        )}
        {emailError && (
          <Typography component="h5" variant="h6" color="darkred">
            Enter a valid email address
          </Typography>
        )}
        {passwordError && (
          <Typography component="h5" variant="h6" color="darkred">
            Password must be 8-30 characters
          </Typography>
        )}
        {errorMessage && (
          <Typography component="h5" variant="h6" color="darkred">
            {errorMessage}
          </Typography>
        )}
        <Collapse in={checked} {...(checked ? {timeout: 1000} : {})}>
          <Box component="form" onSubmit={onSubmit} sx={{mt: 1}}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              minLength={3}
              maxLength={20}
              autoFocus
              error={usernameError}
              helperText={usernameError && usernameError}
              InputProps={{style: {color: "white"}}}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              type="email"
              error={emailError}
              helperText={emailError && emailError}
              InputProps={{style: {color: "white"}}}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="new-password"
              error={passwordError}
              helperText={passwordError && passwordError}
              InputProps={{style: {color: "white"}}}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="confirmPassword"
              label="Confirm Password"
              type="password"
              id="confirmPassword"
              autoComplete="off"
              InputProps={{style: {color: "white"}}}
            />
            <StyledButton
              type="submit"
              fullWidth
              variant="contained"
              sx={{mt: 3, mb: 2}}
            >
              Sign Up
            </StyledButton>
            <OAuth2GoogleSignInButton/>
          </Box>
        </Collapse>
      </StyledDiv>
    </Container>
  );
}

export default RegisterForm;

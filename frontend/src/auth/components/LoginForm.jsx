import React, {useEffect, useState} from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import {styled} from "@mui/material/styles";
import Collapse from "@mui/material/Collapse";
import Popover from "@mui/material/Popover";
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

function LoginForm({onSubmit, errorMessage, emailError, passwordError}) {
  const [checked, setChecked] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);

  useEffect(() => {
    setChecked(true);
  }, []);

  const handlePopoverClose = () => {
    setAnchorEl(null);
  };

  const openPopover = Boolean(anchorEl);

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline/>
      <StyledDiv>
        <Avatar sx={{m: 1, bgcolor: "secondary.main"}}>
          <LockOutlinedIcon/>
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign in
        </Typography>
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
        <Box component="form" onSubmit={onSubmit} sx={{mt: 1}}>
          <Collapse in={checked} {...(checked ? {timeout: 1000} : {})}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              minLength={5}
              maxLength={100}
              autoFocus
              error={emailError ? true : false}
              helperText={emailError && "Enter a vaild e-mail address"}
              onFocus={() => setAnchorEl(null)}
              InputProps={{style: {color: "white"}}}
            />
          </Collapse>
          <Collapse in={checked} {...(checked ? {timeout: 1000} : {})}>
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              error={passwordError ? true : false}
              helperText={passwordError && "Enter 8-30 characters"}
              onFocus={() => setAnchorEl(null)}
              InputProps={{style: {color: "white"}}}
            />
          </Collapse>
          <StyledButton
            type="submit"
            fullWidth
            variant="contained"
            sx={{mt: 3, mb: 2}}
          >
            Sign In
          </StyledButton>
          <Grid container>
            <Grid item>
              <Link href="/register" variant="body2">
                <Typography>{"Don't have an account? Sign Up"}</Typography>
              </Link>
            </Grid>
          </Grid>
          <OAuth2GoogleSignInButton/>
        </Box>
      </StyledDiv>
      <Popover
        open={openPopover}
        anchorEl={anchorEl}
        onClose={handlePopoverClose}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "left"
        }}
        transformOrigin={{
          vertical: "top",
          horizontal: "left"
        }}
      >
        <Typography sx={{padding: 2, color: "darkred"}}>
          Invalid input. Please check your email and password.
        </Typography>
      </Popover>
    </Container>
  );
}

export default LoginForm;

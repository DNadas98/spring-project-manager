import React from "react";
import {Outlet} from "react-router-dom";
import NavButton from "../../components/NavButton.jsx";
import useAuth from "../../auth/hooks/useAuth.js";
import {ThemeProvider} from "@mui/material/styles";
import theme from "../../index.jsx";
import CssBaseline from "@mui/material/CssBaseline";
import {Button, Container, Grid, Typography} from "@mui/material";
import {styled} from "@mui/system";
import useLogout from "../../auth/hooks/useLogout.js";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import {AccountCircle} from "@mui/icons-material";

const StyledContainer = styled(Container)(({theme}) => ({
  marginTop: theme.spacing(4),
  marginBottom: theme.spacing(4),
}));

function UserLayout() {
  const {auth} = useAuth();
  const logout = useLogout();

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline/>
      <AppBar position="static">
        <Toolbar sx={{flexGrow: 1}}>
          <AccountCircle sx={{marginRight: 1}}/>
          <Typography variant={"h6"}>Logged in as {auth.username}</Typography>
          <NavButton path="/companies" text="Company Browser"/>
          <NavButton path="/user" text="User Dashboard"/>
          <Button variant="contained" color={"primary"} sx={{marginLeft: 2}}
                  onClick={async () => {
                    await logout(true);
                  }}
          >
            Logout
          </Button>
        </Toolbar>
      </AppBar>
      <StyledContainer>
        <Grid container spacing={4} justifyContent="center">
          <Grid item xs={12} md={12} lg={12}>
            <Outlet className="Outlet"/>
          </Grid>
        </Grid>
      </StyledContainer>
    </ThemeProvider>
  );
}

export default UserLayout;

import {Link as RouterLink, Outlet} from "react-router-dom";
import {AppBar, Button, Container, Grid, Toolbar, Typography} from "@mui/material";
import {styled} from "@mui/system";

const StyledContainer = styled(Container)(({theme}) => ({
  marginTop: theme.spacing(4),
  marginBottom: theme.spacing(4),
}));

function Layout() {
  return (
    <>
      <AppBar position="static">
        <Toolbar sx={{flexGrow: 1}}>
          <Typography variant={"h5"} sx={{marginRight: 3}}>Project Manager</Typography>
          <Button component={RouterLink} to="/" color="inherit">
            Home
          </Button>
          <Button component={RouterLink} to="/login" color="inherit">
            Sign in
          </Button>
          <Button component={RouterLink} to="/register" color="inherit">
            Sign up
          </Button>
        </Toolbar>
      </AppBar>
      <StyledContainer>
        <Grid container spacing={4} justifyContent="center">
          <Grid item xs={12} md={12} lg={12}>
            <Outlet/>
          </Grid>
        </Grid>
      </StyledContainer>
    </>);
}

export default Layout;

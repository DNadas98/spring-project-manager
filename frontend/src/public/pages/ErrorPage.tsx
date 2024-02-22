import {Button, Grid, Typography} from "@mui/material";
import {useRouteError} from "react-router-dom";
import BackButton from "../../common/components/BackButton.tsx";

function ErrorPage() {
  const error: any = useRouteError();
  console.clear();
  console.error("Error: ", error?.message ? error.message : "An unknown error has occurred");

  return (
    <Grid container minHeight="100vh" minWidth={"100%"} textAlign={"left"}
          alignItems="center" justifyContent="center">
      <Grid item>
        <Typography variant="h4" gutterBottom>
          An error has occurred.
        </Typography>
        <Typography variant="h6" gutterBottom>
          Return to the homepage or try again later.
        </Typography>
        <Typography>
          If the issue persists, please contact our support team.
        </Typography>
        <Grid container spacing={1} mt={1} textAlign={"left"}>
          <Grid item>
            <Button href="/" type="button" variant={"contained"}>
              Home
            </Button>
          </Grid>
          <Grid item>
            <BackButton/>
          </Grid>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default ErrorPage;

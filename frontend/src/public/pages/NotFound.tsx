import BackButton from "../../common/components/BackButton.tsx";
import {Grid, Typography} from "@mui/material";

function NotFound() {
  return (
    <Grid container justifyContent="center">
      <Grid item justifyContent="center">
        <Typography variant="h6">
          The page you are looking for does not exist.
        </Typography>
        <Grid container spacing={1} mt={1} textAlign={"left"}>
          <Grid item>
            <BackButton path={"/"} text={"Home"}/>
          </Grid>
          <Grid item>
            <BackButton/>
          </Grid>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default NotFound;

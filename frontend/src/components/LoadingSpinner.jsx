import ClipLoader from "react-spinners/ClipLoader";
import "./LoadingSpinner.css";
import {Grid} from "@mui/material";

function LoadingSpinner() {
  return (
    <Grid container spacing={4} sx={{marginTop:2}} justifyContent="center" alignItems="center">
      <ClipLoader size={80} color={"aliceblue"}/>
    </Grid>
  );
}

export default LoadingSpinner;

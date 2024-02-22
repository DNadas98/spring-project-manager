import {Container, Grid, Typography, useMediaQuery, useTheme} from "@mui/material";
import MUIExampleForm from "./MUIExampleForm.tsx";
import MUIExampleCard from "./MUIExampleCard.tsx";
import MUIExampleDialog from "./MUIExampleDialog.tsx";

export default function MUIExample() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  return (
    <Container>
      <Grid container justifyContent="center" spacing={2} mb={4}>
        {Array.from({length: 3}, (_, i) => {
          return (
            <Grid item xs={10} sm={8} md={6} lg={4} key={i}>
              <MUIExampleCard/>
            </Grid>
          );
        })}
      </Grid>
      <Grid container justifyContent="center" spacing={2} mb={4}>
        <Grid item xs={10}>
          <Typography textAlign="justify" mb={2}>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ac
            magna
            libero. Proin vel lacus vitae massa euismod tincidunt eget ut dui. Sed
            id
            sem a risus fermentum eleifend congue vitae sem. Donec faucibus lorem
            id
            eros scelerisque congue. Praesent vehicula libero vitae purus mollis
            molestie ac at lectus. Praesent ornare ipsum sit amet augue posuere
            venenatis. Curabitur volutpat vitae enim eget rhoncus. Duis venenatis
            venenatis est, tincidunt euismod enim fermentum sed. Nam eu nulla
            metus.
          </Typography>
        </Grid>
        <Grid container justifyContent="center" spacing={2} mb={4}>
          <Grid item>
            <MUIExampleDialog isMobile={isMobile}/>
          </Grid>
          <Grid item>
            <MUIExampleDialog isMobile={isMobile}/>
          </Grid>
        </Grid>
      </Grid>
      <Grid container justifyContent="center" spacing={2} mb={2}>
        <Grid item xs={10} sm={8} md={6} lg={4}
              sx={{backgroundColor: `${theme.palette.background.paper}`}} padding={2}>
          <MUIExampleForm/>
        </Grid>
      </Grid>
    </Container>
  );
}

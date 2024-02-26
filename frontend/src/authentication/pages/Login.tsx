import {Card, CardActions, CardContent, Grid, Stack, Typography} from "@mui/material";
import OAuth2GoogleSignInButton from "../components/OAuth2GoogleSignInButton.tsx";
import OAuth2FacebookSignInButton from "../components/OAuth2FacebookSignInButton.tsx";
import OAuth2GithubSignInButton from "../components/OAuth2GithubSignInButton.tsx";

export default function Login() {
  return (
    <Grid container justifyContent={"center"}>
      <Grid item xs={10} sm={8} md={7} lg={6}>
        <Card sx={{paddingTop: 4}}>
          <CardContent sx={{justifyContent: "center", textAlign: "center"}}>
            <Typography variant="h4" gutterBottom>
              Sign in
            </Typography>
            <CardActions sx={{justifyContent: "center"}}>
              <Stack spacing={2}>
                <OAuth2GoogleSignInButton/>
                <OAuth2FacebookSignInButton/>
                <OAuth2GithubSignInButton/>
              </Stack>
            </CardActions>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  )
}

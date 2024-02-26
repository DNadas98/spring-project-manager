import {useAuthentication} from "../../authentication/hooks/useAuthentication.ts";
import {Card, CardContent, CardHeader, Grid, Stack, Typography} from "@mui/material";
import {AccountCircle} from "@mui/icons-material";

export default function Profile() {
  const authentication = useAuthentication();
  const username = authentication.getUsername();
  const roles = authentication.getRoles();
  const email = authentication.getEmail();
  return (
    <Grid container justifyContent={"center"}>
      <Grid item xs={10} sm={6} md={4} lg={3}>
        <Card sx={{paddingTop: 4}}>
          <CardHeader avatar={<AccountCircle color={"secondary"}/>}
                      title={username} titleTypographyProps={{"variant": "h6"}}
                      subtitle={email}>
          </CardHeader>
          <CardContent>
            <Stack spacing={2}>
              <Typography variant={"h6"}>
                E-mail address:
              </Typography>
              <Typography>
                {email}
              </Typography>
              <Typography variant={"h6"}>
                Roles:
              </Typography>
              <Grid container>
                {roles?.length
                  ? roles.map((role) => (
                    <Grid item key={role}>
                      <Typography>
                        {role}
                      </Typography>
                    </Grid>
                  ))
                  : <Typography>
                    No roles found
                  </Typography>}
              </Grid>
            </Stack>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  )
}

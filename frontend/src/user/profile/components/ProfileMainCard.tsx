import {Card, CardContent, CardHeader, Grid, Stack, Typography} from "@mui/material";
import {AccountBoxRounded} from "@mui/icons-material";
import {GlobalRole} from "../../../authentication/dto/userInfo/GlobalRole.ts";

interface ProfileMainCardProps {
  username: string;
  email: string;
  roles: GlobalRole[]
}

export default function ProfileMainCard(props: ProfileMainCardProps) {
  return (

    <Grid container justifyContent={"center"}>
      <Grid item xs={10} sm={6} md={4} lg={3}>
        <Card sx={{paddingTop: 4}}>
          <CardHeader avatar={<AccountBoxRounded color={"secondary"}/>}
                      title={props.username} titleTypographyProps={{"variant": "h6"}}
                      subtitle={props.email}>
          </CardHeader>
          <CardContent>
            <Stack spacing={2}>
              <Typography variant={"h6"}>
                E-mail address:
              </Typography>
              <Typography>
                {props.email}
              </Typography>
              <Typography variant={"h6"}>
                Roles:
              </Typography>
              <Grid container>
                {props.roles?.length
                  ? props.roles.map((role) => (
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

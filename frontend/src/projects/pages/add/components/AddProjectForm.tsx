import React from "react";
import {Avatar, Button, Card, CardContent, Grid, Stack, Typography} from "@mui/material";
import {DomainAddOutlined} from "@mui/icons-material";
import ProjectNameInput from "../../../components/ProjectNameInput.tsx";
import ProjectDescriptionInput from "../../../components/ProjectDescriptionInput.tsx";
import StartDateInput from "../../../../common/utils/components/StartDateInput.tsx";
import DeadlineInput from "../../../../common/utils/components/DeadlineInput.tsx";

interface AddCompanyFormProps {
  onSubmit: (event: React.FormEvent<HTMLFormElement>) => Promise<void>
}

export default function AddProjectForm(props: AddCompanyFormProps) {
  return (
    <Grid container justifyContent={"center"}>
      <Grid item xs={10} sm={8} md={7} lg={6}>
        <Card sx={{
          paddingTop: 4, textAlign: "center",
          maxWidth: 800, width: "100%",
          marginLeft: "auto", marginRight: "auto"
        }}>
          <Stack
            spacing={2}
            alignItems={"center"}
            justifyContent={"center"}>
            <Avatar variant={"rounded"}
                    sx={{backgroundColor: "secondary.main"}}>
              <DomainAddOutlined/>
            </Avatar>
            <Typography variant="h5" gutterBottom>
              Add new project
            </Typography>
          </Stack>
          <CardContent sx={{justifyContent: "center", textAlign: "center"}}>
            <Grid container sx={{
              justifyContent: "center",
              alignItems: "center",
              textAlign: "center",
              gap: "2rem"
            }}>
              <Grid item xs={10} sm={9} md={7} lg={6}
                    sx={{borderColor: "secondary.main"}}>
                <form onSubmit={props.onSubmit}>
                  <Stack spacing={2}>
                    <ProjectNameInput/>
                    <ProjectDescriptionInput/>
                    <StartDateInput/>
                    <DeadlineInput/>
                    <Button type={"submit"}
                            variant={"contained"}>
                      Add Project
                    </Button>
                  </Stack>
                </form>
              </Grid>
            </Grid>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  )
}

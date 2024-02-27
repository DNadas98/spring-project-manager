import {CompanyResponsePublicDto} from "../../../dto/CompanyResponsePublicDto.ts";
import {Card, CardHeader, Grid, Stack} from "@mui/material";
import CompanyList from "./CompanyList.tsx";

interface CompanyBrowserProps {
  companiesWithUserLoading: boolean;
  companiesWithUser: CompanyResponsePublicDto[];
  companiesWithoutUserLoading: boolean;
  companiesWithoutUser: CompanyResponsePublicDto[];
}

export default function CompanyBrowser(props: CompanyBrowserProps) {
  return (
    <Grid container spacing={2} justifyContent={"center"} alignItems={"center"}>
      <Grid item xs={10} sm={8} md={5} lg={4}>
        <Stack spacing={2}>
          <Card>
            <CardHeader title={"Your companies"} sx={{textAlign: "center"}}/>
          </Card>
          <CompanyList loading={props.companiesWithUserLoading}
                       companies={props.companiesWithUser}
                       notFoundText={"Add a new company or join one to continue!"}
                       userIsMember={true}/>
        </Stack>
      </Grid>
      <Grid item xs={10} sm={8} md={5} lg={4}>
        <Stack spacing={2}>
          <Card>
            <CardHeader title={"Companies to join"} sx={{textAlign: "center"}}/>
          </Card>
          <CompanyList loading={props.companiesWithoutUserLoading}
                       companies={props.companiesWithoutUser}
                       notFoundText={"We haven't found any companies to join."}
                       userIsMember={false}/>
        </Stack>
      </Grid>
    </Grid>
  )
}

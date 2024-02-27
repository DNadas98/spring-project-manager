import {CompanyResponsePublicDto} from "../../../dto/CompanyResponsePublicDto.ts";
import {
  Avatar,
  Card,
  CardContent,
  CardHeader,
  Grid,
  IconButton,
  Stack,
  TextField
} from "@mui/material";
import CompanyList from "./CompanyList.tsx";
import {AddOutlined} from "@mui/icons-material";
import {FormEvent} from "react";

interface CompanyBrowserProps {
  companiesWithUserLoading: boolean;
  companiesWithUser: CompanyResponsePublicDto[];
  companiesWithoutUserLoading: boolean;
  companiesWithoutUser: CompanyResponsePublicDto[];
  handleCompaniesWithUserSearch: (event: FormEvent<HTMLInputElement>) => void;
  handleCompaniesWithoutUserSearch: (event: FormEvent<HTMLInputElement>) => void;
}

export default function CompanyBrowser(props: CompanyBrowserProps) {


  return (
    <Grid container spacing={2} justifyContent={"center"} alignItems={"top"}>
      <Grid item xs={10} sm={8} md={5} lg={4}>
        <Stack spacing={2}>
          <Card>
            <CardHeader title={"Your companies"} sx={{textAlign: "center"}}/>
            <CardContent>
              <Stack direction={"row"} spacing={1} alignItems={"baseline"}>
                <IconButton>
                  <Avatar variant={"rounded"}
                          sx={{bgcolor: "primary.main"}}>
                    <AddOutlined/>
                  </Avatar>
                </IconButton>
                <TextField variant={"standard"} type={"search"}
                           label={"Search"}
                           fullWidth
                           onInput={props.handleCompaniesWithUserSearch}
                />
              </Stack>
            </CardContent>
          </Card>
          <CompanyList loading={props.companiesWithUserLoading}
                       companies={props.companiesWithUser}
                       notFoundText={"We haven't found any companies."}
                       userIsMember={true}/>
        </Stack>
      </Grid>
      <Grid item xs={10} sm={8} md={5} lg={4}>
        <Stack spacing={2}>
          <Card>
            <CardHeader title={"Companies to join"} sx={{textAlign: "center"}}/>
            <CardContent>
              <TextField variant={"standard"} type={"search"} fullWidth
                         sx={{marginBottom: 1}}
                         label={"Search"}
                         onInput={props.handleCompaniesWithoutUserSearch}
              />
            </CardContent>
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

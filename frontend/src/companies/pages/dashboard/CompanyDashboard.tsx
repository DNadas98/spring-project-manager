import {Grid} from "@mui/material";
import {useParams} from "react-router-dom";
import NotFound from "../../../public/pages/errorPages/NotFound.tsx";
import {useEffect, useState} from "react";

interface CompanyDashboardProps {

}

export default function CompanyDashboard(props: CompanyDashboardProps) {
  const params = useParams();
  const [companyId, setCompanyId] = useState<string | undefined>(params?.companyId);

  useEffect(() => {
    const id = params?.companyId;
    if (!id || isNaN(parseInt(id)) || parseInt(id) < 1) {
      setCompanyId(undefined);
    } else {
      setCompanyId(params.companyId);
    }
  }, [params]);

  //

  if (!companyId) {
    return (<NotFound text={"The requested company was not found."}/>)
  }
  return (
    <Grid container justifyContent={"center"} alignItems={"center"}>
      <Grid item xs={10}>
        Company Dashboard, ID: {companyId}
      </Grid>
    </Grid>
  )
}

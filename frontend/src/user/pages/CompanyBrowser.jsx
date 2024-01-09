import React, {useEffect, useState} from "react";
import useAuthFetch from "../../api/useAuthFetch.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import {Link} from "react-router-dom";
import {
  Button, Container, Grid, Paper, Typography, Snackbar, Alert
} from "@mui/material";
import {styled} from "@mui/system";

const StyledPaper = styled(Paper)(({theme}) => ({
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2),
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF"
}));

const StyledTypography = styled(Typography)(({theme}) => ({
  fontWeight: "bold", fontSize: "2rem", color: "#FFFFFF", marginBottom: theme.spacing(2)
}));

function UserDashboard() {
  const authFetch = useAuthFetch();

  const [loading, setLoading] = useState(true);
  const [joinedCompanies, setJoinedCompanies] = useState([]);
  const [companiesToJoin, setCompaniesToJoin] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("error");

  async function loadJoinedCompanies() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("GET", "companies?withUser=true");
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load joined companies");
      }
      setJoinedCompanies(responseObject.data);
    } catch (e) {
      console.error(e);
      setJoinedCompanies([]);
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load joined companies");
      setSnackbarOpen(true);
    } finally {
      setLoading(false);
    }
  }

  async function loadCompaniesToJoin() {
    try {
      setLoading(true);
      const {httpResponse, responseObject} = await authFetch("GET", "companies?withUser=false");
      if (httpResponse?.status !== 200 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load companies to join");
      }
      setCompaniesToJoin(responseObject.data);
    } catch (e) {
      console.error(e);
      setCompaniesToJoin([]);
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to load companies to join");
      setSnackbarOpen(true);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadJoinedCompanies();
    loadCompaniesToJoin();
  }, []);

  async function handleJoinCompany(companyId) {
    try {
      setLoading(true);
      const {
        httpResponse, responseObject
      } = await authFetch("POST", `companies/${companyId}/requests`);
      if (httpResponse?.status !== 201 || !responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to process join request");
      } else {
        const companyName = responseObject?.data?.company?.name;
        const joinRequestStatus = responseObject?.data?.status;
        if (!companyName || !joinRequestStatus) {
          throw new Error("Invalid response received for join request");
        }
        await loadCompaniesToJoin();
        setSnackbarSeverity("success");
        setSnackbarMessage(`Request to join ${companyName} sent successfully`);
        setSnackbarOpen(true);
      }
    } catch (e) {
      setSnackbarSeverity("error");
      setSnackbarMessage(e.message ?? "Failed to process join request");
      setSnackbarOpen(true);
      console.error(e);
    } finally {
      setLoading(false);
    }
  }

  return loading ? (<LoadingSpinner/>) : (<Container>
    <Snackbar
      open={snackbarOpen}
      autoHideDuration={6000}
      onClose={() => setSnackbarOpen(false)}
      anchorOrigin={{vertical: "top", horizontal: "center"}}
    >
      <Alert
        onClose={() => setSnackbarOpen(false)}
        severity={snackbarSeverity}
      >
        {snackbarMessage}
      </Alert>
    </Snackbar>
    <Grid container spacing={4} justifyContent="center">
      <Grid item xs={12} md={12} lg={12}>
        <StyledPaper>
          <StyledTypography variant="h2">Your companies</StyledTypography>
          {joinedCompanies?.length ? (<ul className={"companyList"}>
            {joinedCompanies.map((company) => {
              return (<li className={"companyItem"} key={company?.companyId}>
                <Link
                  to={`/companies/${company.companyId}`}
                  style={{color: "#FFFFFF"}}
                >
                  {company.name}
                </Link>
                <p>Description: {company.description}</p>
              </li>);
            })}
          </ul>) : (<div>
            <p>No companies found</p>
          </div>)}

          <StyledTypography variant="h2">Companies to join</StyledTypography>
          {companiesToJoin?.length ? (<ul className={"companyList"}>
            {companiesToJoin.map((company) => {
              return (<li className={"companyItem"} key={company?.comapnyId}>
                <Button variant={"contained"}
                        onClick={() => {
                          handleJoinCompany(company.companyId);
                        }}
                        style={{color: "#FFFFFF"}}
                >
                  {company.name}
                </Button>
                <p>Description: {company.description}</p>
              </li>);
            })}
          </ul>) : (<p>No companies to join</p>)}

          <Link to={"/companies/create"} style={{color: "#FFFFFF"}}>
            <h3>Create new company</h3>
          </Link>
        </StyledPaper>
      </Grid>
    </Grid>
  </Container>);
}

export default UserDashboard;

import React from "react";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Alert from "@mui/material/Alert";
import Stack from "@mui/material/Stack";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Avatar from "@mui/material/Avatar";
import { styled } from "@mui/material/styles";
import BackButton from "../../components/BackButton.jsx";

const StyledDiv = styled("div")(({ theme }) => ({
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF",
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2),
}));

const StyledButton = styled(Button)(({ theme }) => ({
  marginTop: theme.spacing(3),
  marginBottom: theme.spacing(2),
  color: "inherit",
}));

const StyledLabel = styled(Typography)({
  color: "#FFFFFF",
});

const StyledInput = styled(TextField)({
  "& .MuiInputBase-input": {
    color: "#FFFFFF",
  },
});

function CompanyCreateUpdateForm({
                                   onSubmit,
                                   company,
                                   errorMessage,
                                   nameError,
                                   descriptionError,
                                 }) {
  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <StyledDiv>
        <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          {company ? "Update company details" : "Create a new company"}
        </Typography>
        <Stack spacing={2} sx={{ width: "100%", marginTop: 2 }}>
          {nameError && (
            <Alert severity="error">Enter a valid company name</Alert>
          )}
          {descriptionError && (
            <Alert severity="error">Company description is invalid</Alert>
          )}
          {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
        </Stack>
        <Box component="form" onSubmit={onSubmit} sx={{ mt: 1 }}>
          <StyledLabel htmlFor="name">Company name</StyledLabel>
          <StyledInput
            margin="normal"
            required
            fullWidth
            id="name"
            name="name"
            defaultValue={company?.name ?? ""}
            autoFocus
          />
          <StyledLabel htmlFor="description">Description</StyledLabel>
          <StyledInput
            margin="normal"
            required
            fullWidth
            name="description"
            id="description"
            defaultValue={company?.description ?? ""}
          />
          <StyledButton type="submit" fullWidth variant="contained">
            Save company
          </StyledButton>
        </Box>
        <BackButton fullWidth={true} />
      </StyledDiv>
    </Container>
  );
}

export default CompanyCreateUpdateForm;

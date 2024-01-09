import React, { useState } from "react";
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

function ProjectCreateUpdateForm({
                                   onSubmit,
                                   project,
                                   errorMessage,
                                   nameError,
                                   descriptionError,
                                   startDateError,
                                   deadlineError,
                                 }) {
  const formattedCurrentDateTime = new Date()
    .toISOString()
    .slice(0, 16);

  const [startDate, setStartDate] = useState(
    project ? getFormattedDateTime(project.startDate) : formattedCurrentDateTime
  );

  const [deadline, setDeadline] = useState(
    project ? getFormattedDateTime(project.deadline) : formattedCurrentDateTime
  );

  function getFormattedDateTime(date) {
    return new Date(date).toISOString().slice(0, 16);
  }

  const handleStartDateChange = (e) => {
    setStartDate(e.target.value);
  };

  const handleDeadlineChange = (e) => {
    setDeadline(e.target.value);
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <StyledDiv>
        <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          {project ? "Update project details" : "Create a new project"}
        </Typography>
        <Stack spacing={2} sx={{ width: "100%", marginTop: 2 }}>
          {nameError && <Alert severity="error">Enter a valid project name</Alert>}
          {descriptionError && (
            <Alert severity="error">Project description is invalid</Alert>
          )}
          {startDateError && (
            <Alert severity="error">Project start date is invalid</Alert>
          )}
          {deadlineError && (
            <Alert severity="error">Project deadline date is invalid</Alert>
          )}
          {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
        </Stack>
        <Box component="form" onSubmit={onSubmit} sx={{ mt: 1 }}>
          <StyledLabel htmlFor="name">Project name</StyledLabel>
          <StyledInput
            margin="normal"
            required
            fullWidth
            id="name"
            name="name"
            defaultValue={project?.name ?? ""}
            autoFocus
          />
          <StyledLabel htmlFor="description">Description</StyledLabel>
          <StyledInput
            margin="normal"
            required
            fullWidth
            name="description"
            id="description"
            defaultValue={project?.description ?? ""}
          />
          <StyledLabel htmlFor="startDate">Start date</StyledLabel>
          <StyledInput
            margin="normal"
            type="datetime-local"
            required
            fullWidth
            name="startDate"
            id="startDate"
            value={startDate}
            onChange={handleStartDateChange}
          />
          <StyledLabel htmlFor="deadline">Deadline</StyledLabel>
          <StyledInput
            margin="normal"
            type="datetime-local"
            required
            fullWidth
            name="deadline"
            id="deadline"
            value={deadline}
            onChange={handleDeadlineChange}
          />
          <StyledButton type="submit" fullWidth variant="contained">
            Save project
          </StyledButton>
        </Box>
        <BackButton fullWidth={true} />
      </StyledDiv>
    </Container>
  );
}

export default ProjectCreateUpdateForm;

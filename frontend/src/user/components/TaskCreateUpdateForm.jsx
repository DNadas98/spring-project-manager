import React, {useState} from "react";
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
import {styled} from "@mui/material/styles";
import BackButton from "../../components/BackButton.jsx";

const StyledDiv = styled("div")(({theme}) => ({
  background: "linear-gradient(45deg, #516d79 30%, #11508d 90%)",
  color: "#FFFFFF",
  accentColor: "#FFFFFF",
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2),
}));

const StyledButton = styled(Button)(({theme}) => ({
  marginTop: theme.spacing(3), marginBottom: theme.spacing(2), color: "inherit",
}));

const StyledLabel = styled(Typography)({
  color: "#FFFFFF",
});

const StyledInput = styled(TextField)({
  "& .MuiInputBase-input": {
    color: "#FFFFFF",
  },
});

function TaskCreateUpdateForm({
                                onSubmit,
                                task,
                                importanceOptions,
                                taskStatusOptions,
                                errorMessage,
                                nameError,
                                descriptionError,
                                startDateError,
                                deadlineError,
                              }) {
  const formattedCurrentDateTime = new Date()
    .toISOString()
    .slice(0, 16);

  const [startDate, setStartDate] = useState(task ? getFormattedDateTime(task.startDate) : formattedCurrentDateTime);

  const [deadline, setDeadline] = useState(task ? getFormattedDateTime(task.deadline) : formattedCurrentDateTime);

  function getFormattedDateTime(date) {
    return new Date(date).toISOString().slice(0, 16);
  }

  const handleStartDateChange = (e) => {
    setStartDate(e.target.value);
  };

  const handleDeadlineChange = (e) => {
    setDeadline(e.target.value);
  };

  return (<Container component="main" maxWidth="xs">
    <CssBaseline/>
    <StyledDiv>
      <Avatar sx={{m: 1, bgcolor: "secondary.main"}}>
        <LockOutlinedIcon/>
      </Avatar>
      <Typography component="h1" variant="h5">
        {task ? "Update task details" : "Create a new task"}
      </Typography>
      <Stack spacing={2} sx={{width: "100%", marginTop: 2}}>
        {nameError && <Alert severity="error">Enter a valid task name</Alert>}
        {descriptionError && (<Alert severity="error">Task description is invalid</Alert>)}
        {startDateError && (<Alert severity="error">Task start date is invalid</Alert>)}
        {deadlineError && (<Alert severity="error">Task deadline date is invalid</Alert>)}
        {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
      </Stack>
      <Box component="form" onSubmit={onSubmit} sx={{mt: 1}}>
        <StyledLabel htmlFor="name">Task name</StyledLabel>
        <StyledInput
          margin="normal"
          required
          fullWidth
          id="name"
          name="name"
          defaultValue={task?.name ?? ""}
          autoFocus
        />
        <StyledLabel htmlFor="description">Description</StyledLabel>
        <StyledInput
          margin="normal"
          required
          fullWidth
          name="description"
          id="description"
          defaultValue={task?.description ?? ""}
        />
        <StyledLabel htmlFor="importance">Importance</StyledLabel>
        <select name="importance" id="importance" required={true} defaultValue={task?.importance ?? ""}>
          {importanceOptions.map(importanceName => {
            return <option key={importanceName}>{importanceName}</option>
          })}
        </select>
        <br/><br/>
        <StyledLabel htmlFor="difficulty">Difficulty (1 - 5)</StyledLabel>
        <input name="difficulty" type="number" min={1} max={5} required={true} defaultValue={task?.difficulty ?? 1}/>
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
        <select name="taskStatus" id="taskStatus" required={true} defaultValue={task?.taskStatus ?? ""}>
          {taskStatusOptions.map(taskStatusName => {
            return <option key={taskStatusName}>{taskStatusName}</option>
          })}
        </select>
        <StyledButton type="submit" fullWidth variant="contained">
          Save task
        </StyledButton>
      </Box>
      <BackButton fullWidth={true}/>
    </StyledDiv>
  </Container>);
}

export default TaskCreateUpdateForm;

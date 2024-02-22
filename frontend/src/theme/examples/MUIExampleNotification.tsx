import {Button, ButtonGroup, Grid, Typography} from "@mui/material";
import {
  NotificationState,
  useNotification
} from "../../common/context/NotificationProvider.tsx";

export default function MUIExampleNotification() {
  const notification = useNotification();

  function openNotification(notificationState: NotificationState) {
    notification.openNotification(notificationState);
  }

  return (
    <Grid container justifyContent="center" textAlign="center" spacing={2} mb={4}>
      <Grid item xs={12}>
        <Typography variant={"h6"} gutterBottom>
          Notifications
        </Typography>
        <ButtonGroup variant={"contained"}>
          <Button onClick={() => {
            openNotification({type: "info", message: "Info notification", vertical: "top", horizontal: "center"});
          }}>
            Info
          </Button>
          <Button onClick={() => {
            openNotification({type: "success", message: "Success notification", vertical: "top", horizontal: "center"});
          }}>
            Success
          </Button>
          <Button onClick={() => {
            openNotification({type: "warning", message: "Warning notification", vertical: "top", horizontal: "center"});
          }}>
            Warning
          </Button>
          <Button onClick={() => {
            openNotification({type: "error", message: "Error notification", vertical: "top", horizontal: "center"});
          }}>
            Error
          </Button>
        </ButtonGroup>
      </Grid>
    </Grid>);
}

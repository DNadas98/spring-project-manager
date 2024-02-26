import {Button, ButtonGroup, Grid, Typography} from "@mui/material";
import {
  useNotification
} from "../../../common/notification/context/NotificationProvider.tsx";

export default function MUIExampleNotification() {
  const notification = useNotification();

  return (
    <Grid container justifyContent="center" textAlign="center" spacing={2} mb={4}>
      <Grid item xs={12}>
        <Typography variant={"h6"} gutterBottom>
          Notifications
        </Typography>
        <ButtonGroup variant={"contained"}>
          <Button onClick={() => {
            notification.openNotification({
              type: "info",
              message: "Info notification",
              vertical: "top",
              horizontal: "center"
            });
          }}>
            Info
          </Button>
          <Button onClick={() => {
            notification.openNotification({
              type: "success",
              message: "Success notification",
              vertical: "top",
              horizontal: "center"
            });
          }}>
            Success
          </Button>
          <Button onClick={() => {
            notification.openNotification({
              type: "warning",
              message: "Warning notification",
              vertical: "top",
              horizontal: "center"
            });
          }}>
            Warning
          </Button>
          <Button onClick={() => {
            notification.openNotification({
              type: "error",
              message: "Error notification",
              vertical: "top",
              horizontal: "center"
            });
          }}>
            Error
          </Button>
        </ButtonGroup>
      </Grid>
    </Grid>);
}

import React, {createContext, ReactNode, useContext} from "react";
import Snackbar, {SnackbarOrigin} from "@mui/material/Snackbar";
import {
  Alert, AlertColor
} from "@mui/material";

export interface NotificationState extends SnackbarOrigin {
  message: string;
  open?: boolean;
  type: AlertColor;
  vertical: "top" | "bottom";
  horizontal: "left" | "center" | "right";
}

interface NotificationProviderProps {
  children: ReactNode;
}

interface NotificationContextType {
  openNotification: (newState: NotificationState) => void;
}

const NotificationContext: React.Context<NotificationContextType> = createContext<NotificationContextType>({
  openNotification: () => {
  }
});

export function NotificationProvider({children}: NotificationProviderProps) {
  const autohideDuration = 6000;

  const [notificationState, setNotificationState] = React.useState<NotificationState>({
    open: false,
    type: "info",
    message: "",
    vertical: "top",
    horizontal: "center"
  });

  /**
   *
   * @param newState {NotificationState}
   */
  const openNotification = (newState: NotificationState) => {
    setNotificationState({...newState, open: true});
  };

  const handleClose = (_event?: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === "clickaway") {
      return;
    }
    setNotificationState((prevState) => ({...prevState, open: false}));
  };

  return (
    <NotificationContext.Provider value={{openNotification}}>
      <Snackbar open={notificationState.open}
                autoHideDuration={autohideDuration}
                anchorOrigin={{vertical: notificationState.vertical, horizontal: notificationState.horizontal}}
                onClose={handleClose}>
        <Alert onClose={handleClose}
               severity={notificationState.type}>
          {notificationState.message}
        </Alert>
      </Snackbar>
      {children}
    </NotificationContext.Provider>
  );
}

export function useNotification() {
  return useContext(NotificationContext);
}

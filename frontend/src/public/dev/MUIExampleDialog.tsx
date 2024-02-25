import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Grid} from "@mui/material";
import React from "react";

interface MuiExampleDialogProps {
  isMobile: boolean;
}

export default function MUIExampleDialog({isMobile}: MuiExampleDialogProps) {
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <Grid container>
      <Button variant="outlined" color="secondary" onClick={handleClickOpen}>
        Open Dialog
      </Button>
      <Dialog
        open={open}
        onClose={handleClose}
        fullScreen={isMobile}
      >
        <DialogTitle>
          Dialog title text
        </DialogTitle>
        <DialogContent>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ac
          magna
          libero. Proin vel lacus vitae massa euismod tincidunt eget ut dui. Sed
          id
          sem a risus fermentum eleifend congue vitae sem.
        </DialogContent>
        <DialogActions>
          <Button autoFocus onClick={handleClose} color="secondary">
            Disagree
          </Button>
          <Button onClick={handleClose} color="secondary" autoFocus>
            Agree
          </Button>
        </DialogActions>
      </Dialog>
    </Grid>
  );
}

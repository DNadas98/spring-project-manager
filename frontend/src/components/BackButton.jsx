import React from "react";
import {useNavigate} from "react-router-dom";
import {Button} from "@mui/material";
import {styled} from "@mui/material/styles";

// eslint-disable-next-line react/prop-types
function BackButton({path, text, fullWidth}) {
  const navigate = useNavigate();

  const StyledButton = styled(Button)(({theme}) => ({
    marginRight: theme.spacing(2),
    color: "inherit",
  }));

  return (
    <StyledButton type="button"
                  variant="contained"
                  fullWidth={fullWidth ? true : false}
                  sx={{mt: 3, mb: 2}} onClick={() => navigate(path ?? -1)}>
      {text ?? "Back"}
    </StyledButton>
  );
}

export default BackButton;

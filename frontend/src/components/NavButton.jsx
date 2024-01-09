import React from "react";
import {Link, useLocation} from "react-router-dom";
import {Button} from '@mui/material';

function NavButton({path, text}) {
  const current = useLocation().pathname;
  return (
    <Link to={path}>
      <Button variant="contained" color={path === current ? "secondary" : "primary"} sx={{marginLeft:2}}>
        {text}
      </Button>
    </Link>
  );
}

export default NavButton;

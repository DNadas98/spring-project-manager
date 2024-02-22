import {IconButton, Menu, MenuItem} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import {Link as RouterLink} from "react-router-dom";
import {useState} from "react";
import {IMenuRoutes} from "../../../config/routing/IMenuRoutes.ts";

interface SmallMenuProps {
  menu: IMenuRoutes;
}

export default function SmallMenu({menu}: SmallMenuProps) {
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  return (<>
    <IconButton
      size="large"
      edge="start"
      color="inherit"
      onClick={handleMenu}
    >
      <MenuIcon/>
    </IconButton>
    <Menu
      id="menu-appbar"
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: "top",
        horizontal: "right"
      }}
      keepMounted
      transformOrigin={{
        vertical: "top",
        horizontal: "right"
      }}
      open={open}
      onClose={handleClose}
    >{menu.elements.length ? menu.elements.map(el => {
      return <MenuItem key={el.path} onClick={handleClose} component={RouterLink}
                       to={el.path}>{el.name}</MenuItem>;
    }) : <></>}
    </Menu>
  </>);
}

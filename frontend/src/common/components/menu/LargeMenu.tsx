import {Button, Stack} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";
import {IMenuRoutes} from "../../../config/routing/IMenuRoutes.ts";

interface LargeMenuProps {
  menu: IMenuRoutes;
}

export default function LargeMenu({menu}: LargeMenuProps) {
  if (!menu?.elements?.length) {
    return <></>;
  }
  return (<Stack direction={"row"}>
    {menu.elements.map(el => {
      return (
        <Button key={el.path} component={RouterLink} to={el.path} color="inherit">
          {el.name}
        </Button>
      );
    })}
  </Stack>);
}

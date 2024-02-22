import {
  AppBar,
  Toolbar,
  Typography,
  useMediaQuery,
  useTheme
} from "@mui/material";
import {IMenuRoutes} from "../../config/routing/IMenuRoutes.ts";
import {publicMenuRoutes} from "../../config/routing/publicMenuRoutes.tsx";
import ThemePaletteModeSwitch from "../../theme/components/ThemePaletteModeSwitch.tsx";
import SmallMenu from "../../common/components/menu/SmallMenu.tsx";
import LargeMenu from "../../common/components/menu/LargeMenu.tsx";
import siteConfig from "../../config/siteConfig.ts";

export default function PublicHeader() {
  const theme = useTheme();
  const {siteName} = siteConfig;
  const isSmallScreen = useMediaQuery(theme.breakpoints.down("sm"));
  const menu: IMenuRoutes = publicMenuRoutes;


  return (
    <AppBar position="static" sx={{marginBottom: 4}}>
      <Toolbar>
        <Typography variant={"h6"} flexGrow={1}>{siteName}</Typography>
        {isSmallScreen
          ? <SmallMenu menu={menu}/>
          : <LargeMenu menu={menu}/>
        }
        <ThemePaletteModeSwitch/>
      </Toolbar>
    </AppBar>
  );
}

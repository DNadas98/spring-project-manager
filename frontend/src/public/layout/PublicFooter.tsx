import {AppBar, Link, Toolbar, Typography} from "@mui/material";
import SmallMenu from "../../common/components/menu/SmallMenu.tsx";
import {publicMenuRoutes} from "../../config/menus/publicMenuRoutes.tsx";
import {IMenuRoutes} from "../../routing/IMenuRoutes.ts";
import {GitHub} from "@mui/icons-material";
import siteConfig from "../../config/siteConfig.ts";

export default function PublicFooter() {
  const currentYear = new Date().getFullYear();
  const {siteName, githubRepoUrl} = siteConfig;
  const menu: IMenuRoutes = publicMenuRoutes;

  return (
    <AppBar position="sticky" color="primary" sx={{top: "auto", bottom: 0, marginTop: 4}}>
      <Toolbar sx={{justifyContent: "center"}}>
        <SmallMenu menu={menu}/>
        <Typography mr={2}>
          {currentYear}{" "}&copy;{" "}{siteName}
        </Typography>
        <Link color={"inherit"} target="_blank" rel="noopener noreferrer"
              href={githubRepoUrl}>
          <GitHub/>
        </Link>
      </Toolbar>
    </AppBar>
  );
}

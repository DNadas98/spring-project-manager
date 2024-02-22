import {Outlet} from "react-router-dom";
import {Box} from "@mui/material";
import PublicHeader from "./PublicHeader.tsx";
import PublicFooter from "./PublicFooter.tsx";

function Layout() {
  return (
    <Box sx={{display: "flex", flexDirection: "column", minHeight: "100vh"}}>
      <PublicHeader/>
      <Box sx={{
        flexGrow: 1,
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center"
      }}>
        <Outlet/>
      </Box>
      <PublicFooter/>
    </Box>
  );
}

export default Layout;

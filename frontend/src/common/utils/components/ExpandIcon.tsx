import {Avatar} from "@mui/material";
import {ExpandMore} from "@mui/icons-material";


export default function ExpandIcon() {
  return (
    <Avatar variant={"rounded"} sx={{
      backgroundColor: "primary.main",
      height: "1.25rem",
      width: "1.25rem"
    }}>
      <ExpandMore/>
    </Avatar>
  )
}

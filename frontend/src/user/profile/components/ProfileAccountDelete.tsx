import {Avatar, IconButton, Stack, Typography} from "@mui/material";
import {ClearOutlined} from "@mui/icons-material";
import {UserAccountDto} from "../../dto/UserAccountDto.ts";

interface ProfileAccountDeleteProps {
  account: UserAccountDto;
  onAccountDelete: (id: number) => void;
  accountDeleteLoading: boolean;
}

export default function ProfileAccountDelete(props: ProfileAccountDeleteProps) {
  return (
    <Stack direction={"row"}
           spacing={"2"}
           justifyContent={"space-between"}
           alignItems={"center"}>
      <Typography>
        {props.account.accountType}
      </Typography>
      <IconButton disabled={props.accountDeleteLoading} onClick={() => {
        props.onAccountDelete(props.account.id);
      }}>
        <Avatar variant={"rounded"} sx={{
          backgroundColor: "error.main",
          height: "1.25rem",
          width: "1.25rem"
        }}>
          <ClearOutlined/>
        </Avatar>
      </IconButton>
    </Stack>
  )
}

import {TextField} from "@mui/material";

export default function CompanyNameInput() {
  return (

    <TextField variant={"outlined"}
               color={"secondary"}
               label={"Company name"}
               name={"name"}
               type={"text"}
               autoFocus={true}
               required
               inputProps={{
                 minLength: 1,
                 maxLength: 50
               }}/>
  )
}

import {useAuthentication} from "../../authentication/hooks/useAuthentication.ts";
import ProfileMainCard from "./components/ProfileMainCard.tsx";

export default function Profile() {
  const authentication = useAuthentication();
  const username = authentication.getUsername();
  const roles = authentication.getRoles();
  const email = authentication.getEmail();
  return username && email && roles ? (
    <ProfileMainCard username={username} email={email} roles={roles}/>
  ) : <></>
}

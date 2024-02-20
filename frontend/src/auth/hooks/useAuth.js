import {useContext} from "react";
import AuthContext from "../context/AuthProvider.tsx";

function useAuth() {
  return useContext(AuthContext);
}

export default useAuth;

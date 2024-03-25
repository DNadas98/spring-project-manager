import {useContext} from "react";
import {PermissionContext} from "../context/PermissionProvider.tsx";

export default function usePermissions() {
  return useContext(PermissionContext);
}
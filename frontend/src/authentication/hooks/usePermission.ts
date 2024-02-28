import {useContext} from "react";
import {PermissionContext} from "../context/PermissionProvider.tsx";
import {IPermissionContext} from "../context/IPermissionContext.ts";

export default function usePermission(): IPermissionContext {
  return useContext<IPermissionContext>(PermissionContext);
}

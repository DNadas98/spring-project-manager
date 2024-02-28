import {PermissionType} from "../dto/applicationUser/PermissionType.ts";

export interface IPermissionContext {
  companyPermissions: PermissionType[];
  projectPermissions?: PermissionType[];
  taskPermissions?: PermissionType[];
}

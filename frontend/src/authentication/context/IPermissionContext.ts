import { PermissionType } from "../dto/applicationUser/PermissionType.ts";

export interface IPermissionContext {
  permissionsLoading: boolean;
  companyPermissions: PermissionType[];
  projectPermissions: PermissionType[];
  taskPermissions: PermissionType[];
}
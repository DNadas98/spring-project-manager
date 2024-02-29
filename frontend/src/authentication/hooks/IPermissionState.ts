import {PermissionType} from "../dto/applicationUser/PermissionType.ts";

export interface IPermissionState {
  loading: boolean;
  permissions: PermissionType[];
}

import { createContext, useState, useEffect } from "react";
import { PermissionType } from "../dto/applicationUser/PermissionType.ts";
import { useAuthJsonFetch } from "../../common/api/service/apiService.ts";
import {Outlet, useParams} from "react-router-dom";
import { IPermissionContext } from "./IPermissionContext.ts";


export const PermissionContext = createContext<IPermissionContext>({
  permissionsLoading: true,
  companyPermissions: [],
  projectPermissions: [],
  taskPermissions: [],
});

export function PermissionProvider() {
  const params = useParams();
  const authJsonFetch = useAuthJsonFetch();

  const companyId = params.companyId;
  const projectId = params.projectId;
  const taskId = params.taskId;

  function isValidId(id: string | undefined) {
    return id && !isNaN(parseInt(id)) && parseInt(id) > 0;
  }

  const [companyPermissionsLoading, setCompanyPermissionsLoading] = useState<boolean>(true);
  const [companyPermissions, setCompanyPermissions] = useState<PermissionType[]>([]);
  const [projectPermissionsLoading, setProjectPermissionsLoading] = useState<boolean>(true);
  const [projectPermissions, setProjectPermissions] = useState<PermissionType[]>([]);
  const [taskPermissionsLoading, setTaskPermissionsLoading] = useState<boolean>(true);
  const [taskPermissions, setTaskPermissions] = useState<PermissionType[]>([]);

  const permissionsLoading = companyPermissionsLoading || projectPermissionsLoading || taskPermissionsLoading;

  async function loadCompanyPermissions(): Promise<void> {
    try {
      if (!isValidId(companyId)) {
        setCompanyPermissions([]);
        return;
      }
      setCompanyPermissionsLoading(true);
      const response = await authJsonFetch({path: `user/permissions/companies/${companyId}`});
      if (!response || !response?.data || response?.error || response?.status > 399) {
        setCompanyPermissions([]);
        return;
      }
      setCompanyPermissions(response.data as PermissionType[]);
    } catch (e) {
      setCompanyPermissions([]);
    } finally {
      setCompanyPermissionsLoading(false);
    }
  }

  async function loadProjectPermissions(): Promise<void> {
    try {
      if (!isValidId(companyId) || !isValidId(projectId)) {
        setProjectPermissions([]);
        return;
      }
      setProjectPermissionsLoading(true);
      const response = await authJsonFetch({path: `user/permissions/companies/${companyId}/projects/${projectId}`});
      if (!response || !response?.data || response?.error || response?.status > 399) {
        setProjectPermissions([]);
        return;
      }
      setProjectPermissions(response.data as PermissionType[]);
    } catch (e) {
      setProjectPermissions([]);
    } finally {
      setProjectPermissionsLoading(false);
    }
  }

  async function loadTaskPermissions(): Promise<void> {
    try {
      if (!isValidId(companyId) || !isValidId(projectId) || !isValidId(taskId)) {
        setTaskPermissions([]);
        return;
      }
      setTaskPermissionsLoading(true);
      const response = await authJsonFetch({path: `user/permissions/companies/${companyId}/projects/${projectId}/tasks/${taskId}`});
      if (!response || !response?.data || response?.error || response?.status > 399) {
        setTaskPermissions([]);
        return;
      }
      setTaskPermissions(response.data as PermissionType[]);
    } catch (e) {
      setTaskPermissions([]);
      return;
    } finally {
      setTaskPermissionsLoading(false);
    }
  }

  useEffect(() => {
    loadCompanyPermissions().then();
  }, [companyId]);

  useEffect(() => {
    loadProjectPermissions().then();
  }, [projectId]);

  useEffect(() => {
    loadTaskPermissions().then();
  }, [taskId]);

  return (
    <PermissionContext.Provider
      value={{ permissionsLoading, companyPermissions, projectPermissions, taskPermissions }}
    >
      <Outlet/>
    </PermissionContext.Provider>
  );
}

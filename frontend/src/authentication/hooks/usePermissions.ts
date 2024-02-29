import {PermissionType} from "../dto/applicationUser/PermissionType.ts";
import {useParams} from "react-router-dom";
import {useAuthJsonFetch} from "../../common/api/service/apiService.ts";
import {useEffect, useState} from "react";
import {IPermissionState} from "./IPermissionState.ts";


export default function usePermissions() {
  const params = useParams();
  const companyId = params.companyId;
  const projectId = params.projectId;
  const taskId = params.taskId;
  const authJsonFetch = useAuthJsonFetch();

  function isValidId(id: string | undefined) {
    return id && !isNaN(parseInt(id)) && parseInt(id) > 0;
  }

  const [permissionsState, setPermissionsState] = useState<IPermissionState>({
    loading: true,
    permissions: []
  });

  async function loadCompanyPermissions() {
    try {
      if (!isValidId(companyId)) {
        return [];
      }
      const response = await authJsonFetch({path: `user/permissions/companies/${companyId}`});
      if (!response || !response?.data || response?.error || response?.status > 399) {
        return [];
      }
      return response.data as PermissionType[];
    } catch (e) {
      return [];
    }
  }

  async function loadProjectPermissions() {
    try {
      if (!isValidId(companyId) || !isValidId(projectId)) {
        return [];
      }
      const response = await authJsonFetch({path: `user/permissions/companies/${companyId}/projects/${projectId}`});
      if (!response || !response?.data || response?.error || response?.status > 399) {
        return [];
      }
      return response.data as PermissionType[];
    } catch (e) {
      return [];
    }
  }

  async function loadTaskPermissions() {
    try {
      if (!isValidId(companyId) || !isValidId(projectId) || !isValidId(taskId)) {
        return [];
      }
      const response = await authJsonFetch({path: `user/permissions/companies/${companyId}/projects/${projectId}/tasks/${taskId}`});
      if (!response || !response?.data || response?.error || response?.status > 399) {
        return [];
      }
      return response.data as PermissionType[];
    } catch (e) {
      return [];
    }
  }

  useEffect(() => {
    async function fetchPermissions() {
      setPermissionsState({loading: true, permissions: []});
      try {
        const combinedPermissions = [];
        const companyPermissions = await loadCompanyPermissions();
        if (companyPermissions?.length) {
          combinedPermissions.push(...companyPermissions);
        }
        const projectPermissions = await loadProjectPermissions();
        if (projectPermissions?.length) {
          combinedPermissions.push(...projectPermissions);
        }
        const taskPermissions = await loadTaskPermissions();
        if (taskPermissions?.length) {
          combinedPermissions.push(...taskPermissions);
        }
        setPermissionsState({
          loading: false, permissions: combinedPermissions
        });
      } catch (e) {
        setPermissionsState({
          loading: false, permissions: []
        });
      }
    }

    fetchPermissions().then();
  }, [companyId, projectId, taskId]);

  return permissionsState;
}

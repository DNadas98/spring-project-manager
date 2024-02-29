import {PermissionType} from "../dto/applicationUser/PermissionType.ts";
import {useNavigate, useParams} from "react-router-dom";
import {
  useNotification
} from "../../common/notification/context/NotificationProvider.tsx";
import {useAuthJsonFetch} from "../../common/api/service/apiService.ts";
import {useEffect, useState} from "react";
import {IPermissionState} from "./IPermissionState.ts";
import {ApiResponseDto} from "../../common/api/dto/ApiResponseDto.ts";


export default function usePermissions() {
  const params = useParams();
  const companyId = params.companyId;
  const projectId = params.projectId;
  const taskId = params.taskId;
  const notification = useNotification();
  const authJsonFetch = useAuthJsonFetch();
  const navigate = useNavigate();

  function isValidId(id: string | undefined) {
    return id && !isNaN(parseInt(id)) && parseInt(id) > 0;
  }

  function openErrorNotification(message: string) {
    notification.openNotification({
      type: "error", horizontal: "center", vertical: "top", message: message
    });
  }

  function handleErrorResponse(response: ApiResponseDto | void) {
    if (response?.status === 401 || response?.status === 403) {
      openErrorNotification(response.error ?? "Access Denied");
      navigate(-1);
    }
    return [];
  }

  const [permissionsState, setPermissionsState] = useState<IPermissionState>({
    loading: true,
    permissions: []
  });

  async function loadCompanyPermissions(): Promise<PermissionType[]> {
    try {
      if (!isValidId(companyId)) {
        return [];
      }
      const response = await authJsonFetch({path: `user/permissions/companies/${companyId}`});
      if (!response || !response?.data || response?.error) {
        return handleErrorResponse(response);
      }
      return response.data as PermissionType[];
    } catch (e) {
      return [];
    }
  }

  async function loadProjectPermissions(): Promise<PermissionType[]> {
    try {
      if (!isValidId(companyId) || !isValidId(projectId)) {
        return [];
      }
      const response = await authJsonFetch({path: `user/permissions/companies/${companyId}/projects/${projectId}`});
      if (!response || !response?.data || response?.error) {
        return handleErrorResponse(response);
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
      if (!response || !response?.data || response?.error) {
        return handleErrorResponse(response);
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
        const companyPermissions = await loadCompanyPermissions();
        const projectPermissions = await loadProjectPermissions();
        const taskPermissions = await loadTaskPermissions();
        const combinedPermissions = [...companyPermissions, ...projectPermissions, ...taskPermissions];
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

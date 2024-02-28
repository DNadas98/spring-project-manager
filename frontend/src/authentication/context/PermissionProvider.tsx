import {createContext, useEffect, useState} from "react";
import {IPermissionContext} from "./IPermissionContext.ts";
import {PermissionType} from "../dto/applicationUser/PermissionType.ts";
import {Outlet, useParams} from "react-router-dom";
import {useAuthJsonFetch} from "../../common/api/service/apiService.ts";
import {
  useNotification
} from "../../common/notification/context/NotificationProvider.tsx";
import LoadingSpinner from "../../common/utils/components/LoadingSpinner.tsx";

export const PermissionContext = createContext<IPermissionContext>({
  companyPermissions: [],
  projectPermissions: [],
  taskPermissions: []
});

export function PermissionProvider() {
  const notification = useNotification();
  const authJsonFetch = useAuthJsonFetch();
  const params = useParams();

  function isValidId(id: string | undefined) {
    return id && !isNaN(parseInt(id)) && parseInt(id) > 0;
  }

  function openErrorNotification(message: string) {
    notification.openNotification({
      type: "error", horizontal: "center", vertical: "top", message: message
    });
  }

  /**
   * Company permissions
   */

  const companyId = params?.companyId;
  const [companyPermissionsLoading, setCompanyPermissionsLoading] = useState<boolean>(true);
  const [companyPermissions, setCompanyPermissions] = useState<PermissionType[]>([]);
  useEffect(() => {
    async function loadCompanyPermissions() {
      try {
        setCompanyPermissionsLoading(true);
        const response = await authJsonFetch({path: `user/permissions/companies/${companyId}`});
        if (!response || !response?.data || response?.error) {
          if (response?.error) {
            openErrorNotification(response.error);
          }
          setCompanyPermissions([]);
          return;
        }
        setCompanyPermissions(response.data as PermissionType[]);
      } catch (e) {
        setCompanyPermissions([]);
      }
    }

    if (isValidId(companyId)) {
      loadCompanyPermissions().finally(() => {
        setCompanyPermissionsLoading(false);
      });
    } else {
      setCompanyPermissions([]);
      setCompanyPermissionsLoading(false);
    }
  }, [companyId]);

  /**
   * Project permissions
   */

  const projectId = params?.projectId;
  const [projectPermissionsLoading, setProjectPermissionsLoading] = useState<boolean>(true);
  const [projectPermissions, setProjectPermissions] = useState<PermissionType[]>([]);

  useEffect(() => {
    async function loadProjectPermissions() {
      try {
        setProjectPermissionsLoading(true);
        const response = await authJsonFetch({path: `user/permissions/companies/${companyId}/projects/${projectId}`});
        if (!response || !response?.data || response?.error) {
          if (response?.error) {
            openErrorNotification(response.error);
          }
          setProjectPermissions([]);
          return;
        }
        setProjectPermissions(response.data as PermissionType[]);
      } catch (e) {
        setProjectPermissions([]);
      }
    }

    if (isValidId(companyId) && isValidId(projectId)) {
      loadProjectPermissions().finally(() => {
        setProjectPermissionsLoading(false);
      });
    } else {
      setProjectPermissions([]);
      setProjectPermissionsLoading(false);
    }
  }, [companyId, projectId]);

  /**
   * Task permissions
   */

  const taskId = params?.taskId;
  const [taskPermissionsLoading, setTaskPermissionsLoading] = useState<boolean>(true);
  const [taskPermissions, setTaskPermissions] = useState<PermissionType[]>([]);

  useEffect(() => {
    async function loadTaskPermissions() {
      try {
        setTaskPermissionsLoading(true);
        const response = await authJsonFetch({path: `user/permissions/companies/${companyId}/projects/${projectId}/tasks/${taskId}`});
        if (!response || !response?.data || response?.error) {
          if (response?.error) {
            openErrorNotification(response.error);
          }
          setTaskPermissions([]);
          return;
        }
        setTaskPermissions(response.data as PermissionType[]);
      } catch (e) {
        setTaskPermissions([]);
      }
    }

    if (isValidId(companyId) && isValidId(projectId) && isValidId(taskId)) {
      loadTaskPermissions().finally(() => {
        setTaskPermissionsLoading(false);
      });
    } else {
      setTaskPermissions([]);
      setTaskPermissionsLoading(false);
    }
  }, [companyId, projectId, taskId]);


  return (
    <PermissionContext.Provider
      value={{
        companyPermissions, projectPermissions, taskPermissions
      }}>
      {companyPermissionsLoading || projectPermissionsLoading || taskPermissionsLoading
        ? <LoadingSpinner/>
        : <Outlet/>}
    </PermissionContext.Provider>
  );
}

import {useAuthentication} from "../../authentication/hooks/useAuthentication.ts";
import ProfileDashboard from "./components/ProfileDashboard.tsx";
import {useEffect, useState} from "react";
import LoadingSpinner from "../../common/utils/components/LoadingSpinner.tsx";
import {useAuthJsonFetch} from "../../common/api/service/apiService.ts";
import {
  useNotification
} from "../../common/notification/context/NotificationProvider.tsx";
import {UserAccountDto} from "../dto/UserAccountDto.ts";
import {ApiResponseDto} from "../../common/api/dto/ApiResponseDto.ts";
import useLogout from "../../authentication/hooks/useLogout.ts";

export default function Profile() {
  const [loading, setLoading] = useState<boolean>(true);
  const [accountDeleteLoading, setAccountDeleteLoading] = useState<boolean>(false);
  const [applicationUserDeleteLoading, setApplicationUserDeleteLoading] = useState<boolean>(false);
  const [accounts, setAccounts] = useState<UserAccountDto[]>([]);
  const authJsonFetch = useAuthJsonFetch();
  const authentication = useAuthentication();
  const notification = useNotification();
  const username = authentication.getUsername();
  const roles = authentication.getRoles();
  const email = authentication.getEmail();
  const logout = useLogout();

  useEffect(() => {
    async function loadAccounts() {
      try {
        const apiResponse = await authJsonFetch({
          path: "user/accounts", method: "GET"
        });
        if (!apiResponse?.data) {
          setAccounts([]);
          return;
        }
        setAccounts(apiResponse.data as UserAccountDto[]);
      } catch (e) {
        setAccounts([]);
      }
    }

    loadAccounts().finally(() => {
      setLoading(false);
    })
  }, []);

  async function deleteAccount(id: number): Promise<void> {
    const defaultError = "Failed to delete account";
    try {
      setAccountDeleteLoading(true);
      const response = await authJsonFetch({
        path: `user/accounts/${id}`, method: "DELETE"
      });
      if (!response?.message) {
        return notifyOnError(defaultError, response ?? undefined);
      }
      notification.openNotification({
        type: "success", vertical: "top", horizontal: "center", message: response.message
      })
      setAccounts((prev) => {
        return prev.filter(el => el.id !== id);
      });
    } catch (e) {
      notifyOnError(defaultError);
    } finally {
      setAccountDeleteLoading(false);
    }
  }

  async function deleteApplicationUser(): Promise<void> {
    const defaultError = "Failed to remove user details";
    try {
      setApplicationUserDeleteLoading(true);
      const response = await authJsonFetch({
        path: `user`, method: "DELETE"
      });
      if (!response?.message) {
        return notifyOnError(defaultError, response ?? undefined);
      }
      notification.openNotification({
        type: "success", vertical: "top", horizontal: "center", message: response.message
      })
      return await logout(true);
    } catch (e) {
      notifyOnError(defaultError);
    } finally {
      setApplicationUserDeleteLoading(false);
    }
  }

  function notifyOnError(defaultError: string, response: ApiResponseDto | undefined = undefined) {
    notification.openNotification({
      type: "error", vertical: "top", horizontal: "center",
      message: `${response?.error ?? defaultError}`
    })
    return;
  }

  return loading
    ? <LoadingSpinner/>
    : username && email && roles ? (
      <ProfileDashboard username={username}
                        email={email}
                        roles={roles}
                        accounts={accounts}
                        onAccountDelete={deleteAccount}
                        accountDeleteLoading={accountDeleteLoading}
                        onApplicationUserDelete={deleteApplicationUser}
                        applicationUserDeleteLoading={applicationUserDeleteLoading}/>
  ) : <></>
}

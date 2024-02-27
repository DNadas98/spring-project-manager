import CompanyBrowser from "./components/CompanyBrowser.tsx";
import {useEffect, useState} from "react";
import {CompanyResponsePublicDto} from "../../dto/CompanyResponsePublicDto.ts";
import {useAuthJsonFetch} from "../../../common/api/service/apiService.ts";
import {
  useNotification
} from "../../../common/notification/context/NotificationProvider.tsx";

export default function Companies() {
  const [companiesWithUserLoading, setCompaniesWithUserLoading] = useState<boolean>(true);
  const [companiesWithUser, setCompaniesWithUser] = useState<CompanyResponsePublicDto[]>([]);
  const [companiesWithoutUserLoading, setCompaniesWithoutUserLoading] = useState<boolean>(true);
  const [companiesWithoutUser, setCompaniesWithoutUser] = useState<CompanyResponsePublicDto[]>([]);

  const authJsonFetch = useAuthJsonFetch();
  const notification = useNotification();

  useEffect(() => {
    async function loadCompaniesWithUser() {
      const response = await authJsonFetch({
        path: `companies?withUser=true`
      });
      if (!response?.status || response.status > 399 || !response?.data) {
        notification.openNotification({
          type: "error", vertical: "top", horizontal: "center",
          message: `${response?.error ?? "Failed to load your companies"}`
        })
        return;
      }
      setCompaniesWithUser(response.data as CompanyResponsePublicDto[]);
    }

    async function loadCompaniesWithoutUser() {
      const response = await authJsonFetch({
        path: `companies?withUser=false`
      });
      if (!response?.status || response.status > 399 || !response?.data) {
        notification.openNotification({
          type: "error", vertical: "top", horizontal: "center",
          message: `${response?.error ?? "Failed to load companies to join"}`
        })
        return;
      }
      setCompaniesWithoutUser(response.data as CompanyResponsePublicDto[]);
    }

    loadCompaniesWithUser().catch(() => {
      setCompaniesWithUser([]);
    }).finally(() => {
      setCompaniesWithUserLoading(false);
    });
    loadCompaniesWithoutUser().catch(() => {
      setCompaniesWithoutUser([]);
    }).finally(() => {
      setCompaniesWithoutUserLoading(false);
    });
  }, []);
  return (
    <CompanyBrowser companiesWithUser={companiesWithUser}
                    companiesWithUserLoading={companiesWithUserLoading}
                    companiesWithoutUser={companiesWithoutUser}
                    companiesWithoutUserLoading={companiesWithoutUserLoading}/>
  )
}

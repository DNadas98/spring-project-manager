import CompanyBrowser from "./components/CompanyBrowser.tsx";
import {FormEvent, useEffect, useMemo, useState} from "react";
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

  const [companiesWithUserFilterValue, setCompaniesWithUserFilterValue] = useState<string>("");
  const [companiesWithoutUserFilterValue, setCompaniesWithoutUserFilterValue] = useState<string>("");

  const companiesWithUserFiltered = useMemo(() => {
    return companiesWithUser.filter(company => {
        return company.name.toLowerCase().includes(companiesWithUserFilterValue)
      }
    );
  }, [companiesWithUser, companiesWithUserFilterValue]);

  const companiesWithoutUserFiltered = useMemo(() => {
    return companiesWithoutUser.filter(company => {
        return company.name.toLowerCase().includes(companiesWithoutUserFilterValue)
      }
    );
  }, [companiesWithoutUser, companiesWithoutUserFilterValue]);

  const handleCompaniesWithUserSearch = (event: FormEvent<HTMLInputElement>) => {
    // @ts-ignore
    setCompaniesWithUserFilterValue(event.target.value.toLowerCase().trim());
  };

  const handleCompaniesWithoutUserSearch = (event: FormEvent<HTMLInputElement>) => {
    // @ts-ignore
    setCompaniesWithoutUserFilterValue(event.target.value.toLowerCase().trim());
  };

  return (
    <CompanyBrowser companiesWithUser={companiesWithUserFiltered}
                    companiesWithUserLoading={companiesWithUserLoading}
                    companiesWithoutUser={companiesWithoutUserFiltered}
                    companiesWithoutUserLoading={companiesWithoutUserLoading}
                    handleCompaniesWithUserSearch={handleCompaniesWithUserSearch}
                    handleCompaniesWithoutUserSearch={handleCompaniesWithoutUserSearch}
    />
  )
}

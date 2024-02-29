import {useNavigate, useParams} from "react-router-dom";
import NotFound from "../../../public/pages/errorPages/NotFound.tsx";
import {useEffect, useState} from "react";
import {useAuthJsonFetch} from "../../../common/api/service/apiService.ts";
import {CompanyResponsePrivateDto} from "../../dto/CompanyResponsePrivateDto.ts";
import {
  useNotification
} from "../../../common/notification/context/NotificationProvider.tsx";
import LoadingSpinner from "../../../common/utils/components/LoadingSpinner.tsx";
import usePermissions from "../../../authentication/hooks/usePermissions.ts";

export default function CompanyDashboard() {
  const {loading: permissionsLoading, permissions: companyPermissions} = usePermissions();

  const companyId = useParams()?.companyId;
  const [companyLoading, setCompanyLoading] = useState(true);
  const [company, setCompany] = useState<CompanyResponsePrivateDto | undefined>(undefined);
  const authJsonFetch = useAuthJsonFetch();
  const notification = useNotification();
  const navigate = useNavigate();

  const idIsValid = companyId && !isNaN(parseInt(companyId)) && parseInt(companyId) > 0;

  function handleErrorNotification(message?: string) {
    notification.openNotification({
      type: "error", vertical: "top", horizontal: "center",
      message: `${message ?? `Failed to load company with ID ${companyId}`}`
    });
  }

  async function loadCompany() {
    try {
      setCompanyLoading(true);
      if (!idIsValid) {
        setCompanyLoading(false);
        return
      }
      const response = await authJsonFetch({
        path: `companies/${companyId}`
      });
      if (!response?.status || response.status > 399 || !response?.data) {
        return handleErrorNotification(response?.error);
      }
      setCompany(response.data as CompanyResponsePrivateDto);
    } catch (e) {
      setCompany(undefined);
      handleErrorNotification();
    } finally {
      setCompanyLoading(false);
    }
  }

  useEffect(() => {
    if (!permissionsLoading && companyPermissions?.length > 0) {
      loadCompany().then();
    } else {
      setCompanyLoading(false);
    }
  }, [permissionsLoading, companyPermissions]);

  if (!companyLoading && company && !companyPermissions?.length) {
    handleErrorNotification("Access Denied: Insufficient privileges");
    return navigate("/companies/");
  }

  if (permissionsLoading || companyLoading) {
    return <LoadingSpinner/>;
  } else if (!company) {
    return <NotFound text={"The requested company was not found."}/>;
  }
  return (
    <div>
      <h1>{company.name}</h1>
      <p>{company.description}</p>
      <p>Permissions: {companyPermissions?.length ? companyPermissions.join(", ") : "None"}</p>
    </div>
  )
}

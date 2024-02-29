import {useNavigate, useParams} from "react-router-dom";
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
  const [companyErrorStatus, setCompanyError] = useState<string | undefined>(undefined);
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
        setCompanyError("The provided company ID is invalid");
        setCompanyLoading(false);
        return
      }
      const response = await authJsonFetch({
        path: `companies/${companyId}`
      });
      if (!response?.status || response.status > 404 || !response?.data) {
        setCompanyError(response?.error ?? `Failed to load company with ID ${companyId}`);
        return handleErrorNotification(response?.error);
      }
      setCompany(response.data as CompanyResponsePrivateDto);
    } catch (e) {
      setCompany(undefined);
      setCompanyError(`Failed to load company with ID ${companyId}`);
      handleErrorNotification();
    } finally {
      setCompanyLoading(false);
    }
  }

  useEffect(() => {
    loadCompany().then();
  }, []);


  if (permissionsLoading || companyLoading) {
    return <LoadingSpinner/>;
  } else if (!companyPermissions?.length || !company) {
    handleErrorNotification(companyErrorStatus ?? "Access Denied: Insufficient permissions");
    return navigate("/companies", {replace: true});
  }
  return (
    <div>
      <h1>{company.name}</h1>
      <p>{company.description}</p>
      <p>Permissions: {companyPermissions?.length ? companyPermissions.join(", ") : "None"}</p>
    </div>
  )
}

import {useParams} from "react-router-dom";
import NotFound from "../../../public/pages/errorPages/NotFound.tsx";
import {useEffect, useState} from "react";
import {useAuthJsonFetch} from "../../../common/api/service/apiService.ts";
import {CompanyResponsePrivateDto} from "../../dto/CompanyResponsePrivateDto.ts";
import {
  useNotification
} from "../../../common/notification/context/NotificationProvider.tsx";
import LoadingSpinner from "../../../common/utils/components/LoadingSpinner.tsx";
import {
  PermissionType
} from "../../../authentication/dto/applicationUser/PermissionType.ts";
import usePermission from "../../../authentication/hooks/usePermission.ts";

export default function CompanyDashboard() {
  const companyId = useParams()?.companyId;
  const [loading, setLoading] = useState(true);
  const [company, setCompany] = useState<CompanyResponsePrivateDto | undefined>(undefined);
  const authJsonFetch = useAuthJsonFetch();
  const notification = useNotification();
  const permission = usePermission();

  function handleErrorNotification(message?: string) {
    notification.openNotification({
      type: "error", vertical: "top", horizontal: "center",
      message: `${message ?? `Failed to load company with ID ${companyId}`}`
    });
  }

  useEffect(() => {
    async function loadCompany() {
      const response = await authJsonFetch({
        path: `companies/${companyId}`
      });
      if (!response?.status || response.status > 399 || !response?.data) {
        return handleErrorNotification(response?.error);
      }
      console.log(company);
      setCompany(response.data as CompanyResponsePrivateDto);
    }

    if (!companyId || isNaN(parseInt(companyId)) || parseInt(companyId) < 1) {
      setLoading(false);
    } else {
      loadCompany().catch(() => {
        handleErrorNotification();
      }).finally(() => {
        setLoading(false);
      });
    }
  }, []);

  if (loading) {
    return <LoadingSpinner/>;
  } else if (!company) {
    return <NotFound text={"The requested company was not found."}/>;
  } else if (permission.companyPermissions?.includes(PermissionType.COMPANY_ADMIN)) {
    return <>Admin</>
  } else if (permission.companyPermissions.includes(PermissionType.COMPANY_EDITOR)) {
    return <>Editor</>
  } else if (permission.companyPermissions.includes(PermissionType.COMPANY_EMPLOYEE)) {
    return <>Employee</>
  }
}

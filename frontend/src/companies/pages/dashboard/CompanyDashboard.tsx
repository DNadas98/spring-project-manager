import {useNavigate, useParams} from "react-router-dom";
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

export default function CompanyDashboard() {
  const companyId = useParams()?.companyId;
  const [loading, setLoading] = useState(true);
  const [company, setCompany] = useState<CompanyResponsePrivateDto | undefined>(undefined);
  const [permissions, setPermissions] = useState<PermissionType[]>([]);
  const authJsonFetch = useAuthJsonFetch();
  const notification = useNotification();
  const navigate = useNavigate();

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
      setCompany(response.data as CompanyResponsePrivateDto);
    }

    async function loadCompanyPermissions() {
      const response = await authJsonFetch({
        path: `user/permissions/companies/${companyId}`
      });
      if (!response?.status || response.status > 399 || !response?.data) {
        return handleErrorNotification();
      }
      if (!response?.data?.length) {
        handleErrorNotification(response?.error ?? "Access Denied");
        return navigate(-1);
      }

      setPermissions(response.data as PermissionType[]);
    }

    if (!companyId || isNaN(parseInt(companyId)) || parseInt(companyId) < 1) {
      setLoading(false);
    } else {
      loadCompanyPermissions().then(() => {
        loadCompany().finally(()=>{
          setLoading(false);
        });
      }).catch(() => {
        notification.openNotification({
          type: "error", vertical: "top", horizontal: "center",
          message: "An unknown error has occurred. Please try again later."
        })
      });
    }
  }, []);

  if (loading) {
    return <LoadingSpinner/>;
  } else if (!company) {
    return <NotFound text={"The requested company was not found."}/>;
  } else if (permissions?.includes(PermissionType.COMPANY_ADMIN)) {
    return <>Admin</>
  } else if (permissions?.includes(PermissionType.COMPANY_EDITOR)) {
    return <>Editor</>
  } else {
    return <>Employee</>
  }
}

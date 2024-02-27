import LoadingSpinner from "../../../../common/utils/components/LoadingSpinner.tsx";
import {Card, CardContent, Typography} from "@mui/material";
import {CompanyResponsePublicDto} from "../../../dto/CompanyResponsePublicDto.ts";
import CompanyListItem from "./CompanyListItem.tsx";

interface CompanyListProps {
  loading: boolean;
  companies: CompanyResponsePublicDto[];
  notFoundText: string;
  userIsMember: boolean;
}

export default function CompanyList(props: CompanyListProps) {
  return props.loading
    ? <LoadingSpinner/>
    : props.companies?.length > 0
      ? props.companies.map((company) => {
        return <CompanyListItem key={company.companyId} company={company}
                                userIsMember={props.userIsMember}/>
      })
      : <Card>
        <CardContent>
          <Typography>
            {props.notFoundText}
          </Typography>
        </CardContent>
      </Card>


}

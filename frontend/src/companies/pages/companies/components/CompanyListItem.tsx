import {
  Accordion,
  AccordionActions,
  AccordionDetails,
  AccordionSummary,
  Button,
  Card,
  Typography
} from "@mui/material";
import {CompanyResponsePublicDto} from "../../../dto/CompanyResponsePublicDto.ts";
import ExpandIcon from "../../../../common/utils/components/ExpandIcon.tsx";
import {useNavigate} from "react-router-dom";

interface CompanyListItemProps {
  company: CompanyResponsePublicDto;
  userIsMember: boolean;
}

export default function CompanyListItem(props: CompanyListItemProps) {
  const navigate = useNavigate();
  return (
    <Card>
      <Accordion defaultExpanded={false}
                 variant={"elevation"}
                 sx={{paddingTop: 0.5, paddingBottom: 0.5}}>
        <AccordionSummary expandIcon={<ExpandIcon/>}>
          <Typography variant={"h6"} sx={{
            wordBreak: "break-word",
            paddingRight: 1
          }}>
            {props.company.name}
          </Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography variant={"body2"}>
            {props.company.description}
          </Typography>
        </AccordionDetails>
        <AccordionActions>
          {props.userIsMember
            ? <Button sx={{textTransform: "none"}}
                      onClick={() => {
                        navigate(`/companies/${props.company.companyId}`);
                      }}>
              View Dashboard
            </Button>
            : <Button>Request to join</Button>
          }
        </AccordionActions>
      </Accordion>
    </Card>
  )
}

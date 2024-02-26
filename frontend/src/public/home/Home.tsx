import {useEffect, useState} from "react";
//import {Link} from "react-router-dom";
import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Collapse,
  Grid,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Typography,
  useTheme
} from "@mui/material";
import siteConfig from "../../common/config/siteConfig.ts";
import {
  AccountCircleOutlined,
  AddchartOutlined,
  DomainAddOutlined
} from "@mui/icons-material";
import {Link as RouterLink} from "react-router-dom";

const Home = () => {
  const {siteName} = siteConfig;
  const [checked, setChecked] = useState(false);
  const theme = useTheme();

  useEffect(() => {
    setChecked(true);
  }, []);

  return (
    <Grid container justifyContent={"center"}>
      <Grid item xs={10} sm={8} md={7} lg={6}>
        <Card sx={{paddingTop: 4}}>
          <CardMedia
            component="img"
            alt="Image"
            height="140"
            image="/logo.png"
            sx={{
              objectFit: "contain",
              filter:
                `drop-shadow(0 0 0.3em ${theme?.palette?.primary?.main})`
            }}
          />
          <CardContent sx={{justifyContent: "center", textAlign: "center"}}>
            <Typography variant="h4" gutterBottom>
              {siteName}
            </Typography>
            <Typography variant="h5">
              Got something to work on?
            </Typography>
            <Typography variant="h5" gutterBottom>
              Get something to work for!
            </Typography>
            <Grid container justifyContent={"center"}>
              <Grid item sx={{maxWidth: "22rem"}}>
                <Collapse in={checked} {...(checked ? {timeout: 1000} : {})}>
                  <List>
                    <ListItem>
                      <ListItemIcon>
                        <AccountCircleOutlined color={"secondary"}/>
                      </ListItemIcon>
                      <ListItemText>
                        Create an account, or sign in with Google, Facebook or GitHub
                      </ListItemText>
                    </ListItem>
                    <ListItem>
                      <ListItemIcon>
                        <DomainAddOutlined color={"secondary"}/>
                      </ListItemIcon>
                      <ListItemText>
                        Add your company or request to join an existing one.
                      </ListItemText>
                    </ListItem>
                    <ListItem>
                      <ListItemIcon>
                        <AddchartOutlined color={"secondary"}/>
                      </ListItemIcon>
                      <ListItemText>
                        Create and manage projects and tasks in a company
                      </ListItemText>
                    </ListItem>
                  </List>
                </Collapse>
              </Grid>
            </Grid>
            <CardActions sx={{justifyContent: "center"}}>
              <Grid spacing={1} container justifyContent={"center"}>
                <Grid item>
                  <Button component={RouterLink}
                          to={"/login"}
                          variant={"contained"}>
                    Sign in
                  </Button>
                </Grid>
                <Grid item>
                  <Button component={RouterLink}
                          to={"/register"}
                          variant={"contained"}>
                    Sign up
                  </Button>
                </Grid>
              </Grid>
            </CardActions>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );
};

export default Home;

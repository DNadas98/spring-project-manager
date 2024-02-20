import {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {
  Button,
  Collapse,
  Container,
  Grid,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Typography
} from '@mui/material';
import {styled} from '@mui/system';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import FollowTheSignsIcon from '@mui/icons-material/FollowTheSigns';
import HardwareIcon from '@mui/icons-material/Hardware';

const StyledTypographyMain = styled(Typography)(({theme}) => ({
  fontWeight: 'bold',
  fontSize: '4rem',
  color: '#FFFFFF',
  fontFamily: 'Arial, sans-serif',
  marginBottom: theme.spacing(2),
}));

const StyledTypographySub = styled(Typography)(({theme}) => ({
  fontSize: '2rem',
  color: '#FFFFFF',
  marginBottom: theme.spacing(3),
}));

const StyledTypographySteps = styled(Typography)(({theme}) => ({
  fontSize: '1.5rem',
  marginBottom: theme.spacing(3),
}));

const StyledButton = styled(Button)(({theme}) => ({
  marginRight: theme.spacing(2),
  color: 'inherit',
}));

const StyledDiv = styled('div')(({theme}) => ({
  background: 'linear-gradient(45deg, #516d79 30%, #11508d 90%)',
  color: '#FFFFFF',
  accentColor: '#FFFFFF',
  padding: theme.spacing(4),
  borderRadius: theme.spacing(2)
}));

const Home = () => {
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    setChecked(true);
  }, []);

  return (
    <Container>
      <Grid container spacing={4} justifyContent="center">
        <Grid item xs={12}>
          <StyledDiv>
            <StyledTypographyMain variant="h1">
              Project Manager
            </StyledTypographyMain>
            <StyledTypographySub variant="h3">
              <HardwareIcon/> Got something to work ON?
            </StyledTypographySub>
            <StyledTypographySub variant="h3">
              <FollowTheSignsIcon/> Get something to work FOR!
            </StyledTypographySub>
            <Collapse in={checked} {...(checked ? {timeout: 1000} : {})}>
              <StyledTypographySteps variant="h3">
                <List>
                  <ListItem>
                    <ListItemIcon>
                      <ArrowForwardIosIcon color={"secondary"}/>
                    </ListItemIcon>
                    <ListItemText>
                      Step 1: Create an account or sign in with Google, Facebook or GitHub
                    </ListItemText>
                  </ListItem>
                  <ListItem>
                    <ListItemIcon>
                      <ArrowForwardIosIcon color={"secondary"}/>
                    </ListItemIcon>
                    <ListItemText>
                      Step 2: Add your company or request to join an existing one.
                    </ListItemText>
                  </ListItem>
                  <ListItem>
                    <ListItemIcon>
                      <ArrowForwardIosIcon color={"secondary"}/>
                    </ListItemIcon>
                    <ListItemText>
                      Step 3: Create and manage the projects and tasks of the company
                    </ListItemText>
                  </ListItem>
                </List>
              </StyledTypographySteps>
            </Collapse>
            <StyledTypographySub variant="body1">
              Already a member?{' '}
              <Link to="/login">
                <StyledButton variant="contained" color="primary">
                Sign in here!
                </StyledButton>
              </Link>
              <br/>
              Don't have an account?{" "}
              <Link to="/register">
                <StyledButton variant="contained" color="primary">
                  Sign up to get started!
              </StyledButton>
              </Link>
            </StyledTypographySub>
          </StyledDiv>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Home;
